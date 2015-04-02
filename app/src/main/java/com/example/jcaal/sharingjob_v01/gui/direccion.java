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

    private boolean parmsOK = false, editar = false;
    private String pertenece;
    private Button guardar, cancelar;
    private Spinner deptos;
    private EditText no, calle, avenida, calzada, zona, municipio;
    private static final String[] departamentos = new String[]{"Alta Verapaz", "Baja Verapaz", "Chimaltenango", "Chiquimula", "Petén", "El Progreso", "Quiché", "Escuintla",
            "Guatemala", "Huehuetenango", "Izabal", "Jalapa", "Jutiapa", "Quetzaltenango", "Retalhuleu", "Sacatepéquez", "San Marcos", "Santa Rosa", "Sololá", "Suchitepéquez", "Totonicapán", "Zacapa"};

    @Override
    public void otrosParametros(String[] parms) {
        Bundle args = this.getArguments();
        if(args != null && parms != null){
            if(parms.length >= 1){
                args.putString("pertenece", parms[0]);;
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

        no = (EditText) getView().findViewById(R.id.dir_et_no);
        calle = (EditText) getView().findViewById(R.id.dir_et_calle);
        avenida = (EditText) getView().findViewById(R.id.dir_et_avenida);
        calzada = (EditText) getView().findViewById(R.id.dir_et_calzada);
        zona = (EditText) getView().findViewById(R.id.dir_et_zona);
        municipio = (EditText) getView().findViewById(R.id.dir_et_municipio);
        deptos = (Spinner) getView().findViewById(R.id.dir_sp_deptos);
        setHabilitado(false);
        editar = false;

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

            //Cargar spinenr
            ArrayAdapter<String> adapt = new ArrayAdapter<>(getView().getContext(), android.R.layout.simple_list_item_1, departamentos);
            deptos.setAdapter(adapt);

            //Obtener argumentos
            Bundle args = this.getArguments();
            pertenece = args.getString("pertenece");

            //cargar datos de direccion
            String json = "{\n" +
                    "\t\"sesion\":\"" + sesion.id_sesion + "\",\n" +
                    "\t\"proposito\":\"" + pertenece + "\"\n" +
                    "}";

            Log.i("direccion", json);
            ws_sharingJob ws = new ws_sharingJob(this);
            try {
                ws.get_direccionAsync(json);
            } catch (Exception e) {
                Log.e("direccion", e.getMessage());
                Toast.makeText(getActivity(), "No se pudo obtener la direccion", Toast.LENGTH_LONG).show();
                e.printStackTrace();

            }
        }else{
            Toast.makeText(getActivity(), "Ocurrio un problema :(", Toast.LENGTH_LONG).show();
        }

        mCallback.seleccion(tipoFragmento);
    }

    private void onClick_guardar(View v){
        if(!editar){
            editar = true;
            guardar.setBackgroundResource(R.color.color_btn_aceptar);
            guardar.setText(R.string.guardar);
            cancelar.setVisibility(View.VISIBLE);
            setHabilitado(true);
        }else{
            String sNo = no.getText().toString().trim();
            String sCalle = calle.getText().toString().trim();
            String sAvenida = avenida.getText().toString().trim();
            String sCalzada = calzada.getText().toString().trim();
            String sZona = zona.getText().toString().trim();
            String sMunicipio = municipio.getText().toString().trim();
            String sDepartamento = deptos.getSelectedItem().toString();

            //Verificar campos
            if(!sZona.isEmpty() && !sMunicipio.isEmpty() && !sDepartamento.isEmpty()){
                //Conversion de campos vacios a nulos
                if(sNo.isEmpty()){
                    sNo = "NULL";
                }

                if(sCalle.isEmpty()){
                    sCalle = "NULL";
                }

                if(sAvenida.isEmpty()){
                    sAvenida = "NULL";
                }

                if(sCalzada.isEmpty()){
                    sCalzada = "NULL";
                }

                String json = "{\n" +
                        "\t\"calle\":\"" + sCalle + "\",\n" +
                        "\t\"avenida\":\"" + sAvenida + "\",\n" +
                        "\t\"calzada\":\"" + sCalzada + "\",\n" +
                        "\t\"zona\":\"" + sZona + "\",\n" +
                        "\t\"municipio\":\"" + sMunicipio + "\",\n" +
                        "\t\"departamento\":\"" + sDepartamento + "\",\n" +
                        "\t\"numero_residencia\":\"" + sNo + "\",\n" +
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
    }

    private void onClick_cancelar(View v){
        editar = false;
        guardar.setBackgroundResource(R.drawable.button_blue);
        guardar.setText(R.string.editar);
        cancelar.setVisibility(View.INVISIBLE);
        setHabilitado(false);
    }

    private void setHabilitado(boolean value){
        no.setEnabled(value);
        calle.setEnabled(value);
        avenida.setEnabled(value);
        calzada.setEnabled(value);
        zona.setEnabled(value);
        municipio.setEnabled(value);
        deptos.setEnabled(value);
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

            }/*else {
                Toast.makeText(getActivity(), "No se pudo obtener la direccion", Toast.LENGTH_LONG).show();
            }*/

        } catch (JSONException e) {
            Log.e("direccion", e.getMessage());
            Toast.makeText(getActivity(), "No se pudo obtener la direccion", Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }
    }

    private void procesarGuardar(String data){
        try {
            JSONObject jso = new JSONObject(data);
            JSONObject t1 = jso.getJSONArray("datos").getJSONObject(0);

            String tipo = t1.getString("Tipo");
            String desc = t1.getString("Descripcion");

            if(tipo.equals("1")){
                editar = false;
                guardar.setBackgroundResource(R.drawable.button_blue);
                guardar.setText(R.string.editar);
                cancelar.setVisibility(View.INVISIBLE);
                setHabilitado(false);
                Toast.makeText(getActivity(), "Dirección guardada", Toast.LENGTH_LONG).show();
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
        Log.i("llave", Data.toString());
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
