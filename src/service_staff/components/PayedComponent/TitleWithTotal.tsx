import React, { Component } from "react";
import { View, Text, StyleSheet } from "react-native";

export default function TitleWithTotal(props: any) {
  return (
    <>
      <View
        style={{
          flexDirection: "row",
          justifyContent: "space-between",
          ...props.style,
        }}
      >
        <Text style={styles.textStyle}>{props.title}</Text>
        <Text style={styles.textStyle}>{props.total}</Text>
      </View>
    </>
  );
}

const styles = StyleSheet.create({
  textStyle: {
    color: "#fff",
    fontSize: 18,
    fontWeight: "bold",
  },
});
