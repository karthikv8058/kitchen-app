import AppBackground from '@components/AppBackground';
import colors from '@theme/colors';
import React, { Component } from 'react';
import {
    FlatList,
    StyleSheet,
    Text,
    TouchableOpacity,
    View,
    ListRenderItemInfo
} from 'react-native';
import Orientation from 'react-native-orientation-locker';
import { Bind } from '../ioc/ServiceContainer';
import StorageService, { Storage } from '@services/StorageService';

interface OrientationSettings {
    id: number;
    label: string;
    isSelected?: boolean
}

interface Props {
    navigation: any;
}

interface State {
    settingList: OrientationSettings[]
}

class OrientationSelectionPage extends Component<Props, State> {

    storageService: StorageService = Bind(StorageService.SERVICE_KEY);
    state: State = {

        settingList: [
            {
                id: 1,
                label: 'Dynamic Mode'

            },
            {
                id: 2,
                label: 'Portrait Mode'

            },
            {
                id: 3,
                label: 'Landscape Mode'

            }
        ]
    }

    updateSelectedItem = (id: number) => {
        for (let setting of this.state.settingList) {
            if (setting.id == id) {
                setting.isSelected = true;
            } else {
                setting.isSelected = false;
            }
        }
        this.setState({
            settingList: this.state.settingList
        })
    }
    componentDidMount() {
        let selected = this.storageService.getFast(Storage.ORIENTATION);
        this.updateSelectedItem(selected);
    }

    setOrientationToStorage = (id: number) => {
        let type = +id;
        switch (type) {
            case 2: Orientation.lockToPortrait(); break;
            case 3: Orientation.lockToLandscape(); break;
            case 1:
            default: Orientation.unlockAllOrientations();
        }
        this.storageService.set(Storage.ORIENTATION, id.toString());
        this.updateSelectedItem(id);
    }

    renderListItem = (item: ListRenderItemInfo<OrientationSettings>) => {

        let orientation = item.item;
        let borderColor = colors.stationBackground;
        if (orientation.isSelected) {
            borderColor = colors.black;
        }
        return (
            <TouchableOpacity
                style={[styles.listWrapper, { borderColor }]}
                onPress={() => { this.setOrientationToStorage(orientation.id) }}>
                <Text style={styles.textSettings}>{orientation.label}</Text>
            </TouchableOpacity>
        );

    }

    render() {
        return (
            <AppBackground
                navigation={this.props.navigation}>
                <View style={styles.container}>
                    <FlatList
                        style={{ width: '70%', marginTop: 10, marginLeft: 5 }}
                        data={this.state.settingList}
                        renderItem={this.renderListItem}
                        keyExtractor={item => String(item.id)}>
                    </FlatList>
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
    listWrapper: {
        flex: 1,
        paddingLeft: 10,
        marginTop: 10,
        backgroundColor: colors.stationBackground,
        borderWidth: 2,
        color: colors.black,
        minHeight: 45,
        borderRadius: 10,
    },
    textSettings: {
        color: colors.black,
        marginHorizontal: 10,
        marginBottom: 6,
        marginTop: 10,
        fontSize: 15
    },
});

export default OrientationSelectionPage;
