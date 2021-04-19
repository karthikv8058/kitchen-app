import React, { Component } from "react";
import { View, Text, StyleSheet } from "react-native";
import TitleWithTotal from "./TitleWithTotal";
import TitleWithTotalSplitAmout from "./TitleWithTotalSplitAmout";

export default class PayedComponent extends Component {
  render() {
    return (
      <>
        <View style={{ marginTop: 30 }}>
          <TitleWithTotal title="Payed" total="146.90" />
          <TitleWithTotalSplitAmout title="Guest 5" total="126.90" />
          <TitleWithTotalSplitAmout title="Anonymous" total="20" />
          <TitleWithTotal
            title="Total"
            total="744.30"
            style={{ marginTop: 15 }}
          />
        </View>
      </>
    );
  }
}

const styles = StyleSheet.create({});
