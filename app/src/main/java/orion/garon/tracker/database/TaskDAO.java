package orion.garon.tracker.database;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by VKI on 06.05.2017.
 */

public class TaskDAO extends BaseDaoImpl<Task, Integer> {

    protected TaskDAO(ConnectionSource connectionSource, Class<Task> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<Task> getAllTasks() throws SQLException {
        return this.queryForAll();
    }

    public Task getTaskById(int id) throws SQLException {
        return this.queryForId(id);
    }
}
