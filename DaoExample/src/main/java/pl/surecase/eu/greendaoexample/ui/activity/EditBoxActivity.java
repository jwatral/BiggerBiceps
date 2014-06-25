package pl.surecase.eu.greendaoexample.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import greendao.Box;
import greendao.ExerciseSet;
import pl.surecase.co.greendaoexample.daoexample.R;
import pl.surecase.eu.greendaoexample.backend.repositories.BoxRepository;
import pl.surecase.eu.greendaoexample.backend.repositories.ExerciseSetRepository;


public class EditBoxActivity extends Activity {

    private Button btnSave;
    private EditText etBoxName;
    private EditText etBoxSlots;
    private EditText etBoxDescription;

    private long boxId;
    private ExerciseSet box;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_box);
        btnSave = (Button) findViewById(R.id.btnSave);
        etBoxName = (EditText) findViewById(R.id.etBoxName);
        etBoxSlots = (EditText) findViewById(R.id.etBoxSlots);
        etBoxDescription = (EditText) findViewById(R.id.etBoxDescription);

        if (getIntent() != null && getIntent().getExtras() != null) {
            boxId = getIntent().getExtras().getLong("boxId");
            box = ExerciseSetRepository.getExerciseForId(EditBoxActivity.this, boxId);
        }

        setupButtons();
        fillViewWithData();
    }

    private void setupButtons() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    if (box == null) {
                        box = new ExerciseSet();
                    } else {
                        box.setId(boxId);
                    }
                    box.setExercise(etBoxName.getText().toString());
                    box.setReps(Integer.parseInt(etBoxSlots.getText().toString()));
                    box.setMuscle(etBoxDescription.getText().toString());
                    ExerciseSetRepository.insertOrUpdate(EditBoxActivity.this, box);
                    finish();
                } else {
                    Toast.makeText(EditBoxActivity.this, getString(R.string.toast_validation_error), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateFields() {
        if (etBoxName.getText().length() == 0) {
            etBoxName.setError(getString(R.string.error_cannot_be_empty));
            return false;
        }
        if (etBoxSlots.getText().length() == 0) {
            etBoxSlots.setError(getString(R.string.error_cannot_be_empty));
            return false;
        }
        try {
            Integer.parseInt(etBoxSlots.getText().toString());
        } catch (Exception e) {
            etBoxSlots.setError(getString(R.string.error_must_be_number));
            return false;
        }

        etBoxName.setError(null);
        etBoxSlots.setError(null);
        return  true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (box != null) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.edit_item_menu, menu);
            return true;
        } else {
            return super.onCreateOptionsMenu(menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_item:
                deleteItem();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteItem() {
        new AlertDialog.Builder(EditBoxActivity.this)
                .setTitle(getString(R.string.dialog_delete_item_title))
                .setMessage(R.string.dialog_delete_item_content)
                .setCancelable(false)
                .setPositiveButton(R.string.dialog_delete_items_confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ExerciseSetRepository.deleteExerciseWithId(EditBoxActivity.this, boxId);
                        dialog.cancel();
                        finish();
                    }
                })
                .setNegativeButton(R.string.dialog_delete_items_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).create().show();
    }

    private void fillViewWithData() {
        if (box != null) {
            etBoxName.setText(box.getExercise());
            etBoxSlots.setText(box.getReps().toString());
            etBoxDescription.setText(box.getMuscle());
        }
    }

}
