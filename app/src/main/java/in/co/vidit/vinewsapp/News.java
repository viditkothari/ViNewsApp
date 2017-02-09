package in.co.vidit.vinewsapp;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;

class News {
    private String mSection;
    private String mTitle;
    private String mURL;
    private String mAuthor;
    private String mImgURL;
    private String mDate;

    News(String mSection, String mTitle, String mURL, String mAuthor, String mImgURL, String mDate) {
        this.mSection = mSection;
        this.mTitle = mTitle;
        this.mURL = mURL;
        this.mAuthor = mAuthor;
        this.mImgURL = mImgURL;
        this.mDate = mDate;
    }

    String getmSection() {
        return mSection;
    }

    String getmTitle() {
        return mTitle;
    }

    String getmURL() {
        return mURL;
    }

    String getmAuthor() {
        return mAuthor;
    }

    String getmImgURL() {
        return mImgURL;
    }

    boolean hasImg(){
        if(!TextUtils.isEmpty(mImgURL))
            return true;
        else
            return false;
    }

    String getmDate() {
        return mDate;
    }
}
