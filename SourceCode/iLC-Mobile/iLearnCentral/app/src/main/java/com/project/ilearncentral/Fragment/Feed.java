package com.project.ilearncentral.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.ilearncentral.Activity.AddEditFeed;
import com.project.ilearncentral.Adapter.PostFeedAdapter;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.CustomInterface.OnBooleanChangeListener;
import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.Model.Post;
import com.project.ilearncentral.MyClass.Posts;
import com.project.ilearncentral.R;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class Feed extends Fragment {

    private PostFeedAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<Post> post;

    private ObservableBoolean done;

    private FloatingActionButton addNewPostBtn;
    private final int NEW_POST = 1;

    private SearchView searchView;
    private TextView toggleView;

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

        if (!Account.isType("Student"))
            view.findViewById(R.id.feed_add_fab).setVisibility(View.VISIBLE);

        searchView = view.findViewById(R.id.feed_searchview);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                post.clear();
                post.addAll(Posts.searchText(query));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                done.set(true);
                return false;
            }
        });

        toggleView = view.findViewById(R.id.feed_toggle_view);
        toggleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toggleView.getText().toString() == "All") {
                    toggleView.setBackgroundResource(R.drawable.bg_unselected_day_rounded);
                    toggleView.setText("Mine");
                } else {
                    toggleView.setBackgroundResource(R.drawable.bg_selected_day_rounded);
                    toggleView.setText("All");
                }
                setToggleView();
            }
        });

        addNewPostBtn = view.findViewById(R.id.feed_add_fab);
        addNewPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getContext(), AddEditFeed.class), NEW_POST);
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
                if (!searchView.isIconified()) {
                    searchView.setQuery("", false);
                    searchView.setIconified(true);
                }
                searchView.clearFocus();
                if (toggleView.getText().toString().equals("Mine")) {
                    setToggleView();
                } else {
                    Posts.retrievePostsFromDB(done);
                }
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
        if (requestCode == NEW_POST && resultCode == RESULT_OK) {
            Posts.retrievePostFromDB(data.getStringExtra("postId"), done);
        }
    }

    private void setToggleView() {
        if (toggleView.getText().toString().equals("Mine")) {
            post.clear();
            post.addAll(Posts.myPosts());
            adapter.notifyDataSetChanged();
        } else {
            post.clear();
            post.addAll(Posts.searchText(""));
            adapter.notifyDataSetChanged();
        }
    }
}
