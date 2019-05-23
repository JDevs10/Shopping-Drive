package com.example.j_lds.shoppingdrive.pages_Client.fragments_Client;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.j_lds.shoppingdrive.R;
import com.example.j_lds.shoppingdrive.adapters_Client.FindMerchantArticlesAdapter_Client;
import com.example.j_lds.shoppingdrive.databaseOffline_Client.DatabaseHelper;
import com.example.j_lds.shoppingdrive.object_class_Client.Article;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentFindMerchantArticles_Client extends Fragment {
    private String TAG = FragmentFindMerchantArticles_Client.class.getSimpleName();
    private Context mContext;
    private View view;
    private Toolbar toolbar;
    private NavigationView navigationView;

    private RecyclerView recyclerView_findMerchantAticles;
    private GridLayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    private DatabaseReference mdatabaseReference;
    private DatabaseHelper db;

    private ArrayList<Article> articleList;
    private String selectedMerchantUid = "";
    private String selectedMerchantCompanyName = "";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHelper(mContext);
        toolbar = getActivity().findViewById(R.id.toolbar);
        navigationView = getActivity().findViewById(R.id.home_nav_view);

        //get merchant uid
        Cursor res = db.getAllSettingsData();
        res.moveToFirst();
        if (!res.getString(1).equals("")){
            selectedMerchantUid = res.getString(1);
            selectedMerchantCompanyName = res.getString(2);
            Log.d(TAG, "db Merchant uid: "+selectedMerchantUid);
            Log.d(TAG, "db Merchant name: "+selectedMerchantCompanyName);
        }else{
            selectedMerchantUid = getArguments().getString("SelectedMerchantUid");
            selectedMerchantCompanyName = getArguments().getString("SelectedMerchantCompanyName");
            Log.d(TAG, "Merchant uid: " + selectedMerchantUid);
            Log.d(TAG, "Merchant name: " + selectedMerchantCompanyName);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_find_merchant_article_client, container, false);

        recyclerView_findMerchantAticles = view.findViewById(R.id.fragment_find_merchant_recyclerView_merchant_articles);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        toolbar.setTitle(selectedMerchantCompanyName+"'s products");
        navigationView.setCheckedItem(R.id.nav_shelves);

        recyclerView_findMerchantAticles.setHasFixedSize(true);
        recyclerView_findMerchantAticles.setLayoutManager(new LinearLayoutManager(mContext));
        layoutManager = new GridLayoutManager(mContext, 2);
        recyclerView_findMerchantAticles.setLayoutManager(layoutManager);

        articleList = new ArrayList<Article>();
        getMerchantArticleDbData();

        adapter = new FindMerchantArticlesAdapter_Client(selectedMerchantUid, articleList);
        recyclerView_findMerchantAticles.setAdapter(adapter);
    }

    public void getMerchantArticleDbData(){
        mdatabaseReference = FirebaseDatabase.getInstance("https://shopping-drive-4bdce.firebaseio.com/").getReference().child("user/merchant/"+selectedMerchantUid+"/articles");
        mdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if(dataSnapshot.exists()){
                            Article article = ds.getValue(Article.class);
                            article.setId(ds.getKey());
                            Log.d("Article id : ", "||=> "+article.getId());
                            Log.d("Article name : ", "||=> "+article.getName());
                            Log.d("Article image : ", "||=> "+article.getImage());
                            Log.d("Article description : ", "||=> "+article.getDescription());

                            articleList.add(article);
                            adapter.notifyDataSetChanged();
                        }else{
                            Log.d("Article Error : ", "Can't find articles !!!");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(mContext, "ERROR 'Get Article' :\n"+databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
