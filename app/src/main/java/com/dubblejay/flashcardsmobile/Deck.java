package com.dubblejay.flashcardsmobile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Deck implements Serializable {

    private UUID mid;
    private String title;
    private ArrayList<Card> cards;
    private Date dateCreated;

    public Deck() {
        this(UUID.randomUUID());
    }

    public Deck(UUID id) {
        mid = id;
        cards = new ArrayList<>();
        dateCreated = new Date();
    }

    public UUID getId() {
        return mid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }


    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

}
