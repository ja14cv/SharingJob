package com.example.jcaal.sharingjob_v01.gui;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.jcaal.sharingjob_v01.R;
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

    private DatePickerDialog dp_inicio, dp_fin;
    private SimpleDateFormat dateFormatter;
    private ProgressDialog dialog;

    @Override
    public void hacerOnCreate() {
        et_fechaI = (EditText) getView().findViewById(R.id.addEmpR_et_fechaI);
        et_fechaF = (EditText) getView().findViewById(R.id.addEmpR_et_fechaF);
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
        ws_sharingJob ws = new ws_sharingJob(this);
        try {
            ws.get_categoria_trabajo_nombresAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
        inicializarDatePicker();
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
        if (methodName.equals("get_categoria_trabajo_nombres")) {
            ArrayAdapter<String> adapt = new ArrayAdapter<>(getView().getContext(), android.R.layout.simple_list_item_1, procesar_categorias((String) Data));
            spin.setAdapter(adapt);
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
