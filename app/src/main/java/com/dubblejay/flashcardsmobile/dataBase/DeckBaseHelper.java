package com.dubblejay.flashcardsmobile.dataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.dubblejay.flashcardsmobile.dataBase.DeckDbSchema.DeckTable;


public class DeckBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "deckBase.db";

    public DeckBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DeckTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                DeckTable.Cols.UUID + ", " +
                DeckTable.Cols.TITLE + ", " +
                DeckTable.Cols.CARDS + ", " +
                DeckTable.Cols.DATE_CREATED +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
