import React, { Component } from 'react';
import { Animated, PanResponder, Platform, StyleSheet, TouchableOpacity, View } from 'react-native';

import PropTypes from 'prop-types';

const SWIPE_THRESHOLD_PERCENTAGE = 50;

interface Props {
    swipeDuration?: number;
    disableSwipeToLeft?: boolean;
    disableSwipeToRight?: boolean;
    previewSwipeDemo?: boolean;
    previewDuration?: number;
    previewOpenValue?: number;
    previewOpenDelay?: number;
    previewCloseDelay?: number;
    swipingLeft?: boolean;
    recalculateHiddenLayout?: boolean;
    directionalDistanceChangeThreshold?: number;
    onSwipedLeft?: Function;
    onSwipedRight?: Function;
    enableScroll?: Function;
}

interface State {
    dimensionsSet: boolean;
    swipingLeft: boolean;
    hiddenWidth: number;
    hiddenHeight: number;
}

export default class SwipeView extends Component<Props, State> {

    horizontalSwipeGestureBegan = false;
    horizontalSwipeGestureEnded = false;
    rowItemJustSwiped = false;
    swipeInitialX = null;
    ranPreview = false;
    _translateX = new Animated.Value(0);
    _panResponder: any = null;

    constructor(props: any) {
        super(props);
        this.state = {
            dimensionsSet: false,
            hiddenHeight: 0,
            hiddenWidth: 0,
            swipingLeft: this.props.swipingLeft
        };
        this._panResponder = PanResponder.create({
            onMoveShouldSetPanResponder: (e, gs) => this.handleOnMoveShouldSetPanResponder(e, gs),
            onPanResponderMove: (e, gs) => this.handlePanResponderMove(e, gs),
            onPanResponderRelease: (e, gs) => this.handlePanResponderEnd(e, gs),
            onPanResponderTerminate: (e, gs) => this.handlePanResponderEnd(e, gs),
            onShouldBlockNativeResponder: _ => false,
            onPanResponderGrant: () => this.handlePanResponderGrant(),
        });
    }

    handlePanResponderGrant = () => {
        if (this.props.enableScroll){
            this.props.enableScroll(false)
        }
    }

    getPreviewAnimation = (toValue: number, delay: number) => {
        return Animated.timing(
            this._translateX,
            { duration: this.props.previewDuration, toValue, delay }
        );
    };

    onContentLayout = (e: any) => {
        this.setState({
            dimensionsSet: !this.props.recalculateHiddenLayout,
            hiddenHeight: e.nativeEvent.layout.height,
            hiddenWidth: e.nativeEvent.layout.width,
        });

        if (this.props.previewSwipeDemo && !this.ranPreview) {
            let { previewOpenValue } = this.props;
            this.ranPreview = true;

            this.getPreviewAnimation(previewOpenValue, this.props.previewOpenDelay)
                .start(_ => {
                    this.getPreviewAnimation(0, this.props.previewCloseDelay).start();
                });
        };
    };

    handleOnMoveShouldSetPanResponder = (e, gs) => {
        const { dx } = gs;
        return Math.abs(dx) > this.props.directionalDistanceChangeThreshold;
    };

    handlePanResponderMove = (e, gestureState) => {
        const { dx, dy } = gestureState;
        const absDx = Math.abs(dx);
        const absDy = Math.abs(dy);

        if (this.horizontalSwipeGestureEnded)
            return;

        if (absDx > this.props.directionalDistanceChangeThreshold) {

            if (this.swipeInitialX === null) {
                this.swipeInitialX = this._translateX._value
            }
            if (!this.horizontalSwipeGestureBegan) {
                this.horizontalSwipeGestureBegan = true;
                this.props.swipeGestureBegan && this.props.swipeGestureBegan();
            }

            let newDX = this.swipeInitialX + dx;
            if (this.props.disableSwipeToLeft && newDX < 0) { newDX = 0; }
            if (this.props.disableSwipeToRight && newDX > 0) { newDX = 0; }

            this._translateX.setValue(newDX);

            let toValue = 0;
            if (this._translateX._value >= 0) {
                this.setState({
                    ...this.state,
                    swipingLeft: false
                });
                this.onSwipedRight((this._translateX._value / this.state.hiddenWidth) * 100);
                // if (this._translateX._value > this.props.leftOpenValue * (this.props.swipeToOpenPercent / 100)) {
                //     toValue = this.props.leftOpenValue;
                //     this.onSwipedRight(toValue);
                // }
            } else {
                this.setState({
                    ...this.state,
                    swipingLeft: true
                });
                this.onSwipedLeft((this._translateX._value / this.state.hiddenWidth) * 100);
                // if (this._translateX._value < this.props.rightOpenValue * (this.props.swipeToOpenPercent / 100)) {
                //     console.log(this._translateX._value + " " + this.props.rightOpenValue * (this.props.swipeToOpenPercent / 100));
                //     toValue = this.props.rightOpenValue;
                //     this.onSwipedLeft(toValue);
                // };
            };
        };
    };

    handlePanResponderEnd = (e, gestureState) => {
        if (this.props.enableScroll){
            this.props.enableScroll(true)
        }
        let value = (this._translateX._value / this.state.hiddenWidth) * 100;
        if (value > SWIPE_THRESHOLD_PERCENTAGE) {
            const { onSwipedRight } = this.props;
            this.manuallySwipeView(this.state.hiddenWidth).then(() => {
                if (onSwipedRight) {
                    onSwipedRight().then(() => {
                        //this.closeRow();
                    });
                }
            });

        } else if ((-1 * value) > SWIPE_THRESHOLD_PERCENTAGE) {
            const { onSwipedLeft } = this.props;
            this.manuallySwipeView(-this.state.hiddenWidth).then(() => {
                if (onSwipedLeft) {
                    onSwipedLeft().then(() => {
                        //this.closeRow();
                    });
                }
            });
        } else {
            //this.closeRow();
        }
        this.closeRow();
        // if (((this._translateX._value / this.state.hiddenWidth) * 100) < SWIPE_THRESHOLD_PERCENTAGE) {
        //     if (!this.horizontalSwipeGestureEnded) this.closeRow();
        // }
    };

    closeRow = () => {
        if (this.rowItemJustSwiped) {
            this.forceCloseRow();
        } else {
            this.manuallySwipeView(0);
        };
    };

    forceCloseRow = () => {
        Animated.timing(
            this._translateX,
            {
                duration: 0,
                toValue: 0,
            }
        ).start();
    };

    forceCloseToRight = () => {
        Animated.timing(
            this._translateX,
            {
                duration: 0,
                toValue: this.state.hiddenWidth,
            }
        ).start();
    };

    onSwipedLeft = (toValue: number) => {
        if (this.props.disableSwipeToLeft) {
            return;
        }
        // if ((toValue * -1) > SWIPE_THRESHOLD_PERCENTAGE) {
        //     const { onSwipedLeft } = this.props;
        //     this.manuallySwipeView(this.state.hiddenWidth).then(() => {
        //         if (onSwipedLeft) {
        //             onSwipedLeft().then(() => {
        //                 this.closeRow();
        //             });
        //         }
        //     });
        // }
    };

    onSwipedRight = (toValue: number) => {
        if (this.props.disableSwipeToRight) {
            return;
        }
        // if (toValue > SWIPE_THRESHOLD_PERCENTAGE) {
        //     const { onSwipedRight } = this.props;
        //     this.manuallySwipeView(this.state.hiddenWidth).then(() => {
        //         if (onSwipedRight) {
        //             onSwipedRight().then(() => {
        //                 this.closeRow();
        //             });
        //         }
        //     });
        // }
    };

    manuallySwipeView = (toValue) => {
        return new Promise((resolve, reject) => {
            Animated.timing(
                this._translateX,
                {
                    duration: this.props.swipeDuration,
                    toValue,
                }
            ).start(_ => {
                this.swipeInitialX = null;
                this.horizontalSwipeGestureBegan = false;
                this.horizontalSwipeGestureEnded = false;
                resolve();
            });
        });
    };

    renderVisibleContent = () => {
        return (
            this.props.renderVisibleContent()
        );
    };

    renderRowContent = () => {

        if (this.state.dimensionsSet) {
            return (
                <Animated.View
                    {...this._panResponder.panHandlers}
                    style={{
                        transform: [
                            { translateX: this._translateX }
                        ]
                    }}
                >
                    {this.props.children}
                </Animated.View>
            );
        } else {
            return (
                <Animated.View
                    {...this._panResponder.panHandlers}
                    onLayout={(e) => this.onContentLayout(e)}
                    style={{
                        transform: [
                            { translateX: this._translateX }
                        ]
                    }}
                >
                    {this.props.children}
                </Animated.View>
            );
        };
    };

    render() {
        return (
            <View>
                {/* <View style={[
                    styles.hidden,
                    {
                        height: this.state.hiddenHeight,
                        width: this.state.hiddenWidth,
                    }
                ]}>
                    {this.state.swipingLeft ?
                        ((this.props.renderRightView && this.props.renderRightView()) || null) :
                        ((this.props.renderLeftView && this.props.renderLeftView()) || null)}
                    </View>*/}
                {this.renderRowContent()}
            </View>
        );
    };

};

SwipeView.defaultProps = {
    swipeDuration: 250,
    disableSwipeToLeft: false,
    disableSwipeToRight: false,
    previewSwipeDemo: false,
    previewDuration: 300,
    previewOpenValue: -60,
    previewOpenDelay: 350,
    previewCloseDelay: 300,
    swipingLeft: true,
    recalculateHiddenLayout: false,
    directionalDistanceChangeThreshold: 2,
};