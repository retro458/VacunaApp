package com.example.pinchaapp.adapters;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pinchaapp.R;
import com.example.pinchaapp.dto.HistorialDto; // Cambiado para apuntar al DTO correcto

import java.util.List;
import java.util.Calendar;
import java.util.Locale;

public class VacunaAdapter extends RecyclerView.Adapter<VacunaAdapter.ViewHolder> {

    private Context context;
    private List<HistorialDto.VacunaHistorialDto> lista; // Actualizado con el nuevo DTO
    private OnVacunaActualizadaListener listener;

    // Interfaz para notificar al Activity cuando se marca aplicada
    public interface OnVacunaActualizadaListener {
        void onMarcadaAplicada(int idHistorial, String fecha);
    }

    public VacunaAdapter(Context context, List<HistorialDto.VacunaHistorialDto> lista,
                         OnVacunaActualizadaListener listener) {
        this.context = context;
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_vacuna, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HistorialDto.VacunaHistorialDto vacuna = lista.get(position);

        holder.txtNombreVacuna.setText(vacuna.getNombreVacuna());
        holder.txtDosis.setText("Dosis " + vacuna.getDosisNumero()
                + " de " + vacuna.getTotalDosis());

        if (vacuna.isAplicada()) {
            holder.txtFecha.setText(vacuna.getFechaAplicacion());
            holder.txtEstado.setText("Completa");
            holder.txtEstado.setTextColor(
                    ContextCompat.getColor(context, android.R.color.holo_green_dark));
            holder.imgEstado.setImageResource(
                    android.R.drawable.checkbox_on_background);
        } else {
            holder.txtFecha.setText(vacuna.getProximaDosis() != null
                    ? "Próxima: " + vacuna.getProximaDosis() : "Sin fecha");
            holder.txtEstado.setText("Pendiente");
            holder.txtEstado.setTextColor(
                    ContextCompat.getColor(context, R.color.skyblue));
            holder.imgEstado.setImageResource(
                    android.R.drawable.checkbox_off_background);
        }

        // CLICK — abrir diálogo para marcar como aplicada
        holder.itemView.setOnClickListener(v -> {
            if (!vacuna.isAplicada()) {
                mostrarDialogAplicar(vacuna, position);
            }
        });
    }

    private void mostrarDialogAplicar(HistorialDto.VacunaHistorialDto vacuna, int position) {
        // DatePicker para elegir fecha
        Calendar calendario = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(
                context,
                (view, year, month, day) -> {
                    String fecha = String.format(
                            Locale.getDefault(),
                            "%02d/%02d/%04d", day, month + 1, year);

                    // Notificar al Activity principal
                    if (listener != null) {
                        listener.onMarcadaAplicada(vacuna.getId(), fecha);
                    }

                    // Actualizar en la lista localmente para refrescar la UI al instante
                    lista.get(position).setAplicada(true);
                    lista.get(position).setFechaAplicacion(fecha);
                    notifyItemChanged(position);
                },
                calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DAY_OF_MONTH)
        );

        dialog.setTitle("¿Cuándo se aplicó " + vacuna.getNombreVacuna() + "?");
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombreVacuna, txtDosis, txtFecha, txtEstado;
        ImageView imgEstado;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombreVacuna = itemView.findViewById(R.id.txtNombreVacuna);
            txtDosis        = itemView.findViewById(R.id.txtDosis);
            txtFecha        = itemView.findViewById(R.id.txtFecha);
            txtEstado       = itemView.findViewById(R.id.txtEstado);
            imgEstado       = itemView.findViewById(R.id.imgEstado);
        }
    }
}