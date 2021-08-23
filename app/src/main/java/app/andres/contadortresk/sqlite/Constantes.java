package app.andres.contadortresk.sqlite;

//representa los campos de la base de datos

public class Constantes {


//TABLA CONTADOR

    public static final String TABLA_CONTADOR = "contador";
    public static final String ID_CAMPO = "id";
    public static final String CAMPO_IDVUELO = "idvuelo";
    public static final String CAMPO_SIIO = "siio";
    public static final String CAMPO_TOTAL_TRIP_HORA= "total_tripulacion_hor";
    public static final String CAMPO_TOTAL_TRIP_MIN= "total_tripulacion_min";
    public static final String CAMPO_PILOT= "piloto";
    public static final String CAMPO_COPILOT= "copiloto";
    public static final String CAMPO_DAY= "day";
    public static final String CAMPO_WEEK= "week";
    public static final String CAMPO_MONTH= "month";
    public static final String CAMPO_YEAR= "year";




    public static final String CREAR_TABLA_CONTADOR = "CREATE TABLE "+TABLA_CONTADOR+"("+ID_CAMPO+" INTEGER primary key AUTOINCREMENT, "+CAMPO_IDVUELO+" TEXT, "+CAMPO_SIIO+" TEXT, "+CAMPO_TOTAL_TRIP_HORA+" INTEGER, "+CAMPO_TOTAL_TRIP_MIN+" INTEGER, "+CAMPO_PILOT+" TEXT, "+CAMPO_COPILOT+" TEXT, "+CAMPO_DAY+" INTEGER, "+CAMPO_WEEK+" INTEGER, "+CAMPO_MONTH+" INTEGER, "+CAMPO_YEAR+" INTEGER );";

    //TABLA AÑOS

    public static final String TABLA_YEAR = "year";

    public static final String CREAR_TABLA_YEAR = "CREATE TABLE "+TABLA_YEAR+"("+CAMPO_YEAR+" INTEGER primary key, "+CAMPO_DAY+" INTEGER, "+CAMPO_WEEK+" INTEGER, "+CAMPO_MONTH+" INTEGER, "+CAMPO_TOTAL_TRIP_HORA+" INTEGER, "+CAMPO_TOTAL_TRIP_MIN+" INTEGER)";

    //TABLA MESES

    public static final String TABLA_MONTH = "month";

    public static final String CREAR_TABLA_MONTH = "CREATE TABLE "+TABLA_MONTH+"("+CAMPO_MONTH+" INTEGER primary key, "+CAMPO_DAY+" INTEGER, "+CAMPO_WEEK+" INTEGER, "+CAMPO_YEAR+" INTEGER, "+CAMPO_TOTAL_TRIP_HORA+" INTEGER, "+CAMPO_TOTAL_TRIP_MIN+" INTEGER)";

    //TABLA SEMANAS

    public static final String TABLA_WEEK = "week";

    public static final String CREAR_TABLA_WEEK = "CREATE TABLE "+TABLA_WEEK+"("+CAMPO_WEEK+" INTEGER primary key, "+CAMPO_DAY+" INTEGER, "+CAMPO_MONTH+" INTEGER, "+CAMPO_YEAR+" INTEGER, "+CAMPO_TOTAL_TRIP_HORA+" INTEGER, "+CAMPO_TOTAL_TRIP_MIN+" INTEGER)";

    //TABLA DIAS

    public static final String TABLA_DAY = "week";

    public static final String CREAR_TABLA_DAY = "CREATE TABLE "+TABLA_DAY+"("+CAMPO_DAY+" INTEGER primary key, "+CAMPO_WEEK+" INTEGER, "+CAMPO_MONTH+" INTEGER, "+CAMPO_YEAR+" INTEGER, "+CAMPO_TOTAL_TRIP_HORA+" INTEGER, "+CAMPO_TOTAL_TRIP_MIN+" INTEGER)";


}
