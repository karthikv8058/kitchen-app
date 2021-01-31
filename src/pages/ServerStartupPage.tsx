
import React from 'react';
import { connect } from 'react-redux';
import {
    TouchableOpacity,
    View,
    StyleSheet,
    Text,
    TextInput,
} from 'react-native';
import colors from '@theme/colors';
import CheckBox from 'react-native-check-box';
import AppBackground from '@components/AppBackground';
import t from '@translate';
import Orientation from 'react-native-orientation-locker';
import HttpClient from '@services/HttpClient';
import AbstractComponent from '@components/AbstractComponent';
import ClientServer, { ApiBaseUrl } from '@native/ClientServer';
import ioc, { HTTP_CLIENT, Bind } from '../ioc/ServiceContainer';
import User from '@models/User';
import StorageService, { Storage } from '@services/StorageService';
import LoaderWithText from '@components/LoaderWithText';
import PasswordInput from '@components/PasswordInput';

interface Props {
    navigation: any;
}

interface State {
    hidePassword: boolean;
    username: string;
    password: string;
    showLoader: boolean;
    error: string;
    remindUsername: boolean;
}
class ServerStartupPage extends AbstractComponent<Props, State> {

    private webUrl = ApiBaseUrl;
    private httpClient: HttpClient = ioc.ServiceFactory.getServiceBy(HTTP_CLIENT);

    private storageService: StorageService = Bind('storageService');
    constructor(props: Props) {
        super(props);
        this.state = {
            hidePassword: true,
            username: 'n@n.nn',
            password: 'Pass@123',
            error: '',
            showLoader: false,
            remindUsername: false
        };
    }

    componentDidMount() {
        // Orientation.lockToLandscapeLeft();
        this.storageService.get(Storage.REMIND_USERNAME).then(username =>  {
            let remindUsername = username ? true : false;
            this.setState({ username, remindUsername });
        });
        this.setState({
            username: 'n@n.nn',
            password: 'Pass@123'
        });
    }

    toggleSaveUsername = () => {
        this.setState({ remindUsername: !this.state.remindUsername });
    }

    togglePasswordVisibility = () => {
        this.setState({ hidePassword: !this.state.hidePassword });
    }

    showRestuarantList = () => {
        if (!this.state.username) {
            this.setState({ error: 'Username is requred' });
        } else if (!this.state.password) {
            this.setState({ error: 'Password is requred' });
        } else {
            this.setState({ showLoader: true, error: '' });
            let loginRequestBody = { username: this.state.username, password: this.state.password };
            this.httpClient.post(this.webUrl + 'public/user/login', loginRequestBody).then(response => {
                if (response.data) {
                    this.setState({ showLoader: false });
                    this.storageService.set(Storage.REMIND_USERNAME, (this.state.remindUsername ? this.state.username : ''));
                    let user: User = {
                        username: this.state.username,
                        password: this.state.password,
                        token: response.data.access_token,
                        name: response.data.details.first_name + ' ' + response.data.details.last_name
                    };
                    this.props.navigation.navigate('RestrauntList', { user });
                } else {
                    this.setState({ showLoader: false, error: t('connect-to-server-manual.wrong-data') });
                }
            }).catch((error) => {
                this.setState({ showLoader: false, error: 'Error communicating with server' });
            });
        }
    }

    render() {
        let hidePasswordIcon = !this.state.hidePassword ? 'eye' : 'eye-off';
        return (
            <AppBackground
                navigation={this.props.navigation}
                doNaviagte={true}
                hideChat={true}
                checkBackgroundColor={true}>
                <View style={styles.container}>
                    <Text style={styles.textStartServer}>Start Server</Text>
                    <View style={styles.innerContainer}>
                        <View style={styles.usernameContainer}>
                            <TextInput
                                style={styles.inputUsername}
                                onChangeText={(username) => this.setState({ username, error: '' })}
                                editable={true}
                                placeholder={t('start-server.username')}
                                multiline={false}
                                value={this.state.username} />
                            <TouchableOpacity style={styles.checkboxContainer}
                                onPress={this.toggleSaveUsername}>
                                <CheckBox
                                    onClick={this.toggleSaveUsername}
                                    isChecked={this.state.remindUsername}
                                    checkBoxColor={'white'}
                                    rightTextStyle={'white'}
                                />
                                <Text style={styles.textRemind}>{t('start-server.remember-username')}</Text>
                            </TouchableOpacity>
                        </View>
                        <PasswordInput
                            placeholder={t('start-server.password')}
                            onChangeText={(password) => this.setState({ password, error: '' })}
                        />
                        <View style={{ flexDirection: 'row' }} >
                            {this.state.error ?
                            <Text style={{ flex :1,color: '#f00', marginLeft: 8, marginTop: 4, alignSelf: 'center' }}>
                                {this.state.error}</Text> : null}
                            <View style={{ flex: 1 }} />
                            {this.state.showLoader ?
                                <LoaderWithText style={{ marginTop: 4 }} />
                                :
                                <TouchableOpacity style={styles.buttonbackground}
                                    onPress={this.showRestuarantList}>
                                    <Text style={styles.textHeader}>{t('start-server.start-server')}</Text>
                                </TouchableOpacity>
                            }
                        </View>

                    </View>
                </View>
            </AppBackground>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'stretch',
        backgroundColor: colors.black,
        padding: 16
    },
    textStartServer: {
        color: colors.white,
        alignSelf: 'stretch',
        fontSize: 16
    },
    innerContainer: {
        marginTop: 5,
        backgroundColor: colors.gradientStart,
        borderRadius: 12,
        paddingLeft: 20,
        paddingRight: 20,
        alignSelf: 'stretch',
        paddingBottom: 15,
    },
    usernameContainer: {
        marginBottom: 4,
        marginTop: 20,
        marginRight: 20,
        flexDirection: 'column',
    },
    inputUsername: {
        backgroundColor: 'white',
        alignSelf: 'stretch',
        marginLeft: 10,
        marginVertical: 4,
        height: 40,
        borderRadius: 4
    },
    checkboxContainer: {
        flexDirection: 'row',
        marginLeft: 8,
        marginTop: 2,
    },
    passwordInputContainer: {
        height: 40,
        marginRight: 20,
        marginLeft: 8,
        marginTop: 10,
        backgroundColor: colors.white,
        alignItems: 'center',
        alignSelf: 'stretch',
        flexDirection: 'row',
        borderRadius: 4,
    },
    passwordContainer: {
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center',
    },
    textHeader: {
        fontSize: 16,
        color: 'white',
    },
    textRemind: {
        fontSize: 14,
        color: 'white',
        marginLeft: 5
    },
    buttonbackground: {
        alignContent: 'flex-end',
        justifyContent: 'flex-end',
        borderColor: '#fff',
        borderWidth: 1,
        alignSelf: 'flex-end',
        paddingHorizontal: 16,
        paddingVertical: 6,
        marginVertical: 16,
        borderRadius: 4,
        marginRight: 16
    }
});

export default ServerStartupPage;
