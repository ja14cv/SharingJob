package com.example.jcaal.sharingjob_v01.logica;

import android.content.Context;
import android.widget.LinearLayout;

/**
 * Created by Jesus on 02/04/2015.
 */
public class EmpleoItem extends LinearLayout {

    private String idEmpleo, titulo;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdEmpleo() {
        return idEmpleo;
    }

    public void setIdEmpleo(String idEmpleo) {
        this.idEmpleo = idEmpleo;
    }

    public EmpleoItem(Context context) {
        super(context);
    }
}
