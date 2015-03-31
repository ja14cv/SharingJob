package com.example.jcaal.sharingjob_v01.gui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jcaal.sharingjob_v01.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class nuevo_empleo extends FragmentGenerico{

    @Override
    public void otrosParametros(Bundle args, String[] parms) {

    }

    private Spinner spin;

    @Override
    public void hacerOnCreate(){
        mCallback.seleccion(tipoFragmento);
        spin = (Spinner) getView().findViewById(R.id.nempleo_spin_categoria);
        ArrayAdapter<String> adapt = new ArrayAdapter<String>(getView().getContext(), android.R.layout.simple_list_item_1, procesar_categorias(""));
        spin.setAdapter(adapt);
    }

    private ArrayList<String> procesar_categorias(String _json){
        ArrayList<String> retorno = new ArrayList<>();
        _json = "{array:[{\"nombre\":\"Profesional\"},{\"nombre\":\"Ingenieria\"},{\"nombre\":\"Ingenieria, Ciencias y Sistemas\"},{\"nombre\":\"Ingenieria, Mecanica Industrial\"},{\"nombre\":\"Medicina\"},{\"nombre\":\"Ciencias Politicas y Sociales\"},{\"nombre\":\"Tecnica\"},{\"nombre\":\"Tecnica, Mecanico\"},{\"nombre\":\"Tecnica, Panadero\"},{\"nombre\":\"Artistica\"},{\"nombre\":\"Artistica, Danza\"},{\"nombre\":\"Artistica, Canto\"},{\"nombre\":\"Artistica, Teatro\"},{\"nombre\":\"Otros\"},{\"nombre\":\"Medicina, Forense\"}]}";
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
}