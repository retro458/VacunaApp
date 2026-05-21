package com.example.pinchaapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import com.example.pinchaapp.adapters.PerfilAdapter;
import com.example.pinchaapp.database.VacunAppDatabase;
import com.example.pinchaapp.database.entities.PerfilHumano;
import com.example.pinchaapp.database.entities.PerfilMascota;
import com.example.pinchaapp.adapters.OnPerfilActionListener;

import java.util.Calendar;
import java.util.List;

public class pantalla_dashboard extends AppCompatActivity {

    RecyclerView rvPerfiles;
    Button btnCrearPerfil, btnLogout;
    PerfilAdapter adapter;
    VacunAppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pantalla_dashboard);

        btnCrearPerfil = findViewById(R.id.btnCrearPerfil);
        rvPerfiles = findViewById(R.id.rvPerfiles);
        ImageButton btnLogout = findViewById(R.id.btnLogout);

        rvPerfiles.setLayoutManager(new LinearLayoutManager(this));

        db = VacunAppDatabase.getInstance(this);

        cargarPerfiles();

        btnCrearPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context wrapper =
                        new ContextThemeWrapper(
                                pantalla_dashboard.this,
                                R.style.PopupMenuStyle
                        );

                PopupMenu popup = new PopupMenu(wrapper, view);
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

                            return true;

                        } else if (id == R.id.op_mascota) {

                            Toast.makeText(
                                    pantalla_dashboard.this,
                                    "Mascota seleccionada",
                                    Toast.LENGTH_SHORT
                            ).show();

                            startActivity(
                                    new Intent(
                                            pantalla_dashboard.this,
                                            nuevo_perfil_mascota.class
                                    )
                            );

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

        btnLogout.setOnClickListener(v -> {

            new AlertDialog.Builder(pantalla_dashboard.this)
                    .setTitle("Cerrar sesión")
                    .setMessage("¿Seguro que deseas salir?")
                    .setPositiveButton("Sí", (dialog, which) -> {

                        SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
                        editor.clear();
                        editor.apply();

                        Intent intent = new Intent(
                                pantalla_dashboard.this,
                                MainActivity.class
                        );

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarPerfiles();
    }

    private void cargarPerfiles() {

        new Thread(() -> {

            List<Object> lista = new ArrayList<>();

            lista.addAll(db.perfilHumanoDao().obtenerPerfiles());
            lista.addAll(db.perfilMascotaDao().obtenerTodos());

            runOnUiThread(() -> {

                adapter = new PerfilAdapter(
                        pantalla_dashboard.this,
                        lista,
                        new OnPerfilActionListener() {

                            @Override
                            public void onEditar(Object perfil, int position) {

                                View view = LayoutInflater.from(pantalla_dashboard.this)
                                        .inflate(R.layout.dialog_editar_perfil, null);

                                EditText etNombre = view.findViewById(R.id.etNombre);
                                EditText etFecha = view.findViewById(R.id.etFecha);
                                Spinner spTipo = view.findViewById(R.id.spTipo);
                                CheckBox cbEmbarazada = view.findViewById(R.id.cbEmbarazada);

                                boolean isHumano = perfil instanceof PerfilHumano;

                                // ======================
                                // SPINNER CONFIG
                                // ======================
                                if (isHumano) {

                                    String[] opciones = {"Masculino", "Femenino"};
                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                            pantalla_dashboard.this,
                                            android.R.layout.simple_spinner_dropdown_item,
                                            opciones
                                    );
                                    spTipo.setAdapter(adapter);

                                    spTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                            String valor = parent.getItemAtPosition(position).toString();

                                            if (valor.equals("Masculino")) {

                                                cbEmbarazada.setChecked(false);
                                                cbEmbarazada.setEnabled(false);
                                                cbEmbarazada.setVisibility(View.GONE);

                                            } else {

                                                cbEmbarazada.setEnabled(true);
                                                cbEmbarazada.setVisibility(View.VISIBLE);
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {}
                                    });

                                } else {

                                    String[] opciones = {"Perro", "Gato"};
                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                            pantalla_dashboard.this,
                                            android.R.layout.simple_spinner_dropdown_item,
                                            opciones
                                    );
                                    spTipo.setAdapter(adapter);
                                }

                                // ======================
                                // CARGAR DATOS
                                // ======================
                                if (isHumano) {

                                    PerfilHumano p = (PerfilHumano) perfil;

                                    etNombre.setText(p.getNombre());
                                    etFecha.setText(p.getFechaNacimiento());
                                    cbEmbarazada.setChecked(p.isEmbarazada());

                                    if (p.getSexo().equals("Masculino")) spTipo.setSelection(0);
                                    else spTipo.setSelection(1);

                                    cbEmbarazada.setVisibility(View.VISIBLE);

                                } else {

                                    PerfilMascota p = (PerfilMascota) perfil;

                                    etNombre.setText(p.getNombre());
                                    etFecha.setText(p.getFechaNacimiento());

                                    cbEmbarazada.setVisibility(View.GONE);

                                    if (p.getEspecie().equals("Perro")) spTipo.setSelection(0);
                                    else spTipo.setSelection(1);
                                }

                                // ======================
                                // DATE PICKER
                                // ======================
                                etFecha.setOnClickListener(v -> {

                                    Calendar calendar = Calendar.getInstance();

                                    DatePickerDialog picker = new DatePickerDialog(
                                            pantalla_dashboard.this,
                                            (view1, year, month, dayOfMonth) -> {

                                                String fecha = dayOfMonth + "/" + (month + 1) + "/" + year;
                                                etFecha.setText(fecha);

                                            },
                                            calendar.get(Calendar.YEAR),
                                            calendar.get(Calendar.MONTH),
                                            calendar.get(Calendar.DAY_OF_MONTH)
                                    );

                                    picker.show();
                                });

                                // ======================
                                // DIALOG
                                // ======================
                                AlertDialog dialog = new AlertDialog.Builder(pantalla_dashboard.this)
                                        .setTitle("Editar perfil")
                                        .setView(view)
                                        .setPositiveButton("Actualizar", null)
                                        .setNegativeButton("Cancelar", null)
                                        .create();

                                dialog.setOnShowListener(d -> {

                                    Button btn = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

                                    btn.setOnClickListener(v -> {

                                        new Thread(() -> {

                                            if (isHumano) {

                                                PerfilHumano p = (PerfilHumano) perfil;

                                                p.setNombre(etNombre.getText().toString());
                                                p.setFechaNacimiento(etFecha.getText().toString());
                                                p.setSexo(spTipo.getSelectedItem().toString());
                                                p.setEmbarazada(
                                                        spTipo.getSelectedItem().toString().equals("Femenino")
                                                                && cbEmbarazada.isChecked()
                                                );

                                                db.perfilHumanoDao().actualizarPerfil(p);

                                            } else {

                                                PerfilMascota p = (PerfilMascota) perfil;

                                                p.setNombre(etNombre.getText().toString());
                                                p.setFechaNacimiento(etFecha.getText().toString());
                                                p.setEspecie(spTipo.getSelectedItem().toString());

                                                db.perfilMascotaDao().actualizarPerfil(p);
                                            }

                                            runOnUiThread(() -> {
                                                adapter.notifyItemChanged(position);
                                                dialog.dismiss();
                                            });

                                        }).start();
                                    });
                                });

                                dialog.show();
                            }

                            @Override
                            public void onEliminar(Object perfil, int position) {

                                new AlertDialog.Builder(pantalla_dashboard.this)
                                        .setTitle("Eliminar perfil")
                                        .setMessage("¿Seguro que deseas eliminar este perfil?")
                                        .setPositiveButton("Eliminar", (dialog, which) -> {

                                            new Thread(() -> {

                                                if (perfil instanceof PerfilHumano) {
                                                    db.perfilHumanoDao()
                                                            .eliminarPerfil((PerfilHumano) perfil);
                                                } else {
                                                    db.perfilMascotaDao()
                                                            .eliminarPerfil((PerfilMascota) perfil);
                                                }

                                                runOnUiThread(() ->
                                                        adapter.eliminarItem(position)
                                                );

                                            }).start();
                                        })
                                        .setNegativeButton("Cancelar", null)
                                        .show();
                            }
                        }
                );

                rvPerfiles.setAdapter(adapter);
            });

        }).start();
    }


}