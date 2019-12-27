package com.ihsinformatics.dynamicformsgenerator.data.core.questions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SkipLogics {



    private String id;
    private int questionID;

    private List<String> equalsList;
    private List<String> notEqualsList;

    private List<Integer> equalsToList;
    private List<Integer> notEqualsToList;
    private List<Integer> lessThanList;
    private List<Integer> greaterThanList;

   // private List<SkipLogics> nestedLogics;
   public SkipLogics(){
       equalsList=new ArrayList<String>();
       notEqualsList=new ArrayList<String>();

       equalsToList=new ArrayList<Integer>();
       notEqualsToList=new ArrayList<Integer>();
       lessThanList=new ArrayList<Integer>();
       greaterThanList=new ArrayList<Integer>();

   }

    public SkipLogics(String id, int questionID, List<String> equalsList, List<String> notEqualsList, List<Integer> equalsToList, List<Integer> notEqualsToList, List<Integer> lessThanList, List<Integer> greaterThanList) {
        this.id = id;
        this.questionID = questionID;
        this.equalsList = equalsList;
        this.notEqualsList = notEqualsList;
        this.equalsToList = equalsToList;
        this.notEqualsToList = notEqualsToList;
        this.lessThanList = lessThanList;
        this.greaterThanList = greaterThanList;

      //  this.nestedLogics = nestedLogic;
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

    public List<Integer> getEqualsToList() {
        return equalsToList;
    }

    public void setEqualsToList(List<Integer> equalsToList) {
        this.equalsToList = equalsToList;
    }

    public List<Integer> getNotEqualsToList() {
        return notEqualsToList;
    }

    public void setNotEqualsToList(List<Integer> notEqualsToList) {
        this.notEqualsToList = notEqualsToList;
    }

    public List<Integer> getLessThanList() {
        return lessThanList;
    }

    public void setLessThanList(List<Integer> lessThanList) {
        this.lessThanList = lessThanList;
    }

    public List<Integer> getGreaterThanList() {
        return greaterThanList;
    }

    public void setGreaterThanList(List<Integer> greaterThanList) {
        this.greaterThanList = greaterThanList;
    }
}
