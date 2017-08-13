package id.cuxxie.bakingapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import id.cuxxie.bakingapp.Data.DataRetriever;
import id.cuxxie.bakingapp.Data.DataRetrieverListener;
import id.cuxxie.bakingapp.Model.Instruction;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest implements DataRetrieverListener.APICallback {
    private CountDownLatch signal = null;
    DataRetrieverListener drl;
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("id.cuxxie.bakingapp", appContext.getPackageName());
    }

    @Test
    public void testCallAPI()
    {
        signal = new CountDownLatch(1);
        Context appContext = InstrumentationRegistry.getTargetContext();
        drl = new DataRetrieverListener(this);
        DataRetriever.getInstance(appContext).downloadDataAndSaveDeltaToDB(this,drl);
        try {
            signal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void didCompleteSyncData() {
        signal.countDown();
    }

    @Override
    public void errorOnAPICall() {

    }
    @Override
    public void didCompleteDataQueryWithInstruction(ArrayList<Instruction> instructions) {
        signal.countDown();
    }

    @Override
    public void didCompleteInsertOrDeleteOperation(boolean isInsert) {
        signal.countDown();
    }
}
