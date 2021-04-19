import React from "react";

import {
  ScrollView,
  Text,
  View,
  TouchableOpacity,
  StyleSheet,
} from "react-native";

export default function BorderButton(props: any) {
  return (
    <>
      <TouchableOpacity
        onPress={props.handleOnPressBorderButton}
        style={[styles.btnRoomStyle, { ...props.style }]}
      >
        <Text style={{ color: "#FFF" }}>{props.title}</Text>
      </TouchableOpacity>
    </>
  );
}

const styles = StyleSheet.create({
  btnRoomStyle: {
    backgroundColor: "#006267",
    paddingVertical: 10,
    paddingHorizontal: 20,
    borderRadius: 15,
    elevation: 5,
    borderWidth: 1,
    borderColor: "#FFF",
  },
});
