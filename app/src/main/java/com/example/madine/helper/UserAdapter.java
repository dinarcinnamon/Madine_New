package com.example.madine.helper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madine.R;
import com.example.madine.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class UserAdapter extends FirebaseRecyclerAdapter<User,UserAdapter.UserViewholder> {

    private String noUnit, name, telpNo, license;
    public UserAdapter(
            @NonNull FirebaseRecyclerOptions<User> options)
    {
        super(options);
    }

    @Override
    protected void
    onBindViewHolder(@NonNull UserViewholder holder,
                     int position, @NonNull User model)
    {
        noUnit = model.getUnitNo();
        name = model.getName();
        telpNo = model.getPhoneNumber();
        license= model.getLicenseNumber();


        holder.noUnittv.setText(noUnit);
        holder.nametv.setText(name);
        holder.telpNotv.setText(telpNo);
        holder.licensetv.setText(license);


    }

    // Function to tell the class about the Card view (here
    // "User.xml")in
    // which the data will be shown
    @NonNull
    @Override
    public UserViewholder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType)
    {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new UserAdapter.UserViewholder(view);
    }

    // Sub Class to create references of the views in Crad
    // view (here "User.xml")
    class UserViewholder
            extends RecyclerView.ViewHolder {
        TextView noUnittv, nametv,telpNotv,licensetv;
        public UserViewholder(@NonNull View itemView)
        {
            super(itemView);

            noUnittv = itemView.findViewById(R.id.info_noUnit_tv);
            telpNotv = itemView.findViewById(R.id.info_telpno_tv);
            nametv = itemView.findViewById(R.id.info_name_tv);
            licensetv = itemView.findViewById(R.id.info_license_tv);
        }
    }


}
