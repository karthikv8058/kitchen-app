import React, { Component } from "react";
import { View, Text, TouchableOpacity, StyleSheet } from "react-native";
import GuestMoveButton from "../Buttons/GuestMoveButton";
import HeaderButton from "../Buttons/HeaderButton";
import { GuestgroupContext } from "../assets/contexts/headerContext";
import StationService from "@services/StationService";
import { Bind } from "../../../ioc/ServiceContainer";
interface State {
  isQREnabled: boolean;
  isEditManuallyPressed: boolean;
  listGuests: any;
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
    };
  }

  componentDidMount() {
    this.guest = this.context;
    console.log(
      "Assignlocation Context valueeeeeeeeesss :::",
      this.guest.guestId,
      this.guest.guestIdTo,
      this.guest.state.isToolbarVisible,
      this.guest.setToolBarState
    );
    this.guest?.guestIdTo !== undefined &&
      this.setState({ isEditManuallyPressed: true });

    this.listGuests(this.guest.guestId);
  }

  listGuests(roomId: any) {
    this.stationService.listGuests(roomId).then((guests: any) => {
      console.log("listGuests :::", guests);
      this.setState({
        listGuests: guests,
      });
    });
  }

  handleOnPress = (guest: any) => {
    if (!this.guestArray.includes(guest)) {
      this.guestArray.push(guest);
      this.guest.setToolBarState(true);
      console.log("handleOnPress guestArray:::", this.guestArray);
    }
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
            {/* {this.props.guestId && (
              <Text style={{ color: "#FFF" }}>{this.props.guestId}</Text>
            )} */}
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
                    this.count++;
                    console.log("C::", this.count);
                    return (
                      <GuestMoveButton
                        handleOnPress={() => this.handleOnPress(guest.name)}
                        style={
                          this.count % 2 === 0 && { backgroundColor: "#006267" }
                        }
                        title={guest.name}
                        textColor={this.count % 2 === 0 && "#FFF"}
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
