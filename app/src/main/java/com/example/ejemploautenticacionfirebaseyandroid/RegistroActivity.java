package com.example.ejemploautenticacionfirebaseyandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
    private Button guardar;
    private Button volver;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        usuario = findViewById(R.id.txtusuario);
        clave = findViewById(R.id.txtclave);
        confirmarclave = findViewById(R.id.txtconfirmarclave);
        guardar = findViewById(R.id.btnguardar);
        volver = findViewById(R.id.btnvolver);
        firebaseAuth = FirebaseAuth.getInstance();

        inicializarFirebase();

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = usuario.getText().toString().trim();
                String pass = clave.getText().toString().trim();
                String pass2 = confirmarclave.getText().toString().trim();

                if(pass.equals(pass2)){
                    Usuarios usuarios = new Usuarios();
                    usuarios.setEmail(user);
                    usuarios.setClave(pass);

                    firebaseAuth.createUserWithEmailAndPassword(usuarios.getEmail(), usuarios.getClave()).addOnCompleteListener(RegistroActivity.this, new OnCompleteListener<AuthResult>() {
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


}
