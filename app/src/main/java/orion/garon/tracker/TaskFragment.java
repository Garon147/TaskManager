package orion.garon.tracker;

import android.app.DatePickerDialog;
import android.content.Intent;
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

    private Presenter presenter;

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
        presenter = new Presenter(getContext());
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentDate = Calendar.getInstance();
        orion.garon.tracker.DatePicker.setDatePicker(getContext(), taskStartDate, taskDueDate, currentDate);
        orion.garon.tracker.DatePicker.setCallback(taskTime);
    }

    @OnClick(R.id.button_create)
    public void onClick() {

        if(checkInput()) {

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    presenter.saveTask(taskName, taskTime, taskStartDate, taskDueDate, taskDescription);
                }
            });
            thread.start();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    public boolean checkInput() {

        if(taskName.getText().toString().isEmpty() || taskTime.getText().toString().isEmpty() ||
                taskStartDate.getText().toString().isEmpty() || taskDueDate.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), R.string.input_error_msg, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

}
