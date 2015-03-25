package com.example.jcaal.sharingjob_v01;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class mi_cuenta extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static mi_cuenta newInstance(int sectionNumber) {
        mi_cuenta fragment = new mi_cuenta();

        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    public mi_cuenta() {
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
        return inflater.inflate(R.layout.fragment_mi_cuenta, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((principal) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

}
