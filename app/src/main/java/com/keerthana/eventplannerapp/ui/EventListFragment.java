package com.keerthana.eventplannerapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import com.keerthana.eventplannerapp.R;
import com.keerthana.eventplannerapp.db.Event;
import com.keerthana.eventplannerapp.viewmodel.EventViewModel;

public class EventListFragment extends Fragment {

    private EventViewModel viewModel;
    private EventAdapter adapter;
    private RecyclerView recyclerView;
    private TextView emptyView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        emptyView = view.findViewById(R.id.emptyView);

        adapter = new EventAdapter(
                new EventAdapter.OnEventClickListener() {
                    @Override
                    public void onEventClick(Event event) {
                        showEventOptionsDialog(event, view);
                    }
                },
                new EventAdapter.OnEventLongClickListener() {
                    @Override
                    public void onEventLongClick(Event event) {
                        showEventOptionsDialog(event, view);
                    }
                }
        );

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        viewModel.getAllEvents().observe(getViewLifecycleOwner(), events -> {
            adapter.setEvents(events);
            if (events.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }
        });
    }

    private void showEventOptionsDialog(Event event, View view) {
        new AlertDialog.Builder(requireContext())
                .setTitle(event.getTitle())
                .setItems(new String[]{"✏️ Edit", "🗑️ Delete"}, (dialog, which) -> {
                    if (which == 0) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("eventId", event.getId());
                        Navigation.findNavController(view)
                                .navigate(R.id.addEditEventFragment, bundle);
                    } else {
                        new AlertDialog.Builder(requireContext())
                                .setTitle("Delete Event")
                                .setMessage("Are you sure you want to delete \"" + event.getTitle() + "\"?")
                                .setPositiveButton("Delete", (d, w) -> {
                                    viewModel.delete(event);
                                    Snackbar.make(view, "Event deleted!", Snackbar.LENGTH_SHORT).show();
                                })
                                .setNegativeButton("Cancel", null)
                                .show();
                    }
                })
                .show();
    }
}