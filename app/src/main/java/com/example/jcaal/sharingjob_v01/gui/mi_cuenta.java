package com.example.jcaal.sharingjob_v01.gui;

import android.os.Bundle;

public class mi_cuenta extends FragmentGenerico {

    @Override
    public void otrosParametros(Bundle args, String[] parms) {

    }

    @Override
    public void hacerOnCreate(){
        mCallback.seleccion(1);
    }
}