import React, { Component } from 'react';
import ApiBuilder from '../api/routes'
import { Bind } from '../ioc/ServiceContainer';

export default class AbstractComponent<Props, State> extends Component<Props, State> {
    protected apiBuilder: ApiBuilder;

    constructor(props: Props) {
        super(props);
        this.apiBuilder = Bind('apiBuilder');
    }
}
