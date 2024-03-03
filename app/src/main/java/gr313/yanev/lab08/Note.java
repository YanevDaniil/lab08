package gr313.yanev.lab08;

import androidx.annotation.NonNull;

public class Note {
    public int id;
    public String title;
    public String content;

    @NonNull
    public String toString(){
        return title;
    }
}