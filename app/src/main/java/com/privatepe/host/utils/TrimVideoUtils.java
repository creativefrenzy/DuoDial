package com.privatepe.host.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import androidx.annotation.NonNull;



import com.abedelazizshe.lightcompressorlibrary.CompressionListener;
import com.abedelazizshe.lightcompressorlibrary.VideoCompressor;
import com.abedelazizshe.lightcompressorlibrary.VideoQuality;
import com.abedelazizshe.lightcompressorlibrary.config.Configuration;
import com.abedelazizshe.lightcompressorlibrary.config.SaveLocation;
import com.abedelazizshe.lightcompressorlibrary.config.SharedStorageConfiguration;
import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.FileDataSourceViaHeapImpl;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;
import com.googlecode.mp4parser.authoring.tracks.CroppedTrack;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;



public class TrimVideoUtils {
    private static final String TAG = TrimVideoUtils.class.getSimpleName();

    public TrimVideoUtils() {
    }

    public static void startTrim(@NonNull Context context, @NonNull File src, @NonNull String dst, long startMs, long endMs, @NonNull OnTrimVideoListener callback) throws IOException {
        String timeStamp = (new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)).format(new Date());
        String fileName = "MP4_" + timeStamp + ".mp4";
        String filePath = dst + fileName;
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        Log.d(TAG, "Generated file path " + filePath);
        genVideoUsingMp4Parser(context,src, file, startMs, endMs, callback);
    }

    private static void genVideoUsingMp4Parser(@NonNull Context context,@NonNull File src, @NonNull File dst, long startMs, long endMs, @NonNull OnTrimVideoListener callback) throws IOException {
        Movie movie = MovieCreator.build(new FileDataSourceViaHeapImpl(src.getAbsolutePath()));
        List<Track> tracks = movie.getTracks();
        movie.setTracks(new LinkedList());
        double startTime1 = (double)(startMs / 1000L);
        double endTime1 = (double)(endMs / 1000L);
        boolean timeCorrected = false;
        Iterator var14 = tracks.iterator();

        Track track;
        while(var14.hasNext()) {
            track = (Track)var14.next();
            if (track.getSyncSamples() != null && track.getSyncSamples().length > 0) {
                if (timeCorrected) {
                    throw new RuntimeException("The startTime has already been corrected by another track with SyncSample. Not Supported.");
                }

                startTime1 = correctTimeToSyncSample(track, startTime1, false);
                endTime1 = correctTimeToSyncSample(track, endTime1, true);
                timeCorrected = true;
            }
        }

        var14 = tracks.iterator();

            track = (Track)var14.next();
            long currentSample = 0L;
            double currentTime = 0.0;
            double lastTime = -1.0;
            long startSample1 = -1L;
            long endSample1 = -1L;

            for(int i = 0; i < track.getSampleDurations().length; ++i) {
                long delta = track.getSampleDurations()[i];
                if (currentTime > lastTime && currentTime <= startTime1) {
                    startSample1 = currentSample;
                }

                if (currentTime > lastTime && currentTime <= endTime1) {
                    endSample1 = currentSample;
                }

                lastTime = currentTime;
                currentTime += (double)delta / (double)track.getTrackMetaData().getTimescale();
                ++currentSample;
            }

            movie.addTrack(new AppendTrack(new Track[]{new CroppedTrack(track, startSample1, endSample1)}));


        dst.getParentFile().mkdirs();
        Log.e("checkTripHerelog", ""+dst.getParentFile());
        if (!dst.exists()) {
            try{
                dst.createNewFile();

            }catch (Exception e)
            {
                Log.e("checkTripHereeexc", "catch "+e);

            }

        }

        Container out = (new DefaultMp4Builder()).build(movie);
        FileOutputStream fos = new FileOutputStream(dst);
        FileChannel fc = fos.getChannel();
        out.writeContainer(fc);
        fc.close();
        fos.close();
        /*callback.getResult(Uri.parse(dst.toString()));*/
        startVideoCompression(context,dst, callback);
    }

    private static void startVideoCompression(@NonNull Context context, File src, OnTrimVideoListener callback) {
        // Create the configuration for video compression


        // Start video compression using VideoCompressor
        VideoCompressor.start(
                context,
                new ArrayList<Uri>(){{ add(Uri.fromFile(src)); }},
                false,
                new SharedStorageConfiguration(src.getName()+"newVideoLatest", SaveLocation.movies),
                null,
                new Configuration(
                        VideoQuality.MEDIUM,
                        true,
                        5,
                        false,
                        false,
                        360.0,
                        480.0
                ),
                new CompressionListener() {
                    @Override
                    public void onProgress(int index, float percent) {
                        // Update UI with progress value
                    }

                    @Override
                    public void onStart(int index) {
                        // Compression start
                    }

                    @Override
                    public void onSuccess(int index, long size, String path) {
                        // On Compression success
                        callback.getResult(Uri.parse(path));
                    }

                    @Override
                    public void onFailure(int index, String failureMessage) {
                        // On Failure
                        Log.e(TAG, "Compression failed: " + failureMessage);
                    }

                    @Override
                    public void onCancelled(int index) {
                        // On Cancelled
                    }
                }
        );
    }


    private static double correctTimeToSyncSample(@NonNull Track track, double cutHere, boolean next) {
        double[] timeOfSyncSamples = new double[track.getSyncSamples().length];
        long currentSample = 0L;
        double currentTime = 0.0;

        for(int i = 0; i < track.getSampleDurations().length; ++i) {
            long delta = track.getSampleDurations()[i];
            if (Arrays.binarySearch(track.getSyncSamples(), currentSample + 1L) >= 0) {
                timeOfSyncSamples[Arrays.binarySearch(track.getSyncSamples(), currentSample + 1L)] = currentTime;
            }

            currentTime += (double)delta / (double)track.getTrackMetaData().getTimescale();
            ++currentSample;
        }

        double previous = 0.0;
        double[] var11 = timeOfSyncSamples;
        int var12 = timeOfSyncSamples.length;

        for(int var13 = 0; var13 < var12; ++var13) {
            double timeOfSyncSample = var11[var13];
            if (timeOfSyncSample > cutHere) {
                if (next) {
                    return timeOfSyncSample;
                }

                return previous;
            }

            previous = timeOfSyncSample;
        }

        return timeOfSyncSamples[timeOfSyncSamples.length - 1];
    }
}
