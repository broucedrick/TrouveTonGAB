package com.example.trouvetongab;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "login" ;
    View view;
    Button button_connection_ggle;
    LoginButton button_connection_fb;
    private GoogleApiClient googleApiClient;
    private static final int SIGN_IN = 1 ;
    private static final int SIGN_FB = 0 ;

    String userName = "";
    String userEmail ="";
    String userFname ="";
    String userDname = "";
    String info = "";
    Context contexts;
    private String nom;
    private String nom_fb;
    private String email_ggle;
    private String email;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_login);
        button_connection_fb = findViewById(R.id.login_button);
        button_connection_fb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject me, GraphResponse response) {
                                if (response.getError() != null) {
                                    // handle error
                                } else {
                                    String name = me.optString("name");
                                    String email = me.optString("email");

/*                                    if(email ==""){
                                        email = "email est vide";
                                        connection(login.this,name,email);

                                    }else{
                                        connection(login.this,name,email);

                                    }*/
                                }
                            }

                        }).executeAsync();
                Toast.makeText(getApplicationContext(),"connection faceook effectuer",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
            @Override
            public void onCancel() {
                info = ("Login attempt canceled.");
                setContentView(R.layout.activity_login);
                Toast.makeText(getApplicationContext(),info,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                info = ("connection facebook echoué verifier la connection .");
                startActivity(new Intent(getApplicationContext(), login.class));
                Toast.makeText(getApplicationContext(),info,Toast.LENGTH_LONG).show();

            }
        });

        //////////////////connnection google/////////////////
        button_connection_ggle = findViewById(R.id.sign_in_button);
        button_connection_ggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(login.this);
                progressDialog.setMessage("connection en cours veillez patienter  svp...");
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                progressDialog.show();
                startActivityForResult(intent,SIGN_IN);

            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();

    }

    private void clearPrefData(){
        SharedPreferences mSharedPreferences = getSharedPreferences("User Data", Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.clear().apply();
    }

    private void storeUserData(String nom,String email){
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
               Toast.makeText(getApplicationContext(),"connection a Google effecetué", Toast.LENGTH_LONG).show();
               handleSignInResult(result);
           }else{
               Toast.makeText(getApplicationContext(), "connection a Google echoué verifier la connection internet ", Toast.LENGTH_LONG).show();
               //finish();
               startActivity(new Intent(getApplicationContext(), login.class));
           }
       }else{
            //Toast.makeText(getApplicationContext(), "facebook on result", Toast.LENGTH_LONG).show();
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
    protected void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount account=result.getSignInAccount();
            userName = (account.getDisplayName());
            userEmail = (account.getEmail());
            userFname= (account.getFamilyName());
            Toast.makeText(getApplicationContext(),userName,Toast.LENGTH_LONG).show();

            //    connection(login.this,userName,userEmail);
            //clearPrefData();
            //storeUserData(userName,userEmail);
            finish();

        }else{
            Toast.makeText(getApplicationContext(),"recuperation impossible ",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, login.class));

        }

    }

    public static void connection(final Context context, final String username, final String usermail){

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST,"http://192.168.43.201/digital/visiteur.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // Toast.makeText(context,"connection bd login effectuer "+response,Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"connection bd login echouer ",Toast.LENGTH_LONG).show();

            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("nom",username);
                params.put("email",usermail);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                params.put("nom",username);
                params.put("email",usermail);
                return params;
            }
        };
        queue.add(sr);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }





















































}




