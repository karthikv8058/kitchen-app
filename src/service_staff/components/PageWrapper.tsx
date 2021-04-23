import React from "react";
import { View, StyleSheet, Dimensions } from "react-native";

export default function PageWrapper(props: any) {
  return <View style={styles.container}>{props.children}</View>;
}

const windowWidth = Dimensions.get("window").width;
const windowHeight = Dimensions.get("window").height;

const styles = StyleSheet.create({
  container: {
    paddingHorizontal: 15,
    paddingVertical: 20,
    height: windowHeight - 50,
    width: windowWidth,
  },
});
