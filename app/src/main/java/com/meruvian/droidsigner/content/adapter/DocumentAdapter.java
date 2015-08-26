package com.meruvian.droidsigner.content.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.meruvian.droidsigner.R;
import com.meruvian.droidsigner.entity.Document;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by root on 8/18/15.
 */
public class DocumentAdapter extends BaseAdapter {
    private List<Document> documents = new ArrayList<Document>();
    private DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");

    private Context context;

    private LayoutInflater inflater;

    public DocumentAdapter(Context context,List<Document> documents){
        this.context = context;
        this.documents = documents;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addDocument(Document document){
        documents.add(document);
        notifyDataSetChanged();
    }
    public void addDocuments(List<Document> documents){
        this.documents.addAll(documents);
        notifyDataSetChanged();
    }
    public void clear(){
        documents.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return documents.size();
    }

    @Override
    public Object getItem(int position) {
        return documents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.main_activity, parent, false);

//            holder.subject = (TextView) convertView.findViewById(R.id.view_subject);
//            holder.description = (TextView) convertView.findViewById(R.id.view_description);
//            holder.date = (TextView) convertView.findViewById(R.id.view_date);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

//        holder.subject.setText(documents.get(position).getSubject());
//        holder.description.setText(documents.get(position).getDescription());
//        holder.date.setText(dateFormat.format(new Date(documents.get(position).getCreateDate())));

        return convertView;
    }
    private class ViewHolder {
        public TextView subject;
        public TextView description;
        private TextView date;
    }
}
