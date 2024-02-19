package com.privatepe.app.retrofit;


import com.privatepe.app.extras.BannerResponseNew;
import com.privatepe.app.model.AgencyResponse;
import com.privatepe.app.model.AppUpdate.UpdateResponse;
import com.privatepe.app.model.BankList.BankListResponce;
import com.privatepe.app.model.CallPriceUpdateResponse;
import com.privatepe.app.model.FcmTokenResponse;
import com.privatepe.app.model.IncomeReportResponce.IncomeReportFemale;
import com.privatepe.app.model.LevelData.LevelDataResponce;
import com.privatepe.app.model.NewWallet.WalletResponce;
import com.privatepe.app.model.PaymentRequestResponce.PaymentRequestResponce;
import com.privatepe.app.model.PriceList.priceupdateModel;
import com.privatepe.app.model.RequestGiftRequest.RequestGiftRequest;
import com.privatepe.app.model.RequestGiftRequest.RequestGiftResponce;
import com.privatepe.app.model.SubmitResponse;
import com.privatepe.app.model.UpdateProfileNewResponse;
import com.privatepe.app.model.UpdateProfileResponse;
import com.privatepe.app.model.Video.VideoResponce;
import com.privatepe.app.model.VideoStatus.VideoStatusResponseModel;
import com.privatepe.app.model.WalletBalResponse;
import com.privatepe.app.model.WalletRechargeResponse;
import com.privatepe.app.model.Walletfilter.WalletfilterResponce;
import com.privatepe.app.model.account.AccountResponse;
import com.privatepe.app.model.body.CallRecordBody;
import com.privatepe.app.model.Authenticate;
import com.privatepe.app.model.EndCallData.EncCallResponce;
import com.privatepe.app.model.EndCallData.EndCallData;
import com.privatepe.app.model.Login;
import com.privatepe.app.model.LoginResponse;
import com.privatepe.app.model.OnlineStatusResponse;
import com.privatepe.app.model.ProfileDetailsResponse;
import com.privatepe.app.model.UserListResponseNew.UserListResponseNewData;
import com.privatepe.app.model.city.CityResponse;
import com.privatepe.app.model.fcm.MyResponse;
import com.privatepe.app.model.fcm.Sender;
import com.privatepe.app.model.gift.ResultGift;
import com.privatepe.app.model.gift.SendGiftRequest;
import com.privatepe.app.model.gift.SendGiftResult;
import com.privatepe.app.model.language.LanguageResponce;
import com.privatepe.app.model.logout.LogoutResponce;
import com.privatepe.app.response.AddAccount.AddAccountResponse;
import com.privatepe.app.response.Agency.AgencyPolicyResponse;
import com.privatepe.app.response.AgencyDate.AgencyCenterDateResponse;
import com.privatepe.app.response.AgencyHostWeekly.AgencyHostWeeklyResponse;
import com.privatepe.app.response.AgencyHostWeekly.WeeklyRewardResponse;
import com.privatepe.app.response.Banner.BannerResponse;
import com.privatepe.app.response.DataFromProfileId.DataFromProfileIdResponse;
import com.privatepe.app.response.DisplayGiftCount.GiftCountResult;
import com.privatepe.app.response.HostIncomeDetail.IncomeDetailResponse;
import com.privatepe.app.response.HostIncomeResponse.IncomeResponse;
import com.privatepe.app.response.NewVideoStatus.NewVideoStatusResponse;
import com.privatepe.app.response.NewZegoTokenResponse;
import com.privatepe.app.response.Otptwillow.OtpTwillowResponce;
import com.privatepe.app.response.Otptwillow.OtpTwillowVerifyResponse;
import com.privatepe.app.response.RecentActiveHostModel;
import com.privatepe.app.response.ReportResponse;
import com.privatepe.app.response.SettlementCenter.HostSettlementDateResponse;
import com.privatepe.app.response.SettlementDate.SettlementHostWeeklyResponse;
import com.privatepe.app.response.TopReceiver.TopReceiverResponse;
import com.privatepe.app.response.UdateAccountResponse;
import com.privatepe.app.response.UserListResponse;
import com.privatepe.app.response.VideoPlayResponce;
import com.privatepe.app.response.accountvarification.CheckFemaleVarifyResponse;
import com.privatepe.app.response.chat_price.PriceListResponse;
import com.privatepe.app.response.chat_price.UpdateCallPriceResponse;
import com.privatepe.app.response.metend.AdapterRes.UserListResponseMet;
import com.privatepe.app.response.metend.AddRemoveFavResponse;
import com.privatepe.app.response.metend.Ban.BanResponce;
import com.privatepe.app.response.metend.CreatePaymentResponse;
import com.privatepe.app.response.metend.DirectUPI.RazorpayPurchaseResponse;
import com.privatepe.app.response.metend.DiscountedRecharge.DiscountedRechargeResponse;
import com.privatepe.app.response.metend.FirstTimeRechargeListResponse;
import com.privatepe.app.response.metend.FollowingUsers;
import com.privatepe.app.response.metend.GenerateCallResponce.GenerateCallResponce;
import com.privatepe.app.response.metend.GenerateCallResponce.NewGenerateCallResponse;
import com.privatepe.app.response.metend.PaymentGatewayDetails.CashFree.CFToken.CfTokenResponce;
import com.privatepe.app.response.metend.PaymentGatewayDetails.CashFree.CashFreePayment.CashFreePaymentRequest;
import com.privatepe.app.response.metend.PaymentGatewayDetails.PaymentGatewayResponce;
import com.privatepe.app.response.metend.PaymentSelector.PaymentSelectorResponce;
import com.privatepe.app.response.metend.PaytmDirect.PaytmResponse;
import com.privatepe.app.response.metend.RechargePlan.RechargePlanResponseNew;
import com.privatepe.app.response.metend.RemainingGiftCard.RemainingGiftCardResponce;
import com.privatepe.app.response.metend.gift.GiftEmployeeResult;
import com.privatepe.app.response.metend.new_notifications.NewNotificationResponse;
import com.privatepe.app.response.metend.store.response.purchase.BuyStoreItemResponse;
import com.privatepe.app.response.metend.store.response.use_or_remove.UseOrRemoveItemResponse;
import com.privatepe.app.response.metend.store_list.StoreResponse;
import com.privatepe.app.response.newgiftresponse.NewGiftListResponse;
import com.privatepe.app.response.sub_agency.SubAgencyResponse;
import com.privatepe.app.response.temporary_block.TemporaryBlockResponse;
import com.privatepe.app.response.trading_response.GetTradingUserNameResponse;
import com.privatepe.app.response.trading_response.TradingAccountResponse;
import com.privatepe.app.response.trading_response.TradingHistoryResponse;
import com.privatepe.app.response.trading_response.TradingTransferModel;
import com.privatepe.app.response.trading_response.TransferTradeAccountResponse;
import com.privatepe.app.response.trading_response.UpdateHistoryModel;
import com.privatepe.app.response.trading_response.UpdateTransferDetailResponse;
import com.privatepe.app.response.trading_response.UserIdBodyModel;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiInterface {
    @POST("loginlocal")
    Call<ResponseBody> post();

    @GET("dialCallNew")
    Call<Login> getLogin();

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("coin-per-second-user-busy")
    Call<ResponseBody> coin_per_second_user_busy(@Body Authenticate authenticate, @Header("Authorization") String auth);

    @POST("coin-per-second-user-busy")
    Call<Object> sendCallRecord(@Header("Authorization") String token,
                                @Header("Accept") String accept,
                                @Body CallRecordBody callRecordBody);

    @FormUrlEncoded
    @POST("loginlocal")
    Call<LoginResponse> loginUser(@Field("username") String username,
                                  @Field("password") String password,
                                  @Field("myhaskey") String hash);

    @FormUrlEncoded
    @POST("loginlocalmobile")
    Call<LoginResponse> loginUserMobile(@Field("mobile") String username,
                                        @Field("device_id") String password,
                                        @Field("myhaskey") String hash);

    @FormUrlEncoded
    @POST("loginlocalmobilelatest")
    Call<LoginResponse> loginUserMobileLatest(@Field("countrycode") String countrycode,
                                              @Field("mobile") String username,
                                              @Field("session_uuid") String session_uuid,
                                              @Field("otp") String otp,
                                              @Field("device_id") String password,
                                              @Field("myhaskey") String hash);


    @GET("getbannerList")
    Call<BannerResponse> getBannerData(@Header("Authorization") String token, @Header("Accept") String accept, @Query("type") String type);

    @GET("getbannerList")
    Call<BannerResponseNew> getBannerDataNew(@Header("Authorization") String token, @Header("Accept") String accept, @Query("type") String type);

    /* @FormUrlEncoded
       @POST("loginlocal")
       Call<LoginResponse> loginUser(@Field("username") String username,
                                     @Field("password") String password,
                                     @Field("myhaskey") String hash);*/
    @FormUrlEncoded
    @POST("updateWalletAndCallToZero")
    Call<Object> callCutByHost(@Header("Authorization") String token,
                               @Header("Accept") String accept,
                               @Field("unique_id") String unique_id);

    @FormUrlEncoded
    @POST("checkAgencyHostRegisterTime")
    Call<AgencyResponse> getAgencyInfo(@Header("Authorization") String token,
                                       @Header("Accept") String accept,
                                       @Field("agency_id") String agency_id);

    @FormUrlEncoded
    @POST("update-host-profile")
    Call<UpdateProfileResponse> updateProfileDetailsName(@Header("Authorization") String token,
                                                         @Header("Accept") String accept,
                                                         @Field("name") String name);

    @FormUrlEncoded
    @POST("update-host-profile")
    Call<UpdateProfileResponse> updateProfileDetailsCity(@Header("Authorization") String token,
                                                         @Header("Accept") String accept,
                                                         @Field("city") String city);

    @FormUrlEncoded
    @POST("update-host-profile")
    Call<UpdateProfileResponse> updateProfileDetailsDob(@Header("Authorization") String token,
                                                        @Header("Accept") String accept,
                                                        @Field("dob") String dob);

    @FormUrlEncoded
    @POST("update-host-profile")
    Call<UpdateProfileResponse> updateProfileDetailsAbout(@Header("Authorization") String token,
                                                          @Header("Accept") String accept,
                                                          @Field("about_user") String about_user);

    @FormUrlEncoded
    @POST("update-host-profile")
    Call<UpdateProfileResponse> updateProfileDetailsLanguage(@Header("Authorization") String token,
                                                             @Header("Accept") String accept,
                                                             @Field("language_id") int language_id);//name, dob, city, about_user, language_id

    @Multipart
    @POST("update-host-profile")
    Call<UpdateProfileResponse> updateProfileDetailsProfile(@Header("Authorization") String token,
                                                            @Header("Accept") String accept,
                                                            @Part MultipartBody.Part picToProfile);

    @Multipart
    @POST("update-host-profile-new")
    Call<UpdateProfileNewResponse> updateProfileDetailsProfileNew(@Header("Authorization") String token,
                                                                  @Header("Accept") String accept,
                                                                  @Part MultipartBody.Part picToProfile);

    @GET("top-receiver-giver")
    Call<TopReceiverResponse> getWinnerList(@Header("Authorization") String token, @Header("Accept") String accept,
                                            @Query("interval") String interval);


    @Multipart
    @POST("update-host-profile")
    Call<UpdateProfileResponse> updateProfileDetailsAlbum(@Header("Authorization") String token,
                                                          @Header("Accept") String accept,
                                                          @Part MultipartBody.Part[] picToAlbum);

    @GET("online-status-golive-new")
    Call<OnlineStatusResponse> manageOnlineStatus(@Header("Authorization") String token, @Header("Accept") String accept, @Query("is_online") int is_online);

    @GET("cities")
    Call<CityResponse> cityList(@Header("Authorization") String token);

    @GET("languages")
    Call<LanguageResponce> languageList(@Header("Authorization") String token);

    @POST("end-call-new")
    Call<EncCallResponce> sendEndCallTime(@Header("Authorization") String token,
                                          @Header("Accept") String accept,
                                          @Body ArrayList<EndCallData> endCallData);

    @GET("getprofiledata")
    Call<UserListResponseNewData> getProfileData(@Header("Authorization") String token, @Header("Accept") String accept, @Query("q") String q,
                                                 @Query("page") String p, @Query("id") String id, @Query("language_id") String lanid);

    @GET("getprofiledata")
    Call<com.privatepe.app.response.metend.UserListResponseNew.UserListResponseNewData> getProfileDataNew(@Header("Authorization") String token, @Header("Accept") String accept, @Query("q") String q,
                                                                                                          @Query("page") String p, @Query("id") String id, @Query("language_id") String lanid);

    @POST("details")
    Call<ProfileDetailsResponse> getProfileDetails(@Header("Authorization") String token, @Header("Accept") String accept);

    @GET("languages")
    Call<LanguageResponce> getLanguageData(@Header("Authorization") String token, @Header("Accept") String accept);

    @GET("getGifts")
    Call<ResultGift> getGift(
            @Header("Authorization") String header1);

    @POST("ask_for_gift")
    Call<RequestGiftResponce> sendGiftRequestFromHost(
            @Header("Authorization") String header1,
            @Body RequestGiftRequest requestGiftRequest
    );


    //  @GET("checkAppStatusnewtest")
    @GET("checkAppStatusnew")
    Call<UpdateResponse> getUpdateApp();

    @POST("logout")
    Call<LogoutResponce> logout(@Header("Authorization") String token, @Header("Accept") String accept);

    @GET("deleteUserAccount")
    Call<Object> getAccountDelete(@Header("Authorization") String token, @Header("Accept") String accept);


    //changed as album is not being sent now
    @Multipart
    @POST("hostregisterAfterProfileDataLatest")
    Call<SubmitResponse> updateProfileDetails(@Header("Authorization") String token,
                                              @Header("Accept") String accept,
                                              @Part("agency_id") RequestBody agency_id,
                                              @Part("name") RequestBody name,
                                              @Part("dob") RequestBody age,
                                              @Part("city") RequestBody city,
                                              @Part("language_id") RequestBody language,
                                              @Part MultipartBody.Part picToProfile);

    @GET("getLevelData")
        //getLevelData
    Call<LevelDataResponce> getLevelData(@Header("Authorization") String token,
                                         @Header("Accept") String accept);

    @GET("wallet-history-latest")
    Call<WalletResponce> getWalletHistoryFemale(@Header("Authorization") String token, @Header("Accept") String accept
            , @Query("page") int page);

    @GET("wallet-history-incomereport")
    Call<IncomeReportFemale> getWalletHistoryFemaleNew(@Header("Authorization") String token, @Header("Accept") String accept);

    @GET("points")
    Call<WalletBalResponse> getWalletBalance(@Header("Authorization") String token, @Header("Accept") String accept);

    @GET("wallet-history-latest")
    Call<WalletfilterResponce> getWalletHistoryFilter(@Header("Authorization") String token, @Header("Accept") String accept
            , @Query("by_week") String filterData);

    @FormUrlEncoded
    @POST("point_update")
    Call<WalletRechargeResponse> redeemCoins(@Header("Authorization") String token, @Header("Accept") String accept, @Field("redeem_point") String points);

    @FormUrlEncoded
    @POST("updateFCMToken")
    Call<FcmTokenResponse> registerFcmToken(@Header("Authorization") String token, @Header("Accept") String accept, @Field("token") String fcmToken, @Field("app_version") String AppVersion);


    @Headers({"Content-Type:application/json", "Authorization:key=AAAA_HjaAC8:APA91bGL2g1vHs9QoazpxJyn4JDSm22-Ooupn88VjqGaNjEKGVF4VvA2ShiGn8TOvGhNC_uS_GawxZcn2-BmU-JZoUK9JvWoLxtkWW8067yzdlClgYK44PwYMFreGdaqhAFpvvWXJnZz"})
    @POST("fcm/send")
    Call<MyResponse> sendNotificationInBox(@Body Sender sender);


    @Multipart
    @POST("upload-video-via-mobile")
    Call<VideoResponce> sendVideo(@Header("Authorization") String token, @Header("Accept") String accept, @Part MultipartBody.Part vdo);

    @GET("userListLatest")
        // @GET("userlistdemo")
    Call<com.privatepe.app.model.UserListResponse> getUserList(@Header("Authorization") String token, @Header("Accept") String accept, @Query("q") String q,
                                                               @Query("page") String p, @Query("per_page_records") String lim, @Query("language_id") String lanid);

    @GET("userListLatest")
    Call<UserListResponseMet> getUserList2(@Header("Authorization") String token, @Header("Accept") String accept, @Query("q") String q,
                                           @Query("page") String p, @Query("per_page_records") String lim, @Query("language_id") String lanid);

    @GET("userListLatest")
        // @GET("userlistdemo")
    Call<UserListResponseMet> getUserListNew(@Header("Authorization") String token, @Header("Accept") String accept, @Query("q") String q,
                                             @Query("page") String p, @Query("per_page_records") String lim, @Query("language_id") String lanid);

    @GET("getNearbyList")
    Call<com.privatepe.app.model.UserListResponse> getNearbyList(@Header("Authorization") String token, @Header("Accept") String accept, @Query("q") String q,
                                                                 @Query("page") String p, @Query("per_page_records") String lim, @Query("language_id") String lanid);

    @GET("getPopularList")
    Call<com.privatepe.app.model.UserListResponse> getPopulartList(@Header("Authorization") String token, @Header("Accept") String accept, @Query("q") String q,
                                                                   @Query("page") String p, @Query("per_page_records") String lim, @Query("language_id") String lanid);

    //agency & subagency
    @GET("agencyhostprofilelist")
    Call<UserListResponse> getUserList(@Header("Authorization") String token, @Header("Accept") String accept, @Query("page") String p);


    @GET("agencyhostprofilelist")
    Call<UserListResponse> searchUserList(@Header("Authorization") String token, @Header("Accept") String accept, @Query("q") String search);


    @GET("agencycommissionList")
    Call<AgencyPolicyResponse> getAgencyList(@Header("Authorization") String token, @Header("Accept") String accept);

    @GET("subagencylist")
    Call<SubAgencyResponse> getSubAgencyList(@Header("Authorization") String token, @Header("Accept") String accept, @Query("q") String type);

    @GET("agencyHostsWeeklyReportList")
    Call<AgencyCenterDateResponse> getAgencyDateList(@Header("Authorization") String token, @Header("Accept") String accept);


    @POST("agencyHostsWeeklyReportDetails")
    Call<AgencyHostWeeklyResponse> getAgencyWeeklyData(@Header("Authorization") String token, @Header("Accept") String accept, @Query("month") String month);


    @GET("agencyHostsSettlementList")
    Call<HostSettlementDateResponse> getSettlementDateList(@Header("Authorization") String token, @Header("Accept") String accept);

    @POST("agencyHostsSettlementDetails")
    Call<SettlementHostWeeklyResponse> getSettlementWeeklyData(@Header("Authorization") String token, @Header("Accept") String accept, @Query("month") String month);


    @GET("agencyAccountDetails")
    Call<AddAccountResponse> getAccountDetail(@Header("Authorization") String token, @Header("Accept") String accept);

    @FormUrlEncoded
    @POST("agencyAddEditAccountDetails")
    Call<UdateAccountResponse> updateAccountDetail(@Header("Authorization") String token, @Header("Accept") String accept,
                                                   @Field("account_number") String account_number,
                                                   @Field("confirm_account_number") String confirm_account_number,
                                                   @Field("bank_name") String bank_name,
                                                   @Field("ifsc_code") String ifsc_code,
                                                   @Field("account_name") String account_name,
                                                   @Field("address") String address,
                                                   @Field("email") String email,
                                                   @Field("mobile") String phone,
                                                   @Field("upi_id") String upi_id,
                                                   @Field("type") String type);

    //{
    //    "account_number": 6511770257,
    //    "": "Axis Bankt",
    //    "": "KKBK0000677",
    //    "": "shivam tyagiffdsa",
    //    "": "fdsaffdf",
    //    "": "stylebetsfasf.com@gmail.com",
    //    "": "880209185330",
    //    "":1
    //}
    @GET("paymentRequests")
    Call<AccountResponse> updateAccountDetailNew(@Header("Authorization") String token, @Header("Accept") String accept,
                                                 @Query("account_number") String account_number,
                                                 @Query("bank_name") String bank_name,
                                                 @Query("ifsc_code") String ifsc_code,
                                                 @Query("account_name") String account_name,
                                                 @Query("address") String address,
                                                 @Query("email") String email,
                                                 @Query("mobile") String mobile,
                                                 @Query("upi_id") String upi_id,
                                                 @Query("type") String type);

    @GET("paymentRequestByUser")
    Call<PaymentRequestResponce> getPaymentRequestDetail(@Header("Authorization") String token, @Header("Accept") String accept);

    @GET("bankList")
    Call<BankListResponce> getBankListDetail(@Header("Authorization") String token, @Header("Accept") String accept);


    @GET("femaleincomereportList")
    Call<IncomeResponse> getHostThisWeekIncome(@Header("Authorization") String token, @Header("Accept") String accept);

    @GET("femaleincomereportList")
    Call<IncomeResponse> getHostAllWeekIncome(@Header("Authorization") String token, @Header("Accept") String accept, @Query("month") String month);


    @GET("femaleincomereportDetails")
    Call<IncomeDetailResponse> geIncomeDetails(@Header("Authorization") String token, @Header("Accept") String accept, @Query("month") String month);


    @GET("callpriceList")
    Call<PriceListResponse> getCallPriceList(@Header("Authorization") String token);


    @POST("updatecallprice")
    Call<CallPriceUpdateResponse> updateCallPrice(@Header("Authorization") String token, @Body priceupdateModel priceModel);


    @FormUrlEncoded
    @POST("updatecallprice")
    Call<UpdateCallPriceResponse> updateCallPrice(@Header("Authorization") String token,
                                                  @Field("call_rate") String call_rate);

    @GET("userstatus")
    Call<VideoStatusResponseModel> getUserVideoStatus(@Header("Authorization") String token);


    @GET("generateNewZegoToken")
    Call<NewZegoTokenResponse> getnewZegoToken(@Header("Authorization") String token, @Query("room_id") String roomId);

    @GET("getTradeAccount")
    Call<TradingAccountResponse> getTradingAccount(@Header("Authorization") String token);


    @POST("getnametradeaccount")
    Call<GetTradingUserNameResponse> getTradingUserName(@Header("Authorization") String token, @Body UserIdBodyModel profileId);

    @POST("sendtransferTradeAccount")
    Call<TransferTradeAccountResponse> sendtransferTradeAccount(@Header("Authorization") String token, @Body TradingTransferModel tradingTransferModel);

  /*
     @GET("wallet-history-tradeaccount")
     Call<TradingHistoryResponse> getWalletHistoryTradeAccount(@Header("Authorization") String token);
  */

    @GET("wallet-history-tradeaccount")
    Call<TradingHistoryResponse> getWalletHistoryTradeAccount(@Header("Authorization") String token,
                                                              @Query("page") String p,
                                                              @Query("per_page_records") String per_page_records);


    @GET("getEmployeeVideoRandomly")
    Call<VideoPlayResponce> getVideoplay(@Header("Authorization") String token,
                                         @Header("Accept") String accept,
                                         @Query("user_id") String convId);


    @POST("updatetransferdetails")
    Call<UpdateTransferDetailResponse> updateTransferDetail(@Header("Authorization") String token, @Body UpdateHistoryModel updateHistoryModel);


    @POST("checkfemaleverify")
    Call<CheckFemaleVarifyResponse> checkFemaleVarify(@Header("Authorization") String token);


    @GET("getWeeklyRewardList")
    Call<WeeklyRewardResponse> getWeeklyRewardList(@Header("Authorization") String token);


    @GET("checktemporaryblockuser")
    Call<TemporaryBlockResponse> checktemporaryblockuser(@Header("Authorization") String token);


    @POST("getProfileIdData")
    Call<DataFromProfileIdResponse> getProfileIdData(@Header("Authorization") String token, @Query("profile_id") String unique_id);


    @GET("report-user")
    Call<ReportResponse> reportUser(@Header("Authorization") String token, @Header("Accept") String accept,
                                    @Query("report_to") String id, @Query("is_user_block") String b,
                                    @Query("description") String des, @Query("input_description") String in_des);

    @GET("get-gift-count")
    Call<GiftCountResult> getGiftCountForHost(@Header("Authorization") String token, @Header("Accept") String accept,
                                              @Query("id") String convId);

    @GET("getprofiledata")
    Call<UserListResponseNewData> getRateCountForHost(@Header("Authorization") String token, @Header("Accept") String accept,
                                                      @Query("id") String host_profileId);

    @POST("delete-profile-video")
    Call<Object> profileVideoDelete(@Header("Authorization") String token, @Header("Accept") String accept, @Query("video_id") String video_id);

    @GET("getProfileImageReview")
    Call<UpdateProfileNewResponse> getProfileImageReview(@Header("Authorization") String token);


    @GET("getcategoryGifts")
    Call<NewGiftListResponse> getCategoryGifts();


    @GET("userVideoStatusList")
    Call<NewVideoStatusResponse> getStatusVideosList(@Header("Authorization") String authToken, @Query("id") String userId);


    @FormUrlEncoded
    @POST("aaoregistrationkare")
    Call<LoginResponse> loginFbGoogle(@Field("login_type") String login_type,
                                      @Field("name") String name,
                                      @Field("username") String username,
                                      @Field("myhaskey") String hash,
                                      @Field("device_id") String device_id);

    @FormUrlEncoded
    @POST("aaoregistrationkare")
    Call<LoginResponse> guestRegister(@Field("login_type") String login_type,
                                      @Field("device_id") String device_id,
                                      @Field("myhaskey") String hash);

    @GET("planlist")
    Call<RechargePlanResponseNew> getRechargeList(@Header("Authorization") String token, @Header("Accept") String accept);

    @GET("checkFirstTimeRecharge")
    Call<DiscountedRechargeResponse> checkFirstTimeRecharge(@Header("Authorization") String token);

    @GET("getcategoryStores")
    Call<StoreResponse> getStoreTabList(@Header("Authorization") String token);

    @Multipart
    @POST("update-guest-profile")
    Call<Object> upDateGuestProfile(@Header("Authorization") String token,
                                    @Header("Accept") String accept,
                                    @Part("name") RequestBody name,
                                    @Part MultipartBody.Part profile_pic);

    @GET("remaininggiftcard")
    Call<RemainingGiftCardResponce> getRemainingGiftCardResponce(@Header("Authorization") String token, @Header("Accept") String accept);

    @POST("check-user-ban_status")
    Call<BanResponce> getBanData(@Header("Authorization") String token, @Header("Accept") String accept);// @FormUrlEncoded

    @GET("update-lat-lng")
    Call<Object> getLatLonUpdated(@Header("Authorization") String token, @Header("Accept") String accept, @Query("country") String user_country, @Query("user_city") String user_city,
                                  @Query("lat") String lat, @Query("lng") String lng);

    @GET("userListWithLastCallLatest")
    Call<UserListResponseMet> getUserListWithLastCallLatest(@Header("Authorization") String token, @Header("Accept") String accept, @Query("q") String q,
                                                            @Query("page") String p, @Query("per_page_records") String lim, @Query("language_id") String lanid);

    @GET("getApiKeysAndBalance")
    Call<PaymentGatewayResponce> getPaymentData(@Header("Authorization") String token,
                                                @Header("Accept") String accept);

    @GET("userListLatest")
    Call<UserListResponseMet> searchUser(@Header("Authorization") String token, @Header("Accept") String accept, @Query("q") String q,
                                         @Query("page") String p, @Query("per_page_records") String lim);

    @GET("dialCallZegoSendNotification")
    Call<NewGenerateCallResponse> getDailCallRequestZ(@Header("Authorization") String token,
                                                      @Header("Accept") String accept,
                                                      @Query("connecting_user_id") int id,
                                                      @Query("outgoing_time") String outgoingTime,
                                                      @Query("convId") String convId,
                                                      @Query("call_rate") int callRate,
                                                      @Query("is_free_call") boolean isFreeCall,
                                                      @Query("rem_gift_cards") String remGiftCards);

    @GET("getmessageList")
    Call<NewNotificationResponse> getNotificationList(@Header("Authorization") String token);

    @GET("firstrechargeplanlist")
    Call<FirstTimeRechargeListResponse> getFirstTimeRechargeList(@Header("Authorization") String token, @Header("Accept") String accept);

    @GET("getPaymentGatewayZeeplive")
    Call<PaymentSelectorResponce> getPaymentSelector(@Header("Authorization") String token, @Header("Accept") String accept);

    @FormUrlEncoded
    @POST("checkpayment")
    Call<ReportResponse> verifyPayment(@Header("Authorization") String token, @Header("Accept") String accept, @Field("transaction_id") String transaction_id, @Field("order_id") String order_id);

    @FormUrlEncoded
    @POST("createpaymentpaytm")
    Call<PaytmResponse> createpaymentpaytm(@Header("Authorization") String token,
                                           @Field("plan_id") String plan_id);

    @FormUrlEncoded
    @POST("createpayment")
    Call<CreatePaymentResponse> createPayment(@Header("Authorization") String token, @Header("Accept") String accept, @Field("plan_id") int plan_id);

    @POST("cash-free-payment")
    Call<Object> cashFreePayment(
            @Header("Authorization") String token,
            @Header("Accept") String accept,
            @Body CashFreePaymentRequest cashFreePaymentRequest);

    @FormUrlEncoded
    @POST("paytmPaymentCheck")
    Call<RazorpayPurchaseResponse> paytmPaymentCheck(@Header("Authorization") String token,
                                                     @Header("Accept") String accept,
                                                     @Field("transaction_id") String transaction_id,
                                                     @Field("order_id") String orderId);

    @FormUrlEncoded
    @POST("confirmInAppPurchase")
    Call<WalletRechargeResponse> confirmInAppPurchase(@Header("Authorization") String token, @Header("Accept") String accept,
                                                      @Field("razorpay_id") String id, @Field("plan_id") String planId,
                                                      @Field("plan_type") String plan_type,
                                                      @Field("amount") String amount,
                                                      @Field("order_currency") String order_currency,
                                                      @Field("customerName") String customerName,
                                                      @Field("signature") String IAPSignature,
                                                      @Field("myhaskey") String hash);

    @GET("getCashFreeToken")
    Call<CfTokenResponce> getCfToken(@Header("Authorization") String token,
                                     @Header("Accept") String accept,
                                     @Query("amount") String amount,
                                     @Query("plan_id") String plan_id);

    @GET("getfollowFollowerCount")
    Call<FollowingUsers> getFollowingUserList(@Header("Authorization") String token, @Query("page") Integer p);

    @POST("follow")
    Call<AddRemoveFavResponse> followedHost(@Header("Authorization") String token, @Query("following_id") String userId);

    @GET("getPurchaseStoreList")
    Call<StoreResponse> getStorePurchaseList(@Header("Authorization") String token);

    @POST("PurchaseStorePlanUsingByUser")
    Call<UseOrRemoveItemResponse> useOrRemoveItem(@Header("Authorization") String token, @Query("store_id") String store_id,
                                                  @Query("store_category_id") String store_category_id, @Query("type") String type);

    @POST("userPurchaseStorePlan")
    Call<BuyStoreItemResponse> buyStoreItem(@Header("Authorization") String token, @Query("store_id") String store_id, @Query("store_category_id") String store_category_id,
                                            @Query("store_coin") String store_coin, @Query("store_validity") String store_validity);

    @FormUrlEncoded
    @POST("update-profile")
    Call<UpdateProfileResponse> updateProfileDetailsNew(@Header("Authorization") String token,
                                                        @Header("Accept") String accept,
                                                        @Field("name") String name,
                                                        @Field("city") String city,
                                                        @Field("dob") String dob,
                                                        @Field("about_user") String about_user);

    @Multipart
    @POST("update-profile")
    Call<UpdateProfileResponse> updateProfileDetailsNew(@Header("Authorization") String token,
                                                        @Header("Accept") String accept,
                                                        @Part MultipartBody.Part picToUpload,
                                                        @Part("is_album") boolean is_album);

    @FormUrlEncoded
    @POST("compalint")
    Call<ReportResponse> sendComplaint(@Header("Authorization") String token,
                                       @Header("Accept") String accept,
                                       @Field("issue_heading") String heading,
                                       @Field("description") String des);

    @POST("send_gift")
    Call<SendGiftResult> sendGift(
            @Header("Authorization") String header1,
            @Body SendGiftRequest requestGift
    );

    @FormUrlEncoded
    @POST("add_gift")
    Call<GiftEmployeeResult> addGift(
            @Header("Authorization") String header1,
            @Field("gift_receiver_id") String unique_id
    );

    @FormUrlEncoded
    @POST("send-otp")
    Call<OtpTwillowResponce> otp2Factor(@Field("encryptedData") String encryptedData);

    @FormUrlEncoded
    @POST("verify-otp")
    Call<OtpTwillowVerifyResponse> otp2FactorVerify(@Header("Authorization") String token,
                                                    @Field("session_uuid") String session_uuid,
                                                    @Field("otp") String otp,
                                                    @Field("myhaskey") String myhaskey
    );

    @FormUrlEncoded
    @POST("createOneToOneMessage")
    Call<Object> markMessageRead(@Header("Authorization") String token,
                                 @Field("report_account") String report_account,
                                 @Field("peer_account") String peer_account);

    @GET("recent-active-host-list")
    Call<RecentActiveHostModel> recentActiveHost(@Header("Authorization") String token);
}