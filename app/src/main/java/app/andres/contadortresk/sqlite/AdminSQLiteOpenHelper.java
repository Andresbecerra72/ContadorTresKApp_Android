package app.andres.contadortresk.sqlite;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import app.andres.contadortresk.fragments.HomeFragment;
import app.andres.contadortresk.models.TablaContador;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

    // variables
    private static final String DATABASE_ALTER_1 = "ALTER TABLE " + Constantes.TABLA_CONTADOR + " ADD COLUMN " + Constantes.CAMPO_SIIO + " string;";

    // Instancia el modelo
    TablaContador tablaContador = new TablaContador();//codigo para llamar metodos de la clase TablaCOntador

    public AdminSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version); //parametros de la base de datos
    }

    @Override
    public void onCreate(SQLiteDatabase BaseDeDatos) {
        //se crea la tabla
        BaseDeDatos.execSQL(Constantes.CREAR_TABLA_CONTADOR);//parametros de la tabla CONTADOR se encuentran en el paquete constantes

    }

    @Override
    public void onUpgrade(SQLiteDatabase BaseDeDatos, int versionAntigua, int versionNueva) {

        if (versionAntigua < 2) { // codigo para actualizar la tabla ** Version 2 es con la nueva columna SIIO
            BaseDeDatos.execSQL(DATABASE_ALTER_1);
        }

        /*
        if(versionAntigua < versionNueva)  {
            BaseDeDatos.execSQL("DROP TABLE IF EXISTS contador");
            onCreate(BaseDeDatos);

        }
*/
    }


    ////////////abrir base de datos

    public void abrirBD(){
        this.getWritableDatabase();

    }

    ////////////////cerrar base de datos

    public void cerrarBD(){
        this.close();
    }


    ////////////////////////////
    ////////////////////////////////////////**
    ///////////////////////
//METODO PARA INSERTAR DATOS EN LA BASE DE DATOS CONTADOR------------------------------------------
    public void IngresarDatosContador(){

        // HomeActivity mainActivity = new HomeActivity();
        HomeFragment homeFragment = new HomeFragment();


        //este codigo ingresa el valor del ususario del formulario Fullactivity en la clase TotalesVuelos ***TABLA CONTADOR
        tablaContador.setNumvuelo(homeFragment.numvuelo);
        tablaContador.setNumsiio(homeFragment.numsiio);
        tablaContador.setTotal_tripulacion_hor(homeFragment.Total_tripulacion_hor);
        tablaContador.setTotal_tripulacion_min(homeFragment.Total_tripulacion_min);
        tablaContador.setNombrePilot(homeFragment.nombrePilot);
        tablaContador.setNombreCopilot(homeFragment.nombreCopilot);
        tablaContador.setDay(homeFragment.days);
        tablaContador.setWeek(homeFragment.weeks);
        tablaContador.setMonth(homeFragment.months);
        tablaContador.setYear(homeFragment.years);




        //se obtienen o captura los datos de las variables ingresados en la actividad FullActvity desde la clase TablaContador ***TABLA CONTADOR
        String numvuelo = tablaContador.getNumvuelo();
        String numsiio = tablaContador.getNumsiio();
        int Total_Trip_Hora = tablaContador.getTotal_tripulacion_hor();
        int Total_Trip_Min = tablaContador.getTotal_tripulacion_min();
        String nombrePilot = tablaContador.getNombrePilot();
        String nombreCopilot = tablaContador.getNombreCopilot();
        int day = tablaContador.getDay();
        int week = tablaContador.getWeek();
        String month = tablaContador.getMonth();
        String year = tablaContador.getYear();



        //////////////////////////////////
        ContentValues poner = new ContentValues();//BASE DE DATOS CONTADOR
        //se insertan los datos en la base de datos para la TABLA CONTADOR
        poner.put(Constantes.CAMPO_IDVUELO, numvuelo);
        poner.put(Constantes.CAMPO_SIIO, numsiio);
        poner.put(Constantes.CAMPO_TOTAL_TRIP_HORA, Total_Trip_Hora);
        poner.put(Constantes.CAMPO_TOTAL_TRIP_MIN, Total_Trip_Min);
        poner.put(Constantes.CAMPO_PILOT, nombrePilot);
        poner.put(Constantes.CAMPO_COPILOT, nombreCopilot);
        poner.put(Constantes.CAMPO_DAY, day);
        poner.put(Constantes.CAMPO_WEEK, week);
        poner.put(Constantes.CAMPO_MONTH, month);
        poner.put(Constantes.CAMPO_YEAR, year);

        this.getWritableDatabase().insert(Constantes.TABLA_CONTADOR, null, poner);

    }

    ///////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////
    //    //METODO PARA ELIMINAR DATOS EN LA BASE DE DATOS CONTADOR--------------------------------------

    public int EliminarDatosContador(String idCode){


        String[] parametros = {idCode};//parametro para eliminar es el numero de vuelo
        //codigo para eliminar Vuelo en tabla CONTADOR
        int cantidad= this.getWritableDatabase().delete(Constantes.TABLA_CONTADOR, Constantes.ID_CAMPO+"=?",parametros);



        return cantidad;

    }


    ////////////////////////
    /////////////////////////////////////////////**

    //////////////////////////////////////////////////////////////////////////////////////////////
    //    //METODO PARA MODIFICAR DATOS EN LA BASE DE DATOS ------------------------------------------

    public int ModificarDatosContador(String numvuelo){

        HomeFragment homeFragment = new HomeFragment();


        //este codigo ingresa el valor del ususario del formulario Fullactivity en la clase TotalesVuelos ***TABLA CONTADOR
        tablaContador.setNumvuelo(homeFragment.numvuelo);
        tablaContador.setTotal_tripulacion_hor(homeFragment.Total_tripulacion_hor);
        tablaContador.setTotal_tripulacion_min(homeFragment.Total_tripulacion_min);
        tablaContador.setNombrePilot(homeFragment.nombrePilot);
        tablaContador.setNombreCopilot(homeFragment.nombreCopilot);
        tablaContador.setDay(homeFragment.days);
        tablaContador.setWeek(homeFragment.weeks);
        tablaContador.setMonth(homeFragment.months);
        tablaContador.setYear(homeFragment.years);



        //se obtienen o captura los datos de las variables ingresados en la actividad FullActvity desde la clase TablaContador ***TABLA CONTADOR

        int Total_Trip_Hora = tablaContador.getTotal_tripulacion_hor();
        int Total_Trip_Min = tablaContador.getTotal_tripulacion_min();
        String nombrePilot = tablaContador.getNombrePilot();
        String nombreCopilot = tablaContador.getNombreCopilot();
        int day = tablaContador.getDay();
        int week = tablaContador.getWeek();
        String month = tablaContador.getMonth();
        String year = tablaContador.getYear();



        //////////////////////////////////
        ContentValues poner = new ContentValues();//BASE DE DATOS CONTADOR
        //se insertan los datos en la base de datos para la TABLA CONTADOR
        poner.put(Constantes.CAMPO_IDVUELO, numvuelo);
        poner.put(Constantes.CAMPO_TOTAL_TRIP_HORA, Total_Trip_Hora);
        poner.put(Constantes.CAMPO_TOTAL_TRIP_MIN, Total_Trip_Min);
        poner.put(Constantes.CAMPO_PILOT, nombrePilot);
        poner.put(Constantes.CAMPO_COPILOT, nombreCopilot);
        poner.put(Constantes.CAMPO_DAY, day);
        poner.put(Constantes.CAMPO_WEEK, week);
        poner.put(Constantes.CAMPO_MONTH, month);
        poner.put(Constantes.CAMPO_YEAR, year);

        String[] parametros = {numvuelo};//parametro para modificar los datos del numero de vuelo

        //se MODIFICAN los datos en la BASE de datos TABLA RADIO
        int cantidad = this.getWritableDatabase().update(Constantes.TABLA_CONTADOR, poner, Constantes.CAMPO_IDVUELO+"=?", parametros);



        return cantidad;
    }
    ////////////////////////////


}

