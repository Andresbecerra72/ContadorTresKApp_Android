package app.andres.contadortresk.models;

public class TablaContador {
    //estructura de la tabla *****   9 registros
    private String idCode;// codigo ID
    private String numvuelo;// orden de vuelo
    private String numsiio;// numero de SIIO
    private Integer total_tripulacion_hor;
    private Integer total_tripulacion_min;
    private Integer day;
    private Integer week;
    private String month;
    private String year;
    private String nombrePilot;
    private String nombreCopilot;


    public TablaContador() {
        this.numsiio = numsiio;
        this.idCode = idCode;
        this.numvuelo = numvuelo;
        this.total_tripulacion_hor = total_tripulacion_hor;
        this.total_tripulacion_min = total_tripulacion_min;
        this.day = day;
        this.week = week;
        this.month = month;
        this.year = year;
        this.nombrePilot = nombrePilot;
        this.nombreCopilot = nombreCopilot;
    }

    // getters y setters

    public String getNumsiio() {
        return numsiio;
    }

    public void setNumsiio(String numsiio) {
        this.numsiio = numsiio;
    }


    public String getIdCode() {
        return idCode;
    }

    public void setIdCode(String idCode) {
        this.idCode = idCode;
    }

    public String getNumvuelo() {
        return numvuelo;
    }

    public void setNumvuelo(String numvuelo) {
        this.numvuelo = numvuelo;
    }

    public Integer getTotal_tripulacion_hor() {
        return total_tripulacion_hor;
    }

    public void setTotal_tripulacion_hor(Integer total_tripulacion_hor) {
        this.total_tripulacion_hor = total_tripulacion_hor;
    }

    public Integer getTotal_tripulacion_min() {
        return total_tripulacion_min;
    }

    public void setTotal_tripulacion_min(Integer total_tripulacion_min) {
        this.total_tripulacion_min = total_tripulacion_min;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
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

    public String getNombrePilot() {
        return nombrePilot;
    }

    public void setNombrePilot(String nombrePilot) {
        this.nombrePilot = nombrePilot;
    }

    public String getNombreCopilot() {
        return nombreCopilot;
    }

    public void setNombreCopilot(String nombreCopilot) {
        this.nombreCopilot = nombreCopilot;
    }
}
