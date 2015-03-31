package com.example.jcaal.sharingjob_v01.gui;

import android.os.Bundle;

public class configuracion extends FragmentGenerico {

    @Override
    public void otrosParametros(Bundle args, String[] parms) {

    }

    @Override
    public void hacerOnCreate(){
        mCallback.seleccion(tipoFragmento);
    }

}