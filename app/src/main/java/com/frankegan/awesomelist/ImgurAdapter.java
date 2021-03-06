package com.frankegan.awesomelist;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

/**
 * @author frankegan created on 6/2/15.
 */
public class ImgurAdapter extends RecyclerView.Adapter<ImgurAdapter.ViewHolder> {
    static ArrayList<ImgurData> myDataset = new ArrayList<>();
    static OnDisplayListener mListener;

    public interface OnDisplayListener {
        void onDisplay(ImgurData data);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public NetworkImageView networkImageView;

        public ViewHolder(View root) {
            super(root);
            root.setOnClickListener(v -> {
                getDisplayListener().onDisplay(myDataset.get(getPosition()));
                Log.i("frankegan", "element clicked");
            });
            title = (TextView) root.findViewById(R.id.title_text);
            networkImageView = (NetworkImageView) root.findViewById(R.id.net_img);
        }

        void setText(String titleString) {
            if (title != null)
                title.setText(titleString);
        }

        void setColor(int color) {
            title.setBackgroundColor(color);
        }

        void setImageUrl(String url) {
            if (networkImageView != null) {
                networkImageView.setImageUrl(url, MyApp.getImageLoader());
            }
        }

        TextView getTitle() {
            return title;
        }

        NetworkImageView getImage() {
            return networkImageView;
        }
    }

    @Override
    public ImgurAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tile_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.setText(myDataset.get(position).getTitle());
        holder.setImageUrl(myDataset.get(position).getLink());
//        Response.Listener<Bitmap> successResponse =
//                response -> {Palette.generateAsync(response, new Palette.PaletteAsyncListener() {
//                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//                @Override
//                public void onGenerated(Palette palette) {
//                    Integer colorVib = palette.getVibrantColor(Color.parseColor("white"));
//                    holder.setColor(colorVib);
//                }
//            });};
//        ImageRequest request = new ImageRequest(myDataset.get(position).getLink(), successResponse, 720, 720,
//                null, e -> Log.e("volley", e.toString()));
//        MyApp.getVolleyRequestQueue().add(request);
    }

    @Override
    public int getItemCount() {
        return myDataset.size();
    }

    public void setDataset(ArrayList<ImgurData> dataset) {
        myDataset.addAll(dataset);
    }

    public void setOnDisplayListener(OnDisplayListener listener) {
        mListener = listener;
    }

    public static OnDisplayListener getDisplayListener() {
        return mListener;
    }
}
