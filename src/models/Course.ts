export default interface Course {
    isOnCall?: boolean | undefined;
    actualDeliveryTime?: number | undefined;
    deliveryTime?: string;
    meals: [];
    deliveryDate: string;
}
