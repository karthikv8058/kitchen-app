import React, { Component } from "react";
import { View, Text, StyleSheet, TextInput } from "react-native";
import AppBackground from "@components/AppBackground";
import PageWrapper from "../components/PageWrapper";
import TotalOpenButton from "../components/Buttons/TotalOpenButton";
import GuestAmoutButton from "../components/Buttons/GuestAmoutButton";
import PayedComponent from "../components/PayedComponent";
import BillingPageHeader from "../components/BillingPageHeader";
import BillListHeader from "../components/BillList/BillListHeader";
import HeaderButton from "../components/Buttons/HeaderButton";
import WhiteTextInput from "../components/TextInput/whiteTextInput";
import TitleAmountList from "../components/BillList/TitleAmountList";

import { commonStyles } from "../components/assets/Styles/commonStyles";
import TitleWithTextinput from "../components/BillList/TitleWithTextinput";

interface State {
  isGuestTotalButtonPressed: boolean;
  isChecked: boolean;
}

interface Props {
  navigation: any;
}

export default class BillingPaymentPage extends Component<Props, State> {
  constructor(props: Props) {
    super(props);
    this.state = {
      isGuestTotalButtonPressed: false,
      isChecked: false,
    };
  }

  private icon = require("@components/assets/chat_avatar.png");

  render() {
    return (
      <>
        <AppBackground>
          <PageWrapper>
            <BillingPageHeader id="20" guestName="Guest 1" />
            <TitleAmountList title="Invoice" amount="138.40" />
            <TitleWithTextinput title="Tip" />
            <TitleWithTextinput title="Total payable" />
            <View style={commonStyles.flexRowJustifyFlexendMtop15}>
              <HeaderButton
                style={{ width: 125, textAlign: "center" }}
                title="Paid Cash"
              />
            </View>
            <View style={commonStyles.flexRowJustifyFlexendMtop15}>
              <HeaderButton
                style={{ width: 125, textAlign: "center" }}
                title="Paid by Card"
              />
            </View>
          </PageWrapper>
        </AppBackground>
      </>
    );
  }
}
