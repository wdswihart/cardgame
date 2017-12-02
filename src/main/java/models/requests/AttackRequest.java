package models.requests;

import models.Card;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AttackRequest implements Serializable {
    private List<Card> mAttackingMonsters = new ArrayList<>();

    public AttackRequest() {

    }

    public AttackRequest(List<Card> attackers) {
        mAttackingMonsters = attackers;
    }

    public List<Card> getAttackingMonsters() {
        return mAttackingMonsters;
    }

    public void setAttackingMonsters(List<Card> monsters) {
        mAttackingMonsters = monsters;
    }
}
