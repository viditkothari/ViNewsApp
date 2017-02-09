package in.co.vidit.vinewsapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    public void setNews(ArrayList<News> news) {
        this.news = news;
        notifyDataSetChanged();
    }

    private List<News> news;
    ImageView imgv_thumbnail;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_newstitle;
        TextView tv_newssection;
        TextView tv_author;
        TextView tv_date;
        ImageView imgv_thumbnail;

        ViewHolder(TextView v) {
            super(v);
            tv_newstitle = (TextView) v.findViewById(R.id.tv_newstitle);
            tv_newssection = (TextView) v.findViewById(R.id.tv_newssection);
            tv_author = (TextView) v.findViewById(R.id.tv_author);
            tv_date = (TextView) v.findViewById(R.id.tv_date);
            imgv_thumbnail = (ImageView) v.findViewById(R.id.imgv_thumbnail);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public NewsAdapter(ArrayList<News> news) {
        this.news = news;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Create a new view and return a ViewHolder object created from it.
        return (new ViewHolder((TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false)));
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tv_newstitle.setText(news.get(position).getmTitle());
        holder.tv_newssection.setText(news.get(position).getmSection());
        holder.tv_author.setText(news.get(position).getmAuthor());
        holder.tv_date.setText(news.get(position).getmDate());

        if (news.get(position).getmImgURL() != null)
            new DownloadImageTask().execute(news.get(position).getmImgURL());
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        protected Bitmap doInBackground(String... str) {
            Bitmap bmp = null;
            try {
                bmp = makeHttpRequestAndFetchImage(str[0]);
            } catch (IOException x) {
                Log.e("DownloadImageTask", "IOException");
            }
            return bmp;
        }

        protected void onPostExecute(Bitmap bmp) {
            imgv_thumbnail.setImageBitmap(bmp);
        }

        private Bitmap readFromStream(InputStream iS) throws IOException {
            Bitmap bmp = null;
            if (iS != null) {
                try {
                    bmp = BitmapFactory.decodeStream(iS);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
            }
            return bmp;
        }

        private Bitmap makeHttpRequestAndFetchImage(String url) throws IOException, NullPointerException {
            URL finalURL = null;
            try {
                finalURL = new URL(url);
            } catch (MalformedURLException except) {
                Log.e("Yo!", "Error with URL creation" + except);
            }

            // HttpURLConnection object declaration
            HttpURLConnection urlConnForImage = null;

            // InputStream object declaration to store the InputStream to be received from the HTTP request
            InputStream iS = null;
            Bitmap bmp = null;
            try {
                urlConnForImage = (HttpURLConnection) finalURL.openConnection();
                urlConnForImage.setRequestMethod("GET");
                urlConnForImage.setReadTimeout(9000 /* milliseconds */);
                urlConnForImage.setConnectTimeout(12000 /* milliseconds */);
                urlConnForImage.connect();
                if (urlConnForImage.getResponseCode() == 200) {
                    iS = urlConnForImage.getInputStream();
                    Log.i("URL Connection Done", " Code 200");
                    Log.i("URL Connection Done", iS.toString());
                    bmp = readFromStream(iS);
                } else
                    Log.i("Found Error code: ", urlConnForImage.getResponseCode() + " ! ");
            } catch (IOException e) {
                Log.i("Invalid URL! ", " Please validity of URL: " + finalURL.getPath());
            } finally {
                if (urlConnForImage != null) {
                    urlConnForImage.disconnect();
                }
                if (iS != null) {
                    try {
                        iS.close();
                    } catch (IOException ioExcept) {
                        Log.i("Closing InputStream: ", "caused " + ioExcept + "to occur!");
                    }

                }
            }
            return bmp;
        }

    }

    // Return the size of your data-set (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return news.size();
    }
}