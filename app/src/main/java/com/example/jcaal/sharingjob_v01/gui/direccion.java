package com.example.jcaal.sharingjob_v01.gui;

import android.os.Bundle;

/**
 * Created by Jesus on 01/04/2015.
 */
public class direccion extends FragmentGenerico {
    @Override
    public void otrosParametros(Bundle args, String[] parms) {

    }

    @Override
    public void hacerOnCreate() {
        mCallback.seleccion(tipoFragmento);
    }
}
