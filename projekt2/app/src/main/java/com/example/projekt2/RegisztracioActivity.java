package com.example.projekt2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisztracioActivity extends AppCompatActivity {
    private static final int SECRET_KEY = 69;
    private static final String LOG_TAG = RegisztracioActivity.class.getName();
    public static final String PREF_KEY = MainActivity.class.getPackage().toString();

    EditText felhasznalonev;
    EditText jelszo;
    EditText jelszomegint;
    EditText emailcim;

    Button megse;
    private FirebaseAuth mAuth;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regisztracio);



        int secret_key = getIntent().getIntExtra("SECRET_KEY",0);
        if(secret_key !=69){
            finish();
        }
        felhasznalonev=findViewById(R.id.felhasznalonev);
        jelszo=findViewById(R.id.jelszo);
        jelszomegint=findViewById(R.id.jelszomegint);
        emailcim=findViewById(R.id.emailcim);

        megse=findViewById(R.id.megsee);

        mAuth = FirebaseAuth.getInstance();
        preferences = getSharedPreferences(PREF_KEY,MODE_PRIVATE);
        String fhnev = preferences.getString("fhnev","");

        felhasznalonev.setText(fhnev);
        Log.i(LOG_TAG,"onCreate");
    }

    public void regisztracio(View view) {
        String fhnev= felhasznalonev.getText().toString();
        String jelsz = jelszo.getText().toString();
        String jelsz2 = jelszomegint.getText().toString();
        String emailc = emailcim.getText().toString();


        if(jelsz.length()==0 || fhnev.length() == 0 ||jelsz2.length()==0 || emailc.length()==0){
            Toast.makeText(RegisztracioActivity.this,"Add meg az adatokat!",Toast.LENGTH_SHORT).show();
        }else {
            if(!jelsz.equals(jelsz2)){
                Toast.makeText(RegisztracioActivity.this,"A ket jelszo nem egyezik!",Toast.LENGTH_SHORT).show();
            }else{
                mAuth.createUserWithEmailAndPassword(emailc,jelsz).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(LOG_TAG,"Sikeres felhasznalo keszites");
                            kerdoivKeszites();
                        }else{
                            Log.d(LOG_TAG,"Sikertelen felhasznalo keszites");
                            Toast.makeText(RegisztracioActivity.this, "Sikertelen regisztracio: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }
    public void kerdoivKeszites(){
        Intent intent = new Intent(this, KerdoivkeszitesActivity.class);
        intent.putExtra("SECRET_KEY",SECRET_KEY);
        startActivity(intent);
    }

    public void megsem(View view) {
        Animation animation = AnimationUtils.loadAnimation(RegisztracioActivity.this,R.anim.kicsusszan);
        megse.startAnimation(animation);
        try{
            Thread.sleep(501);
        }catch (InterruptedException e){
            e.printStackTrace();
        }        finish();
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.i(LOG_TAG,"onStart");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.i(LOG_TAG,"onStop");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i(LOG_TAG,"onDestroy");

    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.i(LOG_TAG,"onPause");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.i(LOG_TAG,"onResume");
    }
}