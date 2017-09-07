package ua.dp.michaellang.flickr.photogallery.ui.holders;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import ua.dp.michaellang.flickr.photogallery.entity.Photo;
import ua.dp.michaellang.flickr.photogallery.ui.PhotoPageActivity;
import ua.dp.michaellang.flickr.photogallery.utils.ImageUtil;

/**
 * Date: 04.08.2017
 *
 * @author Michael Lang
 */
public class PhotoHolder extends LoadmoreHolder<Photo> {

    public PhotoHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBind(final Fragment fragment, final Photo photo) {
        ImageUtil.loadImage(fragment, (ImageView) itemView, photo.getUrlS());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = fragment.getContext();
                PhotoPageActivity.start(context, photo.getPhotoPageUri());
            }
        });
    }
}
