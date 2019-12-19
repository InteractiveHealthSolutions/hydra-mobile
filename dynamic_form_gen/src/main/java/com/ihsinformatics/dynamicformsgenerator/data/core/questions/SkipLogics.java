package com.ihsinformatics.dynamicformsgenerator.data.core.questions;

import java.util.List;

public class SkipLogics {


    private String operator;
    private String id;
    private int questionID;

    private List<String> equalsList;
    private List<String> notEqualsList;

    private int[] equalsToList;
    private int[] notEqualsToList;
    private int[] lessThanList;
    private int[] greaterThanList;


    public SkipLogics(String id, int questionID, String operation, List<String> equalsList, List<String> notEqualsList, int[] equalsToList, int[] notEqualsToList, int[] lessThanList, int[] greaterThanList) {
        this.id = id;
        this.questionID = questionID;
        this.operator=operation;
        this.equalsList = equalsList;
        this.notEqualsList = notEqualsList;
        this.equalsToList = equalsToList;
        this.notEqualsToList = notEqualsToList;
        this.lessThanList = lessThanList;
        this.greaterThanList = greaterThanList;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public List<String> getEqualsList() {
        return equalsList;
    }

    public void setEqualsList(List<String> equalsList) {
        this.equalsList = equalsList;
    }

    public List<String> getNotEqualsList() {
        return notEqualsList;
    }

    public void setNotEqualsList(List<String> notEqualsList) {
        this.notEqualsList = notEqualsList;
    }

    public int[] getEqualsToList() {
        return equalsToList;
    }

    public void setEqualsToList(int[] equalsToList) {
        this.equalsToList = equalsToList;
    }

    public int[] getNotEqualsToList() {
        return notEqualsToList;
    }

    public void setNotEqualsToList(int[] notEqualsToList) {
        this.notEqualsToList = notEqualsToList;
    }

    public int[] getLessThanList() {
        return lessThanList;
    }

    public void setLessThanList(int[] lessThanList) {
        this.lessThanList = lessThanList;
    }

    public int[] getGreaterThanList() {
        return greaterThanList;
    }

    public void setGreaterThanList(int[] greaterThanList) {
        this.greaterThanList = greaterThanList;
    }
}
