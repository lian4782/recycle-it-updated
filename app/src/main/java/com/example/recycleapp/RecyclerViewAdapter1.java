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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerViewAdapter1 extends RecyclerView.Adapter<RecyclerViewAdapter1.MyViewHolder>
{
    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    ArrayList<recycleItem> ItemsList;
    StorageReference storageReference;

    public RecyclerViewAdapter1(Context context, ArrayList<recycleItem> ItemsList, RecyclerViewInterface recyclerViewInterface  ){
        this.context=context;
        this.ItemsList=ItemsList;
        this.recyclerViewInterface=recyclerViewInterface;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter1.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.recycle_view_item, parent, false);
        return new RecyclerViewAdapter1.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter1.MyViewHolder holder, int position) {
        recycleItem r= this.ItemsList.get(position);

        holder.name.setText(r.getName());
        holder.kind.setText("סוג הפח: "+r.getKindOfBin());
        holder.value.setText("ערך הפריט"+r.getValue()+" ");
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

        TextView name,kind, value;
        ImageView pic;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            name = itemView.findViewById(R.id.ItemName);
            value = itemView.findViewById(R.id.ItemValue);
            kind = itemView.findViewById(R.id.ItemKind);
            pic=itemView.findViewById(R.id.ItemImg);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface!=null){
                        int pos=getAbsoluteAdapterPosition();

                        if(pos!=RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }

                    }
                }
            });

        }
    }
}
