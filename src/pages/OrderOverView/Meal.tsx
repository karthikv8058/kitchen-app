import colors from '@theme/colors';
import React from 'react';
import { StyleSheet, Text, View, } from 'react-native';

interface OrderLine {
    recipeName: string;
}
interface Props {
    meal: { orderLines: OrderLine[] };
}

function Meal(props: Props): JSX.Element {

    let orderLine = props.meal.orderLines ? props.meal.orderLines : [];

    return (
        <>{orderLine.map((orderLine: OrderLine, index: number) => {
            return (
                <View style={{ flexDirection: 'column' }}>
                    <Text style={(index === 0 ? styles.recipeContainer : styles.recipeItemContainer)}>
                        {orderLine.recipeName}
                    </Text>
                </View>
            );
        })}
        </>
    );
}

export default Meal;

const styles = StyleSheet.create({
    recipeContainer: {
        marginLeft: 10,
        marginTop: 5,
        color: colors.black,
        fontWeight: 'bold',
        fontSize: 15
    },
    recipeItemContainer: {
        color: colors.black,
        marginLeft: 25
    },

});
