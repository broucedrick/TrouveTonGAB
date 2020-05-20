package com.example.trouvetongab;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "login" ;

    SignInButton button_connection_ggle;
    LoginButton button_connection_fb;
    private GoogleApiClient googleApiClient;
    private static final int SIGN_IN = 0 ;
    private static final int SIGN_FB = 0 ;

    String userName = "";
    String userEmail ="";
    String userFname ="";
    String userDname = "";
    String info = "";
    private String nom;
    private String nom_fb;
    private String email_ggle;
    private String email;
    String name;
     LoadingDialog loadingDialog;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        if (ContextCompat.checkSelfPermission(login.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

// Permission is not granted
// Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(login.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(login.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);

                // MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
// Permission has already been granted
        }

        setContentView(R.layout.activity_login);
        button_connection_fb = findViewById(R.id.login_button);
        button_connection_fb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                //loadingDialog.dismissDialog();

                GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject me, GraphResponse response) {
                                if (response.getError() != null) {
                                    // handle error
                                } else {

                                    JSONObject data = response.getJSONObject();
                                    Intent i = new Intent(login.this, MainActivity.class);

                                    name = me.optString("name");
                                    String email = me.optString("email");
                                    String id = me.optString("id");
                                    Toast.makeText(getApplicationContext(),name,Toast.LENGTH_LONG).show();



                                    Bundle bundle = new Bundle();
                                    bundle.putString("id", id);
                                    bundle.putString("name",name);
                                    bundle.putString("fb", "fb");
                                    i.putExtras(bundle);

                                    startActivity(i, bundle);

                      //  Toast.makeText(getApplicationContext(),data.toString(),Toast.LENGTH_LONG).show();
                                    String fb = "facebook";

                                    //connection(login.this,name,email);
                                    storeUserData(name,email);
                                    if(email ==""){
                                        email = "email inconnu" + " "+id;
                                        connection(login.this,name,email,fb);

                                    }else{
                                        connection(login.this,name,email,fb);
                                    }
                                }
                            }

                        }).executeAsync();
            }
            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "connection a facebook echoué verifier la connection internet ", Toast.LENGTH_LONG).show();
               loadingDialog.dismissDialog();
                loadingDialog.startWarningDialog_log();
            //    setContentView(R.layout.activity_login);
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "connection a facebook echoué verifier la connection internet ", Toast.LENGTH_LONG).show();
               loadingDialog.dismissDialog();
                loadingDialog.startWarningDialog_log();
            //    setContentView(R.layout.activity_login);
              //  startActivity(new Intent(getApplicationContext(), login.class));

            }
        });

        //////////////////connnection google/////////////////
        button_connection_ggle = findViewById(R.id.sign_in_button);
        button_connection_ggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                google();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();

    }

    public void google(){
        loadingDialog = new LoadingDialog(login.this);
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        loadingDialog.startLoadingDialog();
        startActivityForResult(intent,SIGN_IN);
    }
    private void clearPrefData(){
        SharedPreferences mSharedPreferences = getSharedPreferences("User Data", Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.clear().apply();
    }

    private void    storeUserData(String nom,String email){
        SharedPreferences mSharedPreferences = getSharedPreferences("User Data", Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString("nom", nom);
        mEditor.putString("email", email);
        mEditor.apply();
    }




    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data ){
       super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
           if(result.isSuccess()){
               loadingDialog.startLoadingDialog();
               handleSignInResult(result);
              // startActivity(new Intent(getApplicationContext(), MainActivity.class));
              // finish();
           }else{
               //Toast.makeText(getApplicationContext(), "connection a Google echoué verifier la connection internet ", Toast.LENGTH_LONG).show();
               loadingDialog.dismissDialog();
               loadingDialog.startWarningDialog_log();
               // startActivity(new Intent(getApplicationContext(), login.class));
           }
       }else{
            //Toast.makeText(getApplicationContext(), "facebook on result", Toast.LENGTH_LONG).show();
            loadingDialog = new LoadingDialog(login.this);
            loadingDialog.startLoadingDialog();
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
    protected void handleSignInResult(GoogleSignInResult result){
        String google = "google";
        if(result.isSuccess()){
            loadingDialog.dismissDialog();
            Intent i = new Intent(login.this, MainActivity.class);
            GoogleSignInAccount account=result.getSignInAccount();
            userName = (account.getDisplayName());
            userEmail = (account.getEmail());
            userFname= (account.getFamilyName());

            Bundle bundle = new Bundle();
            bundle.putString("ggle",google);
            i.putExtras(bundle);
            connection(login.this,userName,userEmail,google);
           // Toast.makeText(getApplicationContext(),userName + userEmail ,Toast.LENGTH_LONG).show();

            storeUserData(userName,userEmail);

            startActivity(i, bundle);
            finish();

        }else{
            Toast.makeText(getApplicationContext(),"recuperation impossible ",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, login.class));

        }

    }

    public static void connection(final Context context, final String username, final String usermail, final String plateforme){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST,"https://digitalfinances.innovstech.com/visiteur.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
             //  Toast.makeText(context,"connection bd login effectuer "+response,Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"verifier la connection internet ",Toast.LENGTH_LONG).show();

            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("plateforme",plateforme);
                params.put("nom",username);
                params.put("email",usermail);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                params.put("plateforme",plateforme);
                params.put("nom",username);
                params.put("email",usermail);
                return params;
            }
        };
     //   Toast.makeText(context,usermail,Toast.LENGTH_LONG).show();

        queue.add(sr);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }





















































}




