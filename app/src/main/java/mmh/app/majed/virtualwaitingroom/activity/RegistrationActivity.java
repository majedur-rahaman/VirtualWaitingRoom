package mmh.app.majed.virtualwaitingroom.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import mmh.app.majed.virtualwaitingroom.R;
import mmh.app.majed.virtualwaitingroom.Session.SessionManager;
import mmh.app.majed.virtualwaitingroom.ViewModel.RegisterUserInformation;
import mmh.app.majed.virtualwaitingroom.model.UserRegistration;
import mmh.app.majed.virtualwaitingroom.rest.ApiClient;
import mmh.app.majed.virtualwaitingroom.rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends Activity {

    //The view objects
    private EditText editTextFirstName, editTextLastName,editTextEmail, editTextConfirmEmail,
            editTextPassword,editTextConfirmPassword;

    private String selectedItem;
    private Spinner typeSpinner;
    private Button buttonSubmit;
    //defining AwesomeValidation object
    private AwesomeValidation awesomeValidation;

    //string valiable

    private String editTextFirstNameStr, editTextLastNameStr,editTextEmailStr, editTextConfirmEmailStr,
            editTextPasswordStr,editTextConfirmPasswordStr, roleStr;

    SessionManager session;
    String ipAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        session = new SessionManager(getApplicationContext());

        typeSpinner = (Spinner) findViewById(R.id.type_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.type_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        typeSpinner.setAdapter(adapter);


        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        //initializing view objects
        editTextFirstName = (EditText) findViewById(R.id.your_first_name);
        editTextLastName = (EditText) findViewById(R.id.your_last_name);
        editTextEmail = (EditText) findViewById(R.id.your_email_address);
        editTextConfirmEmail = (EditText) findViewById(R.id.your_confirm_email_address);
        editTextPassword = (EditText) findViewById(R.id.create_new_password);
        editTextConfirmPassword=(EditText) findViewById(R.id.confirm_new_password);


        //adding validation to edittexts
        awesomeValidation.addValidation(this, R.id.your_first_name, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.your_last_name, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.your_email_address, Patterns.EMAIL_ADDRESS, R.string.nameerror);
       // awesomeValidation.addValidation(this, R.id.type_spinner, "((?!--Select--)$", R.string.typeerror);

        buttonSubmit=(Button) findViewById(R.id.signup);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == buttonSubmit) {
                    submitForm();
                }
            }
        });

    }
    private void submitForm() {
        //first validate the form then move ahead
        //if this becomes true that means validation is successfull
        if (awesomeValidation.validate()) {

             selectedItem = typeSpinner.getSelectedItem().toString();


//            WifiManager wifiMan = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//            WifiInfo wifiInf = wifiMan.getConnectionInfo();
//            int ipAddress = wifiInf.getIpAddress();
//            ipAddresses =URLEncoder.encode( String.format("%d.%d.%d.%d", (ipAddress & 0xff),(ipAddress >> 8 & 0xff),(ipAddress >> 16 & 0xff),(ipAddress >> 24 & 0xff)));

            if(selectedItem.equals("--Select--")){

                Toast.makeText(this, "User type Does not selected", Toast.LENGTH_LONG).show();
            }
            else if(!editTextEmail.getText().toString().equals(editTextConfirmEmail.getText().toString())){

                Toast.makeText(this, "Email Does not match with confirm email", Toast.LENGTH_LONG).show();
            }
            else if(!editTextPassword.getText().toString().equals(editTextConfirmPassword.getText().toString())){
                Toast.makeText(this, "Password Does not match with confirm password" + selectedItem, Toast.LENGTH_LONG).show();
            }
            else{

             //   Toast.makeText(this, "Validation Successful", Toast.LENGTH_LONG).show();

                Registration();
            }

        }
    }

    private void Registration(){



        editTextFirstNameStr = editTextFirstName.getText().toString();
        editTextLastNameStr=editTextLastName.getText().toString();
        editTextEmailStr=editTextEmail.getText().toString();
        editTextConfirmEmailStr=editTextConfirmEmail.getText().toString();
        editTextPasswordStr=editTextPassword.getText().toString();
        editTextConfirmPasswordStr=editTextConfirmPassword.getText().toString();
        roleStr=selectedItem;
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        ipAddress = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        UserRegistration userRegistration= new UserRegistration(editTextFirstNameStr,editTextLastNameStr,
                editTextEmailStr,editTextPasswordStr,roleStr,ipAddress);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<RegisterUserInformation> call = apiService.UserRegistration(userRegistration);

        call.enqueue(new Callback<RegisterUserInformation>() {
            @Override
            public void onResponse(Call<RegisterUserInformation> call, Response<RegisterUserInformation> response) {

                RegisterUserInformation userInformation = response.body();

                if(userInformation!=null){
                    if(userInformation.Role=="1"){
                        session.createLoginSession(userInformation.FullName,907660001,userInformation.ContactId,"Doctor");
                    }else {
                        session.createLoginSession(userInformation.FullName,907660001,userInformation.ContactId,"Patient");
                    }

                        Toast.makeText(RegistrationActivity.this, "You successfully Registered", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        finish();
                }else{
                    Toast.makeText(getApplicationContext(),"please Try again!",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterUserInformation> call, Throwable t) {

            }
        });

    }




}
