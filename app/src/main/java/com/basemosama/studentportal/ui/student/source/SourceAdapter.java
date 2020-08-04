package com.basemosama.studentportal.ui.student.source;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.basemosama.studentportal.databinding.ItemSourcesBinding;
import com.basemosama.studentportal.model.student.Source;

import java.util.List;

public class SourceAdapter extends RecyclerView.Adapter<SourceAdapter.SourceViewHolder> {

    private Context context;
    private List<Source> sources;
    private SourceClickListener sourceClickListener;

    public SourceAdapter(Context context, List<Source> sources, SourceClickListener sourceClickListener) {
        this.context = context;
        this.sources = sources;
        this.sourceClickListener = sourceClickListener;
    }

    @NonNull
    @Override
    public SourceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ItemSourcesBinding itemBinding = ItemSourcesBinding.inflate(layoutInflater, parent, false);
        return new SourceViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SourceViewHolder holder, int position) {
        Source source = sources.get(position);
        holder.bind(source);
    }

    @Override
    public int getItemCount() {
        return sources != null ? sources.size() : 0;
    }

    public void updateAdapter(List<Source> newSources) {
        sources.clear();
        sources.addAll(newSources);
        notifyDataSetChanged();
    }


    public interface SourceClickListener {
        void onSourceClickListener(Source source);
    }

    class SourceViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener {
        ItemSourcesBinding itemBinding;

        SourceViewHolder(@NonNull ItemSourcesBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            itemBinding.getRoot().setOnClickListener(this);
        }

        private void bind(Source source) {
            itemBinding.setSource(source);
            itemBinding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            sourceClickListener.onSourceClickListener(sources.get(getAdapterPosition()));
        }
    }


}
