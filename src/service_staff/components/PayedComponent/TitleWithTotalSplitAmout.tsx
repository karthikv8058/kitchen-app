import React, { Component } from "react";
import { View, Text, StyleSheet } from "react-native";

export default function TitleWithTotalSplitAmout(props: any) {
  return (
    <>
      <View
        style={{
          flexDirection: "row",
          justifyContent: "space-between",
          marginTop: 5,
        }}
      >
        <Text style={[styles.textStyle, { paddingLeft: 15 }]}>
          {props.title}
        </Text>
        <Text style={styles.textStyle}>{props.total}</Text>
      </View>
    </>
  );
}

const styles = StyleSheet.create({
  textStyle: {
    color: "#fff",
  },
});
