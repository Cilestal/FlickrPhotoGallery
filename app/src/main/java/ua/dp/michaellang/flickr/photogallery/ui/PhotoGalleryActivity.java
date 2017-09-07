package ua.dp.michaellang.flickr.photogallery.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Date: 04.08.2017
 *
 * @author Michael Lang
 */
public class PhotoGalleryActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context) {
        return new Intent(context, PhotoGalleryActivity.class);
    }

    @Override
    protected Fragment createFragment() {
        return PhotoGalleryFragment.newInstance();
    }
}
