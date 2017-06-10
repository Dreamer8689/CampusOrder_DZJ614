package com.dreamer.neusoft.campusorder_dzj614.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.dreamer.neusoft.campusorder_dzj614.Adapter.SearchResultsListAdapter;
import com.dreamer.neusoft.campusorder_dzj614.R;
import com.dreamer.neusoft.campusorder_dzj614.data.DataHelper;
import com.dreamer.neusoft.campusorder_dzj614.data.Suggestion;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.FoodBean;
import com.dreamer.neusoft.campusorder_dzj614.model.Api.CookApi;
import com.dreamer.neusoft.campusorder_dzj614.model.Service.ShopService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    private final String TAG = "BlankFragment";

    public static final long FIND_SUGGESTION_SIMULATED_DELAY = 250;

    private FloatingSearchView mSearchView;

    private RecyclerView mSearchResultsList;
    private SearchResultsListAdapter mSearchResultsAdapter;

    private boolean mIsDarkSearchTheme = false;
    private CookApi cookApi;
    private ShopService shopService;
    private String mLastQuery = "";
    private  View view;
    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

      view= inflater.inflate(R.layout.fragment_search, container, false);

        initView();
        initData();
        setupFloatingSearch();
        Mylistener();
        return view;
    }

    private void setupFloatingSearch() {
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mSearchView.clearSuggestions();
                } else {
                    mSearchView.showProgress();

                    DataHelper.findSuggestions(getActivity(), newQuery, 5,
                            FIND_SUGGESTION_SIMULATED_DELAY, new DataHelper.OnFindSuggestionsListener() {

                                @Override
                                public void onResults(List<Suggestion> results) {

                                    mSearchView.swapSuggestions(results);

                                    mSearchView.hideProgress();
                                }
                            });

                }

                Log.d(TAG, "onSearchTextChanged()");
            }
        });

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

               Suggestion suggestion = (Suggestion) searchSuggestion;
                mLastQuery = searchSuggestion.getBody();
                    searchDish(mLastQuery);




            }

            @Override
            public void onSearchAction(String currentQuery) {

            }
        });

    }

    private void searchDish(String mLastQuery) {
        Call<List<FoodBean>> call=shopService.toSearch(mLastQuery);
          call.enqueue(new Callback<List<FoodBean>>() {
              @Override
              public void onResponse(Call<List<FoodBean>> call, Response<List<FoodBean>> response) {
                  if (response.isSuccessful()) {
                      List<FoodBean> Result=response.body();
                      mSearchResultsList.setAdapter(new SearchResultsListAdapter(getActivity(),Result));
                  }else{
                      Toast.makeText(getActivity(), "返回数据失败", Toast.LENGTH_SHORT).show();
                  }
              }

              @Override
              public void onFailure(Call<List<FoodBean>> call, Throwable t) {
                  Toast.makeText(getActivity(), "网络请求失败", Toast.LENGTH_SHORT).show();
              }
          });

    }


    private void Mylistener() {
    }

    private void initData() {
    }

    private void initView() {
        mSearchView = (FloatingSearchView) view.findViewById(R.id.floating_search_view);
        mSearchResultsList = (RecyclerView) view.findViewById(R.id.search_results_list);
        mSearchResultsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        cookApi=new CookApi(1);
        shopService=cookApi.getShopService();
    }


}
