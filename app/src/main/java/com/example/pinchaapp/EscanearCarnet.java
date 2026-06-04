package com.example.pinchaapp;


import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.pinchaapp.database.CarnetViewModel;
import com.example.pinchaapp.database.entities.Carnet;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import org.jspecify.annotations.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class EscanearCarnet extends BasePerfilActivity {

    // ── Room ─────────────────────────────────────────────────────
    private CarnetViewModel viewModel;

    // ── Vistas ───────────────────────────────────────────────────
    private ImageView imgFrontal, imgTrasera;
    private View placeholderFrontal, placeholderTrasera;
    private TextInputEditText etNombrePropietario, etClinica, etFecha;

    // ── URIs de las fotos seleccionadas ──────────────────────────
    private Uri uriFrontal = null;
    private Uri uriTrasera = null;
    private Uri uriCameraTemp = null;


    // ── Códigos de request para galería ──────────────────────────
    private static final int REQ_FRONTAL = 101;
    private static final int REQ_TRASERA = 102;
    private static final int REQ_CAM_FRONTAL = 103;
    private static final int REQ_CAM_TRASERA = 104;
    private static final int CAMERA_PERMISSION_CODE = 200;

    // ── Obligatorios de la base ──────────────────────────────────
    @Override
    protected int getLayoutId()      { return R.layout.activity_escanear_carnet; }

    @Override
    protected int getNavItemActivo() { return R.id.nav_escanear; }

    // ════════════════════════════════════════════════════════════
    @Override
    protected void onPerfilReady() {

        viewModel = new ViewModelProvider(this).get(CarnetViewModel.class);

        inicializarVistas();
        configurarFotos();
        configurarFecha();
        configurarBotonGuardar();
    }

    // ─────────────────────────────────────────────────────────────
    // INICIALIZAR VISTAS
    // ─────────────────────────────────────────────────────────────
    private void inicializarVistas() {
        imgFrontal           = findViewById(R.id.imgFrontal);
        imgTrasera           = findViewById(R.id.imgTrasera);
        placeholderFrontal   = findViewById(R.id.placeholderFrontal);
        placeholderTrasera   = findViewById(R.id.placeholderTrasera);
        etNombrePropietario  = findViewById(R.id.etNombrePropietario);
        etClinica            = findViewById(R.id.etClinica);
        etFecha              = findViewById(R.id.etFecha);

        // Pre-llenar nombre con el perfil activo
        if (nombrePerfil != null)
            etNombrePropietario.setText(nombrePerfil);
    }

    // ─────────────────────────────────────────────────────────────
    // FOTOS — abrir galería al tocar cada recuadro
    // ─────────────────────────────────────────────────────────────
    private void configurarFotos() {
        findViewById(R.id.btnFotoFrontal).setOnClickListener(v ->
                mostrarOpcionesFoto(REQ_FRONTAL));
        findViewById(R.id.btnFotoTrasera).setOnClickListener(v ->
                mostrarOpcionesFoto(REQ_TRASERA));
    }

    private void mostrarOpcionesFoto(int requestCode) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Seleccionar foto")
                .setItems(new String[]{"Tomar foto", "Elegir de galería"}, (dialog, which) -> {
                    if (which == 0) abrirCamara(requestCode);
                    else            abrirGaleria(requestCode);
                })
                .show();
    }

    private void abrirCamara(int requestCode) {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_CODE);

            return;
        }
        lanzarCamara(requestCode);
    }

    private void lanzarCamara(int requestCode) {

        try {

            File archivoFoto = new File(
                    getCacheDir(),
                    "carnet_" + System.currentTimeMillis() + ".jpg");

            uriCameraTemp = FileProvider.getUriForFile(
                    this,
                    "com.example.pinchaapp.provider",
                    archivoFoto);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            intent.putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    uriCameraTemp);

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            List<ResolveInfo> resInfoList =
                    getPackageManager().queryIntentActivities(
                            intent,
                            PackageManager.MATCH_DEFAULT_ONLY);

            for (ResolveInfo resolveInfo : resInfoList) {

                grantUriPermission(
                        resolveInfo.activityInfo.packageName,
                        uriCameraTemp,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
                                Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }

            startActivityForResult(
                    intent,
                    requestCode == REQ_FRONTAL
                            ? REQ_CAM_FRONTAL
                            : REQ_CAM_TRASERA);

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Error: " + e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();

            e.printStackTrace();
        }
    }
    private void abrirGaleria(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data) {

        super.onActivityResult(
                requestCode,
                resultCode,
                data);

        if (resultCode != RESULT_OK)
            return;

        if (requestCode == REQ_CAM_FRONTAL) {

            if (uriCameraTemp != null) {

                uriFrontal = uriCameraTemp;

                mostrarFoto(
                        imgFrontal,
                        placeholderFrontal,
                        uriFrontal);

                uriCameraTemp = null;
            }

            return;
        }

        if (requestCode == REQ_CAM_TRASERA) {

            if (uriCameraTemp != null) {

                uriTrasera = uriCameraTemp;

                mostrarFoto(
                        imgTrasera,
                        placeholderTrasera,
                        uriTrasera);

                uriCameraTemp = null;
            }

            return;
        }

        if (data == null)
            return;

        if (requestCode == REQ_FRONTAL) {

            Uri uri = data.getData();

            if (uri == null)
                return;

            getContentResolver()
                    .takePersistableUriPermission(
                            uri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION);

            uriFrontal = uri;

            mostrarFoto(
                    imgFrontal,
                    placeholderFrontal,
                    uri);

        } else if (requestCode == REQ_TRASERA) {

            Uri uri = data.getData();

            if (uri == null)
                return;

            getContentResolver()
                    .takePersistableUriPermission(
                            uri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION);

            uriTrasera = uri;

            mostrarFoto(
                    imgTrasera,
                    placeholderTrasera,
                    uri);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {

            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(
                        this,
                        "Permiso de cámara concedido",
                        Toast.LENGTH_SHORT
                ).show();

            } else {

                Toast.makeText(
                        this,
                        "Permiso de cámara denegado",
                        Toast.LENGTH_LONG
                ).show();
            }
        }
    }

    private void mostrarFoto(ImageView img, View placeholder, Uri uri) {
        placeholder.setVisibility(View.GONE);
        img.setVisibility(View.VISIBLE);
        Glide.with(this)
                .load(uri)
                .centerCrop()
                .into(img);
    }

    // ─────────────────────────────────────────────────────────────
    // FECHA — DatePickerDialog al tocar el campo
    // ─────────────────────────────────────────────────────────────
    private void configurarFecha() {
        etFecha.setOnClickListener(v -> mostrarDatePicker());

        // También el ícono del calendario dispara el picker
        // (el endIcon del TextInputLayout no tiene id directo,
        //  se maneja tocando el campo que está focusable=false)
    }

    private void mostrarDatePicker() {
        Calendar hoy = Calendar.getInstance();

        // Forzar tema claro independiente del tema de la app
        DatePickerDialog dialog = new DatePickerDialog(
                this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                (view, year, month, day) -> {
                    String fecha = String.format(Locale.getDefault(),
                            "%02d/%02d/%04d", day, month + 1, year);
                    etFecha.setText(fecha);
                },
                hoy.get(Calendar.YEAR),
                hoy.get(Calendar.MONTH),
                hoy.get(Calendar.DAY_OF_MONTH)
        );

        dialog.getDatePicker().setMaxDate(hoy.getTimeInMillis());
        dialog.show();

        // Forzar color visible en los botones
        dialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
                .setTextColor(ContextCompat.getColor(this, R.color.blue));
        dialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
                .setTextColor(ContextCompat.getColor(this, R.color.blue));
    }

    // ─────────────────────────────────────────────────────────────
    // GUARDAR
    // ─────────────────────────────────────────────────────────────
    private void configurarBotonGuardar() {
        findViewById(R.id.btnGuardar).setOnClickListener(v -> guardar());
    }

    private void guardar() {
        // 1. Leer campos
        String nombre  = etNombrePropietario.getText() != null
                ? etNombrePropietario.getText().toString().trim() : "";
        String clinica = etClinica.getText() != null
                ? etClinica.getText().toString().trim() : "";
        String fecha   = etFecha.getText() != null
                ? etFecha.getText().toString().trim() : "";

        // 2. Validar
        if (!validar(nombre, clinica, fecha)) return;

        // 3. Construir entidad
        Carnet carnet = new Carnet(
                idPerfil,
                nombre,
                "Clinico",
                clinica,
                fecha,
                uriFrontal != null ? uriFrontal.toString() : "",
                uriTrasera != null ? uriTrasera.toString() : ""
        );

        // 4. Insertar en Room (hilo background vía ViewModel)
        viewModel.insertar(carnet);

        // 5. Feedback y volver a la lista
        Toast.makeText(this, "Carnet guardado correctamente ✓",
                Toast.LENGTH_SHORT).show();
        finish();
    }


    private boolean validar(String nombre, String clinica, String fecha) {
        if (nombre.isEmpty()) {
            etNombrePropietario.setError("Ingresa el nombre del propietario");
            etNombrePropietario.requestFocus();
            return false;
        }
        if (clinica.isEmpty()) {
            etClinica.setError("Ingresa el nombre de la clínica");
            etClinica.requestFocus();
            return false;
        }
        if (fecha.isEmpty()) {
            etFecha.setError("Selecciona la fecha de registro");
            etFecha.requestFocus();
            return false;
        }
        return true;
    }
}
