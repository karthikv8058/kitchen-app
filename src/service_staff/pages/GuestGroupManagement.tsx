import React, { Component } from "react";
import AppBackground from "@components/AppBackground";
import PageWrapper from "../components/PageWrapper";
import { ScrollView, View, StyleSheet, Text, Linking } from "react-native";
import Icons from "react-native-vector-icons/Entypo";
import Icon from "react-native-vector-icons/Feather";

import Header from "../components/Header";
import AssignLocation from "../components/TabViews/AssignLocation";
import Merge from "../components/TabViews/Merge";
import Split from "../components/TabViews/Split";
import TabButton from "../components/Buttons/TabButton";

import QRCodeScanner from "react-native-qrcode-scanner";
import { TouchableOpacity } from "react-native-gesture-handler";
import colors from "@theme/colors";

import {
  HeaderContext,
  GuestgroupContext,
} from "../components/assets/contexts/headerContext";
import AssignLocationFromScanQR from "../components/TabViews/AssignLocationFromScanQR";
import StationService from "@services/StationService";
import { Bind } from "../../ioc/ServiceContainer";

interface State {
  tabValue: number;
  isQREnabled: boolean;
  isEditManuallyPressed: boolean;
  showScannedQRTab: boolean;
  isToolbarVisible: boolean;
  currentPage: any;
  scannedQRData: any;
  qrCodeDataResponse: any;
}

interface Props {
  navigation: any;
  route: any;
}

export default class GuestGroupManagement extends Component<Props, State> {
  private stationService: StationService = Bind("stationService");
  SplitRef: any;
  MergeRef: any;
  AssignLocationRef: any;
  private tooblarList: any[] = [];
  private guestId: any =
    this.props.route.params?.guestId || this.props.route.params?.fromID;
  private guestIdTo: any = this.props.route.params?.toID;
  constructor(props: Props) {
    super(props);
    this.state = {
      tabValue: 1,
      isQREnabled: false,
      isEditManuallyPressed: false,
      showScannedQRTab: false,
      isToolbarVisible: false,
      currentPage: null,
      scannedQRData: null,
      qrCodeDataResponse: null,
    };
    this.SplitRef = React.createRef();
    this.MergeRef = React.createRef();
    this.AssignLocationRef = React.createRef();
  }

  componentDidMount() {
    this.loadToolbarSecondColoumnItems();
    console.log("TAB ADDED");
  }

  componentDidUpdate() {
    console.log("this.state.isToolbarVisible", this.state.isToolbarVisible);
  }

  UNSAFE_componentWillMount() {
    console.log("UNSAFE_componentWillMount PROPS:::", this.props.route.params);
    if (this.props.route?.params.pageName === "SplitGuestgroup") {
      this.setState({ tabValue: 3 });
    }
    if (this.props.route?.params.pageName === "MergeGuestgroup") {
      this.setState({ tabValue: 2 });
    }
    if (this.props.route?.params.pageName === "guestGroupManagementPage") {
      this.setState({ tabValue: 1 });
      this.setState({ isEditManuallyPressed: true });
      this.setState({ showScannedQRTab: false });
    }
  }

  setToolBarState = (value: any) => {
    this.setState({ isToolbarVisible: value });
  };

  setCurrentPage = (value: any) => {
    this.setState({ currentPage: value });
  };

  loadToolbarSecondColoumnItems = () => {
    this.tooblarList = [];
    this.setState({});
    this.tooblarList.push(
      <>
        <TouchableOpacity
          onPress={() => {
            this.setState({ isToolbarVisible: false });
          }}
          activeOpacity={0.8}
          style={{ height: 40, width: 40 }}
        >
          <Icon name="x" size={40} color={colors.white} />
        </TouchableOpacity>
        <TouchableOpacity
          onPress={() => {
            this.state.currentPage === "splitPage" &&
              this.SplitRef.current.SplitGuestgroupAPIcall();
            this.state.currentPage === "mergePage" &&
              this.MergeRef.current.mergeGuestgroupAPIcall();
            this.state.currentPage === "assignLocationPage" &&
              this.AssignLocationRef.current.updateGuestgroupAPIcall();
          }}
          activeOpacity={0.8}
          style={{ height: 40, width: 40 }}
        >
          <Icon name="check" size={40} color={colors.white} />
        </TouchableOpacity>
      </>
    );
  };

  onSuccess = (e: any) => {
    var json = JSON.parse(e.data);
    console.log("e.data QR JSON :::", json.name);

    console.log("DATATATATATATA");

    this.passQRcodeData(json);
    this.setState({ scannedQRData: json });
    this.setState({ isQREnabled: false, showScannedQRTab: true });
  };
  // Linking.openURL(e.data).catch((err) =>
  //   console.error("An error occured", err)
  // );

  passQRcodeData(qr: any) {
    console.log("passQRcodeData :::");
    this.stationService.passQRcodeData(qr).then((data: any) => {
      console.log("passQRcodeData :::", data);
      this.setState({
        qrCodeDataResponse: data,
      });
    });
  }

  renderQRscanner = () => {
    this.setState({ isQREnabled: true });
    this.setState({ isEditManuallyPressed: false });
  };
  renderTabArea = () => {
    this.props.navigation.navigate("GuestList", {
      pageName: "guestGroupManagementPage",
    });
    // this.setState({ isEditManuallyPressed: true });
    // this.setState({ showScannedQRTab: false });
  };

  render() {
    console.log("props.navigation:::", this.props.route.params);
    console.log("guestIdTo", this.guestIdTo);
    console.log(
      "this.state.scannedQRData.name",
      this.state.scannedQRData?.name
    );

    const {
      renderQRscanner,
      renderTabArea,
      guestId,
      guestIdTo,
      state,
      setToolBarState,
      setCurrentPage,
    } = this;
    return (
      <>
        {this.state.isQREnabled && (
          <QRCodeScanner
            containerStyle={{ backgroundColor: "#00767C" }}
            onRead={this.onSuccess}
            reactivate={true}
            // flashMode={RNCamera.Constants.FlashMode.torch}
            permissionDialogMessage="Need permission to access camera"
            reactivateTimeout={10}
            showMarker={true}
            markerStyle={{ borderColor: "#FFF", borderRadius: 10 }}
            bottomContent={
              <TouchableOpacity
                onPress={() =>
                  this.setState({ isQREnabled: false, showScannedQRTab: true })
                }
                style={styles.buttonTouchable}
              >
                <Text style={styles.buttonText}>Done</Text>
              </TouchableOpacity>
            }
          />
        )}
        {!this.state.isQREnabled && (
          <AppBackground
            toolbarMenu={this.state.isToolbarVisible ? this.tooblarList : []}
            navigation={this.props.navigation}
          >
            <PageWrapper>
              <HeaderContext.Provider
                value={{
                  renderQRscanner,
                  renderTabArea,
                }}
              >
                <Header
                  id={
                    this.state.scannedQRData !== null
                      ? this.state.scannedQRData?.name
                      : this.props.route.params?.guestId ||
                        this.props.route.params?.fromID
                  }
                />
              </HeaderContext.Provider>

              <View>
                <View
                  style={{
                    flexDirection: "row",
                    justifyContent: "space-between",
                    marginTop: 30,
                  }}
                >
                  <TabButton
                    tabValue={this.state.tabValue}
                    title="Assign Location"
                    style={{
                      borderBottomColor:
                        this.state.tabValue === 1 ? "white" : "#6ec2c6",
                    }}
                    handleOnTabPress={() =>
                      this.setState({ tabValue: 1, isToolbarVisible: false })
                    }
                  />
                  <TabButton
                    tabValue={this.state.tabValue}
                    title="Merge"
                    style={{
                      borderBottomColor:
                        this.state.tabValue === 2 ? "white" : "#6ec2c6",
                    }}
                    handleOnTabPress={() =>
                      this.setState({ tabValue: 2, isToolbarVisible: false })
                    }
                  />
                  <TabButton
                    tabValue={this.state.tabValue}
                    title="Split"
                    style={{
                      borderBottomColor:
                        this.state.tabValue === 3 ? "white" : "#6ec2c6",
                    }}
                    handleOnTabPress={() =>
                      this.setState({ tabValue: 3, isToolbarVisible: false })
                    }
                  />
                </View>
                <ScrollView showsVerticalScrollIndicator={false}>
                  <View style={{ paddingBottom: 150 }}>
                    <GuestgroupContext.Provider
                      value={{
                        guestId,
                        guestIdTo,
                        state,
                        setToolBarState,
                        setCurrentPage,
                      }}
                    >
                      {this.state.tabValue === 1 &&
                        this.state.isEditManuallyPressed && (
                          <AssignLocation ref={this.AssignLocationRef} />
                        )}
                      {this.state.tabValue === 1 &&
                        this.state.showScannedQRTab && (
                          <AssignLocationFromScanQR />
                        )}
                      {this.state.tabValue === 2 && (
                        <Merge
                          ref={this.MergeRef}
                          navigation={this.props.navigation}
                        />
                      )}
                      {this.state.tabValue === 3 && (
                        <Split
                          ref={this.SplitRef}
                          // guestId={this.props.route?.params?.toID || ""}
                          navigation={this.props.navigation}
                        />
                      )}
                    </GuestgroupContext.Provider>
                  </View>
                </ScrollView>
              </View>
            </PageWrapper>
          </AppBackground>
        )}
      </>
    );
  }
}

const styles = StyleSheet.create({
  centerText: {
    flex: 1,
    fontSize: 18,
    padding: 32,
    color: "#777",
  },
  textBold: {
    fontWeight: "500",
    color: "#000",
  },
  buttonText: {
    fontSize: 21,
    color: "#fff",
  },
  buttonTouchable: {
    padding: 16,
  },
});
