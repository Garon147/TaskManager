package orion.garon.tracker;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.KeyListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.misc.TransactionManager;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import orion.garon.tracker.database.HelperFactory;
import orion.garon.tracker.database.Task;

/**
 * Created by VKI on 09.05.2017.
 */

public class ShowTaskActivity extends AppCompatActivity {

    private Task selectedTask;
    private Intent intent;
    private List<EditText> textFields;
    private Calendar currentDate;
    private Presenter presenter;
    private Context context;

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

    @Bind(R.id.task_progress)
    EditText taskProgress;

    @Bind(R.id.task_state)
    EditText taskState;

    @Bind(R.id.content_edit)
    LinearLayout contentEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_task);
        ButterKnife.bind(this);
        presenter = new Presenter(this);

        selectedTask = new Task();
        intent = getIntent();
        context = this;
        currentDate = Calendar.getInstance();

        initTask();
        initViews();
        setListeners();
        groupTextFields();
    }

    @OnClick(R.id.button_create)
    public void onClick(){

        if(checkInput() && checkProgress()) {

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    presenter.updateTask(selectedTask, taskName, taskTime, taskStartDate, taskDueDate, taskDescription,
                            taskState, taskProgress);
                }
            });
            thread.start();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }

    public void initViews() {

        contentEdit.setVisibility(View.VISIBLE);
        createButton.setText(R.string.save_changes_button);
        createButton.setEnabled(false);

        taskName.setText(selectedTask.name);
        taskTime.setText(String.valueOf(selectedTask.estimatedTime));
        taskDescription.setText(selectedTask.description);
        taskDueDate.setText(selectedTask.dueDate);
        taskStartDate.setText(selectedTask.startDate);
        taskProgress.setText(String.valueOf(selectedTask.completion));
        taskState.setText(selectedTask.state);

        orion.garon.tracker.DatePicker.setCallback(taskTime);
        taskProgress.setLongClickable(false);
        taskState.setLongClickable(false);
    }

    public void initTask() {

        selectedTask.id = intent.getIntExtra(Task.ID, 0);
        selectedTask.name = intent.getStringExtra(Task.NAME);
        selectedTask.dueDate = intent.getStringExtra(Task.DUE_DATE);
        selectedTask.startDate = intent.getStringExtra(Task.START_DATE);
        selectedTask.description = intent.getStringExtra(Task.DESCRIPTION);
        selectedTask.completion = intent.getIntExtra(Task.COMPLETION, 0);
        selectedTask.estimatedTime = intent.getFloatExtra(Task.ESTIMATED_TIME, 0);
        selectedTask.state = intent.getStringExtra(Task.STATE);
    }

    public void groupTextFields() {

        textFields = new ArrayList<>();

        textFields.add(taskName);
        textFields.add(taskTime);
        textFields.add(taskDescription);
        textFields.add(taskDueDate);
        textFields.add(taskStartDate);
        textFields.add(taskState);
        textFields.add(taskProgress);

        for(int i = 0;i < textFields.size();i++) {
            textFields.get(i).setEnabled(false);
        }
    }

    public void changeEdit(boolean buttonIsChecked) {

        for(int i = 0;i < textFields.size();i++) {

            if(buttonIsChecked) {

                textFields.get(i).setEnabled(true);
                createButton.setEnabled(true);

            } else {

                textFields.get(i).setEnabled(false);
                createButton.setEnabled(false);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_show_task, menu);

        final MenuItem toggleservice = menu.findItem(R.id.toggle_test);
        final Switch actionView = (Switch) toggleservice.getActionView();
        actionView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                changeEdit(isChecked);
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void setListeners() {

        orion.garon.tracker.DatePicker.setDatePicker(context, taskStartDate, taskDueDate, currentDate);
        taskState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog dialog = orion.garon.tracker.DatePicker.createDialog(context, taskState);
                dialog.show();
            }
        });
    }

    public boolean checkInput() {

        if(taskName.getText().toString().isEmpty() || taskTime.getText().toString().isEmpty() ||
                taskProgress.getText().toString().isEmpty()) {
            Toast.makeText(context, R.string.input_error_msg, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public boolean checkProgress() {

        if(Integer.valueOf(taskProgress.getText().toString()) > 100) {
            Toast.makeText(context, R.string.progress_error_msg, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}
