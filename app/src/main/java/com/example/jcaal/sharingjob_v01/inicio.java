package com.example.jcaal.sharingjob_v01;

import android.os.Bundle;

public class inicio extends generico_fragment{


    @Override
    public void otrosParametros(Bundle args, String lista) {
    }

    @Override
    public void hacerOnCreate() {
        mCallback.seleccion(0);
    }
}