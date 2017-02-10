package in.co.vidit.vinewsapp;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class NewsLoader extends AsyncTaskLoader<ArrayList<News>> {

    public NewsLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<News> loadInBackground() {
        Log.i("In loadInBackground", " method ;)");

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = "";
        ArrayList<News> news = new ArrayList<>();
        try {
            jsonResponse = makeHttpRequest(createURL()); // createURL forms the URL String by formatting the search words, keyword
        } catch (IOException e) {
            Log.i("makeHttpRequest()", " Error found while"); //  Handle the IOException
        }

        if (TextUtils.isEmpty(jsonResponse)) {
            return news;
        } else {
            news = extractDataFromJSON(jsonResponse);
            return news;
        }
    }

    // Creates URL object from a URL Search string
    private URL createURL() {
        URL completeURL = null;
        try {
            completeURL = new URL("https://content.guardianapis.com/search?q=politics&show-fields=thumbnail,byline&page-size=50&format=json&api-key=8b062c45-166a-4242-816e-1764c05c1da2");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return completeURL;
    }

    /* readFromStream(InputStream) is accessed via makeHttpRequest(URL) method. It does following
     * reads from InputStream received from method call made in makeHttpRequest(URL) method
     * uses BfrdReader to read data in nice amount of chunks (lines)
     */
    private String readFromStream(InputStream iS) throws IOException {
        StringBuilder outSB = new StringBuilder();
        if (iS != null) {
            InputStreamReader iSReader = new InputStreamReader(iS, "UTF-8");
            BufferedReader reader = new BufferedReader(iSReader);
            String line = reader.readLine();
            while (line != null) {
                outSB.append(line);
                line = reader.readLine();
            }
        }
        return outSB.toString();
    }

    /* makeHttpRequest does following:
     * takes in "URL" Object and returns JSON Response in terms of a string.
     * checks if Android Device is connected to a network
     * if yes, then uses the URL Object passed in as parameter {returned from the createURL method()} to create a 'HttpURLConnection' object
     * configure the HttpURLConnection object created
     * try {make HttpURLConnection} using 'connect()'
     * if successful, tries reading the InputStream returned.
     * handles IOExceptions while creating HttpURLConnection object or closing the InputStream or checks for errors if "getResponseCode()" is not '200'
     */
    private String makeHttpRequest(URL url) throws IOException {
        // String declaration for JSON Response to be fetched from the internet
        String jsonResponse = "";

        // HttpURLConnection object declaration
        HttpURLConnection urlconn = null;

        // InputStream object declaration to store the InputStream to be received from the HTTP request
        InputStream iS = null;

        try {
            urlconn = (HttpURLConnection) url.openConnection();
            urlconn.setRequestMethod("GET");
            urlconn.setReadTimeout(9000);
            urlconn.setConnectTimeout(12000);
            urlconn.connect();
            if (urlconn.getResponseCode() == 200) {
                iS = urlconn.getInputStream();
                jsonResponse = readFromStream(iS);
            } else {
                Log.e("Found Error code: ", urlconn.getResponseCode() + " ! ");
                return null;
            }

        } catch (IOException e) {
            Log.e("makeHttpRequest()", " Invalid URL/connection " + url.getPath());
        } finally {
            if (urlconn != null) {
                urlconn.disconnect();
            }
            if (iS != null) {
                try {
                    iS.close();
                } catch (IOException ioExcept) {
                    Log.e("makeHttpRequest()", "Closing InputStream caused: " + ioExcept);
                }

            }
        }
        return jsonResponse;
    }

    private ArrayList<News> extractDataFromJSON(String JSON_NewsList) {
        ArrayList<News> news;
        try {

            JSONObject rootJSONObject = (new JSONObject(JSON_NewsList)).getJSONObject("response");
            JSONArray newsListArray = rootJSONObject.getJSONArray("results");

            String mTitle = "", mSection = "", mURL = "", mAuthor = "", mImgURL = "", mDate = "";

            // Add 'New' object to 'News' ArrayList
            news = new ArrayList<>();

            if (newsListArray.length() > 0) {
                Log.e("list array Count: ", " at NewsArray: " + String.valueOf(newsListArray.length()));

                JSONObject newsObject;

                for (int i = 0; i < newsListArray.length(); i++) {
                    newsObject = newsListArray.getJSONObject(i);

                    // logic for 'mTitle'
                    if (newsObject.has("webTitle"))
                        mTitle = newsObject.getString("webTitle");

                    // logic for 'mSection'
                    if (newsObject.has("sectionName"))
                        mSection = newsObject.getString("sectionName");

                    // logic for 'mURL'
                    if (newsObject.has("webUrl"))
                        mURL = newsObject.getString("webUrl");

                    // logic for 'mDate'
                    if (newsObject.has("webPublicationDate"))
                        mDate = newsObject.getString("webPublicationDate");

                    JSONObject fieldsObject;
                    fieldsObject = newsObject.getJSONObject("fields");

                    // logic for 'mAuthor'
                    if (fieldsObject.has("byline"))
                        mAuthor = fieldsObject.getString("byline");

                    // logic for 'mImgURL'
                    if (fieldsObject.has("thumbnail"))
                        mImgURL = fieldsObject.getString("thumbnail");
                    news.add(new News(mTitle, mSection, mURL, mAuthor, mImgURL, mDate));
                }
                return news;
            } else
                return null;
        } catch (JSONException e) {
            Log.e("extractData()", "JSONException: " + e);
        }
        return null;
    } // End of extractDataFromJSON

}