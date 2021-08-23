package app.andres.contadortresk.sqlite;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import app.andres.contadortresk.R;


///CODIGO PARA MOSTRAR L TABLA EN EL TABLELAYOUD

public class DatosTabla {
    private TableLayout tableLayout;
    private Context context;
    private String[] header;
    private ArrayList<String[]> datos;
    private TableRow tableRow;
    private TextView txCeldas;
    private int indexC;
    private int indexR;
    private boolean multiColor = false;
    int firstColor;
    int secondColor;

    public DatosTabla(TableLayout tableLayout, Context context) {
        this.tableLayout = tableLayout;
        this.context = context;
    }

    //codigo para crear el encabezado de la tabla datos
    public void addHeader(String [] header){
        this.header = header;
        createrHeader();
    }
    //codigo para crear las filas con los datos
    public void addDatos(ArrayList<String[]> datos){
        this.datos = datos;
        createTablaDatos();
    }

    private void newRow(){
        tableRow = new TableRow(context);
    }

    private void newCell(){
        txCeldas = new TextView(context);
        txCeldas.setGravity(Gravity.CENTER);
        txCeldas.setTextSize(14);
    }

    private void createrHeader(){
        indexC=0;
        newRow();
        while (indexC < header.length){
            newCell();
            txCeldas.setText(header[indexC++]);
            tableRow.addView(txCeldas,newTableRowParams());
        }

        tableLayout.addView(tableRow);

    }

    private void createTablaDatos (){
        String info;
        for (indexR = 1; indexR <= header.length; indexR++){
            newRow();
            for (indexC = 0; indexC < header.length; indexC++){
                newCell();
                String[] fila = datos.get(indexR - 1);
                info = (indexC < fila.length)?fila[indexC]:"";
                txCeldas.setText(info);
                tableRow.addView(txCeldas,newTableRowParams());
            }
            tableLayout.addView(tableRow);
        }
    }

//////////////////////////////////////////////////*******
//crear tabla
public void createTable(String[]header, ArrayList<String[]>clients){


    try{
        indexC=0;
        newRow();
        //crea el encabezado
        while (indexC < header.length){
            newCell();
            txCeldas.setText(header[indexC++]);
            tableRow.addView(txCeldas,newTableRowParams());
            txCeldas.setPadding(10,10,10,10);
            txCeldas.setBackgroundResource(R.color.colorPrimaryDark);
            txCeldas.setTextColor(Color.WHITE);
        }
        tableLayout.addView(tableRow);

        String info;
        for (indexR = 0; indexR < clients.size(); indexR++){
            newRow();
           // String[] row = clients.get(indexR);
            for (indexC = 0; indexC < header.length; indexC++){
                newCell();
                String[] fila = clients.get(indexR);
                info = (indexC < fila.length)?fila[indexC]:"";
                txCeldas.setText(info);
                tableRow.addView(txCeldas,newTableRowParams());
            }
            tableLayout.addView(tableRow);
        }



    }catch (Exception e){
        Log.e("create Tabla ",e.toString());
    }

}


    ////////////////////////////////////////////////***


    //codigo para cambiar el color del encabezado "header"
    public void backgroundHeader (int color){
        indexC = 0;
        while (indexC < header.length){
            txCeldas = getCell(0, indexC++);
            txCeldas.setPadding(10,10,10,10);
            txCeldas.setTextColor(Color.WHITE);
            txCeldas.setBackgroundResource(color);

        }

    }
    //codigo para cambiar el color de los datos de la tabla
    public void backgroundDatos (int firstColor, int secondColor, ArrayList<String[]>clients, int dataSize){

        for (indexR = 1; indexR < clients.size() + 1; indexR++){
            multiColor = !multiColor;
            for (indexC = 0; indexC < dataSize; indexC++){//numero 6 por los datos del header

                txCeldas = getCell(indexR, indexC);
                txCeldas.setPadding(10,10,10,10);
                txCeldas.setTextColor(Color.BLACK);
                txCeldas.setBackgroundColor((multiColor)?firstColor:secondColor);
            }

        }
        this.firstColor = firstColor;
        this.secondColor = secondColor;
    }



    //codigo para encontrar las celda
    private TableRow getRow(int index){
        return (TableRow)tableLayout.getChildAt(index);
    }
    private TextView getCell(int rowIndex, int columIndex){
        tableRow = getRow(rowIndex);
        return (TextView) tableRow.getChildAt(columIndex);
    }

    private TableRow.LayoutParams newTableRowParams(){
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.setMargins(1,1,1,1);
        params.weight = 1;
        return params;
    }

}
