import ApiBuilder from '@api/routes';
import AppBackground from '@components/AppBackground';
import colors from '@theme/colors';
import t from '@translate';
import React, { Component } from 'react';
import {
  ActivityIndicator,
  AsyncStorage,
  StyleSheet,
  Text,
  TextInput,
  ToastAndroid,
  TouchableOpacity,
  View,
} from 'react-native';
import ClientServer from '@native/ClientServer';
import { Bind } from '../ioc/ServiceContainer';
import StorageService, { Storage } from '@services/StorageService';
import User from '@models/User';
import UserService from '@services/UserService';
import LoaderWithText from '@components/LoaderWithText';
import PasswordInput from '@components/PasswordInput';

import NavigationService from '@services/NavigationService';
import PermissionService from '@services/PermissionService';

interface Props {
  navigation: any;
}

class LoginWithPasswordPage extends Component<Props> {

  state = {
    hidePassword: true,
    name: '',
    password: '',
    isLoginStarted: false,

    showLoader: false,
    error: ''
  };

  private storageService: StorageService = Bind('storageService');
  private userService: UserService = Bind('userService');
  private clientServer: ClientServer = new ClientServer();
  private permissionService: PermissionService = Bind('permissionService');;
  private apiBuilder: ApiBuilder = Bind('apiBuilder');
  private navigationService: NavigationService = Bind('navigationService');
  private user: User = {};
  private deviceIp = '';
  constructor(props: Props) {
    super(props);
  }

  componentDidMount() {
    this.user = this.props.route.params.user;

    this.deviceIp = this.storageService.getFast('deviceIp');
    if (!this.deviceIp) {
      this.clientServer.getIPAddress().then(ip => {
        this.storageService.set('deviceIp', ip);
        this.deviceIp = ip;
      });
    }

    if (this.user.password) {
      this.login();
    } else {
      this.setState({ isLoginStarted: false });
    }
  }

  login = () => {

    if (!this.user.password) {
      this.setState({ error: t('passwordScreen.password-validation') });
      return;
    }

    this.setState({ showLoader: true, error: '' });

    this.userService.login(this.user, this.deviceIp).then((response: any) => {

      let error = '';
      if (response && response.authToken) {

        this.storageService.set(Storage.AUTH_TOKEN, response.authToken);
        this.storageService.set(Storage.CHEF_NAME, response.name);
        this.storageService.set(Storage.USER_ID, response.userid);
        this.storageService.set(Storage.LIKELY_HOOD_LIMIT, response.LikelyHoodLimit);
        AsyncStorage.setItem('userRights', response.UserRights.toString());
        let permissions : string[] = [];
        for(let right of JSON.parse(response.UserRights) ){
          permissions.push(right.right);
        }
        //this.storageService.set(Storage.ROLES,permissions.toString);
        this.permissionService.setPermissions(permissions);
        const settings = response.settings;
        if (settings) {
          for (let key of Object.keys(settings)) {
            this.storageService.set(key, settings[key]);
          }
        }
        this.navigationService.reset('StationList', {
          routeName: 'StationList',
          params: { navigationPath: false, userId: response.userid, backNavigation: true }
        });

      } else {
        error = t('passwordScreen.login-validation');
      }
      this.setState({ showLoader: false, isLoginStarted: false, error });
    });
  }
  updateUser = (password: string) => {
    if (this.user) {
      this.user.password = password;
    }
    this.setState({ password });
  }

  render() {
    return (
      <AppBackground
        navigation={this.props.navigation}
        hideBack={this.state.isLoginStarted}
        hideChat={true}
        doNaviagte={true}>
        <View style={styles.container}>
          {this.state.showLoader ? <LoaderWithText text='Please wait...' /> :
            <View
              style={styles.wrapLogin} >
              <Text style={styles.textUserName}>{this.user?.name }</Text>
              <PasswordInput
                placeholder='Password'
                onChangeText={this.updateUser} />

              <View style={{ flexDirection: 'row' }} >
                {this.state.error ?
                <Text style={{ color: '#f00', marginLeft: 8, marginTop: 4, alignSelf: 'center' }}>
                  {this.state.error}</Text> : null}
                <View style={{ flex: 1 }} />
                <TouchableOpacity
                  style={styles.loginButton}
                  onPress={this.login}>
                  <Text style={{ color: colors.LoginText }}>Login</Text>
                </TouchableOpacity>
              </View>
            </View>}
        </View>
      </AppBackground>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  wrapLogin: {
    elevation: 20,
    padding: 20,
    marginHorizontal: 20,
    borderRadius: 10,
    backgroundColor: colors.liteGray,
    flexDirection: 'column',
    alignSelf: 'stretch'
  },
  textUserName: {
    marginTop: 20,
    marginLeft: 15,
    color: colors.black
  },
  loginButton: {
    marginTop: 20,
    height: 40,
    width: 100,
    marginRight: 20,
    borderRadius: 10,
    backgroundColor: 'white',
    alignItems: 'center',
    alignSelf: 'flex-end',
    justifyContent: 'center'
  }
});

export default LoginWithPasswordPage;
