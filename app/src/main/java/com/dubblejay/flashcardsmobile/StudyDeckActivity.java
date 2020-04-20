package com.dubblejay.flashcardsmobile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.widget.Toast;

import java.util.UUID;

public class StudyDeckActivity extends FragmentActivity implements CardButtonsFragment.CardNavButtonsListener {

    private CardViewPager cardViewPager;
    private CardButtonsFragment cardButtonsFragment;
    public static final String EXTRA_DECK_ID = "com.dubblejay.flashcardsmobile.deck_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.study_deck);

        FragmentManager fragmentManager = getSupportFragmentManager();

        cardViewPager = (CardViewPager)fragmentManager.findFragmentById(R.id.container_card);
        cardButtonsFragment= (CardButtonsFragment) fragmentManager.findFragmentById(R.id.container_nav_buttons);

        if(cardViewPager == null && cardButtonsFragment == null) {

            cardViewPager = new CardViewPager();
            cardButtonsFragment = new CardButtonsFragment();

            fragmentManager.beginTransaction()
                    .add(R.id.container_card, cardViewPager)
                    .add(R.id.container_nav_buttons, cardButtonsFragment)
                    .commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.study_deck_menu, menu);
        return false;
    }

    public static Intent newIntent(Context packageContext, UUID deckId) {
        Intent intent = new Intent(packageContext, StudyDeckActivity.class);
        intent.putExtra(EXTRA_DECK_ID, deckId);
        return intent;
    }

    @Override
    public void onNextButtonPressed() {
        cardViewPager.goToNextCard();
    }

    @Override
    public void onBackButtonPressed() {
        cardViewPager.goToPreviousCard();
    }

    @Override
    public void onShuffleButtonPressed() {
        cardViewPager.shuffleCards();
        Toast.makeText(this, isDeckShuffled() ? "Shuffle On" : "Shuffle Off", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean isDeckShuffled() {
        return cardViewPager.getIsDeckShuffled();
    }


}
