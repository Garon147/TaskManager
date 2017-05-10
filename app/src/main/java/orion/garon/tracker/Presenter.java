package orion.garon.tracker;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.j256.ormlite.misc.TransactionManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import orion.garon.tracker.database.HelperFactory;
import orion.garon.tracker.database.Task;

/**
 * Created by VKI on 10.05.2017.
 */

public class Presenter extends AppCompatActivity {

    public ArrayList<Task> tasks;

    public Presenter(Context context) {
        HelperFactory.setDatabaseHelper(context);

        try {
            this.tasks = (ArrayList) ((HelperFactory.getDatabaseHelper().getTaskDAO())).getAllTasks();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveTask(final EditText taskName, final EditText taskTime, final EditText taskStartDate,
                         final EditText taskDueDate, final EditText taskDescription) {

        try {

            TransactionManager.callInTransaction(HelperFactory.getDatabaseHelper().getConnectionSource(), new Callable<Void>() {

                @Override
                public Void call() throws Exception {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {

                                Task task = new Task(taskName.getText().toString(), 0, Status.NEW.toString(),
                                        Float.parseFloat(taskTime.getText().toString()),
                                        taskStartDate.getText().toString(),
                                        taskDueDate.getText().toString(),
                                        taskDescription.getText().toString());
                                HelperFactory.getDatabaseHelper().getTaskDAO().createOrUpdate(task);


                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    return null;
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTask(final Task selectedTask, final EditText taskName, final EditText taskTime,
                           final EditText taskStartDate, final EditText taskDueDate,
                           final EditText taskDescription, final EditText taskState,
                           final EditText taskProgress) {

        try {

            TransactionManager.callInTransaction(HelperFactory.getDatabaseHelper().getConnectionSource(), new Callable<Void>() {

                @Override
                public Void call() throws Exception {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {

                                Task task = HelperFactory.getDatabaseHelper().getTaskDAO().getTaskById(selectedTask.id);

                                if(task != null) {
                                    task.state = taskState.getText().toString();
                                    task.estimatedTime = Float.valueOf(taskTime.getText().toString());
                                    task.completion = Integer.valueOf(taskProgress.getText().toString());
                                    task.name = taskName.getText().toString();
                                    task.description = taskDescription.getText().toString();
                                    task.startDate = taskStartDate.getText().toString();
                                    task.dueDate = taskDueDate.getText().toString();

                                    HelperFactory.getDatabaseHelper().getTaskDAO().update(task);
                                }

                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    return null;
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
