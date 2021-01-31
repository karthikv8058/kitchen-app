export enum InterventionPosition {
    POSITION_START = 1,
    POSITION_NORMAL = 2,
    POSITION_END = 3,
}

export default interface Intervention {
    intervention_position: InterventionPosition;
    intervention_time: number;
}
