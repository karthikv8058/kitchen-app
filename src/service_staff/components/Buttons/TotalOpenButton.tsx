import React from "react";
import { TouchableOpacity, Text, View, StyleSheet } from "react-native";

export default function TotalOpenButton(props: any) {
  return (
    <>
      <TouchableOpacity style={styles.btnStyle}>
        <View style={{ flexDirection: "row", justifyContent: "space-between" }}>
          <Text style={styles.textStyle}>{props.title}</Text>
          <Text style={styles.textStyle}>{props.total}</Text>
        </View>
      </TouchableOpacity>
    </>
  );
}

const styles = StyleSheet.create({
  btnStyle: {
    backgroundColor: "#d5d5d5",

    borderRadius: 10,
    elevation: 5,
    paddingHorizontal: 20,
    paddingVertical: 10,
  },
  textStyle: {
    fontWeight: "bold",
    fontSize: 18,
    color: "#000",
  },
});
