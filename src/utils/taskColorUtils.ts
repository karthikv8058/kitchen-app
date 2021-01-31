
export const getBackgroundViewStyle = (stationId: number, stationList: any) => {
    let stationColor = '';
    stationList.map((station: any) => {
        if (station.uuid === stationId) {
            stationColor = station.color;
        }
    });
    return stationColor;
};
