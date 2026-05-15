package com.example.pinchaapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pinchaapp.adapters.PerfilAdapter;
import com.example.pinchaapp.database.VacunAppDatabase;
import com.example.pinchaapp.database.entities.PerfilHumano;

import java.util.List;

public class pantalla_dashboard extends AppCompatActivity {
    RecyclerView rvPerfiles;
    Button btnCrearPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pantalla_dashboard);

        btnCrearPerfil = findViewById(R.id.btnCrearPerfil);
        rvPerfiles = findViewById(R.id.rvPerfiles);

        rvPerfiles.setLayoutManager(
                new LinearLayoutManager(this)
        );
        VacunAppDatabase db =
                VacunAppDatabase.getInstance(this);

        new Thread(() -> {

            List<PerfilHumano> lista =
                    db.perfilHumanoDao()
                            .obtenerPerfiles();

            runOnUiThread(() -> {

                PerfilAdapter adapter =
                        new PerfilAdapter(
                                pantalla_dashboard.this,
                                lista
                        );

                rvPerfiles.setAdapter(adapter);

            });

        }).start();

        btnCrearPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context wrapper =
                        new ContextThemeWrapper(
                                pantalla_dashboard.this,
                                R.style.PopupMenuStyle
                        );

                PopupMenu popup =
                        new PopupMenu(wrapper, view);
                popup.getMenuInflater().inflate(R.menu.menu_perfil, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        int id = item.getItemId();

                        if (id == R.id.op_humano) {

                            Toast.makeText(
                                    pantalla_dashboard.this,
                                    "Humano seleccionado",
                                    Toast.LENGTH_SHORT
                            ).show();

                            startActivity(
                                    new Intent(
                                            pantalla_dashboard.this,
                                            NuevoPerfilHumano.class
                                    )
                            );

                            finish();

                            return true;

                        } else if (id == R.id.op_mascota) {

                            Toast.makeText(
                                    pantalla_dashboard.this,
                                    "Mascota seleccionada",
                                    Toast.LENGTH_SHORT
                            ).show();
                            startActivity(
                                    new Intent(
                                            pantalla_dashboard.this, nuevo_perfil_mascota.class

                                    )
                            );
                            finish();
                            return true;
                        }

                        return false;
                    }
                });

                try {

                    Field fieldPopup =
                            PopupMenu.class.getDeclaredField("mPopup");

                    fieldPopup.setAccessible(true);

                    Object menuPopupHelper = fieldPopup.get(popup);

                    Class<?> classPopupHelper =
                            Class.forName(
                                    menuPopupHelper.getClass().getName()
                            );

                    Method setForceIcons =
                            classPopupHelper.getMethod(
                                    "setForceShowIcon",
                                    boolean.class
                            );

                    setForceIcons.invoke(menuPopupHelper, true);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                popup.show();
            }
        });
    }
}