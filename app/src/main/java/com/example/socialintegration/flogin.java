package com.example.socialintegration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class flogin extends AppCompatActivity {
    private TextView e;
    private TextView a;
    private ImageView image;
    private LoginButton b;
    private CallbackManager c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flogin);
        LinearLayout linearLayout = findViewById(R.id.l);
        AnimationDrawable animationDrawable = (AnimationDrawable) linearLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1000);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();
        e=findViewById(R.id.textView);
        b=findViewById(R.id.login_button);
        a=findViewById(R.id.textView1);
        image=findViewById(R.id.im);
        c = CallbackManager.Factory.create();
        b.setPermissions(Arrays.asList("email","user_birthday"));
        b.registerCallback(c,new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        c.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    AccessTokenTracker t=new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null) {
                LoginManager.getInstance().logOut();
                e.setText("");
                a.setText("");
                image.setImageResource(0);

                Toast.makeText(flogin.this, "Logged Out!!", Toast.LENGTH_SHORT).show();
            }
            // Set the access token using

            // currentAccessToken when it's loaded or set.
            else {
                loaduserProfile(currentAccessToken);
            }
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        t.stopTracking();
    }


    private void loaduserProfile(AccessToken newAccessToken){
        GraphRequest request=GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if(object!=null){
                    try{
                        String email=object.getString("email");
                        String name=object.getString("name");
                        String id=object.getString("id");
                       // Uri uri=object.getJSONObject("picture").getJSONObject("data");

                        e.setText("Name: "+name);
                        a.setText("Email: "+email);
                        image.setVisibility(View.VISIBLE);
                        Picasso.get().load("https://graph.facebook.com/" + id + "/picture?type=large").into(image);
                        //Glide.with(flogin.this).load(profilePicUrl).into(image);
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        Bundle parametres=new Bundle();
        parametres.putString("fields","picture.type(large),email,name");
        request.setParameters(parametres);
        request.executeAsync();

    }


}
