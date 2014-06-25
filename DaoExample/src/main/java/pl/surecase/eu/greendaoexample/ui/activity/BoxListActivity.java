package pl.surecase.eu.greendaoexample.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;

import greendao.Box;
import greendao.ExerciseSet;
import pl.surecase.co.greendaoexample.daoexample.R;
import pl.surecase.eu.greendaoexample.backend.repositories.BoxRepository;
import pl.surecase.eu.greendaoexample.backend.repositories.ExerciseSetRepository;
import pl.surecase.eu.greendaoexample.ui.adapter.DbItemsAdapter;
import pl.surecase.eu.greendaoexample.ui.utils.CsvParser;
import pl.surecase.eu.greendaoexample.ui.utils.FileUtils;

public class BoxListActivity extends Activity {

    private static final int FILE_SELECT_CODE = 0;
    private ListView lvItemList;

    private DbItemsAdapter boxAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box_list);

        lvItemList = (ListView) this.findViewById(R.id.lvItemList);
        boxAdapter = new DbItemsAdapter(BoxListActivity.this);
        lvItemList.setAdapter(boxAdapter);

        setupButtons();
    }

    private void setupButtons() {
        lvItemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent editItemIntent = new Intent(BoxListActivity.this, EditBoxActivity.class);

                ExerciseSet clickedBox = boxAdapter.getItem(position);
                editItemIntent.putExtra("boxId", clickedBox.getId());

                startActivity(editItemIntent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        boxAdapter.updateData(ExerciseSetRepository.getAllExercises(BoxListActivity.this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.box_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_item:
                createItem();
                return true;

            case R.id.delete_items:
                clearAllItems();
                return true;

            case R.id.upload_items:
                showFileChooser();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    // Get the path
                    String path = FileUtils.getPath(this, uri);
                    // Get the file instance
                    try {
                        List<ExerciseSet> exerciseSets = CsvParser.importFromCsvFile(new File(path));
                        updateList(exerciseSets);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    // File file = new File(path);
                    // Initiate the upload
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateList(List<ExerciseSet> exerciseSets) {
        for(ExerciseSet exerciseSet :exerciseSets)
        {
            ExerciseSetRepository.insertOrUpdate(this, exerciseSet);
        }
        boxAdapter.updateData(exerciseSets);
    }

    private void createItem() {
        Intent addBoxActivityIntent = new Intent(BoxListActivity.this, EditBoxActivity.class);
        startActivity(addBoxActivityIntent);
    }

    private void clearAllItems() {
        if (boxAdapter.getCount() == 0) {
            Toast.makeText(BoxListActivity.this, getString(R.string.toast_no_items_to_delete), Toast.LENGTH_SHORT).show();
        } else {
            new AlertDialog.Builder(BoxListActivity.this)
                    .setTitle(getString(R.string.dialog_delete_items_title))
                    .setMessage(R.string.dialog_delete_items_content)
                    .setCancelable(false)
                    .setPositiveButton(R.string.dialog_delete_items_confirm, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ExerciseSetRepository.clearBoxes(BoxListActivity.this);
                            boxAdapter.updateData(ExerciseSetRepository.getAllExercises(BoxListActivity.this));
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton(R.string.dialog_delete_items_cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).create().show();
        }
    }
}
