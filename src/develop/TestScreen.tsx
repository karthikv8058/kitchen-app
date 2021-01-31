import * as React from 'react';
import { View, Text, Button, FlatList } from 'react-native';

interface Prop {
    navigation: any
}


function TestScreen(props: Prop) {

    return (
        <View style={{ flex: 1, alignItems: 'center', justifyContent: 'center' }}>

            <FlatList
                style={{ alignContent: "stretch", alignSelf: 'stretch' }}
                data={[{ title: 'Title Text', key: 'item1' }, { title: 'Title Text', key: 'item2' },]}
                renderItem={({ item, index, separators }) => (
                    <View key={item.key}>
                        <Text>{item.title}</Text>
                    </View>

                )}
            />
        </View>
    );
}

export default TestScreen;