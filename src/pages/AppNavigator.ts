import AdvancedStartupPage from '@pages/AdvancedStartupPage';
import PrinterDetailPage from '@pages/DetailPage/PrinterDetailPage';
import TaskDetail from '@pages/DetailPage/DetailPage';

import DetectedRestuarants from '@pages/DetectedRestuarants';
import InterventionTaskPage from '@pages/InterventionTaskPage';
import InventoryManagementPage from '@pages/InventoryPage';
import LoadingPage from '@pages/LoadingPage';
import LoginUserListPage from '@pages/LoginUserListPage';
import LoginWithPasswordPage from '@pages/LoginWithPasswordPage';
import MenuPage from '@pages/MenuPage';
import OrderDetailPage from '@pages/OrderDetailPage';
import OrderOverView from '@pages/OrderOverView/OrderOverView';
import PosPage from '@pages/PosPage';
import Recipe from '@pages/RecipeDetailPage';
import RecipeLookUp from '@pages/RecipeLookUp';
import RestrauntList from '@pages/RestrauntList';
import ServerStartupPage from '@pages/ServerStartupPage';
import StationList from '@pages/StationList';
import BlankPage from '@pages/BlankPage';
import TaskOverviewPage from '@pages/TaskOverviewPage';
import { Animated, Easing } from 'react-native';
import { createAppContainer } from 'react-navigation';
import { createStackNavigator } from 'react-navigation-stack';
import SmarttoniWeb from '@pages/SmarttoniWeb';
import ChatPage from './ChatPage';
import PrinterMessageView from './PrinterMessageView';
import ShutdownPage from './ShutdownPage';
import ExternalOrderOverView from '@pages/ExternalOverView/ExternalOrderOverView'
import OrientationSelectionPage from './OrientationSelectionPage';

const navigationOptions = {
  title: 'Back',
  headerTintColor: 'white',
  headerTitleStyle: { color: 'white' },
  gesturesEnabled: false,
  gestureResponseDistance: { horizontal: 1000 },
  headerStyle: {
    backgroundColor: '#00767C',
    borderBottomColor: 'white',
  },
  header: null
};

let getNavigationOptions = function (options: any = undefined) {
  if (options === undefined) { options = navigationOptions; }
  if (options.title === undefined) { options.title = navigationOptions.title; }
  if (options.headerTintColor === undefined) { options.headerTintColor = navigationOptions.headerTintColor; }
  if (options.headerTitleStyle === undefined) { options.headerTitleStyle = navigationOptions.headerTitleStyle; }
  if (options.gesturesEnabled === undefined) { options.gesturesEnabled = navigationOptions.gesturesEnabled; }
  if (options.gestureResponseDistance === undefined) { options.gestureResponseDistance = navigationOptions.gestureResponseDistance; }
  if (options.headerStyle === undefined) { options.headerStyle = navigationOptions.headerStyle; }
  if (options.header === undefined) { options.header = navigationOptions.header; }
  return options;
};

let CollapseExpand = (index, position) => {
  const inputRange = [index - 1, index, index + 1];
  const opacity = position.interpolate({
    inputRange,
    outputRange: [0, 1, 1],
  });

  const scaleY = position.interpolate({
    inputRange,
    outputRange: ([0, 1, 1]),
  });

  return {
    opacity,
    transform: [
      { scaleY }
    ]
  };
};

let SlideFromRight = (index, position, width) => {
  const translateX = position.interpolate({
    inputRange: [index - 1, index, index + 1],
    outputRange: [width, 0, 0]
  });
  const slideFromRight = { transform: [{ translateX }] };
  return slideFromRight;
};

const TransitionConfiguration = () => {
  return {
    transitionSpec: {
      duration: 100,
      easing: Easing.out(Easing.poly(4)),
      timing: Animated.timing,
      useNativeDriver: true,
    },
    screenInterpolator: (sceneProps) => {
      const { layout, position, scene } = sceneProps;
      const width = layout.initWidth;
      const { index, route } = scene;
      const params = route.params || {}; // <- That's new
      const transition = params.transition || 'default'; // <- That's new
      return {
        collapseExpand: CollapseExpand(index, position),
        default: SlideFromRight(index, position, width),
      }[transition];
    },
  };
};

export const navigator = {
  LoadingPage: {
    screen: LoadingPage,
    navigationOptions: {
      header: null
    }
  },
  DetectedRestuarants: {
    screen: DetectedRestuarants,
    navigationOptions: {
      header: null
    }
  },
  AdvancedStartupPage: {
    screen: AdvancedStartupPage,
    navigationOptions: {
      header: null
    }
  },
  ServerStartupPage: {
    screen: ServerStartupPage,
    navigationOptions: {
      header: null
    }
  },
  RestrauntList: {
    screen: RestrauntList,
    navigationOptions: {
      header: null
    }
  },
  LoginUserListPage: {
    screen: LoginUserListPage,
    navigationOptions: {
      header: null
    }
  },
  LoginWithPasswordPage: {
    screen: LoginWithPasswordPage,
    navigationOptions: getNavigationOptions()
  },
  StationList: {
    screen: StationList,
    navigationOptions: {
      header: null
    }
  },
  BlankPage: {
    screen: BlankPage,
    navigationOptions: {
      header: null
    }
  },
  MenuPage: {
    screen: MenuPage,
    navigationOptions: getNavigationOptions({ gesturesEnabled: true })
  },
  TaskOverview: {
    screen: TaskOverviewPage,
    navigationOptions: {
      header: null
    }
},
  PosPage: {
    screen: PosPage,
    navigationOptions: getNavigationOptions()
  },
  DetailPage: {
    screen: TaskDetail,
    animationEnabled: false,
    gesturesEnabled: false,
    navigationOptions: getNavigationOptions({ gesturesEnabled: false })
  },
  PrinterDetailPage: {
    screen: TaskDetail,
    animationEnabled: false,
    gesturesEnabled: false,
    navigationOptions: getNavigationOptions({ gesturesEnabled: false })
  },
  OrderOverView: {
    screen: OrderOverView,
    navigationOptions: {
      header: null
    }
  },
  RecipeLookUp: {
    screen: RecipeLookUp,
    navigationOptions: {
      header: null
    }
  },
  Recipe: {
    screen: Recipe,
    navigationOptions: {
      header: null
    }
  },
  InventoryManagementPage: {
    screen: InventoryManagementPage,
    navigationOptions: {
      header: null
    },
  },
  PrinterMessage: {
    screen: PrinterMessageView,
    navigationOptions: {
      header: null
    }
  },
  OrderDetailPage: {
    screen: OrderDetailPage,
    navigationOptions: {
      header: null
    }
  },
  
  InterventionTaskPage: {
    screen: InterventionTaskPage,
    navigationOptions: {
      header: null
    }
  },

  ChatPage: {
    screen: ChatPage,
    navigationOptions: {
      header: null
    }
  },
  SmarttoniWeb: {
    screen: SmarttoniWeb,
    navigationOptions: {
      header: null
    }
  },ExternalOrderOverView: {
    screen: ExternalOrderOverView,
    navigationOptions: {
      header: null
    }
  },
  ShutdownPage: {
    screen: ShutdownPage,
    navigationOptions: {
      header: null
    }
  },
  OrientationSelectionPage: {
    screen: OrientationSelectionPage,
    navigationOptions: {
      header: null
    }
  },
 
};

const AppNavigator = createStackNavigator(
  navigator, {
  initialRouteName: 'LoadingPage',
  gesturesEnabled: false,
  transitionConfig: TransitionConfiguration,

}
);

export default createAppContainer(AppNavigator);
