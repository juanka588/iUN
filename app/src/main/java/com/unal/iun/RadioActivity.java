package com.unal.iun;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.VideoView;

import com.unal.iun.LN.onItemSpinSelected;


public class RadioActivity extends ActionBarActivity implements
        MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, SurfaceHolder.Callback {
    public static MediaPlayer mediaPlayer;
    public static String path = "http://m.youtube.com/add_favorite?v=wIn-wIgWPXM";
    private VideoView surfaceView;
    private SurfaceHolder surfaceHolder;
    private ImageButton bPlay, bPause;
    private boolean pause;
    private int savePos = 0;
    private Spinner spin;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_radio);
        this.getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        spin = (Spinner) findViewById(R.id.spinnerSelector);
        String[] list = this.getResources().getStringArray(R.array.radio);
        ArrayAdapter dataAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TextView text = (TextView) findViewById(R.id.textRadioTEMP);
        spin.setAdapter(dataAdapter);
        bPlay = (ImageButton) findViewById(R.id.play);
        bPlay.setEnabled(false);
        bPause = (ImageButton) findViewById(R.id.pause);
        spin.setOnItemSelectedListener(new onItemSpinSelected(bPlay, bPause, text));
    }

    public void playVideo(View v) {
        v.setEnabled(false);
        playVideo();
    }

    public void pauseVideo(View v) {
        v.setEnabled(false);
        bPlay.setEnabled(true);
        if (RadioActivity.mediaPlayer != null) {
            pause = true;
            RadioActivity.mediaPlayer.pause();
        }
    }

    private void playVideo() {
        try {
            pause = false;
            bPause.setEnabled(true);
            RadioActivity.mediaPlayer.stop();
            RadioActivity.mediaPlayer.release();
            RadioActivity.mediaPlayer = null;
            if (path.equals("")) {
                RadioActivity.mediaPlayer = MediaPlayer.create(getApplicationContext(),
                        R.raw.himno);
            } else {
                RadioActivity.mediaPlayer = new MediaPlayer();
                RadioActivity.mediaPlayer.setDataSource(path);
                RadioActivity.mediaPlayer.prepare();
            }
            RadioActivity.mediaPlayer.seekTo(savePos);
            RadioActivity.mediaPlayer.start();
        } catch (Exception e) {
            log("ERROR: " + e.getMessage());
        }
    }

    public void onBufferingUpdate(MediaPlayer arg0, int percent) {
        log("onBufferingUpdate percent:" + percent);
    }

    public void onCompletion(MediaPlayer arg0) {
        log("onCompletion called");
    }

    public void onPrepared(MediaPlayer mediaplayer) {
        log("onPrepared called");
        int mVideoWidth = mediaPlayer.getVideoWidth();
        int mVideoHeight = mediaPlayer.getVideoHeight();
        if (mVideoWidth != 0 && mVideoHeight != 0) {
            surfaceHolder.setFixedSize(mVideoWidth, mVideoHeight);
            mediaPlayer.start();
        }
    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        log("surfaceCreated called");
        playVideo();
    }

    public void surfaceChanged(SurfaceHolder surfaceholder, int i, int j, int k) {
        log("surfaceChanged called");
    }

    public void surfaceDestroyed(SurfaceHolder surfaceholder) {
        log("surfaceDestroyed called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (RadioActivity.mediaPlayer != null) {
            RadioActivity.mediaPlayer.release();
            RadioActivity.mediaPlayer = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            if (RadioActivity.mediaPlayer != null) {
                RadioActivity.mediaPlayer.pause();
            }
        } catch (Exception e) {
            log(e.toString());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (RadioActivity.mediaPlayer != null & !pause) {
            RadioActivity.mediaPlayer.start();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle guardarEstado) {
        super.onSaveInstanceState(guardarEstado);
        if (RadioActivity.mediaPlayer != null) {
            int pos = RadioActivity.mediaPlayer.getCurrentPosition();
            guardarEstado.putString("ruta", path);
            guardarEstado.putInt("posicion", pos);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle recEstado) {
        super.onRestoreInstanceState(recEstado);
        if (recEstado != null) {
            path = recEstado.getString("ruta");
            savePos = recEstado.getInt("posicion");
        }
    }

    private void log(String s) {
        Log.e("LOG radio", s);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                home();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void home() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        if (RadioActivity.mediaPlayer != null) {
            RadioActivity.mediaPlayer.stop();
            RadioActivity.mediaPlayer.release();
        }
        this.finish();
    }

}