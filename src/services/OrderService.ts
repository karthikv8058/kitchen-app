import ApiBuilder from '@api/routes';
import Order from '@models/Order';
import HttpClient from '@services/HttpClient';
import Inventory from '@models/Inventory';
import Room from '@models/Room';
import Recipe from '@models/Recipe';

export default class OrderService {

    private apiBuilder: ApiBuilder;
    private httpClient: HttpClient;

    constructor(httpClient: HttpClient, apiBuilder: ApiBuilder) {
        this.httpClient = httpClient;
        this.apiBuilder = apiBuilder;
    }
    getAllOrders(pageCount: number) {
        return new Promise((resolve, reject) => {
            this.httpClient.post(this.apiBuilder!.paths!.overviewTask,{pageCount:pageCount}
               ).then(response => {
                    resolve(response);
                }).catch(error => {
                    resolve(null);
                });
        });
    }
    // getAllOrders(pageCount: number,
    //     successesCallback: (response: Order[]) => void,
    //     errorCallback: (error: Error) => void) {
    //     if (this.apiBuilder.paths) {
    //         this.httpClient.post(this.apiBuilder.paths.overviewTask, {pageCount:pageCount}).then(response => {
    //             if (successesCallback) {
    //                 successesCallback(response);
    //             }
    //         }).catch(error => {
    //             if (errorCallback) {
    //                 errorCallback(error);
    //             }
    //         });
    //     }
    // }

    loadExternalOrders(
        successesCallback: (response: Order[]) => void,
        errorCallback: (error: Error) => void) {
        if (this.apiBuilder.paths) {
            this.httpClient.post(this.apiBuilder.paths.loadExternalOrders, {}).then(response => {
                if (successesCallback) {
                    successesCallback(response);
                }
            }).catch(error => {
                if (errorCallback) {
                    errorCallback(error);
                }
            });
        }
    }
    loadorederFromWeb(isExternalOrder: number) {
        return new Promise((resolve, reject) => {
            this.httpClient.post(this.apiBuilder!.paths!.loadorederFromWeb,
                { isExternalOrder: isExternalOrder }).then(response => {
                    resolve(response);
                }).catch(error => {
                    resolve(null);
                });
        });
    }
    loadArchivedOrder(isExternalOrder: number) {
        return new Promise((resolve, reject) => {
            this.httpClient.post(this.apiBuilder!.paths!.loadArchivedOrder,
                { isExternalOrder: isExternalOrder }).then(response => {
                    resolve(response);
                }).catch(error => {
                    resolve(null);
                });
        });
    }

    deletePrinterMessage(orderId: String) {
        return new Promise((resolve, reject) => {
            this.httpClient.post(this.apiBuilder!.paths!.deletePrinterMessage,
                { orderId: orderId }).then(response => {
                    resolve(response);
                }).catch(error => {
                    resolve(null);
                });
        });
    }
    getAllRooms(): Promise<Room[]> {
        return new Promise((resolve, reject) => {
            this.httpClient.post(this.apiBuilder!.paths!.getAllRooms, {}).then(response => {
                resolve(response);
            }).catch(error => {
                resolve([]);
            });
        });
    }

    getInventoryList(): Promise<Room[] | any> {
        return new Promise((resolve, reject) => {
            this.httpClient.post(this.apiBuilder!.paths!.getInventoryList, {}).then(response => {
                resolve(response);
            }).catch(error => {
                resolve([]);
            });
        });
    }

    placeOrder(order: Order): Promise<Order[]> {
        return new Promise((resolve, reject) => {
            this.httpClient.post(this.apiBuilder!.paths!.placeOrder, { order: order }).then(response => {
                resolve(response);
            }).catch(error => {
                resolve([]);
            });
        });
    }
    addNewIngredient(imageUuid: string, unitUuid: string, recipeName: string, inventoryType: number, barcodeData: string): Promise<Order[]> {
        return new Promise((resolve, reject) => {
            this.httpClient.post(this.apiBuilder!.paths!.addNewIngredient, { imageUuid, unitUuid, recipeName, inventoryType, barcodeData }).then(response => {
                resolve(response);
            }).catch(error => {
                resolve([]);
            });
        });
    }
    updateInventoryQuantity(recipeId: string | undefined, quantity: number, unit: string): Promise<[]> {
        return new Promise((resolve, reject) => {
            this.httpClient.post(this.apiBuilder!.paths!.updateInventoryQuantity,
                { inventoryId: recipeId, inventoryQuantity: quantity, unit }).then(response => {
                    resolve(response);
                }).catch(error => {
                    resolve([]);
                });
        });
    }
    getRecipes(): Promise<Recipe[]> {
        return new Promise((resolve, reject) => {
            this.httpClient.post(this.apiBuilder!.paths!.getRecipes, {}).then(response => {
                resolve(response);
            }).catch(error => {
                resolve([]);
            });
        });
    }
    deleteOrder(orderId: string) {
        return new Promise((resolve, reject) => {
            this.httpClient.post(this.apiBuilder!.paths!.deleteOrder,
                { orderId: orderId }).then(response => {
                    resolve(response);
                }).catch(error => {
                    resolve(null);
                });
        });
    }
    deleteExternalOrder(orderId: string) {
        return new Promise((resolve, reject) => {
            this.httpClient.post(this.apiBuilder!.paths!.deleteExternalOrder,
                { orderId: orderId }).then(response => {
                    resolve(response);
                }).catch(error => {
                    resolve(null);
                });
        });
    }

    checkOrderStarted(orderId: number) {
        return new Promise((resolve, reject) => {
            this.httpClient.post(this.apiBuilder!.paths!.checkOrderStarted,
                { orderId: orderId }).then(response => {
                    resolve(response);
                }).catch(error => {
                    resolve(null);
                });
        });
    }
    placeExternalOrder(orderId: string) {
        return new Promise((resolve, reject) => {
            this.httpClient.post(this.apiBuilder!.paths!.placeExternalOrder,
                { orderId: orderId }).then(response => {
                    resolve(response);
                }).catch(error => {
                    resolve(null);
                });
        });
    }

    postOnCall(orderId: string) {
        return new Promise((resolve, reject) => {
            this.httpClient.post(this.apiBuilder!.paths!.postOnCall,
                { courseId: orderId }).then(response => {
                    resolve(response);
                }).catch(error => {
                    resolve(null);
                });
        });
    }

    storeToAnalyze(orderId: String) {
        return new Promise((resolve, reject) => {
            this.httpClient.post(this.apiBuilder!.paths!.storeToAnalyze,
                { orderId: orderId }).then(response => {
                    resolve(response);
                }).catch(error => {
                    resolve(null);
                });
        });
    }
    getOrderList(orderId: number,
        successesCallback: (response: Order[]) => void,
        errorCallback: (error: Error) => void) {
        if (this.apiBuilder.paths) {
            this.httpClient.post(this.apiBuilder.paths.getOrderList, { orderId: orderId }).then(response => {
                if (successesCallback) {
                    successesCallback(response);
                }
            }).catch(error => {
                if (errorCallback) {
                    errorCallback(error);
                }
            });
        }
    }

    getRecipesAndLabels(
        successesCallback: (response: Array<Order>) => void,
        errorCallback: (error: Error) => void) {
        if (this.apiBuilder.paths) {
            this.httpClient.get(this.apiBuilder.paths!.getRecipesAndLabels).then(response => {
                if (successesCallback) {
                    successesCallback(response);
                }
            }).catch(error => {
                if (errorCallback) {
                    errorCallback(error);
                }
            });
        }
    }

    fetchOrderList(orderId: number,
        successesCallback: (response: Order) => void,
        errorCallback: (error: Error) => void) {
        if (this.apiBuilder.paths) {
            this.httpClient.post(this.apiBuilder.paths.fetchOrderList, { orderId: orderId }).then(response => {
                if (successesCallback) {
                    successesCallback(response);
                }
            }).catch(error => {
                if (errorCallback) {
                    errorCallback(error);
                }
            });
        }
    }

    finishOrder(orderId: string,
        successesCallback: (response: []) => void,
        errorCallback: (error: Error) => void) {
        if (this.apiBuilder.paths) {
            this.httpClient.post(this.apiBuilder.paths.finishOrder, { orderId: orderId }).then(response => {
                if (successesCallback) {
                    successesCallback(response);
                }
            }).catch(error => {
                if (errorCallback) {
                    errorCallback(error);
                }
            });
        }
    }
}
