import colors from "@theme/colors";
import React, { Component } from "react";
import {
  Dimensions,
  StyleSheet,
  Text,
  TouchableWithoutFeedback,
  View,
  ActivityIndicator,
  ScaledSize,
  GestureResponderEvent,
  Platform,
  AsyncStorage,
  LayoutChangeEvent,
} from "react-native";
import RadialGradient from "react-native-radial-gradient";
import Icon from "react-native-vector-icons/Feather";
import AntDesignIcon from "react-native-vector-icons/MaterialIcons";
import { NavigationActions, StackActions } from "react-navigation";
import { BACK, menuCategory } from "../utils/constants";
import { TouchableOpacity } from "react-native-gesture-handler";
import Orientation from "react-native-orientation-locker";

interface Props {
  hideBack?: boolean;
  hideChat?: boolean;
  toolbarMenu?: any[];
  navigation: any;
  proceed?: boolean;
  placeOrderCallback?: Function;
  pressOrderCallback?: Function;
  pressWebOrderCallback?: Function;
  onGoBack?: Function;
  showLoader?: boolean;
  doNaviagte?: boolean;
  checkBackgroundColor?: boolean;
  setStations?: boolean;
  enableBack?: boolean;
  category?: string;
  index?: number;
  isToSwitch?: boolean;
  isFromInventory?: boolean;
  bottomMenu?:
    | {
        icon: string;
        onClick: ((event: GestureResponderEvent) => void) | undefined;
      }[]
    | null;
}

interface State {
  time?: string;
  category: number;
  isPortrait: any;

  orientation: "landscape" | "portrait";
  dimensions: { width: number; height: number };
}

export default class AppBackground extends Component<Props, State> {
  private menuCount: number = 0;

  private timer = null;
  constructor(props: any) {
    super(props);
    this.state = {
      time: "",
      category: 0,
      isPortrait: "",
      orientation: "landscape",
      dimensions: { width: 0, height: 0 },
    };

    this.setData = this.setData.bind(this);
  }

  componentDidMount() {
    this.checkCategory();
    this.getCurrentTime();
    this.timer = setInterval(() => {
      this.getCurrentTime();
    }, 1000 * 30);
  }

  checkCategory() {
    this.props.category === menuCategory.taskCategory
      ? (this.menuCount = 0)
      : this.props.category === menuCategory.orderCategory
      ? (this.menuCount = 1)
      : this.props.category === menuCategory.inventoryCategory
      ? (this.menuCount = 2)
      : this.props.category === menuCategory.profileCategory
      ? (this.menuCount = 3)
      : (this.menuCount = 0);
    this.setState({
      category: this.menuCount,
    });
  }

  onLayout = (event: LayoutChangeEvent) => {
    let { width, height } = event.nativeEvent.layout;
    let orientation: "landscape" | "portrait" = "portrait";
    if (width > height) {
      orientation = "landscape";
    } else {
    }
    if (this.state.orientation == orientation) return;
    this.setState({ orientation, dimensions: { width, height } });
  };

  getCurrentTime = () => {
    let hour = new Date().getHours();
    let minutes = new Date().getMinutes();
    this.setState({
      time: this.padNumber(hour) + ":" + this.padNumber(minutes),
    });
  };

  padNumber(d: number) {
    return d < 10 ? "0" + d.toString() : d.toString();
  }

  componentWillUnmount() {
    clearInterval(this.timer);
  }

  onBackPress = () => {
    if (this.props.onGoBack) {
      this.props.onGoBack();
    } else if (this.props.enableBack) {
      const resetAction = StackActions.reset({
        index: 0,
        actions: [
          NavigationActions.navigate({ routeName: "DetectedRestuarants" }),
        ],
      });
      this.props.navigation.dispatch(resetAction);
    } else {
      this.props.navigation.goBack();
    }
  };

  setData(category: number) {
    this.setState({
      category: category,
    });
  }

  goToMenuPage() {
    this.props.navigation.push("MenuPage", {
      isFromIndex: false,
    });
  }
  renderMenuButton() {
    if (!this.props.doNaviagte) {
      return (
        <TouchableWithoutFeedback onPress={() => this.goToMenuPage()}>
          <Icon
            style={{ alignSelf: "center", marginBottom: 10, marginRight: 2 }}
            name="menu"
            size={30}
            color={colors.white}
          />
        </TouchableWithoutFeedback>
      );
    }
  }

  openChat = () => {
    this.props.navigation.push("ChatPage");
  };

  render() {
    let flexDirection: "row" | "column" = "row";
    let menuItemFlexDirection: "row" | "column" = "column";
    let toolbarStyle = {};
    if (this.state.orientation == "portrait") {
      flexDirection = "column";
      menuItemFlexDirection = "row";
      toolbarStyle = { height: 50 };
    } else {
      toolbarStyle = { width: 50 };
    }

    let sideBarTheme = this.props.checkBackgroundColor
      ? styles.sidebarBlack
      : styles.sidebarNormal;

    let center = null;
    if (this.state.dimensions.width > 0) {
      center = [
        this.state.dimensions.width / 2,
        this.state.dimensions.height / 2,
      ];
    }
    return (
      <RadialGradient
        style={{
          flex: 1,
          flexDirection: flexDirection,
        }}
        center={center}
        colors={["#00ABB4", "#00767C"]}
        onLayout={this.onLayout}
      >
        {this.props.children}

        <View
          style={[
            {
              elevation: 5,
              flexDirection: menuItemFlexDirection,
              justifyContent: "flex-start",
              paddingTop: 10,
              alignItems: "center",
              ...toolbarStyle,
            },
            sideBarTheme,
          ]}
        >
          <View style={{ width: 8, height: 8 }} />
          {!this.props.hideBack && (
            <TouchableOpacity activeOpacity={0.8} onPress={this.onBackPress}>
              <AntDesignIcon name="arrow-back" size={30} color={colors.white} />
            </TouchableOpacity>
          )}
          <View style={{ flex: 1 }} />
          <View>
            {this.props.showLoader ? (
              <ActivityIndicator size="large" color={colors.white} />
            ) : (
              <View
                style={{
                  flexDirection: menuItemFlexDirection,
                  alignItems: "center",
                  justifyContent: "center",
                }}
              >
                {this.props.toolbarMenu &&
                  this.props.toolbarMenu.map((item, i) => item)}
              </View>
            )}
          </View>

          <View style={{ flex: 1 }} />

          {/* <View style={[{ flexDirection: menuItemFlexDirection }, styles.timeText]}> */}
          {this.props.bottomMenu &&
            this.props.bottomMenu.map((item, i) => (
              <TouchableOpacity
                onPress={item.onClick}
                style={{ marginBottom: 8 }}
              >
                <Icon name={item.icon} size={30} color={colors.white} />
              </TouchableOpacity>
            ))}
          {!this.props.hideChat && !this.props.bottomMenu && (
            <TouchableOpacity
              onPress={this.openChat}
              style={{
                marginRight: !!this.state.isPortrait ? 5 : 0,
                marginBottom: 8,
              }}
            >
              <Icon name="message-square" size={30} color={colors.white} />
            </TouchableOpacity>
          )}
          {/* </View> */}
          {this.renderMenuButton()}
          <Text
            style={{
              fontWeight: "bold",
              color: colors.white,
            }}
          >
            {this.state.time}
          </Text>
          <View style={{ width: 4, height: 4 }} />
        </View>
      </RadialGradient>
    );
  }
}

const styles = StyleSheet.create({
  sidebarHeight: {
    height: 50,
  },
  sidebarWidth: {
    width: 60,
  },
  sidebarNormal: {
    backgroundColor: colors.toolBarColor,
  },
  sidebarBlack: {
    backgroundColor: colors.black,
  },

  listContainer: {
    padding: 10,
  },
  timeText: {
    alignItems: "center",
    marginTop: "auto",
    color: "#fff",
    flexDirection: "column",
  },
  imageConatiner: {
    marginBottom: 10,
  },
  textContainer: {
    alignItems: "center",
    marginTop: 8,
    flexDirection: "row",
    color: colors.white,
  },
  backContainer: {
    color: colors.white,
    fontSize: 14,
    fontWeight: "bold",
    alignItems: "center",
    textAlign: "center",
    marginLeft: 2,
  },
  menuIconContainer: {
    padding: 20,
  },
  menuContainer: {
    marginLeft: 10,
    margin: 8,
    color: colors.black,
    fontWeight: "bold",
  },

  ProceedConatiner: {
    flex: 1,
    padding: 30,
    alignItems: "center",
    justifyContent: "center",
  },
});
