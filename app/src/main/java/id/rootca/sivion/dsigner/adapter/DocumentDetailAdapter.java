package id.rootca.sivion.dsigner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import id.rootca.sivion.dsigner.R;
import id.rootca.sivion.dsigner.entity.Document;

/**
 * Created by dianw on 8/29/15.
 */
public class DocumentDetailAdapter extends RecyclerView.Adapter<DocumentDetailAdapter.ViewHolder> {
    private Context context;
    private List<Map.Entry<String, String>> kv = new ArrayList<>();

    public DocumentDetailAdapter(Context context) {
        this.context = context;
    }

    public void update(Document document) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("Subject", document.getSubject());
        map.put("SHA256 Hash", document.getSha256Hash());
        kv.addAll(map.entrySet());

        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.key_value, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Map.Entry<String, String> keyValue = kv.get(position);
        holder.key.setText(keyValue.getKey());
        holder.value.setText(keyValue.getValue());
    }

    @Override
    public int getItemCount() {
        return kv.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.kv_key)
        TextView key;
        @Bind(R.id.kv_value)
        TextView value;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
