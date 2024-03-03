package gr313.yanev.lab08;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {

    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE notes (id INT not null unique primary key, title TEXT, content TEXT);";
        db.execSQL(sql);
    }


    // For creating unique id in new Note
    public int getMaxId() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT MAX(id) FROM notes;";

        Cursor cur = db.rawQuery(sql, null);

        if (cur.moveToFirst()) {
            int ind = cur.getInt(0);
            cur.close();
            return ind;
        }

        return 0;
    }

    public ArrayList<Note> getAllNotes() {
        ArrayList<Note> noteList = new ArrayList<Note>();
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT id, title, content FROM notes;";
        Cursor cur = db.rawQuery(sql, null);
        if (cur.moveToFirst()) {
            do {
                Note note = new Note();
                note.id = cur.getInt(0);                    // TODO: Самая важная строка - связь заметок в БД и во вьюшке
                note.title = cur.getString(1);
                note.content = cur.getString(2);
                noteList.add(note);
            } while (cur.moveToNext());

        }
        cur.close();
        return noteList;
    }


    public void insert(String title, String content) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "INSERT INTO notes VALUES (" + (getMaxId()+1) + ", '" + title + "', '" + content + "');";
        db.execSQL(sql);
        // createToastMessage("Успешное добавление заметки в БД!");
    }


    public void update(int id, String title, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE notes SET title = '" + title + "', content = '" + content + "' where id = " + id + ";";
        db.execSQL(query);
        createToastMessage("Успешное обновление заметки в БД!");
    }

    public void delete(int id, String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE from notes WHERE id = " + id + ";";
        db.execSQL(query);
        createToastMessage(String.format("Заметка \"%s\" успешно удалена", title));
    }


    private static void createToastMessage(String message) {
        Toast.makeText(MainActivity.context, message, Toast.LENGTH_SHORT).show();
    }



    // todo: drop all rows in notes

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}
}