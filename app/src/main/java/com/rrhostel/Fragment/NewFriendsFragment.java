package com.rrhostel.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rrhostel.Adapters.MyFriendListAdapter;
import com.rrhostel.Bean.UserFriedListBean;
import com.rrhostel.R;

import java.util.ArrayList;


public class NewFriendsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    private Context mContext;
    private RecyclerView recyclerListFrends;
    private MyFriendListAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private View mMainView;

    ArrayList<UserFriedListBean> myFriendsBeen;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mMainView = inflater.inflate(R.layout.fragment_people, container, false);

        lazyLoad();
        return mMainView;
    }


    protected void lazyLoad() {


        myFriendsBeen = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerListFrends = (RecyclerView) mMainView.findViewById(R.id.recycleListFriend);
        recyclerListFrends.setLayoutManager(linearLayoutManager);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mMainView.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        setFriendlist(myFriendsBeen);

    }

    private ArrayList<UserFriedListBean> setList() {
        ArrayList<UserFriedListBean> aMyFriendsBeen = new ArrayList<>();
        for (int i = 1; i < 6; i++) {

            UserFriedListBean userFriedListBean = aMyFriendsBeen.get(i);
            userFriedListBean.setFirstName("RJ" + i);
            userFriedListBean.setFrdEmailId("rj@gmail.com" + i);
        }

        return aMyFriendsBeen;
    }


    private void setFriendlist(ArrayList<UserFriedListBean> aMyFriendsBeen) {

        myFriendsBeen = setList();

        MyFriendListAdapter requestedFriendsAdapter = new MyFriendListAdapter(getActivity(), myFriendsBeen);
        recyclerListFrends.setAdapter(requestedFriendsAdapter);
    }


    /*private void handleSentFriendList(final String aStrUID) {

        boolean internetAvailable = Utils.isConnectingToInternet(mContext);

        if (internetAvailable) {
            String baseUrl = Constant.API_MY_FRIEND_LIST;
            showProgressBar();
            StringRequest mStrRequest = new StringRequest(Request.Method.POST, baseUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Gson gson = new GsonBuilder().create();
                                JsonParser jsonParser = new JsonParser();
                                JsonObject jsonResp = jsonParser.parse(response).getAsJsonObject();
                                UserFriendsListResponceBean listResponceBean = gson.fromJson(jsonResp, UserFriendsListResponceBean.class);

                                hideProgressBar();
                                if (listResponceBean != null && listResponceBean.getFrdsentListing() != null) {
                                    if (Utils.isStatusSuccess(listResponceBean.getStatus()) && listResponceBean.getFrdsentListing().size() > 0) {
                                        setFriendlist(listResponceBean.getFrdsentListing());
                                    } else {
                                        UIUtils.showToast(mContext, listResponceBean.getMsg());
                                    }
                                } else {
                                    mNoSearch.setVisibility(View.VISIBLE);
                                    mNoInternetFullScreenRl.setVisibility(View.GONE);
                                    mRlFind.setVisibility(View.VISIBLE);
                                    mRlFrdList.setVisibility(View.GONE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                mNoSearch.setVisibility(View.VISIBLE);
                                mNoInternetFullScreenRl.setVisibility(View.GONE);
                                mRlFrdList.setVisibility(View.GONE);
                                mRlFind.setVisibility(View.VISIBLE);
                                hideProgressBar();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error instanceof NoConnectionError) {
                                UIUtils.showToast(mContext, mContext.getResources().getString(R.string.InternetErrorMsg));
                                mNoSearch.setVisibility(View.GONE);
                                mNoInternetFullScreenRl.setVisibility(View.VISIBLE);
                                mRlFind.setVisibility(View.GONE);
                                mRlFrdList.setVisibility(View.GONE);
                            } else {
                                UIUtils.showToast(mContext, mContext.getResources().getString(R.string.VolleyErrorMsg));
                            }
                            hideProgressBar();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(Constant.RESPONSE_USER_ID_KEY, aStrUID);
                    return params;
                }
            };
            mStrRequest.setShouldCache(false);
            mStrRequest.setTag("");
            AppController.getInstance().addToRequestQueue(mStrRequest);
        } else {
            mNoSearch.setVisibility(View.GONE);
            mNoInternetFullScreenRl.setVisibility(View.VISIBLE);
            mRlFrdList.setVisibility(View.GONE);
            mRlFind.setVisibility(View.GONE);
        }
    }*/

    @Override
    public void onRefresh() {
        adapter.notifyDataSetChanged();
    }
}
