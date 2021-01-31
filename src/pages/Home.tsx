/**
* SmartToni React Native App
*/

import t from '@translate';
import React, { Component } from 'react';
import { StatusBar, StyleSheet } from 'react-native';

interface Props {
  navigation: any;
}

export default class Home extends Component<Props , any> {
  constructor(props: Props) {
    super(props);
  }
  componentDidMount() {
    StatusBar.setHidden(true);
    t('lang');
  }
  render() {
      return (<AppContainer />);
  }

}
