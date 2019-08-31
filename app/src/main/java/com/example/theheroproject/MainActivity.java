package com.example.theheroproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class MainActivity extends AppCompatActivity {
    Button btnMostrar;
    EditText txtBusqueda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnMostrar = (Button) findViewById(R.id.btnBuscar);
        txtBusqueda = (EditText) findViewById(R.id.txtBusqueda);

        btnMostrar.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              mostrarResultado(v);
                                          }
        }

        );

    }

    public void mostrarResultado(View view){
        String nombre_heroe = txtBusqueda.getText().toString();
        Intent intent = new Intent(this.getBaseContext(), Resultados.class);
        intent.putExtra("heroe",nombre_heroe);
        startActivity(intent);

    }


}
