package com.example.jcaal.sharingjob_v01.gui;

/**
 * Created by Jesus on 01/04/2015.
 */
public class empresa extends FragmentGenerico{
    @Override
    public void otrosParametros(String[] parms) {

    }

    @Override
    public void hacerOnCreate() {
        mCallback.seleccion(tipoFragmento);
    }
}
