

package com.JotaSolutions.APC.adaptadores;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.JotaSolutions.APC.R;
import com.JotaSolutions.APC.adaptadores.holders.UserViewHolder;
import com.JotaSolutions.APC.ayudante.ProfileManager;
import com.JotaSolutions.APC.ayudante.listeners.OnObjectChangedListenerSimple;
import com.JotaSolutions.APC.modelo.Profile;

import java.util.ArrayList;
import java.util.List;



public class SearchUsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = SearchUsersAdapter.class.getSimpleName();

    private List<Profile> itemsList = new ArrayList<>();

    private UserViewHolder.Callback callback;
    private Activity activity;

    public SearchUsersAdapter(Activity activity) {
        this.activity = activity;
    }

    public void setCallback(UserViewHolder.Callback callback) {
        this.callback = callback;
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new UserViewHolder(inflater.inflate(R.layout.user_item_list_view, parent, false),
                callback, activity);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((UserViewHolder) holder).bindData(itemsList.get(position));
    }

    public void setList(List<Profile> list) {
        itemsList.clear();
        itemsList.addAll(list);
        notifyDataSetChanged();
    }

    public void updateItem(int position) {
        Profile profile = getItemByPosition(position);
        ProfileManager.getInstance(activity.getApplicationContext()).getProfileSingleValue(profile.getId(), new OnObjectChangedListenerSimple<Profile>() {
            @Override
            public void onObjectChanged(Profile updatedProfile) {
                itemsList.set(position, updatedProfile);
                notifyItemChanged(position);
            }
        });
    }

    public Profile getItemByPosition(int position) {
        return itemsList.get(position);
    }
}
