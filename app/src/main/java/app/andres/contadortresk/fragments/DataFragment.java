package app.andres.contadortresk.fragments;


import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import app.andres.contadortresk.R;
import app.andres.contadortresk.models.RegistroModel;
import app.andres.contadortresk.models.TablaContador;
import app.andres.contadortresk.services.Service;
import app.andres.contadortresk.sqlite.AdminSQLiteOpenHelper;
import app.andres.contadortresk.sqlite.Constantes;
import app.andres.contadortresk.sqlite.DatosTabla;
import app.andres.contadortresk.utilities.VolleyCallback;


public class DataFragment extends Fragment {


    // Objetos
    private CardView cvRegistro, cvActualizar, cvEnviarCorreo;
    private TextView tvCorreo_user, tvBackup_name;
    private EditText etCorreo, etBackup, etCorreo_send;
    private Button btnEnviar, btnActualizar, btnEnviar_correo;
    private ProgressBar progressBar;

    //variable del Banner
    private AdView mAdView;

    //para conectar la base de datos
    AdminSQLiteOpenHelper admincount;
    Service service;


    //-----------------constructor----------------------------
    public DataFragment() {
        // Required empty public constructor
    }


    //-----------------onCreate----------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         // ** Version 2 es con la nueva columna SIIO
        admincount = new AdminSQLiteOpenHelper(getContext(), "countBD", null, 2);//base de datos para el contador de horas
        service = new Service();

    } // End onCreateView


    //-----------------onCreateView----------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Vista del fragment
        View view = inflater.inflate(R.layout.fragment_data, container, false);
        //objetos
        cvRegistro = (CardView) view.findViewById(R.id.cvRegistro);
        cvActualizar = (CardView) view.findViewById(R.id.cvActualizar);
        cvEnviarCorreo = (CardView) view.findViewById(R.id.cvEnviarCorreo);
        tvCorreo_user = (TextView) view.findViewById(R.id.tvCorreo_user);
        tvBackup_name = (TextView) view.findViewById(R.id.tvBackup_name);
        etCorreo = (EditText) view.findViewById(R.id.etCorreo);
        etBackup = (EditText) view.findViewById(R.id.etBackup);
        etCorreo_send = (EditText) view.findViewById(R.id.etCorreo_send);
        btnEnviar = (Button) view.findViewById(R.id.btnEnviar);
        btnActualizar = (Button) view.findViewById(R.id.btnActualizar);
        btnEnviar_correo = (Button) view.findViewById(R.id.btnEnviar_correo);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

       // condicion para mostrar - ocultar los CARDVIEW
        String correo_user = "";
        String backup_name = "";
        correo_user = service.getLocalData(getContext(), "correo_user");
        backup_name = service.getLocalData(getContext(), "backup_name");
        if (correo_user == null){

            cvActualizar.setVisibility(View.GONE);
            cvEnviarCorreo.setVisibility(View.GONE);
            cvRegistro.setVisibility(View.VISIBLE);

        }else {
            tvCorreo_user.setText(correo_user);
            tvBackup_name.setText(backup_name);
            cvActualizar.setVisibility(View.VISIBLE);
            cvEnviarCorreo.setVisibility(View.VISIBLE);
            cvRegistro.setVisibility(View.GONE);

            etCorreo_send.setText(correo_user);
        }


        // ------------------------BANNER-----------------------------------------
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        // MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

        // SOLO PRUEBAS BANNER
        // codigo para visiualizar Banner en Un equipo: Solo Test
  /*    List<String> testDevices = new ArrayList<>();
        testDevices.add(AdRequest.DEVICE_ID_EMULATOR);
        // asigna el Dispositivo de Prueba
        RequestConfiguration requestConfiguration= new RequestConfiguration.Builder().setTestDeviceIds(testDevices).build();
        MobileAds.setRequestConfiguration(requestConfiguration);
*/
        //codigo del BANNER
        mAdView = view.findViewById(R.id.AdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
       // ------------------------END BANNER-----------------------------------------



        ///////////////////ACCIONES DE LOS BOTONES Y TV///////////////////////
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (etCorreo.getText().equals("") && etBackup.getText().equals("")) {
                    Toast.makeText(getContext(),"Llene todos los campos", Toast.LENGTH_LONG).show();
                    return;
                }

                if(!isValidEmailId(etCorreo.getText().toString().trim())){
                    Toast.makeText(getContext(), "Ingrese un Correo Valido", Toast.LENGTH_LONG).show();
                    return;
                }
                // si to_do OK
                post_put_BackUp(true); // llama el metodo para crear el BackUp

            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // si to_do OK
                post_put_BackUp(false); // llama el metodo para Actualizar el BackUp

            }
        });

        btnEnviar_correo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(etCorreo_send.getText().equals("") || !isValidEmailId(etCorreo_send.getText().toString().trim())){
                    Toast.makeText(getContext(), "Ingrese un Correo Valido", Toast.LENGTH_LONG).show();
                    return;
                }
                // si to_do OK
                enviarBackUpByEmail(); // envia el backup por Email

            }
        });


        /////////////////////////////////////////


        return view;
    } // End onCreateView




    // --------------------------------------Metodos---------------------------------------------------------


    //-----------------------------
    // metodo para crear el Backup en Hosting
    private void post_put_BackUp(Boolean flag){

        ArrayList<RegistroModel> registersArrayList = new ArrayList<>();
        RegistroModel registroModel;

        //Codigo para efectuar la consulta a la base de datos
        Cursor cursor = admincount.getReadableDatabase().rawQuery("SELECT id, idvuelo, siio, total_tripulacion_hor, total_tripulacion_min, piloto, copiloto, day, week, month, year FROM " + Constantes.TABLA_CONTADOR, null);

        String data_siio = "N/A";

        // moving our cursor to first position.
        if (cursor.moveToFirst()) {
            do {

                registroModel = new RegistroModel();

                registroModel.setId(cursor.getString(0));
                registroModel.setNum_vuelo(cursor.getString(1));
                if (cursor.getString(2) != null) // condicion para guardar cambiar dato null
                    data_siio = cursor.getString(2);
                registroModel.setNum_siio(data_siio);
                registroModel.setTotal_trip_hor(String.valueOf(cursor.getInt(3)));
                registroModel.setTotal_trip_min(String.valueOf(cursor.getInt(4)));
                registroModel.setPiloto_name(cursor.getString(5));
                registroModel.setCopiloto_name(cursor.getString(6));
                registroModel.setDay(String.valueOf(cursor.getInt(7)));
                registroModel.setWeek(cursor.getString(8));
                registroModel.setMonth(cursor.getString(9));
                registroModel.setYear(cursor.getString(10));
                registroModel.setBackup_name(String.valueOf(etBackup.getText()));
                registroModel.setCorreo_user(String.valueOf(etCorreo.getText()));

                registersArrayList.add(registroModel);


            } while (cursor.moveToNext());
            // moving our cursor to next.
        }

        admincount.cerrarBD(); // cierra BD Sqlite

        if (registersArrayList.size() == 0)
        {
            Toast.makeText(getContext(),"No existen registros", Toast.LENGTH_LONG).show();
            return;
        }


        for(int i = 0; i < registersArrayList.size(); i++)
        {


            JSONObject objectJson = new JSONObject(); // crea el Objeto JSON con los datos
            try {

                //input your API parameters
                objectJson.put("num_id", registersArrayList.get(i).getId());
                objectJson.put("num_vuelo", registersArrayList.get(i).getNum_vuelo());
                objectJson.put("num_siio", registersArrayList.get(i).getNum_siio());
                objectJson.put("total_trip_hor", registersArrayList.get(i).getTotal_trip_hor());
                objectJson.put("total_trip_min", registersArrayList.get(i).getTotal_trip_min());
                objectJson.put("piloto_name", registersArrayList.get(i).getPiloto_name());
                objectJson.put("copiloto_name", registersArrayList.get(i).getCopiloto_name());
                objectJson.put("correo_user", registersArrayList.get(i).getCorreo_user());
                objectJson.put("backup_name", registersArrayList.get(i).getBackup_name());
                objectJson.put("year", registersArrayList.get(i).getYear());
                objectJson.put("month", registersArrayList.get(i).getMonth());
                objectJson.put("week", registersArrayList.get(i).getWeek()); // no hay data de week
                objectJson.put("day", registersArrayList.get(i).getDay());
                objectJson.put("source", "ANDROID");
                objectJson.put("user_mail", registersArrayList.get(i).getCorreo_user());

            } catch (JSONException e) {
                Log.i("MyTag JSONException", String.valueOf(e.getMessage()));
            }

            // Log.d(" TAG-AQUI JSONObject", String.valueOf(objectJson));

            if(flag) {
                service.saveLocalData(getContext(), "correo_user", String.valueOf(etCorreo.getText()));
                service.saveLocalData(getContext(), "backup_name", String.valueOf(etBackup.getText()));
                tvCorreo_user.setText(String.valueOf(etCorreo.getText()));
                tvBackup_name.setText(String.valueOf(etBackup.getText()));
                cvRegistro.setVisibility(View.GONE);
                cvActualizar.setVisibility(View.VISIBLE);
                cvEnviarCorreo.setVisibility(View.VISIBLE);

                etCorreo_send.setText(String.valueOf(etCorreo.getText()));
            }

            String url = getResources().getString(R.string.url) + "register";
            postDataBackUp(url, objectJson); // llama el metodo para almecenar el Backup



        }




    }


    // ---------------------------------------------
    private void enviarBackUpByEmail() { // envia el backup por correo

        progressBar.setVisibility(View.VISIBLE);


        JSONObject objectJson = new JSONObject(); // crea el Objeto JSON con los datos
        try {

            //input your API parameters
            objectJson.put("correo_user", service.getLocalData(getContext(), "correo_user"));
            objectJson.put("backup_name", service.getLocalData(getContext(), "backup_name"));


        } catch (JSONException e) {
            Log.i("MyTag JSONException", String.valueOf(e.getMessage()));
        }

        String url = getResources().getString(R.string.url) + "bymail";
        postDataBackUp(url, objectJson); // llama el metodo para almecenar el Backup

    }



    // ---------------------------------------------
    // envia datos al Hosting
    private void postDataBackUp(String url, JSONObject objectJson) {

        // Log.d("CURSOR AQUI", String.valueOf(registersArrayList));



        service.postData(getContext(), url, objectJson, new VolleyCallback() {
            @Override
            public void onSuccessResponse(Object result) {

                // Log.d("TAG - Response", String.valueOf(result));
                progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onErrorResponse(String error) {

                // Log.d("TAG - Error", error);

            }
        });


    }


    //--------------------------
    // metodo para validar el Correo
    private boolean isValidEmailId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }





} // END class

