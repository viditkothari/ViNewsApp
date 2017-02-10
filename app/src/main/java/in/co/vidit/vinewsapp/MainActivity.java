package in.co.vidit.vinewsapp;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<News>> {
    private RecyclerView mRecyclerView;
    private NewsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView tv_emptyView;

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
            Log.e("isNetworkAvailable(): ", "Network Unavailable!");
            tv_emptyView.setText(R.string.nodata);
            mRecyclerView.setVisibility(View.GONE);
            tv_emptyView.setVisibility(View.VISIBLE);
            // mAdapter.clear();
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            tv_emptyView.setVisibility(View.GONE);
        }

        // specify an adapter (see also next example)
        mAdapter = new NewsAdapter(this,new ArrayList<News>());
        mRecyclerView.setAdapter(mAdapter);

        getSupportLoaderManager().initLoader(1, null, this).forceLoad();
    }


    // String url = "https://content.guardianapis.com/search?q=politics&show-fields=thumbnail,byline&page-size=50&format=json&api-key=8b062c45-166a-4242-816e-1764c05c1da2";


    // Checks for Network availability. Accessed via method call in makeHttpRequest()
    private boolean isNetworkAvailable() {
        // TODO: Please suggest how to check if accessing getApplicationContext() is causing some leak.
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return (activeNetworkInfo != null) && activeNetworkInfo.isConnected();
    }

    // shows Indeterminate Progress Bar during network access operation
    /*@Override
    protected void onPreExecute() {
        findViewById(R.id.ll_pbar).setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(ArrayList<Book> books) {
        findViewById(R.id.ll_pbar).setVisibility(View.GONE);
        if (books != null) {
            mAdapter = new BookAdapter(getApplicationContext(), 0, books);
            ((ListView) findViewById(R.id.mRecyclerView)).setAdapter(mAdapter);
        } else {
            tv_emptyView.setVisibility(View.GONE);
            // mAdapter.clear();
        }
    }*/


    @Override
    public Loader<ArrayList<News>> onCreateLoader(int id, Bundle args) {
        return (new NewsLoader(MainActivity.this));
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<News>> loader, ArrayList<News> data) {
        mAdapter.setNews(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<News>> loader) {
        mAdapter.setNews(new ArrayList<News>());
    }

} // End of MainActivity class