package com.example.jcaal.sharingjob_v01.gui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jcaal.sharingjob_v01.R;
import com.example.jcaal.sharingjob_v01.logica.TipoFragmento;
import com.example.jcaal.sharingjob_v01.logica.sesion;
import com.example.jcaal.sharingjob_v01.ws.IWsdl2CodeEvents;
import com.example.jcaal.sharingjob_v01.ws.ws_sharingJob;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jCaal on 02/04/2015.
 */
public class empleo extends FragmentGenerico implements IWsdl2CodeEvents {

    private String id_empleo;
    private boolean parmsOK = false;
    private ProgressDialog dialog;
    private Button ingresar;

    @Override
    public void otrosParametros(String[] parms) {
        Bundle args = this.getArguments();
        if(args != null && parms != null){
            if(parms.length >= 1){
                args.putString("id_empleado", parms[0]);
                parmsOK = true;
            }else{
                parmsOK = false;
            }
        }else{
            parmsOK = false;
        }
    }

    @Override
    public void hacerOnCreate() {
        //Verificar que esta logueado
        if(!sesion.isLogin()){ //Si No esta logueado
            this.onDestroy();
            mCallback.onNavigationDrawerItemSelected(TipoFragmento.LOGIN); //ir a login
            return;
        }

        //Evento aplicar
        ingresar = (Button) getView().findViewById(R.id.em_bt_aplicar);
        ingresar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                onClick_validar(v);
            }
        });

        //Verificar parametros
        if(parmsOK){
            id_empleo = this.getArguments().getString("id_empleado");

            //Cargar resultado de busqueda
            ws_sharingJob ws = new ws_sharingJob(this);
            try {
                String json = "{\"id_empleo\":\"" + id_empleo +"\"}";
                Log.i("empleo", json);
                ws.get_empleo_especificoAsync(json);

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("empleo", e.getMessage());
                Toast.makeText(getActivity(), "No se pudo buscar el empleo", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getActivity(), "No se encuentra el empleo", Toast.LENGTH_LONG).show();
        }

        mCallback.seleccion(tipoFragmento);
    }

    @Override
    public void Wsdl2CodeStartedRequest() {
        dialog = ProgressDialog.show(getView().getContext(), "Cargando", "Por favor espere...", true);
    }

    @Override
    public void Wsdl2CodeFinished(String methodName, Object Data) {
        if(methodName.equals("get_empleo_especifico")){
            procesarEmpleo(Data.toString());
        }else{
            procesarAplicar(Data.toString());
        }
        dialog.dismiss();
    }

    @Override
    public void Wsdl2CodeFinishedWithException(Exception ex) {
        Log.e("empleo", ex.getMessage());
    }

    @Override
    public void Wsdl2CodeEndedRequest() {

    }

    private void onClick_validar(View v){
        //Cargar resultado de busqueda
        ws_sharingJob ws = new ws_sharingJob(this);
        try {
            String json = "{\"id_empleo\":\"" + id_empleo +"\",\"sesion\":\"" + sesion.id_sesion + "\"}";
            Log.i("empleo", json);
            ws.aplicar_empleoAsync(json);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("empleo", e.getMessage());
            Toast.makeText(getActivity(), "No se pudo buscar el empleo", Toast.LENGTH_LONG).show();
        }
    }

    private void procesarEmpleo(String data){
        try {
            JSONObject jso = new JSONObject(data);
            JSONObject t1 = jso.getJSONArray("datos").getJSONObject(0);

            String tipo = t1.getString("Tipo");
            String desc = t1.getString("Descripcion");

            if(tipo.equals("1")){
                JSONObject t2 = jso.getJSONArray("array").getJSONObject(0);
                String titulo = t2.getString("titulo");
                String desc2 = t2.getString("descripcion");
                String salario = t2.getString("propuesta_salarial");

                ((TextView)getView().findViewById(R.id.em_tv_titulo)).setText(titulo);
                ((TextView)getView().findViewById(R.id.em_tv_desc)).setText(desc2);
                ((TextView)getView().findViewById(R.id.em_tv_salario)).setText(salario);

                Toast.makeText(getActivity(), desc, Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getActivity(), desc, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            Log.e("empleo", e.getMessage());
            Toast.makeText(getActivity(), "No se pudo buscar el empleo", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void procesarAplicar(String data){
        try {
            JSONObject jso = new JSONObject(data);
            JSONObject t1 = jso.getJSONArray("datos").getJSONObject(0);

            String tipo = t1.getString("Tipo");
            String desc = t1.getString("Descripcion");

            if(tipo.equals("1")){
                ingresar.setText("Aplicado");
                ingresar.setEnabled(false);
                Toast.makeText(getActivity(), desc, Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getActivity(), desc, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            Log.e("empleo", e.getMessage());
            Toast.makeText(getActivity(), "No se pudo buscar el empleo", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
