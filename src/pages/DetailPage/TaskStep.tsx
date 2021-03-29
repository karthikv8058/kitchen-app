import AbstractComponent from "@components/AbstractComponent";
import React from "react";
import {
  View,
  Image,
  Text,
  Dimensions,
  NativeScrollEvent,
  NativeSyntheticEvent,
  StyleSheet,
} from "react-native";
import colors from "@theme/colors";
import { ScrollView } from "react-native-gesture-handler";
import placeHolder from "@components/assets/placeholder.png";
import FullWidthImage from "@components/FullWidthImage";
import TtsService from "@services/TtsService";
import IngredientWithQuantity from "@models/detail/IIngredientWithQuantity";
import ITaskStep from "@models/detail/ITaskStep";
import ITaskWithQuantity from "@models/detail/ITaskWithQuantity";
import IIngredientWithQuantity from "@models/detail/IIngredientWithQuantity";
import Work from "@models/Work";
import { ImageType } from "@models/ImageType";
import Orientation from "react-native-orientation-locker";

interface Props {
  workId: string;
  work?: Work;
  isIntervention: boolean;
  step: ITaskStep;
  recipeHeaderHeight: number;
  showQuantity: boolean;
  nextStep: Function;
  previousStep: Function;
  index: any;
  loadData: any;
}

interface State {
  screen: any;
  pageHeight: any;
  disableLeftSwipe: boolean;
  disableRightSwipe: boolean;
  nestedScrollEnabled: boolean;
  isEnd: boolean;
  scrollContentHeight: number;
  url: any;
  isPortrait: boolean;
}

class TaskStep extends AbstractComponent<Props, State> {
  private ttsService: TtsService = new TtsService();
  private swiping = false;
  private offset = 0;
  private scrollBeginsFrom = 0;

  constructor(props: Props) {
    super(props);
    this.state = {
      screen: Dimensions.get("window"),
      pageHeight: Dimensions.get("window").height,
      disableLeftSwipe: false,
      disableRightSwipe: false,
      nestedScrollEnabled: true,
      isEnd: false,
      scrollContentHeight: Dimensions.get("window").height,
      url: this.props.step.image,
      isPortrait: true,
    };
  }

  componentDidMount() {
    let disableLeftSwipe = false;
    let disableRightSwipe = false;
    if (!this.props.workId) {
      disableLeftSwipe = true;
      disableRightSwipe = true;
    } else if (!this.props.isIntervention) {
      disableLeftSwipe = true;
    }
    this.setState({
      pageHeight: this.state.screen.height - this.props.recipeHeaderHeight,
      disableLeftSwipe: disableLeftSwipe,
      disableRightSwipe: disableRightSwipe,
      url: this.props.step.image,
    });
  }

  componentWillUnmount() {
    this.ttsService.stop();
  }

  loadData(url: any) {
    this.setState({
      url: url,
    });
    this.props.loadData();
  }

  renderImage(fileName: any) {
    if (this.props.step.uuid === "1") {
      return null;
    }
    let type;
    if (this.props.index > 0) {
      type = ImageType.STEPS;
    } else if (this.props.isIntervention) {
      type = ImageType.INTERVENTION;
    } else {
      type = ImageType.TASK;
    }
    return (
      <FullWidthImage
        isFromRecipe={false}
        loadData={this.loadData.bind(this)}
        imageType={type}
        intervention={this.props.isIntervention}
        index={this.props.index}
        item={fileName}
        imageStyle={{ height: 150, width: 300 }}
        source={this.state.url}
      />
    );
  }

  renderQuantityAndUnit(data: IIngredientWithQuantity) {
    if (!this.props.showQuantity) return "";
    let symbol = data.recipe.outputUnits ? data.recipe.outputUnits.symbol : "";
    let value =
      this.props.work && data.recipe.outputQuantity
        ? data.quantity
        : data.quantity;
    let outputValue =
      symbol != undefined
        ? "(" + value + " " + symbol + ")"
        : "(" + value + ")";

    return outputValue;
  }

  renderTaskQuantityAndUnit(data: ITaskWithQuantity) {
    if (!this.props.showQuantity) return "";
    let symbol = data.task.unit ? data.task.unit.symbol : "";
    let value = data.task.outputQuantity
      ? data.task.outputQuantity * data.quantity
      : data.quantity;
    let outputValue =
      symbol != undefined
        ? "(" + value + " " + symbol + ")"
        : "(" + value + ")";
    return outputValue;
  }

  renderIngredientImage = (image: string | undefined) => {
    let img = image
      ? { uri: this.apiBuilder.paths!.imageUrl + image }
      : placeHolder;
    return (
      <Image
        style={{
          height: 50,
          width: 50,
          marginTop: 5,
          borderRadius: 5,
          borderWidth: 0.1,
          borderColor: colors.toniTheme,
        }}
        source={img}
      />
    );
  };

  renderIngredients() {
    let hasIngredients =
      this.props.step.ingredients && this.props.step.ingredients.length;
    let hasTasks = this.props.step.tasks && this.props.step.tasks.length;

    return (
      <>
        {hasTasks ? (
          <>
            <Text>Prepared Items:</Text>
            {this.props.step.tasks.map(
              (data: ITaskWithQuantity, key: number) => {
                return (
                  <View key={key} style={styles.ingredientContainer}>
                    {this.renderIngredientImage(data.task.image)}
                    <Text style={styles.ingredientText}>{data.task.name}</Text>
                  </View>
                );
              }
            )}
          </>
        ) : null}
        {hasIngredients ? (
          <>
            <Text style={{ fontWeight: "bold" }}>Ingredients:</Text>
            {this.props.step.ingredients.map(
              (data: IngredientWithQuantity, key: number) => {
                return (
                  <View key={key} style={styles.ingredientContainer}>
                    {this.renderIngredientImage(data.image)}
                    <Text style={styles.ingredientText}>
                      {data.name} ({data.qty})
                    </Text>
                  </View>
                );
              }
            )}
          </>
        ) : null}
      </>
    );
  }
  scrollBegins(event: NativeSyntheticEvent<NativeScrollEvent>) {
    this.scrollBeginsFrom = event.nativeEvent.contentOffset.y;
  }

  handleScroll(event: NativeSyntheticEvent<NativeScrollEvent>) {
    let scrollEnds = event.nativeEvent.contentOffset.y;
    if (scrollEnds == this.scrollBeginsFrom) {
      if (this.scrollBeginsFrom == 0) {
        this.props.previousStep();
      } else {
        this.props.nextStep();
      }
    }
  }
  onLayout() {
    Orientation.getOrientation((orientation) => {
      if (orientation === "PORTRAIT") {
        this.setState({
          isPortrait: true,
          pageHeight: Dimensions.get("window").height,
        });
      } else {
        this.setState({
          isPortrait: false,
          pageHeight: Dimensions.get("window").height,
        });
      }
    });
  }

  render() {
    return (
      <ScrollView
        onLayout={this.onLayout.bind(this)}
        style={{ maxHeight: this.state.pageHeight, flex: 1 }}
        onScrollBeginDrag={this.scrollBegins.bind(this)}
        onMomentumScrollEnd={this.handleScroll.bind(this)}
        onContentSizeChange={(a, b) =>
          this.setState({ scrollContentHeight: b })
        }
        nestedScrollEnabled={false}
      >
        <View
          style={{
            flexDirection: !!this.state.isPortrait ? "column" : "row",
            flex: 1,
          }}
        >
          <View
            style={{
              flex: 5,
              flexDirection: "column",
            }}
          >
            {this.props.step.name ? (
              <Text
                style={{
                  fontSize: 16,
                  fontWeight: "bold",
                  paddingHorizontal: 15,
                  marginTop: 5,
                }}
              >
                {this.props.step.name}
              </Text>
            ) : null}
            {this.props.step.description ? (
              <Text style={styles.taskDescription}>
                {this.props.step.description}
              </Text>
            ) : null}
            {
              <View
                style={{
                  paddingLeft: 25,
                  paddingRight: 5,
                  paddingBottom: 10,
                }}
              >
                {this.props.work != null && this.props.work.wishes != null && this.props.work.wishes.length > 0 &&
                  this.props.work.wishes.map((item: any) => {
                    return (
                      <Text
                        key={item}
                        style={{
                          color: "red",
                          fontSize: 10,
                        }}
                      >
                        {`\u2022 ${item}`}
                      </Text>
                    );
                  })}
              </View>
            }
            {this.renderImage(this.props.step)}
          </View>
          <View
            style={[
              styles.secondColumn,
              { marginTop: this.state.isPortrait ? 0 : 5 },
            ]}
          >
            {this.renderIngredients()}
          </View>
        </View>
      </ScrollView>
    );
  }
}

const styles = StyleSheet.create({
  backgroundVideo: {
    flex: 1,
    marginVertical: 8,
  },
  ingredientText: {
    flex: 1,
    marginHorizontal: 2,
    paddingLeft: 2,
  },
  mainContainer: {
    flexDirection: "row",
    backgroundColor: colors.white,
  },
  firstColumn: {
    flex: 1,
    marginLeft: 10,
    flexDirection: "column",
    padding: 10,
  },
  secondColumn: {
    marginLeft: 50,
    width: 200,
  },
  image: {
    minWidth: 100,
    minHeight: 100,
    margin: 8,
    flex: 1,
    marginTop: 10,
  },
  imagePlaceHolder: {
    minWidth: 50,
    minHeight: 50,
    margin: 8,
    flex: 1,
    marginTop: 10,
  },
  taskDescription: {
    marginTop: 10,
    marginHorizontal: 15,
    marginBottom: 10,
  },
  ingredientContainer: {
    flexDirection: "row",
    alignItems: "center",
    marginTop: 10,
  },
});

export default TaskStep;
