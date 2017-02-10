package in.co.vidit.vinewsapp;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<News>> {
    RecyclerView mRecyclerView;
    NewsAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    TextView tv_emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_emptyView = (TextView) findViewById(R.id.tv_emptyView);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (!isNetworkAvailable()) {
            if(!(findViewById(R.id.tv_emptyView).getVisibility() == View.VISIBLE))
                findViewById(R.id.tv_emptyView).setVisibility(View.VISIBLE);
            findViewById(R.id.ll_pbar).setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            if(findViewById(R.id.tv_emptyView).getVisibility() == View.VISIBLE)
                findViewById(R.id.tv_emptyView).setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }

        // specify an adapter (see also next example)
        mAdapter = new NewsAdapter(this, new ArrayList<News>());
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click

                        String url_string = ((TextView)view.findViewById(R.id.newURL)).getText().toString();
                        URL url;
                        Uri uri=null;
                        try {
                            url = new URL(url_string);
                            uri = Uri.parse( url.toURI().toString() );
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }

                        Intent websiteIntent = new Intent(Intent.ACTION_VIEW,uri);
                        startActivity(websiteIntent);
                    }
                })
        );

        getSupportLoaderManager().initLoader(1, null, this).forceLoad();
    }

    // Checks for Network availability. Accessed via method call in makeHttpRequest()
    private boolean isNetworkAvailable() {
        // TODO: Please suggest how to check if accessing getApplicationContext() is causing some leak.
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return (activeNetworkInfo != null) && activeNetworkInfo.isConnected();
    }
    @Override
    public Loader<ArrayList<News>> onCreateLoader(int id, Bundle args) {
        findViewById(R.id.ll_pbar).setVisibility(View.VISIBLE);
        findViewById(R.id.my_recycler_view).setVisibility(View.GONE);
        return (new NewsLoader(MainActivity.this));
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<News>> loader, ArrayList<News> data) {
        mAdapter.setNews(data);
        findViewById(R.id.ll_pbar).setVisibility(View.GONE);
        findViewById(R.id.my_recycler_view).setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<News>> loader) {
        mAdapter.setNews(new ArrayList<News>());
    }

} // End of MainActivity class