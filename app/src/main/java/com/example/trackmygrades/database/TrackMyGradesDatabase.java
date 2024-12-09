package com.example.trackmygrades.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.trackmygrades.activities.MainActivity;
import com.example.trackmygrades.database.entities.Assessment;
import com.example.trackmygrades.database.entities.Grade;
import com.example.trackmygrades.database.entities.User;
import com.example.trackmygrades.database.typeConverter.LocalDateTypeConverter;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@TypeConverters(LocalDateTypeConverter.class)
@Database(entities = {Assessment.class, Grade.class, User.class}, version = 1, exportSchema = false)
public abstract class TrackMyGradesDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "GradeTrackerDatabase";
    public static final String USER_TABLE = "userTable";
    public static final String ASSESSMENT_TABLE = "assessmentTable";
    public static final String GRADE_TABLE = "gradeTable";

    private static volatile TrackMyGradesDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static TrackMyGradesDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (TrackMyGradesDatabase.class){
                if(INSTANCE == null){
                    context.deleteDatabase(DATABASE_NAME); // For testing, deletes the database file

                    Log.d(MainActivity.TAG, "Initializing database...");
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    TrackMyGradesDatabase.class,
                                    DATABASE_NAME
                            )
                            .fallbackToDestructiveMigration()
                            .addCallback(addDefaultValues)
                            .build();
                    Log.d(MainActivity.TAG, "Database Builder completed");
                }

            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback addDefaultValues = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Log.i(MainActivity.TAG, "DATABASE CREATED!");
            databaseWriteExecutor.execute(() -> {
                UserDAO userDao = INSTANCE.userDAO();
                userDao.deleteALL();
                userDao.insert(new User( "teacher1", "teacher1@gmail.com", true, "teacher1", 1));
                userDao.insert(new User( "student1", "student1@gmail.com", false, "student1", 2));

                AssessmentDAO assessmentDao = INSTANCE.assessmentDAO();
                assessmentDao.deleteALL();
                assessmentDao.insert(new Assessment(1, LocalDateTime.of(2024, 10, 10, 0, 0, 0), 1, "Math Quiz"));

                GradeDAO gradeDao = INSTANCE.gradeDAO();
                gradeDao.deleteALL();
                gradeDao.insert(new Grade(1, "well done!", 4.0,1, 2));

            });
        }
    };

    public abstract UserDAO userDAO();
    public abstract AssessmentDAO assessmentDAO();
    public abstract GradeDAO gradeDAO();

}
