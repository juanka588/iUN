package com.unal.iun.LN;


import android.os.Handler;
import android.os.Message;
import android.widget.Button;

public class Timer implements Runnable {
	public Handler puente = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			b.setText(msg.obj + "");
		}
	};
	public Button b;
	public int count, signo;

	public Timer(Button a, int x) {
		this(a, x, -1);
	}

	public Timer(Button a, int x, int s) {
		b = a;
		count = x;
		signo = s;
	}

	@Override
	public void run() {
		while (count >= 0) {
			count += signo;
			 Message msg = new Message();
		      msg.obj = count;
		      puente.sendMessage(msg);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
