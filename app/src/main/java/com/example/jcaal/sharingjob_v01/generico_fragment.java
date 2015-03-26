package com.example.jcaal.sharingjob_v01;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

//Cambiar nombre de clase
public abstract class generico_fragment extends Fragment{
    public static final String ARG_SECTION_NUMBER = "section_number";
    public int layout;

    //Cambiar nombre del metodo por el nombre de clase
    public static generico_fragment newInstance(int sectionNumber, generico_fragment fragment, int _layout, String lista) {
        fragment.setLayout(_layout);
        //Paso de argumentos para recibir en onAttach
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.otrosParametros(args, lista);
        fragment.setArguments(args);
        //Fin paso de argumentos
        return fragment;
    }

    public abstract void otrosParametros(Bundle args, String lista);

    public generico_fragment(){

    }
    public void setLayout(int _layout){
        layout = _layout;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(layout, container, false);
    }

    //Variables de uso local
    public NavigationDrawerFragment.NavigationDrawerCallbacks mCallback;
    public int seleccion;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hacerOnCreate();
    }

    public abstract void hacerOnCreate();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //Guardar el id de fragment
        seleccion = getArguments().getInt(ARG_SECTION_NUMBER);
        ((principal) activity).onSectionAttached(seleccion);

        try {
            mCallback = (NavigationDrawerFragment.NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
}
