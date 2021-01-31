import AbstractComponent from '@components/AbstractComponent';
import AppBackground from '@components/AppBackground';
import React from 'react';
import { ManualPages } from '@components/Manual';
import EventEmitterService, { EventTypes } from '@services/EventEmitterService';
import { Bind } from '../ioc/ServiceContainer';
import { View } from 'react-native';
import StorageService, { StorageKeys, Storage } from '@services/StorageService';
import NavigationService from '@services/NavigationService';

interface Props {
    navigation: any;
}
interface State {
    height: any;
    width: any;
}
export default class BlankPage extends AbstractComponent<Props, State> {

    private eventEmitterService: EventEmitterService = Bind('eventEmitterService');
    private storageService: StorageService = Bind('storageService');
    private navigationService:NavigationService = Bind('navigationService');

    componentDidMount() {
        this.navigationService.setNavigation(this.props.navigation);
        this.storageService.get(Storage.USER_ID).then(userId => {
            this.storageService.getObject(Storage.MANUAL).then(object => {
                let already = object[userId] && object[userId][ManualPages.MENU] == '1';
                if (!already) {
                    let extras = { close: this.onClose };
                    this.eventEmitterService.emit({ type: EventTypes.Manual, data: ManualPages.MENU, extras });
                } else {
                    this.onClose();
                }
            });
        });
    }

    onClose = () => {
        this.navigationService.reset('MenuPage');
        this.storageService.set(Storage.USER_READY,true);
    }

    render() {
        return (
            <AppBackground
                navigation={this.props.navigation}
                hideBack={true}
                hideChat={true}
                doNaviagte={true}>
                <View style={{ flex: 1 }}></View>
            </AppBackground>

        );
    }
}