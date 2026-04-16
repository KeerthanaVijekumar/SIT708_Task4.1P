package com.keerthana.eventplannerapp.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.keerthana.eventplannerapp.R;
import com.keerthana.eventplannerapp.db.Event;
import com.keerthana.eventplannerapp.viewmodel.EventViewModel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddEditEventFragment extends Fragment {

    private TextInputEditText etTitle, etLocation;
    private Spinner spinnerCategory;
    private TextView tvSelectedDateTime;
    private Button btnPickDate, btnSave;
    private EventViewModel viewModel;
    private Calendar selectedCalendar = null;
    private Event existingEvent = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_edit_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etTitle = view.findViewById(R.id.etTitle);
        etLocation = view.findViewById(R.id.etLocation);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);
        tvSelectedDateTime = view.findViewById(R.id.tvSelectedDateTime);
        btnPickDate = view.findViewById(R.id.btnPickDate);
        btnSave = view.findViewById(R.id.btnSave);

        // Setup category spinner
        String[] categories = {"Work", "Social", "Travel", "Health", "Personal", "Other"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                categories
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(spinnerAdapter);

        viewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);

        // Check if editing an existing event
        int eventId = -1;
        if (getArguments() != null) {
            eventId = getArguments().getInt("eventId", -1);
        }

        if (eventId != -1) {
            int finalEventId = eventId;
            viewModel.getAllEvents().observe(getViewLifecycleOwner(), events -> {
                for (Event e : events) {
                    if (e.getId() == finalEventId) {
                        existingEvent = e;
                        populateFields(e);
                        break;
                    }
                }
            });
            // Change button text for edit mode
            btnSave.setText("Update Event");
        }

        // Date & Time picker
        btnPickDate.setOnClickListener(v -> showDateTimePicker(view));

        // Save button
        btnSave.setOnClickListener(v -> saveEvent(view));
    }

    private void populateFields(Event event) {
        etTitle.setText(event.getTitle());
        etLocation.setText(event.getLocation());

        String[] categories = {"Work", "Social", "Travel", "Health", "Personal", "Other"};
        for (int i = 0; i < categories.length; i++) {
            if (categories[i].equals(event.getCategory())) {
                spinnerCategory.setSelection(i);
                break;
            }
        }

        selectedCalendar = Calendar.getInstance();
        selectedCalendar.setTimeInMillis(event.getDateTime());
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy  hh:mm a", Locale.getDefault());
        tvSelectedDateTime.setText(sdf.format(new Date(event.getDateTime())));
    }

    private void showDateTimePicker(View view) {
        Calendar now = Calendar.getInstance();
        new DatePickerDialog(requireContext(), (datePicker, year, month, day) -> {
            new TimePickerDialog(requireContext(), (timePicker, hour, minute) -> {
                Calendar picked = Calendar.getInstance();
                picked.set(year, month, day, hour, minute, 0);
                picked.set(Calendar.MILLISECOND, 0);

                // Validate: no past dates for new events
                if (existingEvent == null && picked.before(Calendar.getInstance())) {
                    Snackbar.make(view, "Cannot pick a date in the past!", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                selectedCalendar = picked;
                SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy  hh:mm a", Locale.getDefault());
                tvSelectedDateTime.setText(sdf.format(picked.getTime()));

            }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false).show();
        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void saveEvent(View view) {
        String title = etTitle.getText() != null ? etTitle.getText().toString().trim() : "";
        String location = etLocation.getText() != null ? etLocation.getText().toString().trim() : "";
        String category = spinnerCategory.getSelectedItem().toString();

        // Validation
        if (title.isEmpty()) {
            Snackbar.make(view, "Title cannot be empty!", Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (selectedCalendar == null) {
            Snackbar.make(view, "Please pick a date and time!", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (existingEvent == null) {
            // Create new event
            Event event = new Event(title, category, location, selectedCalendar.getTimeInMillis());
            viewModel.insert(event);
            Snackbar.make(view, "Event saved!", Snackbar.LENGTH_SHORT).show();
        } else {
            // Update existing event
            existingEvent.setTitle(title);
            existingEvent.setCategory(category);
            existingEvent.setLocation(location);
            existingEvent.setDateTime(selectedCalendar.getTimeInMillis());
            viewModel.update(existingEvent);
            Snackbar.make(view, "Event updated!", Snackbar.LENGTH_SHORT).show();
        }

        // Navigate back to event list
        Navigation.findNavController(view).navigate(R.id.eventListFragment);
    }
}