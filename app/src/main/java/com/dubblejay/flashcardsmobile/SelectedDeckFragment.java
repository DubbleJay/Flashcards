package com.dubblejay.flashcardsmobile;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

public class SelectedDeckFragment extends Fragment {

    private Deck deck;
    private TextView deckTitleTextView;
    private TextView deckSizeTextView;
    private static final String DIALOG_DELETE_DECK = "DialogDeleteDeck";
    private static final int REQUEST_DELETE_DECK = 0;
    private static final String DIALOG_EDIT_DECK_TITLE = "DialogEditDeckTitle";
    private static final int REQUEST_EDIT_DECK_TITLE = 1;
    private FloatingActionButton fab_main, addCardsFAB, editDeleteCardsFAB;
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;
    private TextView addCardsTextView, editDeleteCardsTextView;
    private Boolean isOpen = false;
    private UUID deckId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        deckId = (UUID) getActivity().getIntent().getSerializableExtra(SelectedDeckActivity.EXTRA_DECK_ID);
        deck = Model.get(getActivity()).getDeck(deckId);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.selcected_deck, container, false);

        deckTitleTextView = view.findViewById(R.id.deckTitlTextView);

        deckSizeTextView = view.findViewById(R.id.deck_size_text_view);

        final Button reviewButton = view.findViewById(R.id.reviewButton);
        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deck.getCards().size() > 0) {
                    Intent intent = StudyDeckActivity.newIntent(getActivity(), deck.getId());
                    startActivity(intent);
                } else
                    Snackbar.make(getView().findViewById(R.id.coordinatorLayout), "This deck doesn't have any cards yet!", Snackbar.LENGTH_LONG).
                            setAction("Add Cards", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = AddNewCardsActivity.newIntent(getActivity(), deck.getId());
                                    startActivity(intent);
                                }
                            }).show();

            }
        });

        fab_main = view.findViewById(R.id.fab);
        addCardsFAB = view.findViewById(R.id.add_cards_fab);
        editDeleteCardsFAB = view.findViewById(R.id.edit_delete_cards_fab);
        fab_close = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(getContext(), R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(getContext(), R.anim.fab_rotate_anticlock);

        addCardsTextView = view.findViewById(R.id.add_cards_text_view);
        editDeleteCardsTextView = view.findViewById(R.id.edit_delete_cards_text_view);

        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isOpen) {

                    closeFab();

                } else {
                    addCardsTextView.setVisibility(View.VISIBLE);
                    editDeleteCardsTextView.setVisibility(View.VISIBLE);
                    editDeleteCardsFAB.startAnimation(fab_open);
                    addCardsFAB.startAnimation(fab_open);
                    fab_main.startAnimation(fab_clock);
                    editDeleteCardsFAB.setClickable(true);
                    addCardsFAB.setClickable(true);
                    isOpen = true;
                }

            }
        });

        editDeleteCardsFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (deck.getCards().size() > 0) {
                    Intent intent = CardListActivity.newIntent(getContext(), deck.getId());
                    startActivity(intent);
                } else
                    Snackbar.make(getView().findViewById(R.id.coordinatorLayout), "This deck doesn't have any cards yet!", Snackbar.LENGTH_LONG).
                            setAction("Add Cards", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = AddNewCardsActivity.newIntent(getActivity(), deck.getId());
                                    startActivity(intent);
                                }
                            }).show();
            }
        });

        addCardsFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AddNewCardsActivity.newIntent(getActivity(), deck.getId());
                startActivity(intent);

            }
        });

        updateUI();

        return view;

    }


    @Override
    public void onPause() {
        super.onPause();
        if (isOpen)
            closeFab();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.edit_title_item:
                EditDeckTitleFragment editDeckTitleFragment = EditDeckTitleFragment.newInstance(deck.getTitle());
                editDeckTitleFragment.setTargetFragment(SelectedDeckFragment.this, REQUEST_EDIT_DECK_TITLE);
                editDeckTitleFragment.show(getFragmentManager(), DIALOG_EDIT_DECK_TITLE);
                return true;

            case R.id.delete_deck_item:
                DeleteDeckFragment deleteDeckFragment = DeleteDeckFragment.newInstance(deck.getTitle());
                deleteDeckFragment.setTargetFragment(SelectedDeckFragment.this, REQUEST_DELETE_DECK);
                deleteDeckFragment.show(getFragmentManager(), DIALOG_DELETE_DECK);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {

            case REQUEST_DELETE_DECK:

                int resultl = data.getIntExtra(DeleteDeckFragment.EXTRA_RESULT, 0);

                if (resultl == Activity.RESULT_OK) {
                    Model.get(getContext()).deleteDeck(deck.getId());
                    Toast.makeText(getActivity(), deck.getTitle() + " deleted.", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                }
                break;

            case REQUEST_EDIT_DECK_TITLE:
                String title = data.getStringExtra(EditDeckTitleFragment.EXTRA_TITLE);
                if (!deck.getTitle().equals(title)) {
                    deck.setTitle(title);
                    Model.get(getContext()).updateDeck(deck);
                    Snackbar.make(getView(), "Title changed to " + title, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    updateUI();
                }
                break;

        }

    }

    private void closeFab() {
        addCardsTextView.setVisibility(View.INVISIBLE);
        editDeleteCardsTextView.setVisibility(View.INVISIBLE);
        editDeleteCardsFAB.startAnimation(fab_close);
        addCardsFAB.startAnimation(fab_close);
        fab_main.startAnimation(fab_anticlock);
        editDeleteCardsFAB.setClickable(false);
        addCardsFAB.setClickable(false);
        isOpen = false;
    }

    private void updateUI() {

        Model model = Model.get(getActivity());
        deck = model.getDeck(deckId);
        int cardCount = deck.getCards().size();

        String cardsString;

        if (cardCount != 1)
            cardsString = cardCount + " Cards";
        else
            cardsString = cardCount + " Card";

        deckTitleTextView.setText(deck.getTitle().length() < 26 ? deck.getTitle() : deck.getTitle().substring(0, 25) + "...");
        deckSizeTextView.setText(cardsString);

    }

    public static class DeleteDeckFragment extends DialogFragment {

        private static final String ARG_TITLE = "title";
        public static final String EXTRA_RESULT = "result";

        public static DeleteDeckFragment newInstance(String title) {
            Bundle args = new Bundle();
            args.putString(ARG_TITLE, title);
            DeleteDeckFragment fragment = new DeleteDeckFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final String title = getArguments().getString(ARG_TITLE);

            return new AlertDialog.Builder(getActivity())
                    .setTitle("Delete " + title + "?")
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

    public static class EditDeckTitleFragment extends DialogFragment {

        private static final String ARG_TITLE = "title";
        public static final String EXTRA_TITLE = "com.dubblej.flashcardsmobile.title";

        public static EditDeckTitleFragment newInstance(String title) {
            Bundle args = new Bundle();
            args.putString(ARG_TITLE, title);
            EditDeckTitleFragment fragment = new EditDeckTitleFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final String title = getArguments().getString(ARG_TITLE);

            final EditText editText = new EditText(getActivity());
            editText.setText(title);
            editText.requestFocus();
            editText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);

            return new AlertDialog.Builder(getActivity())
                    .setTitle("Name Your Deck:")
                    .setView(editText)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (editText.getText().toString().trim().length() > 0) {
                                sendResult(Activity.RESULT_OK, editText.getText().toString());
                            }

                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .create();
        }

        private void sendResult(int resultCode, String title) {
            if (getTargetFragment() == null) {
                return;
            }
            Intent intent = new Intent();
            intent.putExtra(EXTRA_TITLE, title);
            getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
        }
    }
}