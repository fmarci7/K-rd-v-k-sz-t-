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

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();
    private static final int SECRET_KEY = 69;
    public static final String PREF_KEY = MainActivity.class.getPackage().toString();
    EditText felhasznalonev;
    EditText jelszo;
    Button bej;

    private FirebaseAuth mAuth;
    private SharedPreferences preferences;
    private ErtesitesKezelo mKezelo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        felhasznalonev = findViewById(R.id.EditTextFelhasznalonev);
        jelszo = findViewById(R.id.EditTextJelszo);

        preferences = getSharedPreferences(PREF_KEY,MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();
        mKezelo=new ErtesitesKezelo(this);

        bej = findViewById(R.id.ButtonBejelentkezes);

        Button asbutton = findViewById(R.id.ButtonVendegkent);
        new RandomAsyncTask(asbutton).execute();
    }

    public void bejelentkezes(View view) {
        String felhaszn = felhasznalonev.getText().toString();
        String jelsz = jelszo.getText().toString();
        if(jelsz.length()==0 || felhaszn.length() == 0){
            Animation animation = AnimationUtils.loadAnimation(MainActivity.this,R.anim.slide_in_row);
            bej.startAnimation(animation);
            Toast.makeText(MainActivity.this,"Add meg az adatokat!",Toast.LENGTH_SHORT).show();
        }else{
            mAuth.signInWithEmailAndPassword(felhaszn,jelsz).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Log.d(LOG_TAG,"Belépés sikeres");
                        kerdoivKeszites();
                    }else{
                        mKezelo.send("A belépés sikertelen!");
                        Log.d(LOG_TAG,"Belépés sikertelen");
                    }
                }
            });
        }
    }
    public void kerdoivKeszites(){
        Intent intent = new Intent(this, KerdoivkeszitesActivity.class);
        intent.putExtra("SECRET_KEY",SECRET_KEY);
        startActivity(intent);
    }
    public void regisztracio(View view) {
        Intent intent = new Intent(this, RegisztracioActivity.class);
        intent.putExtra("SECRET_KEY",SECRET_KEY);
        startActivity(intent);
    }

    public void belepesVendegkent(View view) {
        mAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(LOG_TAG,"Belépés vendégként sikeres");
                    kerdoivKeszites();
                }else{
                    Log.d(LOG_TAG,"Belépés vendégként sikertelen");
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("fhnev",felhasznalonev.getText().toString());
        editor.apply();
    }
}