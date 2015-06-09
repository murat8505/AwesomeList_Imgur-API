package com.frankegan.awesomelist;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

/**
 * @author frankegan created on 6/5/15.
 */
public class ImageDisplayFragment extends Fragment {
    SubsamplingScaleImageView img;
    OnDismissListener dismisser;
    OnAppBarChangeListener changer;

    public interface OnDismissListener {
        void onDismiss();
    }

    public static ImageDisplayFragment getInstance(String link) {
        ImageDisplayFragment fragment = new ImageDisplayFragment();
        Bundle args = new Bundle();
        args.putString("link", link);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            dismisser = (OnDismissListener) activity;
            changer = (OnAppBarChangeListener) activity;
        } catch (ClassCastException e) {
            Log.e("frankegan", e.toString());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        changer.onVisibilityChanged(OnAppBarChangeListener.GONE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.display_fragment, container, false);
        img = (SubsamplingScaleImageView) v.findViewById(R.id.big_net_img);

        View dismiss = v.findViewById(R.id.dismiss);
        if (dismisser != null)
            dismiss.setOnClickListener(v1 -> dismisser.onDismiss());

        setImage(getArguments().getString("link"));
        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        changer.onVisibilityChanged(OnAppBarChangeListener.VISIBLE);
    }

    public void setImage(String link) {
        Response.Listener<Bitmap> successResponse = (Bitmap response) -> {
            if (img != null)
                img.setImage(ImageSource.bitmap(response));//TODO sample bitmap down to max size
        };
        ImageRequest request = new ImageRequest(link, successResponse, 0, 0, null, (VolleyError error) -> Log.e("volley", error.toString()));
        MyApp.getVolleyRequestQueue().add(request);
    }
}
