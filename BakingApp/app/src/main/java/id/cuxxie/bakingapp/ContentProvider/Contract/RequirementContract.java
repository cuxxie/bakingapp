package id.cuxxie.bakingapp.ContentProvider.Contract;

import android.net.Uri;
import android.provider.BaseColumns;

import static id.cuxxie.bakingapp.ContentProvider.Contract.InstructionContract.PATH_INSTRUCTION;

/**
 * Created by hendr on 8/11/2017.
 */

public class RequirementContract extends BaseContract {
    public static final String PATH_REQUIREMENT = "requirement";
    public static final class RequirementEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REQUIREMENT).build();
        public static final String TABLE_NAME = "requirement";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_INSTRUCTION_ID = "instructionid";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_MEASURE = "measure";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_STATUS = "status";
    }
}
