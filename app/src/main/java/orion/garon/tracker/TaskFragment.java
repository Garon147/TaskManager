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

    @Bind(R.id.task_state)
    EditText taskState;

    @Bind(R.id.task_progress)
    EditText taskProgress;

    @Bind(R.id.button_create)
    Button createButton;

    @Bind(R.id.content_edit)
    LinearLayout editContent;

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
                Calendar mcurrentDate=Calendar.getInstance();
                DatePickerDialog mDatePicker=new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {

                        date = null;
                        date = new Date(selectedyear, selectedmonth, selectedday);
                        taskStartDate.setText(date.toString());

                    }
                },mcurrentDate.get(Calendar.YEAR), mcurrentDate.get(Calendar.MONTH), mcurrentDate.get(Calendar.DAY_OF_MONTH));
                mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });

        taskDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate=Calendar.getInstance();
                DatePickerDialog mDatePicker=new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {

                        date = null;
                        date = new Date(selectedyear, selectedmonth, selectedday);
                        taskDueDate.setText(date.toString());

                    }
                },mcurrentDate.get(Calendar.YEAR), mcurrentDate.get(Calendar.MONTH), mcurrentDate.get(Calendar.DAY_OF_MONTH));
                mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });
    }

    Date date;

    public void
    saveTask() {

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
