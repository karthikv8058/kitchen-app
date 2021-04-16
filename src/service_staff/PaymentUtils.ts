
enum PaymnetType {
    NOT_PAYED = 0,
    CARD = 1,
    CASH = 2
}

export interface PaymentMeal {
    mealId: string,
    recipe: string
    amount: number,
    guest: string,
    guestName: string,
    paymnet: PaymnetType
}

export interface GustPaymentDescription {
    id: string,
    name: string
    amount: number,
    meals: PaymentMeal[]
}

export interface PaymentDescription {
    amount: number,
    guests: GustPaymentDescription[]
}

export const fillDate = (meals: PaymentMeal[]): PaymentDescription => {
    let paymentDescription = { amount: 0, guests: [] as GustPaymentDescription[] };
    let paymentByGuest: any = {}
    let paymentAmountByGuest: any = {}
    for (let meal of meals) {
        paymentDescription.amount += meal.amount;
        if (paymentByGuest[meal.guest] == null) {
            paymentByGuest[meal.guest] = [];
            paymentAmountByGuest[meal.guest] = 0;
        }
        paymentByGuest[meal.guest].push(meal);
        paymentAmountByGuest[meal.guest] += meal.amount
    }
    for (let p in paymentByGuest) {
        let gustPaymentDescription: GustPaymentDescription = {
            id: p,
            name: paymentByGuest[p][0].guestName,
            amount: paymentAmountByGuest[p],
            meals: paymentByGuest[p]
        }
        paymentDescription.guests.push(gustPaymentDescription);
    }
    return paymentDescription;
}