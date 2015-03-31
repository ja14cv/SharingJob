package com.example.jcaal.sharingjob_v01.gui;

import android.os.Bundle;

/**
 * Created by Jesus on 31/03/2015.
 */
public class registro extends FragmentGenerico {

    @Override
    public void otrosParametros(Bundle args, String[] parms) {

    }

    @Override
    public void hacerOnCreate() {
        mCallback.seleccion(tipoFragmento);
    }
}
