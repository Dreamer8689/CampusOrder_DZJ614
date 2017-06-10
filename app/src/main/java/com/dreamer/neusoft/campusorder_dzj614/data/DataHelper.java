package com.dreamer.neusoft.campusorder_dzj614.data;

import android.content.Context;
import android.widget.Filter;

import com.dreamer.neusoft.campusorder_dzj614.javaBean.FoodBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by DZJ-PC on 2017/5/26.
 */

public class DataHelper {

    private static final String COLORS_FILE_NAME = "colors.json";

    private static List<FoodBean> sColorWrappers = new ArrayList<>();

    private static List<Suggestion> sColorSuggestions =
            new ArrayList<>(Arrays.asList(
                    new Suggestion("酸菜鱼"),
                    new Suggestion("口水鸡"),
                    new Suggestion("毛血旺"),
                    new Suggestion("麻辣豆腐"),
                    new Suggestion("烤鱼"),
                    new Suggestion("木桶饭"),
                    new Suggestion("香辣烤鱼"),
                    new Suggestion("白灼虾"),
                    new Suggestion("乱炖"),
                    new Suggestion("五花肉"),
                    new Suggestion("酸菜丸子"),
                    new Suggestion("辣炒蚬子"),
                    new Suggestion("fish"),
                    new Suggestion("糖醋排骨")

            ));

    public interface OnFindColorsListener {
        void onResults(List<ColorWrapper> results);
    }

    public interface OnFindSuggestionsListener {
        void onResults(List<Suggestion> results);
    }

    public static List<Suggestion> getHistory(Context context, int count) {

        List<Suggestion> suggestionList = new ArrayList<>();
        Suggestion suggestion;
        for (int i = 0; i < sColorSuggestions.size(); i++) {
            suggestion = sColorSuggestions.get(i);
            suggestion.setIsHistory(true);
            suggestionList.add(suggestion);
            if (suggestionList.size() == count) {
                break;
            }
        }
        return suggestionList;
    }

    public static void resetSuggestionsHistory() {
        for (Suggestion colorSuggestion : sColorSuggestions) {
            colorSuggestion.setIsHistory(false);
        }
    }

    public static void findSuggestions(Context context, String query, final int limit, final long simulatedDelay,
                                       final OnFindSuggestionsListener listener) {
        new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                try {
                    Thread.sleep(simulatedDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                DataHelper.resetSuggestionsHistory();
                List<Suggestion> suggestionList = new ArrayList<>();
                if (!(constraint == null || constraint.length() == 0)) {

                    for (Suggestion suggestion : sColorSuggestions) {
                        if (suggestion.getBody()
                                .startsWith(constraint.toString())) {

                            suggestionList.add(suggestion);
                            if (limit != -1 && suggestionList.size() == limit) {
                                break;
                            }
                        }
                    }
                }

                FilterResults results = new FilterResults();
                Collections.sort(suggestionList, new Comparator<Suggestion>() {
                    @Override
                    public int compare(Suggestion lhs, Suggestion rhs) {
                        return lhs.getIsHistory() ? -1 : 0;
                    }
                });
                results.values = suggestionList;
                results.count = suggestionList.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if (listener != null) {
                    listener.onResults((List<Suggestion>) results.values);
                }
            }
        }.filter(query);

    }






}
