package id.cuxxie.bakingapp.ContentProvider.Contract;

import android.net.Uri;
import android.provider.BaseColumns;

import static id.cuxxie.bakingapp.ContentProvider.Contract.InstructionContract.PATH_INSTRUCTION;

/**
 * Created by hendr on 8/11/2017.
 */

public class StepContract extends BaseContract {
    public static final String PATH_STEP = "step";
    String shortDescription;
    String description;
    String videoURL;
    String thumbnailURL;
    public static final class StepEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_STEP).build();
        public static final String TABLE_NAME = "step";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_ROWID = "rowid";
        public static final String COLUMN_INSTRUCTION_ID = "instructionid";
        public static final String COLUMN_SHORT_DESC = "shortDescription";
        public static final String COLUMN_DESC = "description";
        public static final String COLUMN_VIDEO = "videoURL";
        public static final String COLUMN_THUMBNAIL = "thumbnailURL";
    }
}
