package com.example.jcaal.sharingjob_v01.gui;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by jCaal on 01/04/2015.
 */
public class add_empleo_realizado extends FragmentGenerico implements IWsdl2CodeEvents {
    @Override
    public void otrosParametros(String[] parms) {

    }

    private EditText et_fechaI, et_fechaF;
    private Button btn_setI, btn_setF;
    private Spinner spin;

    private EditText et_puesto, et_empresa, et_proyecto;
    private CheckBox cb_temporal;
    private Button btn_agregar, btn_cancelar;

    private DatePickerDialog dp_inicio, dp_fin;
    private SimpleDateFormat dateFormatter;
    private ProgressDialog dialog;

    @Override
    public void hacerOnCreate() {
        et_fechaI = (EditText) getView().findViewById(R.id.addEmpR_et_fechaI);
        et_fechaF = (EditText) getView().findViewById(R.id.addEmpR_et_fechaF);
        et_puesto = (EditText) getView().findViewById(R.id.addEmpR_et_puesto);
        et_empresa = (EditText) getView().findViewById(R.id.addEmpR_et_empresa);
        et_proyecto = (EditText) getView().findViewById(R.id.addEmpR_et_proyecto);
        btn_setI = (Button) getView().findViewById(R.id.addEmpR_btn_setI);
        btn_setI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dp_inicio.show();
            }
        });
        btn_setF = (Button) getView().findViewById(R.id.addEmpR_btn_setF);
        btn_setF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dp_fin.show();
            }
        });
        spin = (Spinner) getView().findViewById(R.id.addEmpR_sp_categorias);
        btn_agregar = (Button) getView().findViewById(R.id.addEmpR_btn_agregar);
        btn_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_btn_agregar(v);
            }
        });
        btn_cancelar = (Button) getView().findViewById(R.id.addEmpR_btn_cancelar);
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_btn_cancelar(v);
            }
        });
        cb_temporal = (CheckBox) getView().findViewById(R.id.addEmpR_cb_temporal);

        try {
            new ws_sharingJob(this).get_categoria_trabajo_nombresAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
        inicializarDatePicker();
    }

    private void onClick_btn_agregar(View v){
        String puesto = et_puesto.getText().toString().trim();
        String nombre_empresa = et_empresa.getText().toString().trim();
        String nombre_proyecto = et_proyecto.getText().toString().trim();
        String temporal = "0";
        if (cb_temporal.isSelected()){
            temporal = "1";
        }
        String fecha_ini = et_fechaI.getText().toString().trim();
        String fecha_fin = et_fechaF.getText().toString().trim();
        String categoria = spin.getSelectedItem().toString();
        if(puesto.isEmpty()){
            puesto = "NULL";
        }
        if(nombre_empresa.isEmpty()){
            nombre_empresa = "NULL";
        }
        if(nombre_proyecto.isEmpty()){
            nombre_proyecto = "NULL";
        }
        if(temporal.isEmpty()){
            temporal = "NULL";
        }
        if(fecha_ini.isEmpty()){
            fecha_ini = "NULL";
        }
        if(fecha_fin.isEmpty()){
            fecha_fin = "NULL";
        }
        if(categoria.isEmpty()){
            categoria = "NULL";
        }

        String json = "{\n" +
                "\t\"puesto\":\""+puesto+"\",\n" +
                "\t\"nombre_empresa\":\""+nombre_empresa+"\",\n" +
                "\t\"nombre_proyecto\":\""+nombre_proyecto+"\",\n" +
                "\t\"fecha_inicio\":\""+fecha_ini+"\",\n" +
                "\t\"fecha_fin\":\""+fecha_fin+"\",\n" +
                "\t\"temporal\":\""+temporal+"\",\n" +
                "\t\"categoria\":\""+categoria+"\",\n" +
                "\t\"sesion\":\""+ sesion.id_sesion+"\"\n" +
                "}";
        Log.i("empleo_realizado", "json_entrada: " + json);
        try {
            new ws_sharingJob(this).add_trabajo_realizadoAsync(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onClick_btn_cancelar(View v){
        this.onDestroy();
        mCallback.onNavigationDrawerItemSelected(TipoFragmento.CUENTA);
    }

    private void inicializarDatePicker(){
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        dp_inicio = new DatePickerDialog(getView().getContext(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                et_fechaI.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        dp_fin = new DatePickerDialog(getView().getContext(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                et_fechaF.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
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
        Log.i("empleo_realizado", "metodo: " + methodName + ", Data: " + (String) Data);
        if (methodName.equals("get_categoria_trabajo_nombres")) {
            ArrayAdapter<String> adapt = new ArrayAdapter<>(getView().getContext(), android.R.layout.simple_list_item_1, procesar_categorias((String) Data));
            spin.setAdapter(adapt);
        }
        if (methodName.equals("add_trabajo_realizado")){
            switch(procesarData((String) Data)){
                case 0:     //no agrego el trabajo
                    break;
                case 1:     //agrego el trabajo
                    this.onDestroy();
                    mCallback.onNavigationDrawerItemSelected(TipoFragmento.CUENTA);
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
