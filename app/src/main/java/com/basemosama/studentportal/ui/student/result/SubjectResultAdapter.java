package com.basemosama.studentportal.ui.student.result;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.basemosama.studentportal.databinding.ItemSubjectResultBinding;
import com.basemosama.studentportal.model.student.Subject;

import java.util.List;

public class SubjectResultAdapter extends RecyclerView.Adapter<SubjectResultAdapter.SubjectResultViewHolder> {

    private Context context;
    private List<Subject> subjectList;
    private ItemSubjectResultBinding resultBinding;

    public SubjectResultAdapter(Context context, List<Subject> subjectList) {
        this.context = context;
        this.subjectList = subjectList;
    }

    public void updateAdapter(List<Subject> newSubjects) {
        subjectList.clear();
        subjectList.addAll(newSubjects);

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SubjectResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        resultBinding = ItemSubjectResultBinding.inflate(inflater, parent, false);
        return new SubjectResultViewHolder(resultBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectResultViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return subjectList != null ? subjectList.size() : 0;
    }

    public class SubjectResultViewHolder extends RecyclerView.ViewHolder {
        private TableRow resultTextRow;


        public SubjectResultViewHolder(@NonNull ItemSubjectResultBinding resultBinding) {
            super(resultBinding.getRoot());
            resultTextRow = resultBinding.resultTextTable;
        }

        public void bind(int position) {
            Subject subject = subjectList.get(position);
            resultBinding.setSubject(subject);
            if (position > 0) {
                resultTextRow.setVisibility(View.INVISIBLE);
            }
            if (subject.getSubjectResults() != null
                    && subject.getSubjectResults().size() > 0)
                resultBinding.setStudentResult(subject.getSubjectResults().get(0));
        }
    }
}
