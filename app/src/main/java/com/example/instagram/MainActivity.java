package com.example.instagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginBtn;
    private Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameInput = findViewById(R.id.etUsername);
        passwordInput = findViewById(R.id.etPassword);
        loginBtn = findViewById(R.id.loginBtn);
        signUpBtn = findViewById(R.id.btnSignUp);

        //if user is already logged in then take them directly to the homepage
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            // show HomeActivity page
            final Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            // show the signup or login screen
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String username = usernameInput.getText().toString();
                    final String password = passwordInput.getText().toString();

                    login(username, password);
                }
            });

            signUpBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //open a new activity that will allow the user to sign
                    //final Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                    //startActivity(intent);
                    //finish();
                    Toast.makeText(MainActivity.this, "Add username and password", Toast.LENGTH_LONG).show();
                    final String username = usernameInput.getText().toString();
                    final String password = passwordInput.getText().toString();

                    ParseUser user = new ParseUser();
                    user.setUsername(username);
                    user.setPassword(password);

                    user.signUpInBackground(new SignUpCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                // Hooray! Let them use the app now
                                login(username, password);
                            } else {
                                // Sign up didn't succeed. Look at the ParseException
                                // to figure out what went wrong
                                Log.e("LoginActivity", "Sign Up failure");
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        }

    }

    private void login(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    Log.d("LoginActivity", "Login successful");
                    final Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("LoginActivity", "Login failure");
                    e.printStackTrace();
                }
            }
        });
    }
}
