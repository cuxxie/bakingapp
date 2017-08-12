package id.cuxxie.bakingapp.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.cuxxie.bakingapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepsFragment extends Fragment {


    public StepsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_steps, container, false);
    }

}
