/*
 * Copyright 2016 Dominion Enterprises. All Rights Reserved.
 */

package com.dominion.mobile.ddsrefactortest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.dominion.mobile.ddsrefactortest.adapters.UsersAdapter;
import com.dominion.mobile.ddsrefactortest.api.UsersRequest;
import com.dominion.mobile.ddsrefactortest.api.UsersResponse;
import com.dominion.mobile.ddsrefactortest.api.entities.User;
import com.octo.android.robospice.Jackson2SpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity that lists all current users
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public class UsersActivity extends Activity
{
    public UsersActivity()
    {
        spiceManager = new SpiceManager( Jackson2SpringAndroidSpiceService.class );
    }
    
    @Override
    protected void onCreate( final Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        
        setContentView( R.layout.activity_users );
        
        adapter = new UsersAdapter( this, users );
        
        ListView listView = (ListView) findViewById( R.id.users );
        listView.setAdapter( adapter );
        listView.setOnItemClickListener( new OnItemClickListener()
        {
            @Override
            public void onItemClick( AdapterView<?> adapterView, View view, int position, long id )
            {
                Intent intent = new Intent( UsersActivity.this, UserPostsActivity.class );
                intent.putExtra( UserPostsActivity.EXTRA_USER, users.get( position ) );
                startActivity( intent );
            }
        } );
        
        loadingIndicator = (ProgressBar) findViewById( R.id.loading_indicator );
    }
    
    @Override
    protected void onStart()
    {
        super.onStart();
        
        spiceManager.start( this );
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        
        loadingIndicator.setVisibility( View.VISIBLE );
        
        spiceManager.execute( new UsersRequest(), CACHEKEY_USERS, DurationInMillis.ONE_MINUTE, new UsersRequestListener() );
    }
    
    @Override
    protected void onStop()
    {
        spiceManager.shouldStop();
        
        super.onStop();
    }
    
    public class UsersRequestListener implements RequestListener<UsersResponse>
    {
        @Override
        public void onRequestSuccess( final UsersResponse response )
        {
            users.addAll( response );
            
            adapter.notifyDataSetChanged();
            
            loadingIndicator.setVisibility( View.INVISIBLE );
        }
        
        @Override
        public void onRequestFailure( final SpiceException spiceException )
        {
            loadingIndicator.setVisibility( View.INVISIBLE );
            
            new AlertDialog.Builder( UsersActivity.this ).setTitle( R.string.error ).setMessage( R.string.something_went_wrong ).show();
        }
    }
    
    private static final String CACHEKEY_USERS = "cache-key-users";
    private UsersAdapter adapter;
    private ProgressBar loadingIndicator;
    private final SpiceManager spiceManager;
    private final List<User> users = new ArrayList<>();
}