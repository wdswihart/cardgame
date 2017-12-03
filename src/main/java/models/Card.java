package models;

public class Card extends ModelBase {
    private String mName = "";

    private String mDescription = "";

    private int mPower = 0;

    private int mToughness = 0;


    public Card() {

    }

    public Card(String name) {
        mName = name;
    }


    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    @Override
    public boolean isDefault() {
        //TODO: Update this with more infos.
        return mName.isEmpty();
    }

    @Override
    public boolean equals(Object obj) {
        Card card = (Card) obj;

        if (card == null ||
                !card.getName().equals(mName) ||
                !card.getDescription().equals(mDescription) ||
                card.getPower() != mPower ||
                card.getToughness() != mToughness) {
            return false;
        }

        if (card.isDefault()) {
            //If the objects are default, check by addresses.
            return this == obj;
        }

        return true;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public int getPower() {
        return mPower;
    }

    public void setPower(int mPower) {
        this.mPower = mPower;
    }

    public int getToughness() {
        return mToughness;
    }

    public void setToughness(int mToughness) {
        this.mToughness = mToughness;
    }
}
