import {StyleSheet } from "react-native";

const commonStyles = StyleSheet.create({
    flexRowJustifyBetweenMtop15:{
                flexDirection: "row",
                justifyContent: "space-between",
                marginTop: 15,
    },
    flexRowJustifyFlexendMtop15:{
        flexDirection: "row",
        justifyContent: "flex-end",
        marginTop: 15,
},
    headingTextStyle: {
        color: "#fff",
        fontWeight: "bold",
        fontSize: 18,
      },
      normalTextStyle: {
        color: "#fff",
      },
})

export {commonStyles};