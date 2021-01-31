import React, { useState } from 'react';
import { View, TextInput, TouchableOpacity, StyleSheet } from 'react-native';
import Icon from 'react-native-vector-icons/Feather';
import colors from '@theme/colors';

interface Prop {
    text?: string,
    style?: any,
    placeholder?: string,
    onChangeText: ((text: string) => void) | undefined;
}

function PasswordInput(props: Prop) {

    const [hidePassword, toggleHidePassword] = useState(true);

    let hidePasswordIcon = !hidePassword ? 'eye' : 'eye-off';

    return (
        <View style={styles.passwordInputContainer}>
            <TextInput
                style={{ flex: 1 }}
                underlineColorAndroid='transparent'
                placeholder={props.placeholder}
                secureTextEntry={hidePassword}
                onChangeText={props.onChangeText}
            />
            <TouchableOpacity
                activeOpacity={0.8}
                style={{ marginRight: 4 }}
                onPress={() => toggleHidePassword(!hidePassword)}>
                <Icon name={hidePasswordIcon} size={28} color={colors.grey} />
            </TouchableOpacity>
        </View>
    );
}


const styles = StyleSheet.create({
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
    }
});

export default PasswordInput;




