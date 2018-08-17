package com.marshaarts.myapplication.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.marshaarts.myapplication.R;

public class UserViewHolder extends RecyclerView.ViewHolder {

    private final View rootView;
    private final TextView name;
    private final TextView email;
    private final TextView town;
    private final TextView address;
    private final TextView dob;
    private final TextView education_level;
    private final TextView driving_status;

    UserViewHolder(View view) {
        super(view);

        rootView = view;
        name = (TextView) view.findViewById(R.id.name);
        email = (TextView) view.findViewById(R.id.email);
        town = (TextView) view.findViewById(R.id.town);
        address = (TextView) view.findViewById(R.id.address);
        dob = (TextView) view.findViewById(R.id.dob);
        education_level = (TextView) view.findViewById(R.id.education);
        driving_status = (TextView) view.findViewById(R.id.d_status);
    }
}
