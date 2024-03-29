package com.example.theheroproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



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

    /* Funcion que obtiene el valor del EditText con parte del nombre de un heroe
    y envia esto a la nueva vista que se va abrir con la lista de
    los superheroes que coincidan con la busqueda*/
    public void mostrarResultado(View view){
        String nombre_heroe = txtBusqueda.getText().toString();
        Intent intent = new Intent(this.getBaseContext(), Resultados.class);
        intent.putExtra("heroe",nombre_heroe);
        startActivity(intent);

    }
}
