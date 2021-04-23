import React from "react";
import { Picker } from "react-native";
export default function index() {
  return (
    <>
      <Picker
        style={{
          height: 30,
          width: 100,
          borderColor: "#000",
          borderWidth: 1,
        }}
      >
        <Picker.Item label="1" value="1" />
        <Picker.Item label="2" value="2" />
        <Picker.Item label="3" value="3" />
        <Picker.Item label="4" value="4" />
        <Picker.Item label="5" value="5" />
      </Picker>
    </>
  );
}
