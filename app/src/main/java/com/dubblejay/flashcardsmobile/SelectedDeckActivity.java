package com.dubblejay.flashcardsmobile;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.Menu;

import java.util.UUID;

public class SelectedDeckActivity extends FragmentActivityTemplate {

    public static final String EXTRA_DECK_ID = "com.dubblejay.flashcardsmobile.deck_id";

    @Override
    protected Fragment createFragment() {
        return new SelectedDeckFragment();
    }

    public static Intent newIntent(Context packageContext, UUID deckId) {
        Intent intent = new Intent(packageContext, SelectedDeckActivity.class);
        intent.putExtra(EXTRA_DECK_ID, deckId);
        return intent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.deck_options_menu, menu);
        return false;
    }


}
