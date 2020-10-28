package com.example.ecobeauty.authorization;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecobeauty.main.MainActivity;
import com.example.ecobeauty.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin, btnReset;

    private ImageView lipstickImageView;
    private ImageView eyeshadowImageView;
    private ImageView blushImageView;
    private ImageView brushImageView;
    private ImageView mascaraImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }


        setContentView(R.layout.activity_login);


        lipstickImageView =  findViewById(R.id.lipstick);
        eyeshadowImageView =  findViewById(R.id.eyeshadow);
        blushImageView = findViewById(R.id.blush);
        brushImageView = findViewById(R.id.brush);
        mascaraImageView =  findViewById(R.id.mascara);

        Animation motionUpLipstickAnimation = AnimationUtils.loadAnimation(this, R.anim.motion_up_lipstick);
        Animation motionUpEyeshadowAnimation = AnimationUtils.loadAnimation(this, R.anim.motion_up_eyeshadow);
        Animation motionUpBlushAnimation = AnimationUtils.loadAnimation(this, R.anim.motion_up_blush);
        Animation motionUpBrushAnimation = AnimationUtils.loadAnimation(this, R.anim.motion_up_brush);
        Animation motionUpMascaraAnimation = AnimationUtils.loadAnimation(this, R.anim.motion_up_mascara);


        lipstickImageView.startAnimation(motionUpLipstickAnimation);
        eyeshadowImageView.startAnimation(motionUpEyeshadowAnimation);
        blushImageView.startAnimation(motionUpBlushAnimation);
        brushImageView.startAnimation(motionUpBrushAnimation);
        mascaraImageView.startAnimation(motionUpMascaraAnimation);
        inputEmail = (TextInputEditText) findViewById(R.id.email);
        inputPassword = (TextInputEditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = (Button) findViewById(R.id.btn_reset_password);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.robotoMedium)));
        btnReset.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.robotoMediumItalic)));
        btnSignup.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.robotoMedium)));
        inputEmail.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.robotoRegular)));


                auth = FirebaseAuth.getInstance();

        btnSignup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.inputEmail), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.inputPassword), Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);


                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {

                                    if (password.length() < 6) {
                                        inputPassword.setError(getString(R.string.minimumPassword));
                                    } else {
                                        Toast.makeText(LoginActivity.this, getString(R.string.authFailed), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });
    }
}
