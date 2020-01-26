package com.project.ilearncentral.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.ilearncentral.Adapter.NewsFeedAdapter;
import com.project.ilearncentral.Model.News;
import com.project.ilearncentral.R;

import java.util.ArrayList;

public class NewsFeed extends Fragment {

    private NewsFeedAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<News> news;

    public NewsFeed() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news_feed, container, false);
        // set up the RecyclerView
        news = new ArrayList<>();
        news.add(new News(R.drawable.user, "OnePlus 6T Camera Review:", "6 July 1994", "11:50 A.M.", R.drawable.news_image, "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."));
        news.add(new News(R.drawable.avatar_boy, "I love Programming And Design", "6 July 1994", "11:51 A.M", R.drawable.news_image, "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam,"));
        news.add(new News(R.drawable.user, "My first trip to Thailand story ", "6 July 1994", "11:52 A.M.", R.drawable.news_image, "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."));
        news.add(new News(R.drawable.user, "After Facebook Messenger, Viber now gets...", "6 July 1994", "11:50 A.M.", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s,"));
        news.add(new News(R.drawable.avatar_boy, "Isometric Design Grid Concept", "6 July 1994", "11:53 A.M.", "lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit"));
        news.add(new News(R.drawable.user, "Android R Design Concept 4K", "6 July 1994", "11:54 A.M.", R.drawable.news_image, "lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit "));

        news.add(new News(R.drawable.user, "OnePlus 6T Camera Review:", "7 July 1994", "11:50 A.M.", R.drawable.news_image, "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."));
        news.add(new News(R.drawable.user, "I love Programming And Design", "7 July 1994", "11:51 A.M", R.drawable.news_image, "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam,"));
        news.add(new News(R.drawable.avatar_boy, "My first trip to Thailand story ", "7 July 1994", "11:52 A.M.", R.drawable.news_image, "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."));
        news.add(new News(R.drawable.user, "After Facebook Messenger, Viber now gets...", "7 July 1994", "11:50 A.M.", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s,"));
        news.add(new News(R.drawable.user, "Isometric Design Grid Concept", "7 July 1994", "11:53 A.M.", "lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit"));
        news.add(new News(R.drawable.avatar_boy, "Android R Design Concept 4K", "6 July 1994", "11:54 A.M.", R.drawable.news_image, "lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit "));

        news.add(new News(R.drawable.avatar_boy, "OnePlus 6T Camera Review:", "8 July 1994", "11:50 A.M.", R.drawable.news_image, "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."));
        news.add(new News(R.drawable.user, "I love Programming And Design", "8 July 1994", "11:51 A.M", R.drawable.news_image, "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam,"));
        news.add(new News(R.drawable.user, "My first trip to Thailand story ", "8 July 1994", "11:52 A.M.", R.drawable.news_image, "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."));
        news.add(new News(R.drawable.avatar_boy, "After Facebook Messenger, Viber now gets...", "6 July 1994", "11:50 A.M.", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s,"));
        news.add(new News(R.drawable.avatar_boy, "Isometric Design Grid Concept", "8 July 1994", "11:53 A.M.", "lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit"));
        news.add(new News(R.drawable.user, "Android R Design Concept 4K", "8 July 1994", "11:54 A.M.", R.drawable.news_image, "lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit "));
        recyclerView = view.findViewById(R.id.news_container_layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new NewsFeedAdapter(getContext(), news);
        recyclerView.setAdapter(adapter);
        return view;
    }
}