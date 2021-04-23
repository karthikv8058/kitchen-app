import React, { Component } from "react";
import { View, Text, ToastAndroid } from "react-native";
import HeaderButton from "../Buttons/HeaderButton";
import { NavigationActions, StackActions } from "react-navigation";
import { GuestgroupContext } from "../assets/contexts/headerContext";
import StationService from "@services/StationService";
import { Bind } from "../../../ioc/ServiceContainer";

interface State {
  isQREnabled: boolean;
  isEditManuallyPressed: boolean;
}

interface Props {
  navigation: any;
  route: any;
}

export default class Merge extends Component<Props, State> {
  private stationService: StationService = Bind("stationService");
  static contextType = GuestgroupContext;
  private guest: any = null;
  constructor(props: Props) {
    super(props);
    this.state = {
      isQREnabled: false,
      isEditManuallyPressed: false,
    };
  }

  componentDidMount() {
    this.guest = this.context;

    this.guest.setCurrentPage("mergePage");
    setTimeout(() => {
      this.guest?.guestIdTo !== undefined && this.guest.setToolBarState(true);
    }, 1000);
  }

  mergeGuestgroup = (guestId: any, guestIdTo: any) => {
    this.stationService.mergeGuestgroup(guestId, guestIdTo).then((res: any) => {
      console.log("handleMergeGroup IDs :::", res);
      if (res) {
        this.guest.setToolBarState(false);
        ToastAndroid.show("Successfully merged", ToastAndroid.SHORT);
      }
    });
  };

  mergeGuestgroupAPIcall = () => {
    console.log(
      "mergeGuestgroupAPIcall",
      this.guest.guestId,
      this.guest.guestIdTo
    );
    this.mergeGuestgroup(this.guest.guestId, this.guest.guestIdTo);
  };
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
            {this.guest?.guestIdTo && (
              <Text style={{ color: "#FFF" }}>{this.guest?.guestIdTo}</Text>
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
