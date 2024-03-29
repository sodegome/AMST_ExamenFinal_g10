package com.example.theheroproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Grafico extends AppCompatActivity {

    public BarChart graficoBarras;
    private LinearLayout contenedorHeroes;
    private Map<String, TextView> heroes;
    TextView txtName, txtFName;

    String id_hero;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico);
        heroes = new HashMap<String, TextView>();

        mQueue = Volley.newRequestQueue(this);

        txtName = findViewById(R.id.txtName);
        txtFName = findViewById(R.id.txtFName);

        // Se obtiene el id del heroe de la pantalla anterior
        Intent registros = getIntent();
        this.id_hero = (String)registros.getExtras().get("id_hero");

        iniciarGrafico();
        obtenerDatos(id_hero);
    }


    /*
    Función donde se referencia al gráfico de baras y se le setean
    todas las propiedades que queramos
     */
    public void iniciarGrafico() {
        graficoBarras = findViewById(R.id.barChart);
        graficoBarras.getDescription().setEnabled(false);
        graficoBarras.setMaxVisibleValueCount(60);
        graficoBarras.setPinchZoom(false);
        graficoBarras.setDrawBarShadow(false);
        graficoBarras.setDrawGridBackground(false);
        XAxis xAxis = graficoBarras.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        graficoBarras.getAxisLeft().setDrawGridLines(false);
        graficoBarras.animateY(1500);
        graficoBarras.getLegend().setEnabled(false);
    }

    /* Función que recibe el id del heroe, para hacer un get request al backend donde
    se encuentra la información del heroe y  lo devuelve en fomato JSON.
     */
    public void obtenerDatos(String id){

        String url = "https://www.superheroapi.com/api.php/2167533850019629/"+id;
        JsonObjectRequest request = new JsonObjectRequest(

                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());

                        try {
                            String nombre =  response.getString("name");
                            txtName.setText(nombre);

                            JSONObject fResultado = response.getJSONObject("biography");

                            String nombreCompleto = fResultado.getString("full-name");
                            txtFName.setText(nombreCompleto);

                            JSONObject resultado = response.getJSONObject("powerstats");

                            /*
                            Se llama a la función que actualiza el gráfico y se le envia
                            el JSONObject con los powerstats del heroe
                             */
                            actualizarGrafico(resultado);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error: "+error);
            }
        }){

        };
        mQueue.add(request);

    }

    /*
    Función que obtiene los valores en X y Y para llenar el grafico
     */
    private void actualizarGrafico(JSONObject heroes){
        String inteligencia;
        String strength;
        String velocidad;
        String durabilidad;
        String poder;
        String combate;

        ArrayList<BarEntry> dato_heroe = new ArrayList<>();

        try{

            /*
            Del JSONObject obtenemos las variables que queremos mostrar y añadimos
            al ArrayList de BarEntrys. El BarEntry tiene los valores de X y valores en
            Y que es el valor obtenido de las distintas caracteristicas del heroe
             */
            inteligencia = heroes.getString("intelligence");
            strength = heroes.getString("strength");
            velocidad = heroes.getString("speed");
            durabilidad = heroes.getString("durability");
            poder = heroes.getString("power");
            combate = heroes.getString("combat");
            dato_heroe.add(new BarEntry(1,Float.parseFloat(inteligencia)));
            dato_heroe.add(new BarEntry(2,Float.parseFloat(strength)));
            dato_heroe.add(new BarEntry(3,Float.parseFloat(velocidad)));
            dato_heroe.add(new BarEntry(4,Float.parseFloat(durabilidad)));
            dato_heroe.add(new BarEntry(5,Float.parseFloat(poder)));
            dato_heroe.add(new BarEntry(6,Float.parseFloat(combate)));
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        System.out.println(dato_heroe);

        //Se llama a la funcion que llena el grafico con el ArrayList lleno
        llenarGrafico(dato_heroe);
    }

    /*
    Función que recibe el ArrayList con los valores de X y Y para llenar el
    gráfico de barras
     */
    private void llenarGrafico(ArrayList<BarEntry> dato_heroe){
        BarDataSet heroesDataSet;

        if ( graficoBarras.getData() != null &&
                graficoBarras.getData().getDataSetCount() > 0) {
            heroesDataSet = (BarDataSet) graficoBarras.getData().getDataSetByIndex(0);
            heroesDataSet.setValues(dato_heroe);
            graficoBarras.getData().notifyDataChanged();
            graficoBarras.notifyDataSetChanged();
        } else {
            heroesDataSet = new BarDataSet(dato_heroe, "Data Set");
            heroesDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
            heroesDataSet.setDrawValues(true);
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(heroesDataSet);
            BarData data = new BarData(dataSets);
            graficoBarras.setData(data);
            graficoBarras.setFitBars(true);
        }
        graficoBarras.invalidate();
    }
}
