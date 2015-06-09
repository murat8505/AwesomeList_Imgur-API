package com.frankegan.awesomelist;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author frankegan created on 6/2/15.
 */
public class AwesomeFragment extends Fragment {

    private Integer page = 0;
    private String URL = "https://api.imgur.com/3/gallery/r/pic/" + page + ".json";
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    OnAppBarChangeListener appbarListener;

    public static AwesomeFragment getInstance() {
        AwesomeFragment awesome = new AwesomeFragment();
        return awesome;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            appbarListener = (MainActivity) activity;
        }catch (ClassCastException e){
            Log.e("frankegan", e.toString());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_with_list, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        mLayoutManager = new GridLayoutManager(container.getContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if(mAdapter == null)
            mAdapter = new MyAdapter();
        mAdapter.setOnDisplayListener((MyAdapter.OnDisplayListener) container.getContext());
        loadPage(page);//TODO re-add should load
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener((LinearLayoutManager) mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                loadPage(current_page);
            }

            @Override
            public void onShow(){
                appbarListener.onAppBarScrollIn();
            }

            @Override
            public void onHide(){
                appbarListener.onAppBarScrollOut();
            }
        });
        return v;
    }

    void loadPage(int newPage) {
        ArrayList<ImgurData> imgurDataSet = new ArrayList<>();
        Response.Listener<JSONObject> successResponse = (JSONObject response) -> {
            try {
                JSONArray responseJSONArray = response.getJSONArray("data");
                for (int i = 0; i < responseJSONArray.length(); i++) {
                    JSONObject responseObj = responseJSONArray.getJSONObject(i);
                    ImgurData datum = new ImgurData(responseObj.get("id").toString(),
                            responseObj.get("title").toString(), responseObj.get("description").toString());
                    imgurDataSet.add(datum);
                }
                mAdapter.setDataset(imgurDataSet);
                mAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        };
        setPage(newPage);//TODO is this necessary?
        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET, URL, null,
                successResponse, (VolleyError error) -> Log.e("volley", error.toString())) {
            @Override//You must set the request header
            public HashMap<String, String> getHeaders() {
                HashMap<String, String> params = new HashMap<>();
                params.put("Authorization", "Client-ID " + MyConstants.IMGUR_CLIENT_ID);
                return params;
            }
        };
        MyApp.getVolleyRequestQueue().add(jsonReq);
        imgurDataSet.clear();
    }

    public void setPage(Integer i) {
        page = i;
        URL = "https://api.imgur.com/3/gallery/r/pic/" + page + ".json";
    }
}
