package com.dubblejay.flashcardsmobile;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class EditCardActivity extends FragmentActivityTemplate {

    public static final String EXTRA_CARD_INDEX = "card_index";
    public static final String EXTRA_DECK_ID = "deck_id";
    public static final String EXTRA_CARD = "card";

    @Override
    protected Fragment createFragment() {
        return new EditCardFragment();
    }

    public static Intent newIntent(Context packageContext, UUID deckId, int cardIndex) {
        Intent intent = new Intent(packageContext, EditCardActivity.class);
        intent.putExtra(EXTRA_DECK_ID, deckId);
        intent.putExtra(EXTRA_CARD_INDEX, cardIndex);
        return intent;
    }

    public static Intent newIntent1(Context packageContext, UUID deckId, Card card) {
        Intent intent = new Intent(packageContext, EditCardActivity.class);
        intent.putExtra(EXTRA_DECK_ID, deckId);
        intent.putExtra(EXTRA_CARD, card);
        return intent;
    }

}
