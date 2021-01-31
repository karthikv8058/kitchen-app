import React, { Component } from 'react';
import { Image, ToastAndroid, ActivityIndicator, View, TouchableOpacity, Modal, Text, ScrollView } from 'react-native';
import colors from '@theme/colors';
import ioc, { HTTP_CLIENT } from '../ioc/ServiceContainer';
import { ApiBaseUrl } from '@native/ClientServer';
import NetInfo from '@react-native-community/netinfo';
import RNFetchBlob from 'rn-fetch-blob';
import { ImageType } from '@models/ImageType';
import Icon from 'react-native-vector-icons/Feather';
import ImagePicker from 'react-native-image-crop-picker';
import t from '@translate';
import Recipe from '@models/Recipe';

interface Props {
    source?: string;
    imageStyle?: Element;
    imageType?: ImageType;
    loadData?: Function;
    intervention?: boolean;
    isFromRecipe?: boolean;
    index?: number;
    item?: Recipe;
    onPress: Function;
}
interface State {
    width: number;
    height: number;
    imageWidth: number;
    imageHeight: number;
    source: string;
    webAppCredentials: string;
    isLoaderVisible: boolean;
    imageUpload: boolean;

}

export default class FullWidthImage extends Component<Props, State> {

    httpClient = ioc.ServiceFactory.getServiceBy(HTTP_CLIENT);
    apiBuilder = ioc.ServiceFactory.getServiceBy('apiBuilder');

    webUrl = ApiBaseUrl;

    constructor(props: Props) {
        super(props);
        this.state = {
            width: 200,
            height: 200,
            imageWidth: 200,
            imageHeight: 200,
            source: '',
            webAppCredentials: '0',
            isLoaderVisible: false,
            imageUpload: false
        };
    }

    _onLayout(event) {
        const containerWidth = event.nativeEvent.layout.width;
        this.setState({
            width: containerWidth,
            height: containerWidth
        });

    }

    componentDidMount() {
        let url = this.props.source ? this.apiBuilder.paths.imageUrl + this.props.source : null;
        this.setState({
            source: url
        });
        if (this.props.source) {
            Image.getSize(this.props.source.uri, (width, height) => {
                this.setState({ imageWidth: width, imageHeight: height });
            }, (error) => { });
            let url = this.props.source ? this.apiBuilder.paths.imageUrl + this.props.source : null;
            this.setState({
                source: url
            });
        }
        this.httpClient.post(this.apiBuilder.paths.webCredentials, {}).then((response) => {
            this.setState({
                webAppCredentials: response
            });
        }).catch((err: Error) => {
        });
    }
    componentDidUpdate(prevProps, prevState) {
        if (this.props.source !== '' && prevProps.source !== this.props.source) {
            let url = this.props.source ? this.apiBuilder.paths.imageUrl + this.props.source : null;
            this.setState({
                source: url
            });
        }
    }
    static getDerivedStateFromProps(nextProps, prevState) {
        if (nextProps.source !== prevState.source) {
            return { someState: nextProps.source };
        } else {return null; }
    }

    getImageType = (): string => {
        let type = this.props.imageType;
        switch (type) {
            case ImageType.RECIPE: return 'recipe_media';
            case ImageType.STEPS: return 'taskstep_media';
            default: return 'task_media';
        }
    }

    uploadImage(data) {
        this.setState({
            isLoaderVisible: true
        });
        NetInfo.fetch().then(state => {
            if (state.isConnected) {
        RNFetchBlob.fetch('POST', this.webUrl + 'api/1.0/assets/upload', {
            Accept: 'application/json',
            Authorization: 'Bearer ' + this.state.webAppCredentials.webCredentialData.accessToken,
            'Content-Type': 'multipart/form-data',
        }, [
            { name: 'object_type', data: 'restaurant' },
            { name: 'object_uuid', data: this.state.webAppCredentials.webCredentialData.resturantId },
            { name: 'entity_type', data: this.getImageType() },
            { name: 'entity_uuid', data: this.props.item.uuid },
            { name: 'images[]', filename: data.modificationDate, data: RNFetchBlob.wrap(data.path) }
        ]).then((resp) => {
            this.httpClient.post(this.apiBuilder.paths.uploadImage,
                {
                    imageType: this.props.imageType,
                    isSteps: this.props.index === 0 ? false : true,
                    isFromRecipe: this.props.isFromRecipe ? true : false,
                    isIntervention: this.props.intervention ? true : false,
                    uri: JSON.parse(resp.text()).data.images[0].url,
                    uuid: this.props.item.uuid
                }).then((responses) => {
                        this.props.loadData();
                    let url = this.apiBuilder.paths.imageUrl + responses.nameValuePairs.data;
                    setTimeout(() => {
                        this.setState({
                            source: url,
                            isLoaderVisible: false
                        });
                    }, 2000);
                }).catch((error: Error) => {
                    ToastAndroid.show('Something went wrong..', ToastAndroid.SHORT);
                    this.setState({
                        isLoaderVisible: false
                    });
                });
        }).catch((error: Error) => {
            ToastAndroid.show('Something went wrong..', ToastAndroid.SHORT);
            this.setState({
                isLoaderVisible: false
            });
        });
    } else {
        ToastAndroid.show('Please check your internet connectivity', ToastAndroid.SHORT);
    }
    });

    }
    setCameraOpen = () => {
        NetInfo.fetch().then(state => {
            if (state.isConnected) {
                this.setState({
                    imageUpload: true
                });
            } else {
                ToastAndroid.show('Please check your internet connectivity', ToastAndroid.SHORT);
            }
            });
        }
    toggleImageUpload = () => {

                this.setState({
                    imageUpload: !this.state.imageUpload
                });
            }
    renderCameraOption = () => {
                return (
                    <Modal
                        transparent={true}
                        visible={this.state.imageUpload}
                        onRequestClose={() => { this.toggleImageUpload(); }}>
                        <View style={{ flex: 1, flexDirection: 'column', justifyContent: 'center' }}>
                            <View style={{
                                flexDirection: 'column',
                                width: 300,
                                borderRadius: 8,
                                minHeight: 100,
                                maxHeight: 300,
                                justifyContent: 'center',
                                alignSelf: 'center',
                                backgroundColor: colors.primaryButton,
                            }}>
                                <View style={{ flexDirection: 'row', height: 50, }}>
                                    <Text style={{ flex: .85, alignSelf: 'center', marginBottom: 5,
                                    textAlign: 'center', color: colors.white, marginTop: 10 }}>
                                        {t('order-overview.choose-option')}</Text>
                                    <TouchableOpacity style={{ flex: .15, alignSelf: 'center' }} 
                                    onPress={this.toggleImageUpload.bind(this)}>
                                        <Icon name='x' size={25} color={colors.white} />
                                    </TouchableOpacity >
                                </View>
                                <ScrollView style={{ marginBottom: 10 }}>
                                    <Text style={{
                                        marginTop: 5,
                                        backgroundColor: colors.white,
                                        borderRadius: 10,
                                        textAlign: 'center',
                                        textAlignVertical: 'center',
                                        margin: 10,
                                        height: 50,
                                        color: colors.black
                                    }}
                                        onPress={this.openImagePicker.bind(this, true)}>Camera</Text>
                                    <Text style={{
                                        backgroundColor: colors.white,
                                        borderRadius: 10,
                                        textAlign: 'center',
                                        textAlignVertical: 'center',
                                        margin: 10,
                                        height: 50,
                                        color: colors.black
                                    }}
                                        onPress={this.openImagePicker.bind(this, false)}>Gallery</Text>
                                </ScrollView>
                            </View>
                        </View>
                    </Modal>
                );
            }
    openImagePicker = (isCamera: boolean) => {
                if (!isCamera) {
                    ImagePicker.openPicker({
                        cropping: true, width: 300, height: 400, cropperCircleOverlay: true,
                        freeStyleCropEnabled: true, avoidEmptySpaceAroundImage: true,
                        includeBase64: true
                    }).then(image => {
                        this.setState({
                            imageUpload: false
                        });
                        this.uploadImage(image);
                    }).catch(e => {
                        this.setState({
                            imageUpload: false
                        });
                    });

                } else {
                    ImagePicker.openCamera({
                        cropping: true, width: 500, height: 500, cropperCircleOverlay: true,
                        compressImageMaxWidth: 640, compressImageMaxHeight: 480, freeStyleCropEnabled: true,
                        includeBase64: true
                    }).then((image) => {
                        this.setState({
                            imageUpload: false
                        });
                        this.uploadImage(image);
                    }).catch(e => {
                        this.setState({
                            imageUpload: false
                        });
                    });
                }
    }

onDidFocus() {   
    let url = this.props.source ? this.apiBuilder.paths.imageUrl + this.props.source : null;
    this.setState({
        source: url
    }); }

render() {
    let image = this.state.source ? { uri: this.state.source } : require('./assets/placeholder.png');
    return (
        <View
            style={{
                alignSelf: 'center',
                flexDirection: 'row',
                justifyContent: 'center', marginBottom: 50
            }} onLayout={this._onLayout.bind(this)} >
            {this.renderCameraOption()}
            {this.state.isLoaderVisible ?
                <ActivityIndicator style={{ height: 150, alignItems: 'center', marginRight: 30 }} size='large' color={colors.black} />
                :
                <View style={{ flexDirection: 'column', alignItems: 'flex-end' }}>
                    <Image
                        resizeMode='cover'
                        source={image}
                        key={(new Date()).getTime()}
                        CACHE='reload'
                        resizeMode='cover'
                        style={this.props.imageStyle ? this.props.imageStyle : { height: 150, width: 300 }}
                    />
                    {!!this.state.webAppCredentials.recipeMode ?
                        <View style={{ marginLeft: 20, position: 'absolute', bottom: 5, right: 15, backgroundColor: 'rgba(255, 255, 255, 0.2)', padding: 8 }}>
                            <TouchableOpacity onPress={this.setCameraOpen}>
                                <Icon name='camera' size={25} color={colors.black} />
                            </TouchableOpacity >
                        </View> : null}
                </View>
            }
        </View >
    );
}
}
