package br.com.facom.poo2.voxxx;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import br.com.facom.poo2.voxxx.customViews.VuMeterView;
import br.com.facom.poo2.voxxx.helpers.AudioRecorder;
import br.com.facom.poo2.voxxx.helpers.VoxxxRecord;

public class VoxxxRecorder extends Activity {
	private AudioRecorder mRecorder = null;
	private VoxxxRecord mRecord = null;
	private Timer mUiTimer = null;
	private String mLastFileName = "";
	private MediaPlayer mMediaPlayer = null;
	int peek = 0;
	double finalDuration = 0;
	private String deviceID;

	class UiUpdateTask extends TimerTask {
		public void run() {
			final VuMeterView vuMeter = (VuMeterView) findViewById(R.id.vuMeterView);
			final TextView durationText = (TextView) findViewById(R.id.textRecordDuration);

			vuMeter.setValue(mRecorder.getAmplitude());

			//MAX is 32767 MEAN is 16383
			if (mRecorder.getAmplitude() >= 5000) {
				peek+=1;
			}

			final long duration = System.currentTimeMillis()
					- mRecord.getStartTime();
			finalDuration = duration;

			runOnUiThread(new Runnable() {
				public void run() {
					durationText.setText(String.format(
							"%02d:%02d",
							TimeUnit.MILLISECONDS.toMinutes(duration),
							TimeUnit.MILLISECONDS.toSeconds(duration)
									- TimeUnit.MINUTES
											.toSeconds(TimeUnit.MILLISECONDS
													.toMinutes(duration))));
					vuMeter.invalidate();
				}
			});

		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_voxx_recorder);

		ToggleButton playBtn = (ToggleButton) findViewById(R.id.btnPlayLastRecord);
		playBtn.setEnabled(false);
		playBtn.setAlpha(0.3f);

		deviceID = Secure.getString(getContentResolver(), Secure.ANDROID_ID);

		// makeAlert("CRAZY DEVICE: "+deviceID);

	}

	@SuppressLint("SdCardPath")
	@Override
	public void onDestroy() {
		String PATH_TO_FILE = "/sdcard/Recording/";
		File path = new File(PATH_TO_FILE);

		File f = new File(PATH_TO_FILE);
		if (f.isDirectory()) {
			File file[] = f.listFiles();
			Log.d("B_Files", "B_Size: " + file.length);
			for (int i = 0; i < file.length; i++) {
				Log.d("B_Files", "B_FileName:" + file[i].getName());
			}

			boolean deleted = deleteDirectory(path);

			if (deleted)
				Log.d("DESTROYING_EVIDENCES",
						"If at first you don't succeed, destroy all evidence that you tried.");

		}
		super.onDestroy();
	}

	private boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			if (files == null) {
				return true;
			}
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.voxx_recorder, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.action_exit_recorder) {
			finish();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	public void makeAlert(final String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	@SuppressLint("SdCardPath")
	public void onClickPlay(View v) {
		final ToggleButton tb = (ToggleButton) findViewById(R.id.btnPlayLastRecord);

		if (mMediaPlayer == null) {
			String PATH_TO_FILE = "/sdcard/Recording/" + mLastFileName;
			mMediaPlayer = new MediaPlayer();

			try {
				mMediaPlayer.setDataSource(PATH_TO_FILE);
				mMediaPlayer.prepare();
			} catch (IllegalArgumentException e) {
				Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
			} catch (IllegalStateException e) {
				Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
			} catch (IOException e) {
				Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
			}

			mMediaPlayer.start();
			mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer arg0) {
					tb.setChecked(false);
					mMediaPlayer.stop();
					mMediaPlayer = null;
				}
			});
		} else {
			tb.setChecked(false);
			mMediaPlayer.stop();
			mMediaPlayer = null;
		}
	}

	public void onClickRecord(View v) {
		final ToggleButton tb = (ToggleButton) findViewById(R.id.btnRecord);
		final Button shareBtn = (Button) findViewById(R.id.btnShare);

		if (tb.isChecked()) {
			// force disabled before recording
			tb.setChecked(false);
			tb.setEnabled(false);

			// prepare recording
			SimpleDateFormat fileNameDate = new SimpleDateFormat(
					"yyyy-MM-dd HH.mm.ss", Locale.US);
			mLastFileName = fileNameDate.format(new Date()) + ".3gp";

			mRecorder = new AudioRecorder(mLastFileName);
			mRecord = new VoxxxRecord(mLastFileName, deviceID);

			// start recording
			try {
				mRecorder.start();
			} catch (IOException e) {
				makeAlert("Error while starting recording:\n" + e.getMessage());
				tb.setChecked(false);
				tb.setEnabled(true);
			} finally {
				tb.setChecked(true);
				tb.setEnabled(true);

				mUiTimer = new Timer();
				mUiTimer.schedule(new UiUpdateTask(), 10, 50);

				mRecord.setStartTime(System.currentTimeMillis());

				shareBtn.setEnabled(false);
			}
		} else {
			tb.setChecked(true);
			tb.setEnabled(false);

			mRecord.setPeeks(peek);
			mRecord.setDuration(finalDuration);

			// peeks.add(peek);
			// Toast.makeText(getApplicationContext(),
			// (CharSequence) ("CRAZY DIAMOND " + peeks.toString()),
			// Toast.LENGTH_LONG).show();
			peek = 0;
			// Toast.makeText(getApplicationContext(),
			// (CharSequence) ("CRAZY DURATION " + finalDuration),
			// Toast.LENGTH_LONG).show();
			finalDuration = 0;

			try {
				mUiTimer.cancel();
				mRecorder.stop();

				ToggleButton playBtn = (ToggleButton) findViewById(R.id.btnPlayLastRecord);
				playBtn.setEnabled(true);
				playBtn.setAlpha(1f);
			} catch (IOException e) {
				makeAlert("Error while stopping recording:\n" + e.getMessage());
				tb.setChecked(true);
				tb.setEnabled(true);
			} finally {
				tb.setChecked(false);
				tb.setEnabled(true);
				shareBtn.setEnabled(true);
			}
		}
	}

	@SuppressLint("SdCardPath")
	public void onClickShare(View v) {
		if (mLastFileName.equals("")) {
			Toast.makeText(this,
					getResources().getString(R.string.no_recording_done),
					Toast.LENGTH_LONG).show();
		} else {
			mRecord.setPathToStream("file:///sdcard/Recording/" + mLastFileName);

			Intent i = new Intent();
			Bundle b = new Bundle();

			b.putSerializable("record", mRecord);
			i.putExtras(b);
			i.setClass(VoxxxRecorder.this, VoxxxScore.class);
			startActivity(i);

		}
	}

}
