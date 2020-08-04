package com.basemosama.studentportal.ui.professor.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.basemosama.studentportal.databinding.ItemProjectBinding;
import com.basemosama.studentportal.model.professor.ProfessorProject;

import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {
    private Context context;
    private List<ProfessorProject> projects;

    public ProjectAdapter(Context context, List<ProfessorProject> projects) {
        this.context = context;
        this.projects = projects;
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemProjectBinding projectsBinding = ItemProjectBinding.inflate(inflater, parent, false);
        return new ProjectViewHolder(projectsBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return projects == null ? 0 : projects.size();
    }

    public void updateAdapter(List<ProfessorProject> newProjects) {
        projects.clear();
        projects.addAll(newProjects);
        notifyDataSetChanged();
    }

    public class ProjectViewHolder extends RecyclerView.ViewHolder {
        private ItemProjectBinding projectsBinding;

        public ProjectViewHolder(@NonNull ItemProjectBinding ItemProjectBinding) {
            super(ItemProjectBinding.getRoot());
            projectsBinding = ItemProjectBinding;
        }

        private void bind(int position) {
            ProfessorProject project = projects.get(position);
            projectsBinding.setProject(project);
            if (position == getItemCount() - 1) {
                projectsBinding.profileProjectBorder.setVisibility(View.GONE);
            }
        }
    }
}
