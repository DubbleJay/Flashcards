package com.dubblejay.flashcardsmobile.dataBase;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.dubblejay.flashcardsmobile.Card;
import com.dubblejay.flashcardsmobile.Deck;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import com.dubblejay.flashcardsmobile.dataBase.DeckDbSchema.DeckTable;

public class DeckCursorWrapper extends CursorWrapper {

    public DeckCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Deck getDeck() {

        String uuidString = getString(getColumnIndex(DeckTable.Cols.UUID));
        String title = getString(getColumnIndex(DeckTable.Cols.TITLE));
        byte[] blob = getBlob(getColumnIndex(DeckTable.Cols.CARDS));
        String json = new String(blob);
        Gson gson = new Gson();
        ArrayList<Card> cards = gson.fromJson(json, new TypeToken<ArrayList<Card>>()
        {}.getType());
        long dateCreated = getInt(getColumnIndex(DeckTable.Cols.DATE_CREATED));


        Deck deck = new Deck(UUID.fromString(uuidString));
        deck.setTitle(title);
        deck.setCards(cards);
        deck.setDateCreated(new Date(dateCreated));

        return deck;


    }
}
