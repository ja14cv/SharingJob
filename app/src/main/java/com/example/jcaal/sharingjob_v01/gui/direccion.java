package com.example.jcaal.sharingjob_v01.gui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jcaal.sharingjob_v01.R;
import com.example.jcaal.sharingjob_v01.logica.TipoFragmento;
import com.example.jcaal.sharingjob_v01.logica.sesion;
import com.example.jcaal.sharingjob_v01.ws.IWsdl2CodeEvents;
import com.example.jcaal.sharingjob_v01.ws.ws_sharingJob;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Jesus on 01/04/2015.
 */
public class direccion extends FragmentGenerico implements IWsdl2CodeEvents{

    private boolean parmsOK = false;
    private String pertenece, operacion;
    private Button guardar, cancelar;
    private Spinner deptos;
    private static final String[] departamentos = new String[]{"Alta Verapaz", "Baja Verapaz", "Chimaltenango", "Chiquimula", "Petén", "El Progreso", "Quiché", "Escuintla",
            "Guatemala", "Huehuetenango", "Izabal", "Jalapa", "Jutiapa", "Quetzaltenango", "Retalhuleu", "Sacatepéquez", "San Marcos", "Santa Rosa", "Sololá", "Suchitepéquez", "Totonicapán", "Zacapa"};

    @Override
    public void otrosParametros(String[] parms) {
        Bundle args = this.getArguments();
        if(args != null && parms != null){
            if(parms.length >= 2){
                args.putString("pertenece", parms[0]);
                args.putString("operacion", parms[1]);
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
        if(!sesion.isLogin()){ //Si No esta logueado
            this.onDestroy();
            mCallback.onNavigationDrawerItemSelected(TipoFragmento.LOGIN); //ir a login
            return;
        }

        if(parmsOK){
            //Eventos de botones
            guardar = (Button) getView().findViewById(R.id.dir_bt_guardar);
            guardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClick_guardar(v);
                }
            });

            cancelar = (Button) getView().findViewById(R.id.dir_bt_cancelar);
            cancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClick_cancelar(v);
                }
            });

            //Crear spinenr
            deptos = (Spinner) getView().findViewById(R.id.dir_sp_deptos);
            ArrayAdapter<String> adapt = new ArrayAdapter<String>(getView().getContext(), android.R.layout.simple_list_item_1, departamentos);
            deptos.setAdapter(adapt);

            //Obtener argumentos
            Bundle args = this.getArguments();
            pertenece = args.getString("pertenece");
            operacion = args.getString("operacion");

            //Condicion para cargar datos
            if(operacion.equals("editar")){
                //cargar datos de empresa
                String json = "{\n" +
                        "\t\"sesion\":\"" + sesion.id_sesion + "\",\n" +
                        "\t\"proposito\":\"" + pertenece + "\"\n" +
                        "}";
                Log.i("direccion", "json: " + json);

                ws_sharingJob ws = new ws_sharingJob(this);
                try {
                    ws.get_direccionAsync(json);
                } catch (Exception e) {
                    Log.e("direccion", e.getMessage());
                    e.printStackTrace();
                    this.onDestroy();
                    mCallback.onNavigationDrawerItemSelected(TipoFragmento.INICIO);
                    Toast.makeText(getActivity(), "No se pudo obtener la direccion", Toast.LENGTH_LONG).show();
                }
            }
        }else{
            Toast.makeText(getActivity(), "Ocurrio un problema :(", Toast.LENGTH_LONG).show();
        }

        mCallback.seleccion(tipoFragmento);
    }

    private void onClick_guardar(View v){
        String no = ((EditText) getView().findViewById(R.id.dir_et_no)).getText().toString().trim();
        String calle = ((EditText) getView().findViewById(R.id.dir_et_calle)).getText().toString().trim();
        String avenida = ((EditText) getView().findViewById(R.id.dir_et_avenida)).getText().toString().trim();
        String calzada = ((EditText) getView().findViewById(R.id.dir_et_calzada)).getText().toString().trim();
        String zona = ((EditText) getView().findViewById(R.id.dir_et_zona)).getText().toString().trim();
        String municipio = ((EditText) getView().findViewById(R.id.dir_et_municipio)).getText().toString().trim();
        String departamento = deptos.getSelectedItem().toString();

        //Verificar campos
        if(!zona.isEmpty() && !municipio.isEmpty() && !departamento.isEmpty()){
            //Conversion de campos vacios a nulos
            if(no.isEmpty()){
                no = "NULL";
            }

            if(calle.isEmpty()){
                calle = "NULL";
            }

            if(avenida.isEmpty()){
                avenida = "NULL";
            }

            if(calzada.isEmpty()){
                calzada = "NULL";
            }

            String json = "{\n" +
                    "\t\"calle\":\"" + calle + "\",\n" +
                    "\t\"avenida\":\"" + avenida + "\",\n" +
                    "\t\"calzada\":\"" + calzada + "\",\n" +
                    "\t\"zona\":\"" + zona + "\",\n" +
                    "\t\"municipio\":\"" + municipio + "\",\n" +
                    "\t\"departamento\":\"" + departamento + "\",\n" +
                    "\t\"numero_residencia\":\"" + no + "\",\n" +
                    "\t\"sesion\":\"" + sesion.id_sesion + "\",\n" +
                    "\t\"proposito\":\"" + pertenece + "\"\n" +
                    "}";
            Log.i("direccion", "json: " + json);

            ws_sharingJob ws = new ws_sharingJob(this);
            try {
                ws.nueva_direccionAsync(json);
            } catch (Exception e) {
                Log.e("direccion", e.getMessage());
                Toast.makeText(getActivity(), "No se pudo guardar la direccion", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }else{
            //Faltan datos
            Toast.makeText(getActivity(), "Ingrese los datos requeridos", Toast.LENGTH_LONG).show();
        }
    }

    private void onClick_cancelar(View v){
        regresar();
    }

    private void regresar(){
        if(pertenece.equals("ente")){
            //direccion de ente
            this.onDestroy();
            mCallback.onNavigationDrawerItemSelected(TipoFragmento.CUENTA); //es para pasar al perfil completo
        }else {
            //direccion de empresa
            this.onDestroy();
            mCallback.onNavigationDrawerItemSelected(TipoFragmento.CUENTA);
        }
    }

    private void procesarDireccion(String data){
        try {
            JSONObject jso = new JSONObject(data);
            JSONObject t1 = jso.getJSONArray("datos").getJSONObject(0);

            String tipo = t1.getString("Tipo");

            if(tipo.equals("1")){
                JSONObject t2 = jso.getJSONArray("array").getJSONObject(0);

                //Datos de direccion
                String no = t2.getString("numero_residencia");
                String calle = t2.getString("calle");
                String avenida = t2.getString("avenida");
                String calzada = t2.getString("calzada");
                String zona = t2.getString("zona");
                String municipio = t2.getString("municipio");
                String departamento = t2.getString("departamento");

                if(!no.equals("NULL")){
                    ((EditText)getView().findViewById(R.id.dir_et_no)).setText(no);
                }

                if(!calle.equals("NULL")){
                    ((EditText)getView().findViewById(R.id.dir_et_calle)).setText(calle);
                }

                if(!avenida.equals("NULL")){
                    ((EditText)getView().findViewById(R.id.dir_et_avenida)).setText(avenida);
                }

                if(!calzada.equals("NULL")){
                    ((EditText)getView().findViewById(R.id.dir_et_calzada)).setText(calzada);
                }

                ((EditText)getView().findViewById(R.id.dir_et_zona)).setText(zona);
                ((EditText)getView().findViewById(R.id.dir_et_municipio)).setText(municipio);
                ((Spinner)getView().findViewById(R.id.dir_sp_deptos)).setSelection(positionDepto(departamento));

            }else {
                this.onDestroy();
                mCallback.onNavigationDrawerItemSelected(TipoFragmento.INICIO);
                Toast.makeText(getActivity(), "No se pudo obtener la direccion", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            Log.e("direccion", e.getMessage());
            e.printStackTrace();
            this.onDestroy();
            mCallback.onNavigationDrawerItemSelected(TipoFragmento.INICIO);
            Toast.makeText(getActivity(), "No se pudo obtener la direccion", Toast.LENGTH_LONG).show();
        }
    }

    private void procesarGuardar(String data){
        try {
            JSONObject jso = new JSONObject(data);
            JSONObject t1 = jso.getJSONArray("array").getJSONObject(0);

            String tipo = t1.getString("Tipo");
            String desc = t1.getString("Descripcion");

            if(tipo.equals("1")){
                Toast.makeText(getActivity(), "Dirección guardada", Toast.LENGTH_LONG).show();
                regresar();
            }else{
                //Error al guardar
                Toast.makeText(getActivity(), desc, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            Log.e("direccion", e.getMessage());
            Toast.makeText(getActivity(), "No se pudo guardar la direccion", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private int positionDepto(String depto){
        for(int i = 0;i < departamentos.length; i++){
            if(depto.equals(departamentos[i])){
                return i;
            }
        }
        return 0;
    }

    @Override
    public void Wsdl2CodeStartedRequest() {
        guardar.setClickable(false);
    }

    @Override
    public void Wsdl2CodeFinished(String methodName, Object Data) {
        if(methodName.equals("nueva_direccion")){
            procesarGuardar(Data.toString());
        }else if(methodName.equals("get_direccion")){
            procesarDireccion(Data.toString());
        }
    }

    @Override
    public void Wsdl2CodeFinishedWithException(Exception ex) {
        //No se puede conectar
        Log.e("direccion", ex.getMessage());
        Toast.makeText(getActivity(), "No se pudo guardar la direccion", Toast.LENGTH_LONG).show();
        guardar.setClickable(true);
    }

    @Override
    public void Wsdl2CodeEndedRequest() {
        guardar.setClickable(true);
    }
}
