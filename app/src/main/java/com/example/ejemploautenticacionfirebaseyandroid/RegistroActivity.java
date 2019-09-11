package com.example.ejemploautenticacionfirebaseyandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ejemploautenticacionfirebaseyandroid.model.Usuarios;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegistroActivity extends AppCompatActivity {

    private EditText usuario;
    private EditText clave;
    private EditText confirmarclave;
    private EditText numerodocumento;
    private EditText nombrecompleto;
    private EditText edad;
    private Button guardar;
    private Button volver;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        numerodocumento = findViewById(R.id.txtdocumento);
        nombrecompleto = findViewById(R.id.txtnombre);
        edad = findViewById(R.id.txtedad);
        usuario = findViewById(R.id.txtusuario);
        clave = findViewById(R.id.txtclave);
        confirmarclave = findViewById(R.id.txtconfirmarclave);
        guardar = findViewById(R.id.btnguardar);
        volver = findViewById(R.id.btnvolver);
        firebaseAuth = FirebaseAuth.getInstance();

        inicializarFirebase();

        numerodocumento.addTextChangedListener(validarcampo);
        nombrecompleto.addTextChangedListener(validarcampo);
        edad.addTextChangedListener(validarcampo);
        usuario.addTextChangedListener(validarcampo);
        clave.addTextChangedListener(validarcampo);
        confirmarclave.addTextChangedListener(validarcampo);


        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String valorDocumento = numerodocumento.getText().toString().trim();
                String valorNombre = nombrecompleto.getText().toString().trim();
                String valorEdad = edad.getText().toString().trim();
                String user = usuario.getText().toString().trim();
                String pass = clave.getText().toString().trim();
                String pass2 = confirmarclave.getText().toString().trim();

                if(pass.equals(pass2)){
                    Usuarios usuarios = new Usuarios();
                    usuarios.setNumerodocumento(valorDocumento);
                    usuarios.setNombrecompleto(valorNombre);
                    usuarios.setEdad(valorEdad);
                    usuarios.setEmail(user);
                    databaseReference.child("Usuarios").child(usuarios.getNumerodocumento()).setValue(usuarios);
                    firebaseAuth.createUserWithEmailAndPassword(usuarios.getEmail(), pass).addOnCompleteListener(RegistroActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                task.getResult();
                                Toast.makeText(RegistroActivity.this, "Error en el registro", Toast.LENGTH_LONG).show();
                            }else{
                                Intent ventanaLogin = new Intent(RegistroActivity.this, MainActivity.class);
                                startActivity(ventanaLogin);
                            }
                        }
                    });

                }else{
                    Toast.makeText(RegistroActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
                }
            }
        });

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void inicializarFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private TextWatcher validarcampo = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            //No is need
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            final String regex = "(?:[^<>()\\[\\].,;:\\s@\"]+(?:\\.[^<>()\\[\\].,;:\\s@\"]+)*|\"[^\\n\"]+\")@(?:[^<>()\\[\\].,;:\\s@\"]+\\.)+[^<>()\\[\\]\\.,;:\\s@\"]{2,63}";
            String valorDocumento = numerodocumento.getText().toString().trim();
            String valorNombre = nombrecompleto.getText().toString().trim();
            String valorEdad = edad.getText().toString().trim();
            String user = usuario.getText().toString().trim();
            String pass = clave.getText().toString().trim();
            String pass2 = confirmarclave.getText().toString().trim();

            guardar.setEnabled(
                    !valorDocumento.isEmpty() &&
                    !valorNombre.isEmpty() &&
                    !valorEdad.isEmpty() &&
                    !user.isEmpty() &&
                    !pass.isEmpty() &&
                    !pass2.isEmpty() &&
                    user.matches(regex) &&
                    pass.length() > 5 &&
                    pass2.length() > 5 &&
                    (pass.equals(pass2)) &&
                    valorDocumento.length() > 5
            );

        }

        @Override
        public void afterTextChanged(Editable editable) {
            //No is need
        }
    };


}
