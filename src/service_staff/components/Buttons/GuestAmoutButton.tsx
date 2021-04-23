import React from "react";
import { TouchableOpacity, Text, View, StyleSheet, Image } from "react-native";

export default function GuestMoveButton(props: any) {
  return (
    <>
      <TouchableOpacity
        onPress={props.handleGuestAmoutButtonPress}
        style={[
          styles.btnStyle,
          { paddingVertical: props.source ? 1 : 15, ...props.style },
        ]}
      >
        <View
          style={{
            flexDirection: "row",
            justifyContent: props.total ? "space-between" : "center",
            alignItems: "center",
          }}
        >
          <View
            style={{
              flexDirection: "row",
              justifyContent: "space-between",
              alignItems: "center",
            }}
          >
            {props.source && (
              <Image source={props.source} width={10} height={10} />
            )}
            <Text style={styles.textStyle}>{props.title}</Text>
          </View>

          {props.total && <Text style={styles.textStyle}>{props.total}</Text>}
        </View>
      </TouchableOpacity>
    </>
  );
}

const styles = StyleSheet.create({
  btnStyle: {
    backgroundColor: "#006267",
    borderRadius: 10,
    elevation: 5,
    paddingHorizontal: 20,
    paddingVertical: 1,
    marginTop: 15,
  },
  textStyle: {
    color: "#fff",
  },
});
