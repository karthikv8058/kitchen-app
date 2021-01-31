import * as React from 'react';
import { View, Text } from 'react-native';
import { SkypeIndicator } from 'react-native-indicators';

interface Prop {
    text?: string,
    style?: any
}

function LoaderWithText(props: Prop) {
    return (
        <View style={props.style}>
            <View style={{ flexDirection: "row", alignItems: "center" }}>
                <SkypeIndicator count={5} color='white' />
                <Text style={{ color: "#fff", marginLeft: 32 }}>{props.text}</Text>
            </View>
        </View>

    );
}

export default LoaderWithText;