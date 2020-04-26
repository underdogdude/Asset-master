package xyz.codecool.android.asset.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import xyz.codecool.android.asset.R;
import xyz.codecool.android.asset.modle.AssetModel;
import xyz.codecool.android.asset.modle.GetAssetListModel;

public class AdapterAssetList extends RecyclerView.Adapter<AdapterAssetList.VersionViewHolder> {

    ArrayList<AssetModel> model;
    Context context;
    OnItemClickListener clickListener;
    String TAG = "AdapterAssetList";

    public AdapterAssetList(Context applicationContext, ArrayList<AssetModel> model) {
        this.context = applicationContext;
        this.model = model;
    }

    @Override
    public VersionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_item_asset_list, viewGroup, false);
        return new VersionViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final VersionViewHolder holder, final int i) {
        holder.txtDescription1.setText(model.get(i).getDescription1());
        holder.txtInvNumber.setText((model.get(i).getInvNumber()));
        holder.txtErpNumber.setText(model.get(i).getErpNumber());
    }

    @Override
    public int getItemCount() {
        return model == null ? 0 : model.size();
    }

    class VersionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtDescription1, txtInvNumber, txtErpNumber;

        VersionViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            txtDescription1 = itemView.findViewById(R.id.txtDescription1);
            txtInvNumber = itemView.findViewById(R.id.txtInvNumber);
            txtErpNumber = itemView.findViewById(R.id.txtErpNumber);
        }

        @Override
        public void onClick(View v) {

            clickListener.onItemClick(v, getPosition());
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

}
