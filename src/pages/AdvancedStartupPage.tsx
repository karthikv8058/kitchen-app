import ApiBuilder from '@api/routes';
import AppBackground from '@components/AppBackground';
import colors from '@theme/colors';
import t from '@translate';
import React, { Component } from 'react';
import {
    ActivityIndicator,
    StyleSheet,
    Text,
    TextInput,
    TouchableOpacity,
    View,
} from 'react-native';
import CheckBox from 'react-native-check-box';
import { connect } from 'react-redux';
import ioc, { Bind } from '../ioc/ServiceContainer';
import { setIP } from '../redux/AppActions';
import StorageService, { Storage } from '@services/StorageService';
import NavigationService from '@services/NavigationService';
import RestrauntService from '@services/RestrauntService';

interface Props {
    navigation: any;
    setIP: any;
}

interface State {
    id?: number;
    radioSelected?: boolean;
    ip: string;
    ipDiscoveryOptions: IpDiscoveryOption[];
    remindSettings: boolean;
    isLoading: boolean;

    isIpValid: boolean;
}

interface IpDiscoveryOption {
    label: string;
    value: string;
    color: string;
    size: number;
    selected: boolean;
}

const REGEX_IP = /((^\s*((([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))\s*$)|(^\s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:)))(%.+)?\s*$))/;

class AdvancedStartupPage extends Component<Props, State> {

    state: State = {
        ip: '',
        ipDiscoveryOptions: [],
        remindSettings: false,
        isLoading: false,
        isIpValid: false
    };

    private storageService: StorageService = ioc.ServiceFactory.getServiceBy('storageService');

    private navigationService: NavigationService = Bind('navigationService');
    private restrauntService: RestrauntService = Bind('restrauntService');

    constructor(props: Props) {
        super(props);
    }

    componentDidMount() {
        //update ip from storage (if youser chooswe remind ip)
        this.storageService.get(Storage.ADVANCED_CONNECTION_IP).then(ip => {
            if (ip) {
                this.setState({
                    ip,
                    remindSettings: true,
                    isIpValid: true
                })
            }
        })
    }


    showLogin = () => {
        const ip = this.state.ip;
        console.log("-------------------------",ip);
        this.restrauntService.checkRestrauntServer(ip).then(isRestaurantAvailable => {
            console.log("------------------------->>",isRestaurantAvailable);
            if (isRestaurantAvailable) {
                if (this.state.remindSettings) {
                    this.storageService.set(Storage.ADVANCED_CONNECTION_IP, ip);
                } else {
                    this.storageService.set(Storage.ADVANCED_CONNECTION_IP, '');
                }
                this.storageService.set(Storage.SERVER_IP, ip);
                Bind('apiBuilder').setIP(ip);
                this.props.setIP(ip);
                this.navigationService.push('LoginUserListPage');
            }
        })
        .catch(e=>
            {
                console.log(e)
            });
    }


    toggleReminder = () => {
        this.setState({
            remindSettings: !this.state.remindSettings
        });
    }

    updateIP = (ip: string) => {
        //validate
        const isIpValid = REGEX_IP.test(ip) ? true : false;
        this.setState({ ip, isIpValid });
    }


    render() {
        return (
            <AppBackground
                navigation={this.props.navigation}
                doNaviagte={true}
                hideChat={true}
                checkBackgroundColor={true}>
                <View style={styles.container}>
                    <Text style={{ color: colors.white, alignSelf: 'stretch', marginLeft: 20, fontSize: 16, }}>Advanced Connection</Text>
                    <View style={styles.innerContainer}>
                        <View style={styles.ipContainer}>
                            <Text style={styles.textStyle}>Server IP</Text>
                            <TextInput
                                style={styles.inputIP}
                                onChangeText={this.updateIP}
                                editable={true}
                                multiline={false}
                                value={this.state.ip}
                            />
                        </View>
                        <TouchableOpacity
                            style={styles.checkboxContainer}
                            onPress={this.toggleReminder}>
                            <CheckBox
                                onClick={this.toggleReminder}
                                isChecked={this.state.remindSettings}
                                checkBoxColor={'white'}
                                rightTextStyle={'white'}
                            />
                            <Text style={[styles.textStyle, { marginLeft: 5 }]}>
                                {t('server-settingslogin.remember-connection-settings')}</Text>
                        </TouchableOpacity>
                        <View style={styles.buttonContainer}>
                            {this.state.isIpValid && (this.state.isLoading ?
                                <ActivityIndicator size='large' color={colors.white} /> :
                                <TouchableOpacity
                                    onPress={this.showLogin}
                                    style={styles.buttonConnect}>
                                    <Text style={styles.textStyle}>{"Connect"}</Text>
                                </TouchableOpacity>)}
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
        alignItems: 'center',
        backgroundColor: colors.black,
    },
    innerContainer: {
        marginLeft: 20,
        marginRight: 20,
        marginBottom: 10,
        marginTop: 10,
        alignSelf: 'stretch',
        paddingLeft: 10,
        borderRadius: 12,
        paddingBottom: 20,
        backgroundColor: colors.gradientStart,
    },
    textStyle: {
        fontSize: 16,
        color: 'white',
    },
    ipContainer: {
        alignItems: 'center',
        flexDirection: 'row',
        marginLeft: 20,
        marginRight: 30,
        marginTop: 35
    },
    checkboxContainer: {
        justifyContent: 'flex-start',
        alignItems: 'flex-start',
        flexDirection: 'row',
        marginTop: 30,
        marginLeft: 20
    },
    buttonConnect: {
        borderColor: '#d6d7da',
        borderWidth: 1,
        paddingTop: 5,
        paddingBottom: 5,
        paddingLeft: 60,
        paddingRight: 60,
        borderRadius: 4,
        flexWrap: 'wrap',
        fontSize: 16,
        color: 'white',
    },
    buttonContainer: {
        alignContent: 'flex-end',
        justifyContent: 'flex-end',
        alignItems: 'flex-end',
        paddingRight: 10,
        marginTop: 10,
        marginRight: 20
    },
    inputIP: {
        backgroundColor: 'white',
        alignSelf: 'stretch',
        // width: 300,
        flex: 1,
        marginLeft: 40,
        marginTop: 3,
        marginBottom: 3,
        height: 38
    }
});

export default connect(null, { setIP })(AdvancedStartupPage);
