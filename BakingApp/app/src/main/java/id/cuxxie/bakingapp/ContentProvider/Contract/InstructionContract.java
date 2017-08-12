package id.cuxxie.bakingapp.ContentProvider.Contract;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by hendr on 8/11/2017.
 */

public class InstructionContract extends BaseContract {
    public static final String PATH_INSTRUCTION = "instruction";
    public static final class InstructionEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_INSTRUCTION).build();

        public static final String TABLE_NAME = "instruction";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SERVINGS = "servings";
        public static final String COLUMN_IMAGE = "image";
    }
}
