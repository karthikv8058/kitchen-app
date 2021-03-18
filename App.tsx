/**
* SmartToni React Native App
*/
// if (__DEV__) {
//   import('./ReactotronConfig')
//   }
import AbstractComponent from '@components/AbstractComponent';
import ClientServer from '@native/ClientServer';
import VoiceRecognition from '@native/VoiceRecognition';
import HttpClient from '@services/HttpClient';
import EventEmitterService, { EventTypes } from '@services/EventEmitterService';
import StorageService, { Storage, UserSettings } from '@services/StorageService';
import React from 'react';
import { PermissionsAndroid, StatusBar, Text } from 'react-native';
import Orientation from 'react-native-orientation-locker';
import { NavigationActions, StackActions } from 'react-navigation';
import { Provider } from 'react-redux';

import AclNavigation from './src/acl/AclNavigation';
import ioc, { Bind } from './src/ioc/ServiceContainer';
import store from './src/redux/store';
import { INTERVENTION_TASK_TYPE, TYPE_SERVER_LIST, PUSH_USER_SETTINS } from './src/utils/constants';
import { HTTP_CLIENT } from './src/ioc/ServiceContainer';
import { byteArrayToString } from './src/utils/commonUtil';
import Manual from '@components/Manual';
import NavigationService from '@services/NavigationService';
import AppNavigation from '@pages/AppNavigation';
import AsyncStorage from '@react-native-community/async-storage';

console.disableYellowBox = true;
interface Props {
  navigation: any;
}
interface State {
  isLogout: boolean;
}

export default class App extends AbstractComponent<Props, State> {

  private voiceRecognition: VoiceRecognition = new VoiceRecognition();
  private clientServer: ClientServer = new ClientServer();
  private eventEmitterService: EventEmitterService = ioc.ServiceFactory.getServiceBy('eventEmitterService');
  private httpClient: HttpClient = ioc.ServiceFactory.getServiceBy(HTTP_CLIENT);
  private storageService: StorageService = Bind('storageService');
  private navigationService: NavigationService = Bind('navigationService');


  constructor(props: Props) {
    super(props);

    this.navigateToInterventionTaskPage = this.navigateToInterventionTaskPage.bind(this);
    //this.setNavigation = this.setNavigation.bind(this);
    this.goToIntervention = this.goToIntervention.bind(this)
    this.clientServer.getIPAddress().then(ip => {
      this.ip = ip;
    });
  }

  componentDidMount() {

    // TODO quick fix
    this.storageService.get(Storage.ORIENTATION).then(value => {
      let type = +value;
      switch (type) {
        case 2: Orientation.lockToPortrait(); break;
        case 3: Orientation.lockToLandscape(); break;
        case 1:
        default: Orientation.unlockAllOrientations();
      }
    });

    AsyncStorage.getItem(Storage.ORIENTATION)

    StatusBar.setHidden(true);
    this.clientServer.addListener((data => {
      this._handlePush(data);
    }));
    this.storageService.clearFlags();
    let _this = this;
    async function requestPermission() {
      try {
        const granted = await PermissionsAndroid.request(
          PermissionsAndroid.PERMISSIONS.WRITE_EXTERNAL_STORAGE,
        );
        const granted1 = await PermissionsAndroid.request(
          PermissionsAndroid.PERMISSIONS.RECORD_AUDIO,
        );
        if (granted !== PermissionsAndroid.RESULTS.GRANTED) {
          requestPermission();
        }
        if (granted1 === PermissionsAndroid.RESULTS.GRANTED) {
          _this.voiceRecognition.start();
        }
      } catch (err) {
      }
    }
    requestPermission();

  }

  randomPort() {
    return Math.random() * 60536 | 0 + 5000 // 60536-65536
  }



  navigateToInterventionTaskPage() {
    this.aclNavigation.push('InterventionTaskPage')
  }

  _handlePush(data: any) {

    const TYPE_SERVER_LOGOUT = 1;
    const TYPE_UPDATE_QUEUE = 2;
    const TYPE_UPDATE_USER = 3;
    const TYPE_INTERVENTION = 4;
    const TYPE_UDP = 5;
    const TYPE_PRINT_COMPLETE = 6;

    data = JSON.parse(data);

    switch (data.type) {
      case TYPE_UPDATE_QUEUE:
        this.eventEmitterService.emit({ type: EventTypes.SYNC_TASK });
        break;
      case TYPE_SERVER_LOGOUT:
        let navigationService: NavigationService = Bind('navigationService');
        navigationService.reset('DetectedRestuarants');
        break;
      case TYPE_UPDATE_USER:
        let user = data.data;
        for (const us in UserSettings) {
          const key = UserSettings[us];
          this.storageService.set(key, user[key]);
        }
        break;
      case TYPE_INTERVENTION:
        let interventionJobId = data.data;
        let userReady = this.storageService.getFast(Storage.USER_READY)
        if (userReady && interventionJobId) {
          this.goToIntervention(interventionJobId);
        }
        break;
      case TYPE_UDP:
        this.eventEmitterService.emit({ type: EventTypes.SERVER_FOUND, data: data.data });
        break;

      case TYPE_PRINT_COMPLETE:
        this.navigationService.push('PrinterDetailPage', {
          printDetails: data.data
        });
        break;
    }
  }

  goToIntervention(interventionJobId: number) {
    let interventionid = this.storageService.getFast(Storage.CURRENT_INTERVENTION);
    if (!interventionid) {
      const lastIntervention = this.storageService.getFast(Storage.LAST_INTERVENTION);
      if (lastIntervention == interventionJobId) {
        let old = this.storageService.getFast(Storage.LAST_INTERVENTION_TIME);
        old = old ? old : 0;
        let diff = new Date().getTime() - old;
        if (diff < 5000) {
          return;
        }
      }
      this.storageService.set(Storage.LAST_INTERVENTION_TIME, new Date().getTime());
      this.storageService.set(Storage.CURRENT_INTERVENTION, interventionJobId);
      this.storageService.set(Storage.LAST_INTERVENTION, interventionJobId);
      this.navigationService.push('DetailPage', {
        workId: interventionJobId,
        //assignmentid: resp.data.work.id, index: 0,
        //recipeName: resp.data.work.recipe.name, background: '#fff', textColor: '#000',
        // assignedTask: resp.data.work,
        isIntervention: true,
        //interventionTask: resp.data.intervention,
        // interventionData: resp.data,
      });
      //this.isDetailOpening = false;
    }
  }

  componentWillUnmount() {
    // Orientation.lockToLandscapeLeft();
    this.voiceRecognition.stop();
    this.clientServer.removeListener();
  }


  setNavigation(nav: any) {
    // if (nav != null) {
    //   this.navigation = nav;
    //   this.aclNavigation = new AclNavigation(this.navigation._navigation);
    // }
  }

  render() {
    return (
      <Provider store={store}>
        <Manual />
        <AppNavigation />
      </Provider>
    );
  }
}
