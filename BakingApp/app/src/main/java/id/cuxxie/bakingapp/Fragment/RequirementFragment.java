package id.cuxxie.bakingapp.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.cuxxie.bakingapp.Adapter.RequirementRecyclerAdapter;
import id.cuxxie.bakingapp.Adapter.StepRecyclerAdapter;
import id.cuxxie.bakingapp.Model.Requirement;
import id.cuxxie.bakingapp.Model.Step;
import id.cuxxie.bakingapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequirementFragment extends Fragment {

    @BindView(R.id.req_recycleview) RecyclerView recyclerView;
    RequirementRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    public RequirementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_requirement, container, false);
        ButterKnife.bind(this,v);
        ArrayList<Requirement> requirements = new ArrayList<>();
        if(savedInstanceState != null && savedInstanceState.containsKey("requirements"))
            requirements = savedInstanceState.getParcelableArrayList("requirements");
        else if(getActivity().getIntent().hasExtra("requirements"))
            requirements = getActivity().getIntent().getParcelableArrayListExtra("requirements");

        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        adapter = new RequirementRecyclerAdapter(requirements,getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        return v;
    }

}
