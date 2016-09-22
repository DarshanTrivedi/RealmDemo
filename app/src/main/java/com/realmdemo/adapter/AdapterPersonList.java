package com.realmdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.realmdemo.R;
import com.realmdemo.realm_bean.Person;

import io.realm.RealmList;

public class AdapterPersonList extends BaseAdapter {

    private final Context mContext;
    LayoutInflater inflater;
    private RealmList<Person> lsitPerson = new RealmList<Person>();

    public AdapterPersonList(Context activity, RealmList<Person> personList) {
        mContext = activity;
        lsitPerson.addAll(personList);
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;

        if (convertView == null) {

            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.inflator_list, null);

            holder.textAge = (TextView) convertView.findViewById(R.id.tvAge);

            holder.textName = (TextView) convertView.findViewById(R.id.tvName);

            holder.textGender = (TextView) convertView.findViewById(R.id.tvGender);


            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();

        }

        holder.textName.setText(lsitPerson.get(position).getName());
        holder.textAge.setText(lsitPerson.get(position).getAge()+"");
        holder.textGender.setText(lsitPerson.get(position).getGender());


        return convertView;
    }

    private class ViewHolder {

        TextView textName,textAge,textGender;

    }

    @Override
    public int getCount() {
        return lsitPerson.size();
    }

    @Override
    public Object getItem(int position) {

        return lsitPerson.get(position);

    }

    @Override
    public long getItemId(int position) {

        return position;

    }
}