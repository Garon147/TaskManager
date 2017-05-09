package orion.garon.tracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import orion.garon.tracker.database.Task;

/**
 * Created by VKI on 09.05.2017.
 */

public class ShowTaskActivity extends AppCompatActivity {

    private Task selectedTask;
    private Intent intent;

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

        selectedTask = new Task();
        intent = getIntent();

        initTask();
        initViews();



    }

    public void initViews() {

        contentEdit.setVisibility(View.VISIBLE);
        createButton.setText("Save changes");
        createButton.setClickable(false);

        taskName.setText(selectedTask.name);
        taskTime.setText(String.valueOf(selectedTask.estimatedTime));
        taskDescription.setText(selectedTask.description);
        taskDueDate.setText(selectedTask.dueDate);
        taskStartDate.setText(selectedTask.startDate);
        taskProgress.setText(String.valueOf(selectedTask.completion));
        taskState.setText(selectedTask.state);

        taskName.setKeyListener(null);

    }

    public void initTask() {

        selectedTask.name = intent.getStringExtra(Task.NAME);
        selectedTask.dueDate = intent.getStringExtra(Task.DUE_DATE);
        selectedTask.startDate = intent.getStringExtra(Task.START_DATE);
        selectedTask.description = intent.getStringExtra(Task.DESCRIPTION);
        selectedTask.completion = intent.getIntExtra(Task.COMPLETION, 0);
        selectedTask.estimatedTime = intent.getFloatExtra(Task.ESTIMATED_TIME, 0);
        selectedTask.state = intent.getStringExtra(Task.STATE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_show_task, menu);

        final MenuItem toggleservice = menu.findItem(R.id.toggle_test);
        final Switch actionView = (Switch) toggleservice.getActionView();
        actionView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Start or stop your Service
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
