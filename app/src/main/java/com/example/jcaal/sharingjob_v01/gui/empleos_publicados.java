package com.example.jcaal.sharingjob_v01.gui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.jcaal.sharingjob_v01.R;
import com.example.jcaal.sharingjob_v01.logica.sesion;
import com.example.jcaal.sharingjob_v01.ws.IWsdl2CodeEvents;
import com.example.jcaal.sharingjob_v01.ws.ws_sharingJob;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jCaal on 02/04/2015.
 */
public class empleos_publicados extends FragmentGenerico implements IWsdl2CodeEvents {
    @Override
    public void otrosParametros(String[] parms) {

    }

    private ListView lv_empleos;
    private int posicion;

    private AlertDialog.Builder dialogo1;
    private ProgressDialog dialog;
    private ArrayList<String> mapa_empleos;

    @Override
    public void hacerOnCreate() {
        lv_empleos = (ListView) getView().findViewById(R.id.empPub_lv_empleos);
        lv_empleos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                posicion = position;
                dialogo1.show();
            }
        });
        dialogo1 = new AlertDialog.Builder(getView().getContext());
        dialogo1.setTitle("Empleo publicado");
        dialogo1.setMessage("¿Realmente desea eliminar el registro de empleo?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                removeItem(posicion);
            }
        });
        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
            }
        });

        try {
            new ws_sharingJob(this).get_empleos_publicadosAsync("{\"sesion\":\"" + sesion.id_sesion + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeItem(int posicion){
        String json = "{\n" +
                "\t\"id_trabajo\":\""+ mapa_empleos.get(posicion)+"\",\n" +
                "\t\"sesion\":\""+ sesion.id_sesion+"\"\n" +
                "}";
        try {
            new ws_sharingJob(this).del_empleo_publicadoAsync(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void Wsdl2CodeStartedRequest() {
        dialog = ProgressDialog.show(getView().getContext(), "Cargando", "Por favor espere...", true);
    }

    @Override
    public void Wsdl2CodeFinished(String methodName, Object Data) {
        dialog.dismiss();
        Log.i("empleos_publicados", "metodo: " + methodName + ", Data: " + (String) Data);
        if (methodName.equals("get_empleos_publicados")){
            switch(procesarData((String) Data)){
                case 0:     //no se obtuvieron
                    break;
                case 1:     //se obtuvieron
                    procesar_empleos((String) Data);
                    break;
                default:    //error general
            }
        }
        if (methodName.equals("del_empleo_publicado")){
            switch(procesarData((String) Data)){
                case 0:     //no se elimino
                    break;
                case 1:     //se elimino
                    try {
                        new ws_sharingJob(this).get_empleos_publicadosAsync("{\"sesion\":\"" + sesion.id_sesion + "\"}");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:    //error general
            }
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

    private void procesar_empleos(String _json){
        mapa_empleos = new ArrayList<>();
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        String[] from = new String[] { "titulo", "descripcion" };
        int[] to = new int[] { android.R.id.text1, android.R.id.text2 };
        int nativeLayout = android.R.layout.two_line_list_item;

        try {
            JSONArray temp =  new JSONObject(_json).getJSONArray("array");
            for (int i=0 ; i<temp.length() ; i++){
                JSONObject temporal = temp.getJSONObject(i);
                mapa_empleos.add(i,temporal.getString("id_empleo"));
                HashMap<String, String> item = new HashMap<>();
                item.put("titulo", temporal.getString("titulo"));
                item.put("descripcion",  " (" + temporal.getString("fecha_oferta") + ")\n" + temporal.getString("descripcion"));
                list.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        lv_empleos.setAdapter(new SimpleAdapter(getView().getContext(), list, nativeLayout , from, to));
    }

    @Override
    public void Wsdl2CodeFinishedWithException(Exception ex) {

    }

    @Override
    public void Wsdl2CodeEndedRequest() {

    }
}
