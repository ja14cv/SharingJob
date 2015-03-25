package com.example.jcaal.sharingjob_v01;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class nuevo_empleo extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static nuevo_empleo newInstance(int sectionNumber) {
        nuevo_empleo fragment = new nuevo_empleo();

        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    public nuevo_empleo() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nuevo_empleo, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((principal) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
}
