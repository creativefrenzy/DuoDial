package com.klive.app.retrofit;

import static com.klive.app.utils.Constant.BUY_STORE_ITEM;
import static com.klive.app.utils.Constant.GET_NOTIFICATION_LIST;
import static com.klive.app.utils.Constant.GET_STORE_PURCHASE_TAB_LIST;
import static com.klive.app.utils.Constant.GET_STORE_TAB_LIST;
import static com.klive.app.utils.Constant.GET_VIDEO_STATUS_LIST;
import static com.klive.app.utils.Constant.USE_OR_REMOVE_ITEM;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.klive.app.dialogs.MyProgressDialog;
import com.klive.app.extras.BannerResponseNew;
import com.klive.app.extras.MessageCallDataRequest;
import com.klive.app.model.AgencyResponse;
import com.klive.app.model.AppUpdate.UpdateResponse;
import com.klive.app.model.BankList.BankListResponce;
import com.klive.app.model.CallPriceUpdateResponse;
import com.klive.app.model.FcmTokenResponse;
import com.klive.app.model.IncomeReportResponce.IncomeReportFemale;
import com.klive.app.model.NewWallet.WalletResponce;
import com.klive.app.model.PaymentRequestResponce.PaymentRequestResponce;
import com.klive.app.model.PriceList.priceupdateModel;
import com.klive.app.model.PriceListResponse;
import com.klive.app.model.RequestGiftRequest.RequestGiftRequest;
import com.klive.app.model.RequestGiftRequest.RequestGiftResponce;
import com.klive.app.model.SubmitResponse;
import com.klive.app.model.UpdateProfileNewResponse;
import com.klive.app.model.UpdateProfileResponse;
import com.klive.app.model.Video.VideoResponce;
import com.klive.app.model.VideoStatus.VideoStatusResponseModel;
import com.klive.app.model.WalletBalResponse;
import com.klive.app.model.WalletRechargeResponse;
import com.klive.app.model.Walletfilter.WalletfilterResponce;
import com.klive.app.model.account.AccountResponse;
import com.klive.app.model.body.CallRecordBody;
import com.klive.app.model.EndCallData.EncCallResponce;
import com.klive.app.model.EndCallData.EndCallData;
import com.klive.app.model.LoginResponse;
import com.klive.app.model.OnlineStatusResponse;
import com.klive.app.model.ProfileDetailsResponse;
import com.klive.app.model.UserListResponseNew.UserListResponseNewData;
import com.klive.app.model.city.CityResponse;
import com.klive.app.model.fcm.Data;
import com.klive.app.model.fcm.MyResponse;
import com.klive.app.model.fcm.Sender;
import com.klive.app.model.gift.SendGiftRequest;
import com.klive.app.model.gift.SendGiftResult;
import com.klive.app.model.language.LanguageResponce;
import com.klive.app.model.level.LevelDataResponce;
import com.klive.app.model.logout.LogoutResponce;
import com.klive.app.response.AddAccount.AddAccountResponse;
import com.klive.app.response.Agency.AgencyPolicyResponse;
import com.klive.app.response.AgencyDate.AgencyCenterDateResponse;
import com.klive.app.response.AgencyHostWeekly.AgencyHostWeeklyResponse;
import com.klive.app.response.AgencyHostWeekly.WeeklyRewardResponse;
import com.klive.app.response.Banner.BannerResponse;
import com.klive.app.response.DataFromProfileId.DataFromProfileIdResponse;
import com.klive.app.response.DisplayGiftCount.GiftCountResult;
import com.klive.app.response.HostIncomeDetail.IncomeDetailResponse;
import com.klive.app.response.HostIncomeResponse.IncomeResponse;
import com.klive.app.response.NewVideoStatus.NewVideoStatusResponse;
import com.klive.app.response.NewZegoTokenResponse;
import com.klive.app.response.ReportResponse;
import com.klive.app.response.SettlementCenter.HostSettlementDateResponse;
import com.klive.app.response.SettlementDate.SettlementHostWeeklyResponse;
import com.klive.app.response.TopReceiver.TopReceiverResponse;
import com.klive.app.response.UdateAccountResponse;
import com.klive.app.response.UserListResponse;
import com.klive.app.response.VideoPlayResponce;
import com.klive.app.response.accountvarification.CheckFemaleVarifyResponse;
import com.klive.app.response.metend.AdapterRes.UserListResponseMet;
import com.klive.app.response.metend.AddRemoveFavResponse;
import com.klive.app.response.metend.Ban.BanResponce;
import com.klive.app.response.metend.CreatePaymentResponse;
import com.klive.app.response.metend.DirectUPI.RazorpayPurchaseResponse;
import com.klive.app.response.metend.DiscountedRecharge.DiscountedRechargeResponse;
import com.klive.app.response.metend.FirstTimeRechargeListResponse;
import com.klive.app.response.metend.FollowingUsers;
import com.klive.app.response.metend.GenerateCallResponce.GenerateCallResponce;
import com.klive.app.response.metend.PaymentGatewayDetails.CashFree.CFToken.CfTokenResponce;
import com.klive.app.response.metend.PaymentGatewayDetails.CashFree.CashFreePayment.CashFreePaymentRequest;
import com.klive.app.response.metend.PaymentGatewayDetails.PaymentGatewayResponce;
import com.klive.app.response.metend.PaymentSelector.PaymentSelectorResponce;
import com.klive.app.response.metend.PaytmDirect.PaytmResponse;
import com.klive.app.response.metend.RechargePlan.RechargePlanResponseNew;
import com.klive.app.response.metend.RemainingGiftCard.RemainingGiftCardResponce;
import com.klive.app.response.metend.gift.GiftEmployeeResult;
import com.klive.app.response.metend.new_notifications.NewNotificationResponse;
import com.klive.app.response.metend.store.response.purchase.BuyStoreItemResponse;
import com.klive.app.response.metend.store.response.use_or_remove.UseOrRemoveItemResponse;
import com.klive.app.response.metend.store_list.StoreResponse;
import com.klive.app.response.newgiftresponse.NewGift;
import com.klive.app.response.newgiftresponse.NewGiftListResponse;
import com.klive.app.response.newgiftresponse.NewGiftResult;
import com.klive.app.response.sub_agency.SubAgencyResponse;
import com.klive.app.response.temporary_block.TempBlockResponse;
import com.klive.app.response.temporary_block.TemporaryBlockResponse;
import com.klive.app.response.trading_response.GetTradingUserNameResponse;
import com.klive.app.response.trading_response.TradingAccountResponse;
import com.klive.app.response.trading_response.TradingHistoryResponse;
import com.klive.app.response.trading_response.TradingTransferModel;
import com.klive.app.response.trading_response.TransferTradeAccountResponse;
import com.klive.app.response.trading_response.UpdateHistoryModel;
import com.klive.app.response.trading_response.UpdateTransferDetailResponse;
import com.klive.app.response.trading_response.UserIdBodyModel;
import com.klive.app.utils.Constant;
import com.klive.app.utils.SessionManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {
    private Context mContext;
    private MyProgressDialog dialog;
    private ApiResponseInterface mApiResponseInterface;
    private ApiInterface apiService;
    String authToken;

    public ApiManager(Context context, ApiResponseInterface apiResponseInterface) {
        this.mContext = context;
        this.mApiResponseInterface = apiResponseInterface;
        apiService = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        dialog = new MyProgressDialog(mContext);
        authToken = Constant.BEARER + new SessionManager(context).getUserToken();
        Log.e("authToken", authToken);
    }

    public ApiManager(Context context) {
        this.mContext = context;
        apiService = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        this.authToken = authToken;
        authToken = Constant.BEARER + new SessionManager(context).getUserToken();
    }

    public void sendOTP(String url) {
        //showDialog();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        ApiInterface retrofitAPI = retrofit.create(ApiInterface.class);
        //Call<ResponseBody> call = retrofit.create(JsonPlaceHolderApi.class);
        Call<ResponseBody> call = retrofitAPI.post();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //  Log.e("loginResponce", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {
                    mApiResponseInterface.isSuccess(response.body(), Constant.OTP);
                } else {
                    Toast.makeText(mContext, "Invalid User", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    public void loginGuest(String email, String password, String hash) {
        showDialog();
        Call<LoginResponse> call = apiService.loginUser(email, password, hash);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Log.e("loginResponce", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.LOGIN);

                    } else {
                        mApiResponseInterface.isError(response.body().getError());
                    }
                } else if (response.code() == 401) {
//                    Log.e("errorResponce", response.body().getError());
                    Toast.makeText(mContext, "Wrong Password", Toast.LENGTH_SHORT).show();
                    //    Toast.makeText(mContext, response.body().getError(), Toast.LENGTH_SHORT).show();

                }
                closeDialog();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                closeDialog();
                //     Log.e("loginResponceError", t.getMessage());
                //   Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void login(String id, String password, String hash) {
        //showDialog();
        Call<LoginResponse> call = apiService.loginUserMobile(id, password, hash);
        Log.e("loginResponce", "request => " + call.request());

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
//                Log.e("loginRequest", call.request().toString());
                Log.e("loginResponce", new Gson().toJson(response.body()));

                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.LOGIN);
                    } else {
                        mApiResponseInterface.isError(response.body().getError());
                        // Log.e("loginResponce1", "onResponse: error " + response.body().getError() + "  error message  " + response.body().getMessage());

                        Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 401) {
//                    Log.e("errorResponce", response.body().getError());
                    Toast.makeText(mContext, "Invalid User", Toast.LENGTH_SHORT).show();
                    //    Toast.makeText(mContext, response.body().getError(), Toast.LENGTH_SHORT).show();

                }
                mApiResponseInterface.isError(response.raw().message());
                //closeDialog();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                //closeDialog();
                //     Log.e("loginResponceError", t.getMessage());
                //   Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }


    public void getWinnerList(String interval) {
        //showDialog();
        Call<TopReceiverResponse> call = apiService.getWinnerList(authToken, "application/json", interval);
        // Log.e("lanID", String.valueOf(new SessionManager(mContext).gettLangState()));
        call.enqueue(new Callback<TopReceiverResponse>() {
            @Override
            public void onResponse(Call<TopReceiverResponse> call, Response<TopReceiverResponse> response) {
                Log.e("winnerrequest", call.request().toString());
                Log.e("winnerList", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getResult() != null) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.WINNER_USER);
                    }
                }
            }

            @Override
            public void onFailure(Call<TopReceiverResponse> call, Throwable t) {
                //Log.e("userListErr", t.getMessage());
                //Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getBannerList(String type) {
        Call<BannerResponse> call = apiService.getBannerData(authToken, "application/json", type);
        Log.e("authToken", authToken);
        call.enqueue(new Callback<BannerResponse>() {
            @Override
            public void onResponse(Call<BannerResponse> call, Response<BannerResponse> response) {
                //Log.e("BannerList", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getResult() != null) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.BANNER_LIST);
                    }
                }
            }

            @Override
            public void onFailure(Call<BannerResponse> call, Throwable t) {
                Log.e("BannerListErr", t.getMessage());
                Toast.makeText(mContext, "Banner Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getBannerListNew(String type) {
        Call<BannerResponseNew> call = apiService.getBannerDataNew(authToken, "application/json", type);
        Log.e("authToken", authToken);
        call.enqueue(new Callback<BannerResponseNew>() {
            @Override
            public void onResponse(Call<BannerResponseNew> call, Response<BannerResponseNew> response) {
                //Log.e("BannerList", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getResult() != null) {
                        Log.e("BannerListRes", "onResponse: " + new Gson().toJson(response.body()));
                        mApiResponseInterface.isSuccess(response.body(), Constant.BANNER_LIST);
                    }
                }
            }

            @Override
            public void onFailure(Call<BannerResponseNew> call, Throwable t) {
                Log.e("BannerListErr", t.getMessage());
                Toast.makeText(mContext, "Banner Error", Toast.LENGTH_LONG).show();
            }
        });
    }


    public void getHostThisWeekIncome() {
        Call<IncomeResponse> call = apiService.getHostThisWeekIncome(authToken, "application/json");
        call.enqueue(new Callback<IncomeResponse>() {
            @Override
            public void onResponse(Call<IncomeResponse> call, Response<IncomeResponse> response) {
                Log.e("Incomerequest", call.request().toString());
                Log.e("Incomerequest", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getResult() != null) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.GET_HOST_THIS_WEEK_DATA);
                    }
                }

            }

            @Override
            public void onFailure(Call<IncomeResponse> call, Throwable t) {
                //Log.e("userListErr", t.getMessage());
                //Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getHostAllWeekIncome(String month) {
        Call<IncomeResponse> call = apiService.getHostAllWeekIncome(authToken, "application/json", month);
        call.enqueue(new Callback<IncomeResponse>() {
            @Override
            public void onResponse(Call<IncomeResponse> call, Response<IncomeResponse> response) {
                Log.e("IncomerequestAll", call.request().toString());
                Log.e("IncomerequestAll", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {
                    mApiResponseInterface.isSuccess(response.body(), Constant.GET_HOST_ALL_WEEK_DATA);
                }
            }

            @Override
            public void onFailure(Call<IncomeResponse> call, Throwable t) {
                //   Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getIncomeDetails(String month) {
        Call<IncomeDetailResponse> call = apiService.geIncomeDetails(authToken, "application/json", month);
        call.enqueue(new Callback<IncomeDetailResponse>() {
            @Override
            public void onResponse(Call<IncomeDetailResponse> call, Response<IncomeDetailResponse> response) {
                Log.e("Incomedeatilrequest", call.request().toString());
                Log.e("Incomedetailrequest", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {
                    mApiResponseInterface.isSuccess(response.body(), Constant.INCOME_REPORT_DETAIL);
                }
            }

            @Override
            public void onFailure(Call<IncomeDetailResponse> call, Throwable t) {
                //   Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }


    public void changeOnlineStatus(int status) {
        Call<OnlineStatusResponse> call = apiService.manageOnlineStatus(authToken, "application/json", status);
        call.enqueue(new Callback<OnlineStatusResponse>() {
            @Override
            public void onResponse(Call<OnlineStatusResponse> call, Response<OnlineStatusResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("changeOnlineStatus", new Gson().toJson(response.body()));
                    Log.e("FastScreenDestroy", "onDestroy: set offline in api :   " + new Gson().toJson(response.body()));
                    try {
                        if (response.body().getResult() != null) {
                            mApiResponseInterface.isSuccess(response.body(), Constant.MANAGE_ONLINE_STATUS);
                        } else {
                            mApiResponseInterface.isError(response.body().getError());
                        }
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onFailure(Call<OnlineStatusResponse> call, Throwable t) {
            }
        });
    }

    public void languageList(String token) {
        Call<LanguageResponce> call = null;

        String role = new SessionManager(mContext).getRole();
        Log.e("roleLog", role);

        if (role.equals("5")) {
            call = apiService.languageList(authToken);
        } else {
            call = apiService.languageList(token);
        }

        call.enqueue(new Callback<LanguageResponce>() {
            @Override
            public void onResponse(Call<LanguageResponce> call, Response<LanguageResponce> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("changeOnlineStatus", new Gson().toJson(response.body()));
                    try {
                        if (response.body().getResult() != null) {
                            mApiResponseInterface.isSuccess(response.body(), Constant.LANGUAGE);
                        } else {
                            mApiResponseInterface.isError(response.body().getError());
                        }
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onFailure(Call<LanguageResponce> call, Throwable t) {
            }
        });
    }

    public void cityList(String token) {
        Call<CityResponse> call = null;

        String role = new SessionManager(mContext).getRole();
        Log.e("roleLog", role);

        if (role.equals("5")) {
            call = apiService.cityList(authToken);
        } else {
            call = apiService.cityList(token);
        }

        call.enqueue(new Callback<CityResponse>() {
            @Override
            public void onResponse(Call<CityResponse> call, Response<CityResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("changeOnlineStatus", new Gson().toJson(response.body()));
                    try {
                        if (response.body().getGetResponse() != null) {
                            mApiResponseInterface.isSuccess(response.body(), Constant.CITY);
                        } else {
                            mApiResponseInterface.isError(response.body().getError());
                        }
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onFailure(Call<CityResponse> call, Throwable t) {
            }
        });
    }

    public void getUserListNextPage(String pageNumber, String search) {
        //  showDialog();
        Call<com.klive.app.model.UserListResponse> call = apiService.getUserList(authToken, "application/json", search, pageNumber, "16", String.valueOf(new SessionManager(mContext).gettLangState()));
        // Log.e("authToken", authToken);
       /* Log.e("authToken", authToken);
        Log.e("pageNumber", pageNumber);
        Log.e("lanID", String.valueOf(new SessionManager(mContext).gettLangState()));
*/
        call.enqueue(new Callback<com.klive.app.model.UserListResponse>() {
            @Override
            public void onResponse(Call<com.klive.app.model.UserListResponse> call, Response<com.klive.app.model.UserListResponse> response) {
                Log.e("userListNXT", new Gson().toJson(response.body()));

                if (response.isSuccessful() && response.body() != null) {

                    if (response.body().getResult().getData() != null) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.USER_LIST_NEXT_PAGE);
                    }
                }
                //  closeDialog();
            }

            @Override
            public void onFailure(Call<com.klive.app.model.UserListResponse> call, Throwable t) {
                //   closeDialog();
                //        Log.e("userListErrNXT", t.getMessage());
                //   Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getPopularList(String pageNumber, String search) {
        //showDialog();
        Call<com.klive.app.model.UserListResponse> call = apiService.getPopulartList(authToken, "application/json", search, pageNumber, "16", String.valueOf(new SessionManager(mContext).gettLangState()));

        // Log.e("lanID", String.valueOf(new SessionManager(mContext).gettLangState()));

        call.enqueue(new Callback<com.klive.app.model.UserListResponse>() {
            @Override
            public void onResponse(Call<com.klive.app.model.UserListResponse> call, Response<com.klive.app.model.UserListResponse> response) {
                //Log.e("userList", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {

                    if (response.body().getResult().getData() != null) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.USER_LIST);
                    }
                }
                //   closeDialog();
            }

            @Override
            public void onFailure(Call<com.klive.app.model.UserListResponse> call, Throwable t) {
                //     closeDialog();
                //      Log.e("userListErr", t.getMessage());
                //  Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getUserListNearbyNextPage(String pageNumber, String search) {
        //  showDialog();
        Call<com.klive.app.model.UserListResponse> call = apiService.getNearbyList(authToken, "application/json", search, pageNumber, "16", String.valueOf(new SessionManager(mContext).gettLangState()));

       /* Log.e("authToken", authToken);
        Log.e("pageNumber", pageNumber);
        Log.e("lanID", String.valueOf(new SessionManager(mContext).gettLangState()));
*/
        call.enqueue(new Callback<com.klive.app.model.UserListResponse>() {
            @Override
            public void onResponse(Call<com.klive.app.model.UserListResponse> call, Response<com.klive.app.model.UserListResponse> response) {
                Log.e("userListNXT", new Gson().toJson(response.body()));

                if (response.isSuccessful() && response.body() != null) {

                    if (response.body().getResult().getData() != null) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.USER_LIST_NEXT_PAGE);
                    }
                }
                //  closeDialog();
            }

            @Override
            public void onFailure(Call<com.klive.app.model.UserListResponse> call, Throwable t) {
                //   closeDialog();
                //        Log.e("userListErrNXT", t.getMessage());
                //   Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getUserListNearby(String pageNumber, String search) {
        //showDialog();
        Call<com.klive.app.model.UserListResponse> call = apiService.getNearbyList(authToken, "application/json", search, pageNumber, "16", String.valueOf(new SessionManager(mContext).gettLangState()));

        // Log.e("lanID", String.valueOf(new SessionManager(mContext).gettLangState()));

        call.enqueue(new Callback<com.klive.app.model.UserListResponse>() {
            @Override
            public void onResponse(Call<com.klive.app.model.UserListResponse> call, Response<com.klive.app.model.UserListResponse> response) {
                // Log.e("nearByUserList", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {

                    if (response.body().getResult().getData() != null) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.USER_LIST);
                    }
                }
                //   closeDialog();
            }

            @Override
            public void onFailure(Call<com.klive.app.model.UserListResponse> call, Throwable t) {
                //     closeDialog();
                //      Log.e("userListErr", t.getMessage());
                //   Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void changeOnlineStatusBack(int status) {
        //  showDialog();
        Call<OnlineStatusResponse> call = apiService.manageOnlineStatus(authToken, "application/json", status);

        call.enqueue(new Callback<OnlineStatusResponse>() {
            @Override
            public void onResponse(Call<OnlineStatusResponse> call, Response<OnlineStatusResponse> response) {
                //   Log.e("onlineSatusBack", new Gson().toJson(response.body()));
                //      closeDialog();
                new SessionManager(mContext).setOnlineFromBack(0);
            }

            @Override
            public void onFailure(Call<OnlineStatusResponse> call, Throwable t) {
                //     closeDialog();
            }
        });
    }


    public void getcallCutByHost(String unique_id) {
        Call<Object> call = apiService.callCutByHost(authToken, "application/json", unique_id);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.e("getcallCutByHost", new Gson().toJson(response.body()));
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e("getcallCutByHostError", t.getMessage());
            }
        });
    }

    public void submitEndCallData(ArrayList<EndCallData> endCallData) {

        Call<EncCallResponce> call = apiService.sendEndCallTime(authToken, "application/json", endCallData);

        call.enqueue(new Callback<EncCallResponce>() {
            @Override
            public void onResponse(Call<EncCallResponce> call, Response<EncCallResponce> response) {
                //   Log.e("submitEndCallData", new Gson().toJson(response.body()));
                try {
                    if (response.body().getSuccess()) {
                        new SessionManager(mContext).setUserGetendcalldata("success");
                    } else {
                        new SessionManager(mContext).setUserGetendcalldata("error");
                    }
                } catch (Exception e) {
                    new SessionManager(mContext).setUserGetendcalldata("error");
                }
            }

            @Override
            public void onFailure(Call<EncCallResponce> call, Throwable t) {
                // Log.e("submitEndCallError", t.getMessage());
                new SessionManager(mContext).setUserGetendcalldata("error");

            }
        });
    }

    public void getUpdateApp() {
        Call<UpdateResponse> call = apiService.getUpdateApp();
        call.enqueue(new Callback<UpdateResponse>() {
            @Override
            public void onResponse(Call<UpdateResponse> call, Response<UpdateResponse> response) {
                try {
                    Log.e("getUpdateApp", new Gson().toJson(response.body()));
                    mApiResponseInterface.isSuccess(response.body(), Constant.GET_MAINTAINENCE_DATA);
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<UpdateResponse> call, Throwable t) {
            }
        });
    }

    public void getProfileData(String id, String search) {
        //showDialog();
        Log.e("newProfileFemaleId", id);
        Call<UserListResponseNewData> call = apiService.getProfileData(authToken, "application/json", search, "", id, String.valueOf(new SessionManager(mContext).gettLangState()));
        // Log.e("lanID", String.valueOf(new SessionManager(mContext).gettLangState()));
        call.enqueue(new Callback<UserListResponseNewData>() {
            @Override
            public void onResponse(Call<UserListResponseNewData> call, Response<UserListResponseNewData> response) {
                Log.e("getprofiledata", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {

                    //if (response.body().getResult().get(0) != null) {
                    mApiResponseInterface.isSuccess(response.body(), Constant.GET_PROFILE_DATA);
                    // }
                }
                //   closeDialog();
            }

            @Override
            public void onFailure(Call<UserListResponseNewData> call, Throwable t) {
                //     closeDialog();
                Log.e("getprofiledataerror", t.getMessage());
                //  Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getProfileDataNew(String id, String search) {
        //showDialog();
        Log.e("newProfileFemaleId", id);
        Call<com.klive.app.response.metend.UserListResponseNew.UserListResponseNewData> call = apiService.getProfileDataNew(authToken, "application/json", search, "", id, String.valueOf(new SessionManager(mContext).gettLangState()));
        // Log.e("lanID", String.valueOf(new SessionManager(mContext).gettLangState()));
        call.enqueue(new Callback<com.klive.app.response.metend.UserListResponseNew.UserListResponseNewData>() {
            @Override
            public void onResponse(Call<com.klive.app.response.metend.UserListResponseNew.UserListResponseNewData> call, Response<com.klive.app.response.metend.UserListResponseNew.UserListResponseNewData> response) {
                Log.e("getprofiledata", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {

                    //if (response.body().getResult().get(0) != null) {
                    mApiResponseInterface.isSuccess(response.body(), Constant.GET_PROFILE_DATA);
                    // }
                }
                //   closeDialog();
            }

            @Override
            public void onFailure(Call<com.klive.app.response.metend.UserListResponseNew.UserListResponseNewData> call, Throwable t) {
                //     closeDialog();
                Log.e("getprofiledataerror", t.getMessage());
                //  Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void updateProfileDetails(String data, String type) {
        //showDialog();
        if (type.equals("name")) {
            Call<UpdateProfileResponse> call;
            call = apiService.updateProfileDetailsName(authToken, "application/json", data);
            call.enqueue(new Callback<UpdateProfileResponse>() {
                @Override
                public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {
                    Log.e("updateProfile", new Gson().toJson(response.body()));
                    if (response.isSuccessful() && response.body() != null) {

                        if (response.body().getSuccess()) {
                            mApiResponseInterface.isSuccess(response.body(), Constant.UPDATE_PROFILE);
                        }
                    } else {
                        //   mApiResponseInterface.isError(response.body().getError());
                    }
                }

                @Override
                public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
                    Log.e("profileError", t.getMessage());
                    //closeDialog();
                    //   Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
                }
            });
        } else if (type.equals("age")) {
            Call<UpdateProfileResponse> call;
            call = apiService.updateProfileDetailsDob(authToken, "application/json", data);
            call.enqueue(new Callback<UpdateProfileResponse>() {
                @Override
                public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {
                    Log.e("updateProfile", new Gson().toJson(response.body()));
                    if (response.isSuccessful() && response.body() != null) {

                        if (response.body().getSuccess()) {
                            mApiResponseInterface.isSuccess(response.body(), Constant.UPDATE_PROFILE);
                        }
                    } else {
                        //   mApiResponseInterface.isError(response.body().getError());
                    }
                }

                @Override
                public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
                    Log.e("profileError", t.getMessage());
                    //closeDialog();
                    //   Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
                }
            });
        } else if (type.equals("city")) {
            Call<UpdateProfileResponse> call;
            call = apiService.updateProfileDetailsCity(authToken, "application/json", data);
            call.enqueue(new Callback<UpdateProfileResponse>() {
                @Override
                public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {
                    Log.e("updateProfile", new Gson().toJson(response.body()));
                    if (response.isSuccessful() && response.body() != null) {

                        if (response.body().getSuccess()) {
                            mApiResponseInterface.isSuccess(response.body(), Constant.UPDATE_PROFILE);
                        }
                    } else {
                        //   mApiResponseInterface.isError(response.body().getError());
                    }
                }

                @Override
                public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
                    Log.e("profileError", t.getMessage());
                    //closeDialog();
                    //   Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
                }
            });
        } else if (type.equals("language")) {
            Call<UpdateProfileResponse> call;
            call = apiService.updateProfileDetailsLanguage(authToken, "application/json", Integer.parseInt(data));
            call.enqueue(new Callback<UpdateProfileResponse>() {
                @Override
                public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {
                    Log.e("updateProfile", new Gson().toJson(response.body()));
                    if (response.isSuccessful() && response.body() != null) {

                        if (response.body().getSuccess()) {
                            mApiResponseInterface.isSuccess(response.body(), Constant.UPDATE_PROFILE);
                        }
                    } else {
                        //   mApiResponseInterface.isError(response.body().getError());
                    }
                }

                @Override
                public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
                    Log.e("profileError", t.getMessage());
                    //closeDialog();
                    //   Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
                }
            });
        } else if (type.equals("bio")) {
            Call<UpdateProfileResponse> call;
            call = apiService.updateProfileDetailsAbout(authToken, "application/json", data);
            call.enqueue(new Callback<UpdateProfileResponse>() {
                @Override
                public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {
                    Log.e("updateProfile", new Gson().toJson(response.body()));
                    if (response.isSuccessful() && response.body() != null) {

                        if (response.body().getSuccess()) {
                            mApiResponseInterface.isSuccess(response.body(), Constant.UPDATE_PROFILE);
                        }
                    } else {
                        //   mApiResponseInterface.isError(response.body().getError());
                    }
                }

                @Override
                public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
                    Log.e("profileError", t.getMessage());
                    //closeDialog();
                    //   Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
                }
            });
        } else if (type.equals("profile")) {
            Call<UpdateProfileResponse> call;
            call = apiService.updateProfileDetailsAbout(authToken, "application/json", data);
            call.enqueue(new Callback<UpdateProfileResponse>() {
                @Override
                public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {
                    Log.e("updateProfile", new Gson().toJson(response.body()));
                    if (response.isSuccessful() && response.body() != null) {

                        if (response.body().getSuccess()) {
                            mApiResponseInterface.isSuccess(response.body(), Constant.UPDATE_PROFILE);
                        }
                    } else {
                        //   mApiResponseInterface.isError(response.body().getError());
                    }
                }

                @Override
                public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
                    Log.e("profileError", t.getMessage());
                    //closeDialog();
                    //   Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
                }
            });
        } else if (type.equals("album")) {
            Call<UpdateProfileResponse> call;
            call = apiService.updateProfileDetailsAbout(authToken, "application/json", data);
            call.enqueue(new Callback<UpdateProfileResponse>() {
                @Override
                public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {
                    Log.e("updateProfile", new Gson().toJson(response.body()));
                    if (response.isSuccessful() && response.body() != null) {

                        if (response.body().getSuccess()) {
                            mApiResponseInterface.isSuccess(response.body(), Constant.UPDATE_PROFILE);
                        }
                    } else {
                        //   mApiResponseInterface.isError(response.body().getError());
                    }
                }

                @Override
                public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
                    Log.e("profileError", t.getMessage());
                    //closeDialog();
                    //   Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void uploadProfileImage(MultipartBody.Part profile) {
        Call<UpdateProfileResponse> call;
        call = apiService.updateProfileDetailsProfile(authToken, "application/json", profile);
        call.enqueue(new Callback<UpdateProfileResponse>() {
            @Override
            public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {
                Log.e("updateProfile", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {

                    if (response.body().getSuccess()) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.UPDATE_PROFILE);
                    }
                } else {
                    //   mApiResponseInterface.isError(response.body().getError());
                }
            }

            @Override
            public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
                Log.e("profileError", t.getMessage());
                //closeDialog();
                //   Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void uploadProfileImageNew(MultipartBody.Part profile) {
        Call<UpdateProfileNewResponse> call;
        call = apiService.updateProfileDetailsProfileNew(authToken, "application/json", profile);
        call.enqueue(new Callback<UpdateProfileNewResponse>() {
            @Override
            public void onResponse(Call<UpdateProfileNewResponse> call, Response<UpdateProfileNewResponse> response) {
                Log.e("updateProfileNew", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {

                    if (response.body().getSuccess()) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.UPDATE_PROFILE_NEW);
                    }
                } else {
                    //   mApiResponseInterface.isError(response.body().getError());
                }
            }

            @Override
            public void onFailure(Call<UpdateProfileNewResponse> call, Throwable t) {
                Log.e("profileError", t.getMessage());
                //closeDialog();
                //   Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void uploadAlbumImage(MultipartBody.Part profile, MultipartBody.Part[] album) {
        Call<UpdateProfileResponse> call;
        call = apiService.updateProfileDetailsAlbum(authToken, "application/json", album);
        call.enqueue(new Callback<UpdateProfileResponse>() {
            @Override
            public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {
                Log.e("updateProfile", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {

                    if (response.body().getSuccess()) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.UPDATE_PROFILE);
                    }
                } else {
                    //   mApiResponseInterface.isError(response.body().getError());
                }
            }

            @Override
            public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
                Log.e("profileError", t.getMessage());
                //closeDialog();
                //   Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getProfileDetails() {
        //   showDialog();
        Call<ProfileDetailsResponse> call = apiService.getProfileDetails(authToken, "application/json");
        call.enqueue(new Callback<ProfileDetailsResponse>() {
            @Override
            public void onResponse(Call<ProfileDetailsResponse> call, Response<ProfileDetailsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("profileDetail: ", new Gson().toJson(response.body()));

                    //  if (response.body().isSuccess()) {
                    mApiResponseInterface.isSuccess(response.body(), Constant.PROFILE_DETAILS);

                    // } else {
                    //      mApiResponseInterface.isError(response.body().getError());
                    //  }
                }
                //         closeDialog();
            }

            @Override
            public void onFailure(Call<ProfileDetailsResponse> call, Throwable t) {
                //        closeDialog();
                //   Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getUserLanguage() {
        Call<LanguageResponce> call = apiService.getLanguageData(authToken, "application/json");

        call.enqueue(new Callback<LanguageResponce>() {
            @Override
            public void onResponse(Call<LanguageResponce> call, Response<LanguageResponce> response) {
                Log.e("Language", new Gson().toJson(response.body()));
                // Log.e("Auth", new SessionManager(mContext).getUserToken());

                try {
                    if (!response.body().getSuccess()) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.LAN_DATA);
                    }
                    if (response.body().getSuccess()) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.LAN_DATA);
                    } else {
                        mApiResponseInterface.isError(response.body().getError());
                    }
                } catch (Exception e) {
                    try {
                        mApiResponseInterface.isError(response.body().getError());
                    } catch (Exception ex) {

                    }
                }
            }

            @Override
            public void onFailure(Call<LanguageResponce> call, Throwable t) {
                //     Log.e("LanguageError", t.getMessage());
                mApiResponseInterface.isError(t.getMessage());
            }
        });
    }

    public void hostSendGiftRequest(RequestGiftRequest requestGiftRequest) {

        Call<RequestGiftResponce> call = apiService.sendGiftRequestFromHost(authToken, requestGiftRequest);
        //      Log.e("hostSendGiftRequest", authToken + new Gson().toJson(requestGiftRequest));
        call.enqueue(new Callback<RequestGiftResponce>() {
            @Override
            public void onResponse(Call<RequestGiftResponce> call, Response<RequestGiftResponce> response) {
                // Log.e("hostSendGiftRequest", new Gson().toJson(response.body()));
                Toast.makeText(mContext, "Gift request send.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<RequestGiftResponce> call, Throwable t) {
                //     Log.e("SendGiftError", t.getMessage());
                //    mApiResponseInterface.isError("Network Error");
            }
        });

    }

    public void getUserLogout() {
        Call<LogoutResponce> call = apiService.logout(authToken, "application/json");
        //      Log.e("logoutReq", authToken + "application/json");

        call.enqueue(new Callback<LogoutResponce>() {
            @Override
            public void onResponse(Call<LogoutResponce> call, Response<LogoutResponce> response) {
                Log.e("logout", new Gson().toJson(response.body()));
            }

            @Override
            public void onFailure(Call<LogoutResponce> call, Throwable t) {
                //            Log.e("logoutError", t.getMessage());
            }
        });
    }

    public void registerFcmToken(String userToken, String token, String AppVersion) {
        //  showDialog();
        Call<FcmTokenResponse> call = null;

        String role = new SessionManager(mContext).getRole();
        // Log.e("roleLog", role);

        //send app version from here

        if (role.equals("5")) {
            call = apiService.registerFcmToken(userToken, "application/json", authToken, AppVersion);
        } else {
            call = apiService.registerFcmToken(userToken, "application/json", token, AppVersion);
        }

        call.enqueue(new Callback<FcmTokenResponse>() {
            @Override
            public void onResponse(Call<FcmTokenResponse> call, Response<FcmTokenResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("registerFcmToken", "onResponse: " + new Gson().toJson(response.body()));

                    if (!response.body().getResult().isEmpty()) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.REGISTER_FCM_TOKEN);


                    }
                }
                //        closeDialog();
            }

            @Override
            public void onFailure(Call<FcmTokenResponse> call, Throwable t) {
                //      closeDialog();
                //   Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void sendVideo(MultipartBody.Part part) {
        Log.e("vdoResponce", "sendVideo: apiManager ");
        showDialog();
        Call<VideoResponce> call;
        call = apiService.sendVideo(authToken, "application/json", part);
        call.enqueue(new Callback<VideoResponce>() {
            @Override
            public void onResponse(Call<VideoResponce> call, Response<VideoResponce> response) {
                closeDialog();
                Log.e("vdoResponce", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getSuccess()) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.VIDEO_STATUS_UPLOAD);
                    }
                }
            }

            @Override
            public void onFailure(Call<VideoResponce> call, Throwable t) {
                Log.e("vdoERROR", t.getMessage());
                closeDialog();
                if (t.getMessage().equals("timeout")) {
                    mApiResponseInterface.isError("OnFailure_timeout_CloseActivity");
                }

                //Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();

            }
        });
    }

    public void getAccountDelete() {
        Call<Object> call = apiService.getAccountDelete(authToken, "application/json");
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.e("getAccountDelete", new Gson().toJson(response.body()));
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e("getAccountDeleteError", t.getMessage());
            }
        });
    }

    public void getAgencyInfo(String token, String agency_id) {

        Call<AgencyResponse> call = null;
        String role = new SessionManager(mContext).getRole();
        Log.e("roleLog", role);

        if (role.equals("5")) {
            call = apiService.getAgencyInfo(authToken, "application/json", agency_id);
        } else {
            call = apiService.getAgencyInfo(token, "application/json", agency_id);
        }

        Log.e("agencyInfoReq", call.request().toString());
        call.enqueue(new Callback<AgencyResponse>() {
            @Override
            public void onResponse(Call<AgencyResponse> call, Response<AgencyResponse> response) {
                assert response.body() != null;
                Log.e("angency__", "success " + new Gson().toJson(response.body()));
                if (response.body().getSuccess()) {
                    mApiResponseInterface.isSuccess(response.body(), Constant.CHECK_AGENCY);
                } else if (!response.body().getSuccess()) {
                    //if (response.body().getResult().equals("Adult image found")) {
                    mApiResponseInterface.isError("adult_content");
                   /* } else {
                        //   mApiResponseInterface.isError(response.body().getError());
                    }*/
                } else {
                    //   mApiResponseInterface.isError(response.body().getError());
                }
            }

            @Override
            public void onFailure(Call<AgencyResponse> call, Throwable t) {
                Log.e("angency__", "error " + t.getMessage());
                Toast.makeText(mContext, "Invalid Agency", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateProfileDetails(String token, String agency_id, String mobile, String android_id, String name, String age, String city, String language, MultipartBody.Part profile) {
        // Log.e("authToken",authToken);
        showDialog();
        RequestBody agency = RequestBody.create(MediaType.parse("text/plain"),
                agency_id);
        RequestBody requestName = RequestBody.create(MediaType.parse("text/plain"),
                name);
        RequestBody requestDob = RequestBody.create(MediaType.parse("text/plain"),
                age);
        RequestBody requestCity = RequestBody.create(MediaType.parse("text/plain"),
                city);
        RequestBody requestLanguage = RequestBody.create(MediaType.parse("text/plain"),
                language);

        Call<SubmitResponse> call = null;

        String role = new SessionManager(mContext).getRole();
        Log.e("roleLog", role);

        if (role.equals("5")) {
            call = apiService.updateProfileDetails(token, "application/json", agency, requestName, requestDob, requestCity, requestLanguage, profile);
        } else {
            call = apiService.updateProfileDetails(token, "application/json", agency, requestName, requestDob, requestCity, requestLanguage, profile);
        }

        call.enqueue(new Callback<SubmitResponse>() {
            @Override
            public void onResponse(Call<SubmitResponse> call, Response<SubmitResponse> response) {
                Log.e("updateProfile", new Gson().toJson(response.body()));
                closeDialog();
                if (response.isSuccessful() && response.body() != null) {
                    mApiResponseInterface.isSuccess(response.body(), Constant.UPDATE_PROFILE);

                    /*
                     if (response.body().getSuccess()) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.UPDATE_PROFILE);
                    } else if (!response.body().getSuccess()) {
                        if (response.body().getResult().equals("Adult image found")) {
                            mApiResponseInterface.isError("adult_content");
                        } else {
                            //   mApiResponseInterface.isError(response.body().getError());
                        }
                    } else {
                        //   mApiResponseInterface.isError(response.body().getError());
                    }
                    */

                }
                //closeDialog();
            }

            @Override
            public void onFailure(Call<SubmitResponse> call, Throwable t) {
                Log.e("profileError", t.getMessage());
                closeDialog();
                //   Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getLevelData() {
        //showDialog();
        Call<LevelDataResponce> call = apiService.getLevelData(authToken, "application/json");
        call.enqueue(new Callback<LevelDataResponce>() {
            @Override
            public void onResponse(Call<LevelDataResponce> call, Response<LevelDataResponce> response) {

                try {
                    if (response.body().getSuccess()) {
                        Log.e("getLevelData", new Gson().toJson(response.body()));
                        mApiResponseInterface.isSuccess(response.body(), Constant.GET_LEVEL_DATA);
                    }
                } catch (Exception e) {
                    Toast.makeText(mContext, "No Data Available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LevelDataResponce> call, Throwable t) {
                Log.e("getLevelData", "fail " + t.getMessage());
            }
        });
    }

    public void redeemCoins(String points) {
        //showDialog();
        Call<WalletRechargeResponse> call = apiService.redeemCoins(authToken, "application/json", points);
        Log.e("redeemCoinLog", call.request().toString());
        call.enqueue(new Callback<WalletRechargeResponse>() {
            @Override
            public void onResponse(Call<WalletRechargeResponse> call, Response<WalletRechargeResponse> response) {
                Log.e("redeemCoinLog", "responce " + response.body());

                if (response.isSuccessful() && response.body() != null) {

                    if (response.body().isSuccess()) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.REDEEM_EARNING);

                    } else {
                        mApiResponseInterface.isError(response.body().getError());
                    }
                }
                //closeDialog();
            }

            @Override
            public void onFailure(Call<WalletRechargeResponse> call, Throwable t) {
                Log.e("redeemCoinLog", "request " + t.getMessage());

                //   closeDialog();
                //   Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getWalletHistroyFilter(String filterData) {
        //showDialog();
        Call<WalletfilterResponce> call = apiService.getWalletHistoryFilter(authToken, "application/json", filterData);
        call.enqueue(new Callback<WalletfilterResponce>() {
            @Override
            public void onResponse(Call<WalletfilterResponce> call, Response<WalletfilterResponce> response) {
                //  Log.e("walletFilter", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {
                    mApiResponseInterface.isSuccess(response.body(), Constant.FILTER_DATA);
                }
                // closeDialog();
            }

            @Override
            public void onFailure(Call<WalletfilterResponce> call, Throwable t) {
                //   Log.e("walletFilter", t.getMessage());
                //   closeDialog();
                //   Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getWalletAmount() {
        //  showDialog();
        Call<WalletBalResponse> call = apiService.getWalletBalance(authToken, "application/json");
        call.enqueue(new Callback<WalletBalResponse>() {
            @Override
            public void onResponse(Call<WalletBalResponse> call, Response<WalletBalResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("wallHisBalence", new Gson().toJson(response.body()));
                    mApiResponseInterface.isSuccess(response.body(), Constant.WALLET_AMOUNT);
                }
                //     closeDialog();
            }

            @Override
            public void onFailure(Call<WalletBalResponse> call, Throwable t) {
                //       closeDialog();
                //    Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getWalletHistoryFemaleNew() {
        //showDialog();
        //WallateResponceFemale
        Call<IncomeReportFemale> call = apiService.getWalletHistoryFemaleNew(authToken, "application/json");
        call.enqueue(new Callback<IncomeReportFemale>() {
            @Override
            public void onResponse(Call<IncomeReportFemale> call, Response<IncomeReportFemale> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("wallHisResponceData", new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.TRANSACTION_HISTORY_NEW);
                    }

                } else {
                    mApiResponseInterface.isError(response.raw().message());
                }
                //closeDialog();
            }

            @Override
            public void onFailure(Call<IncomeReportFemale> call, Throwable t) {
                Log.e("wallHisResponceNew", t.getMessage());
                //   closeDialog();
                //   Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getTransactionHistoryFemale(int page) {
        //showDialog();
        Call<WalletResponce> call = apiService.getWalletHistoryFemale(authToken, "application/json", page);
        call.enqueue(new Callback<WalletResponce>() {
            @Override
            public void onResponse(Call<WalletResponce> call, Response<WalletResponce> response) {
                // Log.e("wallHisResponce", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {
                    mApiResponseInterface.isSuccess(response.body(), Constant.TRANSACTION_HISTORY);
                }
                //closeDialog();
            }

            @Override
            public void onFailure(Call<WalletResponce> call, Throwable t) {
                //  Log.e("wallHisResponce", t.getMessage());
                //   closeDialog();
                //    Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getUserList(String pageNumber, String search) {
        //showDialog();
        Call<com.klive.app.model.UserListResponse> call = apiService.getUserList(authToken, "application/json", search, pageNumber, "16", String.valueOf(new SessionManager(mContext).gettLangState()));

        // Log.e("lanID", String.valueOf(new SessionManager(mContext).gettLangState()));

        call.enqueue(new Callback<com.klive.app.model.UserListResponse>() {
            @Override
            public void onResponse(Call<com.klive.app.model.UserListResponse> call, Response<com.klive.app.model.UserListResponse> response) {
                Log.e("userList", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getResult().getData() != null) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.USER_LIST);
                    }
                }
                // closeDialog();
            }

            @Override
            public void onFailure(Call<com.klive.app.model.UserListResponse> call, Throwable t) {
                //     closeDialog();
                //      Log.e("userListErr", t.getMessage());
                //   Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getUserListNew(String pageNumber, String search) {
        //showDialog();
        Call<UserListResponseMet> call = apiService.getUserListNew(authToken, "application/json", search, pageNumber, "16", String.valueOf(new SessionManager(mContext).gettLangState()));

        // Log.e("lanID", String.valueOf(new SessionManager(mContext).gettLangState()));

        call.enqueue(new Callback<UserListResponseMet>() {
            @Override
            public void onResponse(Call<UserListResponseMet> call, Response<UserListResponseMet> response) {
                Log.e("userList", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getResult().getData() != null) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.USER_LIST);
                    }
                }
                // closeDialog();
            }

            @Override
            public void onFailure(Call<UserListResponseMet> call, Throwable t) {
                //     closeDialog();
                //      Log.e("userListErr", t.getMessage());
                //   Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    //Agency & subagency


    public void getUserLists(String pageNumber, String search) {
        //showDialog();
        Call<UserListResponse> call = apiService.getUserList(authToken, "application/json", pageNumber);
        call.enqueue(new Callback<UserListResponse>() {
            @Override
            public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response) {
                Log.e("userList", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getResult().getData() != null) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.USER_LIST);
                    }
                }
                // closeDialog();
            }

            @Override
            public void onFailure(Call<UserListResponse> call, Throwable t) {
                //closeDialog();
                //Log.e("userListErr", t.getMessage());
                //Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void searchaUserLists(String search) {
        Call<UserListResponse> call = apiService.searchUserList(authToken, "application/json", search);
        call.enqueue(new Callback<UserListResponse>() {
            @Override
            public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response) {
                Log.e("userList", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getResult().getData() != null) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.USER_LIST);
                    }
                }
                // closeDialog();
            }

            @Override
            public void onFailure(Call<UserListResponse> call, Throwable t) {
                //closeDialog();
                //Log.e("userListErr", t.getMessage());
                //Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getHostListNextPage(String pageNumber/*,String search*/) {
        //  showDialog();
        Call<UserListResponse> call = apiService.getUserList(authToken, "application/json", pageNumber/*,  search*/);
       /*
        Log.e("authToken", authToken);
        Log.e("pageNumber", pageNumber);
        Log.e("lanID", String.valueOf(new SessionManager(mContext).gettLangState()));*/
        call.enqueue(new Callback<UserListResponse>() {
            @Override
            public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response) {
                //Log.e("userListNXT", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {

                    if (response.body().getResult().getData() != null) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.HOST_LIST_NEXT_PAGE);
                    }
                }
                // closeDialog();
            }

            @Override
            public void onFailure(Call<UserListResponse> call, Throwable t) {
                //closeDialog();
                // Log.e("userListErrNXT", t.getMessage());
                Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }


    public void getAgencyList() {
        Call<AgencyPolicyResponse> call = apiService.getAgencyList(authToken, "application/json");
        call.enqueue(new Callback<AgencyPolicyResponse>() {
            @Override
            public void onResponse(Call<AgencyPolicyResponse> call, Response<AgencyPolicyResponse> response) {
                Log.e("Agencyrequest", call.request().toString());
                Log.e("AgencyList", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getResult() != null) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.GET_AGENCY_LIST);
                    }
                }

            }

            @Override
            public void onFailure(Call<AgencyPolicyResponse> call, Throwable t) {
                //Log.e("userListErr", t.getMessage());
                //Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getSubAgencyList(String search) {
        Call<SubAgencyResponse> call = apiService.getSubAgencyList(authToken, "application/json", search);
        call.enqueue(new Callback<SubAgencyResponse>() {
            @Override
            public void onResponse(Call<SubAgencyResponse> call, Response<SubAgencyResponse> response) {
                Log.e("Agencyrequest", call.request().toString());
                Log.e("AgencyList", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getResult() != null) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.SUB_AGENCY_LIST);
                    }
                }

            }

            @Override
            public void onFailure(Call<SubAgencyResponse> call, Throwable t) {
                //Log.e("userListErr", t.getMessage());
                //Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }


    public void getAgencyDateList() {
        Call<AgencyCenterDateResponse> call = apiService.getAgencyDateList(authToken, "application/json");
        call.enqueue(new Callback<AgencyCenterDateResponse>() {
            @Override
            public void onResponse(Call<AgencyCenterDateResponse> call, Response<AgencyCenterDateResponse> response) {
                Log.e("AgencyCenterrequest", call.request().toString());
                Log.e("AgencyCenterList", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getResult() != null) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.GET_AGENCY_DATE_LIST);
                    }
                }

            }

            @Override
            public void onFailure(Call<AgencyCenterDateResponse> call, Throwable t) {
                //Log.e("userListErr", t.getMessage());
                //Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getAgencyWeeklyData(String month) {
        Call<AgencyHostWeeklyResponse> call = apiService.getAgencyWeeklyData(authToken, "application/json", month);
        call.enqueue(new Callback<AgencyHostWeeklyResponse>() {
            @Override
            public void onResponse(Call<AgencyHostWeeklyResponse> call, Response<AgencyHostWeeklyResponse> response) {
                Log.e("AgencyWeeklyrequestdta", call.request().toString());
                Log.e("AgencyWeeeklyListdta", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {
                    mApiResponseInterface.isSuccess(response.body(), Constant.GET_AGENCY_HOST_WEEKLY_DATA);
                }
            }

            @Override
            public void onFailure(Call<AgencyHostWeeklyResponse> call, Throwable t) {
                //   Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }


    public void getSettlementDateList() {
        Call<HostSettlementDateResponse> call = apiService.getSettlementDateList(authToken, "application/json");
        call.enqueue(new Callback<HostSettlementDateResponse>() {
            @Override
            public void onResponse(Call<HostSettlementDateResponse> call, Response<HostSettlementDateResponse> response) {
                Log.e("Settlementrequest", call.request().toString());
                Log.e("SettlementList", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getResult() != null) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.GET_SETTLEMENT_DATE_LIST);
                    }
                }

            }

            @Override
            public void onFailure(Call<HostSettlementDateResponse> call, Throwable t) {
                //Log.e("userListErr", t.getMessage());
                //Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getSettlementWeeklyData(String month) {
        Call<SettlementHostWeeklyResponse> call = apiService.getSettlementWeeklyData(authToken, "application/json", month);
        call.enqueue(new Callback<SettlementHostWeeklyResponse>() {
            @Override
            public void onResponse(Call<SettlementHostWeeklyResponse> call, Response<SettlementHostWeeklyResponse> response) {
                Log.e("AgencyWeeklyrequestdta", call.request().toString());
                Log.e("AgencyWeeeklyListdta", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {
                    mApiResponseInterface.isSuccess(response.body(), Constant.GET_SETTLEMENT_HOST_WEEKLY_DATA);
                }
            }

            @Override
            public void onFailure(Call<SettlementHostWeeklyResponse> call, Throwable t) {
                //   Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }


    public void getAddAccountDetail() {
        Call<AddAccountResponse> call = apiService.getAccountDetail(authToken, "application/json");
        call.enqueue(new Callback<AddAccountResponse>() {
            @Override
            public void onResponse(Call<AddAccountResponse> call, Response<AddAccountResponse> response) {
                Log.e("accountrequest", call.request().toString());
                Log.e("Accountdata", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getResult() != null) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.GET_ADD_ACCOUNT_DETAIL);
                    }
                }
            }

            @Override
            public void onFailure(Call<AddAccountResponse> call, Throwable t) {
                Log.e("accountDataErr", t.getMessage());
                //Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void upDateAccount(String user_name, String bank_name, String ifsc_code, String account_number, String confirm_account_number, String address, String email, String phone, String upi_id, String tppe) {
        Call<UdateAccountResponse> call = apiService.updateAccountDetail(authToken, "application/json", account_number, confirm_account_number, bank_name, ifsc_code, user_name, address, email, phone, upi_id, tppe);
        call.enqueue(new Callback<UdateAccountResponse>() {
            @Override
            public void onResponse(Call<UdateAccountResponse> call, Response<UdateAccountResponse> response) {
                Log.e("Updaterequestdta", call.request().toString());
                Log.e("Updatetdta", new Gson().toJson(response.body()));

                if (response.isSuccessful() && response.body() != null) {
                    mApiResponseInterface.isSuccess(response.body(), Constant.UPDATE_ACCOUNT);
                }
            }

            @Override
            public void onFailure(Call<UdateAccountResponse> call, Throwable t) {
                //   Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }


    public void upDateAccountNew(String user_name, String bank_name, String ifsc_code, String account_number, String confirm_account_number, String address, String email, String phone, String upi_id, String type) {
        Call<AccountResponse> call = apiService.updateAccountDetailNew(authToken, "application/json", account_number, bank_name, ifsc_code, user_name, address, email, phone, upi_id, type);
        call.enqueue(new Callback<AccountResponse>() {
            @Override
            public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
                Log.e("Updaterequestdta", call.request().toString());
                Log.e("Updatetdata0", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {
                    mApiResponseInterface.isSuccess(response.body(), Constant.UPDATE_ACCOUNT);
                }
            }

            @Override
            public void onFailure(Call<AccountResponse> call, Throwable t) {
                //   Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getPaymentRequestDetail() {
        Call<PaymentRequestResponce> call = apiService.getPaymentRequestDetail(authToken, "application/json");
        call.enqueue(new Callback<PaymentRequestResponce>() {
            @Override
            public void onResponse(Call<PaymentRequestResponce> call, Response<PaymentRequestResponce> response) {
                Log.e("getPaymentRequestDetail", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {
                    mApiResponseInterface.isSuccess(response.body(), Constant.GET_PAYMENT_REQUEST_DETAIL);
                }
            }

            @Override
            public void onFailure(Call<PaymentRequestResponce> call, Throwable t) {
                //   Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getBankListDetail() {
        Call<BankListResponce> call = apiService.getBankListDetail(authToken, "application/json");
        call.enqueue(new Callback<BankListResponce>() {
            @Override
            public void onResponse(Call<BankListResponce> call, Response<BankListResponce> response) {
                Log.e("getBankListDetail", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {
                    mApiResponseInterface.isSuccess(response.body(), Constant.GET_BANK_LIST_DETAIL);
                }
            }

            @Override
            public void onFailure(Call<BankListResponce> call, Throwable t) {
                //   Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }


    public void getCallPriceList() {
        Call<PriceListResponse> call = apiService.getCallPriceList(authToken);
        call.enqueue(new Callback<PriceListResponse>() {
            @Override
            public void onResponse(Call<PriceListResponse> call, Response<PriceListResponse> response) {

                Log.e("getCallPriceList", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {
                    mApiResponseInterface.isSuccess(response.body(), Constant.CALL_PRICE_LIST);
                }


            }

            @Override
            public void onFailure(Call<PriceListResponse> call, Throwable t) {

            }
        });


    }


    public void updateCallPrice(priceupdateModel price) {

        Call<CallPriceUpdateResponse> call = apiService.updateCallPrice(authToken, price);

        call.enqueue(new Callback<CallPriceUpdateResponse>() {
            @Override
            public void onResponse(Call<CallPriceUpdateResponse> call, Response<CallPriceUpdateResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    mApiResponseInterface.isSuccess(response.body(), Constant.UPDATE_CALL_PRICE);
                }

            }

            @Override
            public void onFailure(Call<CallPriceUpdateResponse> call, Throwable t) {

            }
        });


    }


    public void getVideoStatus() {
        Call<VideoStatusResponseModel> call = apiService.getUserVideoStatus(authToken);

        call.enqueue(new Callback<VideoStatusResponseModel>() {
            @Override
            public void onResponse(Call<VideoStatusResponseModel> call, Response<VideoStatusResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mApiResponseInterface.isSuccess(response.body(), Constant.VIDEO_STATUS);
                }
            }

            @Override
            public void onFailure(Call<VideoStatusResponseModel> call, Throwable t) {

            }
        });


    }


    public void getZegoNewToken(String roomid) {
        Call<NewZegoTokenResponse> call = apiService.getnewZegoToken(authToken, roomid);

        call.enqueue(new Callback<NewZegoTokenResponse>() {
            @Override
            public void onResponse(Call<NewZegoTokenResponse> call, Response<NewZegoTokenResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mApiResponseInterface.isSuccess(response.body(), Constant.GENERATE_ZEGO_NEW_TOKEN);
                    Log.e("NewZegoTokenResponse", new Gson().toJson(response.body()));
                }
            }

            @Override
            public void onFailure(Call<NewZegoTokenResponse> call, Throwable t) {

            }
        });
    }

    public void getTradingAccount() {
        Call<TradingAccountResponse> call = apiService.getTradingAccount(authToken);

        call.enqueue(new Callback<TradingAccountResponse>() {
            @Override
            public void onResponse(Call<TradingAccountResponse> call, Response<TradingAccountResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mApiResponseInterface.isSuccess(response.body(), Constant.GET_TRADING_ACCOUNT);
                    Log.e("TradingAccountResponse", new Gson().toJson(response.body()));
                }
            }

            @Override
            public void onFailure(Call<TradingAccountResponse> call, Throwable t) {

            }
        });

    }


    public void getTradingUserName(UserIdBodyModel userId) {

        Log.e("UserIdBodyModel", userId.getUserId());

        Call<GetTradingUserNameResponse> call = apiService.getTradingUserName(authToken, userId);

        call.enqueue(new Callback<GetTradingUserNameResponse>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<GetTradingUserNameResponse> call, Response<GetTradingUserNameResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mApiResponseInterface.isSuccess(response.body(), Constant.GET_TRADING_USERNAME);

                    Log.e("GetTradingUserNameResponse", new Gson().toJson(response.body()));
                } else {
                    Log.e("GetTradingUserNameResponse", "no response");

                    Toast.makeText(mContext, "Receiver Trade Account does not exits.", Toast.LENGTH_SHORT).show();
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<GetTradingUserNameResponse> call, Throwable t) {

                Log.e("GetTradingUserNameResponse", "failure" + t.getMessage());

                Toast.makeText(mContext, "Invalid User Id", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void sendtransferTradeAccount(TradingTransferModel tradingTransferModel) {

        Log.e("UserIdBodyModel", tradingTransferModel.getUserId());

        Call<TransferTradeAccountResponse> call = apiService.sendtransferTradeAccount(authToken, tradingTransferModel);

        call.enqueue(new Callback<TransferTradeAccountResponse>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<TransferTradeAccountResponse> call, Response<TransferTradeAccountResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mApiResponseInterface.isSuccess(response.body(), Constant.TRANSFER_TRADE_ACCOUNT);
                    Log.e("TransferTradeAccountResponse", new Gson().toJson(response.body()));
                } else {
                    Log.e("TransferTradeAccountResponse", "no response");
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<TransferTradeAccountResponse> call, Throwable t) {

                Log.e("TransferTradeAccountResponse", "failure" + t.getMessage());


            }
        });


    }


    @SuppressLint("LongLogTag")
    public void getWalletHistoryTradeAccount(String page_no, String page_per_record) {

        Log.e("getWalletHistoryTradeAccount", "called");
        Call<TradingHistoryResponse> call = apiService.getWalletHistoryTradeAccount(authToken, page_no, page_per_record);

        call.enqueue(new Callback<TradingHistoryResponse>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<TradingHistoryResponse> call, Response<TradingHistoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mApiResponseInterface.isSuccess(response.body(), Constant.WALLET_HISTORY_TRADE_ACCOUNT);
                    Log.e("TradingHistoryResponse", new Gson().toJson(response.body()));
                } else {
                    Log.e("TradingHistoryResponse", "no response");
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<TradingHistoryResponse> call, Throwable t) {

                Log.e("TradingHistoryResponse", "fail " + t.getMessage());

            }
        });


    }

    @SuppressLint("LongLogTag")
    public void updateTransferDetail(String wallet_id, String type) {
        Call<UpdateTransferDetailResponse> call = apiService.updateTransferDetail(authToken, new UpdateHistoryModel(wallet_id, type));

        call.enqueue(new Callback<UpdateTransferDetailResponse>() {

            @Override
            public void onResponse(Call<UpdateTransferDetailResponse> call, Response<UpdateTransferDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mApiResponseInterface.isSuccess(response.body(), Constant.UPDATE_TRANSFER_DETAIL);
                    Log.e("UpdateTransferDetailResponse", new Gson().toJson(response.body()));
                } else {

                }

            }

            @Override
            public void onFailure(Call<UpdateTransferDetailResponse> call, Throwable t) {

                Log.e("UpdateTransferDetailResponse", "Fail " + t.getMessage());

            }
        });
    }


    public void checkFemaleVarification() {
        Call<CheckFemaleVarifyResponse> call = apiService.checkFemaleVarify(authToken);

        call.enqueue(new Callback<CheckFemaleVarifyResponse>() {
            @Override
            public void onResponse(Call<CheckFemaleVarifyResponse> call, Response<CheckFemaleVarifyResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    if (response.body().getSuccess()) {
                        Log.e("CheckFemaleVarify", "onResponse: CheckFemaleVarifyResponse  " + new Gson().toJson(response.body()));
                        mApiResponseInterface.isSuccess(response.body(), Constant.CHECK_FEMALE_VARIFY);

                    } else {

                        Log.e("CheckFemaleVarify", "onResponse: CheckFemaleVarifyResponse  not success " + new Gson().toJson(response.body()));
                    }


                } else {


                }

            }

            @Override
            public void onFailure(Call<CheckFemaleVarifyResponse> call, Throwable t) {
                Log.e("CheckFemaleVarify", "onFailure: " + t.getMessage());
            }
        });


    }


    public void sendCallRecord(CallRecordBody callRecordBody) {
        showDialog();
        Log.e("callStartFun", new Gson().toJson(callRecordBody));
        Call<Object> call = apiService.sendCallRecord(authToken, "application/json", callRecordBody);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.e("callStart", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {

                    //  if (response.body().isSuccess()) {
                    mApiResponseInterface.isSuccess(response.body(), Constant.SEND_CALL_RECORD);

                    //   } else {
                    //    mApiResponseInterface.isError(response.body().getError());
                    //  }
                }
                closeDialog();
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                closeDialog();
                //    Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }


    public void endCall(CallRecordBody callRecordBody) {
        Log.e("callEndFun", new Gson().toJson(callRecordBody));
        //showDialog();
        Call<Object> call = apiService.sendCallRecord(authToken, "application/json", callRecordBody);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.e("callEnd_api", new Gson().toJson(response.body()));

                //   Log.e("callEndArrayData", new Gson().toJson(new SessionManager(mContext).getUserEndcalldata()));

                if (response.isSuccessful() && response.body() != null) {
                    new SessionManager(mContext).setUserGetendcalldata("success");
                    mApiResponseInterface.isSuccess(response.body(), Constant.END_CALL);
                } else {
                    new SessionManager(mContext).setUserGetendcalldata("error");
                }
                //closeDialog();
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                //closeDialog();
                //  Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
                //   Log.e("callEndError", t.getMessage());
                new SessionManager(mContext).setUserGetendcalldata("error");
            }
        });
    }


    @SuppressLint("LongLogTag")
    public void sendChatNotification(String fcmToken, String profileId, String message, String profileName, String profileImage, String type) {
        Data data = new Data("klive_notification", profileId, message, profileName, profileImage, type);
        Sender sender = new Sender(data, fcmToken);
        Log.e("offLineDataLog_inApiManager", new Gson().toJson(sender));
        ApiInterface apiService = FirebaseApiClient.getClient().create(ApiInterface.class);
        apiService.sendNotificationInBox(sender)
                .enqueue(new Callback<MyResponse>() {
                    @Override
                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                        Log.e("offLineDataLog_inApiManager", "response   " + new Gson().toJson(response.body()) + "   " + response.message());
                    }

                    @Override
                    public void onFailure(Call<MyResponse> call, Throwable t) {
                        Log.e("offLineDataLog_inApiManager", "failure  " + t.getMessage());
                    }
                });
    }


    public void getWeeklyRewardList() {
        //  showDialog();
        //  Log.e("callStartFun", new Gson().toJson(callRecordBody));
        Call<WeeklyRewardResponse> call = apiService.getWeeklyRewardList(authToken);

        call.enqueue(new Callback<WeeklyRewardResponse>() {
            @Override
            public void onResponse(Call<WeeklyRewardResponse> call, Response<WeeklyRewardResponse> response) {

                Log.e("WeeklyReward", "onResponse: getWeeklyRewardList " + new Gson().toJson(response.body()));

                if (response.isSuccessful() && response.body() != null) {

                    if (response.body().isSuccess()) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.WEEKLY_REWARD);
                    } else {
                        //  mApiResponseInterface.isSuccess(response.body(), Constant.WEEKLY_REWARD);
                    }


                }


            }

            @Override
            public void onFailure(Call<WeeklyRewardResponse> call, Throwable t) {

            }
        });


    }


    public void checkTemporaryBlock() {

        Call<TemporaryBlockResponse> call = apiService.checktemporaryblockuser(authToken);


        call.enqueue(new Callback<TemporaryBlockResponse>() {
            @Override
            public void onResponse(Call<TemporaryBlockResponse> call, Response<TemporaryBlockResponse> response) {

                Log.e("CheckTemporaryBlock", "onResponse: " + new Gson().toJson(response.body()));
                mApiResponseInterface.isSuccess(response.body(), Constant.CHECK_TEMPORARY_BLOCK);

            }

            @Override
            public void onFailure(Call<TemporaryBlockResponse> call, Throwable t) {
                Log.e("CheckTemporaryBlock", "onFailure: msg " + t.getMessage());
            }
        });


    }

    public void getProfileIdData(String profile_id) {
        Call<DataFromProfileIdResponse> call = apiService.getProfileIdData(authToken, profile_id);

        call.enqueue(new Callback<DataFromProfileIdResponse>() {
            @Override
            public void onResponse(Call<DataFromProfileIdResponse> call, Response<DataFromProfileIdResponse> response) {
                Log.e("getProfileIdData", new Gson().toJson(response.body()));

                if (response.body().getSuccess()) {
                    mApiResponseInterface.isSuccess(response.body(), Constant.GET_DATA_FROM_PROFILE_ID);
                } else {
                    mApiResponseInterface.isError(response.body().getError());
                }

            }

            @Override
            public void onFailure(Call<DataFromProfileIdResponse> call, Throwable t) {
                Log.e("getProfileIdData", "Exception " + t.getMessage());
            }
        });


    }


    public void reportUser(String id, String isBlock, String des, String inputDes) {
        showDialog();
        Call<ReportResponse> call = apiService.reportUser(authToken, "application/json", id, isBlock, des, inputDes);
        call.enqueue(new Callback<ReportResponse>() {
            @Override
            public void onResponse(Call<ReportResponse> call, Response<ReportResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    if (response.body().isSuccess()) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.REPORT_USER);

                    } else {
                        mApiResponseInterface.isError(response.body().getError());
                    }
                }
                closeDialog();
            }

            @Override
            public void onFailure(Call<ReportResponse> call, Throwable t) {
                closeDialog();
                //   Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }


    public void getVideoForProfile(String profileId) {

        Call<VideoPlayResponce> call = apiService.getVideoplay(authToken, "application/json", profileId);
        //  Log.e("sendGiftReq", authToken + new Gson().toJson(sendGiftRequest));
        call.enqueue(new Callback<VideoPlayResponce>() {
            @Override
            public void onResponse(Call<VideoPlayResponce> call, Response<VideoPlayResponce> response) {
                // Log.e("getVideoForProfile", new Gson().toJson(response.body()));

                try {
                    if (response.body().getSuccess()) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.PLAY_VIDEO);
                    }
                } catch (Exception e) {
                    //  mApiResponseInterface.isError("Network Error");
                }
            }

            @Override
            public void onFailure(Call<VideoPlayResponce> call, Throwable t) {
                //Log.e("getVideoForProfileError", t.getMessage());
                //mApiResponseInterface.isError("Network Error");
            }
        });
    }

    public void getRateCountForHost(String id) {
        //showDialog();
        // Call<RatingDataResponce> call = apiService.getRateCountForHost(authToken, "application/json", "397445");
        Call<UserListResponseNewData> call = apiService.getRateCountForHost(authToken, "application/json", id);
        call.enqueue(new Callback<UserListResponseNewData>() {
            @Override
            public void onResponse(Call<UserListResponseNewData> call, Response<UserListResponseNewData> response) {
                try {
                    if (response.body().getSuccess()) {
                        Log.e("getRateCountForHostNew", new Gson().toJson(response.body()));
                        mApiResponseInterface.isSuccess(response.body(), Constant.GET_RATING_COUNT);
                    }
                } catch (Exception e) {
                }

            }

            @Override
            public void onFailure(Call<UserListResponseNewData> call, Throwable t) {
                //Log.e("getrateCountForHostnewError", t.getMessage());
                // closeDialog();
            }
        });
    }


    public void getGiftCountForHost(String id) {
        Log.e("getGiftCountForHostID", "getGiftCountForHost: id " + id);
        // showDialog();
        Log.e("getGiftCountForHostID", id);
        Call<GiftCountResult> call = apiService.getGiftCountForHost(authToken, "application/json", id);

        call.enqueue(new Callback<GiftCountResult>() {
            @Override
            public void onResponse(Call<GiftCountResult> call, Response<GiftCountResult> response) {
                Log.e("getGiftCountForHost", new Gson().toJson(response.body()));
                closeDialog();
                try {
                    if (response.body().getSuccess()) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.GET_GIFT_COUNT);
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<GiftCountResult> call, Throwable t) {
                //Log.e("getGiftCountForHostError", t.getMessage());
                closeDialog();
            }
        });

    }

    public void deleteProfileVideo(String videoId) {
        Log.e("request for delete==", videoId);

        Call<Object> call = apiService.profileVideoDelete(authToken, "application/json", videoId);

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.e("deleteProfileVideo==>", "onResponse===>" + new Gson().toJson(response.body()));
                closeDialog();
                mApiResponseInterface.isSuccess(response.body(), Constant.VIDEO_STATUS_DELETE);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e("deleteProfileVideo==>", "onFailure===>" + t.getMessage());
                closeDialog();
            }
        });

    }

    public void getNewImageUploadStatus() {

        Call<UpdateProfileNewResponse> call = apiService.getProfileImageReview(authToken);

        call.enqueue(new Callback<UpdateProfileNewResponse>() {
            @Override
            public void onResponse(Call<UpdateProfileNewResponse> call, Response<UpdateProfileNewResponse> response) {
                Log.e("NewImageUploadStatus==>", "onResponse===>" + new Gson().toJson(response.body()));
                closeDialog();
                mApiResponseInterface.isSuccess(response.body(), Constant.PROFILE_NEW_IMAGE_STATUS);
            }

            @Override
            public void onFailure(Call<UpdateProfileNewResponse> call, Throwable t) {
                Log.e("NewImageUploadStatus==>", "onFailure===>" + t.getMessage());
                closeDialog();
            }
        });

    }


    public void getCategoryGifts() {

        Call<NewGiftListResponse> call = apiService.getCategoryGifts();

        call.enqueue(new Callback<NewGiftListResponse>() {
            @Override
            public void onResponse(Call<NewGiftListResponse> call, Response<NewGiftListResponse> response) {
                //  Log.e("GETCATEGORYGIFT", "onResponse: " + new Gson().toJson(response.body().getResult().get(1)));
                //  Log.e("GETCATEGORYGIFT11", "onResponse: " + response.body().getResult().get(1).getGifts().size());
                // longLog(new Gson().toJson(response));

                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getSuccess()) {
                        new SessionManager(mContext).setCategoryGiftList(response.body());
                        Log.e("CATEGORY_GIFT_RESP", "onResponse: " + new Gson().toJson(new SessionManager(mContext).getCategoryGiftList()));
                        List<NewGiftResult> giftListNew = response.body().getResult();
                        // creating new list for allGifts
                        List<NewGift> giftsAll = new ArrayList<>();
                        // created new hashmap for allGifts
                        HashMap<Integer, NewGift> giftImgAllList = new HashMap<>();
                        for (int i = 0; i < giftListNew.size(); i++) {
                            giftsAll.addAll(giftListNew.get(i).getGifts());
                        }

                        // using this to iterate all the gifts to save into hashmap
                        for (int i = 0; i < giftsAll.size(); i++) {
                            giftImgAllList.put(giftsAll.get(i).getId(), giftsAll.get(i));
                        }
                        // used to enter all the giftItem data
                        new SessionManager(mContext).setEmployeeAllGiftList(giftImgAllList);

                    } else {
                        //  new SessionManager(mContext).setCategoryGiftList(null);
                    }
                }

            }

            @Override
            public void onFailure(Call<NewGiftListResponse> call, Throwable t) {

            }
        });


    }


    public void getStatusVideosList(String userId) {
        Call<NewVideoStatusResponse> call = apiService.getStatusVideosList(authToken, userId);

        call.enqueue(new Callback<NewVideoStatusResponse>() {
            @Override
            public void onResponse(Call<NewVideoStatusResponse> call, Response<NewVideoStatusResponse> response) {
                Log.e("GET_VIDEO_STATUS_LIST", "onResponse: getStatusVideosList " + new Gson().toJson(response.body()));
                mApiResponseInterface.isSuccess(response.body(), GET_VIDEO_STATUS_LIST);
            }

            @Override
            public void onFailure(Call<NewVideoStatusResponse> call, Throwable t) {
                Log.e("GET_VIDEO_STATUS_LIST", "onFailure: getStatusVideosList Throwable " + t.getMessage());
            }
        });
    }


    public void login_FbGoogle(String name, String login_type, String username, String hash, String deviceId) {
        showDialog();
        Call<LoginResponse> call = apiService.loginFbGoogle(login_type, name, username, hash, deviceId);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                //  Log.e("GoogleSignRequest",call.request().toString());
                Log.e("GoogleSignResponse", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.REGISTER);
                    } else {
                        mApiResponseInterface.isError(response.body().getError());
                    }
                } else if (response.code() == 401) {
                    Gson gson = new GsonBuilder().create();
                    ReportResponse mError;
                    try {
                        mError = gson.fromJson(response.errorBody().string(), ReportResponse.class);
                        Toast.makeText(mContext, mError.getError(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        // handle failure to read error
                    }
                }
                closeDialog();

            }


            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                closeDialog();
                //   Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void guestRegister(String login_type, String device_id, String hash) {
        showDialog();
        Call<LoginResponse> call = apiService.guestRegister(login_type, device_id, hash);
        Log.e("Guestregister", "request => " + call.request());
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Log.e("Guestregister", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.GUEST_REGISTER);
                    }
                } else if (response.code() == 401) {
                    Toast.makeText(mContext, "User already registered. please login", Toast.LENGTH_LONG).show();
                } else if (response.code() == 500) {
                    Toast.makeText(mContext, "Internal Server Error", Toast.LENGTH_LONG).show();
                }
                closeDialog();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("Guestregister", t.getMessage());
                closeDialog();
                //   Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getRechargeListNew() {
        // showDialog();
        Call<RechargePlanResponseNew> call = apiService.getRechargeList(authToken, "application/json");
        call.enqueue(new Callback<RechargePlanResponseNew>() {
            @Override
            public void onResponse(Call<RechargePlanResponseNew> call, Response<RechargePlanResponseNew> response) {
                Log.e("RechargeListData", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {
                    mApiResponseInterface.isSuccess(response.body(), Constant.RECHARGE_LIST);
                    new SessionManager(mContext).setRechargeListResponse(response.body());
                    //   new SessionManager(mContext).getRechargeListResponse();
                }
                // closeDialog();
            }

            @Override
            public void onFailure(Call<RechargePlanResponseNew> call, Throwable t) {
                //   closeDialog();
                Log.e("RechargeListData", "onFailure:  " + t.getMessage());
                //      Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void checkFirstTimeRechargeDone() {
        Call<DiscountedRechargeResponse> call = apiService.checkFirstTimeRecharge(authToken);

        call.enqueue(new Callback<DiscountedRechargeResponse>() {
            @Override
            public void onResponse(Call<DiscountedRechargeResponse> call, Response<DiscountedRechargeResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    Log.e("DiscountedRechargeResponse", "onResponse: " + new Gson().toJson(response.body()));
                    if (response.body().getSuccess()) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.GET_FIRST_TIME_RECHARGE);
                        new SessionManager(mContext).setFirstTimeRecharged(String.valueOf(response.body().getIsRecharge()));
                    } else {
                        mApiResponseInterface.isError(response.body().getError());
                    }
                }

            }

            @Override
            public void onFailure(Call<DiscountedRechargeResponse> call, Throwable t) {
                Log.e("DiscountedRechargeResponse", "onFailure: " + t.getMessage());
            }
        });
    }

    public void getStoreTablist() {
        Call<StoreResponse> call = apiService.getStoreTabList(authToken);
        call.enqueue(new Callback<StoreResponse>() {
            @Override
            public void onResponse(Call<StoreResponse> call, Response<StoreResponse> response) {
                Log.e("GET_STORE_TAB_LIST", "onResponse: " + new Gson().toJson(response.body()));
                mApiResponseInterface.isSuccess(response.body(), GET_STORE_TAB_LIST);
                new SessionManager(mContext).setStoreTabList(response.body());
            }

            @Override
            public void onFailure(Call<StoreResponse> call, Throwable t) {
                Log.e("GET_STORE_TAB_LIST", "onFailure: Throwable " + t.getMessage());
            }
        });
    }

    public void upDateGuestProfile(RequestBody name, MultipartBody.Part part) {
        // Log.e("authToken",authToken);
        Call<Object> call;
        Log.e("currentGuestName", new Gson().toJson(name));
        call = apiService.upDateGuestProfile(authToken, "application/json", name, part);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.e("updateGuestProfile", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {
                    mApiResponseInterface.isSuccess(response.body(), Constant.UPDATE_GUEST_PROFILE);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e("GuestprofileError", t.getMessage());
                //   Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getRemainingGiftCardDisplayFunction() {
        Call<RemainingGiftCardResponce> call = apiService.getRemainingGiftCardResponce(authToken, "application/json");
        call.enqueue(new Callback<RemainingGiftCardResponce>() {
            @Override
            public void onResponse(Call<RemainingGiftCardResponce> call, Response<RemainingGiftCardResponce> response) {
                Log.e("freeGiftCardData", new Gson().toJson(response.body()));
                // if (response.body().getSuccess()) {
                mApiResponseInterface.isSuccess(response.body(), Constant.GET_REMAINING_GIFT_CARD_DISPLAY);
                // }
            }

            @Override
            public void onFailure(Call<RemainingGiftCardResponce> call, Throwable t) {
                Log.e("freeGiftCardError", t.getMessage());
            }
        });
    }

    public void getBanList() {
        Call<BanResponce> call = apiService.getBanData(authToken, "application/json");

        call.enqueue(new Callback<BanResponce>() {
            @Override
            public void onResponse(Call<BanResponce> call, Response<BanResponce> response) {
                //    Log.e("banResponce", new Gson().toJson(response.body()));
                try {
                    if (response.body().isSuccess()) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.BAN_DATAP);
                    } else {
                        mApiResponseInterface.isError(response.body().getError());
                    }
                } catch (Exception e) {
                    mApiResponseInterface.isError("Network Error");
                }
            }

            @Override
            public void onFailure(Call<BanResponce> call, Throwable t) {
                //   Log.e("banError", t.getMessage());
                mApiResponseInterface.isError("Network Error");

                //                mApiResponseInterface.isError("Network Error");
            }
        });
    }

    public void getUserLatLonUpdated(String user_country, String user_city, String lat, String lng) {
        //showDialog();
        Call<Object> call = apiService.getLatLonUpdated(authToken, "application/json", user_country, user_city, lat, lng);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.e("latLonUpdate", new Gson().toJson(response.body()));
                mApiResponseInterface.isSuccess(response.body(), Constant.USER_LOCATION_UPDATED);
                // closeDialog();
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                //closeDialog();
                //Log.e("userListErr", t.getMessage());
                //Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getUserListWithLastCallLatest(String pageNumber, String search) {
        Call<UserListResponseMet> call = apiService.getUserListWithLastCallLatest(authToken, "application/json", search, pageNumber, "16", String.valueOf(new SessionManager(mContext).gettLangState()));
        call.enqueue(new Callback<UserListResponseMet>() {
            @Override
            public void onResponse(Call<UserListResponseMet> call, Response<UserListResponseMet> response) {
                Log.e("getUserListWithLastCallLatest", "onResponse: getUserListWithLastCallLatest:" + new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getResult().getData() != null) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.USER_LIST);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserListResponseMet> call, Throwable t) {

            }
        });
    }


    public void getUserListWithLastCallLatestNextPage(String pageNumber, String search) {
        Call<UserListResponseMet> call = apiService.getUserListWithLastCallLatest(authToken, "application/json", search, pageNumber, "16", String.valueOf(new SessionManager(mContext).gettLangState()));

        call.enqueue(new Callback<UserListResponseMet>() {
            @Override
            public void onResponse(Call<UserListResponseMet> call, Response<UserListResponseMet> response) {
                Log.e("userListNXT", new Gson().toJson(response.body()));

                Log.e("getUserListWithLastCallLatest", "onResponse: getUserListWithLastCallLatestNextPage :  " + new Gson().toJson(response.body()));

                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getResult().getData() != null) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.USER_LIST_NEXT_PAGE);
                    }
                }
                //  closeDialog();
            }

            @Override
            public void onFailure(Call<UserListResponseMet> call, Throwable t) {
                //   closeDialog();
                //        Log.e("userListErrNXT", t.getMessage());
                //   Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getPaymentData() {

        Call<PaymentGatewayResponce> call = apiService.getPaymentData(authToken, "application/json");

       /*  Log.e("authToken", authToken);
        Log.e("payRequestinFUN", new Gson().toJson(payRequest));*/

        call.enqueue(new Callback<PaymentGatewayResponce>() {
            @Override
            public void onResponse(Call<PaymentGatewayResponce> call, Response<PaymentGatewayResponce> response) {
                //  Log.e("getOnePayData", new Gson().toJson(response.body()));

                try {
                    if (response.body().getSuccess()) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.PAY_DETAILS);
                    }
                } catch (Exception e) {
                    //  mApiResponseInterface.isError("Network Error");
                }
            }

            @Override
            public void onFailure(Call<PaymentGatewayResponce> call, Throwable t) {
                //Log.e("getOnePayDataError", t.getMessage());

            }
        });
    }

    public void searchUser(String keyword, String pageNumber) {

     /*   Log.e("keyword", keyword);
        Log.e("pageNumber", pageNumber);
        Log.e("authToken", authToken);*/

        Call<UserListResponseMet> call = apiService.searchUser(authToken, "application/json", keyword, pageNumber, "40");
        Log.e("authhhTok", authToken);
        call.enqueue(new Callback<UserListResponseMet>() {
            @Override
            public void onResponse(Call<UserListResponseMet> call, Response<UserListResponseMet> response) {
                // if (response.isSuccessful() && response.body() != null) {
                Log.e("callTest", new Gson().toJson(response.body()));
                Log.e("SearchUserCallTestSearchUserCallTest", "onResponse: " + new Gson().toJson(response.body()));

                Log.e("callCheckLog", "searchUserData => " + new Gson().toJson(response.body()));
                mApiResponseInterface.isSuccess(response.body(), Constant.SEARCH_USER);

            }

            @Override
            public void onFailure(Call<UserListResponseMet> call, Throwable t) {
                //   Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
                Log.e("SearchUserCallTestSearchUserCallTest", "onFailure: Error " + t.getMessage());
            }
        });
    }

    public void generateCallRequestZ(int id, String outgoingTime, String convId, int callRate, boolean isFreeCall, String remGiftCards) {
        //Log.e("userIdinCall", id + "");
        //Log.e("userIdinCall", id + "");
        showDialog();
        Call<GenerateCallResponce> call = apiService.getDailCallRequestZ(authToken, "application/json", id, outgoingTime, convId, callRate, isFreeCall, remGiftCards);
        Log.e("genToken", call.request().toString());
        call.enqueue(new Callback<GenerateCallResponce>() {
            @Override
            public void onResponse(Call<GenerateCallResponce> call, Response<GenerateCallResponce> response) {
                Log.e("genToken","response"+ new Gson().toJson(response.body()));

                try {
                    if (response.body().getSuccess()) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.NEW_GENERATE_AGORA_TOKENZ);
                    } else {
                        //Toast.makeText(mContext, response.body().getError(), Toast.LENGTH_SHORT).show();
                        mApiResponseInterface.isError("227");
                    }
                    closeDialog();
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<GenerateCallResponce> call, Throwable t) {
                closeDialog();
                //  Log.e("generateCallError", t.getMessage());
                //     Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getNotificationsList() {
        Call<NewNotificationResponse> call = apiService.getNotificationList(authToken);
        call.enqueue(new Callback<NewNotificationResponse>() {
            @Override
            public void onResponse(Call<NewNotificationResponse> call, Response<NewNotificationResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getSuccess()) {
                        Log.e("NewNotificationTest", "onResponse: " + new Gson().toJson(response.body()));
                        mApiResponseInterface.isSuccess(response.body(), GET_NOTIFICATION_LIST);
                    }
                } else {

                }

            }

            @Override
            public void onFailure(Call<NewNotificationResponse> call, Throwable t) {

                Log.e("NewNotificationTest", "onFailure: " + t.getMessage());
            }

        });
    }

    public void getRemainingGiftCardFunction() {
        Call<RemainingGiftCardResponce> call = apiService.getRemainingGiftCardResponce(authToken, "application/json");
        call.enqueue(new Callback<RemainingGiftCardResponce>() {
            @Override
            public void onResponse(Call<RemainingGiftCardResponce> call, Response<RemainingGiftCardResponce> response) {
                Log.e("freeGiftCardData", new Gson().toJson(response.body()));
                Log.e("callCheckLog", "giftCardData => " + new Gson().toJson(response.body()));
                // if (response.body().getSuccess()) {
                mApiResponseInterface.isSuccess(response.body(), Constant.GET_REMAINING_GIFT_CARD);
                // }
            }

            @Override
            public void onFailure(Call<RemainingGiftCardResponce> call, Throwable t) {
                Log.e("freeGiftCardError", t.getMessage());
            }
        });
    }

    public void getFirstTimeRechargeList() {
        Call<FirstTimeRechargeListResponse> call = apiService.getFirstTimeRechargeList(authToken, "application/json");

        call.enqueue(new Callback<FirstTimeRechargeListResponse>() {
            @Override
            public void onResponse(Call<FirstTimeRechargeListResponse> call, Response<FirstTimeRechargeListResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getSuccess()) {
                        Log.e("GET_FIRST_TIME_RECHARGE_LIST", "onResponse: " + new Gson().toJson(response.body()));
                        mApiResponseInterface.isSuccess(response.body(), Constant.GET_FIRST_TIME_RECHARGE_LIST);
                    }
                }
            }

            @Override
            public void onFailure(Call<FirstTimeRechargeListResponse> call, Throwable t) {
                Log.e("GET_FIRST_TIME_RECHARGE_LIST", "onFailure: " + t.getMessage());
            }
        });

    }

    public void getPaymentSelector() {
        //showDialog();
        Call<PaymentSelectorResponce> call = apiService.getPaymentSelector(authToken, "application/json");
        call.enqueue(new Callback<PaymentSelectorResponce>() {
            @Override
            public void onResponse(Call<PaymentSelectorResponce> call, Response<PaymentSelectorResponce> response) {
                try {
                    if (response.body().getSuccess()) {
                        Log.e("getPaymentSelectorData", new Gson().toJson(response.body()));
                        mApiResponseInterface.isSuccess(response.body(), Constant.GET_PAYMENT_SELECTOR);
                    }
                } catch (Exception e) {
                }

            }

            @Override
            public void onFailure(Call<PaymentSelectorResponce> call, Throwable t) {
                Log.e("getPaymentSelectorError", t.getMessage());
                // closeDialog();
            }
        });
    }


    public void verifyPayment(String transaction_id, String orderId) {
        showDialog();

     /* Log.e("transaction_id", transaction_id);
        Log.e("orderId", orderId);
        Log.e("plan_id", plan_id);*/

        Call<ReportResponse> call = apiService.verifyPayment(authToken, "application/json", transaction_id, orderId);
        call.enqueue(new Callback<ReportResponse>() {
            @Override
            public void onResponse(Call<ReportResponse> call, Response<ReportResponse> response) {
                //           Log.e("verifyPayment", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {

                    if (response.body().isSuccess()) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.VERIFY_PAYMENT);
                    } else {
                        mApiResponseInterface.isError(response.body().getError());
                    }
                }
                closeDialog();
            }

            @Override
            public void onFailure(Call<ReportResponse> call, Throwable t) {
                closeDialog();
                //         Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void createpaymentpaytm(String plan_id) {
        Call<PaytmResponse> call = apiService.createpaymentpaytm(authToken, plan_id);
        //Log.e("frxLog", "request => " + call.request().toString());
        Log.e("paytmLog", "request => " + call.request().toString());

        call.enqueue(new Callback<PaytmResponse>() {
            @Override
            public void onResponse(Call<PaytmResponse> call, Response<PaytmResponse> response) {
                Log.e("paytmLog", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {
                    mApiResponseInterface.isSuccess(response.body(), Constant.PAYTM_RESPONSE);
                }
            }

            @Override
            public void onFailure(Call<PaytmResponse> call, Throwable t) {
                //Log.e("frxLog", "error => " + t.getMessage());
            }
        });
    }

    public void createPayment(int plan_id) {
        Log.e("createPaymentPlainId", plan_id + "");
        showDialog();
        Call<CreatePaymentResponse> call = apiService.createPayment(authToken, "application/json", plan_id);
        call.enqueue(new Callback<CreatePaymentResponse>() {
            @Override
            public void onResponse(Call<CreatePaymentResponse> call, Response<CreatePaymentResponse> response) {
                Log.e("createPayment", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.CREATE_PAYMENT);
                    } else {
                        mApiResponseInterface.isError(response.body().getError());
                    }
                }
                closeDialog();
            }

            @Override
            public void onFailure(Call<CreatePaymentResponse> call, Throwable t) {
                closeDialog();
//                Log.e("createPaymentError", t.getMessage());

                //   Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void cashFreePayment(CashFreePaymentRequest cashFreePaymentRequest) {
        Call<Object> call = apiService.cashFreePayment(authToken, "application/json", cashFreePaymentRequest);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.e("cashFreePaymentResponce", new Gson().toJson(response.body()));

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e("cashFreePaymentError", t.getMessage());
            }
        });
    }

    public void paytmPaymentCheck(String transactionId, String orderId) {
        showDialog();
        Call<RazorpayPurchaseResponse> call = apiService.paytmPaymentCheck(authToken, "application/json", transactionId, orderId);
        Log.e("paytmLog", call.request().toString());
        call.enqueue(new Callback<RazorpayPurchaseResponse>() {
            @Override
            public void onResponse(Call<RazorpayPurchaseResponse> call, Response<RazorpayPurchaseResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("paytmLog", new Gson().toJson(response.body()));
                    if (response.body().success) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.GET_RAZORPAY_SUCCESS);
                    } else {
                        mApiResponseInterface.isError(response.body().error.toString());
                    }
                }
                closeDialog();
            }

            @Override
            public void onFailure(Call<RazorpayPurchaseResponse> call, Throwable t) {
                Log.e("PHONEPE_DIRECT", "error => " + t.getMessage());
                closeDialog();
            }
        });
    }

    public void confirmInAppPurchase(String transactionId, String planId, String amount, String customerName, String IAPSignature, String hash) {
        showDialog();
        Log.e("transactionId", transactionId);
        Log.e("planId", planId);

        Call<WalletRechargeResponse> call = apiService.confirmInAppPurchase(authToken, "application/json", transactionId, planId, "IAP", amount, "USD", customerName, IAPSignature, hash);
        call.enqueue(new Callback<WalletRechargeResponse>() {
            @Override
            public void onResponse(Call<WalletRechargeResponse> call, Response<WalletRechargeResponse> response) {
                Log.e("IAP-Recharge", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {

                    if (response.body().isSuccess()) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.RECHARGE_WALLET);

                    } else {
                        mApiResponseInterface.isError(response.body().getError());
                    }
                }
                closeDialog();
            }

            @Override
            public void onFailure(Call<WalletRechargeResponse> call, Throwable t) {
                closeDialog();
                Log.e("IAP-Recharge-Error", t.getMessage());
                //    Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getCfToken(String amount, String plan_id) {
        Log.e("CfTokenAmount", amount);
        Log.e("CfTokenPlanId", plan_id);
        dialog.show();
        Call<CfTokenResponce> call = apiService.getCfToken(authToken, "application/json", amount, plan_id);
        call.enqueue(new Callback<CfTokenResponce>() {
            @Override
            public void onResponse(Call<CfTokenResponce> call, Response<CfTokenResponce> response) {
                Log.e("CfTokenResponce", new Gson().toJson(response.body()));
                dialog.cancel();
                try {
                    mApiResponseInterface.isSuccess(response.body(), Constant.GET_CFTOKEN);
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<CfTokenResponce> call, Throwable t) {
                dialog.cancel();
                Log.e("CfTokenError", t.getMessage());
            }
        });
    }

    public void getFollowingHostList(int pageNumber) {
        Call<FollowingUsers> call = apiService.getFollowingUserList(authToken, pageNumber);
        Log.e("authToken", authToken);
        call.enqueue(new Callback<FollowingUsers>() {
            @Override
            public void onResponse(Call<FollowingUsers> call, Response<FollowingUsers> response) {
                //Log.e("BannerList", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getFollowingUserData() != null) {
                        Log.e("FOLLOWING_LIST==", "First time onResponse: " + new Gson().toJson(response.body()));
                        mApiResponseInterface.isSuccess(response.body(), Constant.FOLLOWING_USER_LIST);
                    }
                }
            }

            @Override
            public void onFailure(Call<FollowingUsers> call, Throwable t) {
                Log.e("FOLLOWING_LIST==", "onFailure: " + t.getMessage());

            }
        });
    }


    public void followingHost(String userId) {
        Call<AddRemoveFavResponse> call = apiService.followedHost(authToken, userId);
        Log.e("authToken", authToken);
        call.enqueue(new Callback<AddRemoveFavResponse>() {
            @Override
            public void onResponse(Call<AddRemoveFavResponse> call, Response<AddRemoveFavResponse> response) {
                //Log.e("BannerList", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getResult() != null) {
                        Log.e("FOLLOWING_HOST==", "onResponse: " + new Gson().toJson(response.body()));
                        mApiResponseInterface.isSuccess(response.body(), Constant.FOLLOWING_HOST);
                    }
                }
            }

            @Override
            public void onFailure(Call<AddRemoveFavResponse> call, Throwable t) {
                Log.e("FOLLOWING_HOST==", "onFailure: " + t.getMessage());

            }
        });
    }

    public void getFollowingHostListNext(int pageNumber) {
        Log.e("userListNXT===>", pageNumber + "");
        //  showDialog();
        Call<FollowingUsers> call = apiService.getFollowingUserList(authToken, pageNumber); //String.valueOf(new SessionManager(mContext).gettLangState())
        Log.e("userListNXT", pageNumber + "");
        call.enqueue(new Callback<FollowingUsers>() {
            @Override
            public void onResponse(Call<FollowingUsers> call, Response<FollowingUsers> response) {
                Log.e("userListNXT", new Gson().toJson(response.body()));

                if (response.isSuccessful() && response.body() != null) {

                    if (response.body().getFollowingUserData().getData() != null) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.USER_LIST_NEXT_PAGE);
                    }
                }
                //  closeDialog();
            }

            @Override
            public void onFailure(Call<FollowingUsers> call, Throwable t) {
                //   closeDialog();
                //        Log.e("userListErrNXT", t.getMessage());
                //   Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getStorePurchasedMineTabList() {
        Call<StoreResponse> call = apiService.getStorePurchaseList(authToken);
        call.enqueue(new Callback<StoreResponse>() {
            @Override
            public void onResponse(Call<StoreResponse> call, Response<StoreResponse> response) {
                Log.e("GET_STORE_PURCHASE_TAB_LIST", "onResponse: " + new Gson().toJson(response.body()));
                mApiResponseInterface.isSuccess(response.body(), GET_STORE_PURCHASE_TAB_LIST);
            }

            @Override
            public void onFailure(Call<StoreResponse> call, Throwable t) {
                Log.e("GET_STORE_PURCHASE_TAB_LIST", "onFailure: Throwable " + t.getMessage());
            }
        });

    }

    public void useOrRemoveItem(String store_id, String store_category_id, String type) {

        Log.e("USE_OR_REMOVE_ITEM", "buyStoreItem: store_id " + store_id + " store_category_id " + store_category_id + " type " + type);


        Call<UseOrRemoveItemResponse> call = apiService.useOrRemoveItem(authToken, store_id, store_category_id, type);
        call.enqueue(new Callback<UseOrRemoveItemResponse>() {
            @Override
            public void onResponse(Call<UseOrRemoveItemResponse> call, Response<UseOrRemoveItemResponse> response) {
                Log.e("USE_OR_REMOVE_ITEM", "onResponse: " + new Gson().toJson(response.body()));
                mApiResponseInterface.isSuccess(response.body(), USE_OR_REMOVE_ITEM);
            }

            @Override
            public void onFailure(Call<UseOrRemoveItemResponse> call, Throwable t) {

            }
        });

    }


    public void buyStoreItem(String store_id, String store_category_id, String store_coin, String store_validity) {

        Log.e("BUY_STORE_ITEM", "buyStoreItem: store_id " + store_id + " store_category_id " + store_category_id + " store_coin " + store_coin + " store_validity " + store_validity);


        Call<BuyStoreItemResponse> call = apiService.buyStoreItem(authToken, store_id, store_category_id, store_coin, store_validity);

        call.enqueue(new Callback<BuyStoreItemResponse>() {
            @Override
            public void onResponse(Call<BuyStoreItemResponse> call, @NonNull Response<BuyStoreItemResponse> response) {
                Log.e("BUY_STORE_ITEM", "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    if (response.body().getSuccess()) {
                        mApiResponseInterface.isSuccess(response.body(), BUY_STORE_ITEM);
                    } else {
                        mApiResponseInterface.isError(response.body().getError());
                    }

                }

            }

            @Override
            public void onFailure(Call<BuyStoreItemResponse> call, Throwable t) {
                Log.e("BUY_STORE_ITEM", "onResponse: Throwable " + t.getMessage());
            }
        });
    }

    public void updateProfileDetailsNew(String type, String name, String city, String dob, String aboutUser, MultipartBody.Part part, boolean is_album) {

        Log.e("UPDATE_PROFILE_NEW", "updateProfileDetailsNew: multipart " + part);
        showDialog();
        Call<UpdateProfileResponse> call = null;

        if (type.equals("name")) {

            call = apiService.updateProfileDetailsNew(authToken, "application/json", name, null, null, null);


        } else if (type.equals("dob")) {

            call = apiService.updateProfileDetailsNew(authToken, "application/json", null, null, dob, null);

        } else if (type.equals("aboutUser")) {
            call = apiService.updateProfileDetailsNew(authToken, "application/json", null, null, null, aboutUser);

        } else if (type.equals("profilePic")) {

            call = apiService.updateProfileDetailsNew(authToken, "application/json", part, false);

        }


        call.enqueue(new Callback<UpdateProfileResponse>() {
            @Override
            public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    if (response.body().getSuccess()) {
                        Log.e("UPDATE_PROFILE_NEW", "onFailure: " + new Gson().toJson(response.body()));

                        mApiResponseInterface.isSuccess(response.body(), Constant.UPDATE_PROFILE);
                    } else if (!response.body().getSuccess()) {
                        if (response.body().getResult().equals("Adult image found")) {
                            mApiResponseInterface.isError("adult_content");
                        } else {

                        }
                    }

                }
                closeDialog();

            }

            @Override
            public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
                Log.e("UPDATE_PROFILE_NEW", "onFailure: " + t.getMessage());

                closeDialog();
            }
        });


    }


    public void sendComplaint(String heading, String des) {
        showDialog();
        Call<ReportResponse> call = apiService.sendComplaint(authToken, "application/json", heading, des);
        call.enqueue(new Callback<ReportResponse>() {
            @Override
            public void onResponse(Call<ReportResponse> call, Response<ReportResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    if (response.body().isSuccess()) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.RAISE_COMPLIANT);

                    } else {
                        mApiResponseInterface.isError(response.body().getError());
                    }
                }
                closeDialog();
            }

            @Override
            public void onFailure(Call<ReportResponse> call, Throwable t) {
                closeDialog();
                //   Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getWalletAmount2() {
        //   showDialog();
        Call<WalletBalResponse> call = apiService.getWalletBalance(authToken, "application/json");
        call.enqueue(new Callback<WalletBalResponse>() {
            @Override
            public void onResponse(Call<WalletBalResponse> call, Response<WalletBalResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mApiResponseInterface.isSuccess(response.body(), Constant.WALLET_AMOUNT2);

                }
                //       closeDialog();
            }

            @Override
            public void onFailure(Call<WalletBalResponse> call, Throwable t) {
                //         closeDialog();
                //     Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void sendUserGift(SendGiftRequest sendGiftRequest) {

        Call<SendGiftResult> call = apiService.sendGift(authToken, sendGiftRequest);

        // Log.e("sendGiftReq", authToken + new Gson().toJson(sendGiftRequest));
        call.enqueue(new Callback<SendGiftResult>() {
            @Override
            public void onResponse(Call<SendGiftResult> call, Response<SendGiftResult> response) {
                Log.e("SendGift1", new Gson().toJson(response.body()));

                try {
                    if (response.body().getSuccess()) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.SEND_GIFT);
                    } else {
                        mApiResponseInterface.isError(response.body().getError());
                    }
                } catch (Exception e) {
                    mApiResponseInterface.isError("Network Error");
                }
            }

            @Override
            public void onFailure(Call<SendGiftResult> call, Throwable t) {
                //     Log.e("SendGiftError", t.getMessage());
                mApiResponseInterface.isError("Network Error");
            }
        });

    }

    public void addUserGift(String uid) {

        Call<GiftEmployeeResult> call = apiService.addGift(authToken, uid);

        // Log.e("addGiftReq", authToken + new Gson().toJson(sendGiftRequest));
        call.enqueue(new Callback<GiftEmployeeResult>() {
            @Override
            public void onResponse(Call<GiftEmployeeResult> call, Response<GiftEmployeeResult> response) {
                Log.e("SendGift", new Gson().toJson(response.body()));

                try {
                    if (response.body().getSuccess()) {
                        mApiResponseInterface.isSuccess(response.body(), Constant.ADD_GIFT);
                    } else {
                        mApiResponseInterface.isError(response.body().getError());
                    }
                } catch (Exception e) {
                    //  mApiResponseInterface.isError("Network Error");
                }
            }

            @Override
            public void onFailure(Call<GiftEmployeeResult> call, Throwable t) {
                //     Log.e("SendGiftError", t.getMessage());
                //  mApiResponseInterface.isError("Network Error");
            }
        });
    }

    public void showDialog() {
        try {
            if (dialog != null && !dialog.isShowing()) {
                dialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeDialog() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
