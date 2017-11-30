package models;

public class Card extends ModelBase {
    private String mName = "test-name";

    private String mDescription = "test-desc";

    private int mPower = 0;

    private int mToughness = 0;

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
