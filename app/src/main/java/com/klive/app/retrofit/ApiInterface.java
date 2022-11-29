package com.klive.app.retrofit;


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
import com.klive.app.model.Authenticate;
import com.klive.app.model.EndCallData.EncCallResponce;
import com.klive.app.model.EndCallData.EndCallData;
import com.klive.app.model.Login;
import com.klive.app.model.LoginResponse;
import com.klive.app.model.OnlineStatusResponse;
import com.klive.app.model.ProfileDetailsResponse;
import com.klive.app.model.UserListResponseNew.UserListResponseNewData;
import com.klive.app.model.city.CityResponse;
import com.klive.app.model.fcm.MyResponse;
import com.klive.app.model.fcm.Sender;
import com.klive.app.model.gift.ResultGift;
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
import com.klive.app.response.NewZegoTokenResponse;
import com.klive.app.response.ReportResponse;
import com.klive.app.response.SettlementCenter.HostSettlementDateResponse;
import com.klive.app.response.SettlementDate.SettlementHostWeeklyResponse;
import com.klive.app.response.TopReceiver.TopReceiverResponse;
import com.klive.app.response.UdateAccountResponse;
import com.klive.app.response.UserListResponse;
import com.klive.app.response.VideoPlayResponce;
import com.klive.app.response.accountvarification.CheckFemaleVarifyResponse;
import com.klive.app.response.newgiftresponse.NewGiftListResponse;
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

import org.json.JSONObject;

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
                                  @Field("password") String password);

    @FormUrlEncoded
    @POST("loginlocalmobile")
    Call<LoginResponse> loginUserMobile(@Field("mobile") String username,
                                        @Field("device_id") String password,
                                        @Field("myhaskey") String hash);


    @GET("getbannerList")
    Call<BannerResponse> getBannerData(@Header("Authorization") String token, @Header("Accept") String accept, @Query("type") String type);

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


    @Multipart
    @POST("hostregisterAfterProfileData")
    Call<SubmitResponse> updateProfileDetails(@Header("Authorization") String token,
                                              @Header("Accept") String accept,
                                              @Part("agency_id") RequestBody agency_id,
                                              @Part("name") RequestBody name,
                                              @Part("dob") RequestBody age,
                                              @Part("city") RequestBody city,
                                              @Part("language_id") RequestBody language,
                                              @Part MultipartBody.Part picToProfile,
                                              @Part MultipartBody.Part[] picToAlbum);

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
    Call<FcmTokenResponse> registerFcmToken(@Header("Authorization") String token, @Header("Accept") String accept, @Field("token") String fcmToken,@Field("app_version") String AppVersion);


    @Headers({"Content-Type:application/json", "Authorization:key=AAAA_HjaAC8:APA91bGL2g1vHs9QoazpxJyn4JDSm22-Ooupn88VjqGaNjEKGVF4VvA2ShiGn8TOvGhNC_uS_GawxZcn2-BmU-JZoUK9JvWoLxtkWW8067yzdlClgYK44PwYMFreGdaqhAFpvvWXJnZz"})
    @POST("fcm/send")
    Call<MyResponse> sendNotificationInBox(@Body Sender sender);


    @Multipart
    @POST("upload-video-via-mobile")
    Call<VideoResponce> sendVideo(@Header("Authorization") String token, @Header("Accept") String accept, @Part MultipartBody.Part vdo);

    @GET("userlistdemo")
    Call<com.klive.app.model.UserListResponse> getUserList(@Header("Authorization") String token, @Header("Accept") String accept, @Query("q") String q,
                                                           @Query("page") String p, @Query("per_page_records") String lim, @Query("language_id") String lanid);

    @GET("getNearbyList")
    Call<com.klive.app.model.UserListResponse> getNearbyList(@Header("Authorization") String token, @Header("Accept") String accept, @Query("q") String q,
                                                             @Query("page") String p, @Query("per_page_records") String lim, @Query("language_id") String lanid);

    @GET("getPopularList")
    Call<com.klive.app.model.UserListResponse> getPopulartList(@Header("Authorization") String token, @Header("Accept") String accept, @Query("q") String q,
                                                               @Query("page") String p, @Query("per_page_records") String lim, @Query("language_id") String lanid);

    //agency & subagency
    @GET("agencyhostprofilelist")
    Call<UserListResponse> getUserList(@Header("Authorization") String token, @Header("Accept") String accept, @Query("page") String p);


    @GET("agencyhostprofilelist")
    Call<UserListResponse> searchUserList(@Header("Authorization") String token, @Header("Accept") String accept, @Query("q") String search);



    @GET("agencycommissionList")
    Call<AgencyPolicyResponse> getAgencyList(@Header("Authorization") String token, @Header("Accept") String accept);

    @GET("subagencylist")
    Call<SubAgencyResponse> getSubAgencyList(@Header("Authorization") String token, @Header("Accept") String accept,@Query("q") String type);

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
    Call<Object> profileVideoDelete(@Header("Authorization") String token, @Header("Accept") String accept,@Query("video_id") String video_id);

    @GET("getProfileImageReview")
    Call<UpdateProfileNewResponse> getProfileImageReview(@Header("Authorization") String token);


    @GET("getcategoryGifts")
    Call<NewGiftListResponse> getCategoryGifts();

}