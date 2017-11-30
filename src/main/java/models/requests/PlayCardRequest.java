package models.requests;

import models.Card;

import java.io.Serializable;

public class PlayCardRequest implements Serializable {
    private Card mCard = new Card();

    public PlayCardRequest() {

    }

    public PlayCardRequest(Card card) {
        mCard = card;
    }

    public Card getCard() {
        return mCard;
    }

    public void setCard(Card card) {
        mCard = card;
    }
}
