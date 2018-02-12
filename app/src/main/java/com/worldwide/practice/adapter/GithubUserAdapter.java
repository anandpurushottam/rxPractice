package com.worldwide.practice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.worldwide.practice.R;
import com.worldwide.practice.data.model.Contributor;
import com.worldwide.practice.data.model.User;
import java.util.ArrayList;
import java.util.List;

/** Created by Anand on 11-02-2018. */
public class GithubUserAdapter extends BaseAdapter {

    private List<Contributor> list;
    private LayoutInflater inflater;
    private Context context;

    public GithubUserAdapter(Context context, List<Contributor> list) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Contributor getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.single_user, null, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        Contributor user = getItem(position);

        mViewHolder.tvName.setText(user.getLogin());
       // mViewHolder.tvPublicRepos.setText(user.getContributions());
        // mViewHolder.ivProfilePic.setImageResource(user.getAvatarUrl());

        return convertView;
    }

    public class MyViewHolder {
        @BindView(R.id.tvName)
        TextView tvName;

        @BindView(R.id.tvPublicRepos)
        TextView tvPublicRepos;

        @BindView(R.id.ivProfilePic)
        ImageView ivProfilePic;

        public MyViewHolder(View item) {
            ButterKnife.bind(item);
        }
    }
}
