package com.dubblejay.flashcardsmobile;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MainMenuFragment extends Fragment {

    private RecyclerView decksRecyclerView;
    private DecksAdapter decksAdapter;
    private static final String DIALOG_CREATE_NEW_DECK = "DialogCreateNewDeck";
    private TextView chooseADeckTextView;
    private TextView noDecksTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_menu, container, false);

        decksRecyclerView = view.findViewById(R.id.decksRecyclerView);
        decksRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        chooseADeckTextView = view.findViewById(R.id.chooseADeckTextView);

        noDecksTextView = view.findViewById(R.id.noDecksTextView);

        FloatingActionButton addDeckFAB  = view.findViewById(R.id.add_deck_fab);
        addDeckFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewDeckFragment createNewDeckFragment = new CreateNewDeckFragment();
                createNewDeckFragment.show(getFragmentManager(), DIALOG_CREATE_NEW_DECK);
            }
        });

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {

        Model model = Model.get(getActivity());

        List<Deck> decks = model.getDecks();

        if (decksAdapter == null) {
            decksAdapter = new DecksAdapter(decks);
            decksRecyclerView.setAdapter(decksAdapter);

        } else {
            decksAdapter.setDecks(decks);
            decksAdapter.notifyDataSetChanged();
        }

        if(decks.size() > 0) {
            noDecksTextView.setVisibility(View.GONE);
            chooseADeckTextView.setVisibility(View.VISIBLE);
        }

        else {
            chooseADeckTextView.setVisibility(View.GONE);
            noDecksTextView.setVisibility(View.VISIBLE);
        }

        String subtitle;

        if(decks.size() != 1)
            subtitle = decks.size() + " Decks";
        else
            subtitle = decks.size()  + " Deck";

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);

    }

    private class DeckHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView titleTextView;
        private ImageView deckImage;
        private Deck deck;

        public DeckHolder(View itemView)  {
            super(itemView);
            itemView.setOnClickListener(this);
            titleTextView = itemView.findViewById(R.id.deck_title_text_view);
            deckImage = itemView.findViewById(R.id.deck_image);
        }

        @Override
        public void onClick(View v) {

            Intent intent = SelectedDeckActivity.newIntent(getActivity(), deck.getId());

            Pair[] pairs = new Pair[2];
            pairs[0] = new Pair<View, String>(titleTextView, "deck_title_transition");
            pairs[1] = new Pair<View, String>(deckImage, "deck_image_transition");

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pairs);

            startActivity(intent, options.toBundle());
        }


        public void bindDeck(Deck deck) {
            this.deck = deck;
            titleTextView.setText(deck.getTitle().length() < 26 ? deck.getTitle() : deck.getTitle().substring(0, 26) + "...");
        }

    }

    private class DecksAdapter extends RecyclerView.Adapter<DeckHolder>  {

        List<Deck> decks;

        public DecksAdapter(List<Deck> decks) {
            this.decks = decks;
        }

        @Override
        public DeckHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.deck_row, parent, false);
            return new DeckHolder(view);
        }

        @Override
        public void onBindViewHolder(DeckHolder holder, int position) {
            Deck deck = decks.get(position);
            holder.bindDeck(deck);
        }

        @Override
        public int getItemCount() {
            return decks.size();
        }

        public void setDecks(List<Deck> decks) {
            this.decks = decks;
        }

    }

    public static class CreateNewDeckFragment extends DialogFragment {

        private EditText editText;
        private String KEY_EDIT_TEXT_STRING = "editTextString";

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            editText = new EditText(getActivity());
            editText.setText(savedInstanceState == null ? "Untitled Deck" : savedInstanceState.getString(KEY_EDIT_TEXT_STRING));
            editText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);

            return new AlertDialog.Builder(getActivity())
                    .setTitle("Name Your Deck:")
                    .setView(editText)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Deck deck = new Deck();

                            if(!editText.getText().toString().isEmpty())
                                deck.setTitle(editText.getText().toString());

                            else
                                deck.setTitle("Untitled Deck");

                            Model.get(getContext()).addDeck(deck);

                            Intent intent = SelectedDeckActivity.newIntent(getContext(), deck.getId());
                            startActivity(intent);

                        }



                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .create();
        }

        public void onResume()
        {
            super.onResume();
            Window window = getDialog().getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);
        }

        @Override
        public void onSaveInstanceState(@NonNull Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putString(KEY_EDIT_TEXT_STRING, editText.getText().toString());
        }
    }
}
