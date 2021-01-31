import React,{
    Image,
    Text,
    View,
} from 'react-native';
import placeHolder from './assets/placeholder.png';

function Ingredient(props) {

    //let ingredient = props.ingredient;
    //let imageURL = props.baseUrl + '/get-image?filename=' + ingredient.image;
    return (
        <View>
            {/* <Image style={{ height: 50, width: 50, marginTop: 5 }}
                source={ingredient.image ?
                    { uri: imageURL + '/get-image?filename=' + ingredient.image } : placeHolder}>
            </Image> */}
            {/* <Text style={{ textAlign: 'right', width: 50, }}>{ingredient.output_quantity}</Text> */}
            {/* <Text style={{ textAlign: 'left', width: 50, }}>{this.renderProps(element)}</Text> */}
            <Text>sdsd</Text>
        </View>
    );
}

export default Ingredient;
