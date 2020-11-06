package com.thang.quiz.Item;

public class ItemHighScore{
    public String name;
    public int score;
    public double time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public ItemHighScore(String name, int score, double time) {
        this.name = name;
        this.score = score;
        this.time = time;
    }
}
