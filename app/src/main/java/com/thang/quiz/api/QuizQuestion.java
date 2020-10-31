package com.thang.quiz.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;



public class QuizQuestion {

    @SerializedName("response_code")
    @Expose
    private Integer responseCode;
    @SerializedName("results")
    @Expose
    private List<Result> results = null;


    public QuizQuestion() {
    }

    public QuizQuestion(Integer responseCode, List<Result> results) {
        super();
        this.responseCode = responseCode;
        this.results = results;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public List<Result> getResults() {
        return results;
    }

}