package com.example.projekt2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Objects;

public class KerdoivkeszitesActivity extends AppCompatActivity {
    private static final String LOG_TAG = KerdoivkeszitesActivity.class.getName();
    private FirebaseUser felhasznalo;
    private FirebaseFirestore mFirestore;
    private CollectionReference mItems;

    String[] lista = new String[5];
    String milyen;

    RadioGroup milyenKerdoiv;
    EditText elso;
    EditText masodik;
    EditText harmadik;
    EditText negyedik;
    EditText otodik;
    EditText mentesnev;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kerdoivkeszites);

        felhasznalo = FirebaseAuth.getInstance().getCurrentUser();
        if(felhasznalo!=null){
            Log.d(LOG_TAG,"autentikált felhasználó");
        }else{
            Log.d(LOG_TAG,"nem autentikált felhasználó");
            finish();
        }

        mFirestore=FirebaseFirestore.getInstance();


        milyenKerdoiv=findViewById(R.id.Kerdesektipusa);
        milyenKerdoiv.check(R.id.eldontendo);
        elso=findViewById(R.id.elso);
        masodik=findViewById(R.id.masodik);
        harmadik=findViewById(R.id.harmadik);
        negyedik=findViewById(R.id.negyedik);
        otodik=findViewById(R.id.otodik);

        mentesnev=findViewById(R.id.mentesnevedittext);
    }

    protected String radioButtonLekeres(RadioGroup radioGroup){
        int valasztott = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = radioGroup.findViewById(valasztott);
        return radioButton.getText().toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.kerdoivkeszites_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case(R.id.Kijelentkezes):
                Log.d(LOG_TAG,"kijelentkezes tortent");
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            case(R.id.Mentes):
                Log.d(LOG_TAG,"mentes tortent");
                kerdoivMentes();
                return true;
            case(R.id.Torles):
                Log.d(LOG_TAG,"torles tortent");
                kerdoivTorles();
                return true;
            case(R.id.Frissites):
                Log.d(LOG_TAG,"frissites tortent");
                kerdoivFrissites();
                return true;
            case(R.id.Letoltes):
                Log.d(LOG_TAG,"letoltes tortent");
                kerdoivLetoltes();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void kollekcionevadas(){
        String mMentesnev = mentesnev.getText().toString();
        mItems = mFirestore.collection(mMentesnev);
    }
    private void kerdoivLetoltes() {
        kollekcionevadas();
        mItems.limit(5).get().addOnSuccessListener(queryDocumentSnapshots -> {
            int i=0;
            for (QueryDocumentSnapshot document : queryDocumentSnapshots){
                Kerdes kerdes = document.toObject(Kerdes.class);
                lista[i] = kerdes.getKerdes();
                milyen = kerdes.getTipus();
                i++;
            }
        });
        if(Objects.equals(milyen, "Eldöntendő")){
            milyenKerdoiv.check(R.id.eldontendo);
        }else{
            milyenKerdoiv.check(R.id.megvalaszolando);
        }
        elso.setText(lista[0]);
        masodik.setText(lista[1]);
        harmadik.setText(lista[2]);
        negyedik.setText(lista[3]);
        otodik.setText(lista[4]);
    }

    private void kerdoivFrissites() {
        //:(
    }

    private void kerdoivTorles() {
        //:(
    }

    public void kerdoivMentes(){
        kollekcionevadas();
        String els = elso.getText().toString();
        String mas = masodik.getText().toString();
        String har = harmadik.getText().toString();
        String neg = negyedik.getText().toString();
        String oto = otodik.getText().toString();
        if(els.length()!=0 || mas.length()!=0 || har.length()!=0 || neg.length()!=0 || oto.length()!=0 ){
            mItems.add(new Kerdes(els,radioButtonLekeres(milyenKerdoiv)));
            mItems.add(new Kerdes(mas,radioButtonLekeres(milyenKerdoiv)));
            mItems.add(new Kerdes(har,radioButtonLekeres(milyenKerdoiv)));
            mItems.add(new Kerdes(neg,radioButtonLekeres(milyenKerdoiv)));
            mItems.add(new Kerdes(oto,radioButtonLekeres(milyenKerdoiv)));
            Toast.makeText(KerdoivkeszitesActivity.this, "Mentes elvileg sikeres!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(KerdoivkeszitesActivity.this, "Tolts ki minden kerdest!", Toast.LENGTH_SHORT).show();
        }
    }
}