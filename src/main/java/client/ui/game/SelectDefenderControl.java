package client.ui.game;

import models.Card;

public class SelectDefenderControl {
    private AddDefenderCallback mCallback;

    private Card mCardRef;
    private Card card;

    public void initialize() {

    }

    public void setAddCallback(AddDefenderCallback callback) {
        mCallback = callback;
    }

    public void addAction() {
        if (mCallback == null) {
            return;
        }

        mCallback.call(mCardRef);
    }

    public void setCard(Card card) {
        this.mCardRef = card;
    }
}
