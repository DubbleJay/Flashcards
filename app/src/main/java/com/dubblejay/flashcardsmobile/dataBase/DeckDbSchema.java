package com.dubblejay.flashcardsmobile.dataBase;

public class DeckDbSchema {

    public static final class DeckTable {

        public static final String NAME = "decks";

        public static final class Cols {

            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String CARDS = "cards";
            public static final String DATE_CREATED = "date_created";

        }
    }
}
