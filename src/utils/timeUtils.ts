export const getTimeDiffrece = (timeInmilliSecs: any) => {
    let currentTime = Date.now();
    let diff = (timeInmilliSecs - currentTime);
    let diffMins = Math.round(((diff % 86400000) % 3600000) / 60000);
    return diffMins;
};
