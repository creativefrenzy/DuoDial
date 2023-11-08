package com.privatepe.app.fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.privatepe.app.R;
import com.privatepe.app.adapter.HomeUserAdapter;
import com.privatepe.app.adapter.OfferImageAdapter;
import com.privatepe.app.model.UserListResponse;
import com.privatepe.app.retrofit.ApiManager;
import com.privatepe.app.retrofit.ApiResponseInterface;
import com.privatepe.app.utils.Constant;
import com.privatepe.app.utils.PaginationAdapterCallback;
import com.privatepe.app.utils.PaginationScrollListener;
import com.privatepe.app.utils.SessionManager;

import java.util.List;


import pl.pzienowicz.autoscrollviewpager.AutoScrollViewPager;

/**
 * A simple {@link Fragment} subclass.
 */
public class NearbyFragment extends Fragment implements ApiResponseInterface, PaginationAdapterCallback {
    private String AppID;

    private AutoScrollViewPager offerBanner;
    OfferImageAdapter offerImageAdapter;

    RecyclerView userList;
    HomeUserAdapter homeUserAdapter;
    List<UserListResponse.Data> list;
    ApiManager apiManager;
    GridLayoutManager gridLayoutManager;

    private static final int PAGE_START = 1;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private int TOTAL_PAGES;
    private int currentPage = PAGE_START;

    private TextView tv_popular, tv_newone;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ViewGroup viewGroup;

    //AppLifecycle appLifecycle;
    public NearbyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        viewGroup = view.findViewById(android.R.id.content);
        offerBanner = view.findViewById(R.id.offer_banner);
        userList = view.findViewById(R.id.user_list);
        mSwipeRefreshLayout = view.findViewById(R.id.swipeToRefresh);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        userList.setLayoutManager(gridLayoutManager);

        tv_popular = view.findViewById(R.id.tv_popular);
        tv_newone = view.findViewById(R.id.tv_newone);

        apiManager = new ApiManager(getContext(), this);
        // apiManager.getPromotionBanner();

        userList.addOnScrollListener(new PaginationScrollListener(gridLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                showProgress();
                // mocking network delay for API call
                new Handler().postDelayed(() -> apiManager.getUserListNearbyNextPage(String.valueOf(currentPage), ""), 500);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        // showProgress();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // mSwipeRefreshLayout.setRefreshing(false);
                currentPage = 1;
                isLastPage = false;
                list.clear();
                apiManager.getUserListNearby(String.valueOf(currentPage), "");
            }
        });

        //  markerForLang();
        checkFreeGift();

        showProgress();
        apiManager.getUserListNearby(String.valueOf(currentPage), "");

        //((Home)getActivity()).checkLocationSatae();

        //appLifecycle = new AppLifecycle();

        return view;
    }

    private void checkFreeGift() {
        if (new SessionManager(getContext()).getGender().equals("male")) {
            //     apiManager.getRemainingGiftCardDisplayFunction();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    @Override
    public void isError(String errorCode) {
        if (errorCode.equals("227")) {
            //new InsufficientCoins(getContext(), 2, Integer.parseInt(callRate));
        } else {
            Toast.makeText(getContext(), errorCode, Toast.LENGTH_SHORT).show();
        }
    }

    //    lateinit var dialog1: Dialog
    private Dialog dialog1;

    private boolean success;
    private int remGiftCard = 0;
    private String freeSeconds;

    @Override
    public void isSuccess(Object response, int ServiceCode) {

        try {

            if (ServiceCode == Constant.GET_REMAINING_GIFT_CARD) {

            }
            if (ServiceCode == Constant.SEARCH_USER) {

            }

            if (ServiceCode == Constant.NEW_GENERATE_AGORA_TOKEN) {


            }

            if (ServiceCode == Constant.USER_LIST) {
                UserListResponse rsp = (UserListResponse) response;

                mSwipeRefreshLayout.setRefreshing(false);

                list = rsp.getResult().getData();
                TOTAL_PAGES = rsp.getResult().getLast_page();
                if (list.size() > 0) {
                    homeUserAdapter = new HomeUserAdapter(getActivity(), NearbyFragment.this, "dashboard", NearbyFragment.this);
                    // userList.setItemAnimator(new DefaultItemAnimator());
                    userList.setAdapter(homeUserAdapter);

                    // Shuffle Data

                    // Collections.shuffle(list);

                    // Set data in adapter
                    homeUserAdapter.addAll(list);

                    if (currentPage < TOTAL_PAGES) {
                        homeUserAdapter.addLoadingFooter();
                    } else {
                        isLastPage = true;
                    }
                }

                consentReminder();
            }

            if (ServiceCode == Constant.USER_LIST_NEXT_PAGE) {
                UserListResponse rsp = (UserListResponse) response;

                mSwipeRefreshLayout.setRefreshing(false);

                homeUserAdapter.removeLoadingFooter();
                isLoading = false;

                List<UserListResponse.Data> results = rsp.getResult().getData();

                // Shuffle Data
                //  Collections.shuffle(results);

                list.addAll(results);


                homeUserAdapter.addAll(results);

                if (currentPage != TOTAL_PAGES) homeUserAdapter.addLoadingFooter();
                else isLastPage = true;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void retryPageLoad() {
        //   apiManager.getUserListNextPage(String.valueOf(currentPage), "");
    }

    void resetPages() {
        // Reset Current page when refresh data
        this.currentPage = 1;
        this.isLastPage = false;
    }

    void consentReminder() {
        if (!new SessionManager(getContext()).getConsent()) {
            // new ConsentDialog(this);
        }
    }

    public void showProgress() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    private String callType = "", profileId = "", callRate = "", hostName = "", hostImage = "";
    private int userId;

    public void startVideoCall(String profileId, String callRate, int userId, String hostName, String hostImage) {
        callType = "video";
        this.profileId = profileId;
        this.callRate = callRate;
        this.userId = userId;
        this.hostName = hostName;
        this.hostImage = hostImage;
        //apiManager.getRemainingGiftCardFunction();
    }


}
