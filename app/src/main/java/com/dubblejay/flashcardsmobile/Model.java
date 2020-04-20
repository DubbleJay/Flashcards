package com.dubblejay.flashcardsmobile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dubblejay.flashcardsmobile.dataBase.DeckBaseHelper;
import com.dubblejay.flashcardsmobile.dataBase.DeckCursorWrapper;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.dubblejay.flashcardsmobile.dataBase.DeckDbSchema.DeckTable;

public class Model {

    private static Model model;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static Model get(Context context) {
        if (model == null) {
            model= new Model(context);
        }
        return model;
    }

    private Model(Context context) {

        mContext = context.getApplicationContext();
        mDatabase = new DeckBaseHelper(mContext).getWritableDatabase();

    }

    public List<Deck> getDecks() {

        List<Deck> decks = new ArrayList<>();

        DeckCursorWrapper cursor = queryDecks(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                decks.add(cursor.getDeck());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return decks;
    }

    public void addDeck(Deck deck) {
        ContentValues values = getContentValues(deck);
        mDatabase.insert(DeckTable.NAME, null, values);
    }

    public void deleteDeck(UUID deckId) {
        String uuidString = deckId.toString();
        mDatabase.delete(DeckTable.NAME, DeckTable.Cols.UUID + " = ?", new String[] {uuidString});
    }

    public Deck getDeck (UUID id) {
        DeckCursorWrapper cursor = queryDecks(
                DeckTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getDeck();
        } finally {
            cursor.close();
        }
    }

    public void updateDeck(Deck deck) {
        String uuidString = deck.getId().toString();
        ContentValues values = getContentValues(deck);
        mDatabase.update(DeckTable.NAME, values, DeckTable.Cols.UUID + " = ?", new String[] { uuidString });
    }

    private static ContentValues getContentValues(Deck deck) {

        Gson gson = new Gson();

        ContentValues values = new ContentValues();

        values.put(DeckTable.Cols.UUID, deck.getId().toString());
        values.put(DeckTable.Cols.TITLE, deck.getTitle());
        values.put(DeckTable.Cols.CARDS, gson.toJson(deck.getCards()).getBytes());
        values.put(DeckTable.Cols.DATE_CREATED, deck.getDateCreated().getTime());

        return values;
    }

    private DeckCursorWrapper queryDecks(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                DeckTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new DeckCursorWrapper(cursor);
    }

}
