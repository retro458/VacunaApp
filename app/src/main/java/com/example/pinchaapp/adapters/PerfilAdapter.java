package com.example.pinchaapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pinchaapp.R;
import com.example.pinchaapp.carnet_de_vacunacion;
import com.example.pinchaapp.dto.MiembroDto.MiembroResponseDto;

import java.util.List;

public class PerfilAdapter extends RecyclerView.Adapter<PerfilAdapter.ViewHolder> {

    private final Context context;
    private final List<MiembroResponseDto> lista;
    private final OnPerfilActionListener listener;

    public interface OnPerfilActionListener {
        void onEditar(MiembroResponseDto perfil, int position);
        void onEliminar(MiembroResponseDto perfil, int position);
    }

    public PerfilAdapter(Context context, List<MiembroResponseDto> lista, OnPerfilActionListener listener) {
        this.context = context;
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_perfil, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MiembroResponseDto miembro = lista.get(position);
        holder.tvNombre.setText(miembro.getNombre());

        boolean isPersona = "persona".equalsIgnoreCase(miembro.getTipo());

        if (isPersona) {
            // =========================
            // PERFIL HUMANO
            // =========================
            String esquema = obtenerEsquemaHumano(miembro);
            holder.tvEsquema.setText(esquema);
            aplicarColorHumano(holder, miembro);
        } else {
            // =========================
            // PERFIL MASCOTA
            // =========================
            holder.tvEsquema.setText(miembro.getEspecie() != null ? miembro.getEspecie() : "Mascota");
            holder.layoutCard.setBackgroundColor(ContextCompat.getColor(context, R.color.pet_light));
            holder.tvNombre.setTextColor(ContextCompat.getColor(context, R.color.pet_dark));
            holder.tvEsquema.setTextColor(ContextCompat.getColor(context, R.color.blue_dark));
        }

        // Navegación al carnet
        holder.layoutCard.setOnClickListener(v -> {
            Intent intent = new Intent(context, carnet_de_vacunacion.class);
            intent.putExtra("idPerfil", miembro.getId());
            intent.putExtra("nombre", miembro.getNombre());
            intent.putExtra("fechaNacimiento", miembro.getFechaNacimiento());
            intent.putExtra("tipoPerfil", miembro.getTipo());
            if (isPersona) {
                intent.putExtra("sexo", miembro.getGenero());
            }
            context.startActivity(intent);
        });

        // Opciones de Editar/Eliminar
        holder.btnOpciones.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(context, v);
            popup.getMenuInflater().inflate(R.menu.menu_opciones_perfil, popup.getMenu());
            popup.setOnMenuItemClickListener(menuItem -> {
                int id = menuItem.getItemId();
                if (id == R.id.op_editar) {
                    listener.onEditar(miembro, holder.getAdapterPosition());
                    return true;
                } else if (id == R.id.op_eliminar) {
                    listener.onEliminar(miembro, holder.getAdapterPosition());
                    return true;
                }
                return false;
            });
            popup.show();
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void eliminarItem(int position) {
        lista.remove(position);
        notifyItemRemoved(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvEsquema;
        LinearLayout layoutCard;
        ImageButton btnOpciones;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvEsquema = itemView.findViewById(R.id.tvEsquema);
            layoutCard = itemView.findViewById(R.id.layoutCard);
            btnOpciones = itemView.findViewById(R.id.btnOpciones);
        }
    }

    // ==========================================
    // LÓGICA VISUAL BASADA EN DTO
    // ==========================================
    private String obtenerEsquemaHumano(MiembroResponseDto perfil) {
        int edad = perfil.getEdad() != null ? perfil.getEdad() : 0;
        String genero = perfil.getGenero() != null ? perfil.getGenero() : "";

        if (edad <= 5) return "Esquema Infantil";
        if (edad < 18) return genero.equalsIgnoreCase("Femenino") ? "Esquema Niñas" : "Esquema Niños";
        return genero.equalsIgnoreCase("Femenino") ? "Esquema Mujeres" : "Esquema Hombres";
    }

    private void aplicarColorHumano(ViewHolder holder, MiembroResponseDto perfil) {
        int edad = perfil.getEdad() != null ? perfil.getEdad() : 0;
        String genero = perfil.getGenero() != null ? perfil.getGenero() : "";
        int fondo, colorNombre;

        if (edad <= 5) {
            fondo = R.color.baby_light;
            colorNombre = R.color.baby_primary;
        } else if (edad <= 17) {
            if (genero.equalsIgnoreCase("Femenino")) {
                fondo = R.color.girl_light;
                colorNombre = R.color.girl_primary;
            } else {
                fondo = R.color.boy_light;
                colorNombre = R.color.boy_primary;
            }
        } else if (genero.equalsIgnoreCase("Femenino")) {
            fondo = R.color.female_light;
            colorNombre = R.color.female_primary;
        } else {
            fondo = R.color.male_light;
            colorNombre = R.color.male_primary;
        }

        holder.layoutCard.setBackgroundColor(ContextCompat.getColor(context, fondo));
        holder.tvNombre.setTextColor(ContextCompat.getColor(context, colorNombre));
        holder.tvEsquema.setTextColor(ContextCompat.getColor(context, R.color.blue_dark));
    }
}