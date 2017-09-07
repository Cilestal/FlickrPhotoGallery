package ua.dp.michaellang.flickr.photogallery.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import ua.dp.michaellang.flickr.photogallery.R;
import ua.dp.michaellang.flickr.photogallery.utils.ImageUtil;

/**
 * Date: 03.09.2017
 *
 * @author Michael Lang
 */
public class PhotoFragment extends DialogFragment {
    private static final String ARG_URL = "ARG_URL";
    private static final String ARG_PAGE_URI = "ARG_PAGE_URI";

    private String mUrl;
    private Uri mPageUri;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUrl = getArguments().getString(ARG_URL);
            mPageUri = getArguments().getParcelable(ARG_PAGE_URI);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setPositiveButton(R.string.open_page, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PhotoPageActivity.start(getContext(), mPageUri);
                    }
                })
                .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_photo, container, false);

        final ImageView img = (ImageView) view
                .findViewById(R.id.dialog_photo_image_view);
        final ProgressBar pb = (ProgressBar) view
                .findViewById(R.id.dialog_photo_progress_bar);

        ImageUtil.loadImage(getContext(), img, mUrl, new ImageUtil.ImageLoadedCallback() {
            @Override
            public void onImageLoaded() {
                pb.setVisibility(View.GONE);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AlertDialog) getDialog()).setView(view);
    }

    public static PhotoFragment newInstance(String url, Uri pageUri) {
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        args.putParcelable(ARG_PAGE_URI, pageUri);
        PhotoFragment fragment = new PhotoFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
