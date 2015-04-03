package com.example.jcaal.sharingjob_v01.gui;

import android.app.ProgressDialog;
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
 * Created by Jesus on 01/04/2015.
 */
public class empresa extends FragmentGenerico implements IWsdl2CodeEvents {
    @Override
    public void otrosParametros(String[] parms) {

    }

    private EditText et_nombre, et_web, et_correo, et_telefono, et_mision, et_vision;
    private Button btn_agregar, btn_cancelar;

    private ProgressDialog dialog;
    private boolean editar;

    @Override   //TERMINADO
    public void hacerOnCreate() {
        et_nombre = (EditText) getView().findViewById(R.id.addEmpresa_et_nombre);
        et_web = (EditText) getView().findViewById(R.id.addEmpresa_et_web);
        et_correo = (EditText) getView().findViewById(R.id.addEmpresa_et_correo);
        et_telefono = (EditText) getView().findViewById(R.id.addEmpresa_et_telefono);
        et_mision = (EditText) getView().findViewById(R.id.addEmpresa_et_mision);
        et_vision = (EditText) getView().findViewById(R.id.addEmpresa_et_vision);
        btn_agregar = (Button) getView().findViewById(R.id.addEmpresa_btn_agregar);
        btn_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_btn_agregar(v);
            }
        });
        btn_cancelar = (Button) getView().findViewById(R.id.addEmpresa_btn_cancelar);
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_btn_cancelar(v);
            }
        });

        try {
            new ws_sharingJob(this).get_empresaAsync("{\"sesion\":\""+ sesion.id_sesion+"\"}");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onClick_btn_agregar(View v){
        if (!editar){
            String nombre = et_nombre.getText().toString().trim();
            String web = et_web.getText().toString().trim();
            String correo = et_correo.getText().toString().trim();
            String telefono = et_telefono.getText().toString().trim();
            String mision = et_mision.getText().toString().trim();
            String vision = et_vision.getText().toString().trim();
            if (nombre.isEmpty()){
                nombre = "NULL";
            }
            if (web.isEmpty()){
                web = "NULL";
            }
            if (correo.isEmpty()){
                correo = "NULL";
            }
            if (telefono.isEmpty()){
                telefono = "NULL";
            }
            if (mision.isEmpty()){
                mision = "NULL";
            }
            if (vision.isEmpty()){
                vision = "NULL";
            }
            String json = "{\n" +
                    "\t\"nombre\":\""+nombre+"\",\n" +
                    "\t\"mision\":\""+mision+"\",\n" +
                    "\t\"vision\":\""+vision+"\",\n" +
                    "\t\"sitio_web\":\""+web+"\",\n" +
                    "\t\"correo_contacto\":\""+correo+"\",\n" +
                    "\t\"telefono_contacto\":\""+telefono+"\",\n" +
                    "\t\"imagen\":\"NULL\",\n" +
                    "\t\"sesion\":\""+sesion.id_sesion+"\"\n" +
                    "}";
            Log.i("empresa", "json_entrada: " + json);
            try {
                new ws_sharingJob(this).nueva_empresaAsync(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            btn_cancelar.setVisibility(View.VISIBLE);
            btn_agregar.setText(R.string.guardar);
            btn_agregar.setBackgroundResource(R.drawable.button_green);
            estado_edicion(true);
            editar = false;
        }
    }
    private void onClick_btn_cancelar(View v){
        if (!editar) {
            btn_agregar.setText(R.string.editar);
            btn_cancelar.setVisibility(View.INVISIBLE);
            btn_agregar.setBackgroundResource(R.drawable.button_blue);
            estado_edicion(false);
            editar = true;
        }else{
            this.onDestroy();
            mCallback.onNavigationDrawerItemSelected(TipoFragmento.CUENTA);
        }
    }

    private void estado_edicion(boolean _estado){
        et_nombre.setEnabled(_estado);
        et_web.setEnabled(_estado);
        et_correo.setEnabled(_estado);
        et_telefono.setEnabled(_estado);
        et_mision.setEnabled(_estado);
        et_vision.setEnabled(_estado);
    }

    @Override
    public void Wsdl2CodeStartedRequest() {
        dialog = ProgressDialog.show(getView().getContext(), "Cargando", "Por favor espere...", true);
    }

    @Override
    public void Wsdl2CodeFinished(String methodName, Object Data) {
        Log.i("empresa", "metodo: " + methodName + ", Data: " + (String) Data);
        if (methodName.equals("get_empresa")){
            switch(procesarData((String) Data)){
                case 0:     //no tiene empresa
                    editar = false;
                    btn_agregar.setVisibility(View.VISIBLE);
                    btn_cancelar.setVisibility(View.VISIBLE);
                    break;
                case 1:     //tiene empresa
                    editar = true;
                    btn_agregar.setText(R.string.editar);
                    btn_agregar.setVisibility(View.VISIBLE);
                    btn_agregar.setBackgroundResource(R.drawable.button_blue);
                    procesarEmpresa((String) Data);
                    estado_edicion(false);
                    break;
                default:    //error general
            }
        }
        if(methodName.equals("nueva_empresa")){
            if(procesarData((String) Data) == 1){
                btn_agregar.setText(R.string.editar);
                btn_cancelar.setVisibility(View.INVISIBLE);
                btn_agregar.setBackgroundResource(R.drawable.button_blue);
                estado_edicion(false);
                editar = true;
            }
        }
        dialog.dismiss();
    }

    @Override
    public void Wsdl2CodeFinishedWithException(Exception ex) {

    }
    @Override
    public void Wsdl2CodeEndedRequest() {

    }

    private void procesarEmpresa(String data){
        try {
            JSONObject t1 = new JSONObject(data).getJSONArray("array").getJSONObject(0);
            String nombre = t1.getString("nombre");
            String sitio_web = t1.getString("sitio_web");
            String correo_contacto = t1.getString("correo_contacto");
            String telefono_contacto = t1.getString("telefono_contacto");
            String mision = t1.getString("mision");
            String vision = t1.getString("vision");

            if(!nombre.equals("NULL")){
                et_nombre.setText(nombre);
            }
            if(!sitio_web.equals("NULL")){
                et_web.setText(sitio_web);
            }
            if(!correo_contacto.equals("NULL")){
                et_correo.setText(correo_contacto);
            }
            if(!telefono_contacto.equals("NULL")){
                et_telefono.setText(telefono_contacto);
            }
            if(!mision.equals("NULL")){
                et_mision.setText(mision);
            }
            if(!vision.equals("NULL")){
                et_vision.setText(vision);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private int procesarData(String data){
        try {
            JSONObject t1 = new JSONObject(data).getJSONArray("datos").getJSONObject(0);
            String tipo = t1.getString("Tipo");
            String descripcion = t1.getString("Descripcion");
            if(tipo.equals("1")){
                Toast.makeText(getActivity(), descripcion, Toast.LENGTH_LONG).show();
                return 1;
            }else{
                Toast.makeText(getActivity(), descripcion, Toast.LENGTH_LONG).show();
                return 0;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
