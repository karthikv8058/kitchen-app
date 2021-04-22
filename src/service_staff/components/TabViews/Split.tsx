import React, { Component } from "react";
import {
  View,
  Text,
  TouchableOpacity,
  StyleSheet,
  ToastAndroid,
} from "react-native";
import GuestMoveButton from "../Buttons/GuestMoveButton";
import HeaderButton from "../Buttons/HeaderButton";
import { GuestgroupContext } from "../assets/contexts/headerContext";
import StationService from "@services/StationService";
import { Bind } from "../../../ioc/ServiceContainer";
interface State {
  isQREnabled: boolean;
  isEditManuallyPressed: boolean;
  listGuests: any;
  selectedItem: any;
}

interface Props {
  navigation: any;
  guestId: any;
}
export default class Split extends Component<Props, State> {
  private stationService: StationService = Bind("stationService");
  private count: any = 0;
  private guestArray: any = [];
  static contextType = GuestgroupContext;
  private guest: any = null;
  constructor(props: Props) {
    super(props);
    this.state = {
      isQREnabled: false,
      isEditManuallyPressed: false,
      listGuests: [],
      selectedItem: null,
    };
  }

  componentDidMount() {
    this.guest = this.context;
    this.guest?.guestIdTo !== undefined &&
      this.setState({ isEditManuallyPressed: true });
    this.listGuests(this.guest.guestId);
    this.guest.setCurrentPage("splitPage");
  }

  listGuests(roomId: any) {
    this.stationService.listGuests(roomId).then((guests: any) => {
      console.log("listGuests :::", guests);
      this.setState({
        listGuests: guests,
      });
    });
  }

  splitGuestgroup(fromId: any, toId: any, guests: any) {
    this.stationService
      .splitGuestgroup(fromId, toId, guests)
      .then((response: any) => {
        console.log("splitGuestgroup :::", response);
        if (response) {
          this.guest.setToolBarState(false);
          ToastAndroid.show("Successfully splitted", ToastAndroid.SHORT);
        }
      });
  }

  SplitGuestgroupAPIcall = () => {
    let filteredArray: any = this.state.listGuests.filter((item: any) => {
      return item.isSelected === true;
    });

    console.log(
      "SplitGuestgroupAPIcall",
      this.state.listGuests,
      this.guest.guestId,
      this.guest.guestIdTo
    );
    this.splitGuestgroup(
      this.guest.guestId,
      this.guest.guestIdTo,
      filteredArray
    );
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
            <Text style={{ color: "#FFF" }}>Move guest to guest group</Text>
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
                    pageName: "SplitGuestgroup",
                  })
                }
                style={{ marginTop: 10 }}
                title="Edit manually"
              />
            </View>
          </View>
          {this.state.isEditManuallyPressed && (
            <View
              style={{
                marginTop: 15,
              }}
            >
              <Text style={{ color: "#FFF" }}>Guest to be moved</Text>
              <View
                style={{
                  marginTop: 10,
                }}
              >
                {this.state.listGuests &&
                  this.state.listGuests.length > 0 &&
                  this.state.listGuests.map((guest: any) => {
                    return (
                      <GuestMoveButton
                        handleOnPress={() => {
                          this.setState({ listGuests: this.state.listGuests });
                          this.guest.setToolBarState(true);
                        }}
                        guest={guest}
                      />
                    );
                  })}
              </View>
            </View>
          )}
        </View>
      </>
    );
  }
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
