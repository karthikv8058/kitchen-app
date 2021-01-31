import React, { Component } from 'react';
import ApiBuilder from '../api/routes'
import AclNavigation from '../acl/AclNavigation';
import { Bind } from '../ioc/ServiceContainer';

export default class AbstractComponent<Props, State> extends Component<Props, State> {
    protected apiBuilder: ApiBuilder;
    protected aclNavigation: AclNavigation;

    constructor(props: Props) {
        super(props);
        this.apiBuilder = Bind('apiBuilder');
        // this.aclNavigation = new AclNavigation(this.props.navigation);
    }
}
