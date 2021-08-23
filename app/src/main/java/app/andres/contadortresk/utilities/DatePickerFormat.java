package app.andres.contadortresk.utilities;


import android.app.DatePickerDialog;


import java.util.Calendar;

public class DatePickerFormat {

    //variables
    private DatePickerDialog datePickerDialog;
    String date;


    // -----------------------------------
    public String getTodayDate() {

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return makeDateString(day, month, year);

    }

// -----------------------------
    public String makeDateString(int dayOfMonth, int month, int year) {

        // return dayOfMonth + "/" + getFormat(month) + "/" + year; // retorna el formato de la fecha
        return dayOfMonth + "/" + month + "/" + year; // retorna el formato de la fecha
    }


// -----------------------------
    private String getFormat(int month) {

        if (month == 1)
            return "ENE";
        if (month == 2)
            return "FEB";
        if (month == 3)
            return "MAR";
        if (month == 4)
            return "ABR";
        if (month == 5)
            return "MAY";
        if (month == 6)
            return "JUN";
        if (month == 7)
            return "JUL";
        if (month == 8)
            return "AGO";
        if (month == 9)
            return "SEP";
        if (month == 10)
            return "OCT";
        if (month == 11)
            return "NOV";
        if (month == 12)
            return "DIC";

        // mes por defeto
        return "ENE";


    }


    /*
    EL SIGUIENTE CODIGO NO ES USADO EN ESTE APP
    sirve para gestionar el datepicker en un activity
     */
/*
    // -----------------------------
    public void openDatePicker(View view) {

        datePickerDialog.show();

    }

    // -----------------------------------

    private void initDatePicker(Context context) { // ***Ajustar el contexto

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth)
            {
                month = month + 1;
                date = makeDateString(dayOfMonth, month, year);
                // en esta linea se asigna la fecha como texto al Boton
                // datePickerBtn.setText(date);

            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(context, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());// limita fechas pasadas

    }

*/


} //END class
