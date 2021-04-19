import React from "react";
import { View, Text, StyleSheet } from "react-native";

export default function BillingPageHeader(props: any) {
  let id = props.id ? props.id : "";
  return (
    <>
      <View
        style={{
          flexDirection: "row",
          justifyContent: "space-between",
          marginBottom: 30,
        }}
      >
        <Text style={styles.textStyle}>{`Guest group : ${id}`}</Text>
        {props.guestName && (
          <Text style={styles.textStyle}>{props.guestName}</Text>
        )}
      </View>
    </>
  );
}

const styles = StyleSheet.create({
  textStyle: {
    color: "#fff",
  },
});
