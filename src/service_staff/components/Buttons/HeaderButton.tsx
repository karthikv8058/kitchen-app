import React from "react";

import {
  ScrollView,
  Text,
  View,
  TouchableOpacity,
  StyleSheet,
} from "react-native";

export default function HeaderButton(props: any) {
  return (
    <>
      <TouchableOpacity
        onPress={props.handleOnPress}
        style={[styles.btnStyle, { ...props.style }]}
      >
        <Text
          style={{ color: "#FFF", textAlign: "center", ...props.textStyle }}
        >
          {props.title}
        </Text>
      </TouchableOpacity>
    </>
  );
}

const styles = StyleSheet.create({
  btnStyle: {
    backgroundColor: "#006267",
    paddingVertical: 10,
    paddingHorizontal: 20,
    borderRadius: 15,
    elevation: 5,
  },
});
