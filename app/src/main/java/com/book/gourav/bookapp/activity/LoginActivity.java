package com.book.gourav.bookapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.book.gourav.bookapp.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.CAMERA;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private GoogleApiClient mGoogleSignInClient;
    //private GoogleSignInClient mGoogleSignInClient;
    LinearLayout googleButton;
    private FirebaseAuth mAuth;
    //FirebaseUser user;
    DatabaseReference mref;
    ProgressDialog progressDialog;
    SharedPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );
        progressDialog = new ProgressDialog(LoginActivity.this);
        //open();



        mAuth= FirebaseAuth.getInstance();
       // user=mAuth.getCurrentUser();
        googleButton = (LinearLayout) findViewById(R.id.googlebutton);
        googleButton.setVisibility(View.VISIBLE);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
            
        //mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mGoogleSignInClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                }).addApi( Auth.GOOGLE_SIGN_IN_API,gso).build();
        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNetworkAvaible(LoginActivity.this)) {
                    progressDialog.setMessage("Logging you in");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    googleButton.setVisibility(View.GONE);
                    signIn();
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Check Network Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public boolean isNetworkAvaible(Context context) {
        ConnectivityManager conMan = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        return conMan.getActiveNetworkInfo() != null && conMan.getActiveNetworkInfo().isConnected();
    }


    public void signIn()
    {

        mGoogleSignInClient.clearDefaultAccountAndReconnect();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleSignInClient);

        startActivityForResult(signInIntent, RC_SIGN_IN);

//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//
//        startActivityForResult(signInIntent,RC_SIGN_IN);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      //  Toast.makeText(this, data.toString(), Toast.LENGTH_SHORT).show();
                    // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                progressDialog.dismiss();
                googleButton.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                // ...
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        pref=this.getSharedPreferences("firebase", Context.MODE_PRIVATE);
        Integer login=pref.getInt("login",0);
       FirebaseDatabase.getInstance().setPersistenceEnabled(true);


       if(login.equals(1)) {

           Intent intent = new Intent(LoginActivity.this,Main2Activity.class);
            startActivity(intent);


        }
        updateUI(currentUser);

    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            pref.edit().putInt("login",1).apply();

            progressDialog.dismiss();
            Intent intent = new Intent(LoginActivity.this,Main2Activity.class);
            startActivity(intent);

        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
      //  Toast.makeText(this, acct.getEmail().toString(), Toast.LENGTH_SHORT).show();

            mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
//                            progressDialog.dismiss();
                            upload();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });

        }

//


    private void upload() {
        String user=mAuth.getCurrentUser().getUid().toString();
        mref= FirebaseDatabase.getInstance().getReference()
                .child("user");
        mref.child(user).setValue("1");






    }

    public void open()
    {
        if(checkPermission())
        {
            }
        else
        {
            requestPermission();
        }
    }
    public Boolean checkPermission()
    {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),INTERNET);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(),WRITE_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED;
    }

    public  void requestPermission()
    {
        int requestCode;
        ActivityCompat.requestPermissions(this,new String[]{WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE,INTERNET},requestCode=1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale( WRITE_EXTERNAL_STORAGE )) {
                            showMessageOKCancel( "You need to allow access to both the permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions( new String[]{ WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, INTERNET},
                                                        1 );
                                            }
                                        }
                                    } );
                            return;
                        }
                    }

                }


                break;
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder( LoginActivity.this )
                .setMessage( message )
                .setPositiveButton( "OK", okListener )
                .create()
                .show();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
