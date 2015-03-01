package com.example.kelei.gridimagesearch.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;
import com.example.kelei.gridimagesearch.R;
import com.example.kelei.gridimagesearch.adapter.ImageResultsAdapter;
import com.example.kelei.gridimagesearch.fragment.EditSettingsDialog;
import com.example.kelei.gridimagesearch.model.ImageResult;
import com.example.kelei.gridimagesearch.utility.EndlessScrollListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class SearchActivity extends ActionBarActivity implements EditSettingsDialog.EditSettingsDialogListener {
    @InjectView(R.id.gvResults) StaggeredGridView gvResults;
    private ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter aImageResults;
    AsyncHttpClient client;
    private String _query;
    private String _imageSize;
    private String _colorFilter;
    private String _imageType;
    private String _siteFilter;

    private static int PAGE_SIZE = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.inject(this);
        setupViews();
        client = new AsyncHttpClient();
        imageResults = new ArrayList();
        aImageResults = new ImageResultsAdapter(this, imageResults);
        gvResults.setAdapter(aImageResults);
    }

    public void setupViews() {
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(SearchActivity.this, ImageDisplayActivity.class);

                ImageResult result = imageResults.get(position);
                i.putExtra("result", result);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                onImageSearch(query, 0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            EditSettingsDialog settingsDialog = EditSettingsDialog.newInstance(
                _imageSize,
                _colorFilter,
                _imageType,
                _siteFilter);
            settingsDialog.show(getSupportFragmentManager(), "fragment_settings");
        }
        return super.onOptionsItemSelected(item);
    }

    public void onImageSearch(String query, final int page) {
        if (!isNetworkAvailable()) {
            Toast toast = Toast.makeText(SearchActivity.this,
                "Network not available.  Please check that you are connected to the internet",
                Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        _query = query;
        String url = "https://ajax.googleapis.com/ajax/services/search/images";
        RequestParams params = new RequestParams();
        params.add("q", query);
        params.add("v", "1.0");
        params.add("start", String.valueOf(page * PAGE_SIZE));
        params.add("rsz", String.valueOf(PAGE_SIZE));
        params.add("imgsz", _imageSize);
        params.add("imgcolor", _colorFilter);
        params.add("imgtype", _imageType);
        params.add("as_sitesearch", _siteFilter);

        if (page == 0) {
            gvResults.setOnScrollListener(new EndlessScrollListener() {
                @Override
                public void onLoadMore(int page, int totalItemsCount) {
                    onImageSearch(_query, page);
                }
            });
        }

        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONObject responseData = response.getJSONObject("responseData");
                    JSONArray imageResultsArray = responseData.getJSONArray("results");
                    if (page == 0) {
                        aImageResults.clear();
                    }
                    aImageResults.addAll(ImageResult.fromJSONArray(imageResultsArray));
                } catch (JSONException e) {
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,
                Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast toast = Toast.makeText(SearchActivity.this,
                    "Failed to fetch search results.", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    @Override
    public void onFinishEditDialog(String imageSize, String colorFilter, String imageType, String siteFilter) {
        _imageSize = imageSize;
        _colorFilter = colorFilter;
        _imageType = imageType;
        _siteFilter = siteFilter;

        onImageSearch(_query, 0);
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
            = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
