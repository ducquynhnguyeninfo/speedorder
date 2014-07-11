package vn.game.speedorder;

import java.util.ArrayList;
import java.util.List;

import vn.game.speedorder.logic.ResultChecker;
import vn.game.speedorder.logic.Timer;
import vn.game.speedorder.models.ElementSupplier;
import vn.game.speedorder.models.NotificationButton;
import vn.game.speedorder.utils.PreferenceUtil;
import vn.game.speedorder.utils.Utils;
import vn.game.speedorder.utils._Log;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * {@link GameActivity} handles all main game's processes.
 * 
 * @author DUC QUYNH
 * 
 */
public class GameActivity extends Activity implements
		NotificationButton.OnClickedListener {

	/**
	 * The flag indicates the game's state.
	 */
	// public static boolean losed = false;

	private boolean isNewGame;
	public static boolean isGameOver;
	/**
	 * Primitive list of number unarranged.
	 */
	private List<Integer> rawElements;
	private List<Integer> player = new ArrayList<Integer>();
	private byte countClick = 0;

	private NotificationButton butt1;
	private NotificationButton butt2;
	private NotificationButton butt3;
	private TextView tvScore;
	private EndGameDialog endGameDialog;
	private int scores = 0;
	private Timer timer;
	private RelativeLayout start_Lo;
	private Bundle achievement;
	private TextView btn_start;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_play);
		initialize();
	}

	/**
	 * Initialize game's fundamental components.
	 */
	private void initialize() {
		timer = new Timer(gameOverHandler);
		start_Lo = (RelativeLayout) findViewById(R.id.lo_guide);
		btn_start = (TextView) findViewById(R.id.tv_click);
		btn_start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isNewGame)
					start_Lo.setVisibility(View.GONE);
				makeNewCycle();
				isNewGame = false;
			}
		});
		butt1 = (NotificationButton) findViewById(R.id.button1);
		butt2 = (NotificationButton) findViewById(R.id.button2);
		butt3 = (NotificationButton) findViewById(R.id.button3);
		tvScore = (TextView) findViewById(R.id.tv_scores);
		Typeface fontMario = Typeface.createFromAsset(getAssets(),
				"SuperMario_Bros.ttf");
		tvScore.setTypeface(fontMario);
		Typeface fontComic = Typeface.createFromAsset(getAssets(),
				"SF_Wonder_Comic_Bold.ttf");
		butt1.setTypeface(fontComic).setOnClickListener(this);
		butt2.setTypeface(fontComic).setOnClickListener(this);
		butt3.setTypeface(fontComic).setOnClickListener(this);
		endGameDialog = new EndGameDialog(this);
		newGame();
	}

	/**
	 * Handler handles game's announcements.
	 */
	Handler gameOverHandler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			stopGame();
			return false;
		}
	});

	public void newGame() {
		scores = 0;
		isNewGame = true;
		isGameOver = false;
		tvScore.setText(scores + "");
		if (isNewGame) {
			start_Lo.setVisibility(View.VISIBLE);
		}
	}

	public int[] getRank(int scores, int[] scoreArr) {
		if (scores > scoreArr[0]) {
			scoreArr[2] = scoreArr[1];
			scoreArr[1] = scoreArr[0];
			scoreArr[0] = scores;
			return scoreArr;
		} else

		if (scores > scoreArr[1]) {
			scoreArr[2] = scoreArr[1];
			scoreArr[1] = scores;
			return scoreArr;
		} else

		if (scores > scoreArr[2]) {
			scoreArr[2] = scores;
			return scoreArr;
		}
		return scoreArr;
	}

	void onStopGame() {
		butt1.setOrderTagChangeable(false).setWasClicked(true)
				.setClickable(false);
		butt2.setOrderTagChangeable(false).setWasClicked(true)
				.setClickable(false);
		butt3.setOrderTagChangeable(false).setWasClicked(true)
				.setClickable(false);
	}

	void stopGame() {
		endGameDialog.showPopup();
		getRank(scores, Utils.scoreArr);
		achievement = new Bundle();
		achievement.putInt(Utils.KEY_BEST_SCORE, Utils.scoreArr[0]);
		achievement.putInt(Utils.KEY_SECOND_SCORE, Utils.scoreArr[1]);
		achievement.putInt(Utils.KEY_THIRD_SCORE, Utils.scoreArr[2]);
		achievement.putInt(Utils.KEY_CURRENT_SCORE, scores);
		endGameDialog.setArguments(achievement);
		PreferenceUtil.putInt(getApplicationContext(), Utils.KEY_BEST_SCORE,
				Utils.scoreArr[0]);
		PreferenceUtil.putInt(getApplicationContext(), Utils.KEY_SECOND_SCORE,
				Utils.scoreArr[1]);
		PreferenceUtil.putInt(getApplicationContext(), Utils.KEY_THIRD_SCORE,
				Utils.scoreArr[2]);
	}

	@Override
	public void onClick(View button) {
		countClick++;
		_Log.v("view clicked id -- number " + countClick, button.getId() + "");
		doOnClick(button);
		/*
		 * If clicked all the button, then we are going to check the result.
		 */
		if (countClick == rawElements.size()) {
			ResultChecker resultChecker = new ResultChecker(getElements(),
					player);
			if (resultChecker.isFailed()) {
				if (!isGameOver) {
					gameOverHandler.sendEmptyMessage(0);
					_Log.i("fail reason", "the arrangement is not correct.");
				}
				isGameOver = true;
			} else {
				tvScore.setText(++scores + "");
				player.removeAll(player);
				makeNewCycle();
			}
		}
	}

	private List<Integer> getElements() {
		return this.rawElements;
	}

	private void doOnClick(View button) {
		switch (catchClickedNotificationButton(button).getId()) {
		case R.id.button1:
			setButtonImgOrder(butt1);
			break;
		case R.id.button2:
			setButtonImgOrder(butt2);
			break;
		case R.id.button3:
			setButtonImgOrder(butt3);
			break;
		}
	}

	/**
	 * Set the image resource to the order tags of each
	 * {@link NotificationButton}.
	 * 
	 * @param button
	 *            The button has compounded in {@link NotificationButton}
	 * 
	 */
	private void setButtonImgOrder(NotificationButton nButton) {
		/*
		 * Adding element to the position at (countClick - 1) with the value of
		 * button.getText().
		 */
		player.add(countClick - 1, Integer.valueOf((String) nButton.getText()));
		if (!isGameOver)
			if (!nButton.wasClicked()) {
				nButton.setOrderTagSource(setOrder(countClick))
						.setOrderTagChangeable(false).setWasClicked(true);
			} else if (!isNewGame) { // If the button has clicked twice and game
										// has started, we
										// failed.
				onStopGame();
				gameOverHandler.sendEmptyMessage(0);
				_Log.i("fail reason", "click button 1 twice");
				isGameOver = true;
			}
	}

	/**
	 * Spawns a new game cycle.
	 */
	private void makeNewCycle() {
		countClick = 0;
		rawElements = ElementSupplier.supply(3, 20);
		for (Integer in : rawElements) {
			_Log.v("element", in + "");
		}
		butt1.buttonResetState();
		butt2.buttonResetState();
		butt3.buttonResetState();
		butt1.setText(String.valueOf(rawElements.get(0)));
		butt2.setText(String.valueOf(rawElements.get(1)));
		butt3.setText(String.valueOf(rawElements.get(2)));
		timer.doStart();
	}

	// private void setElements(List<Integer> elements) {
	// this.rawElements = elements;
	// }

	/**
	 * Decides the {@link NotificationButton}'s order tag depend on values of
	 * countClick.
	 * 
	 * @param countClick
	 *            Number of clicking time on the {@link NotificationButton}s.
	 * @return Resource image that will be wielded as order tag by
	 *         {@link NotificationButton}.
	 */
	private int setOrder(int countClick) {
		switch (countClick) {
		case 1:
			return R.drawable.o1;
		case 2:
			return R.drawable.o2;
		case 3:
			return R.drawable.o3;
		default:
			return 0;
		}
	}

	/**
	 * Gets exactly view (compounded view {@link NotificationButton}) that
	 * contains the button (mentioned here is the button we clicked up there) to
	 * preparing up coming steps.
	 */
	@Override
	public View catchClickedNotificationButton(View normalButton) {
		/*
		 * The view contains the view normalButton input directly.
		 */
		ViewParent parent = normalButton.getParent();
		NotificationButton nB = null;
		if (parent == null) {
			// _Log.d("TEST",
			// "normalButton.getParent() is a NotificationButton");
		} else {
			if (parent instanceof ViewGroup) {
				if (parent instanceof NotificationButton) {
					nB = (NotificationButton) parent;
					// _Log.d("TEST",
					// "this.getParent() is a NotificationButton");
					return nB;
				}
				ViewParent grandParent = ((ViewGroup) parent).getParent();
				if (grandParent == null) {
					// _Log.d("TEST",
					// "((ViewGroup) this.getParent()).getParent() ==> grandparent is null");
				} else if (grandParent instanceof NotificationButton)
					nB = (NotificationButton) grandParent;
				else {
					// _Log.d("TEST",
					// "((ViewGroup) this.getParent()).getParent() is not a NotificationButton");
				}
			} else {
				// _Log.d("TEST", "this.getParent() is not a ViewGroup");
			}
		}
		return nB;
	}

	@Override
	protected void onStop() {
		timer.destroyTimer();
		super.onStop();
	}
}