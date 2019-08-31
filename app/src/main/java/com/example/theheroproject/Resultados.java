package com.example.theheroproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

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

        Intent inicio = getIntent();
        this.heroe = (String)inicio.getExtras().get("heroe");

        buscarHeroe(heroe);




    }


    public void buscarHeroe(String busqueda){
        String url = "https://www.superheroapi.com/api.php/2167533850019629/search/"+busqueda;
        //final TextView nombre_hero;
        final LinearLayout nuevoRegistro = (LinearLayout) findViewById(R.id.listHero);
        JsonObjectRequest request = new JsonObjectRequest(

                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());



                        try {
                            JSONArray resultado = response.getJSONArray("results");

                            txtNumHeroes.setText("Resultado: "+resultado.length());

                            for (int i = 0; i < resultado.length(); i++) {
                                try {

                                    JSONObject jsonObject = resultado.getJSONObject(i);
                                    //System.out.println(jsonObject.toString());

                                    final String id = jsonObject.getString("id");
                                    String nombre_heroe = jsonObject.getString("name");



                                    TextView nombre_hero = new TextView(getBaseContext());

                                    nombre_hero.setId(Integer.parseInt(id));
                                    nombre_hero.setText(nombre_heroe);
                                    nombre_hero.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getBaseContext(), Grafico.class);
                                            intent.putExtra("id_hero",id);
                                            startActivity(intent);
                                        }
                                    });

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
        mQueue.add(request);



    }

}
