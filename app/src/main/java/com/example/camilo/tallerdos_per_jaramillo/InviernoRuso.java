package com.example.camilo.tallerdos_per_jaramillo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.Observable;
import java.util.Observer;

import mensaje.Mensaje;

public class InviernoRuso extends AppCompatActivity implements Observer, View.OnClickListener {

    ImageButton arr;
    ImageButton aba;
    ImageButton izq;
    ImageButton der;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invierno_ruso);

                Comunicacion.getInstance().addObserver(this);

                arr= (ImageButton) findViewById(R.id.arr);
                aba= (ImageButton) findViewById(R.id.aba);
                izq= (ImageButton) findViewById(R.id.izq);
                der= (ImageButton) findViewById(R.id.der);

                arr.setOnClickListener(this);
                aba.setOnClickListener(this);
                izq.setOnClickListener(this);
                der.setOnClickListener(this);
            }


            @Override
            public void update(Observable observable, Object o) {

            }

            @Override
            public void onClick(View view) {

                Mensaje ms;

                switch (view.getId()){

                    case R.id.arr:
                        Comunicacion.getInstance().sendMessage(ms =new Mensaje("arr"), Comunicacion.MULTI_GROUP_ADDRESS, Comunicacion.DEFAULT_PORT );
                        break;
                    case R.id.aba:
                        Comunicacion.getInstance().sendMessage(ms =new Mensaje("aba"), Comunicacion.MULTI_GROUP_ADDRESS, Comunicacion.DEFAULT_PORT );
                        break;
                    case R.id.izq:
                        Comunicacion.getInstance().sendMessage(ms =new Mensaje("izq"), Comunicacion.MULTI_GROUP_ADDRESS, Comunicacion.DEFAULT_PORT );
                        break;
                    case R.id.der:
                        Comunicacion.getInstance().sendMessage(ms =new Mensaje("der"), Comunicacion.MULTI_GROUP_ADDRESS, Comunicacion.DEFAULT_PORT );
                        break;
                }
            }
}

