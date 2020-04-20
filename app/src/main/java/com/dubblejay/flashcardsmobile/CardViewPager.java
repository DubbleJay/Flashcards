package com.dubblejay.flashcardsmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CardViewPager extends Fragment {

    private ViewPager mViewPager;
    private CardsAdapter cardsAdapter;
    private List<Card> cards;
    private CardFragment cardFragment;
    private UUID deckId;
    private Deck deck;
    private boolean isDeckShuffled = false;
    private String KEY_IS_DECK_SHUFFLED = "isDeckShuffled";
    private String KEY_SHUFFLED_CARDS = "shuffledCards";
    private int currentIndex = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        deckId = (UUID) getActivity().getIntent().getSerializableExtra(StudyDeckActivity.EXTRA_DECK_ID);
        deck =  Model.get(getActivity()).getDeck(deckId);
        cards = deck.getCards();
        if(savedInstanceState != null) {

            isDeckShuffled = savedInstanceState.getBoolean(KEY_IS_DECK_SHUFFLED);

            if(isDeckShuffled)
                cards = (ArrayList<Card>) savedInstanceState.getSerializable(KEY_SHUFFLED_CARDS);

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(KEY_IS_DECK_SHUFFLED, isDeckShuffled);

        if(isDeckShuffled)
            outState.putSerializable(KEY_SHUFFLED_CARDS, (ArrayList) cards);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_card_pager, container, false);

        mViewPager = view.findViewById(R.id.view_pager);

        if(savedInstanceState == null)
            playCardViewPagerAnimation();



        cardsAdapter = new CardsAdapter(getChildFragmentManager(), cards);

        mViewPager.setAdapter(cardsAdapter);

        return view;

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.shuffle_deck_item:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void goToNextCard() {
        if(mViewPager.getCurrentItem() != cards.size() - 1)
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
    }

    public void goToPreviousCard() {
        if(mViewPager.getCurrentItem() != 0)
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
    }

    public void shuffleCards() {
        if (!isDeckShuffled) {

            Collections.shuffle(cards);

        } else {
            deck =  Model.get(getActivity()).getDeck(deckId);

            cards = deck.getCards();
        }

        mViewPager.setAdapter(new CardsAdapter(getChildFragmentManager(), cards));

        isDeckShuffled = !isDeckShuffled;

        playCardViewPagerAnimation();

    }

    public boolean getIsDeckShuffled() {
        return isDeckShuffled;
    }

    public void editCard() {
        Intent intent = EditCardActivity.newIntent(getContext(), deckId, currentIndex);
        startActivity(intent);
    }

    private void playCardViewPagerAnimation() {
        Animation viewPagerAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.cards_view_pager_animation);

        mViewPager.setAnimation(viewPagerAnimation);
    }


    private class CardsAdapter extends FragmentStatePagerAdapter {

        List<Card> mCards;

        public CardsAdapter(FragmentManager fragmentManager, List<Card> cards) {
            super(fragmentManager);
            mCards = cards;
        }

        @Override
        public Fragment getItem(int position) {
            Card card = mCards.get(position);
            return CardFragment.newInstance(card, position + 1, cards.size());
        }

        @Override
        public int getCount() {
            return mCards.size();
        }

        @Override
        public int getItemPosition(Object object) { return POSITION_NONE; }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            if (getBlankFragment() != object) {
                cardFragment = (CardFragment) object;
                currentIndex = position;
            }
            super.setPrimaryItem(container, position, object);

        }
        public CardFragment getBlankFragment() {
            return cardFragment;
        }

        public void setCards(List<Card> cards) {
            mCards = cards;
        }

    }

}
