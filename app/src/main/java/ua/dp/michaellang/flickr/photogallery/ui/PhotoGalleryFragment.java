package ua.dp.michaellang.flickr.photogallery.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.*;
import android.widget.TextView;
import timber.log.Timber;
import ua.dp.michaellang.flickr.photogallery.R;
import ua.dp.michaellang.flickr.photogallery.adapter.PhotoAdapter;
import ua.dp.michaellang.flickr.photogallery.entity.Photo;
import ua.dp.michaellang.flickr.photogallery.entity.Photos;
import ua.dp.michaellang.flickr.photogallery.presenter.PhotoGalleryPresenter;
import ua.dp.michaellang.flickr.photogallery.presenter.PhotoGalleryPresenterImpl;
import ua.dp.michaellang.flickr.photogallery.utils.SPUtil;
import ua.dp.michaellang.flickr.photogallery.view.PhotoGalleryView;

import java.util.List;

import static ua.dp.michaellang.flickr.photogallery.Constants.PAGE_ITERATOR_SIZE;

/**
 * Date: 04.08.2017
 *
 * @author Michael Lang
 */
public class PhotoGalleryFragment extends Fragment
        implements PhotoGalleryView {
    private static final String TAG = "PhotoGalleryFragment";

    private TextView mEmptyTextView;

    private PhotoAdapter mPhotoAdapter;
    private GridLayoutManager mLayoutManager;

    private PhotoGalleryPresenter mPresenter;

    protected boolean mIsLoading = false;
    protected boolean mIsLastPage = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        View.OnClickListener mRetryButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNext();
            }
        };

        mPhotoAdapter = new PhotoAdapter(this, mRetryButtonClickListener);
        mPresenter = new PhotoGalleryPresenterImpl(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fragment_photo_gallery_recycler_view);
        mEmptyTextView = (TextView) view.findViewById(R.id.fragment_photo_gallery_text_view);

        int cols = getResources().getInteger(R.integer.fragment_photo_gallery_rv_cols);
        mLayoutManager = new GridLayoutManager(getContext(), cols);
        mLayoutManager.setSpanSizeLookup(getSpanSizeLookup(cols));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mPhotoAdapter);
        recyclerView.addOnScrollListener(recyclerViewOnScrollListener);

        loadNext();

        return view;
    }

    protected final RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (mPresenter != null && mLayoutManager != null) {
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

                if (!mIsLoading && !mIsLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= PAGE_ITERATOR_SIZE) {
                        loadNext();
                    }
                }
            }
        }
    };

    public static PhotoGalleryFragment newInstance() {
        return new PhotoGalleryFragment();
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    @Override
    public void onLoadingStart() {
        mIsLoading = true;
    }

    @Override
    public void onStartNewLoad() {
        mEmptyTextView.setVisibility(View.GONE);
        mPhotoAdapter.clearPhotos();
        mPhotoAdapter.setLastPage(false);
    }

    @Override
    public void onLoadComplete() {
        mIsLoading = false;
        mPhotoAdapter.setError(false);
    }

    @Override
    public void onError(Throwable e) {
        Timber.e(e);
        mPhotoAdapter.setError(true);
        mPhotoAdapter.notifyLastElementChanged();
    }

    @Override
    public void onPhotosLoaded(Photos photos, boolean hasMoreData) {
        mIsLastPage = !hasMoreData;
        List<Photo> photo = photos.getPhoto();
        Timber.d("Loaded %d items.", photo.size());

        int itemCount = mPhotoAdapter.getItemCount();

        mPhotoAdapter.setLastPage(mIsLastPage);
        mPhotoAdapter.addPhotos(photo);

        if (mPhotoAdapter.getItemCount() > 0) {
            SPUtil.setLastResult(getContext(), mPhotoAdapter.getLastPhotoId());
            mEmptyTextView.setVisibility(View.GONE);
            mPhotoAdapter.notifyItemRangeChanged(itemCount, itemCount + photo.size() - 1);
            //mPhotoAdapter.notifyDataSetChanged();
        } else {
            mEmptyTextView.setVisibility(View.VISIBLE);
        }
    }

    private void loadNext() {
        String storedQuery = SPUtil.getStoredQuery(getActivity());
        if (storedQuery == null) {
            mPresenter.loadRecentPhotos();
        } else {
            mPresenter.search(storedQuery);
        }
    }

    @NonNull
    private GridLayoutManager.SpanSizeLookup getSpanSizeLookup(final int spanCount) {
        return new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = mPhotoAdapter.getItemViewType(position);
                return viewType != 0 ? spanCount : 1;
            }
        };
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.fragment_photo_gallery, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Timber.tag(TAG).d("QueryTextSubmit: " + s);
                mPresenter.reset();
                mPresenter.search(s);
                SPUtil.setStoredQuery(getActivity(), s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Timber.tag(TAG).d("QueryTextChange: " + s);
                return false;
            }
        });

        String storedQuery = SPUtil.getStoredQuery(getActivity());
        searchView.setQuery(storedQuery, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_clear:
                SPUtil.setStoredQuery(getActivity(), null);
                mPresenter.reset();
                mPresenter.loadRecentPhotos();
                return true;
            case R.id.menu_item_settings:
                SettingsActivity.start(getContext());
                return true;
            case R.id.menu_refresh:
                mPresenter.reset();
                loadNext();
                return true;
            case R.id.menu_map:
                LocatrActivity.start(getContext());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
