import AbstractComponent from "@components/AbstractComponent";
import React from "react";
import HttpClient from "@services/HttpClient";
import ioc, { HTTP_CLIENT, Bind } from "../../ioc/ServiceContainer";
import {
  Text,
  View,
  StyleSheet,
  Dimensions,
  ActivityIndicator,
  GestureResponderEvent,
  TouchableOpacity,
} from "react-native";
import { connect } from "react-redux";
import Appbackground from "../../components/AppBackground";
import CheckingTask from "@components/CheckingTask";
import Carousel from "react-native-snap-carousel";
import TaskStep from "./TaskStep";
import VoiceRecognitionComponent, {
  VOICE_ACTIONS,
} from "../../components/VoiceRecognitionComponent";
import StorageService, { Storage, StorageKeys } from "@services/StorageService";
import { getFontContrast } from "@components/FontColorHelper";
import Icon from "react-native-vector-icons/Entypo";
import colors from "@theme/colors";
import ConfirmationDialog from "@components/ConfirmationDialog";
import InterventionService from "@services/InterventionService";
import { ManualPages } from "@components/Manual";
import EventEmitterService, { EventTypes } from "@services/EventEmitterService";
import IDetailPage from "@models/detail/IDetailPage";
import ITaskStep from "@models/detail/ITaskStep";
import TtsService from "@services/TtsService";
import SwipeView from "@components/SwipeView";
import TaskService from "@services/TaskService";
import InterventionJob from "@models/InterventionJob";
import RNBeep from "react-native-a-beep";
import Orientation from "react-native-orientation-locker";

interface Props {
  navigation: any;
  priorityTaskcount: number;
  userId: string;
  stepAutoOpen: boolean;
  route: any;
}

interface State {
  isIntervention: boolean;
  isBackButtonVisible: boolean;
  taskId: any;
  items?: IDetailPage;
  content: IDetailPage;
  screen: any;
  workId: any;
  currentPage: number;
  interventionJob?: InterventionJob;
  isLoading: boolean;
  recipeHeaderHeight: any;
  showUpIcon: boolean;
  showDownIcon: boolean;
  showInterventionReductionMessage: boolean;
  bottomIcons: any[] | null;
  isPortrait: boolean;
}

class DetailPage extends AbstractComponent<Props, State> {
  private httpClient: HttpClient = Bind(HTTP_CLIENT);
  private storageService: StorageService = Bind("storageService");
  private interventionService: InterventionService = Bind(
    "interventionService"
  );
  private eventEmitterService: EventEmitterService = Bind(
    "eventEmitterService"
  );
  private taskService: TaskService = Bind("taskService");

  private ttsService: TtsService = new TtsService();

  private vrComponent?: VoiceRecognitionComponent;
  private isTTSOn = this.storageService.getFast(StorageKeys.READ_ALL)
    ? true
    : false;
  private readDescriptionInDetail = this.storageService.getFast(
    StorageKeys.READ_DETAIL_DESCRIPTION
  )
    ? true
    : false;
  private readIngredientInDetail = this.storageService.getFast(
    StorageKeys.READ_DETAIL_INGREDIENT
  )
    ? true
    : false;
  private stepAutoOpen = this.storageService.getFast(StorageKeys.AUTO_OPEN_STEP)
    ? true
    : false;

  private isCompletedTask: boolean = false;

  private _carousel?: Carousel<ITaskStep>;

  private checkingTask?: CheckingTask;

  constructor(props: Props) {
    super(props);
    this.state = {
      isIntervention: false,
      taskId: 0,
      screen: Dimensions.get("window"),
      workId: 0,
      isBackButtonVisible: true,
      currentPage: 0,
      isLoading: true,
      recipeHeaderHeight: 0,
      showUpIcon: false,
      showDownIcon: false,
      showInterventionReductionMessage: false,
      bottomIcons: null,
      isPortrait: false,
    };
  }

  componentDidMount() {
    let taskId = this.props.route?.params?.taskId;
    let isIntervention = this.props.route?.params?.isIntervention;
    let workId = this.props.route?.params?.workId;

    let isBackButtonVisible = true;
    if (isIntervention && workId) {
      RNBeep.PlaySysSound(RNBeep.AndroidSoundIDs.TONE_CDMA_ABBR_ALERT);
      isBackButtonVisible = false;
    }
    let recipeHeaderHeight = 0;

    this.setState(
      {
        recipeHeaderHeight: recipeHeaderHeight,
        isIntervention: isIntervention,
        workId: workId,
        isBackButtonVisible: isBackButtonVisible,
      },
      () => {
        this.showOrHideManualIcon(0);
      }
    );

    this.httpClient
      .post(this.apiBuilder.paths!.getTask, {
        taskId: taskId ? taskId : "",
        isIntervention: isIntervention ? true : false,
        workId: workId ? workId : 0,
      })
      .then((response: IDetailPage) => {
        if (response.station) {
          recipeHeaderHeight = 50;
        }
        this.setState(
          {
            recipeHeaderHeight: recipeHeaderHeight,
            items: response,
            content: response,
            isLoading: false,
            showDownIcon: response != null && response.steps.length > 1,
          },
          () => {
            setTimeout(() => {
              if (this._carousel) {
                if (this.stepAutoOpen && this.state.workId !== 0) {
                  this._carousel.snapToItem(1);
                }
                if (response.steps.length === 1 || !this.stepAutoOpen) {
                  this.onSnapToItem(0);
                }
              }
            }, 1000);
          }
        );
      })
      .catch((err) => {
        console.log(err);
      });

    if (this.state.workId !== 0 && this.vrComponent) {
      this.vrComponent.addVoiceRecognitionListener();
    }
    if (this.state.isIntervention) {
      this.storageService.set(Storage.CURRENT_INTERVENTION, this.state.workId);
    }
  }

  showOrHideManualIcon = (index: number = 0) => {
    const page = index === 0 ? ManualPages.DETAIL : ManualPages.STEP;
    this.storageService.get(Storage.USER_ID).then((userId) => {
      this.storageService.getObject(Storage.MANUAL).then((object) => {
        let already = object[userId] && object[userId][page] === "1";
        if (!already) {
          this.setState({
            bottomIcons: [
              {
                icon: "help-circle",
                onClick: () => {
                  let extras = {
                    close: this.onCloseManualPages,
                    isIntervention: this.state.isIntervention,
                  };
                  this.eventEmitterService.emit({
                    type: EventTypes.Manual,
                    data: page,
                    extras,
                  });
                },
              },
            ],
          });
        } else {
          this.setState({ bottomIcons: null });
        }
      });
    });
  };

  onCloseManualPages = (dontShowAgain: boolean) => {
    if (dontShowAgain) {
      this.setState({
        bottomIcons: null,
      });
    }
  };

  readItemsOnLoad(index: number = 0) {
    if (this.isTTSOn && this.state.workId !== 0) {
      if (this.readDescriptionInDetail && this.readIngredientInDetail) {
        this.readDescriptionAndIngredients(true, true);
      } else if (this.readDescriptionInDetail) {
        this.readDescriptionAndIngredients(true, false);
      } else {
        this.readDescriptionAndIngredients(false, true);
      }
    }
  }

  componentWillUnmount() {
    this.storageService.unset(Storage.CURRENT_INTERVENTION);
    this.storageService.set(
      Storage.LAST_INTERVENTION_TIME,
      new Date().getTime()
    );

    this.eventEmitterService.emit({
      type: EventTypes.Manual,
      data: ManualPages.CLOSE,
    });
    if (this.state.workId !== 0 && this.vrComponent) {
      this.vrComponent.removeVoiceRecognitionListener();
    }
    this.eventEmitterService.emit({ type: EventTypes.SYNC_TASK });
  }

  onSnapToItem = (index: number) => {
    this.setState(
      {
        currentPage: index,
        showDownIcon:
          this.state.items !== null &&
          this.state.items?.steps.length - 1 !== index,
        showUpIcon: index !== 0,
      },
      () => {
        this.readItemsOnLoad(index);
        this.showOrHideManualIcon(index);
      }
    );
  };

  completeTask = () => {
    if (!this.state.isIntervention) {
      if (this.state.workId !== 0 && this.isCompletedTask) {
        return;
      }
      this.isCompletedTask = true;
      return this.taskService.completeTask(this.state.workId).then((r) => {
        this.isCompletedTask = false;
        this.props.navigation.pop();
      });
    } else if (this.state.isIntervention && this.state.workId !== 0) {
      if (
        this.state?.content?.meta != null &&
        this.interventionService.checkForInterventionReductionMesssage(
          this.state?.content?.meta
        )
      ) {
        this.setState({
          showInterventionReductionMessage: true,
        });
      } else {
        this.closeIntervention();
      }
    }
  };

  previousStep = () => {
    if (this._carousel) {
      this._carousel.snapToPrev();
    }
  };

  nextStep = () => {
    if (this._carousel) {
      this._carousel.snapToNext();
    }
  };

  renderRecipeName() {
    let content = this.state.content;
    if (content && content.station) {
      return (
        <View
          style={{
            backgroundColor: content.station.color,
            flexDirection: "row",
            minHeight: this.state.recipeHeaderHeight,
            alignItems: "center",
          }}
        >
          <Text
            style={{
              color: getFontContrast(content.station.color),
              marginLeft: 15,
            }}
          >
            {content.recipe?.name}
          </Text>
          <Text
            style={{
              color: getFontContrast(content.station.color),
              flex: 1,
              textAlign: "right",
              marginRight: 10,
            }}
          >
            {this.state.currentPage == 0
              ? "Overview"
              : "Step " +
                this.state.currentPage.toString() +
                " of " +
                (content.steps.length - 1).toString()}
          </Text>
        </View>
      );
    } else {
      return null;
    }
  }

  loadData() {
    this.componentDidMount();
  }

  renderPage(taskStep: ITaskStep, index: number) {
    return (
      <TaskStep
        loadData={this.loadData.bind(this)}
        index={this.state.currentPage}
        step={taskStep}
        work={this.state.content?.work}
        showQuantity={index === 0}
        nextStep={this.nextStep}
        previousStep={this.previousStep}
        recipeHeaderHeight={this.state.recipeHeaderHeight}
        workId={this.state.workId}
        isIntervention={this.state.isIntervention}
      />
    );
  }

  goBack = () => {
    this.storageService.unset(Storage.CURRENT_INTERVENTION);
    this.props.navigation.pop();
  };

  showTimePicker = () => {
    return new Promise((resolve, reject) => {
      if (this.state.isIntervention) {
        this.refs.checkingTask.showCheckingTask({
          task: this.state.content?.parentTask,
          id: this.state.workId,
          name: this.state.content?.name,
          interventionJob: this.state.interventionJob,
        });
      }
      resolve(true);
    });
  };

  gotoFirstStep = () => {
    this._carousel?.snapToItem(1);
  };

  readDescriptionAndIngredients = (
    readDescription: boolean,
    readIngredient: boolean
  ) => {
    const current = this.state.content?.steps[this.state.currentPage];
    if (!current) {
      return;
    }
    let content = readDescription && current.name ? current.name : "";
    content +=
      " " + readDescription && current.description ? current.description : "";
    content += readIngredient ? "\n" + this.getIngredientsToRead(current) : "";

    this.ttsService.isTtsReady().then((a) => {
      this.ttsService.read(content);
    });
  };

  getIngredientsToRead = (page: ITaskStep) => {
    let textToRead = "";
    if (page.ingredients && page.ingredients.length > 0) {
      textToRead = "Ingredients \n";
      page.ingredients.forEach((ingredients) => {
        textToRead += `${ingredients.quantity} +
          " " +
          ${ingredients.recipe?.outputUnits?.name} +
          " " +
          ${ingredients?.recipe?.name} +
          "\n"`;
      });
    }
    return textToRead;
  };

  closeIntervention = (reduceValue: boolean = false) => {
    if (!this.state.workId) {
      return;
    }
    this.setState({
      showInterventionReductionMessage: false,
    });
    return this.taskService
      .completeIntervention(this.state.workId, reduceValue)
      .then((response) => {
        this.props.navigation.pop();
      });
  };

  closeInterventionAndReduceValue = () => {
    this.closeIntervention(true);
  };

  onRecognizeVoice = (action: VOICE_ACTIONS) => {
    switch (action) {
      case VOICE_ACTIONS.DONE:
        this.completeTask();
        break;
      case VOICE_ACTIONS.STEP_BY_STEP:
        this.gotoFirstStep();
        break;
      case VOICE_ACTIONS.READ_INGREDIENTS:
        this.readDescriptionAndIngredients(false, true);
        break;
      case VOICE_ACTIONS.DETAILS:
        this.readDescriptionAndIngredients(true, false);
        break;
      case VOICE_ACTIONS.READ_TASK:
        this.readDescriptionAndIngredients(true, true);
        break;
      case VOICE_ACTIONS.PREVIOUS:
        this.previousStep();
        break;
      case VOICE_ACTIONS.NEXT:
        this.nextStep();
        break;
      case VOICE_ACTIONS.SHOW_TASK:
        this.goBack();
        break;
    }
  };

  renderPriorityCount() {
    if (!this.state.isIntervention) {
      return (
        <View
          style={{
            position: "absolute",
            alignItems: "center",
            justifyContent: this.state.isPortrait ? "flex-end" : "center",
            right: this.state.isPortrait ? this.state.screen.width / 2.5 : 20,
            top: 0,
            bottom: this.state.isPortrait ? 25 : 0,
            elevation: 6,
          }}
        >
          {this.props.priorityTaskcount ? (
            <TouchableOpacity
              onPress={this.goBack}
              style={{
                width: 70,
                height: 45,
                backgroundColor: "red",
                justifyContent: "center",
                borderRadius: 5,
              }}
            >
              <Text
                style={{
                  textAlign: "center",
                  textAlignVertical: "center",
                  color: colors.white,
                }}
              >
                {this.props.priorityTaskcount}
              </Text>
            </TouchableOpacity>
          ) : null}
        </View>
      );
    } else {
      return <View></View>;
    }
  }
  onLayout = () => {
    Orientation.getOrientation((orientation) => {
      if (orientation === "PORTRAIT") {
        this.setState({
          isPortrait: true,
          screen: Dimensions.get("window"),
        });
      } else {
        this.setState({
          isPortrait: false,
          screen: Dimensions.get("window"),
        });
      }
    });
  };
  render() {
    return (
      <Appbackground
        hideBack={
          this.state.workId === 0 ? false : !this.state.isBackButtonVisible
        }
        doNaviagte={!this.state.isBackButtonVisible}
        navigation={this.props.navigation}
        bottomMenu={this.state.bottomIcons}
      >
        <ConfirmationDialog
          show={this.state.showInterventionReductionMessage}
          message="Should smartTONi remind you after the current elapsed time for the future?"
          onComplete={this.closeInterventionAndReduceValue}
          onClose={this.closeIntervention}
        />
        <CheckingTask
          ref="checkingTask"
          pendingCallback={() => {
            this.goBack();
          }}
          intervention={this.state.workId}
        />
        <VoiceRecognitionComponent
          onRef={(ref) => {
            this.vrComponent = ref;
          }}
          onRecognizeVoice={this.onRecognizeVoice}
        />
        {this.state.isLoading ? (
          <ActivityIndicator
            style={{ flex: 1 }}
            size="large"
            color={colors.white}
          />
        ) : (
          <View
            onLayout={this.onLayout.bind(this)}
            style={{
              flex: 1,
              flexDirection: "column",
              alignSelf: "stretch",
              backgroundColor: "#fff",
            }}
          >
            {this.renderRecipeName()}
            <SwipeView
              onSwipedLeft={this.showTimePicker}
              onSwipedRight={this.completeTask}
              disableSwipeToLeft={
                !(this.state.isIntervention && this.state.workId)
              }
              disableSwipeToRight={!this.state.workId}
            >
              <Carousel
                ref={(c: any) => {
                  this._carousel = c;
                }}
                data={this.state.content.steps}
                renderItem={(item: any, index: number) =>
                  this.renderPage(item.item, item.index)
                }
                sliderWidth={this.state.screen.width}
                sliderHeight={this.state.screen.height}
                itemHeight={this.state.screen.height}
                windowSize={this.state.screen.height}
                onSnapToItem={this.onSnapToItem}
                initialScrollIndex={
                  this.state.content.steps.length > 1 &&
                  this.stepAutoOpen &&
                  this.state.workId != 0
                    ? 1
                    : 0
                }
                extraData={this.state}
                inactiveSlideOpacity={1}
                inactiveSlideScale={1}
                activeSlideAlignment={"start"}
                vertical={true}
                enableMomentum={false}
              />
            </SwipeView>
          </View>
        )}
        <PagerIcon
          icon="chevron-up"
          containerStyle={{ top: 40 }}
          onClick={this.previousStep}
          show={this.state.showUpIcon}
        />
        <PagerIcon
          icon="chevron-down"
          containerStyle={{ bottom: this.state.isPortrait ? 40 : 0 }}
          onClick={this.nextStep}
          show={this.state.showDownIcon}
        />
        {this.state.workId ? this.renderPriorityCount() : <View />}
      </Appbackground>
    );
  }
}

const mapStateToProps = (state: any) => {
  return {
    ip: state.appState.ip,
    priorityTaskcount: state.task.priorityTaskcount,
    userId: state.appState.userId,
  };
};

export default connect(mapStateToProps, null)(DetailPage);

interface PagerIconProps {
  show: boolean;
  onClick: ((event: GestureResponderEvent) => void) | undefined;
  icon: string;
  containerStyle?: any;
}

const PagerIcon = (props: PagerIconProps) => {
  return (
    <View
      style={[
        {
          flex: 1,
          position: "absolute",
          justifyContent: "center",
          alignItems: "center",
          left: 0,
          right: 0,
          zIndex: 99,
          minHeight: 40,
        },
        props.containerStyle,
      ]}
    >
      {props.show && (
        <TouchableOpacity onPress={props.onClick}>
          <Icon
            style={{ alignSelf: "center" }}
            name={props.icon}
            size={30}
            color={colors.darkGrey}
          />
        </TouchableOpacity>
      )}
    </View>
  );
};

const styles = StyleSheet.create({
  mainContainer: {
    flex: 1,
    flexDirection: "column",
  },
  touchableOpacityStyle: {
    position: "absolute",
    alignItems: "center",
    justifyContent: "center",
    right: 20,
    top: 0,
    bottom: 0,
    elevation: 6,
  },
  floatingButtonStyle: {
    resizeMode: "contain",
    width: 70,
    height: 45,
    backgroundColor: "red",
    justifyContent: "center",
    borderRadius: 5,
  },
});
