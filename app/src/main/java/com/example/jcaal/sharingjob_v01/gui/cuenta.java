package com.example.jcaal.sharingjob_v01.gui;

import android.app.ProgressDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jcaal.sharingjob_v01.R;
import com.example.jcaal.sharingjob_v01.logica.TipoFragmento;
import com.example.jcaal.sharingjob_v01.logica.sesion;
import com.example.jcaal.sharingjob_v01.ws.IWsdl2CodeEvents;
import com.example.jcaal.sharingjob_v01.ws.ws_sharingJob;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class cuenta extends FragmentGenerico implements IWsdl2CodeEvents {

    private Button btn_salir;
    private ImageButton btn_miEmpresa, btn_perfilComp;
    private Button btn_estRealizados, btn_empTomados, btn_empPublicados, btn_regEmpresa;

    private ProgressDialog dialog;

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
        btn_salir = (Button) getView().findViewById(R.id.cuenta_btn_salir);
        btn_salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_salir(v);
            }
        });
        btn_miEmpresa = (ImageButton) getView().findViewById(R.id.cuenta_btn_miEmpresa);
        btn_miEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_btn_miEmpresa(v);
            }
        });
        btn_perfilComp = (ImageButton) getView().findViewById(R.id.cuenta_btn_perfilComp);
        btn_perfilComp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_perfilCompleto(v);
            }
        });
        btn_estRealizados = (Button) getView().findViewById(R.id.cuenta_btn_estRealizados);
        btn_estRealizados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_btn_estRealizados(v);
            }
        });
        btn_empTomados = (Button) getView().findViewById(R.id.cuenta_btn_empTomados);
        btn_empTomados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_empTomados(v);
            }
        });
        btn_empPublicados = (Button) getView().findViewById(R.id.cuenta_btn_empPublicados);
        btn_empPublicados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_btn_empPublicados(v);
            }
        });
        btn_regEmpresa = (Button) getView().findViewById(R.id.cuenta_btn_regEmpresa);

        btn_miEmpresa.setVisibility(View.INVISIBLE);
        //btn_regEmpresa.setVisibility(View.INVISIBLE);
        //((TextView) getView().findViewById(R.id.cuenta_tv_miEmpresa)).setVisibility(View.INVISIBLE);
        //btn_miEmpresa.setImageResource(R.mipmap.star_full);

        ws_sharingJob ws = new ws_sharingJob(this);
        try {
            ws.get_datos_ente_pAsync("{\"sesion\":\""+sesion.id_sesion+"\"}");
            Log.i("cuenta", "get_datos_ente_p: " + "{\"sesion\":\""+sesion.id_sesion+"\"}");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("cuenta", "get_datos_ente_p: " + e.getMessage());
            this.onDestroy();
            Toast.makeText(getActivity(), "No se pudieron recuperar los datos.", Toast.LENGTH_LONG).show();
            mCallback.onNavigationDrawerItemSelected(TipoFragmento.PROBLEMA);
        }
    }

    private void onClick_salir(View v){
        ws_sharingJob ws = new ws_sharingJob(this);
        try {
            ws.cierre_sesionAsync(sesion.id_sesion);
        } catch (Exception e) {
            Log.e("cuenta", "Cerrar sesion: " + e.getMessage());
            Toast.makeText(getActivity(), "No se pudo cerrar sesion.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void onClick_btn_miEmpresa(View v){
        this.onDestroy();
        mCallback.onNavigationDrawerItemSelected(TipoFragmento.TAB_EMPRESA);
    }

    private void onClick_perfilCompleto(View v){
        this.onDestroy();
        mCallback.onNavigationDrawerItemSelected(TipoFragmento.TAB_PERFIL);
    }
    private void onClick_empTomados(View v){
        this.onDestroy();
        mCallback.onNavigationDrawerItemSelected(TipoFragmento.EMPLEOS_TOMADOS);
    }
    private void onClick_btn_estRealizados(View v){
        this.onDestroy();
        mCallback.onNavigationDrawerItemSelected(TipoFragmento.ESTUDIOS_REALIZADOS);
    }

    private void onClick_btn_empPublicados(View v){
        this.onDestroy();
        mCallback.onNavigationDrawerItemSelected(TipoFragmento.EMPLEOS_PUBLICADOS);
    }

    private void procesarLogout(String data){
        try {
            JSONObject jso = new JSONObject(data);
            JSONObject t1 = jso.getJSONArray("datos").getJSONObject(0);

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
    private void procesarDatos(String data){
        String nombres;
        String apellidos;
        String correo;
        String id_empresa;

        try {
            JSONObject jso = new JSONObject(data);
            JSONObject t1 = jso.getJSONArray("datos").getJSONObject(0);

            String tipo = t1.getString("Tipo");
            String desc = t1.getString("Descripcion");

            if(tipo.equals("1")){

                JSONArray temp =  jso.getJSONArray("array");
                JSONObject temporal = temp.getJSONObject(0);
                nombres = temporal.getString("nombres");
                apellidos = temporal.getString("apellidos");
                correo = temporal.getString("correo");
                id_empresa = temporal.getString("id_empresa");

                btn_miEmpresa.setVisibility(View.VISIBLE);
                if (id_empresa.equals("null")){
                    btn_miEmpresa.setBackgroundResource(R.mipmap.star_add);
                }else{
                    btn_miEmpresa.setBackgroundResource(R.mipmap.star_full);
                }


                ((TextView) getView().findViewById(R.id.cuenta_tv_nombres)).setText(apellidos +", "+ nombres);
                ((TextView) getView().findViewById(R.id.cuenta_tv_correo)).setText(correo);

            }else{
                this.onDestroy();
                Toast.makeText(getActivity(), desc, Toast.LENGTH_LONG).show();
                mCallback.onNavigationDrawerItemSelected(TipoFragmento.PROBLEMA);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            this.onDestroy();
            Toast.makeText(getActivity(), "No se pudieron recuperar los datos.", Toast.LENGTH_LONG).show();
            mCallback.onNavigationDrawerItemSelected(TipoFragmento.PROBLEMA);
        }
    }

    @Override
    public void Wsdl2CodeStartedRequest() {
        dialog = ProgressDialog.show(getView().getContext(), "Cargando", "Por favor espere...", true);
    }

    @Override
    public void Wsdl2CodeFinished(String methodName, Object Data) {
        if (methodName.equals("cierre_sesion")) {
            Log.i("cuenta", "R: " + Data);
            procesarLogout((String) Data);
        }
        if (methodName.equals("get_datos_ente_p")){
            Log.i("cuenta", "R: " + Data);
            procesarDatos((String) Data);
        }
        dialog.dismiss();
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
        btn_salir.setClickable(true);
    }

}