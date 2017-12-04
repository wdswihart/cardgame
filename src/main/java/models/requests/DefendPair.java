package models.requests;

import models.Card;

import java.io.Serializable;

public class DefendPair implements Serializable {
    private Card mAttacker = new Card();
    private Card mDefender = new Card();

    public DefendPair() {

    }

    public DefendPair(Card attacker, Card defender) {
        mAttacker = attacker;
        mDefender = defender;
    }

    public void setAttacker(Card card) {
        mAttacker = card;
    }

    public Card getAttacker() {
        return mAttacker;
    }

    public void setDefender(Card card) {
        mDefender = card;
    }

    public Card getDefender() {
        return mDefender;
    }
}
