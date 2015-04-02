package com.example.jcaal.sharingjob_v01.gui;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.jcaal.sharingjob_v01.R;
import com.example.jcaal.sharingjob_v01.ws.IWsdl2CodeEvents;
import com.example.jcaal.sharingjob_v01.ws.ws_sharingJob;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jCaal on 02/04/2015.
 */
public class add_estudio_realizado extends FragmentGenerico implements IWsdl2CodeEvents {
    @Override
    public void otrosParametros(String[] parms) {

    }

    private EditText et_grado, et_desc;
    private Spinner sp_categorias;
    private Button btn_agregar, btn_cancelar;

    private ProgressDialog dialog;

    @Override
    public void hacerOnCreate() {
        et_grado = (EditText) getView().findViewById(R.id.addEst_et_grado);
        et_desc = (EditText) getView().findViewById(R.id.addEst_et_desc);
        sp_categorias = (Spinner) getView().findViewById(R.id.addEst_sp_categorias);
        btn_agregar = (Button) getView().findViewById(R.id.addEst_btn_agregar);
        btn_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_btn_cancelar(v);
            }
        });
        btn_cancelar = (Button) getView().findViewById(R.id.addEst_btn_cancelar);
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_btn_agregar(v);
            }
        });

        //obtener categorias de estudio
        try {
            new ws_sharingJob(this).get_categoria_estudio_nombresAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClick_btn_cancelar(View v){

    }

    public void onClick_btn_agregar(View v){

    }

    private ArrayList<String> procesar_categorias(String _json){
        ArrayList<String> retorno = new ArrayList<>();
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
        if (methodName.equals("get_categoria_estudio_nombres")) {
            ArrayAdapter<String> adapt = new ArrayAdapter<>(getView().getContext(), android.R.layout.simple_list_item_1, procesar_categorias((String) Data));
            sp_categorias.setAdapter(adapt);
        }
        dialog.dismiss();
    }

    @Override
    public void Wsdl2CodeFinishedWithException(Exception ex) {

    }

    @Override
    public void Wsdl2CodeEndedRequest() {

    }
}
