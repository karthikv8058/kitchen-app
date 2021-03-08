import { padNumber } from './commonUtil';
const monthNames = [
    'January',
    'February',
    'March',
    'April',
    'May',
    'June',
    'July',
    'August',
    'September',
    'October',
    'November',
    'December'
];

export const getTime = (date: Date) => {
    return padNumber(date.getDate()) + '.' + (monthNames[date.getMonth()]) + '.' + date.getFullYear();
};

export const getDifferenceTime = (deliveryTime: number | undefined): string => {   
    
    if(deliveryTime == undefined){
        return "";
    }
    
    let currentTime = Date.now();
    let dates = new Date(deliveryTime);
    let milliseconds=deliveryTime
    if(dates.toString()!='Invalid Date'){
        milliseconds=new Date(deliveryTime).getTime();
    }
    let difference: number = milliseconds- currentTime;

    let isDelayed = true;
    if (difference < 0) {
        isDelayed = false;
        difference = -1 * difference;
    }
    let date = convertMS(difference);
    let output = '';
    if (isDelayed) {
        output = '-';
    }
    if (date.day > 0) {
        output = output + `${date.day}d `;
    }
    return `${output} ${pad(date.hour, 2)}:${pad(date.minute, 2)}`;
};

export const convertMS = (milliseconds: number) => {
    let day, hour, minute, seconds;
    seconds = Math.floor(milliseconds / 1000);
    minute = Math.floor(seconds / 60);
    seconds = seconds % 60;
    hour = Math.floor(minute / 60);
    minute = minute % 60;
    day = Math.floor(hour / 24);
    hour = hour % 24;
    return {
        day: day,
        hour: hour,
        minute: minute,
        seconds: seconds
    };
};

export const pad = (number: number, size: number) => {
    let s = number + '';
    while (s.length < size) {
        s = '0' + s;
    }
    return s;
};
