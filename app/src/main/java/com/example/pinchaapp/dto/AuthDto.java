package com.example.pinchaapp.dto;

public class AuthDto {

    // (El contenedor maestro se queda vacío de variables, solo guarda las clases internas)

    // =========================================================
    // DTO DE ENTRADA: LOGIN
    // =========================================================
    public static class LoginDto {
        private String correo;
        private String password;

        public LoginDto() {}

        public LoginDto(String correo, String password) {
            this.correo = correo;
            this.password = password;
        }

        public String getCorreo() { return correo; }
        public void setCorreo(String correo) { this.correo = correo; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    // =========================================================
    // DTO DE ENTRADA: REGISTRO
    // =========================================================
    public static class RegistroDto {
        private String nombre;
        private String correo;
        private String password;
        private String telefono;
        private String rol;

        public RegistroDto() {}

        public RegistroDto(String nombre, String correo, String password, String telefono, String rol) {
            this.nombre = nombre;
            this.correo = correo;
            this.password = password;
            this.telefono = telefono;
            this.rol = rol;
        }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public String getCorreo() { return correo; }
        public void setCorreo(String correo) { this.correo = correo; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public String getTelefono() { return telefono; }
        public void setTelefono(String telefono) { this.telefono = telefono; }

        public String getRol() { return rol; }
        public void setRol(String rol) { this.rol = rol; }
    }
    // =========================================================
    // DTO DE ENTRADA: LOGIN CON GOOGLE
    // =========================================================
    public static class GoogleAuthDto {
        private String idToken;

        public GoogleAuthDto() {}

        public GoogleAuthDto(String idToken) {
            this.idToken = idToken;
        }

        public String getIdToken() { return idToken; }
        public void setIdToken(String idToken) { this.idToken = idToken; }
    }
    // =========================================================
    // DTO DE RESPUESTA (Response del Servidor)
    // =========================================================
    public static class AuthResponseDto {
        private String token;
        private String nombre;
        private String correo;
        private String rol;
        private int idUsuario;

        public AuthResponseDto() {}

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public String getCorreo() { return correo; }
        public void setCorreo(String correo) { this.correo = correo; }

        public String getRol() { return rol; }
        public void setRol(String rol) { this.rol = rol; }

        public int getIdUsuario() { return idUsuario; }
        public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    }
}
