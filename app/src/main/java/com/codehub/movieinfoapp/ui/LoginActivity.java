package com.codehub.movieinfoapp.ui;

import android.content.Intent;
import android.service.controls.ControlsProviderService;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.codehub.movieinfoapp.MainActivity;
import com.codehub.movieinfoapp.common.AbstractActivity;
import com.codehub.movieinfoapp.R;
import com.codehub.movieinfoapp.adapters.VPAdapter;
import com.codehub.movieinfoapp.ui.fragments.SignInFragment;
import com.codehub.movieinfoapp.ui.fragments.SignUpFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class LoginActivity extends AbstractActivity {
    private TabLayout tabLayout;
    public static ViewPager2 viewPager;
    private FirebaseAuth mAuth;
    private final String[] namesList ={"Sign In","Sign Up"};
    FloatingActionButton  google_btn,facebook_btn;
    ImageView app_logo;
    TextView welcome_text;
    public ProgressBar circular_indicator;
    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1;
     boolean  keepState = false;
    private FirebaseFirestore fireStoreDb;

    @Override
    public int getLayoutRes() {
        return R.layout.login;
    }

    @Override
    public void startOperations() {
        fireStoreDb = FirebaseFirestore.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            if(currentUser.isEmailVerified()){
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

        }

        //                LoginActivity .this.getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        app_logo = findViewById(R.id.app_logo_icon);
        welcome_text = findViewById(R.id.login_title);
        tabLayout = findViewById(R.id.email_Tabs);
        circular_indicator = findViewById(R.id.login_progress_indicator);
        viewPager = findViewById(R.id.Vpager);
        facebook_btn = findViewById(R.id.facebook_fab);
        google_btn = findViewById(R.id.google_fab);
        viewPager.setUserInputEnabled(false);//disable tabviews scroll
        if(!keepState) {
        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(),getLifecycle());
        vpAdapter.addFragment(new SignInFragment());
        vpAdapter.addFragment(new SignUpFragment());
        viewPager.setAdapter(vpAdapter);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(namesList[position])
        ).attach();

            app_logo.setTranslationY(-300);
            welcome_text.setTranslationX(1000);
            google_btn.setTranslationY(300);
            facebook_btn.setTranslationY(300);
            tabLayout.setTranslationX(1000);
            app_logo.animate().translationY(0).alpha(1).setDuration(1200).setStartDelay(100).start();
            welcome_text.animate().translationX(0).alpha(1).setDuration(1200).setStartDelay(1000).start();
            google_btn.animate().translationY(0).alpha(1).setDuration(1200).setStartDelay(400).start();
            facebook_btn.animate().translationY(0).alpha(1).setDuration(1200).setStartDelay(600).start();
            tabLayout.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        }
        google_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithGoogle();
            }
        });

        keepState = true;

    }

    @Override
    public void stopOperations() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void showIndicator(){
        circular_indicator.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
    public void hideIndicator() {
        circular_indicator.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            showIndicator();
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }



    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnSuccessListener(this, authResult -> {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    boolean isNew = authResult.getAdditionalUserInfo().isNewUser(); //check if it't first time sign in with google in order to avoid adding him again to firestore, thus overwrite favourites data
                    if(isNew) {
                        addGoogleUserToFireStore(acct.getEmail());
                    }
                    finish();
                })
                .addOnFailureListener(this, e -> Toast.makeText(LoginActivity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show());
    }



    public void addGoogleUserToFireStore(String email){
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("favouriteMovies",new ArrayList<>());

        // Add a new document with a generated ID
        fireStoreDb.collection("users").document(FirebaseAuth.getInstance().getUid()).set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(ControlsProviderService.TAG, "DocumentSnapshot added with ID: ");
                    }
                });

    }

}