package com.example.pinchaapp.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.CurrentLocationRequest;

public class UbicacionHelper {

    public static final int REQUEST_PERMISO_UBICACION = 1001;

    // Interfaz para recibir el resultado de forma asíncrona
    public interface UbicacionCallback {
        void onUbicacionObtenida(double latitud, double longitud);
        void onError(String mensaje);
    }

    // Verifica si ya tenemos permiso de ubicación
    public static boolean tienePermiso(Activity activity) {
        return ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    // Pide el permiso al usuario
    public static void pedirPermiso(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                },
                REQUEST_PERMISO_UBICACION);
    }

    // Obtiene la ubicación actual del dispositivo
    public static void obtenerUbicacion(Activity activity, UbicacionCallback callback) {
        if (!tienePermiso(activity)) {
            callback.onError("No hay permiso de ubicación");
            return;
        }

        FusedLocationProviderClient cliente =
                LocationServices.getFusedLocationProviderClient(activity);

        // CurrentLocationRequest fuerza una lectura fresca del GPS
        CurrentLocationRequest request = new CurrentLocationRequest.Builder()
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .build();

        try {
            cliente.getCurrentLocation(request, null)
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            callback.onUbicacionObtenida(
                                    location.getLatitude(),
                                    location.getLongitude());
                        } else {
                            callback.onError("No se pudo obtener la ubicación. "
                                    + "Activa el GPS e intenta de nuevo.");
                        }
                    })
                    .addOnFailureListener(e ->
                            callback.onError("Error de ubicación: " + e.getMessage()));
        } catch (SecurityException e) {
            callback.onError("Permiso de ubicación denegado");
        }
    }
}