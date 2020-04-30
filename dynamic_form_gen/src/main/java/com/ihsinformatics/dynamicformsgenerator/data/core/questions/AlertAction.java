package com.ihsinformatics.dynamicformsgenerator.data.core.questions;

import java.util.List;

public class AlertAction {

    private String alertMessage;
    private List<SExpression> alertWhen;

    public AlertAction(String alertMessage, List<SExpression> alertWhen) {
        this.alertMessage = alertMessage;
        this.alertWhen = alertWhen;
    }

    public AlertAction() {
    }

    public String getAlertMessage() {
        return alertMessage;
    }

    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }

    public List<SExpression> getAlertWhen() {
        return alertWhen;
    }

    public void setAlertWhen(List<SExpression> alertWhen) {
        this.alertWhen = alertWhen;
    }
}
