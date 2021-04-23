import React, { Component } from "react";
import {
  View,
  Text,
  TouchableOpacity,
  TextInput,
  StyleSheet,
} from "react-native";
import BorderedButton from "../Buttons/BorderButton";
import StationService from "@services/StationService";
import { Bind } from "../../../ioc/ServiceContainer";
import { GuestgroupContext } from "../assets/contexts/headerContext";

interface State {
  isRoomButtonPressed: boolean;
  isStationButtonPressed: boolean;
  roomList: any;
  stationList: any;
  selectedStation: any;
  selectedRoom: any;
}

interface Props {
  navigation: any;
  route: any;
}

export default class AssignLocationFromScanQR extends Component<Props, State> {
  private stationService: StationService = Bind("stationService");
  static contextType = GuestgroupContext;
  private guest: any = null;
  constructor(props: Props) {
    super(props);
    this.state = {
      roomList: [],
      stationList: [],
      selectedStation: {},
      selectedRoom: {},
      isRoomButtonPressed: true,
      isStationButtonPressed: true,
    };
  }

  componentDidMount() {
    this.guest = this.context;
    console.log("this.guest", this.guest);

    // if (this.props.route.params.backNavigation) {
    //   setTimeout(() => {
    //     this.LoadRoomList();
    //   }, 3000);
    // } else {
    this.LoadRoomList();
    // }
  }

  LoadRoomList() {
    this.stationService.loadRooms().then((rooms: any) => {
      this.setState({
        roomList: rooms,
      });
    });
  }

  listStationsByRoom(roomId: any) {
    this.stationService.listStationsByRoom(roomId).then((stations: any) => {
      console.log("listStationsByRoom :::", stations, roomId);
      this.setState({
        stationList: stations,
      });
    });
  }

  render() {
    return (
      <>
        {!this.state.isRoomButtonPressed ? (
          <View style={{ marginTop: 15 }}>
            <Text style={{ color: "#FFF" }}>Room :</Text>
            <View
              style={{
                marginTop: 15,
              }}
            >
              <BorderedButton
                handleOnPressBorderButton={() => {
                  this.setState({ isRoomButtonPressed: true });
                  this.setState({ isStationButtonPressed: false });
                }}
                style={{ marginTop: 10 }}
                title={"rooms.name"}
              />
            </View>
          </View>
        ) : (
          <View>
            <View style={{ marginTop: 15 }}>
              <Text style={{ color: "#FFF" }}>Room :</Text>
              <View
                style={{
                  marginTop: 15,
                  flexDirection: "row",
                  justifyContent: "space-between",
                }}
              >
                <BorderedButton style={{ marginTop: 0 }} title={"name"} />

                <TouchableOpacity
                  onPress={() => this.setState({ isRoomButtonPressed: false })}
                >
                  <Text style={{ color: "#FFF" }}>Change</Text>
                </TouchableOpacity>
              </View>
            </View>
            {!this.state.isStationButtonPressed ? (
              <View style={{ marginTop: 15 }}>
                <Text style={{ color: "#FFF" }}>Station :</Text>
                <View
                  style={{
                    marginTop: 15,
                  }}
                >
                  <BorderedButton
                    handleOnPressBorderButton={() => {
                      this.setState({ isStationButtonPressed: true });
                    }}
                    style={{ marginTop: 10 }}
                    title={"stations.name"}
                  />
                </View>
              </View>
            ) : (
              <View style={{ marginTop: 15 }}>
                <Text style={{ color: "#FFF" }}>Station :</Text>
                <View
                  style={{
                    marginTop: 15,
                    flexDirection: "row",
                    justifyContent: "space-between",
                  }}
                >
                  <BorderedButton style={{ marginTop: 0 }} title={"name"} />
                  <TouchableOpacity
                    onPress={() =>
                      this.setState({ isStationButtonPressed: false })
                    }
                  >
                    <Text style={{ color: "#FFF" }}>Change</Text>
                  </TouchableOpacity>
                </View>
                <View
                  style={{
                    marginTop: 15,
                  }}
                >
                  <Text style={{ color: "#FFF" }}>Table :</Text>
                  <TextInput style={styles.textInputStyle} />
                </View>
              </View>
            )}
          </View>
        )}
      </>
    );
  }
}

const styles = StyleSheet.create({
  textInputStyle: {
    backgroundColor: "white",
    marginTop: 15,
    width: 150,
    height: 30,
  },
});
