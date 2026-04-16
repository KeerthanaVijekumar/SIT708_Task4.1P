package com.keerthana.eventplannerapp.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.keerthana.eventplannerapp.db.Event;
import com.keerthana.eventplannerapp.db.EventDao;
import com.keerthana.eventplannerapp.db.EventDatabase;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventViewModel extends AndroidViewModel {

    private final EventDao eventDao;
    private final LiveData<List<Event>> allEvents;
    private final ExecutorService executor;

    public EventViewModel(@NonNull Application application) {
        super(application);
        EventDatabase db = EventDatabase.getInstance(application);
        eventDao = db.eventDao();
        allEvents = eventDao.getAllEvents();
        executor = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Event>> getAllEvents() {
        return allEvents;
    }

    public void insert(Event event) {
        executor.execute(() -> eventDao.insert(event));
    }

    public void update(Event event) {
        executor.execute(() -> eventDao.update(event));
    }

    public void delete(Event event) {
        executor.execute(() -> eventDao.delete(event));
    }
}