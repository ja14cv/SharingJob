package com.example.jcaal.sharingjob_v01.gui;

import android.app.ProgressDialog;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class nuevo_empleo extends FragmentGenerico implements IWsdl2CodeEvents{

    @Override
    public void otrosParametros(Bundle args, String[] parms) {

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
        ((Button) getView().findViewById(R.id.nempleo_btn_crear)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clic_crear(v);
            }
        });
        ((Button) getView().findViewById(R.id.nempleo_btn_cancelar)).setOnClickListener(new View.OnClickListener() {
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
        }

    }

    private void clic_cancelar(View v){
        this.onDestroy();
        mCallback.onNavigationDrawerItemSelected(TipoFragmento.INICIO); //ir a login
        return;
    }

    private ArrayList<String> procesar_categorias(String _json){
        ArrayList<String> retorno = new ArrayList<>();
        //_json = "{array:[{\"nombre\":\"Profesional\"},{\"nombre\":\"Ingenieria\"},{\"nombre\":\"Ingenieria, Ciencias y Sistemas\"},{\"nombre\":\"Ingenieria, Mecanica Industrial\"},{\"nombre\":\"Medicina\"},{\"nombre\":\"Ciencias Politicas y Sociales\"},{\"nombre\":\"Tecnica\"},{\"nombre\":\"Tecnica, Mecanico\"},{\"nombre\":\"Tecnica, Panadero\"},{\"nombre\":\"Artistica\"},{\"nombre\":\"Artistica, Danza\"},{\"nombre\":\"Artistica, Canto\"},{\"nombre\":\"Artistica, Teatro\"},{\"nombre\":\"Otros\"},{\"nombre\":\"Medicina, Forense\"}]}";
        try {
            JSONArray temp =  new JSONObject(_json).getJSONArray("array");

            for (int i=0 ; i<temp.length() ; i++){
                JSONObject temporal = temp.getJSONObject(i);
                retorno.add(temporal.getString("nombre"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return retorno;
    }

    @Override
    public void Wsdl2CodeStartedRequest() {
        dialog = ProgressDialog.show(getView().getContext(), "Cargando", "Por favor espere...", true);
    }

    @Override
    public void Wsdl2CodeFinished(String methodName, Object Data) {
        //Log.v("nuevo_empleo", methodName + " Data: " + (String) Data);
        if (methodName.equals("get_categoria_trabajo_nombres")) {
        ArrayAdapter<String> adapt = new ArrayAdapter<String>(getView().getContext(), android.R.layout.simple_list_item_1, procesar_categorias((String) Data));
        spin.setAdapter(adapt);
        }
        dialog.dismiss();
        if (methodName.equals("nuevo_empleo")) {
            Toast.makeText(getView().getContext(), (String)Data, Toast.LENGTH_SHORT).show();
            ((EditText)getView().findViewById(R.id.nempleo_et_titulo)).setText("");
            ((EditText)getView().findViewById(R.id.nempleo_et_descrip)).setText("");
            ((EditText)getView().findViewById(R.id.nempleo_et_propuesta)).setText("");
            spin.setSelection(0);
        }
    }

    @Override
    public void Wsdl2CodeFinishedWithException(Exception ex) {

    }

    @Override
    public void Wsdl2CodeEndedRequest() {

    }
}