package footballcounter.project.footballcounterlive;

public class CustomScoreItem {
    private String mCustomName;
    private String mCustomUrl;
//    private int mFlagImage;

    public CustomScoreItem(String customName, String url) {
        mCustomName = customName;
        mCustomUrl = url;
//        mFlagImage = flagImage;
    }

    public String getCustomName() {
        return mCustomName;
    }

    public String getCustomUrl() {
        return mCustomUrl;
    }

//    public int getFlagImage() {
//        return mFlagImage;
//    }
}