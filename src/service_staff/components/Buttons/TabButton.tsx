import React from "react";
import { TouchableOpacity, Text, StyleSheet } from "react-native";

export default function TabButton(props: any) {
  return (
    <>
      <TouchableOpacity
        onPress={props.handleOnTabPress}
        style={[
          styles.btnStyle,
          {
            ...props.style,
          },
        ]}
      >
        <Text style={{ color: "#FFF", textAlign: "center" }}>
          {props.title}
        </Text>
      </TouchableOpacity>
    </>
  );
}
//borderBottomColor: props?.tabValue === 1 ? "white" : "#6ec2c6",
const styles = StyleSheet.create({
  btnStyle: {
    paddingVertical: 10,
    borderBottomColor: "#6ec2c6",
    borderBottomWidth: 1,
    flex: 1,
    marginRight: 5,
  },
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
