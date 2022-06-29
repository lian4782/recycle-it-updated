package com.example.recycleapp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerViewAdapterUserItems extends RecyclerView.Adapter<RecyclerViewAdapterUserItems.MyViewHolder>
{
    Context context;
    ArrayList<recycleItem> ItemsList;
    StorageReference storageReference;

    public RecyclerViewAdapterUserItems(Context context, ArrayList<recycleItem> ItemsList){
        this.context=context;
        this.ItemsList=ItemsList;

    }

    @NonNull
    @Override
    public RecyclerViewAdapterUserItems.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.recycle_view_itemforuser, parent, false);
        return new RecyclerViewAdapterUserItems.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterUserItems.MyViewHolder holder, int position) {
        recycleItem r= this.ItemsList.get(position);

        holder.name.setText(r.getName());
        holder.kind.setText("סוג הפח : "+r.getKindOfBin());
        holder.value.setText("ערך הפריט: "+r.getValue()+" ");
        holder.date.setText("תאריך בו מוחזר הפריט: "+r.getDate()+" ");
        storageReference = FirebaseStorage.getInstance().getReference("Image/recycleItem/" + r.getName());
        storageReference = storageReference.child(r.getPicname());
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(context).load(uri).into( holder.pic);
            }
        });

    }

    @Override
    public int getItemCount() {
        return ItemsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name,kind, value, date;
        ImageView pic;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.ItemName);
            value = itemView.findViewById(R.id.ItemValue);
            kind = itemView.findViewById(R.id.ItemKind);
            pic=itemView.findViewById(R.id.ItemImg);
            date=itemView.findViewById(R.id.date);



        }
    }
}
