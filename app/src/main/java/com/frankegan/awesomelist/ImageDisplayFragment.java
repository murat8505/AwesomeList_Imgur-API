package com.frankegan.awesomelist;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Config;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    TextView title, desc;
    OnDismissListener dismisser;
    OnAppBarChangeListener changer;
    private final int MAX_IMG_SIZE = 1080;

    public interface OnDismissListener {
        void onDismiss();
    }

    public static ImageDisplayFragment getInstance(ImgurData data) {
        ImageDisplayFragment fragment = new ImageDisplayFragment();
        Bundle args = new Bundle();
        args.putString("link", data.getLink());
        args.putString("title", data.getTitle());
        args.putString("description", data.getDescription());
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
        //img.setMinimumHeight();
        title = (TextView) v.findViewById(R.id.big_title);
        title.setText(getArguments().getString("title"));//TODO make keys constants
        desc = (TextView) v.findViewById(R.id.desc_text);
        desc.setText(getArguments().getString("description"));

        View dismiss = v.findViewById(R.id.dismiss);//TODO design better UX
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
        Response.Listener<Bitmap> successResponse = response -> {
            if (img != null)
                img.setImage(ImageSource.bitmap(response));
        };
        ImageRequest request = new ImageRequest(link, successResponse, MAX_IMG_SIZE, MAX_IMG_SIZE,
                null, e -> Log.e("volley", e.toString()));
        MyApp.getVolleyRequestQueue().add(request);
    }
}
