package com.example.jcaal.sharingjob_v01.logica;

/**
 * Created by Jesus on 01/04/2015.
 */
public class elementoPila {
    private TipoFragmento tipoFragmento;
    private String[] parametros;

    public elementoPila(TipoFragmento tf, String[] parms){
        this.tipoFragmento = tf;
        this.parametros = parms;
    }

    public TipoFragmento getTipoFragmento() {
        return tipoFragmento;
    }

    public void setTipoFragmento(TipoFragmento tipoFragmento) {
        this.tipoFragmento = tipoFragmento;
    }

    public String[] getParametros() {
        return parametros;
    }

    public void setParametros(String[] parametros) {
        this.parametros = parametros;
    }


}
