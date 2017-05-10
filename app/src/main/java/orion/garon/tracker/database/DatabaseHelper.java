package orion.garon.tracker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by VKI on 06.05.2017.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "mydb.db";
    private static final int DATABASE_VERSION = 1;

    private TaskDAO taskDAO = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

        try {

            TableUtils.createTable(connectionSource, Task.class);
        } catch (SQLException e) {

            Log.e(TAG, "Error creating DB "+DATABASE_NAME);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

        try {

            TableUtils.dropTable(connectionSource, Task.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            Log.e(TAG, "Error upgrading db " + DATABASE_NAME +" from ver "+DATABASE_VERSION);
            throw new RuntimeException(e);
        }
    }

    public TaskDAO getTaskDAO() throws SQLException {

        if(taskDAO == null) {
            taskDAO = new TaskDAO(getConnectionSource(), Task.class);
        }

        return taskDAO;
    }

    @Override
    public void close() {
        super.close();
        taskDAO = null;
    }
}
