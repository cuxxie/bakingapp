package id.cuxxie.bakingapp.ContentProvider.Contract;

import android.net.Uri;

/**
 * Created by hendr on 8/11/2017.
 */

public abstract class BaseContract {
    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "id.cuxxie.bakingapp";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
}
