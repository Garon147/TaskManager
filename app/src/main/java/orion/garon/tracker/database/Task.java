package orion.garon.tracker.database;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by VKI on 06.05.2017.
 */

@DatabaseTable(tableName = "tasks")
public class Task {

    public static final String NAME = "name";
    public static final String COMPLETION = "completion";
    public static final String STATE = "state";
    public static final String ESTIMATED_TIME = "estimated_time";
    public static final String START_DATE = "start_date";
    public static final String DUE_DATE = "due_date";
    public static final String DESCRIPTION = "description";

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(dataType = DataType.STRING, columnName = NAME)
    public String name;

    @DatabaseField(dataType = DataType.INTEGER, columnName = COMPLETION)
    public int completion;

    @DatabaseField(dataType = DataType.STRING, columnName = STATE)
    public String state;

    @DatabaseField(dataType = DataType.FLOAT, columnName = ESTIMATED_TIME)
    public float estimatedTime;

    @DatabaseField(dataType = DataType.STRING, columnName = START_DATE)
    public String startDate;

    @DatabaseField(dataType = DataType.STRING, columnName = DUE_DATE)
    public String dueDate;

    @DatabaseField(dataType = DataType.STRING, columnName = DESCRIPTION)
    public String description;

    public Task() {

    }

    public Task(String name, int completion, String state, float estimatedTime, String startDate,
                String dueDate, String description) {

        this.name = name;
        this.completion = completion;
        this.state = state;
        this.estimatedTime = estimatedTime;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.description = description;
    }
}
