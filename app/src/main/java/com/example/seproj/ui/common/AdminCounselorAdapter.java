package com.example.seproj.ui.common;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seproj.R;
import com.example.seproj.model.Counselor;
import com.example.seproj.ui.admin.AdminCounselorFeedbackListActivity;

import java.util.ArrayList;
import java.util.List;
import com.example.seproj.ui.admin.AdminCounselorFeedbackListActivity;

/**
 * RecyclerView adapter for counselor profiles shown in admin management.
 * Binds counselor identity, specialization, and delete actions into list rows.
 *
 * Outstanding issues:
 * - Inline edit actions are not included in each row yet.
 */
public class AdminCounselorAdapter extends RecyclerView.Adapter<AdminCounselorAdapter.CounselorViewHolder> {

    public interface OnDeleteClickListener {
        void onDelete(Counselor counselor);
    }

    private final List<Counselor> counselors = new ArrayList<>();
    private final OnDeleteClickListener deleteListener;

    public AdminCounselorAdapter(OnDeleteClickListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    public void setCounselors(List<Counselor> counselorList) {
        counselors.clear();

        if (counselorList != null) {
            counselors.addAll(counselorList);
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CounselorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_counselor, parent, false);

        return new CounselorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CounselorViewHolder holder, int position) {
        Counselor counselor = counselors.get(position);
        holder.bind(counselor, deleteListener);
    }

    @Override
    public int getItemCount() {
        return counselors.size();
    }

    static class CounselorViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvName;
        private final TextView tvEmail;
        private final TextView tvSpecialty;
        private final Button btnDelete;

        public CounselorViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvAdminCounselorName);
            tvEmail = itemView.findViewById(R.id.tvAdminCounselorEmail);
            tvSpecialty = itemView.findViewById(R.id.tvAdminCounselorSpecialty);
            btnDelete = itemView.findViewById(R.id.btnDeleteCounselor);
        }

        void bind(Counselor counselor, OnDeleteClickListener listener) {
            tvName.setText(counselor.getName());
            tvEmail.setText(counselor.getEmail());
            tvSpecialty.setText(counselor.getSpecialization());

            btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDelete(counselor);
                }
            });
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), AdminCounselorFeedbackListActivity.class);
                intent.putExtra("counselorId", counselor.getCounselorId());
                intent.putExtra("counselorName", counselor.getName());
                itemView.getContext().startActivity(intent);
            });
        }
    }
}


