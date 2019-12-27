package com.ihsinformatics.dynamicformsgenerator.data.core.questions;


import java.util.ArrayList;
import java.util.List;

public class SExpression {

    private String operator;
    List<SkipLogics> skipLogicsObjects;
    List<SExpression> skipLogicsArray;

    public SExpression() {
        skipLogicsObjects=new ArrayList<SkipLogics>();
        skipLogicsArray=new ArrayList<SExpression>();

    }

    public SExpression(String operator, List<SkipLogics> skipLogicsObjects, List<SExpression> skipLogicsArray) {
        this.operator = operator;
        this.skipLogicsObjects = skipLogicsObjects;
        this.skipLogicsArray = skipLogicsArray;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public List<SkipLogics> getSkipLogicsObjects() {
        return skipLogicsObjects;
    }

    public void setSkipLogicsObjects(List<SkipLogics> skipLogicsObjects) {
        this.skipLogicsObjects = skipLogicsObjects;
    }

    public List<SExpression> getSkipLogicsArray() {
        return skipLogicsArray;
    }

    public void setSkipLogicsArray(List<SExpression> skipLogicsArray) {
        this.skipLogicsArray = skipLogicsArray;
    }
}
