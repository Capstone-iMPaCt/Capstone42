package com.project.ilearncentral.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.project.ilearncentral.Model.Sales;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;

import java.util.List;

public class SubscriptionSalesAdapter extends RecyclerView.Adapter<SubscriptionSalesAdapter.SubscriptionSalesViewHolder> {

    private Context context;
    private List<Sales> salesList;
    private int lastPosition = -1;

    public SubscriptionSalesAdapter(Context context, List<Sales> salesList) {
        this.context = context;
        this.salesList = salesList;
    }

    @NonNull
    @Override
    public SubscriptionSalesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_subscription_sale_row, parent, false);
        return new SubscriptionSalesViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return salesList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final SubscriptionSalesViewHolder holder, final int position) {
        final Sales sale = salesList.get(position);

        holder.cardView.setAnimation(AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.move_up : R.anim.move_down));

        holder.name.setText(sale.getCenterName());
        holder.subscription.setText(sale.getSubscriptionTitle());
        holder.amount.setText(Utility.showPriceInPHP(sale.getFee()));
        holder.date.setText(Utility.getDateAsString(sale.getDate()));
    }

    protected class SubscriptionSalesViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private TextView name, subscription, amount, date;

        public SubscriptionSalesViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.rsrr_root);
            name = itemView.findViewById(R.id.rsrr_user_name);
            subscription = itemView.findViewById(R.id.rsrr_subscription);
            amount = itemView.findViewById(R.id.rsrr_amount);
            date = itemView.findViewById(R.id.rsrr_date);
        }
    }
}
