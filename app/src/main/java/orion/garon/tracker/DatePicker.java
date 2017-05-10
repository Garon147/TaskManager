package orion.garon.tracker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.Calendar;

/**
 * Created by VKI on 10.05.2017.
 */

public class DatePicker {

    public static void setDatePicker(final Context context, final EditText taskStartDate, final EditText taskDueDate,
                                    final Calendar currentDate) {

        taskStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
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

                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                                taskDueDate.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                            }
                        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
                        currentDate.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
    }

    public static AlertDialog createDialog(Context context, final EditText taskState) {

        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ListView lv = ((AlertDialog) dialog).getListView();
                if(which == dialog.BUTTON_POSITIVE) {
                    taskState.setText(Status.getAllStates().get(lv.getCheckedItemPosition()));
                }
            }
        };

        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setTitle("States");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.select_dialog_singlechoice, Status.getAllStates());
        adb.setSingleChoiceItems(adapter, -1, onClickListener);
        adb.setPositiveButton("OK", onClickListener);
        adb.setNegativeButton("Cancel", onClickListener);

        return adb.create();
    }


}
