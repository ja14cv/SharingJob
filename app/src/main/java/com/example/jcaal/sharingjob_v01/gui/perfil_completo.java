package com.example.jcaal.sharingjob_v01.gui;

import android.app.ProgressDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jcaal.sharingjob_v01.R;
import com.example.jcaal.sharingjob_v01.logica.sesion;
import com.example.jcaal.sharingjob_v01.ws.IWsdl2CodeEvents;
import com.example.jcaal.sharingjob_v01.ws.ws_sharingJob;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private TextView tv_correo;
    private ProgressDialog dialog;
    private String password;
    private boolean editar = false;

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
        tv_correo = (TextView) getView().findViewById(R.id.perfilc_tv_correo);

        estado_edicion(false);
        editar = false;

        //Filtro
        et_nombres.setFilters(FragmentGenerico.filtroQuote);
        et_apellidos.setFilters(FragmentGenerico.filtroQuote);
        et_telefono.setFilters(FragmentGenerico.filtroQuote);
        et_claveActual.setFilters(FragmentGenerico.filtroQuote);
        et_claveNueva.setFilters(FragmentGenerico.filtroQuote);

        //Get datos ente
        ws_sharingJob ws = new ws_sharingJob(this);
        try {
            ws.get_datos_ente_cAsync("{\"sesion\":\"" + sesion.id_sesion + "\"}");
            Log.i("perfil_completo", "get_datos_ente_c: " + "{\"sesion\":\"" + sesion.id_sesion + "\"}");
        } catch (Exception e) {
            Log.e("perfil_completo", "get_datos_ente_c: " + e.getMessage());
            Toast.makeText(getActivity(), "No se pudieron recuperar los datos.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        mCallback.seleccion(tipoFragmento);
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
        if(!editar){
            //Activar guardar
            editar = true;
            estado_edicion(true);
            btn_editar.setText(R.string.guardar);
            btn_cancelar.setVisibility(View.VISIBLE);
            btn_editar.setBackgroundResource(R.drawable.button_green);
        }else{
            String clave = password;
            if (!et_claveNueva.getText().toString().isEmpty()){   //cambio clave
                Log.i("perfil_completo", "nueva: " + et_claveNueva.getText().toString());
                if (et_claveActual.getText().toString().equals(password)){    //coiciden
                    clave = et_claveNueva.getText().toString();
                }else{
                    Toast.makeText(getView().getContext(), "Contraseña anterior no coincide", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            int trabajo_actual = 0;
            if(cb_trabajoActual.isChecked()){
                trabajo_actual = 1;
            }

            String nombres = et_nombres.getText().toString();
            String apellidos = et_apellidos.getText().toString();
            String telefono = et_telefono.getText().toString();
            if (nombres.isEmpty()){
                nombres = "NULL";
            }
            if (apellidos.isEmpty()){
                apellidos = "NULL";
            }
            if (telefono.isEmpty()){
                telefono = "NULL";
            }

            String json = "{\n" +
                    "\t\"nombres\":\"" + nombres + "\",\n" +
                    "\t\"apellidos\":\"" + apellidos + "\",\n" +
                    "\t\"correo\":\"" + tv_correo.getText().toString() + "\",\n" +
                    "\t\"password\":\"" + clave + "\",\n" +
                    "\t\"trabajo_actual\":\"" + trabajo_actual + "\",\n" +
                    "\t\"telefono\":\"" + telefono + "\",\n" +
                    "\t\"sesion\":\"" + sesion.id_sesion + "\"\n" +
                    "}";

            Log.i("perfil_completo", "json: " + json);

            ws_sharingJob ws = new ws_sharingJob(this);
            try {
                ws.registro_enteAsync(json);
                Log.i("registro_ente", "registro_ente: " + "{\"sesion\":\"" + sesion.id_sesion + "\"}");
            } catch (Exception e) {
                Log.e("registro_ente", "registro_ente: " + e.getMessage());
                Toast.makeText(getActivity(), "No se pudo actualizar los datos.", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    private void onClick_cancelar(View v){
        editar = false;
        estado_edicion(false);
        btn_editar.setText(R.string.editar);
        btn_cancelar.setVisibility(View.INVISIBLE);
        btn_editar.setBackgroundResource(R.drawable.button_blue);
    }

    @Override
    public void Wsdl2CodeStartedRequest() {
        dialog = ProgressDialog.show(getView().getContext(), "Cargando", "Por favor espere...", true);
    }

    @Override
    public void Wsdl2CodeFinished(String methodName, Object Data) {
        if (methodName.equals("get_datos_ente_c")){
            procesarDatos((String) Data);
        }
        if(methodName.equals("registro_ente")){
            procesarUpdate((String) Data);
        }
        dialog.dismiss();
    }

    @Override
    public void Wsdl2CodeFinishedWithException(Exception ex) {
        Log.e("registro_ente", ex.getMessage());
        Toast.makeText(getActivity(), "No se pudo contectar", Toast.LENGTH_LONG).show();
    }

    @Override
    public void Wsdl2CodeEndedRequest() {

    }

    private void procesarDatos(String data){
        try {
            Log.i("perfil_completo", "json entrada: " + data);

            JSONObject jso = new JSONObject(data);
            JSONObject t1 = jso.getJSONArray("datos").getJSONObject(0);

            String tipo = t1.getString("Tipo");
            String desc = t1.getString("Descripcion");

            if(tipo.equals("1")){
                JSONArray temp =  jso.getJSONArray("array");
                JSONObject temporal = temp.getJSONObject(0);
                String nombres = temporal.getString("nombres");
                String apellidos = temporal.getString("apellidos");
                String telefono = temporal.getString("telefono");
                String correo = temporal.getString("correo");
                String trabajo_actual = temporal.getString("trabajo_actual");
                password = temporal.getString("password");

                tv_correo.setText(correo);
                if (!nombres.equals("NULL")){
                    et_nombres.setText(nombres);
                }
                if (!apellidos.equals("NULL")){
                    et_apellidos.setText(apellidos);
                }
                if (!telefono.equals("NULL")){
                    et_telefono.setText(telefono);
                }
                if (!trabajo_actual.equals("NULL")){
                    if (trabajo_actual.equals("1")){
                        cb_trabajoActual.setChecked(true);
                    }
                }
            }else{
                Toast.makeText(getActivity(), desc, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            Log.e("perfil_completo", "get_datos_ente_c: " + e.getMessage());
            Toast.makeText(getActivity(), "No se pudieron recuperar los datos.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    private void procesarUpdate(String data){
        try {
            JSONObject jso = new JSONObject(data);
            JSONObject t1 = jso.getJSONArray("datos").getJSONObject(0);

            String tipo = t1.getString("Tipo");
            String desc = t1.getString("Descripcion");

            if(tipo.equals("1")){
                editar = false;
                estado_edicion(false);
                btn_editar.setText(R.string.editar);
                btn_cancelar.setVisibility(View.INVISIBLE);
                btn_editar.setBackgroundResource(R.drawable.button_blue);
                Toast.makeText(getActivity(), "Datos guardados", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getActivity(), desc, Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("registro_ente", "registro_ente: " + e.getMessage());
            Toast.makeText(getActivity(), "No se pudo actualizar los datos.", Toast.LENGTH_LONG).show();
        }
    }
}
