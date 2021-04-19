import React, { Component, useContext } from "react";
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

export default class AssignLocation extends Component<Props, State> {
  private stationService: StationService = Bind("stationService");
  constructor(props: Props) {
    super(props);
    this.state = {
      roomList: [],
      stationList: [],
      selectedStation: {},
      selectedRoom: {},
      isRoomButtonPressed: false,
      isStationButtonPressed: false,
    };
  }

  componentDidMount() {
    this.LoadRoomList();
  }

  LoadRoomList() {
    this.stationService.loadRooms().then((rooms: any) => {
      console.log("LoadRoomList :::", rooms);

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
              {this.state.roomList &&
                this.state.roomList.length > 0 &&
                this.state.roomList.map((rooms: any) => {
                  return (
                    <BorderedButton
                      handleOnPressBorderButton={() => {
                        this.setState({ isRoomButtonPressed: true });
                        this.setState({ isStationButtonPressed: false });
                        this.listStationsByRoom(rooms.uuid);
                        this.setState({ selectedRoom: rooms });
                      }}
                      style={{ marginTop: 10 }}
                      title={rooms.name}
                    />
                  );
                })}
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
                {this.state.selectedRoom !== null && (
                  <BorderedButton
                    style={{ marginTop: 0 }}
                    title={this.state.selectedRoom.name}
                  />
                )}
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
                  {this.state.stationList &&
                    this.state.stationList.length > 0 &&
                    this.state.stationList.map((stations: any) => {
                      return (
                        <BorderedButton
                          handleOnPressBorderButton={() => {
                            this.setState({ isStationButtonPressed: true });
                            this.setState({ selectedStation: stations });
                          }}
                          style={{ marginTop: 10 }}
                          title={stations.name}
                        />
                      );
                    })}
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
                  {this.state.selectedStation !== null && (
                    <BorderedButton
                      style={{ marginTop: 0 }}
                      title={this.state.selectedStation.name}
                    />
                  )}
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
