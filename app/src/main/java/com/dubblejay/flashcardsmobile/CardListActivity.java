package com.dubblejay.flashcardsmobile;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class CardListActivity extends FragmentActivityTemplate {

    public static final String EXTRA_DECK_ID = "com.dubblejay.flashcardsmobile";

    @Override
    protected Fragment createFragment() {
        return new CardListFragment();
    }

    public static Intent newIntent(Context packageContext, UUID deckId) {
        Intent intent = new Intent(packageContext, CardListActivity.class);
        intent.putExtra(EXTRA_DECK_ID, deckId);
        return intent;
    }

}
