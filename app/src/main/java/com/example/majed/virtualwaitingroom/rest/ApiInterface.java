package com.example.majed.virtualwaitingroom.rest;

import com.example.majed.virtualwaitingroom.ViewModel.UserInformation;
import com.example.majed.virtualwaitingroom.model.Doctor;
import com.example.majed.virtualwaitingroom.model.OnlineCallStatus;
import com.example.majed.virtualwaitingroom.model.OnlineConversation;
import com.example.majed.virtualwaitingroom.model.OnlineStatus;
import com.example.majed.virtualwaitingroom.model.SignIn;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by majed on 7/24/2017.
 */

public interface  ApiInterface {
    @POST("waitingroom/login")
    Call<UserInformation> getSignin(@Body SignIn signInModel);
    @GET("waitingroom/doctorList")
    Call<List<Doctor>> getDoctorList();
    @POST("waitingroom/UpdateStatus")
    Call<Boolean> SaveUpdatedStatus(@Body OnlineConversation conversation);

    @GET("waitingroom/Notifications/{doctorId}")
    Call<List<OnlineConversation>> getNotifications(@Path("doctorId") String doctorId);

    @POST("waitingroom/CallStatus")
    Call<Boolean> UpdateCallStatus(@Body OnlineCallStatus callStatus);

    @POST("waitingroom/OnlineStatus")
    Call<OnlineStatus> UpdateOnlineStatus(@Body OnlineStatus onlineStatus);
}