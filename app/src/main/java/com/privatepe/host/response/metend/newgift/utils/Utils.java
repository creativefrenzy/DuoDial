package com.privatepe.host.response.metend.newgift.utils;

import static com.bumptech.glide.load.engine.DiskCacheStrategy.ALL;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.privatepe.host.R;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;

import java.net.MalformedURLException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;


public class Utils {

    public static void imageLoaderSvg(Context context, AppCompatImageView imageView, String imageUrl) {

    /*    RequestBuilder<PictureDrawable> requestBuilder =
                GlideApp.with(context)
                        .as(PictureDrawable.class)
                        //.placeholder(R.drawable.dummy_photo)
                        .centerInside()
                        .fitCenter()
                        .error(R.drawable.ic_baseline_image_not_supported_24)
                        .transition(withCrossFade())
                        .listener(new SvgSoftwareLayerSetter());
        Uri uri = Uri.parse(imageUrl);
        requestBuilder.load(uri).into(imageView);*/
    }

    public static void imageLoaderCircleImage(Context context, CircleImageView imageView, String imageUrl) {

        Glide.with(context.getApplicationContext()).load(imageUrl).thumbnail(0.4f).centerInside().fitCenter().diskCacheStrategy(ALL).into(imageView);

    }

    public static void imageLoaderView(Context context, AppCompatImageView imageView, String imageUrl) {

        Glide.with(context.getApplicationContext())
                .load(imageUrl)
                .thumbnail(0.4f)
                .centerInside()
                .fitCenter()
                .error(R.drawable.ic_baseline_image_not_supported_24)
                .diskCacheStrategy(ALL)
                .into(imageView);

    }

    public static void svgaImageViewFromUrl(String url, SVGAImageView svgaImageView) {
        SVGAParser svgaParser = new SVGAParser(svgaImageView.getContext());
        try {
            svgaParser.decodeFromURL(new URL(url), new SVGAParser.ParseCompletion() {
                @Override
                public void onComplete(@NonNull SVGAVideoEntity svgaVideoEntity) {

                    SVGADrawable drawable = new SVGADrawable(svgaVideoEntity);
                    svgaImageView.setImageDrawable(drawable);
                    svgaImageView.startAnimation();
                }

                @Override
                public void onError() {
                    Log.e("SVGAParse", "onError: ");
                }
            }, null);

        } catch (MalformedURLException e) {
            e.printStackTrace();

        }
    }



    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }





}
