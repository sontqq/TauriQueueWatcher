package com.sontme.esp.tauriqueuewatcher;

import android.Manifest;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Vibrator;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import cz.msebera.android.httpclient.Header;

import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

import android.provider.Settings.Secure;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainActivity extends AppCompatActivity {

    //public ProgressDialog pDialog;
    //public ProgressDialog mProgressDialog;
    public static final int progress_bar_type = 0;


    public static String globalresult;
    public static String arenaHorde;
    public static String arenaAlliance;
    public static String bgHorde;
    public static String bgAlliance;
    public static String bgTime;
    public static String arenaTime;
    public static int x;
    public static int y;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logUser();
        hasPermission();

        Tracker t = ((Analytics) this.getApplication()).getTracker(Analytics.TrackerName.APP_TRACKER);
        t.setScreenName("MainActivity");
        t.enableAdvertisingIdCollection(true);

        t.send(new HitBuilders.AppViewBuilder().build());

        final TextView bgq = (TextView) findViewById(R.id.bgq);
        final TextView bgq2 = (TextView) findViewById(R.id.bgq2);
        final TextView arenaT = (TextView) findViewById(R.id.arenaTime);
        final TextView bgT = (TextView) findViewById(R.id.bgTime);
        final TextView arena_alli = (TextView) findViewById(R.id.arena_alli);
        final TextView arena_horde = (TextView) findViewById(R.id.arena_horde);
        final TextView txtBuild = (TextView) findViewById(R.id.txtBuild);
        final Switch sw = (Switch) findViewById(R.id.switch1);
        checkUpdate(this.getBaseContext(), "https://sont.sytes.net/version.txt");
        String ver = String.valueOf(BuildConfig.VERSION_CODE);
        txtBuild.setText("Build: " + ver);

        PublisherAdView mPublisherAdView;
        mPublisherAdView = findViewById(R.id.publisherAdView);
        PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
        mPublisherAdView.loadAd(adRequest);
        mPublisherAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                if (errorCode == 3) {
                } else {
                    //Toast.makeText(getBaseContext(), "Code NOT OK! FAIL: " + errorCode, Toast.LENGTH_SHORT).show();
                }
            }
        });
        query();

        /*
        arena_horde.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //animate(arena_horde);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        arena_alli.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //animate(arena_alli);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        bgT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //animate(bgT);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        arenaT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //animate(arenaT);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        bgq.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //animate(bgq);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        bgq2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //animate(bgq2);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        */
        final Handler handler = new Handler();
        final int delay = 3000;
        handler.postDelayed(new Runnable() {
            public void run() {
                query();
                if (globalresult != null) {
                    try {
                        String[] asd = globalresult.split(",");
                        for (int i = 0; i < asd.length; i++) {
                            if (asd[i].contains("Arena All Arenas RATED [2v2] in queue")) {
                                String title = asd[i];
                                String[] alliance = asd[i + 1].split("\"");
                                String[] horde = asd[i + 2].split("\"");
                                String alli = alliance[3];
                                String hord = horde[3];
                                String arenat[] = asd[i + 3].split("\"");
                                arenaTime = arenat[3];

                                int iA = Integer.parseInt(alli);
                                int iH = Integer.parseInt(hord);
                                int rA = 0;
                                int rH = 0;
                                if (arenaAlliance != null) {
                                    rA = Integer.parseInt(arenaAlliance);
                                    rH = Integer.parseInt(arenaHorde);
                                }
                                arenaAlliance = alli;
                                arenaHorde = hord;

                                int alliDiff = iA - rA;
                                int hordeDiff = iH - rH;

                                if (alliDiff != 0 || i == 1) {
                                    arena_alli.setText(arenaAlliance);
                                    animate(arena_alli);
                                }
                                if (hordeDiff != 0 || i == 1) {
                                    arena_horde.setText(arenaHorde);
                                    animate(arena_horde);
                                }
                            }
                            if (asd[i].contains("Random Battleground NORMAL (90 - 90)")) {
                                String title = asd[i];
                                String[] alliance = asd[i + 1].split("\"");
                                String[] horde = asd[i + 2].split("\"");
                                String alli = alliance[3];
                                String hord = horde[3];
                                String bgT[] = asd[i + 3].split("\"");
                                String bgt[] = asd[i + 3].split("\"");
                                bgTime = bgt[3];

                                int iA = Integer.parseInt(alli);
                                int iH = Integer.parseInt(hord);
                                int rA = 0;
                                int rH = 0;
                                if (bgAlliance != null) {
                                    rA = Integer.parseInt(bgAlliance);
                                    rH = Integer.parseInt(bgHorde);
                                }
                                bgAlliance = alli;
                                bgHorde = hord;

                                int alliDiff = iA - rA;
                                int hordeDiff = iH - rH;
                                if (sw.isChecked()) {
                                    if (alliDiff <= -6 || hordeDiff <= -6) {
                                        Toast.makeText(getBaseContext(), "Random Battleground started!", Toast.LENGTH_SHORT).show();
                                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                        v.vibrate(500);
                                        try {
                                            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                                            r.play();
                                        } catch (Exception e) {
                                        }
                                    }
                                }
                                if (alliDiff != 0 || i == 1) {
                                    bgq.setText(bgAlliance);
                                    animate(bgq);
                                }
                                if (hordeDiff != 0 || i == 1) {
                                    bgq2.setText(bgHorde);
                                    animate(bgq2);
                                }
                            }
                        }
                    } catch (Exception e) {
                        Log.w("APP", e.getMessage());
                    }

                } else if (globalresult == null) {
                    Log.w("APP", "No data received.");
                }
                //arena_alli.setText(arenaAlliance);
                //arena_horde.setText(arenaHorde);
                arenaT.setText(arenaTime);
                //bgq.setText(bgAlliance);
                //bgq2.setText(bgHorde);
                bgT.setText(bgTime);
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);

    }

    public void query() {

        AsyncHttpClient client = new AsyncHttpClient();
        try {
            client.get("https://tauri.moonbeast.net/queue/api?key=wMDVeFDgzpHCc5hFGfVnbcbd", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        String str = new String(responseBody, "US-ASCII");
                        globalresult = str;
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                }
            });
        } catch (Exception e) {
            Log.d("HTTP ERROR", e.getMessage());
        }
    }

    public void checkUpdate(final Context context, String url) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String content = null;
                try {
                    content = new String(response, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    Log.d("APP", e.toString());
                }
                content = content.replaceAll("[^0-9.]", "");
                int currentVersion = BuildConfig.VERSION_CODE;
                int remoteVersion = Integer.parseInt(content);
                Log.d("VERSION", "CURRENT VERSION: " + currentVersion);
                if (remoteVersion > currentVersion) {
                    Toast.makeText(context, "New version available!", Toast.LENGTH_LONG).show();
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("Do you want to update now?");
                    alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // DOWNLOAD AND INSTALL
                            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                downloadUpdate8_http();
                            } else {
                                downloadUpdate8_http();
                            }
                        }
                    });
                    alert.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                }
                            });
                    alert.show();

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Toast.makeText(context, "Failed to retreive version information!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRetry(int retryNo) {
            }
        });
    }

    public void hasPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.e("Permission error", "You have permission");
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    public void forceCrash(View view) {
        throw new RuntimeException("crash");
    }

    private void logUser() {
        String android_id = Secure.getString(this.getContentResolver(),
                Secure.ANDROID_ID);
        Crashlytics.setUserIdentifier(android_id);
        Crashlytics.setUserEmail("some@junk.email");
        Crashlytics.setUserName("Test User");
    }

    public Uri getFileUri(Context context, File file) {
        //file = new File(Environment.getExternalStorageDirectory(), "tauri_queue_watcher.apk");
        return FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".com.sontme.esp.tauriqueuewatcher.provider", file);
    }

    public void installAPK(Context context) {
        final File file = new File(Environment.getExternalStorageDirectory(), "tauri_queue_watcher.apk");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri downloaded_apk = getFileUri(context,file);
            Intent intent = new Intent(Intent.ACTION_VIEW).setDataAndType(downloaded_apk,
                    "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file),
                    "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void animate(TextView t) {
        ObjectAnimator oa = (ObjectAnimator)
                AnimatorInflater.loadAnimator(getBaseContext(), R.animator.flipping);
        oa.setTarget(t);
        oa.setDuration(500);
        oa.start();
    }

    public void downloadUpdate7() {
        final DownloadManager downloadmanager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse("https://sont.sytes.net/tauri_queue_watcher.apk");
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle("Tauri Queue Watcher");
        request.setDescription("Downloading");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setVisibleInDownloadsUi(false);
        final File file = new File(Environment.getExternalStorageDirectory(), "tauri_queue_watcher.apk");
        final Uri uritofile = Uri.fromFile(file);
        request.setDestinationUri(Uri.parse(String.valueOf(uritofile)));
        downloadmanager.enqueue(request);
        Log.d("DL", uritofile.toString());
        Log.d("DL", file.getAbsolutePath());
        //installAPK(uritofile, file);
    }

    public void downloadUpdate8() {
        Toast.makeText(this, "8", Toast.LENGTH_SHORT).show();
        String url = "https://sont.sytes.net/tauri_queue_watcher.apk";
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("Downloading");
        request.setTitle("Tauri Queue Watcher");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "tauri_queue_watcher.apk");
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    public void downloadUpdate8_http() {
        final File file = new File(Environment.getExternalStorageDirectory(), "tauri_queue_watcher.apk");
        final DownloadTask downloadTask = new DownloadTask(MainActivity.this,file,"Updating...");
        downloadTask.execute("https://sont.sytes.net/tauri_queue_watcher.apk");
    }

    public class DownloadTask extends AsyncTask<String, Integer, String> {
        private ProgressDialog mPDialog;
        private Context mContext;
        private PowerManager.WakeLock mWakeLock;
        private File mTargetFile;

        public DownloadTask(Context context,File targetFile,String dialogMessage) {
            this.mContext = context;
            this.mTargetFile = targetFile;
            mPDialog = new ProgressDialog(context);

            mPDialog.setMessage(dialogMessage);
            mPDialog.setIndeterminate(true);
            mPDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mPDialog.setCancelable(true);
            final DownloadTask me = this;
            mPDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    me.cancel(true);
                }
            });
            Log.i("DownloadTask","Constructor done");
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }
                Log.i("DownloadTask","Response " + connection.getResponseCode());

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();
                output = new FileOutputStream(mTargetFile,false);

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        Log.i("DownloadTask","Cancelled");
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();

            mPDialog.show();

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mPDialog.setIndeterminate(false);
            mPDialog.setMax(100);
            mPDialog.setProgress(progress[0]);

        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("DownloadTask", "Work Done! PostExecute");
            mWakeLock.release();
            mPDialog.dismiss();
            if (result != null)
                Toast.makeText(mContext,"Download error: "+result, Toast.LENGTH_LONG).show();
            else{
                installAPK(getBaseContext());
            }
        }
    }

    public static class GenericFileProvider extends FileProvider {}

}