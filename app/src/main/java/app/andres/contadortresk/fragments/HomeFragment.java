package app.andres.contadortresk.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;
import app.andres.contadortresk.DisplayActivity;
import app.andres.contadortresk.R;
import app.andres.contadortresk.models.TablaContador;
import app.andres.contadortresk.services.Service;
import app.andres.contadortresk.sqlite.AdminSQLiteOpenHelper;
import app.andres.contadortresk.sqlite.Constantes;
import app.andres.contadortresk.utilities.DatePickerFormat;


@SuppressLint({"SetTextI18n", "Recycle"})
public class HomeFragment extends Fragment {


    //variables de los objetos
    private EditText etHorasTotales, etMinTotales, etHorasInsert, etMinInsert, etPilot, etCopilot, etOrdenVuelo, etSiio, etHorasObjetivo;
    private TextView tvActuales, tvFaltantes, tvHorasHoy, tvHorasDias, tvHorasMes, tvHorasAno, tvDerechos;

    private Button btnSet, btInsertar, btnConsulta, datePickerBtn;
    //variables para el calculo de las horas
    private int HorasTotales = 0;
    private int MinTotales = 0;

    //variable Spinner
    private Spinner spDias, spMeses, spAnual;

    private static  int Code = 0;
    private static  int CodeMes = 0;
    private static  int CodeAno = 0;
    private String SelectAno = "";
    private String SelectMes = "";
    private String SelectDia = "";

    //CONSULTA DEL SPINNER
    ArrayList<String> listaVuelosAnos;
    ArrayList<TablaContador> vuelosListAnos;
    ArrayList<String> listaVuelosMes;
    ArrayList<TablaContador> vuelosListMes;
    ArrayList<String> listaVuelosDias;
    ArrayList<TablaContador> vuelosListDias;

    //----------VARIABLES PARA CALCULO DE FECHAS------------------------------------
    public static String years = "";
    public static String months = "";
    public static int weeks = 0;
    public static int days = 0;
    public static int Total_tripulacion_hor = 0;
    public static int Total_tripulacion_min = 0;
    public static String numvuelo = "";
    public static String numsiio = "";
    public static String nombrePilot = "";
    public static String nombreCopilot = "";


    //base de datos
    AdminSQLiteOpenHelper admincount;
    Service service;
    DatePickerFormat datePickerFormat; // Instancia la clase DatePicker
    DatePickerDialog datePickerDialog;


    //-----------------constructor----------------------------
    public HomeFragment() {
        // Required empty public constructor
    }

    //-----------------onCreate----------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ** Version 2 es con la nueva columna SIIO
        admincount = new AdminSQLiteOpenHelper(getContext(), "countBD", null, 2);//base de datos para el contador de horas
        service = new Service();
        datePickerFormat = new DatePickerFormat(); // Instancia la clase DatePicker

    } // End OnCreate


    //-----------------onCreateView----------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Vista del fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //objetos
        etHorasTotales = (EditText) view.findViewById(R.id.etHorasTotales);
        etMinTotales = (EditText) view.findViewById(R.id.etMinTotales);
        etHorasInsert = (EditText) view.findViewById(R.id.etHorasInsert);
        etMinInsert = (EditText) view.findViewById(R.id.etMinInsert);
        etPilot = (EditText) view.findViewById(R.id.etNomPilot);
        etCopilot = (EditText) view.findViewById(R.id.etNomCopilot);
        etOrdenVuelo = (EditText) view.findViewById(R.id.etOrdenVuelo);
        etSiio = (EditText) view.findViewById(R.id.etSiio);
        etHorasObjetivo = (EditText) view.findViewById(R.id.etHorasObjetivo);
        tvActuales = (TextView) view.findViewById(R.id.tvActuales);
        tvFaltantes = (TextView) view.findViewById(R.id.tvFaltantes);
        tvHorasHoy = (TextView) view.findViewById(R.id.tvHorasHoy);
        tvHorasDias = (TextView) view.findViewById(R.id.tvHorasDias);
        tvHorasMes = (TextView) view.findViewById(R.id.tvHorasMes);
        tvHorasAno = (TextView) view.findViewById(R.id.tvHorasAno);
        tvDerechos = (TextView) view.findViewById(R.id.tvDerechos);


        spDias = (Spinner) view.findViewById(R.id.spDias);
        spMeses = (Spinner) view.findViewById(R.id.spMeses);
        spAnual = (Spinner) view.findViewById(R.id.spAnual);

        btnSet = (Button) view.findViewById(R.id.btCargar);
        btInsertar = (Button) view.findViewById(R.id.btInsertar);
        btnConsulta = (Button) view.findViewById(R.id.btDatos);
        datePickerBtn = (Button) view.findViewById(R.id.datePickerBtn); // fecha

        // llama el metono que inicializa el datepicker con la fecha de HOY
        datePickerBtn.setText(datePickerFormat.getTodayDate());

        //mantener datos en el activity
        etHorasTotales.setText(service.getLocalData(getContext(),"HT"));
        etMinTotales.setText(service.getLocalData(getContext(),"MT"));
        tvActuales.setText(service.getLocalData(getContext(),"HA"));
        tvFaltantes.setText(service.getLocalData(getContext(),"HF"));
        tvHorasHoy.setText(service.getLocalData(getContext(),"HH"));
        etHorasObjetivo.setText(service.getLocalData(getContext(),"HO"));

        //codigo para cargar el activity con los datos
        spDias.setEnabled(false);
        spMeses.setEnabled(false);
        btnConsulta.setEnabled(false);

        initDatePicker(); // iniicaliza el datePicker
        //horasTotalesSemana();//CARGA LAS HORAS DE LA QUINCENA
        ConsultarListaVuelosAnos();
        cargarSpinnerAnos();
        ConsultarListaVuelosMes();
        cargarSpinnerMeses();
        ConsultarListaVuelosDias();
        cargarSpinnerDias();
        horasTotalesHoy();



        ///////////////////ACCIONES DE LOS BOTONES Y TV///////////////////////

        btnSet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sethorasTotales(v);
            }
        });

        btInsertar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               InsertarContador(view);
            }
        });

        tvDerechos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                abilitaObjets(v);
            }
        });

        btnConsulta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GetDisplayPage(v);
            }
        });

        datePickerBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openDatePicker(v);
            }
        });



        /////////////////////////////////////////

        //Deteccion de ERRORES antes de cargar los datos del activity
        if ((etHorasTotales.getText().toString().length() != 0) && (etMinTotales.getText().toString().length() != 0)){
            //codigo para mantener desabilitado algunos objetos
            btnSet.setEnabled(false);
            btnConsulta.setEnabled(false);
            etHorasTotales.setEnabled(false);
            etMinTotales.setEnabled(false);
            etHorasObjetivo.setEnabled(false);
            sethorasTotales(null);
            //operacion();// muestra las horas actuales
            horasTotalesHoy();
            //horasFaltantes();
        }else{
            Toast.makeText(this.getContext(), "Ingresar horas Totales TSN", Toast.LENGTH_SHORT).show();
        }

        // Inflate the layout for this fragment
        return view;

    } //End OnCreateView



    // --------------------------------------Metodos---------------------------------------------------------


    ////////////////////////////////////////////////
    //este metodo muestra el total de horas voladas en un solo dia ** DAILY
    private void horasTotalesHoy() {


        //codigo para llamar los datos de la fecha
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+ 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        TablaContador numVueloTotal = null;
        ArrayList<TablaContador> Totales = new ArrayList<TablaContador>();

        //Codigo para efectuar la consulta a la base de datos
        Cursor cursor = admincount.getReadableDatabase().rawQuery("SELECT SUM(total_tripulacion_hor), SUM(total_tripulacion_min) FROM " + Constantes.TABLA_CONTADOR + " WHERE day=" + day + " AND month=" + month + " AND year=" + year, null);
        //variables para los calculos y sumar los totales de la base de dato
        int TotalTripHora = 0;
        int TotalTripMin = 0;
        int HorasTotales = 0;
        int MinTotales = 0;



        while(cursor.moveToNext())

        {

            numVueloTotal = new TablaContador();

            numVueloTotal.setTotal_tripulacion_hor(cursor.getInt(0));
            numVueloTotal.setTotal_tripulacion_min(cursor.getInt(1));


            Totales.add(numVueloTotal);
        }

        for(
                int i = 0; i<Totales.size();i++)

        {

            TotalTripHora = Totales.get(i).getTotal_tripulacion_hor();
            TotalTripMin = Totales.get(i).getTotal_tripulacion_min();


        }

        //variables para mostrar datos en archivo PDF
        String Total_Tripulacion = "";


        HorasTotales =HorasTotales +TotalTripHora;
        MinTotales =MinTotales +TotalTripMin;

        //codigo cuando los minutos son mayores a 60
        if(MinTotales >59)

        { //condicion para Minutos tripulacion

            while (MinTotales > 59) {

                MinTotales = MinTotales - 60;
                HorasTotales = HorasTotales + 1;
            }
            if (MinTotales <= 9) {//codigo para mejorar la presentacion de los minutos menores a 10
                Total_Tripulacion = String.valueOf(HorasTotales) + ":0" + String.valueOf(MinTotales);
                tvHorasHoy.setText("►" +Total_Tripulacion+"◄");

            } else {
                Total_Tripulacion = String.valueOf(HorasTotales) + ":" + String.valueOf(MinTotales);
                tvHorasHoy.setText("►" +Total_Tripulacion+"◄");
            }
        }else

        {
            if (MinTotales <= 9) {//codigo para mejorar la presentacion de los minutos menores a 10
                Total_Tripulacion = String.valueOf(HorasTotales) + ":0" + String.valueOf(MinTotales);
                tvHorasHoy.setText("►" +Total_Tripulacion+"◄");
            } else {
                Total_Tripulacion = String.valueOf(HorasTotales) + ":" + String.valueOf(MinTotales);
                tvHorasHoy.setText("►" + Total_Tripulacion +"◄");
            }

        }


    }


    private void horasTotalesDias() {

        TablaContador numVueloTotal = null;
        ArrayList<TablaContador> Totales = new ArrayList<TablaContador>();

        //Codigo para efectuar la consulta a la base de datos
        Cursor cursor = admincount.getReadableDatabase().rawQuery("SELECT total_tripulacion_hor, total_tripulacion_min FROM " + Constantes.TABLA_CONTADOR + " WHERE day=" + Code + " AND month=" + CodeMes + " AND year=" + CodeAno, null);

        //variables para los calculos y sumar los totales de la base de dato
        int TotalTripHora = 0;
        int TotalTripMin = 0;
        int HorasTotales = 0;
        int MinTotales = 0;




        while(cursor.moveToNext())

        {

            numVueloTotal = new TablaContador();

            numVueloTotal.setTotal_tripulacion_hor(cursor.getInt(0));
            numVueloTotal.setTotal_tripulacion_min(cursor.getInt(1));


            Totales.add(numVueloTotal);
        }

        for( int i = 0; i<Totales.size();i++)

        {

            TotalTripHora = TotalTripHora + Totales.get(i).getTotal_tripulacion_hor();
            TotalTripMin = TotalTripMin + Totales.get(i).getTotal_tripulacion_min();

        }

        //variables para mostrar datos en archivo PDF
        String Total_Tripulacion = "";


        HorasTotales = HorasTotales + TotalTripHora;
        MinTotales = MinTotales + TotalTripMin;

        //codigo cuando los minutos son mayores a 60
        if (MinTotales > 59) { //condicion para Minutos tripulacion

            while (MinTotales > 59) {

                MinTotales = MinTotales - 60;
                HorasTotales = HorasTotales + 1;
            }
            if (MinTotales <= 9) {//codigo para mejorar la presentacion de los minutos menores a 10
                Total_Tripulacion = String.valueOf(HorasTotales) + ":0" + String.valueOf(MinTotales);
                tvHorasDias.setText("►" + Total_Tripulacion + "◄");

            } else {
                Total_Tripulacion = String.valueOf(HorasTotales) + ":" + String.valueOf(MinTotales);
                tvHorasDias.setText("►" + Total_Tripulacion + "◄");
            }
        } else {
            if (MinTotales <= 9) {//codigo para mejorar la presentacion de los minutos menores a 10
                Total_Tripulacion = String.valueOf(HorasTotales) + ":0" + String.valueOf(MinTotales);
                tvHorasDias.setText("►" + Total_Tripulacion + "◄");
            } else {
                Total_Tripulacion = String.valueOf(HorasTotales) + ":" + String.valueOf(MinTotales);
                tvHorasDias.setText("►" + Total_Tripulacion + "◄");
            }

        }




    }

    private void horasTotalesMeses() {

        TablaContador numVueloTotal = null;
        ArrayList<TablaContador> Totales = new ArrayList<TablaContador>();

        //Codigo para efectuar la consulta a la base de datos
        Cursor cursor = admincount.getReadableDatabase().rawQuery("SELECT total_tripulacion_hor, total_tripulacion_min FROM " + Constantes.TABLA_CONTADOR + " WHERE month=" + CodeMes + " AND year=" + CodeAno, null);

        //variables para los calculos y sumar los totales de la base de dato
        int TotalTripHora = 0;
        int TotalTripMin = 0;
        int HorasTotales = 0;
        int MinTotales = 0;




        while(cursor.moveToNext())

        {

            numVueloTotal = new TablaContador();

            numVueloTotal.setTotal_tripulacion_hor(cursor.getInt(0));
            numVueloTotal.setTotal_tripulacion_min(cursor.getInt(1));


            Totales.add(numVueloTotal);
        }

        for( int i = 0; i<Totales.size();i++)

        {

            TotalTripHora = TotalTripHora + Totales.get(i).getTotal_tripulacion_hor();
            TotalTripMin = TotalTripMin + Totales.get(i).getTotal_tripulacion_min();

        }

        //variables para mostrar datos en archivo PDF
        String Total_Tripulacion = "";


        HorasTotales = HorasTotales + TotalTripHora;
        MinTotales = MinTotales + TotalTripMin;

        //codigo cuando los minutos son mayores a 60
        if (MinTotales > 59) { //condicion para Minutos tripulacion

            while (MinTotales > 59) {

                MinTotales = MinTotales - 60;
                HorasTotales = HorasTotales + 1;
            }
            if (MinTotales <= 9) {//codigo para mejorar la presentacion de los minutos menores a 10
                Total_Tripulacion = String.valueOf(HorasTotales) + ":0" + String.valueOf(MinTotales);
                tvHorasMes.setText("►" + Total_Tripulacion + "◄");

            } else {
                Total_Tripulacion = String.valueOf(HorasTotales) + ":" + String.valueOf(MinTotales);
                tvHorasMes.setText("►" + Total_Tripulacion + "◄");
            }
        } else {
            if (MinTotales <= 9) {//codigo para mejorar la presentacion de los minutos menores a 10
                Total_Tripulacion = String.valueOf(HorasTotales) + ":0" + String.valueOf(MinTotales);
                tvHorasMes.setText("►" + Total_Tripulacion + "◄");
            } else {
                Total_Tripulacion = String.valueOf(HorasTotales) + ":" + String.valueOf(MinTotales);
                tvHorasMes.setText("►" + Total_Tripulacion + "◄");
            }

        }



    }

    private void horasTotalesAnos() {

        TablaContador numVueloTotal = null;
        ArrayList<TablaContador> Totales = new ArrayList<TablaContador>();

        //Codigo para efectuar la consulta a la base de datos
        Cursor cursor = admincount.getReadableDatabase().rawQuery("SELECT total_tripulacion_hor, total_tripulacion_min FROM " + Constantes.TABLA_CONTADOR + " WHERE year=" + CodeAno, null);
        //Cursor cursor = admincount.getReadableDatabase().rawQuery("SELECT * FROM "+ Constantes.TABLA_CONTADOR,null);
        //variables para los calculos y sumar los totales de la base de dato
        int TotalTripHora = 0;
        int TotalTripMin = 0;
        int HorasTotales = 0;
        int MinTotales = 0;




        while(cursor.moveToNext())

        {

            numVueloTotal = new TablaContador();

            numVueloTotal.setTotal_tripulacion_hor(cursor.getInt(0));
            numVueloTotal.setTotal_tripulacion_min(cursor.getInt(1));


            Totales.add(numVueloTotal);
        }

        for( int i = 0; i<Totales.size();i++)

        {

            TotalTripHora = TotalTripHora + Totales.get(i).getTotal_tripulacion_hor();
            TotalTripMin = TotalTripMin + Totales.get(i).getTotal_tripulacion_min();

        }

        //variables para mostrar datos en archivo PDF
        String Total_Tripulacion = "";


        HorasTotales = HorasTotales + TotalTripHora;
        MinTotales = MinTotales + TotalTripMin;

        //codigo cuando los minutos son mayores a 60
        if (MinTotales > 59) { //condicion para Minutos tripulacion

            while (MinTotales > 59) {

                MinTotales = MinTotales - 60;
                HorasTotales = HorasTotales + 1;
            }
            if (MinTotales <= 9) {//codigo para mejorar la presentacion de los minutos menores a 10
                Total_Tripulacion = String.valueOf(HorasTotales) + ":0" + String.valueOf(MinTotales);
                tvHorasAno.setText("►" + Total_Tripulacion + "◄");

            } else {
                Total_Tripulacion = String.valueOf(HorasTotales) + ":" + String.valueOf(MinTotales);
                tvHorasAno.setText("►" + Total_Tripulacion + "◄");
            }
        } else {
            if (MinTotales <= 9) {//codigo para mejorar la presentacion de los minutos menores a 10
                Total_Tripulacion = String.valueOf(HorasTotales) + ":0" + String.valueOf(MinTotales);
                tvHorasAno.setText("►" + Total_Tripulacion + "◄");
            } else {
                Total_Tripulacion = String.valueOf(HorasTotales) + ":" + String.valueOf(MinTotales);
                tvHorasAno.setText("►" + Total_Tripulacion + "◄");
            }

        }




    }


    /////////////////////////////////////////////////////////////CODIGO PARA LA SELECCION DE LAS HORAS TOTALES DEL DIA ------------------------------------------------------
    //////////////////////////////////////////
    ///////////////////////////////codigo del SPINNER/////////////////////////


    //objeto SPinner DIA
    private void cargarSpinnerDias() {

        ///////////////////////////////codigo del SPINNER DIA////////////////////////



        //adaptador que permite vincular el Array MatriculaFAC con el spinner y tambien permite vincular el diseño de los items en el spinner "spinner_item_edit"
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getContext(), R.layout.spinner_item_edit, listaVuelosDias);
        spDias.setAdapter(adapter);

        //metodo que permite la seleccion de la aeronave y muestra el TIPO y la matricula HK
        spDias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {

                    CodeAno = Integer.parseInt(SelectAno);
                    service.saveLocalData_int(getContext(), "CodeAno", CodeAno);
                    CodeMes = Integer.parseInt(SelectMes);
                    service.saveLocalData_int(getContext(), "CodeMes", CodeMes);
                    SelectDia = String.valueOf(vuelosListDias.get(position - 1).getDay());
                    Code = Integer.parseInt(SelectDia);
                    service.saveLocalData_int(getContext(), "Code", Code);

                    horasTotalesDias();



                }else {
                    tvHorasDias.setText("D00:00");

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }


    private void cargarSpinnerMeses() {

        ///////////////////////////////codigo del SPINNER MESES/////////////////////////



        //adaptador que permite vincular el Array MatriculaFAC con el spinner y tambien permite vincular el diseño de los items en el spinner "spinner_item_edit"
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getContext(), R.layout.spinner_item_edit, listaVuelosMes);
        spMeses.setAdapter(adapter);

        //metodo que permite la seleccion de la aeronave y muestra el TIPO y la matricula HK
        spMeses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {

                    CodeAno = Integer.parseInt(SelectAno);
                    service.saveLocalData_int(getContext(), "CodeAno", CodeAno);
                    SelectMes = vuelosListMes.get(position - 1).getMonth();
                    CodeMes = Integer.parseInt(SelectMes);
                    service.saveLocalData_int(getContext(), "CodeMes", CodeMes);
                    spDias.setEnabled(true);

                    horasTotalesMeses();



                }else {
                    tvHorasMes.setText("M00:00");

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }


    private void cargarSpinnerAnos() {

        ///////////////////////////////codigo del SPINNER AÑOS/////////////////////////



        //adaptador que permite vincular el Array MatriculaFAC con el spinner y tambien permite vincular el diseño de los items en el spinner "spinner_item_edit"
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getContext(), R.layout.spinner_item_edit, listaVuelosAnos);
        spAnual.setAdapter(adapter);

        //metodo que permite la seleccion de la aeronave y muestra el TIPO y la matricula HK
        spAnual.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {

                    SelectAno = vuelosListAnos.get(position - 1).getYear();
                    CodeAno = Integer.parseInt(SelectAno);
                    service.saveLocalData_int(getContext(), "CodeAno", CodeAno);
                    horasTotalesAnos();
                    spMeses.setEnabled(true);
                    btnConsulta.setEnabled(true);


                }else {
                    tvHorasAno.setText("A00:00");

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }



    // -------------Metodos Adicionales CONSULTAS-------------------------------

    private void ConsultarListaVuelosDias() {

        TablaContador numeroVuelo = null;
        vuelosListDias = new ArrayList<TablaContador>();



        //Codigo para efectuar la consulta a la base de datos
        //Cursor cursor = admincount.getReadableDatabase().rawQuery("SELECT total_tripulacion_hor, total_tripulacion_min FROM " + Constantes.TABLA_CONTADOR + " WHERE month=" + CodeMes, null);
        Cursor cursor = admincount.getReadableDatabase().rawQuery("SELECT DISTINCT day FROM "+ Constantes.TABLA_CONTADOR,null);
        //codigo para con
        while (cursor.moveToNext()){
            numeroVuelo = new TablaContador();


            numeroVuelo.setDay(cursor.getInt(0));



            vuelosListDias.add(numeroVuelo);

        }

        obtenerListaDias();

    }

    private void ConsultarListaVuelosMes() {

        TablaContador numeroVuelo = null;
        vuelosListMes = new ArrayList<TablaContador>();

        //Codigo para efectuar la consulta a la base de datos
        //Cursor cursor = admincount.getReadableDatabase().rawQuery("SELECT total_tripulacion_hor, total_tripulacion_min FROM " + Constantes.TABLA_CONTADOR + " WHERE month=" + CodeMes, null);
        Cursor cursor = admincount.getReadableDatabase().rawQuery("SELECT DISTINCT month FROM "+ Constantes.TABLA_CONTADOR,null);
        //codigo para con
        while (cursor.moveToNext()){
            numeroVuelo = new TablaContador();


            numeroVuelo.setMonth(cursor.getString(0));



            vuelosListMes.add(numeroVuelo);

        }

        obtenerListaMeses();

    }

    private void ConsultarListaVuelosAnos() {

        TablaContador numeroVuelo = null;
        vuelosListAnos = new ArrayList<TablaContador>();

        //Codigo para efectuar la consulta a la base de datos
        Cursor cursor = admincount.getReadableDatabase().rawQuery("SELECT DISTINCT year FROM " + Constantes.TABLA_CONTADOR , null);
        //Cursor cursor = admincount.getReadableDatabase().rawQuery("SELECT * FROM "+ Constantes.TABLA_CONTADOR,null);
        //codigo para con
        while (cursor.moveToNext()){
            numeroVuelo = new TablaContador();


            numeroVuelo.setYear(cursor.getString(0));


            vuelosListAnos.add(numeroVuelo);

        }

        obtenerListaAnos();

    }

    // -----------------LISTAS-------------------------------------
    //obtiene la lista de los vuelos ingresados
    private void obtenerListaDias(){

        listaVuelosDias = new ArrayList<String>();
        listaVuelosDias.add("Ver Días");
        //llama todos los registros almacenados en la base de datos y lo spresenta en la tabla del Archivo PDF
        for(int i = 0; i < vuelosListDias.size(); i++){

            listaVuelosDias.add(String.valueOf(vuelosListDias.get(i).getDay()));



        }
    }

    //obtiene la lista de los vuelos ingresados
    private void obtenerListaMeses(){

        listaVuelosMes = new ArrayList<String>();
        listaVuelosMes.add("Ver Meses");
        //llama todos los registros almacenados en la base de datos y lo spresenta en la tabla del Archivo PDF
        for(int i = 0; i < vuelosListMes.size(); i++){

            listaVuelosMes.add(vuelosListMes.get(i).getMonth());



        }
    }


    //obtiene la lista de los vuelos ingresados
    private void obtenerListaAnos(){

        listaVuelosAnos = new ArrayList<String>();
        listaVuelosAnos.add("Ver Años");
        //llama todos los registros almacenados en la base de datos y lo spresenta en la tabla del Archivo PDF
        for(int i = 0; i < vuelosListAnos.size(); i++){

            listaVuelosAnos.add(vuelosListAnos.get(i).getYear());



        }
    }


    // ------------------------------------------------------

    //Este metodo captura las horas totales
    public void sethorasTotales(View view) {

        dataActivity();


        //variables para captura de datos
        String dataH1 = etHorasTotales.getText().toString();
        String dataH2 = etMinTotales.getText().toString();

        if( dataH1.length() != 0 && dataH2.length() != 0 ) {

            HorasTotales = Integer.parseInt(dataH1);
            MinTotales = Integer.parseInt(dataH2);

            btnSet.setEnabled(false);
            btnSet.setVisibility(View.INVISIBLE);
            etHorasTotales.setEnabled(false);
            etMinTotales.setEnabled(false);
            etHorasObjetivo.setEnabled(false);
        }else{
            Toast.makeText(getContext(), "Ingresa Horas Totales", Toast.LENGTH_SHORT).show();
        }
        //llama los metodos para horas actuales y faltantes
        operacion();
        horasFaltantes();


    }

    //metodo que carga los datos del activity para mantenerlos
    private void dataActivity (){

        //codigo para mostarlos datos y se mantengan en el activity
        service.saveLocalData(getContext(), "HT", etHorasTotales.getText().toString());
        service.saveLocalData(getContext(),"MT", etMinTotales.getText().toString());
        service.saveLocalData(getContext(),"HA", tvActuales.getText().toString());
        service.saveLocalData(getContext(),"HF", tvFaltantes.getText().toString());
        service.saveLocalData(getContext(),"HH", tvHorasHoy.getText().toString());
        service.saveLocalData(getContext(),"HO", etHorasObjetivo.getText().toString());
    }




    /////////////////////////////////calculo de las horas faltantes para cumplir el objetivo//////////////////
    @SuppressLint("SetTextI18n")
    private void horasFaltantes() {

        int objetivo = 0;
        int horasHoy = 0;
        int minHoy = 0;
        int resultHora = 0;
        int resultMin = 0;
        int result = 0;


        if ((etHorasObjetivo.getText().toString().length() != 0) && MinTotales > 0 && MinTotales < 60){

            objetivo=  Integer.parseInt(etHorasObjetivo.getText().toString());
            horasHoy = HorasTotales;
            minHoy = MinTotales;

            resultHora = objetivo - horasHoy -1;
            resultMin = 60 - minHoy;

            if (resultHora < 0){
                tvFaltantes.setText(" :) ");
                Toast.makeText(getContext(), "Buen Trabajo!!", Toast.LENGTH_SHORT).show();
            }else {
                tvFaltantes.setText(String.valueOf(resultHora) + ":" + String.valueOf(resultMin));
            }


        }else if ((etHorasObjetivo.getText().toString().length() != 0) && MinTotales == 0){

            objetivo=  Integer.parseInt(etHorasObjetivo.getText().toString());
            horasHoy = HorasTotales;
            minHoy = MinTotales;

            resultHora = objetivo - horasHoy ;

            if (resultHora < 0){
                tvFaltantes.setText(" :) ");
                Toast.makeText(getContext(), "Buen Trabajo!!", Toast.LENGTH_SHORT).show();
            }else {
                tvFaltantes.setText(String.valueOf(resultHora) + ":" + String.valueOf(resultMin));
            }




        }else if(etHorasObjetivo.getText().toString().length() == 0 && MinTotales > 0 && MinTotales < 60){
            horasHoy = HorasTotales;
            minHoy = MinTotales;

            resultHora = 3000 - horasHoy -1;
            resultMin = 60 - minHoy;

            if (resultHora < 0){
                tvFaltantes.setText(" :) ");
                Toast.makeText(getContext(), "Buen Trabajo!!", Toast.LENGTH_SHORT).show();
            }else {
                tvFaltantes.setText(String.valueOf(resultHora) + ":" + String.valueOf(resultMin));
            }

        }else if(etHorasObjetivo.getText().toString().length() == 0 && MinTotales ==0) {
            horasHoy = HorasTotales;
            minHoy = MinTotales;

            resultHora = 3000 - horasHoy ;

            if (resultHora < 0){
                tvFaltantes.setText(" :) ");
                Toast.makeText(getContext(), "Buen Trabajo!!", Toast.LENGTH_SHORT).show();
            }else {
                tvFaltantes.setText(String.valueOf(resultHora) + ":00");
            }



        }else{
            tvFaltantes.setText("00:00");
        }


    }



    //////metodo que muestra las horas ACTUALES
    private void operacion() {

        TablaContador numVueloTotal = null;
        ArrayList<TablaContador> Totales = new ArrayList<TablaContador>();

        //Codigo para efectuar la consulta a la base de datos
        Cursor cursor = admincount.getReadableDatabase().rawQuery("SELECT SUM(total_tripulacion_hor), SUM(total_tripulacion_min) FROM "+ Constantes.TABLA_CONTADOR,null);
        //variables para los calculos y sumar los totales de la base de dato
        int TotalTripHora = 0;
        int TotalTripMin = 0;


        while (cursor.moveToNext()){

            numVueloTotal = new TablaContador();

            numVueloTotal.setTotal_tripulacion_hor(cursor.getInt(0));
            numVueloTotal.setTotal_tripulacion_min(cursor.getInt(1));


            Totales.add(numVueloTotal);
        }

        for(int i = 0; i < Totales.size(); i++) {

            TotalTripHora = Totales.get(i).getTotal_tripulacion_hor();
            TotalTripMin = Totales.get(i).getTotal_tripulacion_min();


        }

        //variables para mostrar datos en archivo PDF
        String Total_Tripulacion = "";



        HorasTotales = HorasTotales + TotalTripHora;
        MinTotales = MinTotales + TotalTripMin;




        //codigo cuando los minutos son mayores a 60
        if(MinTotales > 59){ //condicion para Minutos tripulacion

            while (MinTotales > 59){

                MinTotales = MinTotales - 60;
                HorasTotales = HorasTotales + 1;
                //System.out.println("AQUI");
            }
            if(MinTotales <= 9){//codigo para mejorar la presentacion de los minutos menores a 10
                Total_Tripulacion = String.valueOf(HorasTotales)+":0"+String.valueOf(MinTotales);
                tvActuales.setText(Total_Tripulacion);
                dataActivity();
            }else{
                Total_Tripulacion = String.valueOf(HorasTotales)+":"+String.valueOf(MinTotales);
                tvActuales.setText(Total_Tripulacion);
                dataActivity();
            }
        }else{
            if(MinTotales <= 9){//codigo para mejorar la presentacion de los minutos menores a 10
                Total_Tripulacion = String.valueOf(HorasTotales)+":0"+String.valueOf(MinTotales);
                tvActuales.setText(Total_Tripulacion);
                dataActivity();
            }else{
                Total_Tripulacion = String.valueOf(HorasTotales)+":"+String.valueOf(MinTotales);
                tvActuales.setText(Total_Tripulacion);
                dataActivity();
            }

        }



    }



    //////////////////////
    //codigo para habilitar los objetos
    public void abilitaObjets(View view) {

        btnSet.setEnabled(true);
        etHorasTotales.setEnabled(true);
        etMinTotales.setEnabled(true);
        etHorasObjetivo.setEnabled(true);
        btnSet.setVisibility(View.VISIBLE);

    }

    ///////////////////////////////////////BASE DE DATOS/////////////////

    //inserta datos en la TABLA CONTADOR------BASE DE DATOS CONTADOR***************************
    public void InsertarContador(View view) {

        // codigo para obtener la fecha desde el DatePicker
        String source = String.valueOf(datePickerBtn.getText()); // obtiene la fecha
        String[] sourceSplit= source.split("/");
        years = sourceSplit[2];
        months = sourceSplit[1];
        weeks = 0;
        days = Integer.parseInt(sourceSplit[0]);


/*      // codigo para obtener la fecha actual
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        years = String.valueOf(year);
        int month = calendar.get(Calendar.MONTH)+ 1;
        months = String.valueOf(month);
        weeks = 0;
        days = calendar.get(Calendar.DAY_OF_MONTH);
*/

        numvuelo = etOrdenVuelo.getText().toString();
        if (numvuelo.length() == 0)
            numvuelo = "N/A";

        numsiio = etSiio.getText().toString();
        if (numsiio.length() == 0)
            numsiio = "N/A";

        nombrePilot = etPilot.getText().toString();
        if (nombrePilot.length() == 0)
            nombrePilot = "N/A";

        nombreCopilot = etCopilot.getText().toString();
        if (nombreCopilot.length() == 0)
            nombreCopilot = "N/A";

        String horasInsert = etHorasInsert.getText().toString();
        String minInsert = etMinInsert.getText().toString();

        if((etHorasInsert.getText().toString().length() != 0) && (etMinInsert.getText().toString().length() != 0)){

            Total_tripulacion_hor = Integer.parseInt(horasInsert);
            Total_tripulacion_min = Integer.parseInt(minInsert);

            admincount.abrirBD();
            admincount.IngresarDatosContador();
            admincount.cerrarBD();
            limpiar(view);
            Toast.makeText(getContext(), "Cargado Correctamente", Toast.LENGTH_SHORT).show();
            horasTotalesHoy();// muestra las horas voladas hoy


            //actualiza los datos de los Spinner
            ConsultarListaVuelosAnos();
            cargarSpinnerAnos();
            ConsultarListaVuelosMes();
            cargarSpinnerMeses();
            ConsultarListaVuelosDias();
            cargarSpinnerDias();
        }else {
            Toast.makeText(getContext(), "Ingresar: Horas - Minutos", Toast.LENGTH_SHORT).show();
        }

        sethorasTotales(null); //carga las horas actuales y faltantes
    }

    //limpia los registros de vuelo
    private void limpiar(View view) {

        etHorasInsert.setText(null);
        etMinInsert.setText(null);
        etOrdenVuelo.setText(null);
        etSiio.setText(null);
        etPilot.setText(null);
        etCopilot.setText(null);

        view.clearFocus();

    }


    public void GetDisplayPage(View view){

        //codigo para cargar el activity con los datos
        spDias.setEnabled(false);
        spMeses.setEnabled(false);
        btnConsulta.setEnabled(false);
        ConsultarListaVuelosAnos();
        cargarSpinnerAnos();
        ConsultarListaVuelosMes();
        cargarSpinnerMeses();
        ConsultarListaVuelosDias();
        cargarSpinnerDias();

        Intent getDisplayactivity = new Intent(getContext(), DisplayActivity.class);
        startActivity(getDisplayactivity);

        // finish();


    }


    // -------------------------------------------------------------------------
    // Metodo para mostrar el DatePicker
    // -------------------------------------------------------------------------
    public void openDatePicker(View view) {

        datePickerDialog.show();

    }

    //------metodo para inicializar el DatePicker----------
    // coloca en el boton el texto con la seleccion de la fecha del DatePicker
    private void initDatePicker() {


        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = datePickerFormat.makeDateString(dayOfMonth, month, year);
                datePickerBtn.setText(date);

            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(getContext(), style, dateSetListener, year, month, day);
        // datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());// limita fechas pasadas

    }




} //END class