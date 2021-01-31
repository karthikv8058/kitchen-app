import Room from '@models/Room';
import colors from '@theme/colors';
import React from 'react';
import { Modal, ScrollView, StyleSheet, TextInput, TouchableOpacity, View } from 'react-native';
import Icon from 'react-native-vector-icons/Feather';
import AbstractComponent from './AbstractComponent';
import SectionedMultiSelect from './sectioned-multi-select/sectioned-multi-select';

interface Props {
    roomList: Room[];
    setSelectedItems?: Function;
}

interface State {
    show: boolean,
    storage: [],
    rack: [],
    place: [],
    searchText: string
}

export default class InventoryFilter extends AbstractComponent<Props, State> {
    private selectedRoom: Array<Element> = [];
    private selectedStorage: Array<Element> = [];
    private selectedRack: Array<Element> = [];
    private selectedPlace: Array<Element> = [];

    constructor(props: Props) {
        super(props);
        this.state = {
            show: false,
            storage: [],
            rack: [],
            place: [],
            searchText: ''
        };

    }

    show = () => {  
        this.setState({
            show: true
        });
    }

    close = () => {  
        this.setState({
            show: false
        });
    }

    onSelectedRoom = (selectedRoom) => {
        this.props.roomList.forEach((room) => {
            if (room.uuid == selectedRoom[0]) {
                this.setState({
                    storage: room.storage,

                })
            }
        });
        this.selectedRoom.push(selectedRoom)
        this.selectedStorage = [],
            this.selectedRack = [],
            this.selectedPlace = []

    }

    onSelectedStorage = (selectedStorage) => {
        this.state.storage.forEach((storage) => {
            if (storage.uuid == selectedStorage[0]) {
                this.setState({
                    rack: storage.storageracks,
                })
            }
        });
        this.selectedStorage.push(selectedStorage)
        this.selectedRack = [],
            this.selectedPlace = []
    };

    onSelectedRack = (selectedRack) => {
        this.state.rack.forEach((rack) => {
            if (rack.uuid == selectedRack[0]) {
                this.setState({
                    place: rack.places,
                })
            }
        });
        this.selectedRack.push(selectedRack)
        this.selectedPlace = []
    };

    onselectplace = (selectedPlace) => {
        this.selectedPlace.push(selectedPlace)
    };

    renderStorageInformation = (item: string) => {
        return (
            <View style={{ flex: 1 }}>
                <SectionedMultiSelect
                    styles={{
                        selectedItemText: { color: colors.primaryButton },
                        selectToggle: {
                            marginTop: 5
                        }
                    }}
                    single={true}
                    showCancelButton={true}
                    showRemoveAll={true}
                    confirmText='Apply'
                    colors={{
                        chipColor: '#000', selectToggleTextColor: '#ffffff',
                        primary: colors.primaryButton, cancel: colors.red,
                        success: colors.primaryButton
                    }}
                    items={item == 'rooms' ? this.props.roomList : (item == 'storage' ?
                        this.state.storage : (item == 'rack' ? this.state.rack : (this.state.place)))}
                    uniqueKey="uuid"
                    searchPlaceholderText={'Search ' + item}
                    selectText={' Filter by ' + item}
                    onSelectedItemsChange={item == 'rooms' ? this.onSelectedRoom : (item == 'storage' ?
                        this.onSelectedStorage : (item == 'rack' ? this.onSelectedRack : (this.onselectplace)))}
                    selectedItems={item == 'rooms' ? this.selectedRoom : (item == 'storage' ?
                        this.selectedStorage : (item == 'rack' ? this.selectedRack : (this.selectedPlace)))}
                />
            </View>

        );
    }

    renderFilterByName = () => {
        return (
            <View style={{
                flexDirection: 'row', backgroundColor: colors.white,
                paddingVertical: 5, justifyContent: 'center', margin: 10, height: 40,
                borderRadius: 5
            }}>
                <Icon name="search" size={15} style={{ marginHorizontal: 15, color: colors.black, marginTop: 3 }} />
                <TextInput
                    value={this.state.searchText}
                    selectionColor={'rgba(0,0,0,0.2)'}
                    onChangeText={searchText => this.setState({ searchText: searchText })}
                    placeholder={'Search by name'}
                    selectTextOnFocus
                    placeholderTextColor={'#999'}
                    underlineColorAndroid="transparent"
                    style={{
                        flex: 1,
                        fontSize: 12,
                        paddingVertical: 8,
                    }}
                />
            </View>
        )
    }

    onRoomCancel = () => {
        this.setState({
            rack: [],
            place: [],
            storage: []
        })
        this.selectedRoom = [],
            this.selectedPlace = [],
            this.selectedRack = [],
            this.selectedStorage = []
    }

    onStorageCancel = () => {
        this.setState({
            rack: [],
            place: [],
        })
        this.selectedPlace = []
        this.selectedRack = []
        this.selectedStorage = []
    }

    onRackCancel = () => {
        this.setState({
            place: [],
        })
        this.selectedPlace = [],
            this.selectedRack = []
    }

    onPlaceCancel = () => {
        this.selectedPlace = []
    }

    applyFilter = () => {
        this.props.setSelectedItems!(this.selectedRoom[0], this.selectedStorage[0], this.selectedRack[0], this.selectedPlace[0], this.state.searchText)
    }

    render() {
        return (
            <View style={{ flex: 1 }}>
                <Modal
                    transparent={true}
                    visible={this.state.show}
                    onRequestClose={this.close}
                    supportedOrientations={['portrait', 'landscape']}>
                    <View style={{ flex: 1, flexDirection: 'column', justifyContent: 'center' }}>
                        <ScrollView >
                            <View style={styles.filterModal}>
                                <View style={{ flexDirection: 'row' }}>
                                    {this.renderStorageInformation('rooms')}
                                    {this.selectedRoom!.length > 0 ?
                                        <TouchableOpacity onPress={this.onRoomCancel} style={{ borderRadius: 3, width: 40, height: 50, marginTop: 20 }}>
                                            <Icon name='x-circle' size={25} color={colors.red} />
                                        </TouchableOpacity> : null}
                                </View>
                                <View style={{ flexDirection: 'row' }}>
                                    {this.renderStorageInformation('storage')}
                                    {this.selectedStorage!.length > 0 ?
                                        <TouchableOpacity onPress={this.onStorageCancel} style={{ borderRadius: 3, width: 40, height: 50, marginTop: 15 }}>
                                            <Icon name='x-circle' size={25} color={colors.red} />
                                        </TouchableOpacity> : null}
                                </View>
                                <View style={{ flexDirection: 'row' }}>
                                    {this.renderStorageInformation('rack')}
                                    {this.selectedRack!.length > 0 ?
                                        <TouchableOpacity onPress={this.onRackCancel} style={{ borderRadius: 3, width: 40, height: 50, marginTop: 15 }}>
                                            <Icon name='x-circle' size={25} color={colors.red} />
                                        </TouchableOpacity> : null}
                                </View>
                                <View style={{ flexDirection: 'row' }}>
                                    {this.renderStorageInformation('place')}
                                    {this.selectedPlace!.length > 0 ?
                                        <TouchableOpacity onPress={this.onPlaceCancel} style={{ borderRadius: 3, width: 40, height: 50, marginTop: 15 }}>
                                            <Icon name='x-circle' size={25} color={colors.red} />
                                        </TouchableOpacity> : null}
                                </View>
                                {this.renderFilterByName()}
                                <View style={{
                                    flexDirection: 'row',
                                    justifyContent: 'space-around', marginVertical: 8
                                }}>
                                    <TouchableOpacity onPress={this.close}>
                                        <Icon name='x' size={30} color={colors.white} />
                                    </TouchableOpacity>
                                    <TouchableOpacity onPress={this.applyFilter}>
                                        <Icon name='check' size={30} color={colors.white} />
                                    </TouchableOpacity>
                                </View>
                            </View>
                        </ScrollView>
                    </View>
                </Modal>
            </View>

        );
    }
}

const styles = StyleSheet.create({
    filterModal: {
        flexDirection: 'column',
        minWidth: 300,
        borderRadius: 8,
        minHeight: 100,
        justifyContent: 'center',
        alignSelf: 'center',
        color: colors.black,
        backgroundColor: colors.primaryButton
    },
});
