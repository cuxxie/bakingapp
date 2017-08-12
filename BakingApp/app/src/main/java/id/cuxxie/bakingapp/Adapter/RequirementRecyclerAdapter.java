package id.cuxxie.bakingapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.cuxxie.bakingapp.Data.DataRetriever;
import id.cuxxie.bakingapp.Model.Requirement;
import id.cuxxie.bakingapp.R;

/**
 * Created by hendr on 8/12/2017.
 */

public class RequirementRecyclerAdapter extends RecyclerView.Adapter<RequirementRecyclerAdapter.ViewHolder> {
    ArrayList<Requirement> requirements;
    private final Context context;

    public RequirementRecyclerAdapter(ArrayList<Requirement> requirements, Context context) {
        this.requirements = requirements;
        this.context = context;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        private final Context context;
        @BindView(R.id.req_item_title) TextView itemTitle;
        @BindView(R.id.req_item_check) CheckBox checkBox;
        public ViewHolder(View v, Context context) {
            super(v);
            ButterKnife.bind(this,v);
            this.context = context;
        }

        public void bind(Requirement requirement)
        {
            itemTitle.setText(requirement.getName());
            checkBox.setChecked(requirement.isStatus());
        }

        public void setCheckBoxListener(CompoundButton.OnCheckedChangeListener callback){
            checkBox.setOnCheckedChangeListener(callback);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.requirements_list_item, parent, false);
        return new RequirementRecyclerAdapter.ViewHolder(v,context);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.bind(requirements.get(position));
        final int index = holder.getAdapterPosition();
//        holder.setCheckBoxListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                checkBoxChanged(index,isChecked);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return requirements.size();
    }

    private void checkBoxChanged(int position, boolean status){
        requirements.get(position).setStatus(status);
        DataRetriever.getInstance(context).updateRequirementStatus(requirements.get(position),context,status,this);
    }
}
