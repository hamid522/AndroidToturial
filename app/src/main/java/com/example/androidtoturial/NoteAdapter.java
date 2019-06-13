package com.example.androidtoturial;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.RenderProcessGoneDetail;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private List<NoteModel> noteModels;
    Context context;
    MyDatabase myDatabase;

    public NoteAdapter(Context context, List<NoteModel> noteModels) {
        this.noteModels = noteModels;
        this.context = context;
        this.myDatabase = new MyDatabase(context);

    }

    @NonNull
    @Override
    // this method convert layout to java code
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.noterow, viewGroup, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NoteViewHolder noteViewHolder, final int position) {
        final NoteModel model = noteModels.get(position);

        noteViewHolder.txtTitle.setText(model.getTitle());
        noteViewHolder.txtDesc.setText(model.getDesc());
        noteViewHolder.txtTime.setText(model.getTime());

        if (model.getRemember()==0)
        {
            noteViewHolder.img_clock.setVisibility(View.GONE);
            noteViewHolder.txtTime.setVisibility(View.GONE);
        }

        noteViewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog);
                TextView txtYes = dialog.findViewById(R.id.txt_dialog_yes);
                TextView txtNo = dialog.findViewById(R.id.txt_dialog_no);
                txtYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDatabase.deleteRow(model.getId());
                        noteModels.remove(model);
                        notifyItemRemoved(position);
                        dialog.dismiss();
                    }
                });
                txtNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });
        noteViewHolder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.edit_dialog);
                final EditText edtTitle =dialog.findViewById(R.id.edt_editDialog_title);
                final EditText edtDesc =dialog.findViewById(R.id.edt_editDialog_desc);
                Button btnUpdate = dialog.findViewById(R.id.btn_editDialog_update);
                Button btnCancel = dialog.findViewById(R.id.btn_editDialog_cancel);
                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDatabase.updateRow(model.getId(),edtTitle.getText().toString(),edtDesc.getText().toString());
                        dialog.dismiss();
                        NoteModel noteModel = new NoteModel();
                        noteModel.setTitle(edtTitle.getText().toString());
                        noteModel.setDesc(edtDesc.getText().toString());
                        noteModel.setTime(model.getTime());
                        noteModels.add(position,noteModel);
                        notifyItemChanged(position);
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return noteModels.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtDesc, txtTime;
        ImageView imgEdit, imgDelete,img_clock;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_noteRow_title);
            txtDesc = itemView.findViewById(R.id.txt_noteRow_desc);
            txtTime = itemView.findViewById(R.id.txt_noteRow_time);
            imgEdit = itemView.findViewById(R.id.img_noteRow_edit);
            imgDelete = itemView.findViewById(R.id.img_noteRow_delete);
            img_clock = itemView.findViewById(R.id.img_noteRow_clock);
        }
    }

}
