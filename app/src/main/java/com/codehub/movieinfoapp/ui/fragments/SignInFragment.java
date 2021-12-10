package com.codehub.movieinfoapp.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.codehub.movieinfoapp.MainActivity;
import com.codehub.movieinfoapp.R;
import com.codehub.movieinfoapp.common.AbstractFragment;
import com.codehub.movieinfoapp.ui.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;

public class SignInFragment extends AbstractFragment {
    TextInputLayout username;
    TextInputLayout password;
    EditText email_text;
    EditText password_text;
    TextView forgot_pwd;
    Button login_btn;
    FirebaseAuth mAuth;


    @Override
    public int getLayoutRes() {
        return R.layout.fragment_sign_in;
    }

    @Override
    public void startOperations(View view) {
        mAuth = FirebaseAuth.getInstance();

//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        username = view.findViewById(R.id.textField_email);
        password = view.findViewById(R.id.textField_pwd);
        forgot_pwd = view.findViewById(R.id.login_forgotten);
        login_btn = view.findViewById(R.id.login_btn);
        email_text = view.findViewById(R.id.signIn_email_text);
        password_text = view.findViewById(R.id.signIn_password_text);
        username.setTranslationX(1000);
        password.setTranslationX(1000);
        forgot_pwd.setTranslationX(1000);
        login_btn.setTranslationX(1000);

        username.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(100).start();
        password.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        forgot_pwd.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        login_btn.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyFieldChecks();
//                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        forgot_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();

            }
        });
    }

    @Override
    public void stopOperations() {

    }

    @Override
    public void onDestroy() {
        ((LoginActivity)getActivity()).hideIndicator();
        super.onDestroy();
    }

    private void applyFieldChecks(){
        String email = email_text.getText().toString();
        String password = password_text.getText().toString();

        if(!email.isEmpty() && !password.isEmpty()){
           signInUser();
        }else{
            Toast.makeText(getActivity(), "Fields can't be empty",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void signInUser(){
        ((LoginActivity)getActivity()).showIndicator();
        mAuth.signInWithEmailAndPassword(email_text.getText().toString(), password_text.getText().toString())
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user.isEmailVerified()){

                                Intent intent = new Intent(getContext(), MainActivity.class);
                                startActivity(intent);
                                if(getActivity()!=null){
                                    getActivity().finish();
                                }

                            }else{
                                ((LoginActivity)getActivity()).hideIndicator();
                                Snackbar snackbar = Snackbar.make(getView(),"Please verify your email",
                                        Snackbar.LENGTH_LONG);
                                snackbar.addCallback(new Snackbar.Callback(){
                                    @Override
                                    public void onDismissed(Snackbar transientBottomBar, int event) {
                                        super.onDismissed(transientBottomBar, event);
                                            mAuth.signOut();
                                            System.out.println("USER SIGNED OUT");

                                    }
                                });
                                snackbar.setAction("Send again", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        user.sendEmailVerification()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Snackbar.make(getView(),"A verification link will be sent to your email account in the next 5 minutes. Check your emails!",
                                                                    Snackbar.LENGTH_LONG).show();
                                                            LoginActivity.viewPager.setCurrentItem(0,true);
                                                        }
                                                        else
                                                        {
                                                            Toast.makeText(getContext(), "Unable to send verification email", Toast.LENGTH_SHORT).show();

                                                        }
                                                    }
                                                });

                                    }
                                });
                                snackbar.show();
                            }

                        } else {
                            ((LoginActivity)getActivity()).hideIndicator();
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), task.getException().getLocalizedMessage(),
                                    Toast.LENGTH_LONG).show();

                        }
                    }
                });

    }

    public void resetPassword(){
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.forgot_pass_dialog, null);
        Button reset = dialogView.findViewById(R.id.reset_btn);
        EditText email_text = dialogView.findViewById(R.id.editText_forgot_pass);
        ImageView close_dialog_btn = dialogView.findViewById(R.id.close_dialog);
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setView(dialogView);
        Dialog dialog = alert.create();
        dialog.show();
//        View view = inflater.inflate(R.layout.forgot_pass_dialog, null);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = email_text.getText().toString();
                if(email.isEmpty()){
                    Toast.makeText(getActivity(), "Email can't be empty",
                            Toast.LENGTH_SHORT).show();
                }else {
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                dialog.cancel();
                                Log.d(TAG, "Email sent.");
//                                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                Snackbar.make(getView(), "Reset password link sent to your email.Check your emails!",
                                        Snackbar.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity(), task.getException().getLocalizedMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
        close_dialog_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });






    }



}