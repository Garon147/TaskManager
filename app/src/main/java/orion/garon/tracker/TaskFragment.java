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

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                presenter.saveTask(taskName, taskTime, taskStartDate, taskDueDate, taskDescription);
                Toast.makeText(getContext(), "SAVE SUCCESSFUL", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });


        taskStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

}
