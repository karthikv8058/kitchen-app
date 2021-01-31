import React, { Component } from 'react';
import colors from '@theme/colors';
import {
    Text,
    View,
    Modal,
    Image,
    TouchableOpacity,
} from 'react-native';
import Icon from 'react-native-vector-icons/Feather';
import { Bind } from '../ioc/ServiceContainer';
import StorageService, { Storage, StorageKeys } from '@services/StorageService';
import EventEmitterService, { EventTypes, EventListner } from '@services/EventEmitterService';
import NavigationService from '@services/NavigationService';

interface Props {
    index?: number,
}

interface State {
    show: boolean;
    page: ManualPages;
}

export enum ManualPages {
    CLOSE = 'manual_close',
    MENU = 'manual_menu',
    TASK_OVERVIEW = 'manual_taskoverview',
    DETAIL = 'manual_detail',
    STEP = 'manual_step',
}

export default class Manual extends Component<Props, State> {

    private storageService: StorageService = Bind('storageService');
    private eventEmitterService: EventEmitterService = Bind('eventEmitterService');
    private navigationService: NavigationService = Bind('navigationService');

    state = {
        show: false,
        page: ManualPages.MENU
    }

    private eventListner: EventListner | null = null;
    private extras: any = null;

    componentDidMount() {
        this.eventListner = {
            type: EventTypes.Manual, callback: (data: ManualPages, extras: any) => {
                this.extras = extras;
                if (data == ManualPages.CLOSE) {
                    this.setState({
                        show: false
                    });
                } else {
                    this.storageService.get(Storage.USER_ID).then(userId => {
                        this.storageService.getObject(Storage.MANUAL).then(object => {
                            let already = object[userId] && object[userId][data] == '1';
                            this.setState({
                                show: !already,
                                page: data
                            });
                        });
                    });
                }
            }
        };
        this.eventEmitterService.addListner(this.eventListner);

    }

    close = (dontShowAgain: boolean = false) => {
        if (this.extras) {
            this.extras.close(dontShowAgain);
        }
        this.setState({
            show: false
        });
    }

    dontShowAgain = () => {
        this.storageService.get(Storage.USER_ID).then(userId => {
            this.storageService.getObject(Storage.MANUAL).then(obj => {
                let store = obj ? obj : {};
                if (!store[userId]) {
                    store[userId] = {}
                }
                store[userId][this.state.page] = '1';
                this.storageService.saveObject(Storage.MANUAL, store);
            });
        });
        this.storageService.set(this.state.page, '1');
        this.close(true);
    }

    renderClose = () => {
        return (
            <TouchableOpacity onPress={this.close.bind(this, false)}>
                <Icon name='x' size={40} color={colors.white} />
            </TouchableOpacity>)
    }

    renderMenu() {
        return (
            <View style={{
                backgroundColor: 'rgba(0,0,0,0.6)',
                flexDirection: 'column',
                flex: 1,
                justifyContent: 'center',
                alignItems: 'center',
                paddingLeft: 20
            }}>
                <View style={{ marginTop: 5, flex: 1, flexDirection: 'row' }} >
                    <View style={{ alignSelf: 'center', alignItems: 'center', flex: 1, }}>
                        <Image style={{ width: 50, resizeMode: 'center', height: 75 }}
                            source={require('@components/assets/upp.png')} />
                        <Text style={{ color: colors.white }}> Step-by-step</Text>
                    </View>
                    {this.renderClose()}
                </View>
                <View style={{
                    marginTop: 10,
                    paddingTop: 20,
                    flexDirection: 'row', flex: 1, justifyContent: 'center',
                    alignItems: 'center',
                }}>
                    <View style={{ alignSelf: 'center', marginTop: 5, flex: 1, marginLeft: 5 }} >
                        <Image style={{ width: 100, resizeMode: 'center', height: 100 }}
                            source={require('@components/assets/lef.png')} />
                        <Text style={{ color: colors.white }}> Postpone</Text>
                    </View>
                    <View style={{ alignSelf: 'center', marginTop: 20, flex: 1 }} >
                        <Image style={{ width: 100, resizeMode: 'contain', height: 100, marginTop: 20 }}
                            source={require('@components/assets/tap.png')} />
                        <Text style={{ color: colors.white }}> More options</Text>
                    </View>
                    <View style={{ alignSelf: 'center', marginTop: 5, flex: 1 }} >
                        <Image style={{ width: 100, resizeMode: 'center', height: 100 }}
                            source={require('@components/assets/righ.png')} />
                        <Text style={{ color: colors.white, marginRight: 15 }}> Complete</Text>
                    </View>
                </View>

                <View style={{ marginTop: 50, flex: 1, flexDirection: 'row' }} >

                    <View style={{ flex: 1 }} />
                    <View style={{ alignSelf: 'center', alignItems: 'flex-start', flex: 1, marginLeft: 20, marginTop: 10 }}>
                        <Text style={{ color: colors.white }}> Overview</Text>
                        <Image style={{ width: 50, resizeMode: 'center', height: 75 }}
                            source={require('@components/assets/down.png')} />
                    </View>
                    {/* <View style={{ flex: 1 }} /> */}
                </View>
                <TouchableOpacity onPress={this.dontShowAgain} style={{
                    borderColor: colors.white, borderRadius: 5, borderWidth: 2,
                    height: 35, margin: 20, marginTop: 30, alignItems: 'center'
                }}>
                    <Text style={{ color: colors.white, padding: 8 }}>Don't show again</Text>
                </TouchableOpacity>
            </View>
        );
    }

    renderTaskOverview() {
        return (
            <View style={{
                backgroundColor: 'rgba(0,0,0,0.6)',
                flexDirection: 'column',
                flex: 1,
            }}>
                <View style={{ marginTop: 5, flex: 1, flexDirection: 'row' }} >
                    <View style={{ alignSelf: 'center', alignItems: 'center', flex: 1, }} />
                    {this.renderClose()}
                </View>
                <View style={{ flexDirection: 'row', flex: 1, marginRight: 5, marginLeft: 5 }}>
                    <View style={{ alignSelf: 'center', marginTop: 5, flex: 1 }} >
                        <Image style={{ width: 120, resizeMode: 'contain', height: 120 }}
                            source={require('@components/assets/lef.png')} />
                        <Text style={{ color: colors.white }}> Unsassign</Text>
                    </View>
                    <View style={{ alignSelf: 'center', marginTop: 5, flex: 1 }} >
                        <Image style={{ width: 100, resizeMode: 'contain', height: 100 }}
                            source={require('@components/assets/tap.png')} />
                        <Text style={{ color: colors.white }}> More options</Text>
                    </View>
                    <View style={{ alignSelf: 'center', marginTop: 5, flex: 1 }} >
                        <Image style={{ width: 120, resizeMode: 'contain', height: 120 }}
                            source={require('@components/assets/righ.png')} />
                        <Text style={{ color: colors.white, marginRight: 15 }}> Complete</Text>
                    </View>
                </View>

                <View style={{ marginTop: 5, flex: 1, flexDirection: 'row' }} >

                    <TouchableOpacity onPress={this.openChat} style={{ flex: 1 }}>
                        <View style={{ alignSelf: 'flex-end', alignItems: 'flex-end', marginBottom: 30, marginRight: 20, flex: 1 }}>
                            <Icon name='message-square' size={50} color={colors.white} />
                            <Text style={{ color: colors.white }}> Chat now</Text>
                        </View>
                    </TouchableOpacity>
                </View>
                <TouchableOpacity onPress={this.dontShowAgain} style={{
                    borderColor: colors.white, borderRadius: 5, borderWidth: 2,
                    height: 40, margin: 20, alignItems: 'center'
                }}>
                    <Text style={{ color: colors.white, padding: 8 }}>Don't show again</Text>
                </TouchableOpacity>
            </View>
        );
    }

    renderDetail() {
        let index = this.state.page == ManualPages.DETAIL ? 0 : 1;
        return (
            <View style={{
                backgroundColor: 'rgba(0,0,0,0.6)',
                flexDirection: 'column',
                flex: 1,
            }}>
                <View style={{ marginTop: 5, flex: 1, flexDirection: 'row' }} >
                    <View style={{ alignSelf: 'center', alignItems: 'center', flex: 1, }}>
                        <Image style={{ width: 50, resizeMode: 'contain', height: 80 }}
                            source={require('@components/assets/upp.png')} />
                        <Text style={{ color: colors.white }}>{index === 0 ? 'Show more details' : 'Last step'}</Text>
                    </View>
                    {this.renderClose()}
                </View>
                <View style={{ flexDirection: 'row', flex: 1, marginLeft: 80 }}>
                    <View style={{ alignSelf: 'center', marginTop: 5, flex: 1 }} >
                        <Image style={{ width: 120, resizeMode: 'contain', height: 120 }}
                            source={require('@components/assets/lef.png')} />
                        <Text style={{ color: colors.white }}> Postpone</Text>
                    </View>

                    <View style={{ alignSelf: 'center', marginTop: 5, flex: 1 }} >
                        <Image style={{ width: 120, resizeMode: 'contain', height: 120 }}
                            source={require('@components/assets/righ.png')} />
                        <Text style={{ color: colors.white, marginRight: 15 }}> Complete</Text>
                    </View>
                </View>

                <View style={{ marginTop: 5, flex: 1, flexDirection: 'row', alignSelf: 'center', alignItems: 'center', }} >

                    <View style={{ alignSelf: 'center', alignItems: 'center', flex: 1.4, }}>
                        <Text style={{ color: colors.white }}> {index === 0 ? 'Overview' : 'Next step'}</Text>
                        <Image style={{ width: 50, resizeMode: 'contain', height: 80 }}
                            source={require('@components/assets/down.png')} />
                    </View>
                    {!(this.extras && this.extras.isIntervention) ? <TouchableOpacity onPress={this.openChat}>
                        <View style={{
                            alignSelf: 'center', alignItems: 'center',
                            flex: 1
                        }}>
                            <Icon name='message-square' size={50} color={colors.white} />
                            <Text style={{ color: colors.white }}> Chat now</Text>
                        </View>
                    </TouchableOpacity> : null}

                </View>
                <TouchableOpacity onPress={this.dontShowAgain.bind(this, index)} style={{
                    borderColor: colors.white, borderRadius: 5, borderWidth: 2,
                    height: 40, marginLeft: 15, alignSelf: 'center', alignItems: 'center'
                }}>
                    <Text style={{ color: colors.white, padding: 8 }}>Don't show again</Text>
                </TouchableOpacity>
            </View>
        )
    }

    openChat = () => {
        if (this.navigationService.getNavigation()) {
            this.navigationService.getNavigation().push('ChatPage');
            this.close();
        }
    }

    render() {

        let page = null;
        switch (this.state.page) {
            case ManualPages.MENU:
                page = this.renderMenu(); break;
            case ManualPages.TASK_OVERVIEW:
                page = this.renderTaskOverview(); break;
            case ManualPages.DETAIL:
            case ManualPages.STEP:
                page = this.renderDetail(); break;
        }
        return (<Modal
            transparent={true}
            visible={this.state.show}
            onRequestClose={this.close}>
            {page}
        </Modal >)
    }
}