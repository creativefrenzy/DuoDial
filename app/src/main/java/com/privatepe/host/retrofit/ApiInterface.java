package com.privatepe.host.retrofit;


import com.privatepe.host.extras.BannerResponseNew;
import com.privatepe.host.model.AgencyResponse;
import com.privatepe.host.model.AppUpdate.UpdateResponse;
import com.privatepe.host.model.BankList.BankListResponce;
import com.privatepe.host.model.CallPriceUpdateResponse;
import com.privatepe.host.model.Deletelivebroadresponse;
import com.privatepe.host.model.FcmTokenResponse;
import com.privatepe.host.model.FollowersModelClass;
import com.privatepe.host.model.IncomeReportResponce.IncomeReportFemale;
import com.privatepe.host.model.InvitationRewardReponse;
import com.privatepe.host.model.LevelData.LevelDataResponce;
import com.privatepe.host.model.MyTopFansModel;
import com.privatepe.host.model.NewWallet.WalletResponce;
import com.privatepe.host.model.PaymentRequestResponce.PaymentRequestResponce;
import com.privatepe.host.model.PriceList.priceupdateModel;
import com.privatepe.host.model.RequestGiftRequest.RequestGiftRequest;
import com.privatepe.host.model.RequestGiftRequest.RequestGiftResponce;
import com.privatepe.host.model.SubmitResponse;
import com.privatepe.host.model.UpdateProfileNewResponse;
import com.privatepe.host.model.UpdateProfileResponse;
import com.privatepe.host.model.Video.VideoResponce;
import com.privatepe.host.model.VideoStatus.VideoStatusResponseModel;
import com.privatepe.host.model.WalletBalResponse;
import com.privatepe.host.model.WalletRechargeResponse;
import com.privatepe.host.model.Walletfilter.WalletfilterResponce;
import com.privatepe.host.model.account.AccountResponse;
import com.privatepe.host.model.body.CallRecordBody;
import com.privatepe.host.model.Authenticate;
import com.privatepe.host.model.EndCallData.EncCallResponce;
import com.privatepe.host.model.EndCallData.EndCallData;
import com.privatepe.host.model.Login;
import com.privatepe.host.model.LoginResponse;
import com.privatepe.host.model.OnlineStatusResponse;
import com.privatepe.host.model.ProfileDetailsResponse;
import com.privatepe.host.model.UserListResponseNew.UserListResponseNewData;
import com.privatepe.host.model.city.CityResponse;
import com.privatepe.host.model.fcm.MyResponse;
import com.privatepe.host.model.fcm.Sender;
import com.privatepe.host.model.gift.ResultGift;
import com.privatepe.host.model.gift.SendGiftRequest;
import com.privatepe.host.model.gift.SendGiftResult;
import com.privatepe.host.model.language.LanguageResponce;
import com.privatepe.host.model.logout.LogoutResponce;
import com.privatepe.host.response.AddAccount.AddAccountResponse;
import com.privatepe.host.response.AddReferralCardResponse;
import com.privatepe.host.response.Agency.AgencyPolicyResponse;
import com.privatepe.host.response.AgencyDate.AgencyCenterDateResponse;
import com.privatepe.host.response.AgencyHostWeekly.AgencyHostWeeklyResponse;
import com.privatepe.host.response.AgencyHostWeekly.WeeklyRewardResponse;
import com.privatepe.host.response.Auto_Message.AutoMessageNew.AutoMessageNewResponse;
import com.privatepe.host.response.Auto_Message.AutoMessageRequest;
import com.privatepe.host.response.Auto_Message.AutoMessageResponse;
import com.privatepe.host.response.Banner.BannerResponse;
import com.privatepe.host.response.CallDetailResponse;
import com.privatepe.host.response.HaodaPayResponse.HaodaPayModel;
import com.privatepe.host.response.PaymentGateway.PaymentGatewayModel;
import com.privatepe.host.response.daily_weekly.DailyUserListResponse;
import com.privatepe.host.response.DataFromProfileId.DataFromProfileIdResponse;
import com.privatepe.host.response.DisplayGiftCount.GiftCountResult;
import com.privatepe.host.response.HostIncomeDetail.IncomeDetailResponse;
import com.privatepe.host.response.HostIncomeResponse.IncomeResponse;
import com.privatepe.host.response.NewVideoStatus.NewVideoStatusResponse;
import com.privatepe.host.response.NewWallet.WalletResponceNew;
import com.privatepe.host.response.NewZegoTokenResponse;
import com.privatepe.host.response.Otptwillow.OtpTwillowResponce;
import com.privatepe.host.response.Otptwillow.OtpTwillowVerifyResponse;
import com.privatepe.host.response.RecentActiveHostModel;
import com.privatepe.host.response.ReportResponse;
import com.privatepe.host.response.SettlementCenter.HostSettlementDateResponse;
import com.privatepe.host.response.SettlementDate.SettlementHostWeeklyResponse;
import com.privatepe.host.response.TopReceiver.TopReceiverResponse;
import com.privatepe.host.response.UdateAccountResponse;
import com.privatepe.host.response.UserListResponse;
import com.privatepe.host.response.VideoPlayResponce;
import com.privatepe.host.response.accountvarification.CheckFemaleVarifyResponse;
import com.privatepe.host.response.daily_weekly.DailyWeeklyEarningDetail;
import com.privatepe.host.response.daily_weekly.WeeklyUserListResponse;
import com.privatepe.host.response.chat_price.PriceListResponse;
import com.privatepe.host.response.chat_price.UpdateCallPriceResponse;
import com.privatepe.host.response.daily_weekly.WeeklyUserRewardResponse;
import com.privatepe.host.response.metend.AdapterRes.UserListResponseMet;
import com.privatepe.host.response.metend.AddRemoveFavResponse;
import com.privatepe.host.response.metend.Ban.BanResponce;
import com.privatepe.host.response.metend.CreatePaymentResponse;
import com.privatepe.host.response.metend.DirectUPI.RazorpayPurchaseResponse;
import com.privatepe.host.response.metend.DiscountedRecharge.DiscountedRechargeResponse;
import com.privatepe.host.response.metend.FirstTimeRechargeListResponse;
import com.privatepe.host.response.metend.FollowingUsers;
import com.privatepe.host.response.metend.GenerateCallResponce.GenerateCallResponce;
import com.privatepe.host.response.metend.PaymentGatewayDetails.CashFree.CFToken.CfTokenResponce;
import com.privatepe.host.response.metend.PaymentGatewayDetails.CashFree.CashFreePayment.CashFreePaymentRequest;
import com.privatepe.host.response.metend.PaymentGatewayDetails.PaymentGatewayResponce;
import com.privatepe.host.response.metend.PaymentSelector.PaymentSelectorResponce;
import com.privatepe.host.response.metend.PaytmDirect.PaytmResponse;
import com.privatepe.host.response.metend.RechargePlan.RechargePlanResponseNew;
import com.privatepe.host.response.metend.RemainingGiftCard.RemainingGiftCardResponce;
import com.privatepe.host.response.metend.gift.GiftEmployeeResult;
import com.privatepe.host.response.metend.new_notifications.NewNotificationResponse;
import com.privatepe.host.response.metend.store.response.purchase.BuyStoreItemResponse;
import com.privatepe.host.response.metend.store.response.use_or_remove.UseOrRemoveItemResponse;
import com.privatepe.host.response.metend.store_list.StoreResponse;
import com.privatepe.host.response.newgiftresponse.NewGiftListResponse;
import com.privatepe.host.response.nippyResponse.NippyModel;
import com.privatepe.host.response.sub_agency.SubAgencyResponse;
import com.privatepe.host.response.temporary_block.TemporaryBlockResponse;
import com.privatepe.host.response.trading_response.GetTradingUserNameResponse;
import com.privatepe.host.response.trading_response.TradingAccountResponse;
import com.privatepe.host.response.trading_response.TradingHistoryResponse;
import com.privatepe.host.response.trading_response.TradingTransferModel;
import com.privatepe.host.response.trading_response.TransferTradeAccountResponse;
import com.privatepe.host.response.trading_response.UpdateHistoryModel;
import com.privatepe.host.response.trading_response.UpdateTransferDetailResponse;
import com.privatepe.host.response.trading_response.UserIdBodyModel;

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

    @Multipart
    @POST("update-album-profile")
    Call<Object> updateProfileDetailsAlbumNew(@Header("Authorization") String token,
                                              @Header("Accept") String accept,
                                              @Part MultipartBody.Part[] album_pic);

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
    Call<com.privatepe.host.response.metend.UserListResponseNew.UserListResponseNewData> getProfileDataNew(@Header("Authorization") String token, @Header("Accept") String accept, @Query("q") String q,
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
    Call<VideoResponce> sendVideo(@Header("Authorization") String token,
                                  @Header("Accept") String accept,
                                  @Part("type") RequestBody type,
                                  @Part MultipartBody.Part vdo);

    @Multipart
    @POST("upload-video-via-mobile")
    Call<VideoResponce> sendVideo(@Header("Authorization") String token,
                                  @Header("Accept") String accept,
                                  @Part MultipartBody.Part vdo);

    @GET("livebroadcastList")
        // @GET("userlistdemo")
    Call<com.privatepe.host.model.UserListResponse> getUserList(@Header("Authorization") String token, @Header("Accept") String accept, @Query("q") String q,
                                                                @Query("page") String p, @Query("per_page_records") String lim, @Query("language_id") String lanid);

    @GET("livebroadcastList")
    Call<UserListResponseMet> getUserList2(@Header("Authorization") String token, @Header("Accept") String accept, @Query("q") String q,
                                           @Query("page") String p, @Query("per_page_records") String lim, @Query("language_id") String lanid);

    @GET("livebroadcastList")
        // @GET("userlistdemo")
    Call<UserListResponseMet> getUserListNew(@Header("Authorization") String token, @Header("Accept") String accept, @Query("q") String q,
                                             @Query("page") String p, @Query("per_page_records") String lim, @Query("language_id") String lanid);

    @GET("getNearbyList")
    Call<com.privatepe.host.model.UserListResponse> getNearbyList(@Header("Authorization") String token, @Header("Accept") String accept, @Query("q") String q,
                                                                  @Query("page") String p, @Query("per_page_records") String lim, @Query("language_id") String lanid);

    @GET("getPopularList")
    Call<com.privatepe.host.model.UserListResponse> getPopulartList(@Header("Authorization") String token, @Header("Accept") String accept, @Query("q") String q,
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

    @POST("deletelivebroadcast")
    Call<Deletelivebroadresponse> deleteBroadList(@Header("Authorization") String token);

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
    Call<GenerateCallResponce> getDailCallRequestZ(@Header("Authorization") String token,
                                                   @Header("Accept") String accept
    );

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

    @GET("wallet-history-latest-new")
    Call<WalletResponceNew> getWalletHistoryNew(@Header("Authorization") String token, //@Header("Accept") String accept,
                                                @Query("start_date") String start_Date,
                                                @Query("end_date") String end_Date,
                                                @Query("type") String type,
                                                @Query("page") int page);

    @GET("get-weekly-top-performance-userlist")
    Call<DailyUserListResponse> getDailyEarningUsers(@Header("Authorization") String token, @Query("interval") String interval);

    @GET("get-weekly-top-performance-userlist")
    Call<WeeklyUserListResponse> getWeeklyEarningUsers(@Header("Authorization") String token, @Query("interval") String interval);

    @GET("gettodayweeklyearningdetails")
    Call<DailyWeeklyEarningDetail> getTodayEarningDetail(@Header("Authorization") String token);

    @GET("get-daily-weekly-reward-list")
    Call<WeeklyUserRewardResponse> getWeeklyRewards(@Header("Authorization") String token);

    @GET("call-history")
    Call<CallDetailResponse> getCallDetail(@Header("Authorization") String token, @Query("page") String page);
    @GET("top-fans-user-list")
    Call<MyTopFansModel> getTopFanUserList(@Header("Authorization") String token, @Query("page") int page);

    @GET("getfollowFollowerCount")
    Call<FollowersModelClass> getFollowers(@Header("Authorization") String token,
                                           @Query("follow_type")  int type, @Query("page") int page);

    @POST("getofflinemessageList")
    Call<AutoMessageResponse> getOfflineMessageListData(@Header("Authorization") String token, @Body AutoMessageRequest userid);

    @POST("getofflinemessageListlatest")
    Call<AutoMessageNewResponse> getOfflineMessageListDataNew(@Header("Authorization") String token,
                                                              @Body AutoMessageRequest userid);

    @FormUrlEncoded
    @POST("add-referal")
    Call<AddReferralCardResponse> addReferralCards(@Header("Authorization")String token,
                                                   @Field("profile_id") String profileId,
                                                   @Field("myhaskey") String myhaskey
    );
    @GET("getMonthlyReferalRewardList")
    Call<InvitationRewardReponse> getInviteRewardsData(@Header("Authorization")String token,
                                                       @Query("page") int page);

    @GET("recent-active-host-list")
    Call<RecentActiveHostModel> recentActiveHost(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("createpaymenthaodapay")
    Call<HaodaPayModel> getHaodaPay(@Header("Authorization") String token,
                                    @Field("plan_id") int plan_id);

    @FormUrlEncoded
    @POST("nippy-init")
    Call<NippyModel> getNippy(@Header("Authorization") String token,
                              @Field("plan_id") String plan_id,
                              @Field("user_name") String user_name);

    @GET("payment-gateway-list")
    Call<PaymentGatewayModel> getPaymentGateway();
}