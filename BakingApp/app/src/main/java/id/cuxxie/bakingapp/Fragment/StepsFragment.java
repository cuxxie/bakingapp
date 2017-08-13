package id.cuxxie.bakingapp.Fragment;


import android.content.Intent;
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
import id.cuxxie.bakingapp.Activity.ActivityTransitionInterface;
import id.cuxxie.bakingapp.Activity.DetailActivity;
import id.cuxxie.bakingapp.Activity.RequirementsActivity;
import id.cuxxie.bakingapp.Activity.StepFragmentDataPassing;
import id.cuxxie.bakingapp.Adapter.StepRecyclerAdapter;
import id.cuxxie.bakingapp.Model.Requirement;
import id.cuxxie.bakingapp.Model.Step;
import id.cuxxie.bakingapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepsFragment extends Fragment  implements ActivityTransitionInterface{
    @BindView(R.id.steps_recycleview) RecyclerView recyclerView;
    StepRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    public StepsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_steps, container, false);
        ButterKnife.bind(this,v);
        ArrayList<Step> steps = new ArrayList<>();
        if(savedInstanceState != null && savedInstanceState.containsKey("steps"))
            steps = savedInstanceState.getParcelableArrayList("steps");
        else if(getActivity().getIntent().hasExtra("steps"))
            steps = getActivity().getIntent().getParcelableArrayListExtra("steps");

        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        adapter = new StepRecyclerAdapter(steps,this,getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("steps",adapter.getSteps());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void movingToDetailsWithPosition(int position) {
        if(getContext().getResources().getBoolean(R.bool.isTablet)){
            if(getActivity() instanceof StepFragmentDataPassing)
            {

                StepFragmentDataPassing stepFragmentDataPassing = (StepFragmentDataPassing) getActivity();
                if(position!= 0)
                    stepFragmentDataPassing.passStepToDetailFragments(adapter.getSteps().get(position));
                else{
                    ArrayList<Requirement> reqs = getActivity().getIntent().getParcelableArrayListExtra("requirements");
                    String text = "";
                    for(Requirement req : reqs)
                    {
                        text = text + String.format(getContext().getString(R.string.ingredient_widget),req.getName(),String.valueOf(req.getQuantity()),req.getMeasure()) + "\n";
                    }
                    Step reqStep = new Step();
                    reqStep.setDescription(text);
                    stepFragmentDataPassing.passStepToDetailFragments(reqStep);
                }
            }
        }else {
            Intent intent;
            //TODO: create activity transition
            if (position == 0) {
                //TODO: move to ingredients
                intent = new Intent(getActivity(), RequirementsActivity.class);
                if (getActivity().getIntent().hasExtra("requirements")) {
                    intent.putExtra("requirements", getActivity().getIntent().getParcelableArrayListExtra("requirements"));
                }
            } else {
                //TODO: move to details
                intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("step", adapter.getSteps().get(position));
            }
            getActivity().startActivity(intent);
        }
    }
}
