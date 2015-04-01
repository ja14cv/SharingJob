package com.example.jcaal.sharingjob_v01.gui;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.jcaal.sharingjob_v01.R;
import com.example.jcaal.sharingjob_v01.logica.TipoFragmento;
import com.example.jcaal.sharingjob_v01.logica.sesion;
import com.example.jcaal.sharingjob_v01.ws.IWsdl2CodeEvents;
import com.example.jcaal.sharingjob_v01.ws.ws_sharingJob;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class cuenta extends FragmentGenerico implements IWsdl2CodeEvents {

    Button logout;

    @Override
    public void otrosParametros(String[] parms) {
    }

    @Override
    public void hacerOnCreate(){
        if(!sesion.isLogin()){ //Si No esta logueado
            this.onDestroy();
            mCallback.onNavigationDrawerItemSelected(TipoFragmento.LOGIN); //ir a login
            return;
        }

        //Eventos de botones
        logout = (Button) getView().findViewById(R.id.cuenta_bt_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_logout(v);
            }
        });

        mCallback.seleccion(tipoFragmento);
    }

    private void onClick_logout(View v){
        ws_sharingJob ws = new ws_sharingJob(this);
        try {
            ws.cierre_sesionAsync(sesion.id_sesion);
        } catch (Exception e) {
            Log.e("cuenta", "Cerrar sesion: " + e.getMessage());
            Toast.makeText(getActivity(), "No se pudo cerrar sesion.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void procesarLogout(String data){
        try {
            JSONObject jso = new JSONObject(data);
            JSONObject t1 = jso.getJSONArray("array").getJSONObject(0);

            String tipo = t1.getString("Tipo");

            if(tipo.equals("1")){
                try {
                    sesion.logout();
                    this.onDestroy();
                    mCallback.onNavigationDrawerItemSelected(TipoFragmento.INICIO); //ir a cuenta
                } catch (IOException e) {
                    Log.e("cuenta", "Cerrar sesion: " + e.getMessage());
                    Toast.makeText(getActivity(), "No se pudo cerrar sesion", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }else{
                //Error cerrar sesion
                Toast.makeText(getActivity(), "No se pudo cerrar sesion", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            Log.e("cuenta", "Cerrar sesion: " + e.getMessage());
            Toast.makeText(getActivity(), "No se pudo cerrar sesion", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void Wsdl2CodeStartedRequest() {
        //Antes de enviar
        logout.setClickable(false);
    }

    @Override
    public void Wsdl2CodeFinished(String methodName, Object Data) {
        //Respuesta
        Log.i("cuenta", "R: " + Data);
        procesarLogout((String) Data);
    }

    @Override
    public void Wsdl2CodeFinishedWithException(Exception ex) {
        //No se puede conectar
        Log.e("cuenta", "Cerrar sesion: " + ex.getMessage());
        Toast.makeText(getActivity(), "No se pudo cerrar sesion", Toast.LENGTH_LONG).show();
    }

    @Override
    public void Wsdl2CodeEndedRequest() {
        //Se obtuvo respuesta
        logout.setClickable(true);
    }
}