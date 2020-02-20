package com.project.ilearncentral.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.ilearncentral.Activity.AccountTypeSelection;
import com.project.ilearncentral.Activity.AddEditFeed;
import com.project.ilearncentral.Activity.Login;
import com.project.ilearncentral.Adapter.PostFeedAdapter;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.CustomInterface.OnBooleanChangeListener;
import com.project.ilearncentral.Model.Account;
import com.project.ilearncentral.Model.Post;
import com.project.ilearncentral.Model.Posts;
import com.project.ilearncentral.R;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class Feed extends Fragment {

    private PostFeedAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<Post> post;

    private ObservableBoolean done;

    private FloatingActionButton addNewPostBtn;

    public Feed() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        if (Account.getType() == Account.Type.Student)
            view.findViewById(R.id.feed_add_fab).setVisibility(View.GONE);

        addNewPostBtn = view.findViewById(R.id.feed_add_fab);
        addNewPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getContext(), AddEditFeed.class),1);
            }
        });

        done = new ObservableBoolean();
        done.setOnBooleanChangeListener(new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean newValue) {
                post.clear();
                post.addAll(Posts.setPostsToView());
                adapter.notifyDataSetChanged();
            }
        });
        Posts.retrievePostsFromDB(done);

        final SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.feed_pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Posts.retrievePostsFromDB(done);
                pullToRefresh.setRefreshing(false);
            }
        });
        // set up the RecyclerView
        post = new ArrayList<>();

        recyclerView = view.findViewById(R.id.feed_recylerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new PostFeedAdapter(getContext(), post);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK) {
            Posts.retrievePostsFromDB(done);
        }
    }
}
