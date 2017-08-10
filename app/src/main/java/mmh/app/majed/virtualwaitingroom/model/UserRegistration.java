package mmh.app.majed.virtualwaitingroom.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by majed on 8/9/2017.
 */

public class UserRegistration {

    @SerializedName("FirstName")
    private String FirstName;
    @SerializedName("LastName")
    private String LastName;
    @SerializedName("Email")
    private String Email;
    @SerializedName("Password")
    private String Password;
    @SerializedName("Role")
    private String Role;
    @SerializedName("IpAddress")
    private String IpAddress;

    public UserRegistration(String firstName, String lastName, String email,  String password, String role,String ipAddress) {
        FirstName = firstName;
        LastName = lastName;
        Email = email;
        Password = password;
        Role = role;
        IpAddress=ipAddress;
    }

    @Override
    public String toString() {
        return "UserRegistration{" +
                "FirstName='" + FirstName + '\'' +
                ", LastName='" + LastName + '\'' +
                ", Email='" + Email + '\'' +
                ", Password='" + Password + '\'' +
                ", Role='" + Role + '\'' +
                ", IpAddress='" + IpAddress + '\'' +
                '}';
    }
}
