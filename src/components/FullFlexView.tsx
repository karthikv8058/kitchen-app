import React from 'react';
import { View } from "react-native";

export default function FullFlexView(props: any) {
    return (<View style={{ flex: 1, ...props.style }}>{props.children}</View>)
}