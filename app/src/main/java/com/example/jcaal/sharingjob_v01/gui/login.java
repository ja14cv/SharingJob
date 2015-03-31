package com.example.jcaal.sharingjob_v01.gui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.jcaal.sharingjob_v01.R;
import com.example.jcaal.sharingjob_v01.logica.TipoFragmento;
import com.example.jcaal.sharingjob_v01.logica.sesion;
import com.example.jcaal.sharingjob_v01.ws.IWsdl2CodeEvents;
import com.example.jcaal.sharingjob_v01.ws.ws_sharingJob;

import java.io.IOException;

/***
 * Seleccion 4
 */
public class login extends FragmentGenerico implements IWsdl2CodeEvents{

    private Button ingresar, cancelar, crear;

    @Override
    public void otrosParametros(Bundle args, String[] parms) {

    }

    @Override
    public void hacerOnCreate(){
        if(sesion.isLogin()){ //Ya se esta logueado
            this.onDestroy();
            mCallback.onNavigationDrawerItemSelected(TipoFragmento.CUENTA); //ir a cuenta
        }

        //Eventos de botones
        ingresar = (Button) getView().findViewById(R.id.lo_bt_ingreso);
        ingresar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                onClick_ingresar(v);

            }
        });

        cancelar = (Button) getView().findViewById(R.id.lo_bt_cancelar);
        cancelar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                onClick_cancelar(v);

            }
        });

        /*crear = (Button) getView().findViewById(R.id.lo_bt_ingreso);
        crear.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                onClick_crear(v);
            }
        });*/

        mCallback.seleccion(tipoFragmento);
    }

    private void onClick_ingresar(View v){
        ws_sharingJob ws = new ws_sharingJob(this);
        String json = "{\"correo\":\"ja14cv@gmail.com\",\"password\":\"123\"}";
        try {
            ws.inicio_sesionAsync(json);
        } catch (Exception e) {
            Log.e("login", e.getMessage());
            e.printStackTrace();
        }
    }

    private void onClick_cancelar(View v){
        this.onDestroy();
        mCallback.onNavigationDrawerItemSelected(TipoFragmento.INICIO);
    }

    private void onClick_crear(View v){

    }

    private void procesarLogin(String data){
        if(!data.equals("-1") && !data.equals("0")){
            //Sesion correcta
            sesion s = new sesion();
            try {
                s.setSesion(data);
                this.onDestroy();
                mCallback.onNavigationDrawerItemSelected(TipoFragmento.CUENTA); //ir a cuenta
                Toast.makeText(getActivity(), "Sesion: " + sesion.id_sesion, Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Log.e("login", "Sesion: " + e.getMessage());
                Toast.makeText(getActivity(), "No se pudo iniciar sesion.", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }else{
            Toast.makeText(getActivity(), "No se pudo iniciar sesion.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void Wsdl2CodeStartedRequest() {
        ingresar.setClickable(false);
    }

    @Override
    public void Wsdl2CodeFinished(String methodName, Object Data) {
        //Codigo finalizado
        Log.i("login", "R: " + Data);
        procesarLogin((String) Data);
    }

    @Override
    public void Wsdl2CodeFinishedWithException(Exception ex) {
        //No se puede conectar
        Log.e("login", "Inicio Sesion Fallido: " + ex.getMessage());
        Toast.makeText(getActivity(), "No se pudo iniciar sesion.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void Wsdl2CodeEndedRequest() {
        //Se obtuvo la respuesta
        ingresar.setClickable(true);
    }
}
