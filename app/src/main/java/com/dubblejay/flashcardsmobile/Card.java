package com.dubblejay.flashcardsmobile;

import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.UUID;

public class Card implements Serializable {

    private String question;
    private String answer;
    private UUID uuid;

    public Card(String question, String answer) {
        this.question = question;
        this.answer = answer;
        this.uuid = UUID.randomUUID();
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public UUID getId() {
        return uuid;
    }

}
