package com.dubblejay.flashcardsmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CardListFragment extends Fragment {

    private RecyclerView cardsRecyclerView;
    private CardsAdapter cardsAdapter;
    private Deck deck;
    private UUID deckId;
    private TextView deckSizeTextView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        deckId = (UUID) getActivity().getIntent().getSerializableExtra(CardListActivity.EXTRA_DECK_ID);
        deck =  Model.get(getActivity()).getDeck(deckId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.edit_cards_list, container, false);

        TextView deckTitleTextView = view.findViewById(R.id.edit_cards_deck_title_text_view);
        deckTitleTextView.setText(deck.getTitle());

        deckSizeTextView = view.findViewById(R.id.edit_cards_deck_size_text_view);
        String cardsSizeString = deck.getCards().size() != 1 ? " Cards" : " Card";
        deckSizeTextView.setText(deck.getCards().size() + cardsSizeString);

        cardsRecyclerView = view.findViewById(R.id.cards_recycler_view);
        cardsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder dragged, @NonNull RecyclerView.ViewHolder target) {

                int position_dragged = dragged.getAdapterPosition();
                int position_target = target.getAdapterPosition();

                Collections.swap(deck.getCards(), position_dragged, position_target);

                cardsAdapter.notifyItemMoved(position_dragged, position_target);

                Model.get(getContext()).updateDeck(deck);

                return false;
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                updateUI();
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

            }

        });

        helper.attachToRecyclerView(cardsRecyclerView);

        updateUI();

        if(savedInstanceState == null) {
            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.cards_layout_animation);
            cardsRecyclerView.setLayoutAnimation(animation);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        Model model = Model.get(getActivity());
        deck = model.getDeck(deckId);
        List<Card> cards = deck.getCards();

        if(cards.size() > 0) {
            if (cardsAdapter == null) {
                cardsAdapter = new CardsAdapter(cards);
                cardsRecyclerView.setAdapter(cardsAdapter);
            } else {
                cardsAdapter.notifyDataSetChanged();
                cardsAdapter.setCards(cards);
            }
            String cardsSizeString = deck.getCards().size() != 1 ? " Cards" : " Card";

            deckSizeTextView.setText(deck.getCards().size() + cardsSizeString);


        }

        else
            getActivity().finish();

    }

    public class CardsAdapter extends RecyclerView.Adapter<CardHolder> {

        private List<Card> mCards;

        public CardsAdapter(List<Card> cards) {
            mCards = cards;
        }

        @Override
        public CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.card_row, parent, false);
            return new CardHolder(view);
        }

        @Override
        public void onBindViewHolder(CardHolder holder, int position) {
            Card card = mCards.get(position);
            holder.bindCard(card);
        }
        @Override
        public int getItemCount() {
            return mCards.size();
        }

        public void setCards(List<Card> cards) {
            mCards = cards;
        }

    }

    public class CardHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView cardIndexTextView;
        private TextView mQuestionTextView;
        private TextView mAnswerTextView;
        private Card mCard;
        View rowView;

        public CardHolder(View itemView)  {
            super(itemView);
            itemView.setOnClickListener(this);
            cardIndexTextView = itemView.findViewById(R.id.card_index_text_view);
            mQuestionTextView = itemView.findViewById(R.id.edit_cards_question_text_view);
            mAnswerTextView = itemView.findViewById(R.id.edit_cards_answer_text_view);
            rowView = itemView;
        }

        @Override
        public void onClick(View v) {
            Intent intent = EditCardActivity.newIntent(getActivity(), deckId, getAdapterPosition());

            startActivity(intent);
        }

        public void bindCard(Card card) {
            mCard = card;
            cardIndexTextView.setText(getAdapterPosition() + 1 + " / " + deck.getCards().size());
            mQuestionTextView.setText(mCard.getQuestion());
            mAnswerTextView.setText( mCard.getAnswer());
        }
    }
}





