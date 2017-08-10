package mmh.app.majed.virtualwaitingroom.rest;

import mmh.app.majed.virtualwaitingroom.ViewModel.RegisterUserInformation;
import mmh.app.majed.virtualwaitingroom.ViewModel.UserInformation;
import mmh.app.majed.virtualwaitingroom.model.Doctor;
import mmh.app.majed.virtualwaitingroom.model.OnlineCallStatus;
import mmh.app.majed.virtualwaitingroom.model.OnlineConversation;
import mmh.app.majed.virtualwaitingroom.model.OnlineStatus;
import mmh.app.majed.virtualwaitingroom.model.SignIn;

import java.util.List;

import mmh.app.majed.virtualwaitingroom.model.UserRegistration;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by majed on 7/24/2017.
 */

public interface  ApiInterface {

    @POST("waitingroom/login")
    Call<UserInformation> getSignin(@Body SignIn signInModel);

    @GET("waitingroom/LogOut/{contactId}")
    Call<Boolean> logOut(@Path("contactId") String contactId);

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

    @GET("waitingroom/NotificationCount/{doctorId}")
    Call<Integer> UnReadNotificationCount(@Path("doctorId") String doctorId);

    @POST("waitingroom/Register")
    Call<RegisterUserInformation> UserRegistration(@Body UserRegistration userRegistration);


}