package com.codehub.movieinfoapp.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

import com.codehub.movieinfoapp.MainActivity;
import com.codehub.movieinfoapp.R;
import com.codehub.movieinfoapp.common.AbstractFragment;
import com.codehub.movieinfoapp.ui.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.service.controls.ControlsProviderService.TAG;

public class SignUpFragment extends AbstractFragment {
    private EditText emailText;
    private EditText passWordText;
    private EditText passwordRetypeText;
    private Button singUpBtn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fireStoreDb;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_sign_up;
    }

    @Override
    public void startOperations(View view) {
        fireStoreDb = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        emailText = view.findViewById(R.id.editText_email);
        passWordText = view.findViewById(R.id.editText_password);
        passwordRetypeText = view.findViewById(R.id.editText_password_retype);
        singUpBtn = view.findViewById(R.id.signUp_btn);
        singUpBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                applyFieldChecks();
            }
        });

    }

    @Override
    public void stopOperations() {

    }
    private void signUpUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user!=null){
                                addUserToFireStore(email);
                                sendVerificationEmail(user);
                            }
//                            Intent intent = new Intent(getContext(), MainActivity.class);
//                            startActivity(intent);
//                            if(getActivity()!=null){
//                                getActivity().finish();
//                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), task.getException().getLocalizedMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                        ((LoginActivity)getActivity()).hideIndicator();
                    }
                });


    }

    private void applyFieldChecks(){
        String email = emailText.getText().toString();
        String password = passWordText.getText().toString();
        String passwordRetype = passwordRetypeText.getText().toString();

        if(!email.isEmpty() && !password.isEmpty() && !passwordRetype.isEmpty()){
            if(password.equals(passwordRetype)){
                ((LoginActivity)getActivity()).showIndicator();
                signUpUser(email,password);

            }else{
                Toast.makeText(getActivity(), "Passwords don't match",
                        Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getActivity(), "Fields can't be empty",
                    Toast.LENGTH_LONG).show();
        }

    }

    private void clearAllFields(){
        emailText.setText("");
        passWordText.setText("");
        passwordRetypeText.setText("");
    }

    private void sendVerificationEmail(FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mAuth.signOut();
                            Snackbar.make(getView(),"A verification link will be sent to your email account in the next 5 minutes. Check your emails!",
                                    Snackbar.LENGTH_LONG).show();
                            LoginActivity.viewPager.setCurrentItem(0,true);
                            clearAllFields();
                        }
                        else
                        {
                            Toast.makeText(getContext(), "Unable to send verification email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void addUserToFireStore(String email){
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("favouriteMovies",new ArrayList<>());

        // Add a new document with a generated ID
        fireStoreDb.collection("users").document(FirebaseAuth.getInstance().getUid()).set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "DocumentSnapshot added with ID: ");
                    }
                });

    }
}
