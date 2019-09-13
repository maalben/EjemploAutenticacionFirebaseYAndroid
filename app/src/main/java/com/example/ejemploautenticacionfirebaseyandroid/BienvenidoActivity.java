package com.example.ejemploautenticacionfirebaseyandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class BienvenidoActivity extends AppCompatActivity {

    private String usuario;

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setUsuario(getIntent().getStringExtra("usuario"));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenido);

        TextView nombreusuario = findViewById(R.id.lblusuario);
        nombreusuario.setText(getUsuario());

        findViewById(R.id.btnsalir).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent ventanaMain = new Intent(BienvenidoActivity.this, MainActivity.class);
                startActivity(ventanaMain);
            }
        });


    }
}
