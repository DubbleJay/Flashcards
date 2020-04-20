package com.dubblejay.flashcardsmobile;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

public class CardFragment extends Fragment {

    private static final String ARG_CARD = "card";
    private static final String ARG_CARD_INDEX = "card_index";
    private static final String ARG_DECK_SIZE = "deck_size";
    private Card card;
    private int cardIndex;
    private int deckSize;
    private TextView frontTextView;
    private TextView backTextView;
    private AnimatorSet mSetRightOut;
    private AnimatorSet mSetLeftIn;
    private boolean mIsFrontVisible = true;
    private View mCardFrontLayout;
    private View mCardBackLayout;

    private static final String KEY_IS_FRONT_VISIBLE = "isfrontvisible";

    public static CardFragment newInstance(Card card, int cardIndex, int deckSize) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CARD, card);
        args.putInt(ARG_CARD_INDEX, cardIndex);
        args.putInt(ARG_DECK_SIZE, deckSize);
        CardFragment cardFragment = new CardFragment();
        cardFragment.setArguments(args);
        return cardFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        card = (Card) getArguments().getSerializable(ARG_CARD);
        cardIndex = getArguments().getInt(ARG_CARD_INDEX);
        deckSize = getArguments().getInt(ARG_DECK_SIZE);

        if (savedInstanceState != null) {
            mIsFrontVisible = savedInstanceState.getBoolean(KEY_IS_FRONT_VISIBLE);
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_IS_FRONT_VISIBLE, mIsFrontVisible);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        int layout = mIsFrontVisible ? R.layout.card_fragment : R.layout.card_fragment_flipped;

        View view = inflater.inflate(layout, container, false);

        final FrameLayout frameLayout = view.findViewById(R.id.card_frame_layout);

        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipCard();
            }
        });

        TextView cardIndexTextView = view.findViewById(R.id.card_index_text_view);
        cardIndexTextView.setText(cardIndex + " / " + deckSize);
        mCardBackLayout = view.findViewById(R.id.card_back);
        mCardFrontLayout = view.findViewById(R.id.card_front);

        loadAnimations();
        changeCameraDistance();

        frontTextView = view.findViewById(R.id.front_text_view);
        frontTextView.setText(card.getQuestion());
        backTextView = view.findViewById(R.id.back_text_view);
        backTextView.setText(card.getAnswer());

        frontTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipCard();
            }
        });

        backTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipCard();
            }
        });

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(!isVisibleToUser){

            if(!mIsFrontVisible)
                flipCard();
        }
    }

    private void changeCameraDistance() {
        int distance = 8000;
        float scale = getResources().getDisplayMetrics().density * distance;
        mCardFrontLayout.setCameraDistance(scale);
        mCardBackLayout.setCameraDistance(scale);
    }

    private void loadAnimations() {
        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.flip_anim_1);
        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.flip_anim_2);
    }

    public void flipCard() {

        if(!mSetLeftIn.isRunning() && !mSetRightOut.isRunning()) {
            mSetRightOut.setTarget(!mIsFrontVisible ? mCardFrontLayout : mCardBackLayout);
            mSetLeftIn.setTarget(!mIsFrontVisible ? mCardBackLayout : mCardFrontLayout);
            mSetRightOut.start();
            mSetLeftIn.start();
            mIsFrontVisible = !mIsFrontVisible;

        }
    }

    public Card getCard() {
        return card;
    }
}
