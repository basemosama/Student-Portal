package com.basemosama.studentportal.ui.student.assignments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.basemosama.studentportal.databinding.ItemAssignmentBinding;
import com.basemosama.studentportal.model.student.Assignment;

import java.util.List;

public class AssignmentsAdapter extends RecyclerView.Adapter<AssignmentsAdapter.AssignmentsViewHolder> {

    private Context context;
    private List<Assignment> assignments;
    private AssignmentClickListener assignmentClickListener;

    public AssignmentsAdapter(Context context, List<Assignment> assignments, AssignmentClickListener assignmentClickListener) {
        this.context = context;
        this.assignments = assignments;
        this.assignmentClickListener = assignmentClickListener;
    }

    @NonNull
    @Override
    public AssignmentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ItemAssignmentBinding itemBinding = ItemAssignmentBinding.inflate(layoutInflater, parent, false);
        return new AssignmentsViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignmentsViewHolder holder, int position) {
        Assignment assignment = assignments.get(position);
        holder.bind(assignment);
    }

    @Override
    public int getItemCount() {
        return assignments != null ? assignments.size() : 0;
    }

    public void updateAdapter(List<Assignment> newAssignments) {
        assignments.clear();
        assignments.addAll(newAssignments);
        notifyDataSetChanged();
    }


    public interface AssignmentClickListener {
        void onAssignmentClickListener(Assignment data);
    }

    class AssignmentsViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener {
        ItemAssignmentBinding itemBinding;

        AssignmentsViewHolder(@NonNull ItemAssignmentBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            itemBinding.getRoot().setOnClickListener(this);
        }

        private void bind(Assignment assignment) {
            itemBinding.setAssignment(assignment);
            itemBinding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            assignmentClickListener.onAssignmentClickListener(assignments.get(getAdapterPosition()));
        }
    }


}
