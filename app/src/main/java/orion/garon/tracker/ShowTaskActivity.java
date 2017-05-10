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
    private DialogInterface.OnClickListener onClickListener;

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

    Context context = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_task);
        ButterKnife.bind(this);
        presenter = new Presenter(this);

        selectedTask = new Task();
        intent = getIntent();

        initTask();
        initViews();
        groupTextFields();
    }

    @OnClick(R.id.button_create)
    public void onClick(){
        presenter.updateTask(selectedTask, taskName, taskTime, taskStartDate, taskDueDate, taskDescription,
                taskState, taskProgress);
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
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

        taskStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
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

        taskState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog dialog = createDialog();
                dialog.show();
            }
        });

        onClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ListView lv = ((AlertDialog) dialog).getListView();
                if(which == dialog.BUTTON_POSITIVE) {
                    taskState.setText(Status.getAllStates().get(lv.getCheckedItemPosition()));
                }
            }
        };
    }

    public AlertDialog createDialog() {

        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setTitle("States");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.select_dialog_singlechoice, Status.getAllStates());
        adb.setSingleChoiceItems(adapter, -1, onClickListener);
        adb.setPositiveButton("OK", onClickListener);
        adb.setNegativeButton("Cancel", onClickListener);

        return adb.create();
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
}
