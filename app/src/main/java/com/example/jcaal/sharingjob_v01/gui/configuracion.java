package com.example.jcaal.sharingjob_v01.gui;

//import android.app.ActionBar;
import android.view.View;
import android.widget.Button;

import com.example.jcaal.sharingjob_v01.R;
import com.example.jcaal.sharingjob_v01.logica.TipoFragmento;

public class configuracion extends FragmentGenerico {

    private Button crear, ingresar, busqueda;

    @Override
    public void otrosParametros(String[] parms) {

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

        busqueda = (Button) getView().findViewById(R.id.conf_bt_busqueda);
        busqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_busqueda(v);
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

    private void onClick_busqueda(View v){
        this.onDestroy();
        mCallback.onNavigationDrawerItemSelected(TipoFragmento.RESULTADO_BUSQUEDA, new String[]{"categoria", "15", ""});
    }

}