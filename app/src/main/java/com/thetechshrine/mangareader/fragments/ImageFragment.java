package com.thetechshrine.mangareader.fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.thetechshrine.mangareader.R;
import com.thetechshrine.mangareader.utils.Constants;
import com.thetechshrine.mangareader.utils.Event;
import com.thetechshrine.mangareader.utils.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends Fragment {

    private String url;
    private Context context;

    private PhotoView picture;


    public ImageFragment() {
        // Required empty public constructor
    }

    public static ImageFragment newInstance(String url) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.URL, url);

        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(Constants.URL)) {
            url = bundle.getString(Constants.URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image, container, false);

        picture = rootView.findViewById(R.id.picture);

        Picasso.get().load(url)
            .networkPolicy(NetworkPolicy.OFFLINE)
            .into(picture, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    loadFromNetwork();
                }
            });

        subscribeToBus();

        return rootView;
    }

    private void setScaleType(ImageView.ScaleType scaleType) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> picture.setScaleType(scaleType));
        }
    }

    private void subscribeToBus() {
        EventBus.subscribe(EventBus.SUBJECT_IMAGE_FRAGMENT, this, o -> {
            if (o instanceof Event) {
                Event event = (Event) o;
                if (event.getData() != null && event.getSubject() != 0) {
                    switch (event.getSubject()) {
                        case Event.SUBJECT_IMAGE_FRAGMENT_SCALE_TYPE_FIT_CENTER:
                            setScaleType(ImageView.ScaleType.FIT_CENTER);
                            break;
                        case Event.SUBJECT_IMAGE_FRAGMENT_SCALE_TYPE_CENTER_CROP:
                            setScaleType(ImageView.ScaleType.CENTER_CROP);
                            break;
                    }
                }
            }
        });
    }

    private void loadFromNetwork() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> Picasso.get().load(url)
                    .into(picture));
        }
    }
}
