package com.example.jcaal.sharingjob_v01.gui;

//import android.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.jcaal.sharingjob_v01.R;
import com.example.jcaal.sharingjob_v01.logica.TipoFragmento;

public class configuracion extends FragmentGenerico {

    private Button crear, ingresar, cuenta_nuevo, cuenta_editar, empresa_nuevo, empresa_editar;

    @Override
    public void otrosParametros(Bundle args, String[] parms) {

    }

    @Override
    public void hacerOnCreate(){
        //Eventos de botones
        crear = (Button) getView().findViewById(R.id.conf_bt_crear);
        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_crear(v);
            }
        });

        ingresar = (Button) getView().findViewById(R.id.conf_bt_ingresar);
        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_ingresar(v);
            }
        });

        cuenta_nuevo = (Button) getView().findViewById(R.id.conf_bt_cuenta_nuevo);
        cuenta_nuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_goDireccion(0, 0);
            }
        });

        cuenta_editar = (Button) getView().findViewById(R.id.conf_bt_cuenta_editar);
        cuenta_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_goDireccion(0, 1);
            }
        });

        empresa_nuevo = (Button) getView().findViewById(R.id.conf_bt_empresa_nuevo);
        empresa_nuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_goDireccion(1, 0);
            }
        });

        empresa_editar = (Button) getView().findViewById(R.id.conf_bt_empresa_editar);
        empresa_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_goDireccion(1, 1);
            }
        });

        mCallback.seleccion(tipoFragmento);
    }

    private void onClick_crear(View v){
        android.support.v7.app.ActionBar ab = ((principal) getActivity()).getSupportActionBar();
        if (ab.isShowing()){
            ab.hide();
        }else{
            ab.show();
        }
    }

    private void onClick_ingresar(View v){
        this.onDestroy();
        mCallback.onNavigationDrawerItemSelected(TipoFragmento.LOGIN);
    }

    private void onClick_goDireccion(int vista, int op){
        return;
    }

}