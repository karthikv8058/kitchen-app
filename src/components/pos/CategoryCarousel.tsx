import AbstractComponent from '@components/AbstractComponent';
import leftArrow from '@components/assets/left.png';
import rightArrow from '@components/assets/right.png';
import colors from '@theme/colors';
import React from 'react';
import { Image, StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import Carousel from 'react-native-snap-carousel';

interface Props {
    onCategoryPressed: Function;
    recipeCategory: [];
}

interface State {
    recipeCategory: [];
}
export default class CategoryCarousel extends AbstractComponent<Props, State> {
    private categoryId = 0;
    constructor(props: Props) {
        super(props);
        this.state = {
            recipeCategory: this.props.recipeCategory
        };
        this.gotoPreviouspage = this.gotoPreviouspage.bind(this);
        this.gotoNextPage = this.gotoNextPage.bind(this);
        this.previousItem = this.previousItem.bind(this);
        this.nextItem = this.nextItem.bind(this);
    }

    componentWillReceiveProps(props) {
        const { recipeCategory } = this.props;
        this.setState({ recipeCategory });
    }

    gotoPreviouspage() {
        this._carousel.snapToPrev();
    }
    previousItem() {
        return (
            <TouchableOpacity
                onPress={this.gotoPreviouspage}>
                <Image
                    style={styles.imageContainer}
                    source={leftArrow}>
                </Image>
            </TouchableOpacity>
        );
    }
    gotoNextPage() {
        this._carousel.snapToNext();
    }
    nextItem() {
        return (
            <TouchableOpacity
                onPress={this.gotoNextPage}>
                <Image
                    style={styles.imageContainer}
                    source={rightArrow}>
                </Image>
            </TouchableOpacity>
        );
    }

    getTextStyle(catgeoryId: any, color: string) {
        return (this.categoryId === catgeoryId ? styles.itemSelected : styles.swiperItemContainer);
    }
    onItemClicked(catgeoryId: number) {
        this.categoryId = catgeoryId;
        this.props.onCategoryPressed(catgeoryId);
    }
    setListItem(item) {
        return (
            <View style={{ flexDirection: 'row', flex: 1, justifyContent: 'center', alignItems: 'stretch' }}>
                <View style={{ flex: 1 }}></View>
                <Text style={[this.getTextStyle(item.catgeoryId, item.color)]}
                    onPress={() => this.onItemClicked(item.catgeoryId)}>
                    {item.categoryName.trim()}
                </Text>
                <View style={{ flex: 1 }}></View>
                <View style={styles.lineContainer}></View>
            </View>
        );
    }

    render() {
        return (
            <View style={styles.allignBottom}>
                {this.previousItem()}
                <Carousel
                    style={{ height: '30', marginTop: 10 }}
                    ref={(c) => { this._carousel = c; }}
                    data={this.props.recipeCategory}
                    renderItem={(item: any) => this.setListItem(item.item)}
                    sliderWidth={120}
                    extraData={this.state}
                    itemWidth={120}
                    inactiveSlideOpacity={1}
                    inactiveSlideScale={1}
                    activeSlideAlignment={'start'}
                />
                {this.nextItem()}
            </View>
        );

    }
}

const styles = StyleSheet.create({
    imageContainer: {
        marginTop: 13,
    },
    lineContainer: {
        width: 1,
        backgroundColor: colors.white,
        marginBottom: 5,
        marginLeft: 4,
        marginRight: 4,
        marginTop: 18
    },
    itemSelected: {
        color: colors.white,
        height: 50,
        paddingTop: 20,
        fontWeight: 'bold',
        alignSelf: 'center',
    },
    swiperItemContainer: {
        color: colors.white,
        height: 50,
        paddingTop: 20,
        alignSelf: 'center',
    },
    allignBottom: {
        marginTop: 10,
        flexDirection: 'row',
        bottom: 0,
        height: '20%',
        paddingBottom: 20
    },
});
