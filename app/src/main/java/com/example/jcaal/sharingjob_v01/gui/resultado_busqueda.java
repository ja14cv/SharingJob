package com.example.jcaal.sharingjob_v01.gui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jcaal.sharingjob_v01.R;
import com.example.jcaal.sharingjob_v01.logica.EmpleoItem;
import com.example.jcaal.sharingjob_v01.logica.TipoFragmento;
import com.example.jcaal.sharingjob_v01.ws.IWsdl2CodeEvents;
import com.example.jcaal.sharingjob_v01.ws.ws_sharingJob;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jCaal on 02/04/2015.
 */
public class resultado_busqueda extends FragmentGenerico implements IWsdl2CodeEvents {

    private LinearLayout layout_res;
    private boolean parmsOK = false;
    private ProgressDialog dialog;

    @Override
    public void otrosParametros(String[] parms) {
        Bundle args = this.getArguments();
        if(args != null && parms != null){
            if(parms.length >= 3){
                args.putString("tipo", parms[0]);
                args.putString("valor", parms[1]);
                args.putString("categoria", parms[2]);
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
        //Evento busqueda
        SearchView sv = (SearchView) getView().findViewById(R.id.res_sv);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                realizarBusqueda("palabra", query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        if(parmsOK){
            String tipo = this.getArguments().getString("tipo");
            String valor = this.getArguments().getString("valor");
            layout_res = (LinearLayout) getView().findViewById(R.id.res_ly_busqueda);

            //Cargar resultado de busqueda
            ws_sharingJob ws = new ws_sharingJob(this);
            try {
                if(tipo.equals("categoria")){
                    String json = "{\"id_categoria\":\"" + valor +"\"}";
                    Log.i("resutaldo", json);
                    ws.get_empleos_categoriaAsync(json);
                }else{
                    String json = "{\"nombre\":\"" + valor + "\"}";
                    Log.i("resutaldo", json);
                    ws.get_empleos_nombreAsync(json);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("resultado", e.getMessage());
                Toast.makeText(getActivity(), "No se pudo realizar la busqueda", Toast.LENGTH_LONG).show();
            }
        }

        mCallback.seleccion(tipoFragmento);
    }

    private void onClick_empleo_item(View v){
        EmpleoItem e = (EmpleoItem) v;
        if(e != null){
            e.setBackgroundResource(R.color.color_btn_aceptar);
            Log.i("item", String.valueOf(e.getIdEmpleo()));
            this.onDestroy();
            mCallback.onNavigationDrawerItemSelected(TipoFragmento.EMPLEO);
        }
    }

    private void procesarCategorias(String data){
        try {
            JSONObject jso = new JSONObject(data);
            JSONObject t1 = jso.getJSONArray("datos").getJSONObject(0);

            String tipo = t1.getString("Tipo");
            String desc = t1.getString("Descripcion");

            if(tipo.equals("1")){
                JSONArray t2 = jso.getJSONArray("array");
                for(int i = 0; i < t2.length(); i++){
                    JSONObject t3 = t2.getJSONObject(i);

                    String idEmpleo = t3.getString("id_empleo");
                    String titulo = t3.getString("titulo");

                    EmpleoItem eItem = new EmpleoItem(getActivity());
                    getActivity().getLayoutInflater().inflate(R.layout.empleo_item, eItem, true);
                    eItem.setIdEmpleo(idEmpleo);
                    eItem.setTitulo(titulo);
                    ((TextView)eItem.findViewById(R.id.item_empleo_titulo)).setText(titulo);

                    eItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onClick_empleo_item(v);
                        }
                    });
                    layout_res.addView(eItem);
                }

                Toast.makeText(getActivity(), desc, Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getActivity(), desc, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            Log.e("resutaldo", e.getMessage());
            Toast.makeText(getActivity(), "No se pudo realizar la busqueda", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void realizarBusqueda(String tipo, String valor){
        this.onDestroy();
        mCallback.onNavigationDrawerItemSelected(TipoFragmento.RESULTADO_BUSQUEDA, new String[]{tipo, valor, ""});
    }

    @Override
    public void Wsdl2CodeStartedRequest() {
        dialog = ProgressDialog.show(getView().getContext(), "Cargando", "Por favor espere...", true);
    }

    @Override
    public void Wsdl2CodeFinished(String methodName, Object Data) {
        Log.i("resutlado", Data.toString());
        procesarCategorias(Data.toString());
        dialog.dismiss();
    }

    @Override
    public void Wsdl2CodeFinishedWithException(Exception ex) {
        Log.e("resultado", ex.getMessage());
        //Toast.makeText(getActivity(), "No se pudo realizar la busqueda", Toast.LENGTH_LONG).show();
    }

    @Override
    public void Wsdl2CodeEndedRequest() {

    }
}
