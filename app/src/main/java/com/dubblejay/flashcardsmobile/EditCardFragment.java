package com.dubblejay.flashcardsmobile;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.UUID;


public class EditCardFragment extends Fragment {

    private Card card;
    private static final int REQUEST_DELETE_CARD = 0;
    private static final String DIALOG_DELETE_CARD = "DialogDeleteCard";
    private EditText questionEditText;
    private EditText answerEditText;
    private UUID deckId;
    private Deck deck;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        deckId = (UUID) getActivity().getIntent().getSerializableExtra(EditCardActivity.EXTRA_DECK_ID);
        deck =  Model.get(getActivity()).getDeck(deckId);

        card = deck.getCards().get(getActivity().getIntent().getIntExtra(EditCardActivity.EXTRA_CARD_INDEX, 0));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.edit_card, container, false);

        questionEditText = view.findViewById(R.id.questionEditText);
        questionEditText.setText(card.getQuestion());

        answerEditText = view.findViewById(R.id.answerEditText);
        answerEditText.setText(card.getAnswer());

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
        inflater.inflate(R.menu.edit_card_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_delete:

                DeleteCardFragment deleteCardFragment = new DeleteCardFragment();
                deleteCardFragment.setTargetFragment(EditCardFragment.this, REQUEST_DELETE_CARD);
                deleteCardFragment.show(getFragmentManager(), DIALOG_DELETE_CARD);
                break;

            case R.id.menu_confirm:

                card.setQuestion(questionEditText.getText().toString());
                card.setAnswer(answerEditText.getText().toString());
                Model.get(getContext()).updateDeck(deck);

                Toast.makeText(getActivity(), "Changes Saved", Toast.LENGTH_LONG).show();
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {

            case REQUEST_DELETE_CARD:

                int resultl = data.getIntExtra(DeleteCardFragment.EXTRA_RESULT, 0);

                if(resultl == Activity.RESULT_OK) {

                    deck.getCards().remove(card);

                    getActivity().finish();
                    Toast.makeText(getContext(), "Card Deleted", Toast.LENGTH_LONG).show();

                }
                break;

        }

    }

    public static class DeleteCardFragment extends DialogFragment {

        public static final String EXTRA_RESULT = "result";

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            return new AlertDialog.Builder(getActivity())
                    .setTitle("Delete this card?")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sendResult(Activity.RESULT_OK);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .create();
        }

        private void sendResult(int resultCode) {
            if (getTargetFragment() == null) {
                return;
            }
            Intent intent = new Intent();
            intent.putExtra(EXTRA_RESULT, resultCode);
            getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
        }

    }

}



