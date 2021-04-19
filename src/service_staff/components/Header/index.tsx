import React, { useContext } from "react";

import {
  ScrollView,
  Text,
  View,
  TouchableOpacity,
  StyleSheet,
} from "react-native";

import HeaderButton from "../Buttons/HeaderButton";
import { HeaderContext } from "../assets/contexts/headerContext";

export default function index(props: any) {
  const { renderQRscanner }: any = useContext(HeaderContext);
  const { renderTabArea }: any = useContext(HeaderContext);
  console.log("context", renderQRscanner);

  return (
    <>
      <View
        style={{
          flexDirection: "row",
          justifyContent: "space-between",
        }}
      >
        <View>
          <Text style={{ color: "#FFF" }}>Guest Group</Text>
          <Text style={{ color: "#FFF", textAlign: "center", marginTop: 10 }}>
            {props.id}
          </Text>
        </View>
        <View>
          <HeaderButton handleOnPress={renderQRscanner} title="Scan QR" />
          <HeaderButton
            handleOnPress={renderTabArea}
            style={{ marginTop: 10 }}
            title="Edit Manually"
          />
        </View>
      </View>
    </>
  );
}

const styles = StyleSheet.create({
  btnStyle: {
    backgroundColor: "#006267",
    paddingVertical: 10,
    paddingHorizontal: 20,
    borderRadius: 15,
    elevation: 5,
  },
});
