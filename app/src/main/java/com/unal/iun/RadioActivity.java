package com.unal.iun;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.VideoView;

import com.unal.iun.LN.onItemSpinSelected;

import java.util.ArrayList;
import java.util.List;


public class RadioActivity extends Activity implements
        MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, SurfaceHolder.Callback {
    public static MediaPlayer mediaPlayer;
    public static String path = "http://m.youtube.com/add_favorite?v=wIn-wIgWPXM";
    private VideoView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Button bPlay, bPause;
    private boolean pause;
    private int savePos = 0;
    private Spinner spin;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_radio);
        BitmapDrawable background2 = new BitmapDrawable(
                BitmapFactory.decodeResource(getResources(),
                        R.drawable.fondoinf));
        this.getActionBar().setBackgroundDrawable(background2);
        this.getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        spin = (Spinner) findViewById(R.id.spinnerSelector);
        List list = new ArrayList();
        list.add("UN Bogotá");
        list.add("UN Medellin");
        list.add("UN WEB");
        list.add("RFI");
        ArrayAdapter dataAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TextView text = (TextView) findViewById(R.id.textRadioTEMP);
        spin.setAdapter(dataAdapter);
        bPlay = (Button) findViewById(R.id.play);
        bPlay.setEnabled(false);
        bPause = (Button) findViewById(R.id.pause);
        spin.setOnItemSelectedListener(new onItemSpinSelected(bPlay, bPause, text));
        try {
            // mediaPlayer = new MediaPlayer();
            // mediaPlayer.setDataSource(path);
            // mediaPlayer.prepare();
            // mediaPlayer.start();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

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
            RadioActivity.mediaPlayer = new MediaPlayer();
            RadioActivity.mediaPlayer.setDataSource(path);
            RadioActivity.mediaPlayer.prepare();
            RadioActivity.mediaPlayer.seekTo(savePos);
            RadioActivity.mediaPlayer.start();
            /*
             * mediaPlayer.prepareAsync();// Para streaming
			 * mediaPlayer.setOnBufferingUpdateListener(this);
			 * mediaPlayer.setOnCompletionListener(this);
			 * mediaPlayer.setOnPreparedListener(this);
			 * mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			 */

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
    public void onPause() {
        super.onPause();
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