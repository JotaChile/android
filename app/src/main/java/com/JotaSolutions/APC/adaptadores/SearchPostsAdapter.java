

package com.JotaSolutions.APC.adaptadores;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.JotaSolutions.APC.R;
import com.JotaSolutions.APC.adaptadores.holders.PostViewHolder;
import com.JotaSolutions.APC.controladores.LikeControlador;
import com.JotaSolutions.APC.main.base.BaseActivity;
import com.JotaSolutions.APC.modelo.Post;

import java.util.List;


public class SearchPostsAdapter extends BasePostsAdapter {
    public static final String TAG = SearchPostsAdapter.class.getSimpleName();

    private CallBack callBack;

    public SearchPostsAdapter(final BaseActivity activity) {
        super(activity);
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.post_item_list_view, parent, false);

        return new PostViewHolder(view, createOnClickListener(), activity, true);
    }

    private PostViewHolder.OnClickListener createOnClickListener() {
        return new PostViewHolder.OnClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                if (callBack != null && callBack.enableClick()) {
                    selectedPostPosition = position;
                    callBack.onItemClick(getItemByPosition(position), view);
                }
            }

            @Override
            public void onLikeClick(LikeControlador LikeControlador, int position) {
                if (callBack != null && callBack.enableClick()) {
                    Post post = getItemByPosition(position);
                    LikeControlador.handleLikeClickAction(activity, post);
                }
            }

            @Override
            public void onAuthorClick(int position, View view) {
                if (callBack != null && callBack.enableClick()) {
                    callBack.onAuthorClick(getItemByPosition(position).getAuthorId(), view);
                }
            }
        };
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((PostViewHolder) holder).bindData(postList.get(position));
    }

    public void setList(List<Post> list) {
        cleanSelectedPostInformation();
        postList.clear();
        postList.addAll(list);
        notifyDataSetChanged();
    }

    public void removeSelectedPost() {
        if (selectedPostPosition != RecyclerView.NO_POSITION) {
            postList.remove(selectedPostPosition);
            notifyItemRemoved(selectedPostPosition);
        }
    }

    public interface CallBack {
        void onItemClick(Post post, View view);
        void onAuthorClick(String authorId, View view);
        boolean enableClick();
    }
}
