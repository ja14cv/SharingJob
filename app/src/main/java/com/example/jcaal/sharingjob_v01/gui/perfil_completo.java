package com.example.jcaal.sharingjob_v01.gui;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.jcaal.sharingjob_v01.R;
import com.example.jcaal.sharingjob_v01.ws.IWsdl2CodeEvents;

/**
 * Created by jCaal on 01/04/2015.
 */
public class perfil_completo extends FragmentGenerico implements IWsdl2CodeEvents {
    @Override
    public void otrosParametros(String[] parms) {

    }

    private Button btn_editar, btn_cancelar;
    private EditText et_nombres, et_apellidos, et_telefono, et_claveActual, et_claveNueva;
    private CheckBox cb_trabajoActual;

    @Override
    public void hacerOnCreate() {
        btn_editar = (Button) getView().findViewById(R.id.perfilc_btn_editar);
        btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_editar(v);
            }
        });
        btn_cancelar = (Button) getView().findViewById(R.id.perfilc_btn_cancelar);
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_cancelar(v);
            }
        });

        et_nombres = (EditText) getView().findViewById(R.id.perfilc_et_nombres);
        et_apellidos = (EditText) getView().findViewById(R.id.perfilc_et_apellidos);
        et_telefono = (EditText) getView().findViewById(R.id.perfilc_et_telefono);
        et_claveActual = (EditText) getView().findViewById(R.id.perfilc_et_claveActual);
        et_claveNueva = (EditText) getView().findViewById(R.id.perfilc_et_claveNueva);
        cb_trabajoActual = (CheckBox) getView().findViewById(R.id.perfilc_cb_trabajoActual);

        estado_edicion(false);
    }

    private void estado_edicion(boolean _estado){
        et_nombres.setEnabled(_estado);
        et_apellidos.setEnabled(_estado);
        et_telefono.setEnabled(_estado);
        et_claveActual.setEnabled(_estado);
        et_claveNueva.setEnabled(_estado);
        cb_trabajoActual.setEnabled(_estado);
    }

    private void onClick_editar(View v){
        if(btn_editar.getText().equals("@string/editar")){
            estado_edicion(true);
            btn_editar.setText("@string/guardar");
            btn_cancelar.setVisibility(View.VISIBLE);
        }else{
            //guardar edicion
        }
    }

    private void onClick_cancelar(View v){

    }

    @Override
    public void Wsdl2CodeStartedRequest() {

    }

    @Override
    public void Wsdl2CodeFinished(String methodName, Object Data) {

    }

    @Override
    public void Wsdl2CodeFinishedWithException(Exception ex) {

    }

    @Override
    public void Wsdl2CodeEndedRequest() {

    }
}
