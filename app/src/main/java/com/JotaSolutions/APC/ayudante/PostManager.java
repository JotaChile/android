

package com.JotaSolutions.APC.ayudante;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.database.ValueEventListener;
import com.JotaSolutions.APC.main.interacciones.FollowInteractor;
import com.JotaSolutions.APC.main.interacciones.PostInteractor;
import com.JotaSolutions.APC.ayudante.listeners.OnDataChangedListener;
import com.JotaSolutions.APC.ayudante.listeners.OnObjectExistListener;
import com.JotaSolutions.APC.ayudante.listeners.OnPostChangedListener;
import com.JotaSolutions.APC.ayudante.listeners.OnPostCreatedListener;
import com.JotaSolutions.APC.ayudante.listeners.OnPostListChangedListener;
import com.JotaSolutions.APC.ayudante.listeners.OnTaskCompleteListener;
import com.JotaSolutions.APC.modelo.FollowingPost;
import com.JotaSolutions.APC.modelo.Like;
import com.JotaSolutions.APC.modelo.Post;



public class PostManager extends FirebaseListenersManager {

    private static final String TAG = PostManager.class.getSimpleName();
    private static PostManager instance;
    private int newPostsCounter = 0;
    private PostCounterWatcher postCounterWatcher;
    private PostInteractor postInteractor;

    private Context context;

    public static PostManager getInstance(Context context) {
        if (instance == null) {
            instance = new PostManager(context);
        }

        return instance;
    }

    private PostManager(Context context) {
        this.context = context;
        postInteractor = PostInteractor.getInstance(context);
    }

    public void createOrUpdatePost(Post post) {
        try {
            postInteractor.createOrUpdatePost(post);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void getPostsList(OnPostListChangedListener<Post> onDataChangedListener, long date) {
        postInteractor.getPostList(onDataChangedListener, date);
    }

    public void getPostsListByUser(OnDataChangedListener<Post> onDataChangedListener, String userId) {
        postInteractor.getPostListByUser(onDataChangedListener, userId);
    }

    public void getPost(Context context, String postId, OnPostChangedListener onPostChangedListener) {
        ValueEventListener valueEventListener = postInteractor.getPost(postId, onPostChangedListener);
        addListenerToMap(context, valueEventListener);
    }

    public void getSinglePostValue(String postId, OnPostChangedListener onPostChangedListener) {
        postInteractor.getSinglePost(postId, onPostChangedListener);
    }

    public void createOrUpdatePostWithImage(Uri imageUri, final OnPostCreatedListener onPostCreatedListener, final Post post) {
       postInteractor.createOrUpdatePostWithImage(imageUri, onPostCreatedListener, post);
    }

    public void removePost(final Post post, final OnTaskCompleteListener onTaskCompleteListener) {
        postInteractor.removePost(post, onTaskCompleteListener);
    }

    public void addComplain(Post post) {
        postInteractor.addComplainToPost(post);
    }

    public void hasCurrentUserLike(Context activityContext, String postId, String userId, final OnObjectExistListener<Like> onObjectExistListener) {
        ValueEventListener valueEventListener = postInteractor.hasCurrentUserLike(postId, userId, onObjectExistListener);
        addListenerToMap(activityContext, valueEventListener);
    }

    public void hasCurrentUserLikeSingleValue(String postId, String userId, final OnObjectExistListener<Like> onObjectExistListener) {
        postInteractor.hasCurrentUserLikeSingleValue(postId, userId, onObjectExistListener);
    }

    public void isPostExistSingleValue(String postId, final OnObjectExistListener<Post> onObjectExistListener) {
        postInteractor.isPostExistSingleValue(postId, onObjectExistListener);
    }

    public void incrementWatchersCount(String postId) {
        postInteractor.incrementWatchersCount(postId);
    }

    public void incrementNewPostsCounter() {
        newPostsCounter++;
        notifyPostCounterWatcher();
    }

    public void clearNewPostsCounter() {
        newPostsCounter = 0;
        notifyPostCounterWatcher();
    }

    public int getNewPostsCounter() {
        return newPostsCounter;
    }

    public void setPostCounterWatcher(PostCounterWatcher postCounterWatcher) {
        this.postCounterWatcher = postCounterWatcher;
    }

    private void notifyPostCounterWatcher() {
        if (postCounterWatcher != null) {
            postCounterWatcher.onPostCounterChanged(newPostsCounter);
        }
    }

    public void getFollowingPosts(String userId, OnDataChangedListener<FollowingPost> listener) {
        FollowInteractor.getInstance(context).getFollowingPosts(userId, listener);
    }

    public void searchByTitle(String searchText, OnDataChangedListener<Post> onDataChangedListener) {
        closeListeners(context);
        ValueEventListener valueEventListener = postInteractor.searchPostsByTitle(searchText, onDataChangedListener);
        addListenerToMap(context, valueEventListener);
    }

    public void filterByLikes(int limit, OnDataChangedListener<Post> onDataChangedListener) {
        closeListeners(context);
        ValueEventListener valueEventListener = postInteractor.filterPostsByLikes(limit, onDataChangedListener);
        addListenerToMap(context, valueEventListener);
    }

    public interface PostCounterWatcher {
        void onPostCounterChanged(int newValue);
    }
}
