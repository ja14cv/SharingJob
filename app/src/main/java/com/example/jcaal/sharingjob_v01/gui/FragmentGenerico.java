package com.example.jcaal.sharingjob_v01.gui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jcaal.sharingjob_v01.logica.TipoFragmento;

//Cambiar nombre de clase
public abstract class FragmentGenerico extends Fragment{
    public static final String ARG_ID_LAYOUT = "id_layout";

    public NavigationDrawerFragment.NavigationDrawerCallbacks mCallback;
    public TipoFragmento tipoFragmento;
    public int id_layout;

    public abstract void otrosParametros(String[] parms);
    public abstract void hacerOnCreate();

    //Cambiar nombre del metodo por el nombre de clase
    public static FragmentGenerico newInstance(FragmentGenerico fragment, int _id_layout, TipoFragmento tf, String[] parms) {

        //Paso de argumentos para recibir en onAttach
        fragment.tipoFragmento = tf;
        Bundle args = new Bundle();
        args.putInt(ARG_ID_LAYOUT, _id_layout);
        fragment.setArguments(args);
        fragment.otrosParametros(parms);

        //Fin paso de argumentos
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(id_layout, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hacerOnCreate();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (getArguments() != null) {
            id_layout = getArguments().getInt(ARG_ID_LAYOUT);
        }
        ((principal) activity).onSectionAttached(tipoFragmento);

        try {
            mCallback = (NavigationDrawerFragment.NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    public static InputFilter[] filtroQuote = new InputFilter[] { new InputFilter(){

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if(source.equals("\"") || source.equals("'")){
                return "";
            }

            return  source;
        }
    }};
}
