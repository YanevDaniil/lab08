package gr313.yanev.lab08;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ArrayAdapter<Note> adp;
    static Database db;
    ListView notesView;
    @SuppressLint("StaticFieldLeak")
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adp = new ArrayAdapter<Note>(this, android.R.layout.simple_list_item_1);
        db = new Database(this, "my_notes.db", null, 1);

        context = this.getApplicationContext();

        notesView = findViewById(R.id.notesView);
        notesView.setAdapter(adp);

        updateAdp();

        notesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onEditClick(position);
            }
        });
    }

    private void onEditClick(int position) {
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra("isForEdit", true);


        Note currNote = adp.getItem(position);
        int noteId = currNote.id;
        String noteTitle = currNote.title;
        String noteContent = currNote.content;

        intent.putExtra("noteId", noteId);
        intent.putExtra("noteTitle", noteTitle);
        intent.putExtra("noteContent", noteContent);

        startActivityForResult(intent, 222);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.updateAdp();
    }


    private void updateAdp() {
        adp.clear();
        adp.addAll(db.getAllNotes());
        adp.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.newItem) {
            onNewClick();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onNewClick() {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivityForResult(intent, 2);
    }


    private void createToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}