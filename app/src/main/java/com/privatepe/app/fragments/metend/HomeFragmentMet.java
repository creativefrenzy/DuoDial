package com.privatepe.app.fragments.metend;


import static com.privatepe.app.utils.Constant.GET_FIRST_TIME_RECHARGE;
import static com.privatepe.app.utils.Constant.GET_FIRST_TIME_RECHARGE_LIST;
import static com.privatepe.app.utils.Constant.GET_NOTIFICATION_LIST;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.privatepe.app.R;
import com.privatepe.app.Zego.VideoChatZegoActivityMet;
import com.privatepe.app.activity.MainActivity;
import com.privatepe.app.activity.SelectPaymentMethod;
import com.privatepe.app.adapter.LanguageAdapter;
import com.privatepe.app.adapter.OfferImageAdapter;
import com.privatepe.app.adapter.metend.HomeUserAdapterMet;
import com.privatepe.app.dialogs.InsufficientCoins;
import com.privatepe.app.model.BannerResponse;
import com.privatepe.app.model.ProfileDetailsResponse;
import com.privatepe.app.model.fcm.Data;
import com.privatepe.app.model.fcm.MyResponse;
import com.privatepe.app.model.fcm.Sender;
import com.privatepe.app.model.language.LanguageData;
import com.privatepe.app.model.language.LanguageResponce;
import com.privatepe.app.response.metend.AdapterRes.UserListResponseMet;
import com.privatepe.app.response.metend.DiscountedRecharge.DiscountedRechargeResponse;
import com.privatepe.app.response.metend.FirstTimeRechargeListResponse;
import com.privatepe.app.response.metend.GenerateCallResponce.GenerateCallResponce;
import com.privatepe.app.response.metend.PaymentGatewayDetails.PaymentGatewayResponce;
import com.privatepe.app.response.metend.RechargePlan.RechargePlanResponseNew;
import com.privatepe.app.response.metend.RemainingGiftCard.RemainingGiftCardResponce;
import com.privatepe.app.response.metend.new_notifications.NewNotificationResponse;
import com.privatepe.app.response.metend.new_notifications.NewNotificationResult;
import com.privatepe.app.retrofit.ApiInterface;
import com.privatepe.app.retrofit.ApiManager;
import com.privatepe.app.retrofit.ApiResponseInterface;
import com.privatepe.app.retrofit.FirebaseApiClient;
import com.privatepe.app.utils.AppLifecycle;
import com.privatepe.app.utils.Constant;
import com.privatepe.app.utils.PaginationAdapterCallback;
import com.privatepe.app.utils.PaginationScrollListener;
import com.privatepe.app.utils.SessionManager;
import com.tencent.imsdk.common.IMCallback;
import com.tencent.imsdk.signaling.SignalingManager;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMOfflinePushInfo;
import com.tencent.imsdk.v2.V2TIMSignalingInfo;
import com.tencent.imsdk.v2.V2TIMSignalingListener;
import com.tencent.imsdk.v2.V2TIMSignalingManager;
import com.tencent.imsdk.v2.V2TIMValueCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import pl.pzienowicz.autoscrollviewpager.AutoScrollViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */


public class HomeFragmentMet extends Fragment implements ApiResponseInterface, PaginationAdapterCallback {

    private AutoScrollViewPager offerBanner;
    OfferImageAdapter offerImageAdapter;

    RecyclerView userList;
    HomeUserAdapterMet homeUserAdapter;
    ArrayList<UserListResponseMet.Data> list = new ArrayList<>();
    ApiManager apiManager;
    GridLayoutManager gridLayoutManager;
    AppLifecycle appLifecycle;
    private static final int PAGE_START = 1;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private int TOTAL_PAGES;
    private int currentPage = PAGE_START;

    private ArrayList<LanguageData> languageResponceArrayList;
    private TextView tv_all;
    private TextView tv_lan1;
    private TextView tv_lan2;
    private TextView tv_lan3;
    private TextView tv_lan4;
    private TextView tv_lan5;
    private TextView tv_lan6;
    private TextView tv_more;

    private TextView tv_popular, tv_newone;

    private LanguageAdapter languageAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ViewGroup viewGroup;
    int isGuest = 0;
    // private ZimManager zimManager;
    private DatabaseReference chatRef;

    private boolean isFreeCall = false;
    private Handler notificationHandler = new Handler();

    //AppLifecycle appLifecycle;
    public HomeFragmentMet() {
        // Required empty public constructor
    }

    private ArrayList<String> messageStack = new ArrayList<>();


    private InsufficientCoins insufficientCoins;

    private String TAG = "HomeFragment";

    private void setMessageNoti() {
        messageStack.add("tume ek baat puchni hai. todi us type ki baat hai chalo call par btaugi main");
        messageStack.add("हे बेबी, आज मौसम बहुत अच्छा है। मुझे फ़ोन करो \uD83D\uDE0D");
        messageStack.add("Hi \uD83D\uDE09\uD83D\uDE09 thoda time spend kro mere saath Apni pic bhejo");
        messageStack.add("Kya kr rhe ho Jaan ☺️☺️\uD83D\uDC8B\uD83D\uDC8B aaj kuch alag krte hai Tumse accha lgta baat krna");
    }
    private ShimmerFrameLayout shimmerHomeLay;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_met, container, false);
        appLifecycle = new AppLifecycle();
        viewGroup = view.findViewById(android.R.id.content);
        offerBanner = view.findViewById(R.id.offer_banner);
        userList = view.findViewById(R.id.user_list);
        shimmerHomeLay=view.findViewById(R.id.shimmerHomeLay);
        mSwipeRefreshLayout = view.findViewById(R.id.swipeToRefresh);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        userList.setLayoutManager(gridLayoutManager);
        // zimManager = ZimManager.sharedInstance();

        setMessageNoti();

        tv_all = view.findViewById(R.id.tv_all);
        tv_lan1 = view.findViewById(R.id.tv_lan1);
        tv_lan2 = view.findViewById(R.id.tv_lan2);
        tv_lan3 = view.findViewById(R.id.tv_lan3);
        tv_lan4 = view.findViewById(R.id.tv_lan4);
        tv_lan5 = view.findViewById(R.id.tv_lan5);
        tv_lan6 = view.findViewById(R.id.tv_lan6);
        tv_more = view.findViewById(R.id.tv_more);

        tv_popular = view.findViewById(R.id.tv_popular);
        tv_newone = view.findViewById(R.id.tv_newone);

        tv_all.setTextColor(getResources().getColor(R.color.colorPink));
        //   Log.e("authtoken", "" + Constant.BEARER + new SessionManager(getContext()).getUserToken());


        tv_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_all.setTextColor(getResources().getColor(R.color.colorPink));
                tv_lan1.setTextColor(getResources().getColor(R.color.black));
                tv_lan2.setTextColor(getResources().getColor(R.color.black));
                tv_lan3.setTextColor(getResources().getColor(R.color.black));
                tv_lan4.setTextColor(getResources().getColor(R.color.black));
                tv_lan5.setTextColor(getResources().getColor(R.color.black));
                tv_lan6.setTextColor(getResources().getColor(R.color.black));
                isLastPage = false;
                new SessionManager(getContext()).setLangState(0);
                apiManager.getUserListNew(String.valueOf(currentPage), "");
            }
        });

        tv_lan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_all.setTextColor(getResources().getColor(R.color.black));
                tv_lan1.setTextColor(getResources().getColor(R.color.colorPink));
                tv_lan2.setTextColor(getResources().getColor(R.color.black));
                tv_lan3.setTextColor(getResources().getColor(R.color.black));
                tv_lan4.setTextColor(getResources().getColor(R.color.black));
                tv_lan5.setTextColor(getResources().getColor(R.color.black));
                tv_lan6.setTextColor(getResources().getColor(R.color.black));
                isLastPage = false;
                new SessionManager(getContext()).setLangState(1);
                apiManager.getUserListNew(String.valueOf(currentPage), "");
            }
        });

        tv_lan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_all.setTextColor(getResources().getColor(R.color.black));
                tv_lan1.setTextColor(getResources().getColor(R.color.black));
                tv_lan2.setTextColor(getResources().getColor(R.color.colorPink));
                tv_lan3.setTextColor(getResources().getColor(R.color.black));
                tv_lan4.setTextColor(getResources().getColor(R.color.black));
                tv_lan5.setTextColor(getResources().getColor(R.color.black));
                tv_lan6.setTextColor(getResources().getColor(R.color.black));
                isLastPage = false;
                new SessionManager(getContext()).setLangState(2);
                apiManager.getUserListNew(String.valueOf(currentPage), "");
            }
        });

        tv_lan3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_all.setTextColor(getResources().getColor(R.color.black));
                tv_lan1.setTextColor(getResources().getColor(R.color.black));
                tv_lan2.setTextColor(getResources().getColor(R.color.black));
                tv_lan3.setTextColor(getResources().getColor(R.color.colorPink));
                tv_lan4.setTextColor(getResources().getColor(R.color.black));
                tv_lan5.setTextColor(getResources().getColor(R.color.black));
                tv_lan6.setTextColor(getResources().getColor(R.color.black));
                isLastPage = false;
                new SessionManager(getContext()).setLangState(3);
                apiManager.getUserListNew(String.valueOf(currentPage), "");
            }
        });

        tv_lan4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_all.setTextColor(getResources().getColor(R.color.black));
                tv_lan1.setTextColor(getResources().getColor(R.color.black));
                tv_lan2.setTextColor(getResources().getColor(R.color.black));
                tv_lan3.setTextColor(getResources().getColor(R.color.black));
                tv_lan4.setTextColor(getResources().getColor(R.color.colorPink));
                tv_lan5.setTextColor(getResources().getColor(R.color.black));
                tv_lan6.setTextColor(getResources().getColor(R.color.black));
                isLastPage = false;
                new SessionManager(getContext()).setLangState(4);
                apiManager.getUserListNew(String.valueOf(currentPage), "");
            }
        });

        tv_lan5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_all.setTextColor(getResources().getColor(R.color.black));
                tv_lan1.setTextColor(getResources().getColor(R.color.black));
                tv_lan2.setTextColor(getResources().getColor(R.color.black));
                tv_lan3.setTextColor(getResources().getColor(R.color.black));
                tv_lan4.setTextColor(getResources().getColor(R.color.black));
                tv_lan5.setTextColor(getResources().getColor(R.color.colorPink));
                tv_lan6.setTextColor(getResources().getColor(R.color.black));
                isLastPage = false;
                new SessionManager(getContext()).setLangState(5);
                apiManager.getUserListNew(String.valueOf(currentPage), "");
            }
        });

        tv_lan6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_all.setTextColor(getResources().getColor(R.color.black));
                tv_lan1.setTextColor(getResources().getColor(R.color.black));
                tv_lan2.setTextColor(getResources().getColor(R.color.black));
                tv_lan3.setTextColor(getResources().getColor(R.color.black));
                tv_lan4.setTextColor(getResources().getColor(R.color.black));
                tv_lan5.setTextColor(getResources().getColor(R.color.black));
                tv_lan6.setTextColor(getResources().getColor(R.color.colorPink));
                isLastPage = false;
                new SessionManager(getContext()).setLangState(6);
                apiManager.getUserListNew(String.valueOf(currentPage), "");
            }
        });

        tv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.mylanguagecustom, viewGroup, false);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(dialogView);

                final AlertDialog alertDialog = builder.create();

                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                WindowManager.LayoutParams layoutParams = alertDialog.getWindow().getAttributes();
                layoutParams.gravity = Gravity.TOP | Gravity.CENTER;

                //   layoutParams.x = 100;
                //   layoutParams.y = 120;

                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
                alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                GridView lanGridViewCust;
                lanGridViewCust = dialogView.findViewById(R.id.lanGridView);
                lanGridViewCust.setAdapter(languageAdapter);
                lanGridViewCust.setSelector(new ColorDrawable(Color.TRANSPARENT));
                lanGridViewCust.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                       /* TextView currentLetter = (TextView) view.findViewById(R.id.tv_landata);
                        currentLetter.setTextColor(getResources().getColor(R.color.colorPink));*/

                        switch (i) {
                            case 0:
                                tv_all.performClick();
                                break;
                            case 1:
                                tv_lan1.performClick();
                                break;
                            case 2:
                                tv_lan2.performClick();
                                break;
                            case 3:
                                tv_lan3.performClick();
                                break;
                            case 4:
                                tv_lan4.performClick();
                                break;
                            case 5:
                                tv_lan5.performClick();
                                break;
                            case 6:
                                tv_lan6.performClick();
                                break;
                            default:
                                tv_all.setTextColor(getResources().getColor(R.color.black));
                                tv_lan1.setTextColor(getResources().getColor(R.color.black));
                                tv_lan2.setTextColor(getResources().getColor(R.color.black));
                                tv_lan3.setTextColor(getResources().getColor(R.color.black));
                                tv_lan4.setTextColor(getResources().getColor(R.color.black));
                                tv_lan5.setTextColor(getResources().getColor(R.color.black));
                                tv_lan6.setTextColor(getResources().getColor(R.color.black));

                                if (languageResponceArrayList.get(i).getLanguage().equals("search")) {
                                    ((MainActivity) getActivity()).loadSearchFragement();
                                } else {
                                    new SessionManager(getContext()).setLangState(i);
                                    apiManager.getUserListNew(String.valueOf(currentPage), "");
                                }
                        }

                        alertDialog.dismiss();

                    }
                });

            }
        });


  /*      userList.addOnItemTouchListener(new RecyclerTouchListener(getContext(), userList, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                try {
                    Intent intent = new Intent(getContext(), ViewProfile.class);
                    Bundle bundle = new Bundle();
                    //hide intent sending data homefragment to ViewprofileActivity...
                    //  bundle.putSerializable("user_data", list.get(position));
                    bundle.putSerializable("id", list.get(position).getId());
                    bundle.putSerializable("profileId", list.get(position).getProfile_id());

                    intent.putExtras(bundle);

                    startActivity(intent);

                    //  ((MainActivity) getActivity()).startCountDown();

                } catch (Exception e) {
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
*/
        apiManager = new ApiManager(getContext(), this);
        // apiManager.getPromotionBanner();

        userList.addOnScrollListener(new PaginationScrollListener(gridLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                showProgress();
                // mocking network delay for API call
                new Handler().postDelayed(() -> apiManager.getUserListNextPageForHomeMet(String.valueOf(currentPage), ""), 500);
                //Log.e("test123","getUserListNextPage HomeFragment "+String.valueOf(currentPage));
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

        apiManager.getUserLanguage();
        apiManager.getProfileDetails();


        // showProgress();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // mSwipeRefreshLayout.setRefreshing(false);
                currentPage = 1;
                isLastPage = false;
                list.clear();
                apiManager.getUserListNew(String.valueOf(currentPage), "");
            }
        });

        //  markerForLang();
        //  checkFreeGift();

        if (new SessionManager(getContext()).getUserLoginCompleted()) {
            showGiftDialog();
        }


        tv_popular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_popular.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_popular.setTextSize(18);

                // Typeface typeface = getResources().getFont(R.font.lobster);
                //or to support all versions use
                Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.lato_bold);
                tv_popular.setTypeface(typeface);

                tv_newone.setTextColor(getResources().getColor(R.color.black));
                tv_newone.setTextSize(14);

                typeface = ResourcesCompat.getFont(getContext(), R.font.lato_regular);
                tv_newone.setTypeface(typeface);

                apiManager.getPopularList(String.valueOf(currentPage), "");

            }
        });

        tv_newone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_newone.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_newone.setTextSize(18);

                Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.lato_bold);
                tv_newone.setTypeface(typeface);


                tv_popular.setTextColor(getResources().getColor(R.color.black));
                tv_popular.setTextSize(14);

                typeface = ResourcesCompat.getFont(getContext(), R.font.lato_regular);
                tv_popular.setTypeface(typeface);

                apiManager.getUserListNew(String.valueOf(currentPage), "");

            }
        });

        tv_newone.performClick();

     /*   ItemTouchHelper.SimpleCallback touchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
          //  private final ColorDrawable background = new ColorDrawable(getResources().getColor(R.color.background));

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
             //   adapter.showMenu(viewHolder.getAdapterPosition());
                Log.e("direction",direction+"");
            }

        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchHelperCallback);
        itemTouchHelper.attachToRecyclerView(userList);
*/

       /* ((LinearLayout)view.findViewById(R.id.homeMain)).setOnTouchListener(new OnSwipeTouchListener(getContext(), new TouchListener() {
            @Override
            public void onSingleTap() {
        //        Log.e("TAG", ">> Single tap");

            }

            @Override
            public void onDoubleTap() {
         //       Log.e("TAG", ">> Double tap");
            }

            @Override
            public void onLongPress() {
                Log.e("TAG", ">> Long press");
            }

            @Override
            public void onSwipeLeft() {
                Log.e("TAG", ">> Swipe left");
            }

            @Override
            public void onSwipeRight() {
                Log.e("TAG", ">> Swipe right");

            }
        }));
*/

        isGuest = new SessionManager(getContext()).getGuestStatus();

        //appLifecycle = new AppLifecycle();
       /* try {
            AppID = getActivity().getString(R.string.app_id_rtm);
            // Initialize the RTM client
            mRtmClient = RtmClient.createInstance(getActivity(), AppID,
                    new RtmClientListener() {
                        @Override
                        public void onConnectionStateChanged(int state, int reason) {
                            String text = "Connection state changed to " + state + "Reason: " + reason + "\n";
                            *//*  writeToMessageHistory(text);*//*
                        }

                        @Override
                        public void onImageMessageReceivedFromPeer(RtmImageMessage rtmImageMessage, String s) {
                            String text = "Connection state changed to " + s+ "\n";
                        }

                        @Override
                        public void onFileMessageReceivedFromPeer(RtmFileMessage rtmFileMessage, String s) {
                            String text = "Connection state changed to " + s+ "\n";
                        }

                        @Override
                        public void onMediaUploadingProgress(RtmMediaOperationProgress rtmMediaOperationProgress, long l) {
                        }

                        @Override
                        public void onMediaDownloadingProgress(RtmMediaOperationProgress rtmMediaOperationProgress, long l) {
                        }

                        @Override
                        public void onTokenExpired() {
                        }

                        @Override
                        public void onPeersOnlineStatusChanged(Map<String, Integer> map) {
                        }

                        @Override
                        public void onMessageReceived(RtmMessage rtmMessage, String peerId) {
                            //saveReceivedMsg(rtmMessage.getText(), peerId);

                        }
                    });

        } catch (Exception e) {
            throw new RuntimeException("RTM initialization failed!");
        }*/
        //HashMap<String, String> user = new SessionManager(getContext()).getUserDetails();
        //initializeRTM(user.get(PROFILE_ID));
        return view;
    }

    private void showGiftDialog() {

        apiManager.getRemainingGiftCardDisplayFunction();


        // Log.e(TAG, "showGiftDialog: "+new SessionManager(getContext()).getIsNewUser());


    }

    private void checkFreeGift() {
        if (new SessionManager(getContext()).getGender().equals("male")) {
            apiManager.getRemainingGiftCardDisplayFunction();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        // resetPages();
        // apiManager.getUserList(String.valueOf(currentPage), "");
        //  apiManager.getUserLanguage();
        apiManager.getPaymentData();
        //   Log.e("onlineState", new SessionManager(getContext()).getOnlineState() + "");
        if (new SessionManager(getContext()).getUserLoaddata().equals("yes")) {
//            apiManager.getUserList(String.valueOf(currentPage), "");
            new SessionManager(getContext()).setUserLoaddataNo();
        }
        if (new SessionManager(getContext()).getOnlineState() == 0) {
            //showProgress();
            //  markerForLang();
            new SessionManager(getContext()).setOnlineState(1);
            //new SessionManager(getContext()).setLangState(0);
        }

        //  Log.e("onlineState2", new SessionManager(getContext()).getOnlineState() + "");

    }

    public void markerForLang() {
        switch (new SessionManager(getContext()).gettLangState()) {
            case 0:
                tv_all.performClick();
                break;
            case 1:
                tv_lan1.performClick();
                break;
            case 2:
                tv_lan2.performClick();
                break;
            case 3:
                tv_lan3.performClick();
                break;
            case 4:
                tv_lan4.performClick();
                break;
            case 5:
                tv_lan5.performClick();
                break;
            case 6:
                tv_lan6.performClick();
                break;
            default:
                apiManager.getUserListNew(String.valueOf(currentPage), "");
        }
    }

    /* public void initializeRTM(String username*//*, String profile_id, String unique_id, String channel_name, String token, String call_type, String is_free_call*//*){
        mRtmClient.createMessage();

        mRtmClient.login(null, username, new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void responseInfo) {
                Toast.makeText(getActivity(), "Success", Toast.LENGTH_LONG).show();
                //sendMsg(username, profile_id, unique_id, channel_name, token, call_type, is_free_call);
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
            }
        });
    }
    private void sendMsg(String username, String profile_id, String unique_id, String channel_name, String token, String call_type, String is_free_call, String name, String image){
        final RtmMessage message = mRtmClient.createMessage();
        message.setText(username+","+profile_id+","+unique_id+","+channel_name+","+token+","+call_type+","+is_free_call+","+name+","+image);
        SendMessageOptions option = new SendMessageOptions();
        option.enableOfflineMessaging = true;

        mRtmClient.sendMessageToPeer(profile_id, message, option, new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                String text = message.getText();
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                String text = errorInfo.toString();
            }
        });

    }*/
    @Override
    public void isError(String errorCode) {

        Context context = getContext();


        if (context != null) {
            if (errorCode.equals("227")) {

                Log.e("insufficientCoinsDialog", "isError: " + "insufficientCoinsDialog");
                insufficientCoins = new InsufficientCoins(context, 2, Integer.parseInt(callRate));
                Log.e("insufficientCoinsDialog", "isError: " + errorCode);

                insufficientCoins.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        apiManager.checkFirstTimeRechargeDone();
                    }
                });


            } else {
                Toast.makeText(context, errorCode, Toast.LENGTH_SHORT).show();
            }

        }


    }

    private void FirstTimeRechargeDialog(RechargePlanResponseNew.Data firstRecharge) {
        Log.e("FirstTimeRechargeDialog", "FirstTimeRechargeDialog: HomeFragment");

        // RechargePlanResponse.Data selcted = new RechargePlanResponse.Data(7, 70, 1, 210, 100, 7, true);

        RechargePlanResponseNew.Data selcted = firstRecharge;

        firstTimeRecharge = new Dialog(getContext());
        firstTimeRecharge.setContentView(R.layout.descounted_recharge_popup);
        firstTimeRecharge.setCancelable(true);
        firstTimeRecharge.setCanceledOnTouchOutside(true);
        firstTimeRecharge.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        RelativeLayout container = firstTimeRecharge.findViewById(R.id.container);
        Button btn_buynow = firstTimeRecharge.findViewById(R.id.btn_buynow);

        TextView coins = firstTimeRecharge.findViewById(R.id.tv_coins);
        TextView price = firstTimeRecharge.findViewById(R.id.tv_price);


        coins.setText("" + selcted.getPoints());
        price.setText("₹" + selcted.getAmount());


        btn_buynow.setOnClickListener(view -> {
            //Go to payment activity
            Intent intent = new Intent(getContext(), SelectPaymentMethod.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("selected_plan", selcted);
            intent.putExtras(bundle);
            getContext().startActivity(intent);
            firstTimeRecharge.dismiss();
        });

        container.setOnClickListener(view -> {
            //Go to payment activity
            firstTimeRecharge.dismiss();

        });



         /*      TextView tv_freecardcount = firstTimeRecharge.findViewById(R.id.tv_freecardcount);
        Button btn_gotit = firstTimeRecharge.findViewById(R.id.btn_gotit);
        tv_freecardcount.setText(remGiftCard + " gift cards received. Enjoy your free chance of video call.");
        btn_gotit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstTimeRecharge.dismiss();
            }
        });
*/

        firstTimeRecharge.show();

    }

    private Dialog dialog1, firstTimeRecharge;

    private boolean success;
    private int remGiftCard = 0;
    private String freeSeconds;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void isSuccess(Object response, int ServiceCode) {

        try {

            if (ServiceCode == Constant.GET_REMAINING_GIFT_CARD_DISPLAY) {
                RemainingGiftCardResponce rsp = (RemainingGiftCardResponce) response;
                try {
                    int remGiftCard = rsp.getResult().getRemGiftCards();

                    if (remGiftCard > 0) {
                        dialog1 = new Dialog(getContext());
                        dialog1.setContentView(R.layout.freecard_layout);
                        dialog1.setCancelable(false);
                        dialog1.setCanceledOnTouchOutside(true);
                        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        TextView tv_freecardcount = dialog1.findViewById(R.id.tv_freecardcount);
                        Button btn_gotit = dialog1.findViewById(R.id.btn_gotit);
                        tv_freecardcount.setText(remGiftCard + " gift cards received. Enjoy your free chance of video call.");
                        btn_gotit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog1.dismiss();
                            }
                        });

                        dialog1.show();

                    }

                } catch (Exception e) {
                }

            }
            if (ServiceCode == Constant.GET_REMAINING_GIFT_CARD) {


                RemainingGiftCardResponce rsp = (RemainingGiftCardResponce) response;


                Log.e("HomeFragment", "isSuccess: " + "  GET_REMAINING_GIFT_CARD api called");
                Log.e("HomeFragment", "isSuccess: success " + rsp.getSuccess());
                Log.e("HomeFragment", "isSuccess: remGiftCard " + rsp.getResult().getRemGiftCards());
                Log.e("HomeFragment", "isSuccess: freeSeconds " + rsp.getResult().getFreeSeconds());

                try {
                    try {
                        success = rsp.getSuccess();
                        remGiftCard = rsp.getResult().getRemGiftCards();
                        freeSeconds = rsp.getResult().getFreeSeconds();
                        if (remGiftCard > 0) {

                            apiManager.searchUser(profileId, "1");
                            return;
                        }
                    } catch (Exception e) {

                        Log.e("HomeFragment", "isSuccess: Exception " + e.getMessage());
                    }
                    String walletAmount = String.valueOf(new SessionManager(getContext()).getUserWallet());

                    Log.e("HomeFragment", "isSuccess: " + "  GET_REMAINING_GIFT_CARD api called middle");

                    Log.e("HomeFragment", "isSuccess: callRate " + callRate + "  totalCoins: " + new SessionManager(getContext()).getUserWallet());

                    if (new SessionManager(getContext()).getUserWallet() >= Integer.parseInt(callRate)) {
                        Log.e("HomeFragment", "isSuccess: pid" + ""+profileId);

                        apiManager.searchUser(profileId, "1");
                        Log.e("HomeFragmentHomeFragment", "isSuccess: search user");
                    } else {
                        Log.e("insufficientCoinsDialog", "isSuccess: " + "insufficientCoinsDialog");
                        insufficientCoins = new InsufficientCoins(requireActivity(), 2, Integer.parseInt(callRate));

                        insufficientCoins.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {

                                apiManager.checkFirstTimeRechargeDone();
                            }
                        });


                        // apiManager.searchUser(profileId, "1");
                    }
                } catch (Exception e) {
                    apiManager.searchUser(profileId, "1");
                }
            }
            if (ServiceCode == Constant.SEARCH_USER) {
                UserListResponseMet rsp = (UserListResponseMet) response;
                if (rsp != null) {
                    try {
                        //  int onlineStatus = rsp.getResult().getData().get(0).getIs_online();
                        //   Log.e(TAG, "isSuccess:  onlineStatus "+onlineStatus );


                        Log.e("SearchUserCallTestInSearch", "in search isSuccess: " + rsp.getResult());
                        if (callType.equals("video")) {

                            chatRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                @Override
                                public void onSuccess(@NonNull DataSnapshot dataSnapshot) {
                                    Map<String, Object> map = null;
                                    if (dataSnapshot.exists()) {
                                        map = (Map<String, Object>) dataSnapshot.getValue();

                                        Log.e("HomeFragmentfirebase", "onSuccess: " + map.toString());

                                        if (map.get("status").equals("Online") || map.get("status").equals("Live")) {

                                            Log.e("HomeFragmentfirebase", "onDataChange: " + map.get("status").toString());

                                         /*   if (remGiftCard > 0) {
                                                apiManager.generateCallRequest(Integer.parseInt(profileId), String.valueOf(System.currentTimeMillis()), "0", Integer.parseInt(callRate),
                                                        Boolean.parseBoolean("true"), String.valueOf(remGiftCard));
                                            } else {
                                                apiManager.generateCallRequest(Integer.parseInt(profileId), String.valueOf(System.currentTimeMillis()), "0", Integer.parseInt(callRate),
                                                        Boolean.parseBoolean("false"), String.valueOf(remGiftCard));
                                            }

*/

                                            if (remGiftCard > 0) {
                                                apiManager.generateCallRequestZ(Integer.parseInt(profileId), String.valueOf(System.currentTimeMillis()), "0", Integer.parseInt(callRate),
                                                        Boolean.parseBoolean("true"), String.valueOf(remGiftCard));
                                            } else {
                                                apiManager.generateCallRequestZ(Integer.parseInt(profileId), String.valueOf(System.currentTimeMillis()), "0", Integer.parseInt(callRate),
                                                        Boolean.parseBoolean("false"), String.valueOf(remGiftCard));
                                            }


                                        } else if (map.get("status").equals("Busy")) {

                                            Toast.makeText(getContext(), "User is Busy", Toast.LENGTH_LONG).show();

                                            //  Log.e("HomeFragmentfirebase", "onDataChange: "+"Busy" );
                                            Log.e("HomeFragmentfirebase", "onDataChange: " + map.get("status").toString());
                                        } else if (map.get("status").equals("Offline")) {
                                            Toast.makeText(getContext(), "User is Offline", Toast.LENGTH_LONG).show();
                                            // Log.e("HomeFragmentfirebase", "onDataChange: "+"Offline" );
                                            Log.e("HomeFragmentfirebase", "onDataChange: " + map.get("status").toString());
                                        }
                                    } else {
                                        Log.e("HomeFragmentfirebase", "onSuccess: " + "does not exist");
                                    }
                                }
                            });

                        } else if (callType.equals("audio")) {

                        /*   apiManager.dailVoiceCallUser(String.valueOf(userData.get(0).getAudioCallRate()), String.valueOf(userId),
                         String.valueOf(System.currentTimeMillis()));
                    */
                        }

                       /* Log.e("userbusycatch", "isSuccess: " + new Gson().toJson(rsp));
                        int onlineStatus = rsp.getResult().getData().get(0).getIs_online();
                        Log.e("HF_OnlineStatusss", "" + onlineStatus);
                        Log.e("userbusycatch", "isSuccess:11 " + new Gson().toJson(rsp));

                        int busyStatus = rsp.getResult().getData().get(0).getIs_busy();

                        Log.e("HF_BusyStatusss", "" + busyStatus);

                        //onlineStatus == 1 && busyStatus == 0
                        if (onlineStatus == 1 && busyStatus == 0) {
                            // Check wallet balance before going to make a video call
                            //     apiManager.getWalletAmount();

                            if (callType.equals("video")) {
                                if (remGiftCard > 0) {
                                    apiManager.generateCallRequest(Integer.parseInt(profileId), String.valueOf(System.currentTimeMillis()), "0", Integer.parseInt(callRate),
                                            Boolean.parseBoolean("true"), String.valueOf(remGiftCard));
                                } else {
                                    apiManager.generateCallRequest(Integer.parseInt(profileId), String.valueOf(System.currentTimeMillis()), "0", Integer.parseInt(callRate),
                                            Boolean.parseBoolean("false"), String.valueOf(remGiftCard));
                                }
                            } else if (callType.equals("audio")) {
                    *//*
                         apiManager.dailVoiceCallUser(String.valueOf(userData.get(0).getAudioCallRate()), String.valueOf(userId),
                         String.valueOf(System.currentTimeMillis()));
                    *//*
                            }
                        } else if (onlineStatus == 1) {
                            Toast.makeText(getContext(), hostName + " is Busy", Toast.LENGTH_SHORT).show();

                        } else if (onlineStatus == 0) {
                            Toast.makeText(getContext(), hostName + " is Offline", Toast.LENGTH_SHORT).show();
                        }*/


                    } catch (Exception e) {
                        Log.e("SearchUserCallTest", "isSuccess: " + rsp.getResult());
                        Log.e("userbusycatch", "isSuccess: Exception " + e.getMessage());
                        Toast.makeText(getContext(), "User is Offline!", Toast.LENGTH_SHORT).show();

                        new SessionManager(getContext()).setOnlineState(0);
                        // finish();
                    }
                }
            }

            if (ServiceCode == Constant.NEW_GENERATE_AGORA_TOKENZ) {
                GenerateCallResponce rsp = (GenerateCallResponce) response;
                Log.e("checkkkk",""+profileId);

                V2TIMManager v2TIMManager = V2TIMManager.getInstance();


                Log.e("NEW_GENERATE_AGORA_TOKENZ", "isSuccess: " + new Gson().toJson(rsp));

                int walletBalance = rsp.getResult().getPoints().getTotalPoint();
                int CallRateInt = Integer.parseInt(callRate);
                long talktime = (walletBalance / CallRateInt) * 1000L;
                  Log.e("AUTO_CUT_TESTZ", "CallNotificationDialog: " + talktime);
                long canCallTill = talktime - 2000;
                Log.e("AUTO_CUT_TESTZ", "CallNotificationDialog: canCallTill " + canCallTill);
                String profilePic = new SessionManager(getContext()).getUserProfilepic();
                HashMap<String, String> user = new SessionManager(getContext()).getUserDetails();
                Intent intent = new Intent(getContext(), VideoChatZegoActivityMet.class);
                intent.putExtra("TOKEN", "demo");
                intent.putExtra("ID", profileId);
                intent.putExtra("UID", String.valueOf(userId));
                intent.putExtra("CALL_RATE", callRate);
                intent.putExtra("UNIQUE_ID", rsp.getResult().getData().getUniqueId());

                if (remGiftCard > 0) {
                    int newFreeSec = Integer.parseInt(freeSeconds) * 1000;
                    canCallTill = newFreeSec;
                    newFreeSec = newFreeSec - 2000;
                    intent.putExtra("AUTO_END_TIME", newFreeSec);
                    intent.putExtra("is_free_call", "true");
                    isFreeCall = true;
                    Log.e("callCheckLog", "in free section with freeSeconds =>" + freeSeconds);
                } else {
                    //AUTO_END_TIME converted to long
                    intent.putExtra("AUTO_END_TIME", canCallTill);
                    intent.putExtra("is_free_call", "false");
                    isFreeCall = false;
                }
                intent.putExtra("receiver_name", hostName);
                intent.putExtra("converID", "convId");
                intent.putExtra("receiver_image", hostImage);
                Log.e("NEW_GENERATE_AGORA_TOKENZ", "isSuccess: go to videoChatActivity");


                JSONObject jsonResult = new JSONObject();
                try {
                    jsonResult.put("type", "callrequest");

                    jsonResult.put("caller_name", new SessionManager(getContext()).getName());
                    jsonResult.put("userId", new SessionManager(getContext()).getUserId());

                    jsonResult.put("unique_id", rsp.getResult().getData().getUniqueId());
                    jsonResult.put("caller_image", new SessionManager(getContext()).getUserProfilepic());
                    jsonResult.put("callRate", "1");
                    jsonResult.put("isFreeCall", "false");
                    jsonResult.put("totalPoints", new SessionManager(getContext()).getUserWallet());
                    jsonResult.put("remainingGiftCards", "0");
                    jsonResult.put("freeSeconds", "0");




                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String msg2 = jsonResult.toString();
                V2TIMSignalingManager v2TIMSignalingManager=V2TIMManager.getSignalingManager();
                String inviteId=   v2TIMSignalingManager.invite(  profileId, msg2, true, null, 20, new V2TIMCallback() {
                    @Override
                    public void onSuccess() {
                        Log.e("listensdaa","Yes11 Invitesent"+profileId);

                    }

                    @Override
                    public void onError(int i, String s) {
                        Log.e("listensdaa","Yes22 "+s);

                    }
                });
                Log.e("chdakdaf","yes "+inviteId);
                intent.putExtra("inviteId",inviteId);
                startActivity(intent);

            /*    V2TIMManager.getInstance().sendC2CTextMessage(msg2,
                        profileId, new V2TIMValueCallback<V2TIMMessage>() {
                            @Override
                            public void onSuccess(V2TIMMessage message) {
                                // The one-to-one text message sent successfully
                                Log.e("offLineDataLog", "success to => " + profileId + " with message => " + new Gson().toJson(message));
                            }


                            @Override
                            public void onError(int code, String desc) {

                            }
                        });*/

            }


            if (ServiceCode == Constant.USER_LIST) {
                UserListResponseMet rsp = (UserListResponseMet) response;

                mSwipeRefreshLayout.setRefreshing(false);



                // list = rsp.getResult().getData();
                    list.addAll(rsp.getResult().getData());



                Log.e("dataSize", list.size() + "");

                TOTAL_PAGES = rsp.getResult().getLast_page();
                if (list.size() > 0) {
                    homeUserAdapter = new HomeUserAdapterMet(getActivity(), HomeFragmentMet.this, "dashboard", HomeFragmentMet.this);
                    // userList.setItemAnimator(new DefaultItemAnimator());
                    userList.setAdapter(homeUserAdapter);
                    RecyclerView.LayoutManager rvmanager = userList.getLayoutManager();
                        GridLayoutManager glay = (GridLayoutManager) rvmanager;
userList.addOnScrollListener(new RecyclerView.OnScrollListener() {
    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        Log.e("chaksfs2","state "+newState);

    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        Log.e("chaksfs","x: "+dx+"y: "+dy);
        if(homeUserAdapter.getItemCount()>0) {
            int visiblePosition = glay.findFirstCompletelyVisibleItemPosition();
            Log.e("chaksfs1", "Vposition " + visiblePosition);
            //homeUserAdapter.currentScrollPos(visiblePosition);
        }

    }
});
                    // Shuffle Data

                    // Collections.shuffle(list);

                    // Set data in adapter
                    homeUserAdapter.addAll(list);

                    if (currentPage < TOTAL_PAGES) {
                        homeUserAdapter.addLoadingFooter();
                    } else {
                        isLastPage = true;
                    }
                } else {
                    userList.setAdapter(homeUserAdapter);
                }

                if (new SessionManager(getContext()).getFirstTimeLogin()) {
                    Log.e("ddffffff", "isSuccess: " + "Notification generated");
                    // consentReminder();

                    apiManager.getNotificationsList();
                    new SessionManager(getContext()).setFirstTimeLogin(false);
                } else {
                    Log.e("ddffffff", "isSuccess: " + "Notification not generated");
                }

                shimmerHomeLay.stopShimmer();
                shimmerHomeLay.setVisibility(View.GONE);
            }


            if (ServiceCode == GET_NOTIFICATION_LIST) {


                NewNotificationResponse notificationListRsp = (NewNotificationResponse) response;

                Log.e(TAG, "isSuccess: GET_NOTIFICATION_LIST " + new Gson().toJson(notificationListRsp));

                String fcmToken = new SessionManager(getActivity()).getFcmToken();

                for (int i = 0; i < notificationListRsp.getResult().size(); i++) {
                    long time1 = notificationListRsp.getResult().get(i).getMessageDuration();
                    NewNotificationResult User = notificationListRsp.getResult().get(i);
                    long time2 = 0;
                    if (i == 0) {
                        time2 = 0;
                    } else {
                        time2 = (time1 * notificationListRsp.getResult().get(i - 1).getNotificationMsgDetail().size());
                    }

                    int finalI = i;

                    notificationHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            for (int j = 0; j < User.getNotificationMsgDetail().size(); j++) {
                                int finalJ = j;
                                notificationHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.e(TAG, "run: notificationHandler  user " + User.getName() + "  message  " + User.getNotificationMsgDetail().get(finalJ).getTitle());
                                        String type = "text";
                                        String profileImg = "";
                                        if (User.getNotiProfileImages().get(0).getIsProfileImage() == 1) {
                                            profileImg = User.getNotiProfileImages().get(0).getImageName();
                                        }
                                        try {
                                            sendChatNotification(fcmToken, String.valueOf(User.getProfile_id()), User.getNotificationMsgDetail().get(finalJ).getTitle(), User.getName(), profileImg, type);
                                            Log.e("Exception_GET_NOTIFICATION_LIST", "run: try");
                                        } catch (Exception e) {
                                            Log.e("Exception_GET_NOTIFICATION_LIST", "run: Exception " + e.getMessage());
                                        }
                                    }
                                }, (time1 * (j + 1)));
                            }
                        }
                    }, ((time1 * i) + (time2 * i)));
                }

            }

            if (ServiceCode == Constant.USER_LIST_NEXT_PAGE) {
                UserListResponseMet rsp = (UserListResponseMet) response;

                mSwipeRefreshLayout.setRefreshing(false);

                homeUserAdapter.removeLoadingFooter();
                isLoading = false;

                List<UserListResponseMet.Data> results = rsp.getResult().getData();

                // Shuffle Data
                //  Collections.shuffle(results);

                list.addAll(results);


                homeUserAdapter.addAll(results);

                if (currentPage != TOTAL_PAGES) homeUserAdapter.addLoadingFooter();
                else isLastPage = true;
            }


            if (ServiceCode == Constant.GET_BANNER) {
                BannerResponse rsp = (BannerResponse) response;

                if (rsp.getResult() != null && rsp.getResult().size() > 0) {
                    // Banner Code

                    offerImageAdapter = new OfferImageAdapter(getActivity(), rsp.getResult());
                    offerBanner.setAdapter(offerImageAdapter);
                    offerBanner.setClipChildren(false);
                    offerBanner.setClipToPadding(false);
                    offerBanner.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
                    offerBanner.setPadding(4, 0, 4, 0);
                    offerBanner.setInterval(3000);
                    offerBanner.startAutoScroll();
                    offerBanner.setVisibility(View.VISIBLE);
                }
            }

            if (ServiceCode == Constant.LAN_DATA) {
                LanguageResponce rsp = (LanguageResponce) response;

                if (!rsp.getSuccess()) {
                    //      Log.e("errCode", rsp.getError());
                    logoutDialog();
                    return;
                }

                if (rsp.getResult() != null) {
                    languageResponceArrayList = new ArrayList<>();

                    LanguageData languageData = new LanguageData();

                    languageData.setId(0);
                    languageData.setLanguage("All");

                    languageResponceArrayList.add(languageData);

                    languageResponceArrayList.addAll(rsp.getResult());

                    languageData = new LanguageData();
                    languageData.setId(0);
                    languageData.setLanguage("search");

                    languageResponceArrayList.add(languageData);
                    //    Log.e("arrayData", new Gson().toJson(languageResponceArrayList));

                    languageAdapter = new LanguageAdapter(getContext(), languageResponceArrayList);


                    try {

                        tv_lan1.setText(languageResponceArrayList.get(1).getLanguage());
                        tv_lan1.setVisibility(View.VISIBLE);
                        tv_lan2.setText(languageResponceArrayList.get(2).getLanguage());
                        tv_lan2.setVisibility(View.VISIBLE);
                        tv_lan3.setText(languageResponceArrayList.get(3).getLanguage());
                        tv_lan3.setVisibility(View.VISIBLE);
                        tv_lan4.setText(languageResponceArrayList.get(4).getLanguage());
                        tv_lan4.setVisibility(View.VISIBLE);

                        tv_lan5.setText(languageResponceArrayList.get(5).getLanguage());
                        tv_lan5.setVisibility(View.VISIBLE);
                        tv_lan6.setText(languageResponceArrayList.get(6).getLanguage());
                        tv_lan6.setVisibility(View.VISIBLE);


                    } catch (Exception e) {
                    }

                }
            }

            // PAY_DETAILS
            if (ServiceCode == Constant.PAY_DETAILS) {
                PaymentGatewayResponce rsp = (PaymentGatewayResponce) response;
                new SessionManager(getContext()).setUserWall(rsp.getData().getBalance().getTotalPoint());

                new SessionManager(getContext()).setUserStriepKS(rsp.getData().getStripe().getPublishableKey(),
                        rsp.getData().getStripe().getSecretKey());

                new SessionManager(getContext()).setUserRazKS(rsp.getData().getRazorpay().getKeyId(),
                        rsp.getData().getRazorpay().getKeySecret());

            }


            if (ServiceCode == GET_FIRST_TIME_RECHARGE) {

                DiscountedRechargeResponse rsp = (DiscountedRechargeResponse) response;

                if (rsp.getIsRecharge() == 0) {
                    Log.e("getIsRecharge", "isSuccess: Home Fragment if " + rsp.getIsRecharge());
                    // FirstTimeRechargeDialog();
                    apiManager.getFirstTimeRechargeList();
                } else if (rsp.getIsRecharge() == 1) {
                    Log.e("getIsRecharge", "isSuccess: Home Fragment else " + rsp.getIsRecharge());
                }


            }

            if (ServiceCode == GET_FIRST_TIME_RECHARGE_LIST) {

                FirstTimeRechargeListResponse firstTimeRechargeListResponse = (FirstTimeRechargeListResponse) response;
                RechargePlanResponseNew.Data firstRecharge = firstTimeRechargeListResponse.getResult();

                Log.e("FirstTimeRechargeListResponse", "isSuccess: HomeFrag" + new Gson().toJson(firstTimeRechargeListResponse));
                FirstTimeRechargeDialog(firstRecharge);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        if (ServiceCode == Constant.PROFILE_DETAILS) {
            ProfileDetailsResponse rsp = (ProfileDetailsResponse) response;

            if (rsp.getSuccess().getProfile_images() != null && rsp.getSuccess().getProfile_images().size() > 0) {
                Log.e("profilePicLog", rsp.getSuccess().getProfile_images().get(0).getImage_name());

                new SessionManager(getContext()).setUserProfilepic(rsp.getSuccess().getProfile_images().get(0).getImage_name());
            }
        }
    }


    private String getMessageWithCall(String receiverId, String userName, String userId, String uniqueId, String isFreeCall, String profilePic, String callType, long canCallTill) {
        JSONObject messageObject = new JSONObject();
        JSONObject OtherInfoWithCall = new JSONObject();
        try {
            OtherInfoWithCall.put("UserName", userName);
            OtherInfoWithCall.put("UserId", userId);
            OtherInfoWithCall.put("UniqueId", uniqueId);
            OtherInfoWithCall.put("IsFreeCall", isFreeCall);
            OtherInfoWithCall.put("Name", userName);
            OtherInfoWithCall.put("ProfilePicUrl", profilePic);
            OtherInfoWithCall.put("CallType", callType);
            OtherInfoWithCall.put("CallAutoEnd", canCallTill);
            messageObject.put("isMessageWithCall", "yes");
            messageObject.put("CallMessageBody", OtherInfoWithCall.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String msg = messageObject.toString();
        return msg;
    }


    /* private void initiateMessageWithCall(String receiverId, String userName, String userId, String uniqueId, String isFreeCall, String profilePic, String callType) {

     JSONObject messageObject = new JSONObject();

     JSONObject OtherInfoWithCall = new JSONObject();
     try {
         OtherInfoWithCall.put("UserName", userName);
         OtherInfoWithCall.put("UserId", userId);
         OtherInfoWithCall.put("UniqueId", uniqueId);
         OtherInfoWithCall.put("IsFreeCall", isFreeCall);
         OtherInfoWithCall.put("Name", userName);
         OtherInfoWithCall.put("ProfilePicUrl", profilePic);
         OtherInfoWithCall.put("CallType", callType);
         messageObject.put("isMessageWithCall", "yes");
         messageObject.put("CallMessageBody", OtherInfoWithCall.toString());

     } catch (JSONException e) {
         e.printStackTrace();
     }

     ZIMTextMessage zimMessage = new ZIMTextMessage();
     zimMessage.message = messageObject.toString();

     *//*
            ZegoZIMManager.getInstance().zim.sendPeerMessage(zimMessage, receiverId, (message, errorInfo) -> {
            Log.e("CALLBACK", "message " + message + "   errorinfo  " + errorInfo.getMessage());

        });
        *//*

    }
*/
    void logoutDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_exit);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();

        TextView closeDialog = dialog.findViewById(R.id.close_dialog);
        TextView tv_msg = dialog.findViewById(R.id.tv_msg);
        TextView logout = dialog.findViewById(R.id.logout);

        tv_msg.setText("You have been logout. As your access token is expired.");

        closeDialog.setVisibility(View.GONE);
        closeDialog.setOnClickListener(view -> dialog.dismiss());

        logout.setText("OK");
        logout.setOnClickListener(view -> {
            dialog.dismiss();
            new SessionManager(getContext()).logoutUser();
            apiManager.getUserLogout();
            getActivity().finish();
        });
    }

    @Override
    public void retryPageLoad() {
        //apiManager.getUserListNextPage(String.valueOf(currentPage), "");
    }

    void resetPages() {
        // Reset Current page when refresh data
        this.currentPage = 1;
        this.isLastPage = false;
    }

    private Random randomGenerator = new Random();

    private void sendChatNotification(String fcmToken, String profileId, String message, String profileName, String profileImage, String type) {
        Log.e("offLineDataLog", "sendChatNotification: " + "fcmtoken  " + fcmToken);
        Data data = new Data("offline_notification", profileId, message, profileName, profileImage, type);
        Sender sender = new Sender(data, fcmToken);
        Log.e("offLineDataLog", new Gson().toJson(sender));
        // Log.e("offLineDataLog", "sendChatNotification: "+sender.notification.getTitle() );
        ApiInterface apiService = FirebaseApiClient.getClient().create(ApiInterface.class);

        apiService.sendNotificationInBox(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                Log.e("offline_notification_home", new Gson().toJson(response.body()));
                //Log.e("offline_notification", new Gson().toJson(response.message()));
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                Log.e("notificationFailour", t.getMessage());
            }
        });
    }


    int gStatus;


    void consentReminder() {
        String type = "text";
        String fcmToken = new SessionManager(getContext()).getFcmToken();
        gStatus = new SessionManager(getContext()).getGuestStatus();
      /*  if (gStatus == 1) {
            for (int i = 0; i < 4; i++) {
                int index = randomGenerator.nextInt(list.size());
                UserListResponse.Data data = list.get(index);
                //   int index2 = randomGenerator.nextInt(messageStack.size());
                //  Log.e("randomItemFromList", "index=" + index + " data=" + new Gson().toJson(data));
                Log.e("consentReminder", ": size " + list.size());

                switch (i) {
                    case 0:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    sendChatNotification(fcmToken, data.getProfile_id() + "", messageStack.get(0), data.getName(), data.getProfile_images().get(0).getImage_name(), type);
                                } catch (Exception e) {

                                }
                            }
                        }, 30000);
                        break;

                    case 1:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    sendChatNotification(fcmToken, data.getProfile_id() + "", messageStack.get(1), data.getName(), data.getProfile_images().get(0).getImage_name(), type);
                                } catch (Exception e) {

                                }
                            }
                        }, 90000);
                        break;

                    case 2:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    sendChatNotification(fcmToken, data.getProfile_id() + "", messageStack.get(2), data.getName(), data.getProfile_images().get(0).getImage_name(), type);
                                } catch (Exception e) {

                                }
                            }
                        }, 150000);
                        break;

                    case 3:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    sendChatNotification(fcmToken, data.getProfile_id() + "", messageStack.get(3), data.getName(), data.getProfile_images().get(0).getImage_name(), type);
                                } catch (Exception e) {

                                }
                            }
                        }, 210000);
                        break;
                }

            }


        }*/
    }

    public void showProgress() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    private String callType = "", profileId = "", callRate = "", hostName = "", hostImage = "";
    private long userId;

    public void startVideoCall(String profileId, String callRate, long userId, String hostName, String hostImage) {

        //    CheckPermission();
        Log.e("STARTVIDEOCALL_NEARBY", "startVideoCall: homefragment " + userId);

        //  Log.e("HomeFragmentnn", "startVideoCall: " + CheckPermission());


        if (CheckPermission()) {
            callType = "video";

            this.profileId = profileId;
            //callrate set to 1 for testing
            this.callRate = callRate;
            this.userId = userId;
            this.hostName = hostName;
            this.hostImage = hostImage;
            Log.e("startCallRR", "startVideoCall: userid " + userId + " profileid " + profileId+" "+callRate);
            Log.e("ProfileIdTestFB", "HomeFragment startVideoCall: " + profileId);
            chatRef = FirebaseDatabase.getInstance().getReference().child("Users").child(profileId);
            //apiManager.getRemainingGiftCardFunction();
            apiManager.generateCallRequestZ(Integer.parseInt(profileId), String.valueOf(System.currentTimeMillis()), "0", Integer.parseInt(callRate),
                    Boolean.parseBoolean("false"), String.valueOf(0));

        } else {
            //  Toast.makeText(getContext(), "To Make a call Camera and Audio permission must.Go to setting to allow the permissions", Toast.LENGTH_SHORT).show();
        }
        Log.e("HomeFragment", "startVideoCall: guest call " + "start");
    }

   /*  private void checkbusyOrNot(String profileId) {

        String uid=String.valueOf(profileId);

        chatRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);


        // Read from the database
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Map<String, Object> map = null;

                if (snapshot.exists()) {
                    map = (Map<String, Object>) snapshot.getValue();
                    // Toast.makeText(getApplicationContext(),""+map.get("status"),Toast.LENGTH_LONG).show();

                    if (map.get("status").equals("Online")) {

                        Log.e(TAG, "onDataChange: "+"Online" );

                    }

                    if (map.get("status").equals("Offline")) {

                        Log.e(TAG, "onDataChange: "+"Offline" );

                    }
                    if (map.get("status").equals("Busy"))
                    {
                        Log.e(TAG, "onDataChange: "+"Busy" );
                        Toast.makeText(getContext(),"user busy",Toast.LENGTH_SHORT).show();

                    }





                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });




    }*/


    private boolean CheckPermission() {

        final boolean[] isPermissionGranted = new boolean[1];

        Log.e("CHECK_PERMISSIONS", "CheckPermission: ");


        String[] permissions;

        if (Build.VERSION.SDK_INT >= 33) {
            permissions = new String[]{Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA};
            Log.e(TAG, "onCreate: Permission for android 13");
        } else {

            permissions = new String[]{Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            Log.e("ViewProfile", "onCreate: Permission for below android 13");

        }


        Dexter.withActivity(getActivity()).withPermissions(permissions).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                Log.e("onPermissionsChecked", "onPermissionsChecked: ");

                if (report.areAllPermissionsGranted()) {
                    Log.e("onPermissionsChecked", "all permission granted");
                    isPermissionGranted[0] = true;
                } else {
                    isPermissionGranted[0] = false;
                    Toast.makeText(getContext(), "To use this feature Camera and Audio permissions are must.You need to allow the permissions", Toast.LENGTH_SHORT).show();
                }

              /*        if (report.getDeniedPermissionResponses().size() + report.getGrantedPermissionResponses().size() == 3) {
                    Log.e("onPermissionsChecked", ""+report.getDeniedPermissionResponses().size()+ "    "+report.getGrantedPermissionResponses().size() );

                    if (report.isAnyPermissionPermanentlyDenied()) {
                        isPermissionGranted[0] = false;
                        Log.e("onPermissionsChecked", "all permission denied");


                    }
                }*/
              /*  else {
                    isPermissionGranted[0] = false;

                    Dexter.withActivity(getActivity()).withPermissions(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }*/
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                Log.e("onPermissionsChecked", "onPermissionRationaleShouldBeShown");
                token.continuePermissionRequest();

            }


        }).onSameThread().check();

        return isPermissionGranted[0];
    }


}
