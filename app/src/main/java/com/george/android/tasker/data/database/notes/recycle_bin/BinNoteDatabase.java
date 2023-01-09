package com.george.android.tasker.data.database.notes.recycle_bin;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.george.android.tasker.data.model.BinNote;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {BinNote.class}, version = 1)
public abstract class BinNoteDatabase extends RoomDatabase {

    private static BinNoteDatabase instance;
    public abstract BinNoteDao noteBinDao();

    public static synchronized BinNoteDatabase getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    BinNoteDatabase.class, "bin_note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(() -> instance.noteBinDao());
        }
    };

}
