package com.example.jcaal.sharingjob_v01.gui;

import android.app.ProgressDialog;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jcaal.sharingjob_v01.R;
import com.example.jcaal.sharingjob_v01.logica.TipoFragmento;
import com.example.jcaal.sharingjob_v01.logica.sesion;
import com.example.jcaal.sharingjob_v01.ws.IWsdl2CodeEvents;
import com.example.jcaal.sharingjob_v01.ws.ws_sharingJob;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class nuevo_empleo extends FragmentGenerico implements IWsdl2CodeEvents{

    @Override
    public void otrosParametros(String[] parms) {

    }

    private Spinner spin;
    private ProgressDialog dialog;

    @Override
    public void hacerOnCreate(){
        mCallback.seleccion(tipoFragmento);
        if(!sesion.isLogin()){ //Si No esta logueado
            Toast.makeText(getView().getContext(),"Inicie sesión...", Toast.LENGTH_SHORT).show();
            this.onDestroy();
            mCallback.onNavigationDrawerItemSelected(TipoFragmento.LOGIN); //ir a login
            return;
        }
        spin = (Spinner) getView().findViewById(R.id.nempleo_spin_categoria);
        (getView().findViewById(R.id.nempleo_btn_crear)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clic_crear(v);
            }
        });
        (getView().findViewById(R.id.nempleo_btn_cancelar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clic_cancelar(v);
            }
        });
        ws_sharingJob ws = new ws_sharingJob(this);
        try {
            ws.get_categoria_trabajo_nombresAsync();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("nuevo_empleado", e.getMessage());
            Toast.makeText(getActivity(), "Ocurrio un problema al cargar categorias", Toast.LENGTH_LONG).show();
        }
    }

    private void clic_crear(View v){
        String titulo = ((EditText)getView().findViewById(R.id.nempleo_et_titulo)).getText().toString();
        String descripcion = ((EditText)getView().findViewById(R.id.nempleo_et_descrip)).getText().toString();
        String propuesta = ((EditText)getView().findViewById(R.id.nempleo_et_propuesta)).getText().toString();
        String categoria = spin.getSelectedItem().toString();
        String ofertante = sesion.id_sesion;
        String json = "{" +
                "\"titulo\":\""+titulo+"\",\n" +
                "\"descripcion\":\""+descripcion+"\",\n" +
                "\"propuesta\":\""+propuesta+"\",\n" +
                "\"categoria\":\""+categoria+"\",\n" +
                "\"ofertante\":\""+ofertante+"\"" +
                "}";
        ws_sharingJob ws = new ws_sharingJob(this);
        try {
            ws.nuevo_empleoAsync(json);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("nuevo_empleado", e.getMessage());
            Toast.makeText(getActivity(), "No se pudo guardar empleo", Toast.LENGTH_LONG).show();
        }

    }

    private void clic_cancelar(View v){
        this.onDestroy();
        mCallback.onNavigationDrawerItemSelected(TipoFragmento.INICIO); //ir a login
        return;
    }

    private ArrayList<String> procesar_categorias(String data){
        ArrayList<String> retorno = new ArrayList<>();
        try {
            JSONObject jso = new JSONObject(data);
            JSONObject t1 = jso.getJSONArray("datos").getJSONObject(0);

            String tipo = t1.getString("Tipo");
            String desc = t1.getString("Descripcion");

            if(tipo.equals("1")){
                JSONArray temp =  jso.getJSONArray("array");

                for (int i=0 ; i<temp.length() ; i++){
                    JSONObject temporal = temp.getJSONObject(i);
                    retorno.add(temporal.getString("nombre"));
                }

            }else{
                Toast.makeText(getActivity(), desc, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("nuevo_empleado", e.getMessage());
            Toast.makeText(getActivity(), "Ocurrio un problema al cargar categorias", Toast.LENGTH_LONG).show();
        }
        return retorno;
    }

    private void procesarNuevoEmpleo(String data){
        try {
            JSONObject jso = new JSONObject(data);
            JSONObject t1 = jso.getJSONArray("datos").getJSONObject(0);

            String tipo = t1.getString("Tipo");
            String desc = t1.getString("Descripcion");

            if(tipo.equals("1")){
                ((EditText)getView().findViewById(R.id.nempleo_et_titulo)).setText("");
                ((EditText)getView().findViewById(R.id.nempleo_et_descrip)).setText("");
                ((EditText)getView().findViewById(R.id.nempleo_et_propuesta)).setText("");
                spin.setSelection(0);
                Toast.makeText(getActivity(), "Empleo creado", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getActivity(), desc, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("nuevo_empleo", e.getMessage());
            Toast.makeText(getActivity(), "No se pudo crear empleo", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void Wsdl2CodeStartedRequest() {
        dialog = ProgressDialog.show(getView().getContext(), "Cargando", "Por favor espere...", true);
    }

    @Override
    public void Wsdl2CodeFinished(String methodName, Object Data) {
        //Log.v("nuevo_empleo", methodName + " Data: " + (String) Data);
        if (methodName.equals("get_categoria_trabajo_nombres")) {
            ArrayAdapter<String> adapt = new ArrayAdapter<>(getView().getContext(), android.R.layout.simple_list_item_1, procesar_categorias((String) Data));
            spin.setAdapter(adapt);
        }else if (methodName.equals("nuevo_empleo")) {
            procesarNuevoEmpleo(Data.toString());
        }

        dialog.dismiss();
    }

    @Override
    public void Wsdl2CodeFinishedWithException(Exception ex) {
        Log.e("nuevo_empleado", ex.getMessage());
        Toast.makeText(getActivity(), "Ocurrio un problema al cargar categorias", Toast.LENGTH_LONG).show();
    }

    @Override
    public void Wsdl2CodeEndedRequest() {

    }
}