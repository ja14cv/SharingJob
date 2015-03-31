package com.example.jcaal.sharingjob_v01.gui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.jcaal.sharingjob_v01.R;
import com.example.jcaal.sharingjob_v01.ws.ws_sharingJob;

public class inicio extends FragmentGenerico {

    @Override
    public void otrosParametros(Bundle args, String[] parms) {

    }

    @Override
    public void hacerOnCreate() {
        Button btURL = (Button) getView().findViewById(R.id.nuevo);

        btURL.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                //Consumir el servicio
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ws_sharingJob ws = new ws_sharingJob();
                        String m = ws.saludo("jesus");
                        Log.i("ws", "Mensaje: " + m);
                        Toast.makeText(getActivity(), "ws " + m, Toast.LENGTH_SHORT).show();
                    }
                }).start();


            }
        });

        mCallback.seleccion(0);
    }

    /*public void hacer(View v){
        Log.i("boton", "me presionaste XD");
    }*/
}