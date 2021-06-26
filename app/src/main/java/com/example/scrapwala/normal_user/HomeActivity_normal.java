package com.example.scrapwala.normal_user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


import com.example.scrapwala.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity_normal extends AppCompatActivity {
private FloatingActionButton floatingActionButton;
private Toolbar toolbar;
private TextView textView;
private String user_phn;
private static final String TAG = "HomeActivity_normal";
private RecyclerView recyclerView;
private myadapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_normal);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        page_views();
        setSupportActionBar(toolbar);
        recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity_normal.this));
        //For showing u have not uploaded any post
        //textView.setVisibility(View.INVISIBLE);
        Intent intent = getIntent();
        user_phn = intent.getStringExtra("phn");
       /* Toast.makeText(getApplicationContext(),intent.getStringExtra("fullname") +
                intent.getStringExtra("email") +
                intent.getStringExtra("phn") +
                intent.getStringExtra("password")
                ,Toast.LENGTH_LONG).show();*/

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HomeActivity_normal.this,UploadActivity.class);
                intent.putExtra("user_phn",user_phn);
                startActivity(intent);
            }
        });
        Query queries = FirebaseDatabase.getInstance().getReference().child("ScrapInfo").child(user_phn);
        queries.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG,"snapshot "+snapshot);
                if(snapshot.exists()){

                    textView.setVisibility(View.INVISIBLE);
                   // Toast.makeText(HomeActivity_normal.this,"Exist",Toast.LENGTH_LONG).show();
                }
                else
                {
                    textView.setVisibility(View.VISIBLE);
                   // Toast.makeText(HomeActivity_normal.this,"Not Exist",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseRecyclerOptions<model> options =  new FirebaseRecyclerOptions.Builder<model>().setQuery(queries,model.class).build();
        adapter = new myadapter(options);
        recyclerView.setAdapter(adapter);



    }
    @Override
    protected void onStart()
    {
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }
    private void page_views()
    {
        floatingActionButton = (FloatingActionButton)findViewById(R.id.upload);
        toolbar = findViewById(R.id.toolbar);
        textView = findViewById(R.id.textView);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview_post);
    }
}