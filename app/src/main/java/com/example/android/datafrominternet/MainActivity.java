package com.example.android.datafrominternet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.datafrominternet.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText mSearchBoxEditText;
    TextView mUrlDisplayTextView;
    TextView mSearchResultsTextView;
    TextView mErrorMessageDisplay;
   // TextView errorMessageTextView;
    ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSearchBoxEditText = (EditText) findViewById(R.id.et_search_box);
        mUrlDisplayTextView = (TextView) findViewById(R.id.tv_url_display);
        mSearchResultsTextView = (TextView) findViewById(R.id.tv_github_search_results_json);
        mErrorMessageDisplay=(TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

    }

  private void makeGithubSearchQuery() {
        String githubQuery = mSearchBoxEditText.getText().toString();
        URL githubSearchUrl = NetworkUtils.buildUrl(githubQuery);
        mUrlDisplayTextView.setText(githubSearchUrl.toString());
        String githubSearchResults = null;
        try {
            githubSearchResults = NetworkUtils.getResponseFromHttpUrl(githubSearchUrl);
            mSearchResultsTextView.setText(githubSearchResults);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // TODO (4) Create a new GithubQueryTask and call its execute method, passing in the url to query
        new GithubQueryTask().execute(githubSearchUrl);
    }

    private void showJsonDataView()
    {
        //first, make sure the error is invisible
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);

        //then, make sure the JSON data is visible
        mSearchResultsTextView.setVisibility(View.VISIBLE);

    }
// COMPLETED (15) Create a method called showErrorMessage to show the error and hide the data
    /**
     * This method will make the error message visible and hide the JSON
     * View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showErrorMessage()
    {
        //first, hide the currently visible data
        mSearchResultsTextView.setVisibility(View.INVISIBLE);

        //Then, show the error
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }


    // TODO (1) Create a class called GithubQueryTask that extends AsyncTask<URL, Void, String>
    public class GithubQueryTask extends AsyncTask<URL, Void, String> {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String githubSearchResults = null;
            try {
                githubSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return githubSearchResults;
        }

        @Override
        protected void onPostExecute(String githubSearchResults) {
            if (githubSearchResults != null && !githubSearchResults.equals("")) {
                mSearchResultsTextView.setText(githubSearchResults);
            }
            else
            {
                //call showErrorMessage if the result is null in onPostExecute
                showErrorMessage();
            }
        }
    }
    // TODO (2) Override the doInBackground method to perform the query. Return the results. (Hint: You've already written the code to perform the query)
    // TODO (3) Override onPostExecute to display the results in the TextView

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemThatWasSelected = item.getItemId();
        if (menuItemThatWasSelected == R.id.action_search) {
makeGithubSearchQuery();
return true;
        }
     return super.onOptionsItemSelected(item);
    }

}