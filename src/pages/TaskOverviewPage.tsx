import AbstractComponent from '@components/AbstractComponent';
import AppBackground from '@components/AppBackground';
import TaskAssignmentConfirmation from '@components/TaskAssignmentConfirmation';
import Task from '@components/Task';
import Station from '@models/Station';
import Work, { WORK_STARTED, WORK_QUEUED } from '@models/Work';
import VoiceRecognition from '@native/VoiceRecognition';
import EventEmitterService, { EventTypes } from '@services/EventEmitterService';
import TaskService from '@services/TaskService';
import colors from '@theme/colors';
import t from '@translate';
import React from 'react';
import {
    FlatList,
    StyleSheet,
    Text,
    ToastAndroid,
    View,
    TouchableOpacity,
    GestureResponderEvent,
    Dimensions,
    Alert,
    Animated,
    PanResponder,
    AsyncStorage,
    Image
} from 'react-native';
import PTRView from 'react-native-pull-to-refresh';
import { Bind } from '../ioc/ServiceContainer';
import { menuCategory, MY_TASKS, TRIGGER_PHRASE } from '../utils/constants';
import VoiceService from '@services/VoiceService';
import Painter from '@services/Painter';
import StorageService, { StorageKeys, Storage } from '@services/StorageService';
import { ManualPages } from '@components/Manual';
import Icon from 'react-native-vector-icons/Feather';
import Icons from 'react-native-vector-icons/AntDesign';
import FilterStations from '@components/FilterStations';
import StationService from '@services/StationService';
import TaskFilter from '@models/TaskFilter';
import { connect } from 'react-redux';
import { setHighPriorityTaskCount } from '../redux/actions/TaskAction';
import NavigationService from '@services/NavigationService';
import Orientation from 'react-native-orientation-locker';
import Splitter from '@components/Splitter';

interface Props {
    navigation: any;
    setHighPriorityTaskCount: (count: number) => void;
}

interface State {
    queue: Work[];
    chefQueue: Work[];
    machineQueue: Work[];
    showAssignmentConfirmation: boolean;
    filter: TaskFilter;
    bottomMenu?: { icon: string; onClick: ((event: GestureResponderEvent) => void) | undefined }[] | null;
    offset: number,
    topHeight: number,
    bottomHeight: number,
    deviceHeight: number,
    deviceWidth: number,
    isDividerClicked: boolean,
    pan: any
}

const LIKELY_HOOD_LIMIT = 600000;

class TaskOverviewPage extends AbstractComponent<Props, State> {
    pushListener = { type: 1, callback: (data: string) => { this.getTasks(); } };

    private voiceRecognition: VoiceRecognition = new VoiceRecognition();
    private voiceService: VoiceService = Bind('voiceService');
    private eventEmitterService: EventEmitterService = Bind('eventEmitterService');
    private taskService: TaskService = Bind('taskService');
    private stationService: StationService = Bind('stationService');
    private storageService: StorageService = Bind('storageService');
    private navigationService: NavigationService = Bind('navigationService');
    private painter: Painter = Bind('painter');
    private isTTSOn = this.storageService.getFast(StorageKeys.READ_ALL);
    private autoOpenTask = this.storageService.getFast(StorageKeys.AUTO_OPEN_TASK) ? true : false;
    private readOnAssign = !this.autoOpenTask && this.storageService.getFast(StorageKeys.READ_ON_ASSIGN) ? true : false;
    private isVROn = this.storageService.getFast(StorageKeys.VOICE_RECOGNITION);

    private taskAssignmentConfirmation?: TaskAssignmentConfirmation;
    private filterModal?: FilterStations;
    private stations: Station[] = [];
    private currentUserId = '';
    private assignedTask?: Work | null;
    private pageRefreshInterval?: number;
    private tooblarMenu: any;
    private _panResponder: any;

    constructor(props: Props) {
        super(props);

        let filter: string = this.storageService.getFast(StorageKeys.TASKK_FILTER);

        this.state = {
            queue: [],
            chefQueue: [],
            machineQueue: [],
            showAssignmentConfirmation: false,
            filter: filter ? JSON.parse(filter) : {
                showOtherChefTasks: false,
                showWaitingMachineTasks: false,
                stations: []
            },
            offset: 0,
            topHeight: 200,
            bottomHeight: Dimensions.get('window').width,
            deviceHeight: Dimensions.get('window').height,
            isDividerClicked: false,
            pan: new Animated.ValueXY(),
            deviceWidth: Dimensions.get('window').width
        };
    }

    componentDidMount() {

        this.renderToolbarMenu();

        this.storageService.get(Storage.USER_ID).then(userId => {
            this.userId = userId;
        });

        this.storageService.get(Storage.USER_ID).then(userId => {
            this.currentUserId = userId;
            this.storageService.getObject(Storage.MANUAL).then(object => {
                let already = object[userId] && object[userId][ManualPages.TASK_OVERVIEW] === '1';
                if (!already) {
                    this.setState({
                        bottomMenu: [{
                            icon: 'help-circle', onClick: () => {
                                let extras = { close: this.onCloseManualPages };
                                this.eventEmitterService.emit({ type: EventTypes.Manual, data: ManualPages.TASK_OVERVIEW, extras });
                            }
                        }]
                    });
                    // this.init();
                }
            });
        });

        this.stationService.loadStations().then((stations: any) => {
            this.stations = stations;
            this.getTasks();
        });

        this.eventEmitterService.addListner(this.pushListener);

        if (this.isVROn) {
            if (this.voiceRecognition) {
                this.voiceRecognition.removeListener();
            }
            this.voiceRecognition.startListening();
            this.voiceRecognition.addListener(((voiceCommand: any) => {
                if (voiceCommand.includes(TRIGGER_PHRASE + t('TaskDetails.show-details'))) {
                    if (this.assignedTask) {
                        this.navigateToDetail(this.assignedTask);
                    }
                }
                if (voiceCommand.includes(TRIGGER_PHRASE + t('TaskDetails.done'))) {
                    if (this.assignedTask) {
                        this.finishTask(this.assignedTask);
                    }
                }
                if (voiceCommand.includes(TRIGGER_PHRASE + t('TaskDetails.assign-first-task'))) {
                    if (this.state.chefQueue.length > 0 && !this.assignedTask) {
                        let work = this.state.chefQueue[0];
                        if (work && work.status === WORK_QUEUED) {
                            this.assignTask(work);
                        }
                    }
                }
            }));
        }

        this.pageRefreshInterval = setInterval(() => this.getTasks(), 15000);
    }

    componentWillUnmount() {

        if (this.pageRefreshInterval) {
            clearTimeout(this.pageRefreshInterval);
        }

        this.eventEmitterService.removeListner(this.pushListener);
        if (this.isTTSOn) {
            this.voiceService.silence();
        }
        if (this.isVROn) {
            this.voiceRecognition.stopListening();
            this.voiceRecognition.removeListener();
        }



    }

    onCloseManualPages = (dontShowAgain: boolean) => {
        if (dontShowAgain) {
            this.setState({
                bottomMenu: null
            });
        }
    }

    openStationAssignment = () => {
        this.navigationService.push('StationList', {
            fromTasks: true,
        });
    }

    getTasks = () => {
        if (this.stations == null || this.stations.length == 0) {
            this.stationService.loadStations().then((stations: any) => {
                this.stations = stations;
                this.taskService.getTasks().then((works: Work[]) => {
                    this.filter(works);
                }).catch(e => console.log(e));
            }).catch(e => console.log(e));;
        } else {
            this.taskService.getTasks().then((works: Work[]) => {
                this.filter(works);
            });
        }
    }

    addToQueue = (work: Work, queue: Work[], stationId: string, stationSorted: any) => {
        queue.push(work);

        if (!Array.isArray(stationSorted[stationId])) {
            stationSorted[stationId] = [];
        }

        stationSorted[stationId].push(work);
    }

    filter = (works: Work[] | null = null, taskFilter: TaskFilter | null = null) => {

        this.assignedTask = null;

        let queue: Work[] = works ? works : this.state.queue;
        let filter: TaskFilter = taskFilter ? taskFilter : this.state.filter;

        let chefQueue: Work[] = [];
        let machineQueue: Work[] = [];

        const stationSorted: any = {};

        for (let work of queue) {
            const stationId = work.task.station;

            if (filter.stations && filter.stations.indexOf(work.task.station) === -1 && work.transportType === 0) {
                continue;
            }
            if (!work.readyToStart) {
                if (filter.showWaitingMachineTasks && this.taskService.isMachineTask(work)) {
                    this.addToQueue(work, machineQueue, stationId, stationSorted);
                }
                continue;
            }
            if (this.taskService.isMachineTask(work)) {
                this.addToQueue(work, machineQueue, stationId, stationSorted);
            } else {
                if (work.status === WORK_STARTED) {
                    if (work.userId === this.currentUserId) {
                        this.assignedTask = work;
                        this.props.setHighPriorityTaskCount(chefQueue.length);
                    } else if (work.transportType !== 4 && !filter.showOtherChefTasks) {
                        // work.transportType != 4 >> if not develivarable
                        continue;
                    }
                }
                this.addToQueue(work, chefQueue, stationId, stationSorted);
            }
        }
        for (let station of this.stations) {
            if (stationSorted[station.uuid]) {
                this.painter.generateColorMap(station, stationSorted[station.uuid], LIKELY_HOOD_LIMIT);
            }
        }
        if (this.assignedTask && this.readOnAssign) {
            let lastRead = this.storageService.getFast(StorageKeys.LAST_READ);
            if (lastRead != this.assignedTask.id) {
                this.voiceService.speak(this.taskService.getTaskName(this.assignedTask));
            }
            this.storageService.set(StorageKeys.LAST_READ, this.assignedTask.id + '');
        }
        if (this.assignedTask && this.autoOpenTask && this.assignedTask.userId == this.currentUserId) {
            let lastOpenedTask = this.storageService.getFast(StorageKeys.LAST_OPENED_TASK);
            if (lastOpenedTask != this.assignedTask.id) {
                this.navigateToDetail(this.assignedTask);
            }
            this.storageService.set(StorageKeys.LAST_OPENED_TASK, this.assignedTask.id + '');
        }
        this.storageService.set(StorageKeys.TASKK_FILTER, JSON.stringify(filter));
        this.setState({ chefQueue, machineQueue, queue, filter });
    }

    openFilter = () => {
        this.filterModal?.show(this.state.filter);
    }

    renderToolbarMenu = () => {
        this.tooblarMenu = [];
        this.tooblarMenu.push(
            <>
                <TouchableOpacity
                    activeOpacity={0.8}
                    onPress={this.openFilter} style={{ height: 40, width: 40 }}>
                    <Icon style={{}} name='filter' size={30} color={colors.white} />
                </TouchableOpacity>
                <TouchableOpacity
                    activeOpacity={0.8}
                    onPress={this.openStationAssignment} style={{ height: 40, width: 40 }}>
                    <Icons style={{}} name='select1' size={30} color={colors.white} />
                </TouchableOpacity>
            </>
        );
    }

    navigateToDetail = (work: Work) => {
        if (!work) {
            return;
        }
        this.navigationService.push('DetailPage', {
            taskId: work.task.uuid,
            workId: work.id,
            //taskId: work.task.uuid, 
            //assignmentid: work.id, 
            //index: MY_TASKS,
            //recipeName: work.recipe.name,
            //assignedTask: work
        });
    }

    finishTask = (work: Work) => {
        return this.taskService.finishTask(work).then((isCompleted: boolean) => {
            this.getTasks();
            return isCompleted;
        });
    }

    unassignTask = () => {
        return this.taskService.unassignTask().then((isCompleted: boolean) => {
            if (isCompleted) {
                this.storageService.unset(StorageKeys.LAST_READ);
                this.storageService.unset(StorageKeys.LAST_OPENED_TASK);
                this.assignedTask = null;
            } else {
                ToastAndroid.show(t('task-overview.task-cannot-unassign'), ToastAndroid.SHORT);
            }
            this.getTasks();
            return isCompleted;
        });
    }

    assignTask = (work: Work, forced = false) => {
        if (this.assignedTask == null || forced) {
            this.taskService.assignTask(this.currentUserId, work, (response: any) => {
                this.taskAssignmentConfirmation?.close();
                if (!response) {
                    ToastAndroid.show(t('task-overview.task-cannot-assign'), ToastAndroid.SHORT);
                } else {
                    this.getTasks();
                }
            });
        } else {
            this.taskAssignmentConfirmation?.show(work);
        }
    }

    onApplyFilter = (filter: TaskFilter) => {
        this.filter(null, filter);
    }

    renderTask = (work: any, isMachineTask: boolean) => {
        let isAssigned = this.currentUserId === work.userId ? true : false;
        return (
            <Task
                key={work.id}
                onFinishTask={this.finishTask}
                navigateToDetail={this.navigateToDetail}
                work={work}
                isActive={isAssigned ? true : false}
                assignTask={this.assignTask}
                unassignTask={isAssigned ? this.unassignTask : () => Promise.resolve(false)}
                isMachineTask={isMachineTask}
            />
        );
    }

    renderChefTasks() {
        return (
            <View style={{ flexDirection: 'column', }}>
                <View style={[styles.headerContainer]}>
                    <Text style={{ flex: 1, color: '#fff', fontSize: 16 }}>To Do</Text>
                    <Text style={{ color: '#fff', fontSize: 16, flex: 1, marginLeft: 5 }}>Amount</Text>
                    <Text style={{ color: '#fff', fontSize: 16, flex: 1, marginLeft: 10 }}>Min. left</Text>
                </View>
                <FlatList
                    data={this.state.chefQueue}
                    extraData={this.state}
                    ref='flatList'
                    refreshing={false}
                    onRefresh={this.getTasks}
                    renderItem={({ item }) => this.renderTask(item, false)} />
            </View>
        );
    }

    renderMachineTasks() {
        return (
            <View>
                <View style={styles.headerContainer}>
                    <Text style={{ flex: 1, color: '#fff', fontSize: 16 }}>Running</Text>
                    <Text style={{ color: '#fff', fontSize: 16, flex: 1 }}>Min. left</Text>
                </View>
                <FlatList
                    data={this.state.machineQueue}
                    extraData={this.state}
                    ref='flatList'
                    refreshing={false}
                    onRefresh={this.getTasks}
                    renderItem={({ item }) => this.renderTask(item, true)} />
            </View>
        );
    }

    render() {

        return (
            <AppBackground
                navigation={this.props.navigation}
                hideBack={false}
                category={menuCategory.taskCategory}
                index={MY_TASKS}
                toolbarMenu={this.tooblarMenu}
                bottomMenu={this.state.bottomMenu}
            >
                <View style={{ flex: 1 }}>
                    <TaskAssignmentConfirmation
                        ref={(ref: TaskAssignmentConfirmation) => this.taskAssignmentConfirmation = ref}
                        show={this.state.showAssignmentConfirmation}
                        assignTask={this.assignTask} />
                    <FilterStations
                        ref={(ref: FilterStations) => this.filterModal = ref}
                        filter={this.state.filter}
                        onApplyFilter={this.onApplyFilter}
                        stations={this.stations}
                        close={this.close} />
                        
                    <Splitter>
                        {this.renderChefTasks()}
                        {this.renderMachineTasks()}
                    </Splitter>
                </View>
            </AppBackground>
        );
    }

}

const styles = StyleSheet.create({
    
    headerContainer: {
        marginLeft: 10,
        marginRight: 10,
        flexDirection: 'row',
    },
});

export default connect(null, { setHighPriorityTaskCount })(TaskOverviewPage);
