package com.example.pinchaapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class pantalla_dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pantalla_dashboard);

        Button btnCrearPerfil = findViewById(R.id.btnCrearPerfil);

        btnCrearPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(pantalla_dashboard.this, view);
                popup.getMenuInflater().inflate(R.menu.menu_perfil, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        int id = item.getItemId();

                        if (id == R.id.op_humano) {
                            Toast.makeText(pantalla_dashboard.this, "Humano seleccionado", Toast.LENGTH_SHORT).show();
                            return true;

                        } else if (id == R.id.op_mascota) {
                            Toast.makeText(pantalla_dashboard.this, "Mascota seleccionada", Toast.LENGTH_SHORT).show();
                            return true;
                        }

                        return false;
                    }
                });

                popup.show();
            }
        });
    }
}