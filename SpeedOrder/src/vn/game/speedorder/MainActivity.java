package vn.game.speedorder;

import vn.game.speedorder.utils.PreferenceUtil;
import vn.game.speedorder.utils.Utils;
import vn.game.speedorder.utils._Log;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private Button playBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		// // Set the screen's orientation to be always landscape.
		setContentView(R.layout.activity_main);

		if (PreferenceUtil.determine(this) == 0) {
			_Log.i("PreferenceUtil - determine",
					"doesnot exist - creating a new one");
			PreferenceUtil
					.putInt(this, Utils.KEY_BEST_SCORE, Utils.scoreArr[0]);
			PreferenceUtil.putInt(this, Utils.KEY_SECOND_SCORE,
					Utils.scoreArr[1]);
			PreferenceUtil.putInt(this, Utils.KEY_THIRD_SCORE,
					Utils.scoreArr[2]);
		} else {
			_Log.i("PreferenceUtil - determine", "existed");
			Utils.scoreArr[0] = PreferenceUtil.getInt(this,
					Utils.KEY_BEST_SCORE, 0);
			Utils.scoreArr[1] = PreferenceUtil.getInt(this,
					Utils.KEY_SECOND_SCORE, 0);
			Utils.scoreArr[2] = PreferenceUtil.getInt(this,
					Utils.KEY_THIRD_SCORE, 0);
		}
		playBtn = (Button) findViewById(R.id.play_btn);
		playBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						GameActivity.class);
				startActivity(intent);
			}
		});
	}
}
