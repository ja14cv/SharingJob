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

/***
 * Seleccion 4
 */
public class login extends FragmentGenerico implements IWsdl2CodeEvents{

    private Button ingresar, cancelar, crear;

    @Override
    public void otrosParametros(String[] parms) {

    }

    @Override
    public void hacerOnCreate(){
        if(sesion.isLogin()){ //Ya se esta logueado
            this.onDestroy();
            mCallback.onNavigationDrawerItemSelected(TipoFragmento.CUENTA); //ir a cuenta
            return;
        }

        //Filtros
        ((EditText)getView().findViewById(R.id.lo_et_email)).setFilters(FragmentGenerico.filtroQuote);
        ((EditText)getView().findViewById(R.id.lo_et_email)).setFilters(FragmentGenerico.filtroQuote);

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

        crear = (Button) getView().findViewById(R.id.lo_bt_crear);
        crear.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                onClick_crear(v);
            }
        });

        mCallback.seleccion(tipoFragmento);
    }

    private void onClick_ingresar(View v){
        String correo = ((EditText) getView().findViewById(R.id.lo_et_email)).getText().toString().trim();
        String pass = ((EditText) getView().findViewById(R.id.lo_et_pass)).getText().toString().trim();

        /*
        String correo = "admin@admin.com";
        String pass = "123";*/

        //if(!correo.isEmpty() && !pass.isEmpty()){
            ws_sharingJob ws = new ws_sharingJob(this);
            String json = "{\"correo\":\"" + correo + "\",\"password\":\"" + pass + "\"}";
            Log.i("login", "json: " + json);
            try {
                ws.inicio_sesionAsync(json);
            } catch (Exception e) {
                Log.e("login", e.getMessage());
                Toast.makeText(getActivity(), "No se pudo iniciar sesion", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        /*}else{
            Toast.makeText(getActivity(), "Ingrese correo y password", Toast.LENGTH_LONG).show();
        }*/
    }

    private void onClick_cancelar(View v){
        this.onDestroy();
        mCallback.onNavigationDrawerItemSelected(TipoFragmento.INICIO);
    }

    private void onClick_crear(View v){
        this.onDestroy();
        mCallback.onNavigationDrawerItemSelected(TipoFragmento.REGISTRO);
    }

    private void procesarLogin(String data){
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
                    Log.e("login", e.getMessage());
                    Toast.makeText(getActivity(), "No se pudo iniciar sesion", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }else{
                //Error en login
                Toast.makeText(getActivity(), "No se pudo iniciar sesion", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            Log.e("login", e.getMessage());
            Toast.makeText(getActivity(), "No se pudo iniciar sesion", Toast.LENGTH_LONG).show();
            e.printStackTrace();
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
        Log.e("login", ex.getMessage());
        Toast.makeText(getActivity(), "No se pudo iniciar sesion", Toast.LENGTH_LONG).show();
        ingresar.setClickable(true);
    }

    @Override
    public void Wsdl2CodeEndedRequest() {
        //Se obtuvo la respuesta
        ingresar.setClickable(true);
    }
}
