

package com.JotaSolutions.APC.adaptadores;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.JotaSolutions.APC.R;
import com.JotaSolutions.APC.main.base.BaseActivity;
import com.JotaSolutions.APC.adaptadores.holders.PostViewHolder;
import com.JotaSolutions.APC.controladores.LikeControlador;
import com.JotaSolutions.APC.ayudante.PostManager;
import com.JotaSolutions.APC.ayudante.listeners.OnDataChangedListener;
import com.JotaSolutions.APC.modelo.Post;

import java.util.List;


public class PostsByUserAdapter extends BasePostsAdapter {
    public static final String TAG = PostsByUserAdapter.class.getSimpleName();

    private String userId;
    private CallBack callBack;

    public PostsByUserAdapter(final BaseActivity activity, String userId) {
        super(activity);
        this.userId = userId;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.post_item_list_view, parent, false);

        return new PostViewHolder(view, createOnClickListener(), activity, false);
    }

    private PostViewHolder.OnClickListener createOnClickListener() {
        return new PostViewHolder.OnClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                if (callBack != null) {
                    selectedPostPosition = position;
                    callBack.onItemClick(getItemByPosition(position), view);
                }
            }

            @Override
            public void onLikeClick(LikeControlador LikeControlador, int position) {
                Post post = getItemByPosition(position);
                LikeControlador.handleLikeClickAction(activity, post);
            }

            @Override
            public void onAuthorClick(int position, View view) {

            }
        };
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((PostViewHolder) holder).bindData(postList.get(position));
    }

    private void setList(List<Post> list) {
        postList.clear();
        postList.addAll(list);
        notifyDataSetChanged();
    }

    public void loadPosts() {
        if (!activity.hasInternetConnection()) {
            activity.showSnackBar(R.string.internet_connection_failed);
            callBack.onPostLoadingCanceled();
            return;
        }

        OnDataChangedListener<Post> onPostsDataChangedListener = new OnDataChangedListener<Post>() {
            @Override
            public void onListChanged(List<Post> list) {
                setList(list);
                callBack.onPostsListChanged(list.size());
            }
        };

        PostManager.getInstance(activity).getPostsListByUser(onPostsDataChangedListener, userId);
    }

    public void removeSelectedPost() {
        postList.remove(selectedPostPosition);
        callBack.onPostsListChanged(postList.size());
        notifyItemRemoved(selectedPostPosition);
    }

    public interface CallBack {
        void onItemClick(Post post, View view);
        void onPostsListChanged(int postsCount);
        void onPostLoadingCanceled();
    }
}
