import * as React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';
import TestScreen from '../develop/TestScreen';
import LoadingPage from './LoadingPage';
import DetectedRestuarants from './DetectedRestuarants';
import MenuPage from './MenuPage';
import AdvancedStartupPage from './AdvancedStartupPage';
import ServerStartupPage from './ServerStartupPage';
import RestrauntList from './RestrauntList';
import LoginWithPasswordPage from './LoginWithPasswordPage';
import StationList from './StationList';
import BlankPage from './BlankPage';
import ChatPage from './ChatPage';
import LoginUserListPage from './LoginUserListPage';
import TaskOverviewPage from './TaskOverviewPage';
import OrderOverview from './OrderOverView';
import InventoryPage from './InventoryPage';
import SmarttoniWeb from './SmarttoniWeb';
import ShutdownPage from './ShutdownPage';
import RecipeLookUp from './RecipeLookUp';
import RecipeDetailPage from './RecipeDetailPage';
import PosPage from './PosPage';
import PrinterDetailPage from './DetailPage/PrinterDetailPage';
import DetailPage from './DetailPage/DetailPage';

import OrderDetailPage from './OrderDetailPage';
import ExternalOrderOverView from './ExternalOverView/ExternalOrderOverView';
import OrientationSelectionPage from './OrientationSelectionPage';

//import DetectedRestuarants from './DetectedRestuarants';



const Stack = createStackNavigator();

function AppNavigation() {
    return (
        <NavigationContainer>
            <Stack.Navigator screenOptions={{
                headerShown: false,
                animationEnabled: false 
            }}
            >
                <Stack.Screen name="LoadingPage" component={LoadingPage} />
                <Stack.Screen name="DetectedRestuarants" component={DetectedRestuarants} />
                <Stack.Screen name="AdvancedStartupPage" component={AdvancedStartupPage} />
                <Stack.Screen name="ServerStartupPage" component={ServerStartupPage} />
                <Stack.Screen name="RestrauntList" component={RestrauntList} />
                <Stack.Screen name="LoginUserListPage" component={LoginUserListPage} />
                <Stack.Screen name="LoginWithPasswordPage" component={LoginWithPasswordPage} />
                <Stack.Screen name="StationList" component={StationList} />
                <Stack.Screen name="BlankPage" component={BlankPage} />
                <Stack.Screen name="MenuPage" component={MenuPage} />

                <Stack.Screen name="ChatPage" component={ChatPage} />

                <Stack.Screen name="TaskOverview" component={TaskOverviewPage} />
                <Stack.Screen name="OrderOverview" component={OrderOverview} />
                <Stack.Screen name="OrientationSelectionPage" component={OrientationSelectionPage} />

                <Stack.Screen name="ExternalOrderOverView" component={ExternalOrderOverView} />

                <Stack.Screen name="OrderDetailPage" component={OrderDetailPage} />


                <Stack.Screen name="InventoryPage" component={InventoryPage} />
                <Stack.Screen name="RecipeLookUp" component={RecipeLookUp} />
                <Stack.Screen name="Recipe" component={RecipeDetailPage} />

                <Stack.Screen name="PosPage" component={PosPage} />

                <Stack.Screen name="DetailPage" component={DetailPage} />
                <Stack.Screen name="PrinterDetailPage" component={PrinterDetailPage} />


                <Stack.Screen name="SmarttoniWeb" component={SmarttoniWeb} />

                <Stack.Screen name="ShutdownPage" component={ShutdownPage} />

                {/* <Stack.Screen name="MenuPage" component={MenuPage} /> */}
            </Stack.Navigator>
        </NavigationContainer>
    );
}

export default AppNavigation;