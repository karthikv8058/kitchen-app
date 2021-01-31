import AbstractComponent from '@components/AbstractComponent';
import AppBackground from '@components/AppBackground';
import User from '@models/User';
import colors from '@theme/colors';
import t from '@translate';
import React from 'react';
import {
  FlatList,
  StyleSheet,
  Text,
  View,
  Alert,
} from 'react-native';
import { Bind } from '../ioc/ServiceContainer';
import UserService from '@services/UserService';
import StorageService, { Storage } from '@services/StorageService';

import NavigationService from '@services/NavigationService';
import LoaderWithText from '@components/LoaderWithText';
const Logout = 'Logout';
const cancel = 'cancel';

interface State {
  users: User[];
  showLoader: boolean;
}

interface Props {
  navigation: any;
  ip: string;
  setIP: any;
}

class LoginUserListPage extends AbstractComponent<Props, State> {

  state = {
    users: [],
    showLoader: true
  };

  private storageService: StorageService = Bind('storageService');
  private userService: UserService = Bind('userService');

  private navigationService: NavigationService = Bind('navigationService');

  constructor(props: Props) {
    super(props);
  }

  componentDidMount() {
    this.loadUsers();
  }

  loadUsers() {
    this.userService.getUsersAndStations().then(users => {
      this.setState({
        users: users,
        showLoader: false
      });
    });
  }

  showLoginWithPasswordPage = (user: User) => {
    user.password = '';
    this.navigationService.push('LoginWithPasswordPage', { user: user });
  }

  renderUser = (userListItem: any) => {
    let user: User = userListItem.item;
    return (
      <Text
        style={styles.user}
        onPress={this.showLoginWithPasswordPage.bind(this, user)}>{user.name} </Text>
    );
  }
  shutdownAndChangeServer = () => {
    let serverIP = this.storageService.getFast(Storage.SERVER_IP);
    let deviceIp = this.storageService.getFast(Storage.DEVICE_IP);
    if (serverIP === deviceIp) {
      this.showLogoutAlert();
    } else {
      this.navigationService.reset('DetectedRestuarants');
    }
  }

  showLogoutAlert() {
    Alert.alert(Logout, t('app-background.logout'),
      [{
        text: t('app-background.cancel'), onPress: () => { }, style: cancel
      },
      {
        text: t('app-background.yes'), onPress: () => {
          this.logoutServer();
        }
      }],
      { cancelable: false });
  }

  logoutServer() {
    this.navigationService.reset('ShutdownPage');
  }

  render() {
    return (
      <AppBackground
        navigation={this.props.navigation}
        doNaviagte={true}
        onGoBack={this.shutdownAndChangeServer}
        hideChat={true}>
        <View style={styles.container}>
          {this.state.showLoader ?
            <LoaderWithText text='Loading users' />
            :
            <FlatList
              style={styles.listStyle}
              data={this.state.users}
              renderItem={this.renderUser}
            />}
        </View>
      </AppBackground>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center',
  },
  listStyle: {
    marginLeft: 10,
    paddingRight: 10,
    margin: 10,
    fontWeight: 'bold',
    minWidth: 300
  },
  user: {
    padding: 10,
    borderRadius: 10,
    marginTop: 8,
    backgroundColor: 'rgba(0,0,0,0.3)',
    borderWidth: 2,
    borderColor: '#D5D5D5',
    color: colors.white,
    height: 40,
  }
});

export default LoginUserListPage;
