package com.example.flashcard4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OtpActivity extends AppCompatActivity {

    String generatedOtp ;
    TextView textView , txtResend;

    Button verifyOtpBtn ;
    EditText edtOtp ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        getSupportActionBar().setElevation(0);

        textView = findViewById(R.id.textView) ;
        verifyOtpBtn = findViewById(R.id.btnVerify) ;
        edtOtp = findViewById(R.id.idEdtOtp) ;
        txtResend = findViewById(R.id.idTxtResend) ;

        String number = getIntent().getStringExtra("number") ;
        generatedOtp = getIntent().getStringExtra("generatedOtp") ;

        textView.setText("Enter the OTP sent to +91" + number);

        // verify OTP Button Clicked
        verifyOtpBtn.setOnClickListener( v ->{

            verifyOtp() ;
        });

        // Resend OTP button Clickec
        txtResend.setOnClickListener(v->{


            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    "+91" + number,
                    60,
                    TimeUnit.SECONDS,
                    OtpActivity.this,
                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {


                        }

                        @Override
                        public void onVerificationFailed(@NonNull FirebaseException e) {

                            Toast.makeText(OtpActivity.this, "OnverificationFailed", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                           generatedOtp = s ;
                            Toast.makeText(OtpActivity.this, "OTP sended succesfully", Toast.LENGTH_SHORT).show();


                        }
                    }
            );



        });


    }

    private void verifyOtp() {

        if(!edtOtp.getText().toString().trim().isEmpty()){

            if(generatedOtp != null){ // otp notnull-----------------


                PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                        generatedOtp, edtOtp.getText().toString().trim()
                );
                FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){
                                    Intent intent = new Intent(OtpActivity.this, MainActivity.class) ;
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK) ;
                                    Toast.makeText(OtpActivity.this, "Welcome!", Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                    
                                }
                                else{
                                    Toast.makeText(OtpActivity.this, "Enter the correct Otp! task unsuccesful", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }) ;




                //-------------------------------


            }
            else{
                Toast.makeText(this, "Please check internet connection", Toast.LENGTH_SHORT).show();
            }

        }
        else {
            Toast.makeText(this, "Enter OTP !", Toast.LENGTH_SHORT).show();
        }


    }




}