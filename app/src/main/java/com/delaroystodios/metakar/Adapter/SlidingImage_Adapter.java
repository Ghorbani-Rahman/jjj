package com.delaroystodios.metakar.Adapter;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.delaroystodios.metakar.Model.ImageModel;
import com.delaroystodios.metakar.R;

import java.util.ArrayList;

public class SlidingImage_Adapter extends PagerAdapter {


    private ArrayList<ImageModel> imageModelArrayList;
    private LayoutInflater inflater;
    private Context context;
    private FragmentManager fragmentManager;


    public SlidingImage_Adapter(Context context, ArrayList<ImageModel> imageModelArrayList , FragmentManager fragmentManager) {
        this.context = context;
        this.imageModelArrayList = imageModelArrayList;
        inflater = LayoutInflater.from(context);
        this.fragmentManager = fragmentManager;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imageModelArrayList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        final View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.image);

        Glide.with(context)
                .load(imageModelArrayList.get(position).getImage_drawable())
                .error(R.drawable.metakar)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        // log exception
                        Log.e("TAG", "Error loading image", e);
                        return false; // important to return false so the error placeholder can be placed
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                   //     ShowResultQrCode.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imageView);

    view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}