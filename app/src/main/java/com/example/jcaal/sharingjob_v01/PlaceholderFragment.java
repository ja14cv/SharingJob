package com.example.jcaal.sharingjob_v01;

/**
 * Created by jCaal on 25/03/2015.
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private int seleccion;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {

        PlaceholderFragment fragment = new PlaceholderFragment();
        fragment.set_seleccion(sectionNumber);
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    public PlaceholderFragment() {
    }

    public void set_seleccion(int _seleccion) {
        seleccion = _seleccion;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View rootView = inflater.inflate(R.layout.fragment_principal, container, false);
        View rootView = null;
        switch(seleccion){
            case 1:
                rootView = inflater.inflate(R.layout.fragment_principal, container, false);
                break;
            case 2:
                rootView = inflater.inflate(R.layout.fragment_mi_cuenta, container, false);
                break;
            case 3:
                rootView = inflater.inflate(R.layout.fragment_nuevo_empleo, container, false);
                break;
            default:
                rootView = inflater.inflate(R.layout.fragment_principal, container, false);
                break;
        }
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((principal) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
}