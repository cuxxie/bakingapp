package id.cuxxie.bakingapp.Activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import id.cuxxie.bakingapp.Fragment.DetailFragment;
import id.cuxxie.bakingapp.Model.Step;
import id.cuxxie.bakingapp.R;

public class StepsActivity extends AppCompatActivity implements StepFragmentDataPassing{
    Fragment fragmentDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

    }


    @Override
    public void passStepToDetailFragments(Step step) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.detail_fragment2);
        if(fragment instanceof DetailFragment)
        {
            DetailFragment detailFragment = (DetailFragment)fragment;
            detailFragment.loadAllFromStep(step);
        }
    }
}
