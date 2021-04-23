import React from "react";
import { Text, StyleSheet, View, CheckBox } from "react-native";

export default function Checkbox(props: any) {
  return (
    <>
      <View style={styles.checkboxContainer}>
        <CheckBox style={styles.checkbox} />
        <Text style={styles.label}>{props.value}</Text>
      </View>
    </>
  );
}

const styles = StyleSheet.create({
  checkboxContainer: {
    flexDirection: "row",
    marginBottom: 0,
  },
  checkbox: {
    alignSelf: "center",
  },
  label: {
    margin: 8,
  },
});
