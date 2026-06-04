package com.example.pinchaapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pinchaapp.adapters.PerfilAdapter;
import com.example.pinchaapp.dto.MiembroDto;
import com.example.pinchaapp.dto.MiembroDto.MiembroResponseDto;
import com.example.pinchaapp.dto.RespuestaDto;
import com.example.pinchaapp.network.ApiClient;
import com.example.pinchaapp.network.ApiService;
import com.example.pinchaapp.session.SessionManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class pantalla_dashboard extends AppCompatActivity {

    private RecyclerView rvPerfiles;
    private Button btnCrearPerfil;
    private ImageButton btnLogout;
    private PerfilAdapter adapter;
    private ApiService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pantalla_dashboard);

        rvPerfiles = findViewById(R.id.rvPerfiles);
        btnCrearPerfil = findViewById(R.id.btnCrearPerfil);
        btnLogout = findViewById(R.id.btnLogout);
        com.google.android.material.card.MaterialCardView cardCampanias = findViewById(R.id.cardCampanias);
        rvPerfiles.setLayoutManager(new LinearLayoutManager(this));
        api = ApiClient.getInstance().create(ApiService.class);

        cargarPerfilesAPI();
        cardCampanias.setOnClickListener(v -> {
            Intent intent = new Intent(pantalla_dashboard.this, CentrosDeVacunacion.class);
            startActivity(intent);
        });
        // ==========================================
        // MENÚ FLOTANTE PARA CREAR (INTACTO)
        // ==========================================
        btnCrearPerfil.setOnClickListener(view -> {
            Context wrapper = new ContextThemeWrapper(this, R.style.PopupMenuStyle);
            PopupMenu popup = new PopupMenu(wrapper, view);
            popup.getMenuInflater().inflate(R.menu.menu_perfil, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.op_humano) {
                    startActivity(new Intent(this, NuevoPerfilHumano.class));
                    return true;
                } else if (item.getItemId() == R.id.op_mascota) {
                    startActivity(new Intent(this, nuevo_perfil_mascota.class));
                    return true;
                }
                return false;
            });

            // Forzar iconos (Reflexión original del equipo)
            try {
                Field fieldPopup = PopupMenu.class.getDeclaredField("mPopup");
                fieldPopup.setAccessible(true);
                Object menuPopupHelper = fieldPopup.get(popup);
                Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                setForceIcons.invoke(menuPopupHelper, true);
            } catch (Exception ignored) {
            }

            popup.show();
        });

        // ==========================================
        // LOGOUT
        // ==========================================
        btnLogout.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Cerrar sesión")
                    .setMessage("¿Seguro que deseas salir?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        SessionManager.cerrarSesion();
                        getSharedPreferences("user", MODE_PRIVATE).edit().clear().apply();
                        getSharedPreferences("AUTH_PREFS", MODE_PRIVATE).edit().clear().apply();

                        Intent intent = new Intent(this, MainActivity.class);
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
        cargarPerfilesAPI(); // Refresca la lista desde el servidor al volver
    }

    // ==========================================
    // LÓGICA DE RED: OBTENER PERFILES
    // ==========================================
    private void cargarPerfilesAPI() {
        api.getMiembros().enqueue(new Callback<RespuestaDto<List<MiembroResponseDto>>>() {
            @Override
            public void onResponse(Call<RespuestaDto<List<MiembroResponseDto>>> call, Response<RespuestaDto<List<MiembroResponseDto>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isExito()) {
                    List<MiembroResponseDto> listaAPI = response.body().getData();
                    configurarAdaptador(listaAPI);
                } else {
                    // AQUÍ ATRAPAMOS EL 401
                    if (response.code() == 401) {
                        cerrarSesionPorToken();
                    } else {
                        Toast.makeText(pantalla_dashboard.this, "Error al cargar perfiles", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RespuestaDto<List<MiembroResponseDto>>> call, Throwable t) {
                Toast.makeText(pantalla_dashboard.this, "Falla de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ==========================================
    // CONFIGURAR ADAPTADOR Y DIÁLOGOS (USANDO DTO)
    // ==========================================
    private void configurarAdaptador(List<MiembroResponseDto> lista) {
        adapter = new PerfilAdapter(this, lista, new PerfilAdapter.OnPerfilActionListener() {

            @Override
            public void onEditar(MiembroResponseDto perfil, int position) {
                View view = LayoutInflater.from(pantalla_dashboard.this).inflate(R.layout.dialog_editar_perfil, null);
                EditText etNombre = view.findViewById(R.id.etNombre);
                EditText etFecha = view.findViewById(R.id.etFecha);
                Spinner spTipo = view.findViewById(R.id.spTipo);
                CheckBox cbEmbarazada = view.findViewById(R.id.cbEmbarazada);

                boolean isPersona = "persona".equalsIgnoreCase(perfil.getTipo());
                etNombre.setText(perfil.getNombre());
                etFecha.setText(perfil.getFechaNacimiento());

                if (isPersona) {
                    String[] opciones = {"Masculino", "Femenino"};
                    spTipo.setAdapter(new ArrayAdapter<>(pantalla_dashboard.this, android.R.layout.simple_spinner_dropdown_item, opciones));
                    if ("Femenino".equalsIgnoreCase(perfil.getGenero())) {
                        spTipo.setSelection(1);
                        cbEmbarazada.setVisibility(View.VISIBLE);
                        cbEmbarazada.setEnabled(true);
                    } else {
                        spTipo.setSelection(0);
                        cbEmbarazada.setVisibility(View.GONE);
                    }

                    spTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                            if (pos == 0) { // Masculino
                                cbEmbarazada.setChecked(false);
                                cbEmbarazada.setEnabled(false);
                                cbEmbarazada.setVisibility(View.GONE);
                            } else {
                                cbEmbarazada.setEnabled(true);
                                cbEmbarazada.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                } else {
                    String[] opciones = {"Perro", "Gato"};
                    spTipo.setAdapter(new ArrayAdapter<>(pantalla_dashboard.this, android.R.layout.simple_spinner_dropdown_item, opciones));
                    cbEmbarazada.setVisibility(View.GONE);
                    if ("Gato".equalsIgnoreCase(perfil.getEspecie())) {
                        spTipo.setSelection(1);
                    } else {
                        spTipo.setSelection(0);
                    }
                }

                etFecha.setOnClickListener(v -> {
                    Calendar calendar = Calendar.getInstance();
                    new DatePickerDialog(pantalla_dashboard.this, (view1, year, month, dayOfMonth) -> {
                        etFecha.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                });

                AlertDialog dialog = new AlertDialog.Builder(pantalla_dashboard.this)
                        .setTitle("Editar perfil")
                        .setView(view)
                        .setPositiveButton("Actualizar", null)
                        .setNegativeButton("Cancelar", null)
                        .create();

                dialog.setOnShowListener(d -> {
                    Button btn = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    btn.setOnClickListener(v -> {
                        String nuevoNombre = etNombre.getText().toString().trim();
                        String nuevaFechaStr = etFecha.getText().toString().trim();
                        String seleccionSpinner = spTipo.getSelectedItem().toString();

                        if (nuevoNombre.isEmpty() || nuevaFechaStr.isEmpty()) {
                            Toast.makeText(pantalla_dashboard.this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        MiembroDto.ActualizarMiembroDto updateDto = new MiembroDto.ActualizarMiembroDto();
                        updateDto.setNombre(nuevoNombre);
                        updateDto.setNumeroDocumento(perfil.getNumeroDocumento());
                        updateDto.setFotoUrl(perfil.getFotoUrl());

                        try {
                            java.text.SimpleDateFormat formatoInput = new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());
                            java.text.SimpleDateFormat formatoISO = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());

                            java.util.Date fechaParseada = formatoInput.parse(nuevaFechaStr);
                            String fechaFormatoAPI = formatoISO.format(fechaParseada);

                            updateDto.setFechaNacimiento(fechaFormatoAPI);
                        } catch (Exception e) {
                            Toast.makeText(pantalla_dashboard.this, "Formato de fecha inválido", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (isPersona) {
                            updateDto.setGenero(seleccionSpinner);
                            updateDto.setEspecie(null);
                        } else {
                            updateDto.setEspecie(seleccionSpinner);
                            updateDto.setGenero(null);
                        }

                        api.actualizarMiembro(perfil.getId(), updateDto).enqueue(new Callback<RespuestaDto<Void>>() {
                            @Override
                            public void onResponse(Call<RespuestaDto<Void>> call, Response<RespuestaDto<Void>> response) {
                                if (response.isSuccessful() && response.body() != null && response.body().isExito()) {
                                    Toast.makeText(pantalla_dashboard.this, "Perfil actualizado con éxito", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    cargarPerfilesAPI();
                                } else {
                                    // AQUÍ ATRAPAMOS EL 401
                                    if (response.code() == 401) {
                                        dialog.dismiss(); // Ocultamos el cuadro de diálogo
                                        cerrarSesionPorToken();
                                    } else {
                                        Toast.makeText(pantalla_dashboard.this, "Error de servidor al actualizar", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<RespuestaDto<Void>> call, Throwable t) {
                                Toast.makeText(pantalla_dashboard.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    });
                });
                dialog.show();
            }

            @Override
            public void onEliminar(MiembroResponseDto perfil, int position) {
                new AlertDialog.Builder(pantalla_dashboard.this)
                        .setTitle("Eliminar perfil")
                        .setMessage("¿Seguro que deseas eliminar a " + perfil.getNombre() + "? Esta acción no se puede deshacer.")
                        .setPositiveButton("Eliminar", (dialog, which) -> {

                            // DISPARAR LA LLAMADA AL ENDPOINT DELETE EN C#
                            api.eliminarMiembro(perfil.getId()).enqueue(new Callback<RespuestaDto<Object>>() {
                                @Override
                                public void onResponse(Call<RespuestaDto<Object>> call, Response<RespuestaDto<Object>> response) {
                                    if (response.isSuccessful() && response.body() != null && response.body().isExito()) {
                                        Toast.makeText(pantalla_dashboard.this, "Perfil eliminado con éxito", Toast.LENGTH_SHORT).show();
                                        cargarPerfilesAPI();
                                    } else {
                                        // AQUÍ ATRAPAMOS EL 401
                                        if (response.code() == 401) {
                                            cerrarSesionPorToken();
                                        } else {
                                            Toast.makeText(pantalla_dashboard.this, "Error del servidor al eliminar", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<RespuestaDto<Object>> call, Throwable t) {
                                    Toast.makeText(pantalla_dashboard.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            }
        });
        rvPerfiles.setAdapter(adapter);
    }
    // ==========================================
    // MANEJO DE SESIÓN VENCIDA (401)
    // ==========================================
    private void cerrarSesionPorToken() {
        Toast.makeText(this, "Sesión vencida. Por favor, inicia sesión nuevamente.", Toast.LENGTH_LONG).show();

        // Limpiamos los datos
        SessionManager.cerrarSesion();
        getSharedPreferences("user", MODE_PRIVATE).edit().clear().apply();
        getSharedPreferences("AUTH_PREFS", MODE_PRIVATE).edit().clear().apply();

        // Redirigimos al Login matando el historial de pantallas
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
