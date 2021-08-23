package app.andres.contadortresk;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import app.andres.contadortresk.models.TablaContador;
import app.andres.contadortresk.services.Service;
import app.andres.contadortresk.sqlite.AdminSQLiteOpenHelper;
import app.andres.contadortresk.sqlite.Constantes;
import app.andres.contadortresk.sqlite.DatosTabla;



public class DisplayActivity extends AppCompatActivity {

    private TableLayout tableLayout;
    private EditText etOrdeVuelo;
    private ImageButton btnEliminar;
    //variable del Banner
    private AdView mAdView;



    private String[] header = {"ID","Fecha","O/V", "SIIO", "PILOTO","COPILOTO","TOTAL TRIP"};
   // private String[] header = {" "," "," "," "," "};
    private static  int Code = 0;
    private static  int CodeMes = 0;
    private static  int CodeAno = 0;


    DatosTabla datosTabla;




    //para conectar la base de datos  ** Version 2 es con la nueva columna SIIO
    AdminSQLiteOpenHelper admincount = new AdminSQLiteOpenHelper(this, "countBD", null, 2);//base de datos para el contador de horas

    Service service = new Service();


    //--------------------------OnCreate------------------------------------
        @Override
        protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

             //esta linea de codigo evita la rotacion de la patanlla
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


            // Codigo para reducir las medidas del Activity
            DisplayMetrics medidasVentana = new DisplayMetrics(); // instancia para capturar las medidas del display
            getWindowManager().getDefaultDisplay().getMetrics(medidasVentana);

            int ancho = medidasVentana.widthPixels;
            int alto = medidasVentana.heightPixels;

            getWindow().setLayout((int)(ancho * 0.85), (int)(alto * 0.75)); // los decimales son en %



            Code = service.getLocalData_int(DisplayActivity.this, "Code");
            CodeMes = service.getLocalData_int(DisplayActivity.this, "CodeMes");
            CodeAno = service.getLocalData_int(DisplayActivity.this, "CodeAno");


            //id de aplicacion para el uso del banner
            // MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");


            // ID de Pruebas / cambiar en Manifest
            // ca-app-pub-3940256099942544~3347511713
            // ca-app-pub-3940256099942544/6300978111 // ID para el bloque de anuncios archivo XML
            // ads:adUnitId="ca-app-pub-3940256099942544/6300978111"

            // ID Produccion / cambiar en Manifest
            // ca-app-pub-3455471668290750~8132465991
            // ads:adUnitId="ca-app-pub-3455471668290750/1040922967" // ID para el bloque de anuncios archivo XML


            MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });

            // MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

            // SOLO PRUEBAS BANNER
            // codigo para visiualizar Banner en Un equipo: Solo Test
      /*      List<String> testDevices = new ArrayList<>();
            testDevices.add(AdRequest.DEVICE_ID_EMULATOR);
            // asigna el Dispositivo de Prueba
            RequestConfiguration requestConfiguration= new RequestConfiguration.Builder().setTestDeviceIds(testDevices).build();
            MobileAds.setRequestConfiguration(requestConfiguration);
    */
            //codigo del BANNER
            mAdView = findViewById(R.id.AdView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

            tableLayout = (TableLayout) findViewById(R.id.tableLayout);
            etOrdeVuelo = (EditText) findViewById(R.id.etOrdenVuelo);
            btnEliminar = (ImageButton) findViewById(R.id.btnEliminar);

            datosTabla = new DatosTabla(tableLayout, getApplicationContext());

            //foco
            //btnEliminar.requestFocusFromTouch();
            //datosTabla.addHeader(header1);
            //datosTabla.addDatos(getDatos());
            // datosTabla.backgroundHeader(R.color.colorPrimaryDark);
            datosTabla.createTable(header, getDatos());
            datosTabla.backgroundDatos(Color.LTGRAY, Color.GRAY, getDatos(), header.length);
            setCeroCodes();

            //////// ACCIONES DEL BOTÓN /////////////////////
            btnEliminar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // si to_do OK
                    Eliminar(v); // llama el metodo para Actualizar el BackUp

                }
            });
            /////////////////////////////////


    } //End OnCreate





    private void setCeroCodes() {
        CodeAno = 0;
        service.saveLocalData_int(DisplayActivity.this, "CodeAno", CodeAno);
        CodeMes = 0;
        service.saveLocalData_int(DisplayActivity.this, "CodeMes", CodeMes);
        Code = 0;
        service.saveLocalData_int(DisplayActivity.this, "Code", Code);
    }

    ////inicio de los metodos:
    private ArrayList<String[]> getDatos(){

       // rows.add(new String[]{"0","0","0","0","0"});


        TablaContador numeroVuelo = null;
        ArrayList<String[]> rows = new ArrayList<>();
        ArrayList<TablaContador> registroVuelos = new ArrayList<TablaContador>();


        if (CodeAno !=0 && CodeMes == 0 && Code == 0){ ///condicion para consultar el año

            //System.out.println("A "+CodeAno + "/" + CodeMes + "/" + Code);

            //Codigo para efectuar la consulta a la base de datos
            Cursor cursor = admincount.getReadableDatabase().rawQuery("SELECT id, idvuelo, siio, total_tripulacion_hor, total_tripulacion_min, piloto, copiloto, day, month, year FROM " + Constantes.TABLA_CONTADOR + " WHERE year=" + CodeAno, null);
            //codigo para llenar las celdas en la tabla
            while (cursor.moveToNext()){
                numeroVuelo = new TablaContador();

                numeroVuelo.setIdCode(cursor.getString(0));
                numeroVuelo.setNumvuelo(cursor.getString(1));
                numeroVuelo.setNumsiio(cursor.getString(2));
                numeroVuelo.setTotal_tripulacion_hor(cursor.getInt(3));
                numeroVuelo.setTotal_tripulacion_min(cursor.getInt(4));
                numeroVuelo.setNombrePilot(cursor.getString(5));
                numeroVuelo.setNombreCopilot(cursor.getString(6));
                numeroVuelo.setDay(cursor.getInt(7));
                numeroVuelo.setMonth(cursor.getString(8));
                numeroVuelo.setYear(cursor.getString(9));

                registroVuelos.add(numeroVuelo);

            }


            //llama todos los registros almacenados en la base de datos y los presenta en la tabla
            for(int i = 0; i < registroVuelos.size(); i++){


                String hora = registroVuelos.get(i).getTotal_tripulacion_hor() + ":" + registroVuelos.get(i).getTotal_tripulacion_min();

                String fecha = registroVuelos.get(i).getDay() + "/" + registroVuelos.get(i).getMonth() + "/" + registroVuelos.get(i).getYear();



                rows.add(new String[]{registroVuelos.get(i).getIdCode(), fecha, registroVuelos.get(i).getNumvuelo(), registroVuelos.get(i).getNumsiio(), registroVuelos.get(i).getNombrePilot(), registroVuelos.get(i).getNombreCopilot(), hora});

            }




        }else  if(CodeAno !=0 && CodeMes != 0 && Code == 0){ //condicion para consultar año y mes
            //System.out.println("M "+CodeAno + "/" + CodeMes + "/" + Code);

            //Codigo para efectuar la consulta a la base de datos
            Cursor cursor = admincount.getReadableDatabase().rawQuery("SELECT id, idvuelo, siio, total_tripulacion_hor, total_tripulacion_min, piloto, copiloto, day, month, year FROM " + Constantes.TABLA_CONTADOR + " WHERE month=" + CodeMes + " AND year=" +CodeAno, null);
            //codigo para llenar las celdas en la tabla
            while (cursor.moveToNext()){
                numeroVuelo = new TablaContador();

                numeroVuelo.setIdCode(cursor.getString(0));
                numeroVuelo.setNumvuelo(cursor.getString(1));
                numeroVuelo.setNumsiio(cursor.getString(2));
                numeroVuelo.setTotal_tripulacion_hor(cursor.getInt(3));
                numeroVuelo.setTotal_tripulacion_min(cursor.getInt(4));
                numeroVuelo.setNombrePilot(cursor.getString(5));
                numeroVuelo.setNombreCopilot(cursor.getString(6));
                numeroVuelo.setDay(cursor.getInt(7));
                numeroVuelo.setMonth(cursor.getString(8));
                numeroVuelo.setYear(cursor.getString(9));

                registroVuelos.add(numeroVuelo);

            }



            //llama todos los registros almacenados en la base de datos y lo spresenta en la tabla
            for(int i = 0; i < registroVuelos.size(); i++){


                String hora = registroVuelos.get(i).getTotal_tripulacion_hor() + ":" + registroVuelos.get(i).getTotal_tripulacion_min();

                String fecha = registroVuelos.get(i).getDay() + "/" + registroVuelos.get(i).getMonth() + "/" + registroVuelos.get(i).getYear();



                rows.add(new String[]{registroVuelos.get(i).getIdCode(), fecha, registroVuelos.get(i).getNumvuelo(),  registroVuelos.get(i).getNumsiio(), registroVuelos.get(i).getNombrePilot(), registroVuelos.get(i).getNombreCopilot(), hora});

            }



        }else if(CodeAno !=0 && CodeMes != 0 && Code > 0){//condicion para consultar año/mes/dia
            //System.out.println("D "+CodeAno + "/" + CodeMes + "/" + Code);


            //Codigo para efectuar la consulta a la base de datos
            Cursor cursor = admincount.getReadableDatabase().rawQuery("SELECT id, idvuelo, siio, total_tripulacion_hor, total_tripulacion_min, piloto, copiloto, day, month, year FROM " + Constantes.TABLA_CONTADOR + " WHERE day=" + Code + " AND month=" + CodeMes + " AND year=" + CodeAno, null);
            //codigo para llenar las celdas en la tabla
            while (cursor.moveToNext()){
                numeroVuelo = new TablaContador();

                numeroVuelo.setIdCode(cursor.getString(0));
                numeroVuelo.setNumvuelo(cursor.getString(1));
                numeroVuelo.setNumsiio(cursor.getString(2));
                numeroVuelo.setTotal_tripulacion_hor(cursor.getInt(3));
                numeroVuelo.setTotal_tripulacion_min(cursor.getInt(4));
                numeroVuelo.setNombrePilot(cursor.getString(5));
                numeroVuelo.setNombreCopilot(cursor.getString(6));
                numeroVuelo.setDay(cursor.getInt(7));
                numeroVuelo.setMonth(cursor.getString(8));
                numeroVuelo.setYear(cursor.getString(9));

                registroVuelos.add(numeroVuelo);

            }


            //llama todos los registros almacenados en la base de datos y lo spresenta en la tabla
            for(int i = 0; i < registroVuelos.size(); i++){


                String hora = registroVuelos.get(i).getTotal_tripulacion_hor() + ":" + registroVuelos.get(i).getTotal_tripulacion_min();

                String fecha = registroVuelos.get(i).getDay() + "/" + registroVuelos.get(i).getMonth() + "/" + registroVuelos.get(i).getYear();



                rows.add(new String[]{registroVuelos.get(i).getIdCode(), fecha, registroVuelos.get(i).getNumvuelo(), registroVuelos.get(i).getNumsiio(), registroVuelos.get(i).getNombrePilot(), registroVuelos.get(i).getNombreCopilot(), hora});

            }



        }else {
            rows.add(new String[]{"0","0","0","0","0","0", "0"});
            //System.out.println("aqui");
        }

        admincount.cerrarBD(); // cierra BD Sqlite
        return rows;
    }


///////////////////////////////metodo para eliminar vuelo de la Base de datos
public void Eliminar(View view){

//tableLayout.setVisibility(View.INVISIBLE);

    admincount.abrirBD();


    String idCode = etOrdeVuelo.getText().toString();

    if(!idCode.isEmpty()){


        int cantidad = admincount.EliminarDatosContador(idCode);//llama el medoto eliminar que devuelve un numero entero
        admincount.cerrarBD();



        if(cantidad == 1){

                Toast.makeText(this, "Vuelo Eliminado", Toast.LENGTH_SHORT).show();

            horasTotalesHoy();
            finish(); //cierra la actividad Display

        } else {
            Toast.makeText(this, "El Vuelo no existe", Toast.LENGTH_SHORT).show();

        }

    } else {
        Toast.makeText(this, "Debes de introducir el Num Vuelo", Toast.LENGTH_SHORT).show();

    }
finish(); //cierra la actividad Display

}

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


                //mainActivity.tvHorasHoy.setText("►" +Total_Tripulacion+"◄");

            } else {
                Total_Tripulacion = String.valueOf(HorasTotales) + ":" + String.valueOf(MinTotales);

               // mainActivity.tvHorasHoy.setText("►" +Total_Tripulacion+"◄");
            }
        }else

        {
            if (MinTotales <= 9) {//codigo para mejorar la presentacion de los minutos menores a 10
                Total_Tripulacion = String.valueOf(HorasTotales) + ":0" + String.valueOf(MinTotales);

                // mainActivity.tvHorasHoy.setText("►" +Total_Tripulacion+"◄");
            } else {
                Total_Tripulacion = String.valueOf(HorasTotales) + ":" + String.valueOf(MinTotales);

                // mainActivity.tvHorasHoy.setText("►" + Total_Tripulacion +"◄");
            }

        }
    }


} // END Class
