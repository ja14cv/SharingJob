package com.example.jcaal.sharingjob_v01.gui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.jcaal.sharingjob_v01.R;

public class nuevo_empleo extends FragmentGenerico {

    @Override
    public void otrosParametros(Bundle args, String[] parms) {

    }

    @Override
    public void hacerOnCreate(){
        Button btURL = (Button) getView().findViewById(R.id.nem_bt);

        btURL.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                Log.i("nem", "prueba de boton");
                Toast.makeText(getActivity(), "Clic de prueba", Toast.LENGTH_LONG).show();
                //onClick_ingresar(v);
            }
        });

        mCallback.seleccion(tipoFragmento);
    }


}