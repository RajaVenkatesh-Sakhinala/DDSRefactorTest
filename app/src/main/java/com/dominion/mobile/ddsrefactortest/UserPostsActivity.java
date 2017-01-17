/*
 * Copyright 2016 Dominion Enterprises. All Rights Reserved.
 */

package com.dominion.mobile.ddsrefactortest;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.dominion.mobile.ddsrefactortest.adapters.UserPostsAdapter;
import com.dominion.mobile.ddsrefactortest.api.UserPostsRequest;
import com.dominion.mobile.ddsrefactortest.api.UserPostsResponse;
import com.dominion.mobile.ddsrefactortest.api.entities.Post;
import com.dominion.mobile.ddsrefactortest.api.entities.User;
import com.octo.android.robospice.Jackson2SpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity that lists
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public class UserPostsActivity extends Activity
{
    public UserPostsActivity()
    {
        spiceManager = new SpiceManager( Jackson2SpringAndroidSpiceService.class );
    }
    
    @Override
    protected void onCreate( final Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        
        setContentView( R.layout.activity_user_posts );
        
        adapter = new UserPostsAdapter( this, posts );
        
        ListView listView = (ListView) findViewById( R.id.posts );
        listView.setAdapter( adapter );
        
        loadingIndicator = (ProgressBar) findViewById( R.id.loading_indicator );
        
        user = (User) getIntent().getExtras().get( EXTRA_USER );
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
        
        spiceManager.execute( new UserPostsRequest( user.getId() ),
                              CACHEKEY_USERS_POST,
                              DurationInMillis.ONE_MINUTE,
                              new UserPostsRequestListener() );
    }
    
    @Override
    protected void onStop()
    {
        spiceManager.shouldStop();
        
        super.onStop();
    }
    
    public class UserPostsRequestListener implements RequestListener<UserPostsResponse>
    {
        @Override
        public void onRequestSuccess( final UserPostsResponse response )
        {
            posts.addAll( response );
            
            adapter.notifyDataSetChanged();
            
            loadingIndicator.setVisibility( View.INVISIBLE );
        }
        
        @Override
        public void onRequestFailure( final SpiceException spiceException )
        {
            loadingIndicator.setVisibility( View.INVISIBLE );
            
            new AlertDialog.Builder( UserPostsActivity.this ).setTitle( R.string.error ).setMessage( R.string.something_went_wrong ).show();
        }
    }
    
    public static final String EXTRA_USER = "extra_user";
    private static final String CACHEKEY_USERS_POST = "cache-key-user-posts";
    
    private UserPostsAdapter adapter;
    private User user;
    private ProgressBar loadingIndicator;
    private final SpiceManager spiceManager;
    private final List<Post> posts = new ArrayList<>();
}