package com.dominion.mobile.ddsrefactortest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.dominion.mobile.ddsrefactortest.adapters.UsersAdapter;
import com.dominion.mobile.ddsrefactortest.api.UsersRequest;
import com.dominion.mobile.ddsrefactortest.api.UsersResponse;
import com.dominion.mobile.ddsrefactortest.api.entities.User;
import com.dominion.mobile.ddsrefactortest.rest.UsersClient;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rsakhinala on 1/16/2017.
 */
public class MyUsersActivity extends Activity {

    private static final String CACHEKEY_USERS = "cache-key-users";
    private UsersAdapter adapter;
    private ProgressBar loadingIndicator;
    private final List<User> users = new ArrayList<>();

    @Inject
    UsersClient usersClient;

    @Override
    protected void onCreate( final Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );

        setContentView( R.layout.activity_users );

        // Inject Dependencies here
        ((ApplicationModule) getApplication()).getComponent().inject(this);

        adapter = new UsersAdapter( this, users);

        ListView listView = (ListView) findViewById( R.id.users );
        listView.setAdapter( adapter );
        listView.setOnItemClickListener( new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id )
            {
                Intent intent = new Intent(MyUsersActivity.this, MyUserPostsActivity.class );
                intent.putExtra( MyUserPostsActivity.EXTRA_USER, users.get( position ) );
                startActivity( intent );
            }
        } );

        loadingIndicator = (ProgressBar) findViewById( R.id.loading_indicator );
    }


    @Override
    protected void onResume(){
        super.onResume();

        loadingIndicator.setVisibility( View.VISIBLE );

        getUsersFromServer();
    }

    public void getUsersFromServer(){
        Call<UsersResponse> getAllUsers = usersClient.getUsers();
        getAllUsers.enqueue(new Callback<UsersResponse>() {
            @Override
            public void onResponse(Call<UsersResponse> call, Response<UsersResponse> response) {

                // reset the users array, which was missing in the original code provided
                users.clear();

                users.addAll( response.body() );

                adapter.notifyDataSetChanged();

                loadingIndicator.setVisibility( View.INVISIBLE );
            }

            @Override
            public void onFailure(Call<UsersResponse> call, Throwable t) {
                loadingIndicator.setVisibility( View.INVISIBLE );

                new AlertDialog.Builder( MyUsersActivity.this ).setTitle( R.string.error ).setMessage( R.string.something_went_wrong ).show();
            }
        });
    }
}
