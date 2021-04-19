import React from "react";
import { View, Text } from "react-native";
import { commonStyles } from "../assets/Styles/commonStyles";
import WhiteTextInput from "../TextInput/WhiteTextInput";

export default function TitleWithTextinput(props: any) {
  return (
    <>
      <View style={commonStyles.flexRowJustifyBetweenMtop15}>
        <Text style={commonStyles.normalTextStyle}>{props.title}</Text>
        <WhiteTextInput />
      </View>
    </>
  );
}
