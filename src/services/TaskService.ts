import ApiBuilder from '@api/routes';
import Work from '@models/Work';

import HttpClient from './HttpClient';
export default class TaskService {

    private apiBuilder: ApiBuilder;
    private httpClient: HttpClient;

    constructor(httpClient: HttpClient,apiBuilder: ApiBuilder) {
        this.httpClient = httpClient;
        this.apiBuilder = apiBuilder;
    }

    getTasks = (): Promise<Work[]> => {
        return new Promise((resolve, reject) => {
            this.httpClient.post(this.apiBuilder.paths!.stationTasks, {}).then((tasks: Work[]) => {
                let machineTasks: Work[] = [];
                let chefTasks: Work[] = [];
                resolve(tasks);
            }).catch(error => {
                resolve([]);
            });
        });
    }

    getTaskName =(assignedTask: Work): string => {
        if (assignedTask && assignedTask.transportType > 0) {
            return assignedTask.title;
        } else if (assignedTask.task) {
            return assignedTask.task.name;
        }else{
            return "";
        }
    }

    assignTask(userId: string, work: Work, successCallback: Function) {
        this.httpClient.post(this!.apiBuilder!.paths!.assignTask, { taskId: work.id }).then(response => {
            successCallback(response);
        }).catch();
    }
pingIp(taskId:any){
    return new Promise((resolve, reject) => {
    this.httpClient.post(this!.apiBuilder!.paths!.pingIp, {  taskId: taskId,  }).then(response => {
        resolve(true);
    }).catch(() => resolve(false));
}); 
}
    finishTask(work: Work): Promise<boolean> {
        return new Promise((resolve, reject) => {
            if (work.Orderobject) {
                this.httpClient.post(this!.apiBuilder!.paths!.updateTask, { status: 4, queueId: work.id, time: 0 }).then(response => {
                    resolve(true);
                }).catch(() => resolve(false));
            } else {
                this.httpClient.post(this!.apiBuilder!.paths!.CheckToFinish, { taskId: work.id }).then(response => {
                    if (response) {
                        this.httpClient.post(this!.apiBuilder!.paths!.updateTask,
                            { status: 4, queueId: work.id, time: 0 }).then((responses: any) => {
                                resolve(true);
                            }).catch(() => resolve(false));
                    } else {
                        resolve(false);
                    }
                }).catch(() => resolve(false));
            }
        });
    }

    completeTask(workId: string): Promise<boolean> {
        return new Promise((resolve, reject) => {
            this.httpClient.post(this!.apiBuilder!.paths!.updateTask, { status: 4, queueId: workId, time: 0 }).then(response => {
                resolve(true);
            }).catch(() => resolve(false));
        });
    }

    completeIntervention(interventionId: number, reduceValue: boolean): Promise<boolean> {
        return new Promise((resolve, reject) => {
            this.httpClient.post(this.apiBuilder.paths!.updateIntervention, {
                status: 2,
                intervention: interventionId,
                time: 0,
                reduceValue
            }).then(response => {
                resolve(true);
            }).catch(() => resolve(false));
        });
    }

    unassignTask(): Promise<boolean> {
        return new Promise((resolve, reject) => {
            this.httpClient.post(this!.apiBuilder!.paths!.unassignTask, {}).then(response => {
                if (response) {
                    resolve(true);
                } else {
                    resolve(false);
                }
            }).catch(() => resolve(false));
        });
    }

    isMachineTask(work: Work): boolean {
        return work.task && work.task.machine && !work.task.chefInvolved && work.transportType == 0;
    }
}
