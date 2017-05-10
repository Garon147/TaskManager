package orion.garon.tracker;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import orion.garon.tracker.database.Task;

/**
 * Created by VKI on 06.05.2017.
 */

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.title)
    public TextView title;

    @Bind(R.id.state)
    public TextView state;

    @Bind(R.id.dates)
    public TextView dates;

    @Bind(R.id.progress)
    public ProgressBar progressBar;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setVars(Task task) {

        title.setText(task.name);
        state.setText(task.state);
        dates.setText("Due "+task.dueDate);
        progressBar.setProgress(task.completion);

    }
}
