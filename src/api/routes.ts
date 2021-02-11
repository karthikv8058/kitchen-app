
interface Paths {
    updateTask: string;
    CheckToFinish: string;
    getTask: string;
    getRecipes: string;
    getRecipesAndLabels: string;
    placeOrder: string;
    fetchOrderList: string;
    getAllUsers: string;
    login: string;
    getstations: string;
    stationTasks: string;
    getstationusers: string;
    overviewTask: string;
    checkOrderStarted: string;
    finishOrder: string;
    addStationUser: string;
    getstationlist: string;
    getrecipeLabels: string;
    assignTask: string;
    unassignTask: string;
    logoutUser: string;
    ping: string;
    deleteOrder: string;
    deleteExternalOrder:string;
    getInventoryList: string;
    updateInventoryQuantity: string;
    getPrinterData: string;
    deletePrinterMessage: string;
    storeToAnalyze: string;
    postOnCall: string;
    getOrderList: string;
    updateIntervention: string;
    chat: string;
    recipeDetails: string;
    webCredentials: string;
    requestSync: string;
    uploadImage: string;
    getUserRights: string;
    imageUrl: string;
    getVideo: string;
    placeExternalOrder:string;
    loadExternalOrders:string;
    loadorederFromWeb:string;
    loadArchivedOrder:string;
    getAllRecipes:string
    addNewIngredient:string,
    testStationPrinter:string,
    pingIp:string,
    getPrinterList:string
}

export default class ApiBuilder {
    public paths?: Paths = undefined;
    private baseURL?: string = '';
    private ip: string = '';

    constructor(ip?: string) {
        this.baseURL = 'http://' + ip + ':8888/';
        this._buildPaths();
    }

    setIP(ip: string | null) {
        if (ip) {
            this.ip = ip;
            this.baseURL = 'http://' + ip + ':8888/';
            this._buildPaths();
        }
    }

    getIP(ip?: string) {
        return this.ip;
    }

    _buildPaths() {
        this.paths = {
            updateTask: this.baseURL + 'update-task',
            CheckToFinish: this.baseURL + 'check-task-to-finish',
            getTask: this.baseURL + 'get-task',
            getRecipes: this.baseURL + 'get-recipes',
            getRecipesAndLabels: this.baseURL + 'get-recipe-labels',
            placeOrder: this.baseURL + 'new-pos-order',
            fetchOrderList: this.baseURL + 'fetch-order-data',
            getAllUsers: this.baseURL + 'get-all-users',
            login: this.baseURL + 'login',
            getstations: this.baseURL + 'get-stations',
            stationTasks: this.baseURL + 'station-tasks',
            getstationusers: this.baseURL + 'get-station-users',
            overviewTask: this.baseURL + 'overview-task',
            checkOrderStarted: this.baseURL + 'check-order-started',
            finishOrder: this.baseURL + 'finish-order',
            addStationUser: this.baseURL + 'add-station-user',
            getstationlist: this.baseURL + 'get-station-list',
            getrecipeLabels: this.baseURL + 'get-all-labels',
            assignTask: this.baseURL + 'assign-task',
            unassignTask: this.baseURL + 'unassign-task',
            logoutUser: this.baseURL + 'logout',
            ping: this.baseURL + 'ping',
            deleteOrder: this.baseURL + 'delete-order',
            deleteExternalOrder: this.baseURL + 'delete-external-order',
            getInventoryList: this.baseURL + 'get-inventory-data',
            updateInventoryQuantity: this.baseURL + 'update-inventory-quantity',
            getPrinterData: this.baseURL + 'get-printer-data',
            deletePrinterMessage: this.baseURL + 'delete-printer-message',
            storeToAnalyze: this.baseURL + 'store-to-analyze',
            postOnCall: this.baseURL + 'on-call',
            getOrderList: this.baseURL + 'get-order-details',
            updateIntervention: this.baseURL + 'update-intervention',
            chat: this.baseURL + 'chat',
            recipeDetails: this.baseURL + 'recipe-Details',
            webCredentials: this.baseURL + 'get-web-credentials',
            requestSync: this.baseURL + 'sync-request',
            uploadImage: this.baseURL + 'image-upload',
            getUserRights: this.baseURL + 'get-user-right',
            imageUrl: this.baseURL + 'get-image?filename=',
            getVideo: this.baseURL + 'play-video?filename=',
            placeExternalOrder:this.baseURL + 'place-external-order',
            loadExternalOrders:this.baseURL + 'external-overview-orders',
            loadorederFromWeb:this.baseURL + 'load-order-from-web',
            loadArchivedOrder:this.baseURL + 'load-archived-order',
            getAllRecipes:this.baseURL + 'get-all-recipes',
            addNewIngredient:this.baseURL+'add-new-ingredient',
            testStationPrinter:this.baseURL+'test=station-printer',
            pingIp:this.baseURL+'ping-ip',
            getPrinterList:'get-printers'
        };
    }
}
