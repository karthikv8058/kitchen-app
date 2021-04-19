import React, { Component } from "react";
import { View, Text } from "react-native";
import HeaderButton from "../Buttons/HeaderButton";
import { NavigationActions, StackActions } from "react-navigation";

interface State {
  isQREnabled: boolean;
  isEditManuallyPressed: boolean;
}

interface Props {
  navigation: any;
  route: any;
}

export default class Merge extends Component<Props, State> {
  constructor(props: Props) {
    super(props);
    this.state = {
      isQREnabled: false,
      isEditManuallyPressed: false,
    };
  }
  render() {
    return (
      <>
        <View style={{ marginTop: 15 }}>
          <View
            style={{
              marginTop: 15,
              flexDirection: "row",
              justifyContent: "space-between",
            }}
          >
            <Text style={{ color: "#FFF" }}>Merge with guest group</Text>
            {this.state.isQREnabled && (
              <Text style={{ color: "#FFF" }}>21</Text>
            )}
          </View>

          <View
            style={{
              marginTop: 15,
              flexDirection: "row",
              justifyContent: "flex-end",
            }}
          >
            <View>
              <HeaderButton
                handleOnPress={() => this.setState({ isQREnabled: true })}
                title="Scan QR"
              />
              <HeaderButton
                handleOnPress={() =>
                  this.props.navigation.navigate("GuestList", {
                    pageName: "MergeGuestgroup",
                  })
                }
                style={{ marginTop: 10 }}
                title="Edit manually"
              />
            </View>
          </View>
        </View>
      </>
    );
  }
}
