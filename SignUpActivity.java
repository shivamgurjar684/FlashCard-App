package com.example.flashcard4;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SignUpActivity extends AppCompatActivity {

    Button btnSignup ;
    EditText edtName, edtEmail, edtPhone ;
    TextView txtSignin ;
    ProgressBar progressBar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        getSupportActionBar().setElevation(0);

        txtSignin = findViewById(R.id.idTxtSignIn) ;
        btnSignup = findViewById(R.id.idBtnSignup) ;
        edtName = findViewById(R.id.idEdtNameIn);
        edtEmail = findViewById(R.id.idEdtEmailIn);
        edtPhone = findViewById(R.id.idEdtPhone);
        progressBar = findViewById(R.id.idPb);

        txtSignin.setOnClickListener(v -> {

            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(intent);

        }) ;

        btnSignup.setOnClickListener(v -> {



            if(!edtName.getText().toString().trim().isEmpty()){  // Name check


                String mailId = edtEmail.getText().toString() ;
                StringBuilder sb = new StringBuilder(mailId) ;

                if(sb.indexOf("@")!=-1){  // Mail check

                    if(edtPhone.getText().toString().length() == 10){  // Phone check

                        progressBar.setVisibility(View.VISIBLE);
                        btnSignup.setVisibility(View.INVISIBLE);

                        // Sending OTp.....


                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                "+91" + edtPhone.getText().toString(),
                                60,
                                TimeUnit.SECONDS,
                                SignUpActivity.this,
                                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    @Override
                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                        progressBar.setVisibility(View.GONE);
                                        btnSignup.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onVerificationFailed(@NonNull FirebaseException e) {
                                        progressBar.setVisibility(View.GONE);
                                        btnSignup.setVisibility(View.VISIBLE);
                                        Toast.makeText(SignUpActivity.this, "OnverificationFailed", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                                        progressBar.setVisibility(View.GONE);
                                        btnSignup.setVisibility(View.VISIBLE);

                                        Intent intent = new Intent(SignUpActivity.this, OtpActivity.class) ;
                                        intent.putExtra("number", edtPhone.getText().toString().trim()) ;
                                        intent.putExtra("generatedOtp", s) ;
                                        startActivity(intent);



                                    }
                                }
                        );



                        // --------------------------------------------------------------------




                    }
                    else{
                        Toast.makeText(this, "Enter correct mobile number", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    Toast.makeText(this, "Enter correct mail id", Toast.LENGTH_SHORT).show();
                }



            }
            else{
                Toast.makeText(this, "Enter the name", Toast.LENGTH_SHORT).show();
            }





        }) ;







    }
}