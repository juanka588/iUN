package com.unal.iun.LN;

import android.media.MediaPlayer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.unal.iun.RadioActivity;

import java.io.IOException;

public class onItemSpinSelected implements OnItemSelectedListener {
    Button b;
    Button b2;
    TextView tx;

    public onItemSpinSelected(Button play, Button pause, TextView text) {
        b = play;
        b2 = pause;
        tx = text;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View arg1, int arg2,
                               long arg3) {
        Toast.makeText(parent.getContext(), "" + parent.getSelectedItem(), 1)
                .show();
        switch (parent.getSelectedItemPosition()) {
            case 0:
                RadioActivity.path = "http://streaming.unradio.unal.edu.co:8010/";
                break;
            case 1:
                RadioActivity.path = "http://streaming.unradio.unal.edu.co:8012/";
                break;
            case 2:
                RadioActivity.path = "http://streaming.unradio.unal.edu.co:8014/";
                break;
            case 3:
                RadioActivity.path = "http://95.81.147.3/rfimonde/all/rfimonde-64k.mp3";
                break;
            default:
                break;
        }
        try {
            tx.setText(RadioActivity.path + "");
            if (RadioActivity.mediaPlayer != null) {
                if (RadioActivity.mediaPlayer.isPlaying()) {
                    RadioActivity.mediaPlayer.stop();
                    RadioActivity.mediaPlayer.release();
                }
                RadioActivity.mediaPlayer = new MediaPlayer();
                RadioActivity.mediaPlayer.setDataSource(RadioActivity.path);
                RadioActivity.mediaPlayer.prepare();
                RadioActivity.mediaPlayer.start();
            } else {
                RadioActivity.mediaPlayer = new MediaPlayer();
                RadioActivity.mediaPlayer.setDataSource(RadioActivity.path);
                RadioActivity.mediaPlayer.prepare();
                RadioActivity.mediaPlayer.start();
            }
            b.setEnabled(false);
            b2.setEnabled(true);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

}
