package id.cuxxie.bakingapp.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.cuxxie.bakingapp.Adapter.InstructionRecyclerAdapter;
import id.cuxxie.bakingapp.Data.DataRetriever;
import id.cuxxie.bakingapp.Data.DataRetrieverListener;
import id.cuxxie.bakingapp.Model.Instruction;
import id.cuxxie.bakingapp.R;

public class MainActivity extends AppCompatActivity implements DataRetrieverListener.APICallback, ActivityTransitionInterface {
    @BindView(R.id.instruction_recyclerview) RecyclerView recyclerView;
    InstructionRecyclerAdapter adapter;
    LinearLayoutManager mLayoutManager;
    DataRetrieverListener dataRetrieverListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
      //  recyclerView = (RecyclerView) findViewById(R.id.instruction_recyclerview);
        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        ArrayList<Instruction> instructions = new ArrayList<Instruction>();
        if(savedInstanceState != null && savedInstanceState.containsKey("instructions"))
            instructions = savedInstanceState.getParcelableArrayList("instructions");

        adapter = new InstructionRecyclerAdapter(this,this,instructions);
        recyclerView.setAdapter(adapter);
        dataRetrieverListener = new DataRetrieverListener(this);
        if(savedInstanceState == null)
            DataRetriever.getInstance(this).downloadDataAndSaveDeltaToDB(this,dataRetrieverListener);
    }

    public void loadDataFromDB()
    {
        DataRetriever.getInstance(this).loadAllInstruction(dataRetrieverListener,this,this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("instructions",adapter.getInstructions());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void didCompleteDataQueryWithInstruction(ArrayList<Instruction> instructions) {
        adapter.setInstructions(instructions);
        runOnUiThread(new Runnable() {
            public void run(){
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void didCompleteInsertOrDeleteOperation(boolean isInsert) {

    }

    @Override
    public void didCompleteSyncData() {
        runOnUiThread(new Runnable() {
            public void run(){
                loadDataFromDB();
            }
        });
    }

    @Override
    public void errorOnAPICall() {

    }

    @Override
    public void movingToDetailsWithPosition(int position) {
        Intent intent = new Intent(this,StepsActivity.class);
        intent.putExtra("steps",adapter.getInstructions().get(position).getSteps());
        intent.putExtra("requirements",adapter.getInstructions().get(position).getRequirements());
        startActivity(intent);
    }
}
