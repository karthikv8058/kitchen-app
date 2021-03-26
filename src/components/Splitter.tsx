import React, { Component, ReactElement } from "react"
import {
    StyleSheet,
    View,
    PanResponder,
    PanResponderInstance,
    LayoutChangeEvent,
    Dimensions,
} from "react-native"

import Icon from "react-native-vector-icons/MaterialCommunityIcons";

const SPLITER_THIKNES = 30;
const SPLITER_BAR_THIKNES = 2;
const MIN_VALUE = 180;
const THRESHOLD = 2;

interface State {
    value: number,
    orientation: "row" | "column" | null;
    dimensions?: { width: number, height: number }
}

interface Props {
    children: ReactElement[]
}

class Splitter extends Component<Props, State> {

    state: State = {
        value: 0,
        orientation: null,
    }

    panResponder: PanResponderInstance = PanResponder.create({
        onStartShouldSetPanResponder: (e, gesture) => true,
        onPanResponderMove: (evt, gestureState) => {

            let newValue = 0;
            let max = 0;
            if (this.state.orientation == "row") {
                newValue = gestureState.x0 + gestureState.dx;
                max = this.state.dimensions!!.width;
            } else {
                newValue = gestureState.y0 + gestureState.dy;
                max = this.state.dimensions!!.height;
            }

            if (newValue < MIN_VALUE) {
                newValue = MIN_VALUE;
            }

            if (newValue > (max - MIN_VALUE)) {
                newValue = max - MIN_VALUE;
            }
            let alpha = Math.abs(newValue - this.state.value);
            if (alpha > THRESHOLD) {
                this.setState({
                    value: newValue
                });
            }
        }
    });

    render() {

        let firstContainerStyle = {};
        let splitterStyle = {};
        let secondContainerStyle = {};
        let splitterBarStyle = {};
        let icon;
        if (this.state.orientation == "row") {
            firstContainerStyle = { width: this.state.value }
            secondContainerStyle = { width: (this.state.dimensions?.width ? this.state.dimensions?.width : 0) - this.state.value - 30 }
            splitterStyle = { width: SPLITER_THIKNES, flexDirection: "column" }
            splitterBarStyle = { width: SPLITER_BAR_THIKNES }
            icon = "drag-vertical"
        } else {
            firstContainerStyle = { height: this.state.value ,paddingBottom : 14 }
            splitterStyle = { height: SPLITER_THIKNES, flexDirection: "row" }
            splitterBarStyle = { height: SPLITER_BAR_THIKNES }
            secondContainerStyle = { height: ((this.state.dimensions?.height ? this.state.dimensions?.height : 0) - this.state.value - 30) }
            icon = "drag-horizontal"
        }

        return (
            <View style={[styles.container, { flexDirection: this.state.orientation ? this.state.orientation : "row"}]} onLayout={this.onLayout}>
                <View style={{ ...firstContainerStyle, alignSelf: "stretch" }} >
                    {this.props.children[0]}
                </View>
                <View {...this.panResponder.panHandlers} style={{ ...splitterStyle, alignSelf: "stretch", alignItems: 'center' }} >
                    <View style={{ flex: 1, backgroundColor: "#ddd", ...splitterBarStyle }} />
                    <Icon name={icon} size={30} color="#fff" />
                    <View style={{ flex: 1, backgroundColor: "#ddd", ...splitterBarStyle }} />
                </View>
                <View style={{ ...secondContainerStyle, alignSelf: "stretch" }} >
                    {this.props.children[1]}
                </View>
            </View>
        )
    }

    onLayout = (event: LayoutChangeEvent) => {
        let { width, height } = event.nativeEvent.layout
        let orientation: "row" | "column" = "column";
        let defaultValue = 0
        if (width > height) {
            orientation = "row";
            defaultValue = width / 2;
        } else {
            defaultValue = height / 2;
        }
        if (this.state.orientation == orientation && width == this.state.dimensions?.width && height == this.state.dimensions?.height  ) return
        this.setState({ value: this.state.value ? this.state.value : defaultValue, dimensions: { width, height }, orientation })
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
    }
})

export default Splitter;