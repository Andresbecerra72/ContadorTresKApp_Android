package app.andres.contadortresk.models;

public class RegistroModel {

    String id;
    String num_vuelo;
    String num_siio;
    String total_trip_hor;
    String total_trip_min;
    String piloto_name;
    String copiloto_name;
    String day;
    String week;
    String month;
    String year;
    String correo_user;
    String backup_name;
    String estado;
    String activo;
    String source;

    public RegistroModel() {
        this.id = id;
        this.num_vuelo = num_vuelo;
        this.num_siio = num_siio;
        this.total_trip_hor = total_trip_hor;
        this.total_trip_min = total_trip_min;
        this.piloto_name = piloto_name;
        this.copiloto_name = copiloto_name;
        this.day = day;
        this.week = week;
        this.month = month;
        this.year = year;
        this.correo_user = correo_user;
        this.backup_name = backup_name;
        this.estado = estado;
        this.activo = activo;
        this.source = source;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNum_vuelo() {
        return num_vuelo;
    }

    public void setNum_vuelo(String num_vuelo) {
        this.num_vuelo = num_vuelo;
    }

    public String getNum_siio() {
        return num_siio;
    }

    public void setNum_siio(String num_siio) {
        this.num_siio = num_siio;
    }

    public String getTotal_trip_hor() {
        return total_trip_hor;
    }

    public void setTotal_trip_hor(String total_trip_hor) {
        this.total_trip_hor = total_trip_hor;
    }

    public String getTotal_trip_min() {
        return total_trip_min;
    }

    public void setTotal_trip_min(String total_trip_min) {
        this.total_trip_min = total_trip_min;
    }

    public String getPiloto_name() {
        return piloto_name;
    }

    public void setPiloto_name(String piloto_name) {
        this.piloto_name = piloto_name;
    }

    public String getCopiloto_name() {
        return copiloto_name;
    }

    public void setCopiloto_name(String copiloto_name) {
        this.copiloto_name = copiloto_name;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCorreo_user() {
        return correo_user;
    }

    public void setCorreo_user(String correo_user) {
        this.correo_user = correo_user;
    }

    public String getBackup_name() {
        return backup_name;
    }

    public void setBackup_name(String backup_name) {
        this.backup_name = backup_name;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getActivo() {
        return activo;
    }

    public void setActivo(String activo) {
        this.activo = activo;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

}// END class
