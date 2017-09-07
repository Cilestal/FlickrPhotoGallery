package ua.dp.michaellang.flickr.photogallery.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import timber.log.Timber;
import ua.dp.michaellang.flickr.photogallery.Constants;

import static ua.dp.michaellang.flickr.photogallery.Constants.PERM_PRIVATE;

/**
 * Date: 29.08.2017
 *
 * @author Michael Lang
 */
public class BaseActivity extends AppCompatActivity {

    private BroadcastReceiver mOnShowNotification = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Timber.i("Canceling notification.");
            abortBroadcast();
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(Constants.ACTION_SHOW_NOTIFICATION);
        registerReceiver(mOnShowNotification, filter, PERM_PRIVATE, null);
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterReceiver(mOnShowNotification);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    protected final void showBackButton(){
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
