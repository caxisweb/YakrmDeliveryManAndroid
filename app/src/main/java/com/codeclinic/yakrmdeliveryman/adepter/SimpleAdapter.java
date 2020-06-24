package com.codeclinic.yakrmdeliveryman.adepter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.codeclinic.yakrmdeliveryman.Models.NotificationListItemModel;
import com.codeclinic.yakrmdeliveryman.R;
import com.codeclinic.yakrmdeliveryman.Utils.SessionManager;

import java.util.List;

public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.SimpleViewHolder> {
    final Context mContext;
    List<NotificationListItemModel> mData;
    SessionManager sessionManager;

    public SimpleAdapter(Context context, List<NotificationListItemModel> data) {
        mContext = context;
        this.mData = data;
        sessionManager = new SessionManager(context);
    }

    public void add(NotificationListItemModel s, int position) {
        position = position == -1 ? getItemCount() : position;
        mData.add(position, s);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        if (position < getItemCount()) {
            mData.remove(position);
            notifyItemRemoved(position);
        }
    }

    public SimpleAdapter.SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.custom_notification_list_items, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, final int position) {
      /*  if (sessionManager.getLanguage("Language", "en").equals("en")) {
            holder.tv_notification_Title.setText(mData.get(position).getSubject());
        } else {
            if (mData.get(position).getSubject().equals("Wallet Deduction")) {
                holder.tv_notification_Title.setText("نم الخصم من المحفظة");
                holder.tv_notification.setText(mData.get(position).getDescription());
            } else if (mData.get(position).getSubject().equals("Vouchers purchases")) {
                holder.tv_notification_Title.setText("مشتريات القسائم");
                holder.tv_notification.setText("لقد تم شراء قسائم جديدة");
            } else if (mData.get(position).getSubject().equals("Replace Voucher")) {
                holder.tv_notification_Title.setText("مشتريات القسائم");
                holder.tv_notification.setText("لقد استبدلت بنجاح قسيمة جديدة");
            }

        }*/

        holder.tv_notification_Title.setText(mData.get(position).getSubject());
        holder.tv_notification.setText(mData.get(position).getDescription());

        // holder.tv_notification.setText(mData.get(position).getDescription());
        holder.tv_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mContext, "Position =" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        TextView tv_notification, tv_notification_Title;

        public SimpleViewHolder(View view) {
            super(view);
            tv_notification = view.findViewById(R.id.tv_notification);
            tv_notification_Title = view.findViewById(R.id.tv_notification_Title);
        }
    }
}
