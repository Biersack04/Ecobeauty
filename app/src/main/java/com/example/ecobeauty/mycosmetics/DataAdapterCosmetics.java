package com.example.ecobeauty.mycosmetics;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecobeauty.R;

import java.util.List;


class DataAdapterCosmetics extends RecyclerView.Adapter<DataAdapterCosmetics.ViewHolder> {

    private LayoutInflater inflater;
    private List<UserCosmetics> usercosm;

    DataAdapterCosmetics(Context context, List<UserCosmetics> usercosm) {
        this.usercosm = usercosm;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public DataAdapterCosmetics.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_item_cosm, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapterCosmetics.ViewHolder holder, int position) {
        UserCosmetics userCosm = usercosm.get(position);
        holder.nameView.setText(userCosm.getName());
       // holder.dateView.setText(userCosm.getDate());
    }

    @Override
    public int getItemCount() {
        return usercosm.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView nameView, dateView;
        ViewHolder(View view){
            super(view);
            nameView = (TextView) view.findViewById(R.id.name);
            dateView = (TextView) view.findViewById(R.id.date);
        }
    }
}
