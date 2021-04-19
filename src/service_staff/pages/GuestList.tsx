import React, { Component } from "react";
import AppBackground from "@components/AppBackground";
import PageWrapper from "../components/PageWrapper";
import { ScrollView, View, StyleSheet, Text, Linking } from "react-native";
import HeaderButton from "../components/Buttons/HeaderButton";
import StationService from "@services/StationService";
import { Bind } from "../../ioc/ServiceContainer";

interface State {
  guestGroups: any;
}

interface Props {
  navigation: any;
  route: any;
}
export default class GuestList extends Component<Props, State> {
  private stationService: StationService = Bind("stationService");
  private guestgroupId: any = null;
  private pageNameState: any = null;

  constructor(props: Props) {
    super(props);
    this.state = {
      guestGroups: [],
    };
  }

  componentDidMount() {
    this.listGuestgoup();
  }

  listGuestgoup() {
    this.stationService.listGuestgoup().then((guestGroups: any) => {
      console.log("listGuestgoup :::", guestGroups);

      this.setState({
        guestGroups: guestGroups,
      });
    });
  }

  handleMergeGroup = (id: any) => {
    console.log("handleMergeGroup");

    if (this.pageNameState === "SplitGuestgroup") {
      this.props.navigation.navigate("GuestGroupManagement", {
        fromID: this.guestgroupId,
        toID: id,
        pageName: this.pageNameState,
      });
    }

    this.stationService
      .mergeGuestgroup(this.guestgroupId, id)
      .then((res: any) => {
        console.log(
          "handleMergeGroup IDs :::",
          this.guestgroupId,
          id,
          res,
          this.pageNameState
        );
      });
  };

  render() {
    console.log("props.navigation in Guest list:::", this.props.route.params);
    return (
      <>
        <AppBackground navigation={this.props.navigation}>
          <PageWrapper>
            <View
              style={{
                flexDirection: "row",
                flexWrap: "wrap",
                justifyContent: "space-between",
              }}
            >
              {this.state.guestGroups &&
                this.state.guestGroups.length > 0 &&
                this.state.guestGroups.map((item: any) => {
                  return (
                    <HeaderButton
                      handleOnPress={() => {
                        this.pageNameState = this.props.route.params?.pageName;
                        if (!this.props.route.params?.pageName) {
                          this.guestgroupId = item.name;
                        }
                        !this.props.route.params?.pageName
                          ? this.props.navigation.navigate(
                              "GuestGroupManagement",
                              {
                                guestId: item.name,
                              }
                            )
                          : this.handleMergeGroup(item.name);
                      }}
                      style={{
                        minWidth: "48%",
                        marginBottom: 10,
                        backgroundColor: "#d1d1d1",
                      }}
                      title={item.name}
                      textStyle={{ color: "#000" }}
                    />
                  );
                })}

              {!this.props.route.params?.pageName && (
                <HeaderButton
                  style={{ minWidth: "48%", marginBottom: 10 }}
                  title="+"
                />
              )}
            </View>
          </PageWrapper>
        </AppBackground>
      </>
    );
  }
}
