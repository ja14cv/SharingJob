package com.example.jcaal.sharingjob_v01.gui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.util.HashMap;

/**
 * Created by jCaal on 02/04/2015.
 */
public class empleos_tomados extends FragmentGenerico implements IWsdl2CodeEvents {
    @Override
    public void otrosParametros(String[] parms) {

    }

    private Button btn_add;
    private ListView lv_trabajos;
    private int posicion;

    private ProgressDialog dialog;
    private AlertDialog.Builder dialogo1;
    private ArrayList<String> mapa_trabajos;

    @Override
    public void hacerOnCreate() {
        btn_add = (Button) getView().findViewById(R.id.empTomados_btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_btn_add(v);
            }
        });
        lv_trabajos = (ListView) getView().findViewById(R.id.empTomados_lv_trabajos);
        lv_trabajos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //removeItem(position);
                posicion = position;
                dialogo1.show();
            }
        });

        dialogo1 = new AlertDialog.Builder(getView().getContext());
        dialogo1.setTitle("Empleo tomado");
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
            new ws_sharingJob(this).get_trabajos_realizadosAsync("{\"sesion\":\"" + sesion.id_sesion + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onClick_btn_add(View v){
        this.onDestroy();
        mCallback.onNavigationDrawerItemSelected(TipoFragmento.ADD_EMPLEO_REALIZADO);
    }

    private void removeItem(int posicion){
        String json = "{\n" +
                "\t\"id_trabajo\":\""+ mapa_trabajos.get(posicion)+"\",\n" +
                "\t\"sesion\":\""+ sesion.id_sesion+"\"\n" +
                "}";
        try {
            new ws_sharingJob(this).del_trabajo_realizadoAsync(json);
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
        Log.i("trabajos_realizados", "metodo: " + methodName + ", Data: " + (String) Data);
        if (methodName.equals("get_trabajos_realizados")){
            switch(procesarData((String) Data)){
                case 0:     //no se obtuvieron
                    break;
                case 1:     //se obtuvieron
                    procesar_trabajos((String) Data);
                    break;
                default:    //error general
            }
        }
        if (methodName.equals("del_trabajo_realizado")){
            switch(procesarData((String) Data)){
                case 0:     //no se elimino
                    break;
                case 1:     //se elimino
                    try {
                        new ws_sharingJob(this).get_trabajos_realizadosAsync("{\"sesion\":\"" + sesion.id_sesion + "\"}");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:    //error general
            }
        }
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

    private void procesar_trabajos(String _json){
        mapa_trabajos = new ArrayList<>();
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        String[] from = new String[] { "puesto", "observacion" };
        int[] to = new int[] { android.R.id.text1, android.R.id.text2 };
        int nativeLayout = android.R.layout.two_line_list_item;

        try {
            JSONArray temp =  new JSONObject(_json).getJSONArray("array");
            for (int i=0 ; i<temp.length() ; i++){
                JSONObject temporal = temp.getJSONObject(i);
                mapa_trabajos.add(i, temporal.getString("id_trabajo"));
                HashMap<String, String> item = new HashMap<>();
                item.put("puesto", temporal.getString("puesto") + " (" + temporal.getString("nombre") + ")");
                item.put("observacion", "Empresa: " + temporal.getString("nombre_empresa") +"\n" + temporal.getString("fecha_inicio") + "->" + temporal.getString("fecha_fin"));
                list.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        lv_trabajos.setAdapter(new SimpleAdapter(getView().getContext(), list, nativeLayout , from, to));
    }
}
