package com.dreamer.neusoft.campusorder_dzj614.data;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

/**
 * Created by DZJ-PC on 2017/5/28.
 */

public class Suggestion implements SearchSuggestion {

    private String DishName;
    private boolean mIsHistory = false;

    public Suggestion(String suggestion) {
        this.DishName = suggestion.toLowerCase();
    }

    public Suggestion(Parcel source) {
        this.DishName = source.readString();
        this.mIsHistory = source.readInt() != 0;
    }

    public void setIsHistory(boolean isHistory) {
        this.mIsHistory = isHistory;
    }

    public boolean getIsHistory() {
        return this.mIsHistory;
    }

    @Override
    public String getBody() {
        return DishName;
    }

    public static final Creator<Suggestion> CREATOR = new Creator<Suggestion>() {
        @Override
        public Suggestion createFromParcel(Parcel in) {
            return new Suggestion(in);
        }

        @Override
        public Suggestion[] newArray(int size) {
            return new Suggestion[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(DishName);
        dest.writeInt(mIsHistory ? 1 : 0);
    }
}