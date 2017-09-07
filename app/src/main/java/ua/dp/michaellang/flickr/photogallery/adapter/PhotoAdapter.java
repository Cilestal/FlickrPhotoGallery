package ua.dp.michaellang.flickr.photogallery.adapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ua.dp.michaellang.flickr.photogallery.R;
import ua.dp.michaellang.flickr.photogallery.entity.Photo;
import ua.dp.michaellang.flickr.photogallery.ui.holders.LoadmoreHolder;
import ua.dp.michaellang.flickr.photogallery.ui.holders.PhotoHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 04.08.2017
 *
 * @author Michael Lang
 */
public class PhotoAdapter extends RecyclerView.Adapter<LoadmoreHolder<Photo>>
        implements View.OnClickListener{
    public static final int VIEW_TYPE_LOAD_MORE = 555;
    public static final int VIEW_TYPE_RETRY = 666;

    private List<Photo> mPhotos;
    private Fragment mFragment;

    private View.OnClickListener mOnRetryButtonClickListener;

    private boolean mIsLastPage = false;
    private boolean mIsError = false;

    public PhotoAdapter(Fragment fragment, final View.OnClickListener onRetryButtonClickListener) {
        mPhotos = new ArrayList<>();
        mFragment = fragment;
        mOnRetryButtonClickListener = onRetryButtonClickListener;
    }


    public void setPhotos(List<Photo> photos) {
        mPhotos = photos;
    }

    public void addPhotos(List<Photo> photos) {
        mPhotos.addAll(photos);
    }

    @Override
    public LoadmoreHolder<Photo> onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mFragment.getContext());
        View view;

        if (viewType == VIEW_TYPE_RETRY) {
            view = inflater.inflate(R.layout.item_retry, parent, false);
            return new LoadmoreHolder<>(view, this);
        } else if (viewType == VIEW_TYPE_LOAD_MORE) {
            view = inflater.inflate(R.layout.item_loadmore, parent, false);
            return new LoadmoreHolder<>(view);
        } else {
            view = inflater.inflate(R.layout.item_photo, parent, false);
            return new PhotoHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(LoadmoreHolder<Photo> holder, int position) {
        if (getItemViewType(position) == 0) {
            holder.onBind(mFragment, mPhotos.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (!mIsLastPage && position == getItemCount() - 1) {
            if (mIsError) {
                return VIEW_TYPE_RETRY;
            } else {
                return VIEW_TYPE_LOAD_MORE;
            }
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        int itemCount = mPhotos.size();
        return !mIsLastPage ? itemCount + 1 : itemCount;
    }

    public String getLastPhotoId(){
        return mPhotos.size() > 0 ? mPhotos.get(0).getId() : "";
    }

    public boolean isLastPage() {
        return mIsLastPage;
    }

    public void setLastPage(boolean lastPage) {
        mIsLastPage = lastPage;
    }

    public boolean isError() {
        return mIsError;
    }

    public void setError(boolean error) {
        mIsError = error;
    }

    @Override
    public void onClick(View v) {
        mIsError = false;
        if(mOnRetryButtonClickListener != null){
            mOnRetryButtonClickListener.onClick(v);
        }
        notifyLastElementChanged();
    }

    public void notifyLastElementChanged(){
        this.notifyItemChanged(getItemCount() - 1);
    }

    public void clearPhotos() {
        mPhotos.clear();
        this.notifyDataSetChanged();
    }
}
