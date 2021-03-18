import AbstractComponent from '@components/AbstractComponent';
import AppBackground from '@components/AppBackground';
import React from 'react';
import { WebUrl } from '@native/ClientServer';
import { WebView } from 'react-native-webview';
import { ActivityIndicator, Alert, AsyncStorage, TouchableOpacity, View } from 'react-native';
import { responseChecker } from '../utils/responseChecker';
import ioc, { HTTP_CLIENT, Bind } from '../ioc/ServiceContainer';
import colors from '@theme/colors';
import UserService from '@services/UserService';
import Icons from 'react-native-vector-icons/Entypo';
import NavigationService from '@services/NavigationService';

interface Props {
    navigation: any;
}
interface State {
    webAppUrl: String;
    isLoading: Boolean;
    webAppData: any;
}
export default class SmarttoniWeb extends AbstractComponent<Props, State> {
    private userService: UserService = Bind('userService');
    private pageRefreshInterval: number | undefined;
    private tooblarItems: any[] = [];
    private navigationService:NavigationService = Bind('navigationService');

    private url = '';

    constructor(props: Props) {
        super(props);
        this.state = {
            webAppUrl: '',
            isLoading: true,
            webAppData: [],
        };
    }

    componentDidMount() {
        this.navigationService.setNavigation(this.props.navigation);

        this.getWebAppCredentials();
        this.loadToolbarItems();
    }
    loadToolbarItems = () => {
            this.tooblarItems.push(
                <View>
                   {this.props.route ?.params ?.goToProfile ? <TouchableOpacity
                        onPress={() => this.goToOreintationSelection()}
                        activeOpacity={0.8}
                        style={{ height: 40, width: 40, marginBottom: 15 }}>
                        <Icons style={{ marginTop: 10 }} name='swap' size={35} color={colors.white} />
                    </TouchableOpacity>:null}
                </View>
            );
        
    }
    goToOreintationSelection=()=>{
        this.props.navigation.navigate('OrientationSelectionPage', {});
    }
    componentWillUnmount() {
        this.initiateSync();
    }

    getWebAppCredentials() {
        this.userService.webCredentials().then(response => {
            if (responseChecker(response, this.props.navigation)) {
                this.url = WebUrl + 'webview/' + (this.props.route ?.params ?.goToProfile ?
                    'profile' : 'units/' +  response.webCredentialData.resturantId);
                this.setState({
                    webAppData: response.webCredentialData,
                    isLoading: false,
                });
            }
        }).catch(error => {
            this.setState({
                webAppData: [],
                isLoading: false,
            });
        });
    }
    goToMenuPage=()=>{
        this.navigationService.reset('MenuPage');
    }
    initiateSync() {
        if (this.apiBuilder.paths) {
            this.userService.requestSync().then(response => {
            }).catch(error => { });
        }
    }

    render() {

        const applicationData =  this.state.webAppData ? `(function(){
            let tk = window.localStorage.getItem('ACCESS_TOKEN');
            if(!tk || (tk && tk != '${this.state.webAppData.accessToken}')){
              window.localStorage.setItem('ACCESS_TOKEN', '${this.state.webAppData.accessToken}');
              window.localStorage.setItem('PROFILEUUID', '${this.state.webAppData.profileUuid}');
              window.localStorage.setItem('PROFILE', '${this.state.webAppData.details}');
              window.localStorage.setItem('WEBVIEW', '${true}');
              window.location.reload();
            }
      })();` : '';
        return (
            <AppBackground
                navigation={this.props.navigation}
                onGoBack={this.goToMenuPage}
                toolbarMenu={this.tooblarItems}>
                {this.state.isLoading ? <ActivityIndicator style={{ flex: 1 }} size='large'
                    color={colors.white} /> :
                    <WebView
                        style={{ backgroundColor: '#ffffff00' }}
                        source={{
                            uri: this.url, method: 'POST'
                        }}
                        injectedJavaScript={applicationData}
                    />}
            </AppBackground>
        );
    }
}
