package com.sismatix.drskin.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.sismatix.drskin.Fragment.SignIn;
import com.sismatix.drskin.Preference.Login_preference;
import com.sismatix.drskin.R;

public class Splash extends AppCompatActivity implements View.OnClickListener {
    LinearLayout lv_sikp_splash,lv_signin_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
//        getSupportActionBar().hide();
        // getApplicationContext().setTheme(android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        lv_sikp_splash = (LinearLayout) findViewById(R.id.lv_sikp_splash);
        lv_signin_splash = (LinearLayout) findViewById(R.id.lv_signin_splash);
        String logflag=Login_preference.getLogin_flag(Splash.this);
            if (logflag.equalsIgnoreCase("1") || logflag == "1") {
                Intent i =new Intent(Splash.this,Bottom_navigation.class);
                startActivity(i);
                finish();
            } else {
            }

        lv_signin_splash.setOnClickListener(this);
        lv_sikp_splash.setOnClickListener(this);
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i =new Intent(Splash.this,Bottom_navigation.class);
                startActivity(i);
                finish();
            }
        },1000);*/
    }
    @Override
    public void onClick(View view) {
        if (view == lv_signin_splash){
            pushFragment(new SignIn());
            Intent i=new Intent(Splash.this,Bottom_navigation.class);
            //Bundle b=new Bundle();
            i.putExtra("screen","Login");
            startActivity(i);
        }else if(view == lv_sikp_splash){
            Intent i =new Intent(Splash.this,Bottom_navigation.class);
            startActivity(i);
            finish();
        }

    }
    private void pushFragment(Fragment fragment) {
        if (fragment == null)
            return;
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null) {
                ft.replace(R.id.rootLayout, fragment);
                ft.commit();
            }
        }
    }
}
