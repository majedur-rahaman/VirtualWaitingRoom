package mmh.app.majed.virtualwaitingroom.Session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import mmh.app.majed.virtualwaitingroom.activity.LoginActivity;
import mmh.app.majed.virtualwaitingroom.activity.MainActivity;

import java.util.HashMap;

/**
 * Created by majed on 7/26/2017.
 */

public class SessionManager {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "WaitingRoom";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "FullName";

    // Online Status (make variable public to access from outside)
    public static final String KEY_STATUS = "OnlineStatus";

    // Contact Id (make variable public to access from outside)
    public static final String KEY_CONTACTID = "ContactId";

    // Role (make variable public to access from outside)
    public static final String KEY_ROLE = "Role";

    // Notification Count (make variable public to access from outside)
    public static final String KEY_COUNT = "Count";
    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(String fullName, int onlineStatus,String contactId,String role){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_NAME, fullName);

        // Storing status in pref
        editor.putInt(KEY_STATUS, onlineStatus);
        // Storing status in pref
        editor.putString(KEY_CONTACTID, contactId);
        // Storing Role in pref
        editor.putString(KEY_ROLE, role);
        // commit changes
        editor.commit();
    }

    //Set online status

    public void setUpdatedOnlineStatus(int onlineStatus ){
        editor.putInt(KEY_STATUS, onlineStatus);
        // commit changes
        editor.commit();
    }

    //Set online status

    public void NotificationCount(int count ){
        editor.putInt(KEY_COUNT, count);
        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }
    }

    public void checkLoginSession(){
        // Check login status
        if(this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, MainActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }


    }



    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user status
        user.put(KEY_STATUS, Integer.toString(pref.getInt(KEY_STATUS, 0)));
        // user contact id
        user.put(KEY_CONTACTID, pref.getString(KEY_CONTACTID, null));
        // user role
        user.put(KEY_ROLE, pref.getString(KEY_ROLE, null));

        // user Unread Notifcation Count
        user.put(KEY_COUNT, Integer.toString(pref.getInt(KEY_COUNT, 0)));

        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){

        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();



        // After logout redirect user to Loging Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}
