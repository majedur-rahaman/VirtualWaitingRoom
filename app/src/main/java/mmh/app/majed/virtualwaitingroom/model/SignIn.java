package mmh.app.majed.virtualwaitingroom.model;

/**
 * Created by majed on 7/24/2017.
 */

public class SignIn {
    private String Email;
    private String Password;

    public SignIn() {}

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    @Override
    public String toString() {
        return "SignIn{" +
                "Email='" + Email + '\'' +
                ", Password='" + Password + '\'' +
                '}';
    }
}
