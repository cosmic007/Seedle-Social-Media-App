package com.project.seedle.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.project.seedle.Article;
import com.project.seedle.CategoryRVAdapter;
import com.project.seedle.CategoryRVModel;
import com.project.seedle.NewsModel;
import com.project.seedle.NewsRVAdapter;
import com.project.seedle.R;
import com.project.seedle.RetrofitAPI;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class News extends Fragment implements CategoryRVAdapter.CategoryClickInterface{

    //42eeba6044534d4ea5a3508b983dc1d7


    private RecyclerView newsRV,categoryRV;
    private ProgressBar loadingPB;

    private ArrayList <Article> articleArrayList;

    private ArrayList<CategoryRVModel> categoryRVModelArrayList;

    private CategoryRVAdapter categoryRVAdapter;

    private NewsRVAdapter newsRVAdapter;






    public News() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);



        newsRV =view.findViewById(R.id.newsRV);
        categoryRV = view.findViewById(R.id.Categories);
        loadingPB = view.findViewById(R.id.news_progressbar);

        articleArrayList = new ArrayList<>();
        categoryRVModelArrayList = new ArrayList<>();

        newsRVAdapter = new NewsRVAdapter(articleArrayList,getContext());
        categoryRVAdapter = new CategoryRVAdapter(categoryRVModelArrayList,getContext(),this::onCategoryClick);
        newsRV.setLayoutManager(new LinearLayoutManager(getContext()));
        newsRV.setAdapter(newsRVAdapter);
        categoryRV.setAdapter(categoryRVAdapter);
        getCategories();
        getNews("Technology");
        newsRVAdapter.notifyDataSetChanged();






        return view;
    }

    private void getCategories(){


        categoryRVModelArrayList.add(new CategoryRVModel("All","https://images.unsplash.com/photo-1476242906366-d8eb64c2f661?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mjd8fG5ld3N8ZW58MHx8MHx8&auto=format&fit=crop&w=500&q=60"));
        categoryRVModelArrayList.add(new CategoryRVModel("Technology","https://images.unsplash.com/photo-1461749280684-dccba630e2f6?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MzR8fHRlY2hub2xvZ3l8ZW58MHx8MHx8&auto=format&fit=crop&w=500&q=60"));
        categoryRVModelArrayList.add(new CategoryRVModel("Science","https://images.unsplash.com/photo-1628595351029-c2bf17511435?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTR8fHNjaWVuY2V8ZW58MHx8MHx8&auto=format&fit=crop&w=500&q=60"));
        categoryRVModelArrayList.add(new CategoryRVModel("Sports","https://plus.unsplash.com/premium_photo-1672046218081-5154b14bad06?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTl8fHNwb3J0c3xlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60"));
        categoryRVModelArrayList.add(new CategoryRVModel("General","https://images.unsplash.com/photo-1457369804613-52c61a468e7d?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8OHx8Z2VuZXJhbHxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60"));
        categoryRVModelArrayList.add(new CategoryRVModel("Business","https://images.unsplash.com/photo-1578574577315-3fbeb0cecdc2?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MzR8fGJ1c2luZXNzfGVufDB8fDB8fA%3D%3D&auto=format&fit=crop&w=500&q=60"));
        categoryRVModelArrayList.add(new CategoryRVModel("Entertainment","https://images.unsplash.com/photo-1598518142096-254d1d4d34a4?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MjN8fGVudGVydGFpbm1lbnR8ZW58MHx8MHx8&auto=format&fit=crop&w=500&q=60"));
        categoryRVModelArrayList.add(new CategoryRVModel("Health","https://images.unsplash.com/photo-1576091160550-2173dba999ef?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8NjR8fGhlYWx0aHxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60"));

        categoryRVAdapter.notifyDataSetChanged();


    }

    private void getNews(String category){
        loadingPB.setVisibility(View.VISIBLE);
        articleArrayList.clear();
        String categoryURL = "https://newsapi.org/v2/top-headlines?country=in&category="+category+"&apikey=42eeba6044534d4ea5a3508b983dc1d7";
        String url = "https://newsapi.org/v2/top-headlines?country=in&excludeDomains=stackoverflow.com&sortBy=publishedAt&language=en&apiKey=42eeba6044534d4ea5a3508b983dc1d7";
        String BASE_URL = "https://newsapi.org/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<NewsModel> call;

        if(category.equals("All")){
            call = retrofitAPI.getAllNews(url);
        }
        else{
            call = retrofitAPI.getNewsByCategory(categoryURL);
        }
        call.enqueue(new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                NewsModel newsModel = response.body();
                loadingPB.setVisibility(View.GONE);
                ArrayList<Article> articles = newsModel.getArticles();
                for (int i=0; i<articles.size(); i++)
                {
                    articleArrayList.add(new Article(articles.get(i).getTitle(),articles.get(i).getDescription(),articles.get(i).getUrltoimage(),articles.get(i).getUrl(),
                            articles.get(i).getContent()));
                }
                newsRVAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to get news", Toast.LENGTH_SHORT).show();

            }
        });

    }



    @Override
    public void onCategoryClick(int position) {

        String category = categoryRVModelArrayList.get(position).getCategory();
        getNews(category);

    }
}