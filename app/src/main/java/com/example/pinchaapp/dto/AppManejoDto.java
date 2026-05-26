package com.example.pinchaapp.dto;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class AppManejoDto {

    // ============================================================
    // CERTIFICADO QR / PDF
    // ============================================================
    public static class CertificadoResponseDto {
        private int id;
        private int idMiembro;
        private String codigoQr;
        private Date fechaEmision;
        private String urlPdf;
        private String nombreMiembro;

        public CertificadoResponseDto() {}

        // Getters y Setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public int getIdMiembro() { return idMiembro; }
        public void setIdMiembro(int idMiembro) { this.idMiembro = idMiembro; }
        public String getCodigoQr() { return codigoQr; }
        public void setCodigoQr(String codigoQr) { this.codigoQr = codigoQr; }
        public Date getFechaEmision() { return fechaEmision; }
        public void setFechaEmision(Date fechaEmision) { this.fechaEmision = fechaEmision; }
        public String getUrlPdf() { return urlPdf; }
        public void setUrlPdf(String urlPdf) { this.urlPdf = urlPdf; }
        public String getNombreMiembro() { return nombreMiembro; }
        public void setNombreMiembro(String nombreMiembro) { this.nombreMiembro = nombreMiembro; }
    }

    public static class CrearCertificadoDto {
        private int idMiembro;
        private String codigoQr;
        private String urlPdf;

        public CrearCertificadoDto() {}

        public int getIdMiembro() { return idMiembro; }
        public void setIdMiembro(int idMiembro) { this.idMiembro = idMiembro; }
        public String getCodigoQr() { return codigoQr; }
        public void setCodigoQr(String codigoQr) { this.codigoQr = codigoQr; }
        public String getUrlPdf() { return urlPdf; }
        public void setUrlPdf(String urlPdf) { this.urlPdf = urlPdf; }
    }

    // ============================================================
    // DISPOSITIVO USUARIO (Push Notifications)
    // ============================================================
    public static class RegistrarDispositivoDto {
        private String tokenPush;
        private String plataforma; // android | ios

        public RegistrarDispositivoDto() {}

        public String getTokenPush() { return tokenPush; }
        public void setTokenPush(String tokenPush) { this.tokenPush = tokenPush; }
        public String getPlataforma() { return plataforma; }
        public void setPlataforma(String plataforma) { this.plataforma = plataforma; }
    }

    public static class DispositivoResponseDto {
        private int id;
        private String tokenPush;
        private String plataforma;
        private boolean activo;
        private Date fechaRegistro;

        public DispositivoResponseDto() {}

        // Getters y Setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getTokenPush() { return tokenPush; }
        public void setTokenPush(String tokenPush) { this.tokenPush = tokenPush; }
        public String getPlataforma() { return plataforma; }
        public void setPlataforma(String plataforma) { this.plataforma = plataforma; }
        public boolean isActivo() { return activo; }
        public void setActivo(boolean activo) { this.activo = activo; }
        public Date getFechaRegistro() { return fechaRegistro; }
        public void setFechaRegistro(Date fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    }

    // ============================================================
    // DASHBOARDS
    // ============================================================
    public static class DashboardAdminResponseDto {
        @SerializedName("totalUsuarios")
        private int totalUsuarios;

        @SerializedName("totalMiembros")
        private int totalMiembros;

        @SerializedName("totalVacunaciones")
        private int totalVacunaciones;

        @SerializedName("recordatoriosPendientes")
        private int recordatoriosPendientes;

        @SerializedName("campaniasActivas")
        private int campaniasActivas;

        @SerializedName("centrosActivos")
        private int centrosActivos;
        public DashboardAdminResponseDto() {}

        // Getters y Setters
        public int getTotalUsuarios() { return totalUsuarios; }
        public void setTotalUsuarios(int totalUsuarios) { this.totalUsuarios = totalUsuarios; }
        public int getTotalMiembros() { return totalMiembros; }
        public void setTotalMiembros(int totalMiembros) { this.totalMiembros = totalMiembros; }
        public int getTotalVacunaciones() { return totalVacunaciones; }
        public void setTotalVacunaciones(int totalVacunaciones) { this.totalVacunaciones = totalVacunaciones; }
        public int getRecordatoriosPendientes() { return recordatoriosPendientes; }
        public void setRecordatoriosPendientes(int recordatoriosPendientes) { this.recordatoriosPendientes = recordatoriosPendientes; }
        public int getCampaniasActivas() { return campaniasActivas; }
        public void setCampaniasActivas(int campaniasActivas) { this.campaniasActivas = campaniasActivas; }
        public int getCentrosActivos() { return centrosActivos; }
        public void setCentrosActivos(int centrosActivos) { this.centrosActivos = centrosActivos; }
    }

    public static class VacunacionMesResponseDto {
        private String mesNumero;
        private String mesNombre;
        private String vacuna;
        private int total;

        public VacunacionMesResponseDto() {}

        // Getters y Setters
        public String getMesNumero() { return mesNumero; }
        public void setMesNumero(String mesNumero) { this.mesNumero = mesNumero; }
        public String getMesNombre() { return mesNombre; }
        public void setMesNombre(String mesNombre) { this.mesNombre = mesNombre; }
        public String getVacuna() { return vacuna; }
        public void setVacuna(String vacuna) { this.vacuna = vacuna; }
        public int getTotal() { return total; }
        public void setTotal(int total) { this.total = total; }
    }

    public static class CoberturaVacunaResponseDto {
        private String vacuna;
        private String tipoVacuna;
        private String fabricante;
        private int totalAplicaciones;
        private int miembrosVacunados;

        public CoberturaVacunaResponseDto() {}

        // Getters y Setters
        public String getVacuna() { return vacuna; }
        public void setVacuna(String vacuna) { this.vacuna = vacuna; }
        public String getTipoVacuna() { return tipoVacuna; }
        public void setTipoVacuna(String tipoVacuna) { this.tipoVacuna = tipoVacuna; }
        public String getFabricante() { return fabricante; }
        public void setFabricante(String fabricante) { this.fabricante = fabricante; }
        public int getTotalAplicaciones() { return totalAplicaciones; }
        public void setTotalAplicaciones(int totalAplicaciones) { this.totalAplicaciones = totalAplicaciones; }
        public int getMiembrosVacunados() { return miembrosVacunados; }
        public void setMiembrosVacunados(int miembrosVacunados) { this.miembrosVacunados = miembrosVacunados; }
    }

    public static class DashboardUsuarioResponseDto {
        private int totalMiembros;
        private int totalVacunaciones;
        private int recordatoriosPendientes;
        private int proximasDosis30Dias;
        private int campaniasProximas;

        public DashboardUsuarioResponseDto() {}

        // Getters y Setters
        public int getTotalMiembros() { return totalMiembros; }
        public void setTotalMiembros(int totalMiembros) { this.totalMiembros = totalMiembros; }
        public int getTotalVacunaciones() { return totalVacunaciones; }
        public void setTotalVacunaciones(int totalVacunaciones) { this.totalVacunaciones = totalVacunaciones; }
        public int getRecordatoriosPendientes() { return recordatoriosPendientes; }
        public void setRecordatoriosPendientes(int recordatoriosPendientes) { this.recordatoriosPendientes = recordatoriosPendientes; }
        public int getProximasDosis30Dias() { return proximasDosis30Dias; }
        public void setProximasDosis30Dias(int proximasDosis30Dias) { this.proximasDosis30Dias = proximasDosis30Dias; }
        public int getCampaniasProximas() { return campaniasProximas; }
        public void setCampaniasProximas(int campaniasProximas) { this.campaniasProximas = campaniasProximas; }
    }

    // ============================================================
    // PERFIL COMPLETO MIEMBRO (Compound View)
    // ============================================================
    public static class PerfilMiembroResponseDto {
        private MiembroDto.MiembroResponseDto miembro = new MiembroDto.MiembroResponseDto();
        private List<MiembroDto.MiembroAlergiaResponseDto> alergias;
        private ImcDto.ImcResponseDto ultimoImc;
        private List<HistorialDto.HistorialResponseDto> historial;
        private List<RecordatorioDto.RecordatorioResponseDto> recordatorios;

        public PerfilMiembroResponseDto() {}

        // Getters y Setters
        public MiembroDto.MiembroResponseDto getMiembro() { return miembro; }
        public void setMiembro(MiembroDto.MiembroResponseDto miembro) { this.miembro = miembro; }
        public List<MiembroDto.MiembroAlergiaResponseDto> getAlergias() { return alergias; }
        public void setAlergias(List<MiembroDto.MiembroAlergiaResponseDto> alergias) { this.alergias = alergias; }
        public ImcDto.ImcResponseDto getUltimoImc() { return ultimoImc; }
        public void setUltimoImc(ImcDto.ImcResponseDto ultimoImc) { this.ultimoImc = ultimoImc; }
        public List<HistorialDto.HistorialResponseDto> getHistorial() { return historial; }
        public void setHistorial(List<HistorialDto.HistorialResponseDto> historial) { this.historial = historial; }
        public List<RecordatorioDto.RecordatorioResponseDto> getRecordatorios() { return recordatorios; }
        public void setRecordatorios(List<RecordatorioDto.RecordatorioResponseDto> recordatorios) { this.recordatorios = recordatorios; }
    }
}