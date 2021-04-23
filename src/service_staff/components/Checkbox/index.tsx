import React from "react";
import Checkbox from "./Checkbox";

export default function index(props: any) {
  return (
    <>
      <Checkbox value="Morgen" />
      <Checkbox value="Heute" />
      <Checkbox value="Gestern" />
    </>
  );
}
