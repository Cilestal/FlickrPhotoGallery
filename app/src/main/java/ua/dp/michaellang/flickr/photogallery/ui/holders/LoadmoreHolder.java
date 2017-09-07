package ua.dp.michaellang.flickr.photogallery.ui.holders;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import ua.dp.michaellang.flickr.photogallery.R;

/**
 * Date: 04.08.2017
 *
 * @author Michael Lang
 */
public class LoadmoreHolder<T> extends RecyclerView.ViewHolder {

    public LoadmoreHolder(View itemView) {
        super(itemView);
    }

    public LoadmoreHolder(View itemView, View.OnClickListener retryListener) {
        super(itemView);
        itemView.findViewById(R.id.item_retry_button).setOnClickListener(retryListener);
    }

    public void onBind(Fragment fragment, T data) {
        //stub
    }
}
