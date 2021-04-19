import React from "react";
import { View, Text, StyleSheet } from "react-native";

export default function TitleAmountList(props: any) {
  return (
    <>
      <View style={{ flexDirection: "row", justifyContent: "space-between" }}>
        <Text style={styles.textStyle}>{props.title}</Text>
        <Text style={styles.textStyle}>{props.amount}</Text>
      </View>
    </>
  );
}

const styles = StyleSheet.create({
  textStyle: {
    color: "#fff",
    fontWeight: "bold",
    fontSize: 18,
  },
});
