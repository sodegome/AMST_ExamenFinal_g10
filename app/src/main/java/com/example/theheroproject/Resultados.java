package com.example.theheroproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Resultados extends AppCompatActivity {
    String heroe;
    TextView txtNumHeroes;

    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_resultados);

        mQueue = Volley.newRequestQueue(this);

        txtNumHeroes = findViewById(R.id.txtNumHeroes);

        /*Se obtiene lo que se quiere buscar de la pantalla anterior */
        Intent inicio = getIntent();
        this.heroe = (String)inicio.getExtras().get("heroe");

        buscarHeroe(heroe);

    }

    /* Función que recibe la busqueda, para hacer un get request al backend donde
    se encuentra la información de los heroes y los devuelve en fomato JSON.
    Se crea un TextView para mostrar los nombres de los superheroes que
    nos devuelve.
     */
    public void buscarHeroe(String busqueda){
        String url = "https://www.superheroapi.com/api.php/2167533850019629/search/"+busqueda;

        //Referenciamos al LinearLayout donde se van a insertar los TextView
        final LinearLayout nuevoRegistro = (LinearLayout) findViewById(R.id.listHero);
        JsonObjectRequest request = new JsonObjectRequest(

                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());

                        try {
                            /* El resultado de la informacion que necesitamos se encuentra
                            en un JSONArray */
                            JSONArray resultado = response.getJSONArray("results");

                            txtNumHeroes.setText("Resultado: "+resultado.length());

                            /*
                            Recorremos el JSONArray para obtener solo la informacion de lo que
                            necesitamos, o sea el nombre y el id del heroe
                             */
                            for (int i = 0; i < resultado.length(); i++) {
                                try {

                                    /*
                                    Se obtiene el JSONObject que contiene la información
                                    de cada superheroe
                                     */
                                    JSONObject jsonObject = resultado.getJSONObject(i);

                                    final String id = jsonObject.getString("id");
                                    String nombre_heroe = jsonObject.getString("name");

                                    /*
                                    Creamos un nuevo TextView para mostrar el nombre del heroe
                                     */
                                    TextView nombre_hero = new TextView(getBaseContext());

                                    nombre_hero.setId(Integer.parseInt(id));
                                    nombre_hero.setText(nombre_heroe);

                                    /*
                                    La metodo setOnClickListener lo usamos para que al momento de dar
                                    click en alguno de los nombres de los heroes, se abra una nueva
                                    vista con la informacion de cada heroe.
                                    Se envia el id del heroe para obtener su informacion
                                     */
                                    nombre_hero.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getBaseContext(), Grafico.class);
                                            intent.putExtra("id_hero",id);
                                            startActivity(intent);
                                        }
                                    });


                                    //Se añade el TextView al LinearLayout
                                    nuevoRegistro.addView(nombre_hero);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
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
        //Se añade el request a la cola
        mQueue.add(request);
    }
}
