package com.basemosama.studentportal.ui.student.result;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.basemosama.studentportal.R;
import com.basemosama.studentportal.databinding.ItemResultBinding;
import com.basemosama.studentportal.model.student.Grades;
import com.basemosama.studentportal.model.student.Subject;
import com.basemosama.studentportal.utility.Constants;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder> {

    private Context context;
    private List<Grades> grades;
    private int gradeId;
    private int departmentId;

    public ResultAdapter(Context context, List<Grades> grades, int gradeId, int departmentId) {
        this.context = context;
        this.grades = grades;
        this.gradeId = gradeId;
        this.departmentId = departmentId;
    }

    public void updateAdapter(List<Grades> newGrades) {
        grades.clear();
        if (newGrades != null) {
            for (int i = 0; i < gradeId; i++) {
                Grades studentGrades = newGrades.get(i);
                for (Subject subject : studentGrades.getSubjects()) {
                    if (subject.getDepartmentId() != departmentId) {
                        studentGrades.getSubjects().remove(subject);
                    }
                }
                grades.add(studentGrades);
            }
            //Sort Results to show last results first
            Collections.sort(grades, new Comparator<Grades>() {
                @Override
                public int compare(Grades firstGrades, Grades secondGrades) {
                    Integer firstGradeId = firstGrades.getId();
                    Integer secondGradeId = secondGrades.getId();
                    return secondGradeId.compareTo(firstGradeId);
                }
            });
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemResultBinding resultRvItemBinding = ItemResultBinding.inflate(inflater, parent, false);
        return new ResultViewHolder(resultRvItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return grades != null ? grades.size() : 0;
    }

    public class ResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView unavailableResultTV;
        private RecyclerView recyclerView;
        private ItemResultBinding itemBinding;
        private TableLayout tableLayout;
        private LinearLayout resultLayout;
        private ImageView resultArrow;

        public ResultViewHolder(@NonNull ItemResultBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            recyclerView = itemBinding.subjectResultRv;
            unavailableResultTV = itemBinding.unavailableTv;
            tableLayout = itemBinding.resultTableLayout;
            resultLayout = itemBinding.resultLayout;
            resultArrow = itemBinding.resultArrow;
            resultArrow.setTag(Constants.CLOSED_ARROW_TAG);
            itemBinding.resultCardView.setOnClickListener(this);

        }

        public void bind(int position) {
            itemBinding.setGrade(grades.get(position));
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            final List<Subject> subjects = grades.get(position).getSubjects();
            SubjectResultAdapter adapter = new SubjectResultAdapter(context, subjects);
            if (subjects == null || subjects.size() == 0) {
                unavailableResultTV.setVisibility(View.VISIBLE);
                tableLayout.setVisibility(View.GONE);
            } else {
                unavailableResultTV.setVisibility(View.GONE);
                tableLayout.setVisibility(View.VISIBLE);
            }
            recyclerView.setAdapter(adapter);
            if (position == 0) {
                resultLayout.setVisibility(View.VISIBLE);
                resultArrow.setImageResource(R.drawable.arrow_up);
                resultArrow.setTag(Constants.OPENED_ARROW_TAG);
            }
        }

        @Override
        public void onClick(View view) {
            String tag = String.valueOf(resultArrow.getTag());
            if (TextUtils.equals(tag, Constants.CLOSED_ARROW_TAG)) {
                resultLayout.setVisibility(View.VISIBLE);
                resultArrow.setImageResource(R.drawable.arrow_up);
                resultArrow.setTag(Constants.OPENED_ARROW_TAG);
            } else {
                resultLayout.setVisibility(View.GONE);
                resultArrow.setImageResource(R.drawable.arrow_down);
                resultArrow.setTag(Constants.CLOSED_ARROW_TAG);
            }
        }
    }
}
