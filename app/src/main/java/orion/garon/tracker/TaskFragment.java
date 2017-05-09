package orion.garon.tracker;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.j256.ormlite.misc.TransactionManager;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.concurrent.Callable;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import orion.garon.tracker.database.HelperFactory;
import orion.garon.tracker.database.Task;
import orion.garon.tracker.database.TaskDAO;

/**
 * Created by VKI on 07.05.2017.
 */

public class TaskFragment extends Fragment {

    public Calendar currentDate;

    @Bind(R.id.task_name)
    EditText taskName;

    @Bind(R.id.task_time)
    EditText taskTime;

    @Bind(R.id.task_start)
    EditText taskStartDate;

    @Bind(R.id.task_due)
    EditText taskDueDate;

    @Bind(R.id.task_description)
    EditText taskDescription;

    @Bind(R.id.button_create)
    Button createButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_task, container, false);
        ButterKnife.bind(this, view);
        HelperFactory.setDatabaseHelper(getContext());

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                saveTask();
                Toast.makeText(getContext(), "SAVE SUCCESSFUL", Toast.LENGTH_SHORT).show();
            }
        });


        taskStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                taskStartDate.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                            }
                        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
                        currentDate.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        taskDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                taskDueDate.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                            }
                        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
                        currentDate.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
    }

    public void saveTask() {

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
}
