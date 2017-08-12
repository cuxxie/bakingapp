package id.cuxxie.bakingapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.cuxxie.bakingapp.Model.Instruction;
import id.cuxxie.bakingapp.R;

/**
 * Created by hendr on 8/12/2017.
 */

public class InstructionRecyclerAdapter extends RecyclerView.Adapter<InstructionRecyclerAdapter.ViewHolder> {
    ArrayList<Instruction> instructions;
    private final Activity activity;
    private final Context context;
    public InstructionRecyclerAdapter(Activity activity, Context context, ArrayList<Instruction> instructions) {
        this.instructions = instructions;
        this.activity = activity;
        this.context = context;
    }

     static class ViewHolder extends RecyclerView.ViewHolder {
         private final Context context;
        // each data item is just a string in this case
        @BindView(R.id.instruction_card) CardView mCardView;
        @BindView(R.id.instruction_text) TextView textView;
        @BindView(R.id.instruction_image) ImageView imageView;
        public ViewHolder(View v, Context context) {
            super(v);
            ButterKnife.bind(this,v);
            this.context = context;
        }

        public void bind(Instruction instruction)
        {
            if(instruction.getImage().length() > 0)
                Glide.with(this.context).load(instruction.getImage()).into(imageView);
            textView.setText(String.format(context.getString(R.string.instruction_title),instruction.getName(),String.valueOf(instruction.getServings())));
        }

        public void setOnClickListener(View.OnClickListener onClickListener)
        {
            this.mCardView.setOnClickListener(onClickListener);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.instruction_list_item, parent, false);
        return new ViewHolder(v,context);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(instructions.get(position));
        final int index = holder.getAdapterPosition();
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                instructionItemTouchedAt(index);
            }
        });
    }

    @Override
    public int getItemCount() {
        return instructions.size();
    }

    public void instructionItemTouchedAt(int position) {
    }

    public void setInstructions(ArrayList<Instruction> instructions) {
        this.instructions = instructions;
    }

    public ArrayList<Instruction> getInstructions() {
        return instructions;
    }
}
