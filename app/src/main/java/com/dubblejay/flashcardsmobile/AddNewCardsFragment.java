package com.dubblejay.flashcardsmobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.UUID;

public class AddNewCardsFragment extends Fragment {

    private Deck deck;
    private EditText questionEditText;
    private EditText answerEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        UUID deckId = (UUID) getActivity().getIntent().getSerializableExtra(AddNewCardsActivity.EXTRA_DECK_ID);
        deck =  Model.get(getActivity()).getDeck(deckId);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_cards, container, false);

        questionEditText = view.findViewById(R.id.questionEditText);
        answerEditText = view.findViewById(R.id.answerEditText);

        return view;
    }
    @Override
    public void onPause() {
        super.onPause();
        Model.get(getContext()).updateDeck(deck);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add_card_menu, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_confirm:
                if(!questionEditText.getText().toString().trim().isEmpty() && !answerEditText.getText().toString().trim().isEmpty()) {
                    deck.getCards().add(new Card(questionEditText.getText().toString().trim(), answerEditText.getText().toString().trim()));
                    questionEditText.getText().clear();
                    answerEditText.getText().clear();
                    answerEditText.clearFocus();
                    questionEditText.requestFocus();
                    Toast.makeText(getActivity(), "Card Added to " + deck.getTitle(), Toast.LENGTH_LONG).show();
                }

                else
                    Toast.makeText(getActivity(), "Please Enter a Question and an Answer", Toast.LENGTH_LONG).show();

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
