package gr313.yanev.lab08;

import static gr313.yanev.lab08.MainActivity.db;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    EditText title;
    EditText content;
    boolean isForEdit;
    Intent mainIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        title = findViewById(R.id.titleField);
        content = findViewById(R.id.contentField);

        // ------------------------------------------
        title.setInputType(InputType.TYPE_CLASS_TEXT);
        content.setInputType(InputType.TYPE_CLASS_TEXT);
        // ------------------------------------------

        mainIntent = this.getIntent();
        isForEdit = mainIntent.getBooleanExtra("isForEdit", false);

        if (isForEdit) {
            fillFields();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_menu, menu);
        return true;
    }


    public void onSaveIconClick() {
        if (isForEdit) {
            editNote();
        } else {
            createNewNote();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.saveItem) {
            onSaveIconClick();
        } else {
            onDeleteIconClick();
        }

        return super.onOptionsItemSelected(item);
    }


    public void onDeleteIconClick() {
        if (isForEdit) {
            // Метод для удаления (редактируемой) существующей заметки
            deleteNote();
        } else {
            // При клике на иконку при ТОЛЬКО создании новой
            // createToastMessage("Это только создание");
            finish();
        }
    }

    private void deleteNote() {

        //------------------------------------------------
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Вы уверены в удалении?")
                .setMessage("Заметку будет невозможно восстановить")

                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        int noteId = mainIntent.getIntExtra("noteId", -1);
                        String noteTitle = title.getText().toString();

                        db.delete(noteId, noteTitle);
                        finish();
                    }
                })

                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Ничего
                    }
                })
                .show();
        //------------------------------------------------

    }


    public void fillFields() {
        String noteTitle = mainIntent.getStringExtra("noteTitle");
        String noteContent = mainIntent.getStringExtra("noteContent");

        title.setText(noteTitle);
        content.setText(noteContent);
    }

    public void editNote() {

        String titleStr = title.getText().toString();
        String contentStr = content.getText().toString();


        String noteTitle = mainIntent.getStringExtra("noteTitle");
        String noteContent = mainIntent.getStringExtra("noteContent");

        if (titleStr.isEmpty() || contentStr.isEmpty()) {
            createToastMessage("Заполните обя поля!");
            return;
        }


        if (titleStr.equals(noteTitle) && contentStr.equals(noteContent)) {
            createToastMessage("Вы не обновили заметку");
            finish();
        } else {

            int noteId = mainIntent.getIntExtra("noteId", -1);

            db.update(noteId, titleStr, contentStr);
            finish();
        }

    }

    public void createNewNote() {

        String titleStr = title.getText().toString();
        String contentStr = content.getText().toString();

        if (titleStr.isEmpty() || contentStr.isEmpty()) {
            createToastMessage("Заполните обя поля!");
            return;
        }


        db.insert(titleStr, contentStr);
        finish();
    }


    private static void createToastMessage(String message) {
        Toast.makeText(MainActivity.context, message, Toast.LENGTH_SHORT).show();
    }
}