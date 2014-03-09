package br.com.facom.poo2.voxxx;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import br.com.facom.poo2.voxxx.avgFactory.ScoreDay;
import br.com.facom.poo2.voxxx.avgFactory.ScoreEstNumber;
import br.com.facom.poo2.voxxx.avgFactory.ScoreSpecies;
import br.com.facom.poo2.voxxx.avgFactory.ScoresFactory;
import br.com.facom.poo2.voxxx.helpers.MongoREST;
import br.com.facom.poo2.voxxx.helpers.VoxxxRecord;
import br.com.facom.poo2.voxxx.scoreChain.ScoreLevel;
import br.com.facom.poo2.voxxx.scoreChain.ScoreManager;

public class VoxxxScore extends Activity {
	private ProgressBar pb;
	private TextView titleUpload;
	private ImageView scoreImage;
	private TextView scoreNumber;
	private TextView scoremsg;
	private Button doItAgain;

	private VoxxxRecord score;

	private boolean postResult;
	private double scoreValue;
	private double scoreAvg;

	private MongoREST database = MongoREST.getSelf();
	private ScoreLevel level;
	private String theText = "FUCK THIS S H I T!";
	private int theId = R.drawable.result_10;
	
	public int getTheId() {
		return theId;
	}

	public void setTheId(int theId) {
		this.theId = theId;
	}

	public String getTheText() {
		return theText;
	}

	public void setTheText(String theText) {
		this.theText = theText;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_voxxx_score);

		pb = (ProgressBar) findViewById(R.id.progressBar1);
		scoreImage = (ImageView) findViewById(R.id.imageView1);
		scoreNumber = (TextView) findViewById(R.id.textView2);
		scoremsg = (TextView) findViewById(R.id.textView3);

		pb.setVisibility(View.GONE);
		scoreImage.setVisibility(View.GONE);
		scoreNumber.setVisibility(View.GONE);
		scoremsg.setVisibility(View.GONE);

		Intent i = getIntent();
		score = (VoxxxRecord) i.getSerializableExtra("record");
		// MongoREST.setCollection(score.getDeviceId());
		database.setCollection(score.getDeviceId());
		setScoreLevel();
		
		
		new UploadScoreTask().execute("");

		doItAgain = (Button) findViewById(R.id.button1);
		doItAgain.setVisibility(View.GONE);
		doItAgain.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.voxxx_score, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.action_exit_score) {
			finish();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	class UploadScoreTask extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pb.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(String... f_url) {

			// postResult = MongoREST.insertScore(score);
			postResult = database.insertScore(score);
			if (postResult) {
				scoreValue = score.getScore();
				scoreAvg = retrieveScoreEstFromDB();

			} else {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(
								getApplicationContext(),
								(CharSequence) ("Couldn't insert your record, please try again."),
								Toast.LENGTH_LONG).show();
					}
				});
			}
			return null;
		}

		protected void onProgressUpdate(String... progress) {
			pb.setProgress(Integer.valueOf(progress[0]));
		}

		@Override
		protected void onPostExecute(String file_url) {
			pb.setVisibility(View.GONE);
			titleUpload = (TextView) findViewById(R.id.textView1);
			titleUpload.setVisibility(View.GONE);

			scoreNumber = (TextView) findViewById(R.id.textView2);
			scoreNumber.setText(String.format("%.1f", scoreValue));
			scoreNumber.setVisibility(View.VISIBLE);

			//chainOfResponsability
			ScoreManager mgr = ScoreManager.createChain();
			mgr.handleScore(VoxxxScore.this);
			
			
			doItAgain.setVisibility(View.VISIBLE);
		}
		
	}

	private double retrieveScoreEstFromDB() {
		String pattern = "yyyy-MM-dd";
		DateFormat df = new SimpleDateFormat(pattern, Locale.US);
		String today = df.format(score.getStartTimeAsDate());
		
		//factory
		ScoreEstNumber estTool = ScoresFactory.getScoreEst(ScoreSpecies.TODAY);
		((ScoreDay)estTool).setTheDay(today);
		String query = estTool.getQuery();
		

		// String recordsOfToday = MongoREST.getDocuments(query);
		String recordsOfToday = database.getDocuments(query);

		Log.d("RECORDS", recordsOfToday);

		// return MongoREST.extractDayAvg(recordsOfToday);
		return database.extractDayAvg(recordsOfToday);
	}

	private void setScoreLevel() {
		scoreImage = (ImageView) findViewById(R.id.imageView1);
		Double value = score.getScore();
		switch (value.intValue()) {

		case 0:
			setLevel(ScoreLevel.LEVEL_ZERO);
			break;
		case 1:
			setLevel(ScoreLevel.LEVEL_ONE);
			break;
		case 2:
			setLevel(ScoreLevel.LEVEL_TWO);
			break;
		case 3:
			setLevel(ScoreLevel.LEVEL_THREE);
			break;
		case 4:
			setLevel(ScoreLevel.LEVEL_FOUR);
			break;
		case 5:
			setLevel(ScoreLevel.LEVEL_FIVE);
			break;
		case 6:
			setLevel(ScoreLevel.LEVEL_SIX);
			break;
		case 7:
			setLevel(ScoreLevel.LEVEL_SEVEN);
			break;
		case 8:
			setLevel(ScoreLevel.LEVEL_EIGHT);
			break;
		case 9:
			setLevel(ScoreLevel.LEVEL_NINE);
			break;
		case 10:
			setLevel(ScoreLevel.LEVEL_TEN);
			break;
		default:
			setLevel(ScoreLevel.LEVEL_ZERO);
			break;
		}
	}

	private void setLevel(ScoreLevel levelZero) {
		this.level = levelZero;
	}
	
	public ScoreLevel getLevel(){
		return this.level;
	}
	
	public double getScoreValue(){
		return score.getScore();
	}
	
	public double getScoreAvg(){
		return scoreAvg;
	}
	
	public ImageView getScoreImage(){
		return scoreImage;
	}
	
	public TextView getScoremsg(){
		return scoremsg;
	}

}
