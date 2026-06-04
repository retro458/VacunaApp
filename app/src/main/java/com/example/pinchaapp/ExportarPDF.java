package com.example.pinchaapp;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.example.pinchaapp.database.VacunAppDatabase;
import com.example.pinchaapp.dto.HistorialDto;
import com.example.pinchaapp.dto.RespuestaDto;
import com.example.pinchaapp.network.ApiClient;
import com.example.pinchaapp.network.ApiService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExportarPDF extends BasePerfilActivity {

    private static final int PAGE_WIDTH  = 595;   // A4 a 72 DPI
    private static final int PAGE_HEIGHT = 842;
    private static final int MARGEN      = 40;

    VacunAppDatabase db;
    private ApiService api;
    private Button btnGenerarPdf;
    private TextView txtEstado;

    @Override
    protected int getLayoutId()      { return R.layout.activity_exportar_pdf; }

    @Override
    protected int getNavItemActivo() { return R.id.nav_pdf; }

    @Override
    protected void onPerfilReady() {
        db  = VacunAppDatabase.getInstance(this);
        api = ApiClient.getInstance().create(ApiService.class);

        btnGenerarPdf = findViewById(R.id.btnGenerarPdf);
        txtEstado     = findViewById(R.id.txtEstado);

        btnGenerarPdf.setOnClickListener(v -> cargarHistorialYGenerarPdf());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 1. Obtiene el historial de la API y luego genera el PDF
    // ─────────────────────────────────────────────────────────────────────────
    private void cargarHistorialYGenerarPdf() {
        btnGenerarPdf.setEnabled(false);
        txtEstado.setText("Cargando historial…");

        api.getHistorial(idPerfil).enqueue(
                new Callback<RespuestaDto<List<HistorialDto.HistorialResponseDto>>>() {

            @Override
            public void onResponse(
                    Call<RespuestaDto<List<HistorialDto.HistorialResponseDto>>> call,
                    Response<RespuestaDto<List<HistorialDto.HistorialResponseDto>>> response) {

                btnGenerarPdf.setEnabled(true);

                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().isExito()) {
                    generarPdf(response.body().getData());
                } else {
                    txtEstado.setText("No se pudo cargar el historial.");
                    Toast.makeText(ExportarPDF.this,
                            "Sin datos de vacunación para este perfil.",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(
                    Call<RespuestaDto<List<HistorialDto.HistorialResponseDto>>> call,
                    Throwable t) {
                btnGenerarPdf.setEnabled(true);
                txtEstado.setText("Error de conexión.");
                Toast.makeText(ExportarPDF.this,
                        "Error de red: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 2. Genera el PDF con PdfDocument (API nativa de Android)
    // ─────────────────────────────────────────────────────────────────────────
    private void generarPdf(List<HistorialDto.HistorialResponseDto> historial) {
        txtEstado.setText("Generando PDF…");

        PdfDocument documento = new PdfDocument();
        try {
            PdfDocument.PageInfo infoPage = new PdfDocument.PageInfo.Builder(
                    PAGE_WIDTH, PAGE_HEIGHT, 1).create();
            PdfDocument.Page pagina = documento.startPage(infoPage);
            dibujarContenido(pagina.getCanvas(), historial);
            documento.finishPage(pagina);

            File archivo = guardarArchivo(documento);

            if (archivo != null && archivo.length() > 0) {
                txtEstado.setText("PDF guardado: " + archivo.getName());
                abrirPdf(archivo);
            } else {
                txtEstado.setText("Error: el archivo quedó vacío.");
                Log.e("PDF", "El archivo tiene 0 bytes o es null");
            }
        } catch (Exception e) {
            txtEstado.setText("Error al generar el PDF.");
            Log.e("PDF", "Error generando PDF", e);
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            documento.close();
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 3. Dibuja el contenido en el canvas del PDF
    // ─────────────────────────────────────────────────────────────────────────
    private void dibujarContenido(Canvas canvas,
                                  List<HistorialDto.HistorialResponseDto> historial) {
        Paint titulo  = crearPaint(16, true,  Color.parseColor("#1565C0"));
        Paint subtit  = crearPaint(11, true,  Color.parseColor("#424242"));
        Paint normal  = crearPaint(9,  false, Color.parseColor("#212121"));
        Paint cabFila = crearPaint(8,  true,  Color.WHITE);
        Paint linea   = new Paint();
        linea.setColor(Color.parseColor("#BDBDBD"));
        linea.setStrokeWidth(0.5f);

        float y = MARGEN;

        // ── Encabezado ────────────────────────────────────────────
        canvas.drawText("Carnet Digital de Vacunación", MARGEN, y += 20, titulo);
        canvas.drawText("Nombre: " + (nombrePerfil != null ? nombrePerfil : "—"),
                MARGEN, y += 18, subtit);
        canvas.drawText("Edad: " + calcularEdadCompleta(fechaNacimiento),
                MARGEN, y += 14, normal);
        String fechaHoy = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                .format(new Date());
        canvas.drawText("Generado: " + fechaHoy,
                MARGEN, y += 14, normal);

        y += 10;
        canvas.drawLine(MARGEN, y, PAGE_WIDTH - MARGEN, y, linea);
        y += 14;

        // ── Cabecera de la tabla ───────────────────────────────────
        float[] cols = { MARGEN, 185f, 270f, 370f, 460f };
        float altFila = 16f;

        Paint fondoCab = new Paint();
        fondoCab.setColor(Color.parseColor("#1E88E5"));
        canvas.drawRect(MARGEN, y - 12, PAGE_WIDTH - MARGEN, y + 4, fondoCab);

        canvas.drawText("Vacuna",           cols[0], y, cabFila);
        canvas.drawText("Dosis",            cols[1], y, cabFila);
        canvas.drawText("Fecha aplicación", cols[2], y, cabFila);
        canvas.drawText("Próxima dosis",    cols[3], y, cabFila);
        y += altFila;

        // ── Filas ─────────────────────────────────────────────────
        boolean fondoAlternado = false;
        Paint fondoFila = new Paint();

        for (HistorialDto.HistorialResponseDto item : historial) {
            if (y > PAGE_HEIGHT - MARGEN - altFila) break;  // no desborda la página

            fondoFila.setColor(fondoAlternado
                    ? Color.parseColor("#E3F2FD")
                    : Color.WHITE);
            canvas.drawRect(MARGEN, y - 11, PAGE_WIDTH - MARGEN, y + 5, fondoFila);
            fondoAlternado = !fondoAlternado;

            String nombreVac   = item.getVacuna()        != null ? item.getVacuna()        : "—";
            String dosis       = item.getDosisNumero() + "/" + item.getTotalDosis();
            String fechaAplic  = formatearFecha(item.getFechaAplicacion());
            String proxDosis   = formatearFecha(item.getProximaDosis());

            // Truncar nombre si es muy largo
            if (nombreVac.length() > 22) nombreVac = nombreVac.substring(0, 20) + "…";

            canvas.drawText(nombreVac,  cols[0], y, normal);
            canvas.drawText(dosis,      cols[1], y, normal);
            canvas.drawText(fechaAplic, cols[2], y, normal);
            canvas.drawText(proxDosis,  cols[3], y, normal);

            canvas.drawLine(MARGEN, y + 6, PAGE_WIDTH - MARGEN, y + 6, linea);
            y += altFila;
        }

        // ── Pie de página ──────────────────────────────────────────
        Paint pie = crearPaint(7, false, Color.GRAY);
        canvas.drawText("PinchaApp — documento generado automáticamente",
                MARGEN, PAGE_HEIGHT - 20, pie);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 4. Guarda el PdfDocument en almacenamiento externo de la app
    // ─────────────────────────────────────────────────────────────────────────
    private File guardarArchivo(PdfDocument documento) throws IOException {
        File dir = new File(getFilesDir(), "pdfs");
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("No se pudo crear el directorio: " + dir.getAbsolutePath());
        }

        String nombre = "Carnet_"
                + (nombrePerfil != null ? nombrePerfil.replace(" ", "_") : "Perfil")
                + "_" + new SimpleDateFormat("yyyyMMdd_HHmm", Locale.getDefault()).format(new Date())
                + ".pdf";

        File archivo = new File(dir, nombre);
        try (FileOutputStream fos = new FileOutputStream(archivo)) {
            documento.writeTo(fos);
        }

        Log.d("PDF", "Guardado en: " + archivo.getAbsolutePath() + " (" + archivo.length() + " bytes)");
        return archivo;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 5. Abre el PDF con el visor instalado en el dispositivo
    // ─────────────────────────────────────────────────────────────────────────
    private void abrirPdf(File archivo) {
        try {
            Uri uri = FileProvider.getUriForFile(
                    this,
                    "com.example.pinchaapp.provider",
                    archivo);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(intent, "Abrir PDF con…"));
        } catch (Exception e) {
            Toast.makeText(this,
                    "PDF guardado. Ábrelo desde la carpeta Documents.",
                    Toast.LENGTH_LONG).show();
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Utilidades
    // ─────────────────────────────────────────────────────────────────────────
    private Paint crearPaint(float tamano, boolean negrita, int color) {
        Paint p = new Paint();
        p.setTextSize(tamano);
        p.setTypeface(negrita ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
        p.setColor(color);
        p.setAntiAlias(true);
        return p;
    }

    private String formatearFecha(String fechaIso) {
        if (fechaIso == null || fechaIso.trim().isEmpty()) return "—";
        try {
            String limpia = fechaIso.split("\\.")[0].replace("Z", "");
            SimpleDateFormat parser = new SimpleDateFormat(
                    "yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            Date fecha = parser.parse(limpia);
            return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(fecha);
        } catch (Exception e) {
            // Si ya viene en formato simple yyyy-MM-dd
            if (fechaIso.matches("\\d{4}-\\d{2}-\\d{2}")) {
                try {
                    Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            .parse(fechaIso);
                    return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(d);
                } catch (Exception ignored) {}
            }
            return fechaIso;
        }
    }
}
