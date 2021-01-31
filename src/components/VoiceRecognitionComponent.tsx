import React, { Component } from 'react';
import { StyleSheet, Text, View } from 'react-native';
import VoiceRecognition from '@native/VoiceRecognition';
import {TRIGGER_PHRASE } from '../utils/constants';
import t from '@translate';

export enum VOICE_ACTIONS {
    DONE,
    STEP_BY_STEP,
    READ_INGREDIENTS,
    DETAILS,
    READ_TASK,
    PREVIOUS,
    NEXT,
    SHOW_TASK
}

interface Props {
    onRecognizeVoice : Function
}

interface State {
 
}

export default class VoiceRecognitionComponent extends Component<Props, State> {
    private voiceRecognition: VoiceRecognition = new VoiceRecognition();
    constructor(props : Props){
        super(props)
        this.addVoiceRecognitionListener = this.addVoiceRecognitionListener.bind(this);
        this.removeVoiceRecognitionListener = this.removeVoiceRecognitionListener.bind(this);
    }

    componentDidMount(){
        this.props.onRef(this)
    }

    addVoiceRecognitionListener() {
        this.voiceRecognition.startListening();
        this.voiceRecognition.addListener(((voiceCommand: any) => {
            let action = null;
            if (voiceCommand.includes(TRIGGER_PHRASE + t('TaskDetails.done'))) {
                action = VOICE_ACTIONS.DONE;
            } else if (voiceCommand.includes(TRIGGER_PHRASE + t('TaskDetails.step-by-step'))) {
                action = VOICE_ACTIONS.STEP_BY_STEP;
            } else if (voiceCommand.includes(TRIGGER_PHRASE + t('TaskDetails.read-ingredients'))) {
                action = VOICE_ACTIONS.READ_INGREDIENTS;
            } else if (voiceCommand.includes(TRIGGER_PHRASE + t('TaskDetails.details'))) {
                action = VOICE_ACTIONS.DETAILS;
            } else if (voiceCommand.includes(TRIGGER_PHRASE + t('TaskDetails.read-task'))) {
                action = VOICE_ACTIONS.READ_TASK;
            } else if (voiceCommand.includes(TRIGGER_PHRASE + t('TaskDetails.previous'))) {
                action = VOICE_ACTIONS.PREVIOUS;
            } else if (voiceCommand.includes(TRIGGER_PHRASE + t('TaskDetails.next'))) {
                action = VOICE_ACTIONS.NEXT;
            } else if (voiceCommand.includes(TRIGGER_PHRASE + t('TaskDetails.show-tasks'))) {
                action = VOICE_ACTIONS.SHOW_TASK;
            }
            if(this.props.onRecognizeVoice){
                this.props.onRecognizeVoice(action);
            }
        }));
    }

    removeVoiceRecognitionListener() {
        this.voiceRecognition.stopListening();
        this.voiceRecognition.removeListener();
    }

    render(){
        return(null);
    }

}