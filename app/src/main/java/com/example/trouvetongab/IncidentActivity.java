package com.example.trouvetongab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Text;

public class IncidentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextInputEditText name, bank, number, email;
    TextView erreur;
    EditText comment;
    Button envoyer;
    Spinner incident;
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        name = (TextInputEditText) findViewById(R.id.name);
        bank = (TextInputEditText) findViewById(R.id.bank);
        number = (TextInputEditText) findViewById(R.id.number);
        email = (TextInputEditText) findViewById(R.id.email);
        comment = (EditText) findViewById(R.id.comment);
        envoyer = (Button) findViewById(R.id.envoyer);
        erreur = (TextView) findViewById(R.id.erreur);

        envoyer.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0ffffff, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });

        envoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().length() == 0)
                    erreur.setVisibility(View.VISIBLE);
                else if(bank.getText().length() == 0)
                    erreur.setVisibility(View.VISIBLE);
                else if (number.getText().length() == 0)
                    erreur.setVisibility(View.VISIBLE);
                else if(email.getText().length() == 0)
                    erreur.setVisibility(View.VISIBLE);
                else if(text.contentEquals("Nature de l'incident"))
                    erreur.setVisibility(View.VISIBLE);
                else {
                    erreur.setVisibility(View.GONE);
                    sendEmail();
                }
            }
        });

        incident = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.incident_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        incident.setAdapter(adapter);

        incident.setOnItemSelectedListener(this);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        text = parent.getItemAtPosition(pos).toString();
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        erreur.setVisibility(View.VISIBLE);
    }

    protected void sendEmail() {
        Log.i("Send email", "");

        String[] TO = {"info@digitalefinances.com"};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Déclaration d'incident");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Nom et Prénom(s) : "+name.getText()+
                                                       "\n\nE-mail : "+email.getText()+
                                                       "\n\nNuméro : "+number.getText()+
                                                       "\n\nIncident : "+text);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(IncidentActivity.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
