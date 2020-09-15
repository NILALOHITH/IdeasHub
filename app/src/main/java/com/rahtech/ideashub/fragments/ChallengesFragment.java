package com.rahtech.ideashub.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rahtech.ideashub.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChallengesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChallengesFragment extends Fragment {


    public ChallengesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChallengesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChallengesFragment newInstance(String param1, String param2) {

        return new ChallengesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_challenges, container, false);
    }
}