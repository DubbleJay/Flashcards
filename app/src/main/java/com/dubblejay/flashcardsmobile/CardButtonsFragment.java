package com.dubblejay.flashcardsmobile;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class CardButtonsFragment extends Fragment {

    private ImageButton backButton;
    private ImageButton shuffleButton;
    private ImageButton nextButton;
    private CardNavButtonsListener listener;


    public interface CardNavButtonsListener {
        void onNextButtonPressed();
        void onBackButtonPressed();
        void onShuffleButtonPressed();
        boolean isDeckShuffled();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.card_buttons_fragment_, container, false);

        backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onBackButtonPressed();
            }
        });

        shuffleButton = view.findViewById(R.id.shuffleButton);
        shuffleButton.setBackgroundResource(listener.isDeckShuffled() ? R.drawable.blue_shuffle_icon : R.drawable.shuffle_off_icon);

        shuffleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onShuffleButtonPressed();
                if (listener.isDeckShuffled())
                    shuffleButton.setBackgroundResource(R.drawable.blue_shuffle_icon);

                else
                    shuffleButton.setBackgroundResource(R.drawable.shuffle_off_icon);
            }
        });

        nextButton = view.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNextButtonPressed();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof CardNavButtonsListener)
            listener = (CardNavButtonsListener) context;

        else {
            throw new RuntimeException(context.toString() + " must implement interface listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

}

