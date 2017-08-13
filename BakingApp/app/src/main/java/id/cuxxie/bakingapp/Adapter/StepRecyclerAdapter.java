package id.cuxxie.bakingapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.cuxxie.bakingapp.Activity.ActivityTransitionInterface;
import id.cuxxie.bakingapp.Model.Instruction;
import id.cuxxie.bakingapp.Model.Step;
import id.cuxxie.bakingapp.R;

/**
 * Created by hendr on 8/12/2017.
 */

public class StepRecyclerAdapter extends RecyclerView.Adapter<StepRecyclerAdapter.ViewHolder> {
    ArrayList<Step> steps;
    private final ActivityTransitionInterface activityCallback;
    private final Context context;

    public StepRecyclerAdapter(ArrayList<Step> steps, ActivityTransitionInterface activity, Context context) {
        this.steps = steps;
        this.activityCallback = activity;
        this.context = context;

        Step ingredientStep = new Step();
        ingredientStep.setShortDescription("Recipe Ingredients");
        if(!this.steps.get(0).getShortDescription().equals(ingredientStep.getShortDescription()))
            this.steps.add(0,ingredientStep);
    }

    //TODO add UI for step list
    static class ViewHolder extends RecyclerView.ViewHolder {
        private final Context context;
        @BindView(R.id.step_item_title) TextView textView;
        public ViewHolder(View v, Context context) {
            super(v);
            ButterKnife.bind(this,v);
            this.context = context;
        }
        public void bind(Step step)
        {
            textView.setText(step.getShortDescription());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.steps_list_item, parent, false);
        return new ViewHolder(v,context);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(steps.get(position));
        final int index = holder.getAdapterPosition();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stepItemTouchedAt(index);
            }
        });
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    private void stepItemTouchedAt(int index){
        this.activityCallback.movingToDetailsWithPosition(index);
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

}
