import AbstractComponent from '@components/AbstractComponent';
import AppBackground from '@components/AppBackground';
import colors from '@theme/colors';
import React from 'react';
import { View, Text } from 'react-native';
import HttpClient from '@services/HttpClient';
import { HTTP_CLIENT, Bind } from '../ioc/ServiceContainer';
import { GiftedChat, Send } from 'react-native-gifted-chat';
import { responseChecker } from '../utils/responseChecker';
import Icon from 'react-native-vector-icons/Feather';

interface Props {
    navigation: any;
}

interface State {
    messages: any[];
    isLoading: boolean;
}

class ChatPage extends AbstractComponent<Props, State> {

    private httpClient: HttpClient = Bind(HTTP_CLIENT);

    state = {
        messages: [],
        isLoading: false
    }

    private chatBot = {
        _id: 2,
        name: 'SmartTONi',
        avatar: require('@components/assets/chat_avatar.png'),
    };

    private ids = 1;
    private session = Math.floor(Math.random()) * 100

    componentDidMount() {
        this.setState({
            messages: [
                {
                    _id: 1,
                    text: 'How can I help you?',
                    createdAt: new Date(),
                    user: this.chatBot,
                },
            ],
        });
    }

    onSend(messages: any[] = []) {
        this.ids++;
        this.setState(previousState => ({
            messages: GiftedChat.append(previousState.messages, messages),
            isLoading: true
        }));
        let query = messages.length && messages[0].text ? messages[0].text : 'Hi';
        this.httpClient.post(this.apiBuilder.paths!.chat, { query, session: this.session }).then(response => {
            if (responseChecker(response, this.props.navigation)) {
                let message = {
                    _id: this.ids++,
                    text: response.response,
                    createdAt: new Date(),
                    user: this.chatBot,
                };
                this.setState(previousState => ({
                    messages: GiftedChat.append(previousState.messages, [message]),
                    isLoading: false
                }));
            }
        }).catch(error => {
            let message = {
                _id: this.ids++,
                text: "Something went wrong, please try again",
                createdAt: new Date(),
                user: this.chatBot,
            };
            this.setState(previousState => ({
                messages: GiftedChat.append(previousState.messages, [message]),
                isLoading: false
            }));
        });
    }

    renderSend(props: any) {
        return (
            <Send
                {...props}
            >
                <View style={{ marginRight: 10, marginBottom: 5 }}>
                    <Icon style={{ alignSelf: 'center' }} name='send' size={30} color={colors.gradientStart} />
                </View>
            </Send>
        );
    }

    renderFooter = () => {
        if (this.state.isLoading) {
            return (<Text style={{ color: colors.white, marginLeft: 14, marginBottom: 2 }}>SmartTONi is typing...</Text>);
        } else {
            return (<View />);
        }
    }

    render() {
        return (
            <AppBackground
                hideChat={true}
                navigation={this.props.navigation}>
                <GiftedChat
                    renderSend={this.renderSend}
                    messages={this.state.messages}
                    renderFooter={this.renderFooter}
                    onSend={messages => this.onSend(messages)}
                    user={{
                        _id: 1,
                    }}
                />
            </AppBackground>
        );
    }
}

// const styles = StyleSheet.create({
//     btnNewMeal: {
//         backgroundColor: colors.darkButton,
//         elevation: 5,
//         borderRadius: 10,
//         flexDirection: 'row',
//         justifyContent: 'center',
//         height: 40,
//         margin: 8,
//         alignItems: 'center'
//     }
// });

export default ChatPage;
