package com.example.jcaal.sharingjob_v01.gui;

import android.view.View;
import android.widget.ImageButton;

import com.example.jcaal.sharingjob_v01.R;

public class problema extends FragmentGenerico {

    private ImageButton recargar;

    @Override
    public void otrosParametros(String[] parms) {

    }

    @Override
    public void hacerOnCreate() {
        //Eventos de botones
        recargar = (ImageButton) getView().findViewById(R.id.pro_bt_recargar);
        recargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_recargar();
            }
        });

        mCallback.seleccion(tipoFragmento);
    }

    private void onClick_recargar(){
        //Recargar el ultimo frame
        this.onDestroy();
        ((principal)getActivity()).loadLastFragment();
    }
}
