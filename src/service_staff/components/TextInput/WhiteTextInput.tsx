import React from "react";
import { TextInput, StyleSheet } from "react-native";

export default function WhiteTextInput() {
  return (
    <>
      <TextInput style={styles.inputStyle} />
    </>
  );
}

const styles = StyleSheet.create({
  inputStyle: {
    backgroundColor: "#fff",
    borderRadius: 15,
    elevation: 5,
    width: 125,
    height: 40,
  },
});
