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
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class RecyclerViewRatingAdapter extends RecyclerView.Adapter<RecyclerViewRatingAdapter.MyViewHolder>
{

    Context context;
    ArrayList<UserForRating> UsersList;
    StorageReference storageReference;

    public RecyclerViewRatingAdapter(Context context, ArrayList<UserForRating> UsersList){
        this.context=context;
        this.UsersList=UsersList;
    }

    @NonNull
    @Override
    public RecyclerViewRatingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.rating_item_recycler_view, parent, false);
        return new RecyclerViewRatingAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewRatingAdapter.MyViewHolder holder, int position) {
        UserForRating u = this.UsersList.get(position);
        holder.rating.setText(position+1+"");
        holder.username.setText(u.getName());
        holder.usercnt.setText("כמות פריטים שמוחזרו: "+u.getBin().getCnt());
        holder.userpoints.setText("סך הנקודות: "+ u.getBin().getPoints());

        storageReference = FirebaseStorage.getInstance().getReference("Image/users/" + u.getEmail());
        storageReference = storageReference.child(u.getPicName());
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(context).load(uri).into( holder.pic);
            }
        });

    }

    @Override
    public int getItemCount() {
        return UsersList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView username,usercnt, userpoints, rating;
        ImageView pic;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.UserName);
            usercnt = itemView.findViewById(R.id.UserCnt);
            userpoints = itemView.findViewById(R.id.UserPoints);
            rating=itemView.findViewById(R.id.num);
            pic=itemView.findViewById(R.id.uPicForRating);

        }
    }
}

