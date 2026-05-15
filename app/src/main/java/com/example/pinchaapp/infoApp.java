package com.example.pinchaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class infoApp extends AppCompatActivity {
    Button btnAceptar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_info_app);

        btnAceptar = findViewById(R.id.btnAceptar);

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(infoApp.this,"Puede Iniciar Sesion",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(infoApp.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}