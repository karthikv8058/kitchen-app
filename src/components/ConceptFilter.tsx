import colors from '@theme/colors';
import React from 'react';
import { Modal, ScrollView, StyleSheet, TouchableOpacity, View, TextInput } from 'react-native';
import Icon from 'react-native-vector-icons/Feather';
import AbstractComponent from './AbstractComponent';
import SectionedMultiSelect from './sectioned-multi-select/sectioned-multi-select';

interface Props {
    labelList: [];
    setSelectedItems: Function
}

interface State {
    show: boolean,
    recipeName: string,
    selectedLabels: string[]
}

export default class ConceptFilter extends AbstractComponent<Props, State> {
    constructor(props: Props) {
        super(props);
        this.state = {
            show: false,
            recipeName: '',
            selectedLabels: []
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

    onSelectedItemsChange = (selectedlabels: string[]) => {
        this.setState({
            selectedLabels: selectedlabels
        })
    };

    renderFilterByLabel = () => {
        return (
            <View style={{ marginTop: 10, }}>
                <SectionedMultiSelect
                    styles={{
                        selectedItemText: { color: colors.primaryButton },
                        selectToggle: {
                            marginTop: 10
                        }
                    }}
                    showCancelButton={true}
                    showRemoveAll={true}
                    confirmText='Apply'
                    colors={{
                        chipColor: '#000', selectToggleTextColor: '#ffffff',
                        primary: colors.primaryButton, cancel: colors.red,
                        success: colors.primaryButton
                    }}
                    items={this.props.labelList}
                    uniqueKey="uuid"
                    searchPlaceholderText='Search labels'
                    selectText=' Filter by labels...'
                    onSelectedItemsChange={this.onSelectedItemsChange}
                    selectedItems={this.state.selectedLabels}
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
                <Icon name="search" size={18} style={{ marginHorizontal: 15, color: colors.black, marginTop: 5 }} />
                <TextInput
                    value={this.state.recipeName}
                    selectionColor={'rgba(0,0,0,0.2)'}
                    onChangeText={recipeName => this.setState({ recipeName })}
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

    applyFilter = () => {
        this.props.setSelectedItems!(this.state.selectedLabels, this.state.recipeName)
    }
    
    render() {
        return (
            <View style={{ flexDirection: 'column' ,margin:10}}>
                <Modal
                    transparent={true}
                    visible={this.state.show}
                    onRequestClose={this.close}>
                    <View style={{ flex: 1, flexDirection: 'column', justifyContent: 'center' }}>
                        <ScrollView >
                            <View style={styles.modalContainer}>
                                {this.renderFilterByLabel()}
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
    modalContainer: {
        flexDirection: 'column',
        width: 350,
        borderRadius: 8,
        minHeight: 100,
        marginTop: 80,
        justifyContent: 'center',
        alignSelf: 'center',
        color: colors.black,
        backgroundColor: colors.primaryButton
    },
});
