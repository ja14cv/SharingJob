package com.example.jcaal.sharingjob_v01.gui;

import android.app.ProgressDialog;
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

    @Override   //TERMINADO
    public void hacerOnCreate() {
        et_grado = (EditText) getView().findViewById(R.id.addEst_et_grado);
        et_desc = (EditText) getView().findViewById(R.id.addEst_et_desc);
        sp_categorias = (Spinner) getView().findViewById(R.id.addEst_sp_categorias);
        btn_agregar = (Button) getView().findViewById(R.id.addEst_btn_agregar);
        btn_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_btn_agregar(v);
            }
        });
        btn_cancelar = (Button) getView().findViewById(R.id.addEst_btn_cancelar);
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_btn_cancelar(v);
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
        this.onDestroy();
        mCallback.onNavigationDrawerItemSelected(TipoFragmento.CUENTA);
    }

    public void onClick_btn_agregar(View v){
        String grado = et_grado.getText().toString().trim();
        String observacion = et_desc.getText().toString().trim();
        String categoria = sp_categorias.getSelectedItem().toString();
        if (grado.isEmpty()){
            grado = "NULL";
        }
        if (observacion.isEmpty()){
            observacion = "NULL";
        }
        if (categoria.isEmpty()){
            categoria = "NULL";
        }

        String json = "{\n" +
                "\t\"grado\":\""+grado+"\",\n" +
                "\t\"observacion\":\""+observacion+"\",\n" +
                "\t\"categoria\":\""+categoria+"\",\n" +
                "\t\"sesion\":\""+sesion.id_sesion+"\"\n" +
                "}\n";
        Log.i("estudio_realizado", "json_entrada: " + json);
        try {
            new ws_sharingJob(this).add_estudio_realizadoAsync(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        if (methodName.equals("add_estudio_realizado")){
            switch(procesarData((String) Data)){
                case 0:     //no agrego el trabajo
                    break;
                case 1:     //agrego el trabajo
                    this.onDestroy();
                    mCallback.onNavigationDrawerItemSelected(TipoFragmento.ESTUDIOS_REALIZADOS);
                    break;
                default:    //error general
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
