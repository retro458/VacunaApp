package com.example.pinchaapp.session;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME    = "ApiVacunas";
    private static final String KEY_TOKEN    = "jwt_token";
    private static final String KEY_USUARIO  = "id_usuario";
    private static final String KEY_NOMBRE   = "nombre_usuario";
    private static final String KEY_CORREO   = "correo_usuario";
    private static final String KEY_ROL      = "rol_usuario";
    private static SharedPreferences prefs;

    // Inicialización limpia desde el inicio de la app
    public static void init(Context context) {
        if (prefs == null) {
            prefs = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }
    }

    // Método que pide el MainActivity para guardar todo de un solo golpe
    public static void guardarSesion(String token, int idUsuario, String nombre, String correo, String rol) {
        if (prefs != null) {
            prefs.edit()
                    .putString(KEY_TOKEN, token)
                    .putInt(KEY_USUARIO, idUsuario)
                    .putString(KEY_NOMBRE, nombre)
                    .putString(KEY_CORREO, correo)
                    .putString(KEY_ROL, rol)
                    .apply();
        }
    }

    //  Método que pide el MainActivity para verificar si saltarse el Login
    public static boolean haySesion() {
        if (prefs == null) return false;
        // Si el token no está vacío, significa que hay una sesión activa
        return !prefs.getString(KEY_TOKEN, "").isEmpty();
    }

    public static String getToken() {
        if (prefs == null) return "";
        return prefs.getString(KEY_TOKEN, "");
    }

    public static int getIdUsuario() {
        if (prefs == null) return -1;
        return prefs.getInt(KEY_USUARIO, -1);
    }

    public static String getNombre() {
        if (prefs == null) return "";
        return prefs.getString(KEY_NOMBRE, "");
    }

    public static String getCorreo() {
        if (prefs == null) return "";
        return prefs.getString(KEY_CORREO, "");
    }

    public static String getRol() {
        if (prefs == null) return "";
        return prefs.getString(KEY_ROL, "");
    }

    public static void cerrarSesion() {
        if (prefs != null) {
            prefs.edit().clear().apply();
        }
    }
}