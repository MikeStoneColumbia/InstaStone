package com.example.instastone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button loginButton;
    private Button signUpButton;
    private ImageView instaLogo;


    private TextWatcher textInfo = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            checkEmpty();

        }
    }; //Watches to see if anything has been typed in the EditText

    private void checkEmpty(){

        if(username.getText().toString().equals("") || password.getText().toString().equals(""))
            loginButton.setEnabled(false);
        else
            loginButton.setEnabled(true);

    } // Empty edittexts then disable login, else enable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseUser.getCurrentUser().logOut();

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        signUpButton = findViewById(R.id.signUpButton);
        instaLogo = findViewById(R.id.instagramLogo);
        instaLogo.setImageResource(R.drawable.nav_logo_whiteout);

        username.addTextChangedListener(textInfo); //constantly checking to see if something gets typed.
        password.addTextChangedListener(textInfo);

        checkEmpty();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // Check to see if a person is able to login, if not then check login information or other settings.

                String usernameStr = username.getText().toString();
                String passwordStr = password.getText().toString();
                login(usernameStr,passwordStr);

            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                gotToSignUp();

            }
        });

    }

    private void gotToSignUp() { // go to signUp Activity

        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);

    }

    private void login(String usernameStr, String passwordStr) {

        //TODO: move to the new acitvity and loin
        ParseUser.logInInBackground(usernameStr, passwordStr, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) { //login using the username and password user inputed

                if(user != null) {

                    Log.d("Login", "Login was successful"); //login successful then go to activity info
                    goToInfo();
                }

                else{

                    Log.d("Login", "Login has failed"); // something went wrong with signing up
                    Toast.makeText(MainActivity.this, "Login Failed, Check password or username", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();

                }

            }
        });
    }

    private void goToInfo(){

        Intent info = new Intent(this, Info.class);
        startActivity(info);
        finish();

    }
}
