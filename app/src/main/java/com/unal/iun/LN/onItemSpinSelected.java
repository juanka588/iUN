package com.unal.iun.LN;

import android.media.MediaPlayer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.unal.iun.R;
import com.unal.iun.RadioActivity;

import java.io.IOException;

public class onItemSpinSelected implements OnItemSelectedListener {
    ImageButton b;
    ImageButton b2;
    TextView tx;

    public onItemSpinSelected(ImageButton play, ImageButton pause, TextView text) {
        b = play;
        b2 = pause;
        tx = text;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View arg1, int arg2,
                               long arg3) {
        Toast.makeText(parent.getContext(), "" + parent.getSelectedItem(), Toast.LENGTH_SHORT)
                .show();
        int selected = parent.getSelectedItemPosition();
        switch (selected) {
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
            case 4:
                RadioActivity.path = "";
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
            }
            if (selected == 4) {
                RadioActivity.mediaPlayer = MediaPlayer.create(arg1.getContext(),
                        R.raw.himno);
                tx.setText(arg1.getContext().getString(R.string.himno));
            } else {
                RadioActivity.mediaPlayer = new MediaPlayer();
                RadioActivity.mediaPlayer.setDataSource(RadioActivity.path);
                RadioActivity.mediaPlayer.prepare();
            }
            RadioActivity.mediaPlayer.start();
            b.setEnabled(false);
            b2.setEnabled(true);
        } catch (IllegalArgumentException e) {
            mensaje();
            e.printStackTrace();
        } catch (SecurityException e) {
            mensaje();
            e.printStackTrace();
        } catch (IllegalStateException e) {
            mensaje();
            e.printStackTrace();
        } catch (IOException e) {
            mensaje();
            e.printStackTrace();
        } catch (Exception e) {
            mensaje();
        }
    }

    protected void mensaje() {
        Toast.makeText(b.getContext(), b.getContext().getString(R.string.radio_exception), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

}
