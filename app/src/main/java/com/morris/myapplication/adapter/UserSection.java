package com.morris.myapplication.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.morris.myapplication.R;
import com.morris.myapplication.model.User;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class UserSection extends StatelessSection {

    String title;
    List<User> userlist;

    public UserSection(String title, List<User> userlist) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.user_item)
                .headerResourceId(R.layout.section_title)
                .build());

        this.title = title;
        this.userlist = userlist;
    }

    @Override
    public int getContentItemsTotal() {
        return userlist.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new UserViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        final UserViewHolder userHolder = (UserViewHolder) holder;
        final User usr = userlist.get(position);

        userHolder.name.setText(usr.getFullname());
        userHolder.email.setText(usr.getEmail());
        userHolder.town.setText(usr.getTown());
        userHolder.dob.setText(usr.getYob());
        userHolder.address.setText(usr.getPostaladdress());
        if(usr.isIsdriver()){
            userHolder.driving_status.setText("Driving");
        }else{
            userHolder.driving_status.setText("Not Driving");
        }
        userHolder.education_level.setText(usr.getEducation());

        userHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

        headerHolder.tvTitle.setText(title);
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitle;

        HeaderViewHolder(View view) {
            super(view);

            tvTitle = (TextView) view.findViewById(R.id.header_id);
        }
    }

    private class UserViewHolder extends RecyclerView.ViewHolder {

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

}
