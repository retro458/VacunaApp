package com.example.pinchaapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pinchaapp.R;
import com.example.pinchaapp.database.entities.PerfilHumano;
import com.example.pinchaapp.carnet_de_vacunacion;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PerfilAdapter
        extends RecyclerView.Adapter<PerfilAdapter.ViewHolder> {

    Context context;
    List<PerfilHumano> lista;

    public PerfilAdapter(Context context,
                         List<PerfilHumano> lista) {

        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater.from(context)
                .inflate(
                        R.layout.item_perfil,
                        parent,
                        false
                );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {

        PerfilHumano perfil = lista.get(position);

        holder.tvNombre.setText(perfil.getNombre());

        String esquema =
                obtenerEsquema(
                        calcularEdad(perfil.getFechaNacimiento()),
                        perfil.getSexo()
                );

        holder.tvEsquema.setText(esquema);

        aplicarColor(holder, perfil);

        // CLICK DEL PERFIL
        holder.layoutCard.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            context,
                            carnet_de_vacunacion.class
                    );

            intent.putExtra(
                    "idPerfil",
                    perfil.getId()
            );

            intent.putExtra(
                    "nombre",
                    perfil.getNombre()
            );

            intent.putExtra("fechaNacimiento", perfil.getFechaNacimiento());

            intent.putExtra(
                    "sexo",
                    perfil.getSexo()
            );
            intent.putExtra("embarazada", perfil.isEmbarazada());

            context.startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvNombre, tvEsquema;
        LinearLayout layoutCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNombre =
                    itemView.findViewById(R.id.tvNombre);

            tvEsquema =
                    itemView.findViewById(R.id.tvEsquema);

            layoutCard =
                    itemView.findViewById(R.id.layoutCard);
        }
    }

    private int calcularEdad(String fecha) {

        try {

            SimpleDateFormat sdf =
                    new SimpleDateFormat(
                            "dd/MM/yyyy",
                            Locale.getDefault()
                    );

            Date fechaNac = sdf.parse(fecha);

            Calendar nacimiento =
                    Calendar.getInstance();

            nacimiento.setTime(fechaNac);

            Calendar hoy =
                    Calendar.getInstance();

            int edad =
                    hoy.get(Calendar.YEAR)
                            - nacimiento.get(Calendar.YEAR);

            return edad;

        } catch (Exception e) {
            return 0;
        }
    }
    private String obtenerEsquema(
            int edad,
            String sexo
    ) {

        if (edad <= 5) {
            return "Esquema Infantil";
        }

        if (edad < 18) {

            if (sexo.equals("Femenino")) {
                return "Esquema Niñas";
            }

            return "Esquema Niños";
        }

        if (sexo.equals("Femenino")) {
            return "Esquema Mujeres";
        }

        return "Esquema Hombres";
    }
    private void aplicarColor(
            ViewHolder holder,
            PerfilHumano perfil
    ) {

        int edad =
                calcularEdad(
                        perfil.getFechaNacimiento()
                );

        int fondo;
        int colorNombre;
        int colorEsquema;

        if (edad <= 5) {

            fondo = R.color.boy_light;
            colorNombre = R.color.boy_primary;

        } else if (perfil.getSexo().equals("Femenino")) {

            fondo = R.color.female_light;
            colorNombre = R.color.female_primary;

        } else {

            fondo = R.color.male_light;
            colorNombre = R.color.male_primary;
        }

        colorEsquema = R.color.blue_dark;

        holder.layoutCard.setBackgroundColor(
                ContextCompat.getColor(
                        context,
                        fondo
                )
        );

        holder.tvNombre.setTextColor(
                ContextCompat.getColor(
                        context,
                        colorNombre
                )
        );

        holder.tvEsquema.setTextColor(
                ContextCompat.getColor(
                        context,
                        colorEsquema
                )
        );
    }
}
