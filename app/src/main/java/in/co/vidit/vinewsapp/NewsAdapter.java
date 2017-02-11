package in.co.vidit.vinewsapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<News> news;
    // private Bitmap bitmap_thumbnail;
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_newstitle;
        TextView tv_newssection;
        TextView tv_author;
        TextView tv_date;
        TextView tv_newURL;
        ImageView imgv_thumbnail;

        ViewHolder(CardView v) {
            super(v);
            tv_newstitle = (TextView) v.findViewById(R.id.tv_newstitle);
            tv_newssection = (TextView) v.findViewById(R.id.tv_newssection);
            tv_author = (TextView) v.findViewById(R.id.tv_author);
            tv_date = (TextView) v.findViewById(R.id.tv_date);
            imgv_thumbnail = (ImageView) v.findViewById(R.id.imgv_thumbnail);
            tv_newURL = (TextView)v.findViewById(R.id.newURL);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    NewsAdapter(Context context, ArrayList<News> news) {
        this.news = news;
        this.context=context;
        Log.e("news List Img Count: ",String.valueOf(news.size()));
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Create a new view and return a ViewHolder object created from it.
        return (new ViewHolder((CardView)(LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false))));
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tv_newstitle.setText(news.get(position).getmTitle());
        holder.tv_newssection.setText(news.get(position).getmSection());
        holder.tv_author.setText(news.get(position).getmAuthor());
        holder.tv_date.setText(news.get(position).getmDate());
        holder.tv_newURL.setText(news.get(position).getmURL());


        if (news.get(position).hasImg()){
            Picasso.with(context)
                    .load(news.get(position).getmImgURL())
                    .into(holder.imgv_thumbnail);

            // holder.imgv_thumbnail.setImageBitmap(bitmap_thumbnail);
        }

    }

    void setNews(ArrayList<News> news) {
        this.news = news;
        notifyDataSetChanged();
    }

    // Return the size of your data-set (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return news.size();
    }
}