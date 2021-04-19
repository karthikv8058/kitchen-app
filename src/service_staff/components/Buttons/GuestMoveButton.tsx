import React from "react";
import { TouchableOpacity, Text, StyleSheet } from "react-native";

export default function GuestMoveButton(props: any) {
  return (
    <>
      <TouchableOpacity
        onPress={props.handleOnPress}
        style={[styles.guestMoveBtnStyle, { ...props.style }]}
      >
        <Text
          style={{
            textAlign: "center",
            color: props.textColor ? props.textColor : "#006267",
          }}
        >
          {props.title}
        </Text>
      </TouchableOpacity>
    </>
  );
}
const styles = StyleSheet.create({
  guestMoveBtnStyle: {
    backgroundColor: "white",
    paddingVertical: 10,
    paddingHorizontal: 20,
    borderRadius: 15,
    elevation: 5,
    borderWidth: 1,
    borderColor: "#FFF",
    marginTop: 10,
  },
});
