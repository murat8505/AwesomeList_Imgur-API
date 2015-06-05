package com.frankegan.awesomelist;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.NetworkImageView;

/**
 * @author frankegan created on 6/5/15.
 */
public class ImageDisplayFragment extends Fragment {
    NetworkImageView img;

    public static ImageDisplayFragment getInstance(String link){
        ImageDisplayFragment fragment = new ImageDisplayFragment();
        Bundle args = new Bundle();
        args.putString("link", link);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.display_fragment, container, false);
        img = (NetworkImageView) v.findViewById(R.id.big_net_img);
        setImage(getArguments().getString("link"));

        return v;
    }

    public void setImage(String link){
        if (img != null)
            img.setImageUrl(link, MyApp.getImageLoader());
    }
}
