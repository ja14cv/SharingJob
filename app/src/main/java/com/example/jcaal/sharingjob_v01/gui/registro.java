package com.example.jcaal.sharingjob_v01.gui;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jcaal.sharingjob_v01.R;
import com.example.jcaal.sharingjob_v01.logica.TipoFragmento;
import com.example.jcaal.sharingjob_v01.logica.sesion;
import com.example.jcaal.sharingjob_v01.ws.IWsdl2CodeEvents;
import com.example.jcaal.sharingjob_v01.ws.ws_sharingJob;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Jesus on 31/03/2015.
 */
public class registro extends FragmentGenerico implements IWsdl2CodeEvents{

    private Button registrar, cancelar;

    @Override
    public void otrosParametros(String[] parms) {

    }

    @Override
    public void hacerOnCreate() {
        if(sesion.isLogin()){ //Ya se esta logueado
            this.onDestroy();
            mCallback.onNavigationDrawerItemSelected(TipoFragmento.CUENTA); //ir a cuenta
            return;
        }

        //Eventos de botones
        registrar = (Button) getView().findViewById(R.id.reg_bt_registrar);
        registrar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                onClick_registrar(v);
            }
        });


        cancelar = (Button) getView().findViewById(R.id.reg_bt_cancelar);
        cancelar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onClick_cancelar(v);
            }
        });

        mCallback.seleccion(tipoFragmento);
    }

    private void onClick_registrar(View v){
        String correo = ((EditText) getView().findViewById(R.id.reg_et_email)).getText().toString().trim();
        String pass = ((EditText) getView().findViewById(R.id.reg_et_pass)).getText().toString().trim();
        String nombres = ((EditText) getView().findViewById(R.id.reg_et_nombres)).getText().toString().trim();
        String apellidos = ((EditText) getView().findViewById(R.id.reg_et_apellidos)).getText().toString().trim();

        if(!correo.isEmpty() && !pass.isEmpty() && !nombres.isEmpty() && !apellidos.isEmpty()){
            ws_sharingJob ws = new ws_sharingJob(this);
            String json = "{\"nombres\":\"" + nombres + "\",\"apellidos\":\"" + apellidos + "\",\"correo\":\"" + correo + "\",\"password\":\"" + pass + "\"}";
            Log.i("registro", "json: " + json);
            try {
                ws.registro_enteAsync(json);
            } catch (Exception e) {
                Log.e("registro", e.getMessage());
                Toast.makeText(getActivity(), "No se pudo registrar", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }else{
            //Todos los campos son nesesarios
            Toast.makeText(getActivity(), "Todos los campos son necesarios", Toast.LENGTH_LONG).show();
        }
    }

    private void onClick_cancelar(View v){
        this.onDestroy();
        mCallback.onNavigationDrawerItemSelected(TipoFragmento.INICIO);
    }

    @Override
    public void Wsdl2CodeStartedRequest() {
        registrar.setClickable(false);
    }

    private void procesarRegistro(String data){
        try {
            JSONObject jso = new JSONObject(data);
            JSONObject t1 = jso.getJSONArray("datos").getJSONObject(0);

            String tipo = t1.getString("Tipo");
            String desc = t1.getString("Descripcion");

            if(tipo.equals("1")){
                try {
                    sesion.setSesion(desc);
                    this.onDestroy();
                    mCallback.onNavigationDrawerItemSelected(TipoFragmento.CUENTA); //ir a cuenta
                    Toast.makeText(getActivity(), "Sesion: " + sesion.id_sesion, Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Log.e("registro", e.getMessage());
                    Toast.makeText(getActivity(), "No se pudo registrar", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }else{
                //Error en registro
                Toast.makeText(getActivity(), desc, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            Log.e("registro", e.getMessage());
            Toast.makeText(getActivity(), "No se pudo registrar", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void Wsdl2CodeFinished(String methodName, Object Data) {
        //Codigo finalizado
        Log.i("registro", "R: " + Data);
        procesarRegistro((String) Data);
    }

    @Override
    public void Wsdl2CodeFinishedWithException(Exception ex) {
        //No se puede conectar
        Log.e("registro", ex.getMessage());
        Toast.makeText(getActivity(), "No se pudo registrar", Toast.LENGTH_LONG).show();
    }

    @Override
    public void Wsdl2CodeEndedRequest() {
        //Se obtuvo la respuesta
        registrar.setClickable(true);
    }
}
