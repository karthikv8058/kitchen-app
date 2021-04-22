import React from "react";
import { TouchableOpacity, Text, StyleSheet } from "react-native";

export default function GuestMoveButton(props: any) {
  const handleOnPressButton = () => {
    props.guest.isSelected = !props.guest.isSelected;
    props.handleOnPress();
  };
  return (
    <>
      <TouchableOpacity
        onPress={handleOnPressButton}
        style={[
          styles.guestMoveBtnStyle,
          { backgroundColor: props.guest.isSelected ? "#FFF" : "#006267" },
        ]}
      >
        <Text
          style={{
            textAlign: "center",
            color: props.guest.isSelected ? "#006267" : "#FFF",
          }}
        >
          {props.guest.name}
        </Text>
      </TouchableOpacity>
    </>
  );
}
const styles = StyleSheet.create({
  guestMoveBtnStyle: {
    backgroundColor: "white",
    paddingVertical: 10,
    paddingHorizontal: 20,
    borderRadius: 15,
    elevation: 5,
    borderWidth: 1,
    borderColor: "#FFF",
    marginTop: 10,
  },
});
