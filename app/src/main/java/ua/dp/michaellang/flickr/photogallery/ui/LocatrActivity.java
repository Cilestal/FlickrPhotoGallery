package ua.dp.michaellang.flickr.photogallery.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

/**
 * Date: 31.08.2017
 *
 * @author Michael Lang
 */
public class LocatrActivity extends SingleFragmentActivity {
    private static final int REQUEST_ERROR = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showBackButton();
    }

    @Override
    protected void onResume() {
        super.onResume();

        int response = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (response != ConnectionResult.SUCCESS) {
            GoogleApiAvailability.getInstance()
                    .getErrorDialog(this, response, REQUEST_ERROR)
                    .show();
        }
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, LocatrActivity.class);
    }

    public static void start(Context context) {
        Intent starter = newIntent(context);
        context.startActivity(starter);
    }

    @Override
    protected Fragment createFragment() {
        return LocatrFragment.newInstance();
    }
}
