package orion.garon.tracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import orion.garon.tracker.database.Task;
import orion.garon.tracker.recycler.RecyclerAdapter;
import orion.garon.tracker.recycler.RecyclerItemClickListener;

public class MainActivity extends AppCompatActivity {


    @Bind(R.id.recyclerView)
    public RecyclerView recyclerView;

    @Bind(R.id.fab)
    public FloatingActionButton fab;

    @Bind(R.id.toolbar)
    public Toolbar toolbar;

    private LinearLayoutManager layoutManager;
    private RecyclerAdapter recyclerAdapter;
    private Presenter presenter;

    private TaskFragment taskFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        presenter = new Presenter(this);
        setSupportActionBar(toolbar);

        taskFragment = new TaskFragment();
        fragmentManager = getSupportFragmentManager();

        initViews();
    }

    public void initViews() {

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerAdapter = new RecyclerAdapter();
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.addAll(presenter.tasks);
        recyclerAdapter.refreshData(presenter.tasks);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent = new Intent(getApplicationContext(), ShowTaskActivity.class);
                makeIntent(presenter.tasks.get(position), intent);
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }


    @Override
    public void onBackPressed() {

        if(getSupportFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getSupportFragmentManager().popBackStack();
            fab.setVisibility(View.VISIBLE);
        }

        getSupportActionBar().setTitle(R.string.app_name);
    }

    public void makeIntent(Task task, Intent intent) {

        intent.putExtra(Task.ID, task.id);
        intent.putExtra(Task.NAME, task.name);
        intent.putExtra(Task.COMPLETION, task.completion);
        intent.putExtra(Task.DESCRIPTION, task.description);
        intent.putExtra(Task.DUE_DATE, task.dueDate);
        intent.putExtra(Task.START_DATE, task.startDate);
        intent.putExtra(Task.ESTIMATED_TIME, task.estimatedTime);
        intent.putExtra(Task.STATE, task.state);
    }

    @OnClick(R.id.fab)
    public void onClick() {
        fragmentManager.
                beginTransaction().
                add(R.id.frame_layout, taskFragment).
                addToBackStack(null).
                commit();
        toolbar.setTitle(R.string.create_task_toolbar);
        fab.setVisibility(View.GONE);
    }
}
