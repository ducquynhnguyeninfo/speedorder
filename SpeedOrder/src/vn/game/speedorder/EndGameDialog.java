package vn.game.speedorder;

import vn.game.speedorder.utils.Utils;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This class shows the game's ending when the player failed.
 * 
 * @author DUC QUYNH
 * 
 */
public class EndGameDialog implements View.OnClickListener {

	interface OnReceivedData {
		void onReceivedData(Bundle bundle);
	}

	public OnReceivedData onReceivedData;
	private Animation animShow;
	private EndGameLayout popup;
	private Activity activity;
//	private Bundle bundle;
	private ImageButton btn_replay;
	private ImageButton btn_rate;
	private TextView tv_best;
	private TextView tv_second;
	private TextView tv_third;
	private TextView tv_score;

	public EndGameDialog(Activity activity) {
		this.activity = activity;
		popup = (EndGameLayout) ((Activity) activity)
				.findViewById(R.id.endGamePopup);
		initPopup(activity);
	}

	/**
	 * Initializes all dialog components.
	 * 
	 * @param activity
	 */
	private void initPopup(final Activity activity) {
		popup.setVisibility(View.GONE);
		animShow = AnimationUtils.loadAnimation(activity, R.anim.popup_show);
		btn_replay = (ImageButton) activity.findViewById(R.id.replay);
		btn_replay.setOnClickListener(this);
		btn_rate = (ImageButton) activity.findViewById(R.id.rate);
		btn_rate.setOnClickListener(this);
		tv_score = (TextView) activity.findViewById(R.id.tv_your_score);
		tv_score.setTypeface(Typeface.createFromAsset(activity.getAssets(),
				"SF_Wonder_Comic_Bold.ttf"));
		tv_best = (TextView) activity.findViewById(R.id.tv_your_best);
		tv_second = (TextView) activity.findViewById(R.id.tv_your_second);
		tv_third = (TextView) activity.findViewById(R.id.tv_your_third);
	}

	/**
	 * Pass the bundle contains data to show.
	 * 
	 * @param bundle
	 */
	public void setArguments(Bundle bundle) {
//		this.bundle = (Bundle) bundle;
		tv_score.setText(bundle.getInt(Utils.KEY_CURRENT_SCORE) + "");
		tv_best.setText(bundle.getInt(Utils.KEY_BEST_SCORE) + "");
		tv_second.setText(bundle.getInt(Utils.KEY_SECOND_SCORE) + "");
		tv_third.setText(bundle.getInt(Utils.KEY_THIRD_SCORE) + "");
	}

	/**
	 * Gets data from bundle and sets them to components then shows the dialog.
	 */
	public void showPopup() {

		popup.startAnimation(animShow);

		popup.setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rate:
			Toast.makeText(activity, "Thank you so much for ratting" + activity.getApplicationInfo().labelRes, Toast.LENGTH_SHORT).show();
			break;
		case R.id.replay:
			popup.setVisibility(View.GONE);
			((GameActivity) activity).newGame();
			break;
		default:
			break;
		}
	}

}
