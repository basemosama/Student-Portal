package com.basemosama.studentportal.ui.professor.year_work;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.basemosama.studentportal.databinding.ItemStudentNamesBinding;
import com.basemosama.studentportal.model.student.User;

import java.util.List;

public class StudentNamesAdapter extends RecyclerView.Adapter<StudentNamesAdapter.StudentNamesViewHolder> {
    private Context context;
    private List<User> students;
    private StudentClickListener studentClickListener;

    public StudentNamesAdapter(Context context, List<User> students, StudentClickListener studentClickListener) {
        this.context = context;
        this.students = students;
        this.studentClickListener = studentClickListener;
    }

    @NonNull
    @Override
    public StudentNamesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemStudentNamesBinding studentNamesBinding = ItemStudentNamesBinding.inflate(inflater, parent, false);
        return new StudentNamesViewHolder(studentNamesBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentNamesViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return students != null ? students.size() : 0;
    }

    public void updateAdapter(List<User> studentList) {
        students.clear();
        students.addAll(studentList);
        notifyDataSetChanged();
    }

    public interface StudentClickListener {
        void OnStudentClickListener(User student, View view);
    }

    public class StudentNamesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ItemStudentNamesBinding studentNamesBinding;

        public StudentNamesViewHolder(@NonNull ItemStudentNamesBinding itemStudentNamesBinding) {
            super(itemStudentNamesBinding.getRoot());
            studentNamesBinding = itemStudentNamesBinding;
            studentNamesBinding.getRoot().setOnClickListener(this);
        }

        public void bind(int position) {
            studentNamesBinding.setStudent(students.get(position));
        }

        @Override
        public void onClick(View view) {
            User student = students.get(getAdapterPosition());
            if (student != null) {
                studentClickListener.OnStudentClickListener(students.get(getAdapterPosition()), view);
            }
        }
    }
}
