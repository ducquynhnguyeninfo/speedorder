package vn.game.speedorder.logic;

import vn.game.speedorder.GameActivity;
import vn.game.speedorder.utils._Log;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;

/**
 * The class in charge of handling game's runtime.
 * 
 * @author DUC QUYNH
 * 
 */
public class Timer {

	private Handler handler;
	/**
	 * CountDownTimer to count down the time we use for a cycle of game.
	 */
	private CountDownTimer downTimer;
	/**
	 * Time we use for a game's cycle = 1800ms. 
	 */ 
	private long countingTime = 2000;

	public Timer(Handler handler) {
		this.handler = handler;
	}

	void setHandler(Handler handler) {
		this.handler = handler;
	}

	/**
	 * Start a new cycle of game's timing.
	 */long time = 0;
	public void doStart() {
		time = 0;
		destroyTimer();
		downTimer = new CountDownTimer(countingTime, 1000) {
			@Override
			public void onTick(long millisUntilFinished) {
			}

			@Override
			public void onFinish() {
				if (!GameActivity.isGameOver) {
					handler.sendEmptyMessage(0);
					_Log.i("the cycle time has elapsed out",	System.nanoTime() - Timer.this.time + "");
					_Log.i("fail reason", "the cycle time has elapsed out.");
				}
				GameActivity.isGameOver = true;
			}
		};
		
		_Log.i("the cycle time has started", time + "");
		downTimer.start();
		time = 	System.nanoTime();;
	
	}

	/**
	 * Destroys the recent CountDownTimer we used, if we have.
	 */
	public void destroyTimer() {
		if (downTimer != null) {
			downTimer.cancel();
			downTimer = null;
		}
	}
}
