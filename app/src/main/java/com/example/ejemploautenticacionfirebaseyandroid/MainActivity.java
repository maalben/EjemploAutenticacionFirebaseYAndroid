package com.example.ejemploautenticacionfirebaseyandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private Button registrarse;
    private EditText email;
    private EditText contrasenia;
    private Button ingresar;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.txtusuario);
        contrasenia = findViewById(R.id.txtclave);
        ingresar = findViewById(R.id.btningresar);
        registrarse = findViewById(R.id.btnregistrarse);

        progressDialog = new ProgressDialog(this);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null){
                    progressDialog.setTitle("Conectando...");
                    progressDialog.setMessage("Cargando...");
                    progressDialog.show();
                    Intent ventanaBievenida = new Intent(MainActivity.this, BienvenidoActivity.class);
                    startActivity(ventanaBievenida);
                }else{
                    Toast.makeText(MainActivity.this, "Inicia sesión", Toast.LENGTH_LONG).show();
                }
            }
        };

        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String correo = email.getText().toString().trim();
                String pass = contrasenia.getText().toString().trim();

                progressDialog.setMessage("Espera un momento");
                progressDialog.show();

                firebaseAuth.signInWithEmailAndPassword(correo, pass)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    int pos = correo.indexOf("@");
                                    String user = correo.substring(0,pos);
                                    Toast.makeText(MainActivity.this, "Bienvenido " + user, Toast.LENGTH_LONG).show();
                                    Intent ventanaBievenida = new Intent(getApplication(), BienvenidoActivity.class);
                                    ventanaBievenida.putExtra("usuario", user);
                                    startActivity(ventanaBievenida);
                                }else{
                                    Toast.makeText(MainActivity.this, "Error en la autenticación", Toast.LENGTH_LONG).show();
                                }

                                progressDialog.dismiss();
                            }
                        });



            }
        });



        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ventanaRegistro = new Intent(MainActivity.this, RegistroActivity.class);
                startActivity(ventanaRegistro);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}
