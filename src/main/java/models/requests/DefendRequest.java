package models.requests;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DefendRequest implements Serializable {
    private List<DefendPair> mDefendPairs = new ArrayList<>();

    public void setDefendPairs(List<DefendPair> pairs) {
        mDefendPairs = pairs;
    }

    public List<DefendPair> getDefendPairs() {
        return mDefendPairs;
    }

    public void addDefendPair(DefendPair defendPair) {
        mDefendPairs.add(defendPair);
    }
}
