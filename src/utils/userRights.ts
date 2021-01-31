import { AsyncStorage } from "react-native";

export const userRights = (userRight: String , right: string | null) => {
    let userRights: any = right? JSON.parse(right.toString()) : []
    let rightSet: boolean = false    
    userRights.forEach((right: any) => {
        if (right.right == userRight) {
            rightSet = true;
        }
    });
    return rightSet
};