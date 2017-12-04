package models.responses;

import models.Card;

import java.util.ArrayList;
import java.util.List;

public class CardList {
    private List<Card> mCards = new ArrayList<>();

    public List<Card> getCards() {
        return mCards;
    }

    public void setCards(List<Card> cards) {
        mCards = cards;
    }
}
