package com.midterm.restaurant_app.viewmodel.adapter;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.midterm.restaurant_app.R;
import com.midterm.restaurant_app.model.Table;

import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder> {

    private Context context;
    List<Table> tableItems;

    public TableAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Table> items){
        this.tableItems = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_details_order,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Table tables = tableItems.get(position);

        holder.tvNameTable.setText(tables.getNameTable());
//        holder.tvNumServed.setText(Integer.toString(tables.getNumServed()));
//        holder.tvTotalServe.setText(Integer.toString(tables.getTotalServe()));
//        holder.tvCostFoods.setText(tables.getTotalCostFood().toString());

        holder.llTableItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("nameTable", tables.getNameTable());
                Navigation.findNavController(view).navigate(R.id.action_serveFragment_to_detailsOrderFragment, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(tableItems != null){
            return tableItems.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvNameTable;
        private TextView tvStatus;
        private TextView tvNumServed;
        private TextView tvTotalServe;
        private TextView tvCostFoods;
        private LinearLayout llTableItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNameTable = itemView.findViewById(R.id.tv_nametable);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvNumServed = itemView.findViewById(R.id.tv_numserved);
            tvCostFoods = itemView.findViewById(R.id.tv_totalcostfood);
            llTableItem = itemView.findViewById(R.id.ll_tableitem);
        }
    }

}
