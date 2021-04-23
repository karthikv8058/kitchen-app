import React, { Component } from "react";
import { View } from "react-native";
import AppBackground from "@components/AppBackground";
import PageWrapper from "../components/PageWrapper";
import TotalOpenButton from "../components/Buttons/TotalOpenButton";
import GuestAmoutButton from "../components/Buttons/GuestAmoutButton";
import PayedComponent from "../components/PayedComponent";
import BillingPageHeader from "../components/BillingPageHeader";
import BillListHeader from "../components/BillList/BillListHeader";

interface State {
  isGuestTotalButtonPressed: boolean;
  isChecked: boolean;
}

interface Props {
  navigation: any;
}

export default class BillingPage extends Component<Props, State> {
  constructor(props: Props) {
    super(props);
    this.state = {
      isGuestTotalButtonPressed: false,
      isChecked: false,
    };
  }

  private icon = require("@components/assets/chat_avatar.png");

  handleOnPressCheckBox = () => {
    this.setState({ isChecked: !this.state.isChecked });
  };

  renderAllGuestBill = () => {
    return (
      <>
        <View>
          <TotalOpenButton title="TOTAL Open" total="597.40" />
        </View>
        <View style={{ marginVertical: 30 }}>
          <GuestAmoutButton
            handleGuestAmoutButtonPress={() =>
              this.setState({ isGuestTotalButtonPressed: true })
            }
            source={this.icon}
            title="Guest 1"
            total="14.30"
          />
          <GuestAmoutButton source={this.icon} title="Guest 1" total="14.30" />
          <GuestAmoutButton source={this.icon} title="Guest 1" total="14.30" />
        </View>
        <GuestAmoutButton title="Order by staff" total="14.30" />
        <GuestAmoutButton
          title="Select individual items"
          style={{ marginTop: 30 }}
        />
        <PayedComponent />
      </>
    );
  };

  renderGuestBillView = () => {
    return (
      <>
        <View
          style={{
            flexDirection: "column",
            flex: 1,
            justifyContent: "space-between",
          }}
        >
          <View>
            <BillListHeader type="list-total" title="Total" amount="138.40" />
            <BillListHeader
              title="Salmon Royal"
              amount="32.30"
              type="list-normal"
            />
            <View>
              <BillListHeader
                type="list-checkbox"
                isChecked={this.state.isChecked}
                handleOnPressCheckBox={this.handleOnPressCheckBox}
                title="Salmon Royal"
                amount="32.30"
              />
              <BillListHeader
                title="Salmon Royal"
                amount="32.30"
                type="list-normal"
              />
              <BillListHeader
                title="Salmon Royal"
                amount="32.30"
                type="list-normal"
              />
            </View>
            <View>
              <BillListHeader
                type="list-checkbox"
                isChecked={this.state.isChecked}
                handleOnPressCheckBox={this.handleOnPressCheckBox}
                title="Salmon Royal"
                amount="32.30"
              />
              <BillListHeader
                title="Salmon Royal"
                amount="32.30"
                type="list-normal"
              />
            </View>
          </View>
          <View style={{ justifyContent: "flex-end" }}>
            <BillListHeader
              type="list-total"
              title="Selected"
              amount="597.40"
            />
          </View>
        </View>
      </>
    );
  };

  render() {
    return (
      <>
        <AppBackground navigation={this.props.navigation}>
          <PageWrapper>
            <BillingPageHeader id="20" />
            {!this.state.isGuestTotalButtonPressed && this.renderAllGuestBill()}
            {this.state.isGuestTotalButtonPressed && this.renderGuestBillView()}
          </PageWrapper>
        </AppBackground>
      </>
    );
  }
}
