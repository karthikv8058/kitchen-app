import React from "react";
import { View, Text, StyleSheet, TouchableOpacity } from "react-native";
import CheckBox from "react-native-check-box";

export default function BillListHeader(props: any) {
  const renderListCheckbox = () => {
    return (
      <View
        style={{
          flexDirection: "row",
          justifyContent: "space-between",
          alignItems: "center",
          marginTop: 5,
        }}
      >
        <View>
          <TouchableOpacity
            onPress={props.handleOnPressCheckBox}
            style={{
              flexDirection: "row",
              justifyContent: "space-between",
              alignItems: "center",
            }}
          >
            <CheckBox
              style={{ width: 30 }}
              isChecked={props.isChecked}
              onClick={props.handleOnPressCheckBox}
              checkBoxColor={"white"}
              rightTextStyle={"white"}
            />
            <Text style={styles.textStyle}>{props.title}</Text>
          </TouchableOpacity>
        </View>
        <Text style={styles.textStyle}>{props.amount}</Text>
      </View>
    );
  };

  const renderListTotal = () => {
    return (
      <View
        style={{
          flexDirection: "row",
          justifyContent: "space-between",
          alignItems: "center",
          marginBottom: 25,
        }}
      >
        <Text style={styles.textStyleTotal}>{props.title}</Text>
        <Text style={styles.textStyle}>{props.amount}</Text>
      </View>
    );
  };

  const renderListNormal = () => {
    return (
      <View
        style={{
          flexDirection: "row",
          justifyContent: "space-between",
          alignItems: "center",
          marginTop: 5,
        }}
      >
        <Text style={styles.textStyleNormalWithPadding}>{props.title}</Text>
        <Text style={styles.textStyleNormal}>{props.amount}</Text>
      </View>
    );
  };

  return (
    <>
      {props.type === "list-checkbox" && renderListCheckbox()}
      {props.type === "list-normal" && renderListNormal()}
      {props.type === "list-total" && renderListTotal()}
    </>
  );
}

const styles = StyleSheet.create({
  textStyle: {
    color: "#fff",
    fontWeight: "bold",
  },
  textStyleTotal: {
    color: "#fff",
    fontWeight: "bold",
    fontSize: 18,
    paddingLeft: 30,
  },
  textStyleNormalWithPadding: {
    color: "#fff",
    paddingLeft: 30,
  },
  textStyleNormal: {
    color: "#fff",
  },
});
