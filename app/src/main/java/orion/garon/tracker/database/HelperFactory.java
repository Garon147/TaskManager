package orion.garon.tracker.database;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;

/**
 * Created by VKI on 06.05.2017.
 */

public class HelperFactory {

    private static DatabaseHelper databaseHelper;

    public static DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }

    public static void setDatabaseHelper(Context context) {
        databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
    }

    public static void  releaseDatabaseHelper() {
        OpenHelperManager.releaseHelper();
        databaseHelper = null;
    }
}
