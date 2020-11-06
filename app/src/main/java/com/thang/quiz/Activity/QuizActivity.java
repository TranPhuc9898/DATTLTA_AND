package com.thang.quiz.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thang.quiz.R;
import com.thang.quiz.Item.Question;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class QuizActivity extends AppCompatActivity {

	FirebaseDatabase database;
	DatabaseReference myRef;

	Question qAndA = new Question();

	int ques, score, ans, nextC;
	float percentage;
	boolean submit;

	ArrayList<Integer> Answers;
	ArrayList<Integer> arrayListTime;
	Runnable rTime;
	Handler handler;
	int iTime;

	String q_nos;

	@BindView(R.id.q_numbers)
	TextView q_no;

	@BindView(R.id.question)
	TextView questions;

	@BindView(R.id.q_times)
	TextView txtTimes;

	@BindView(R.id.optionA)
	RadioButton opA;

	@BindView(R.id.optionB)
	RadioButton opB;

	@BindView(R.id.optionC)
	RadioButton opC;

	@BindView(R.id.optionD)
	RadioButton opD;

	@BindView(R.id.options)
	RadioGroup optionsGroup;

	@BindView(R.id.prev)
	Button prevButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);

		ButterKnife.bind(this);
		Intent i = getIntent();
		qAndA = (Question) i.getSerializableExtra("question");
		q_nos = "Question: " + 1 + "/" + qAndA.question.size();
		questions = findViewById(R.id.question);
		questions.setText("Quiz");
		prevButton.setVisibility(View.GONE);
		Answers = new ArrayList<>();
		arrayListTime = new ArrayList<>();

		ques = -1;
		score = 0;
		ans = 0;
		nextC = 0;
		submit = true;
		goNext();
	}

	private void CountTime()
	{
		iTime = 15;
		txtTimes.setText("Time: "+iTime);
		handler = new Handler();
		rTime = new Runnable() {
			@Override
			public void run() {
				if(iTime != 0)
				{
					iTime--;
					txtTimes.setText("Time: "+iTime);
					handler.postDelayed(rTime,1000);
				}
				else {
					goNext();
				}
			}
		};
		handler.postDelayed(rTime,1000);
	}

	public void goNext() {

		ques++;

		if (ques >= qAndA.question.size()) {
			ques = qAndA.question.size() - 1;
		}

		q_no.setVisibility(View.VISIBLE);
		opA.setVisibility(View.VISIBLE);
		opB.setVisibility(View.VISIBLE);

		if (qAndA.results.get(ques).getType().equals("boolean")) {
			opC.setVisibility(View.GONE);
			opD.setVisibility(View.GONE);
		} else {
			opC.setVisibility(View.VISIBLE);
			opD.setVisibility(View.VISIBLE);
		}

		q_nos = "Question: " + (ques + 1) + "/" + qAndA.question.size();
		q_no.setText(q_nos);
		questions.setText(qAndA.question.get(ques));
		opA.setText(qAndA.optA.get(ques));
		opB.setText(qAndA.optB.get(ques));
		opC.setText(qAndA.optC.get(ques));
		opD.setText(qAndA.optD.get(ques));
		try {
			if (Answers.get(ques) == 1) {
				opA.setChecked(true);
			} else if (Answers.get(ques) == 2) {
				opB.setChecked(true);
			} else if (Answers.get(ques) == 3) {
				opC.setChecked(true);
			} else if (Answers.get(ques) == 4) {
				opD.setChecked(true);
			} else {
				optionsGroup.clearCheck();
			}
		} catch (Exception e) {
			optionsGroup.clearCheck();
		}
		CountTime();
	}

	public void clickPrev(View view) {

		int selectedId = optionsGroup.getCheckedRadioButtonId();
		switch (selectedId) {
			case R.id.optionA:
				ans = 1;
				break;
			case R.id.optionB:
				ans = 2;
				break;
			case R.id.optionC:
				ans = 3;
				break;
			case R.id.optionD:
				ans = 4;
				break;
			default:
				ans = 0;
		}
		if (ques >= 0) {
			try {
				Answers.set(ques, ans);
			} catch (Exception e) {
				Answers.add(ques, ans);
			}
		}
		if (ques < qAndA.question.size() - 1) {
			goPrev();
		} else if (ques == qAndA.question.size() - 1) {
			Button button = findViewById(R.id.next);
			button.setVisibility(View.VISIBLE);
			button = findViewById(R.id.submit);
			button.setVisibility(View.INVISIBLE);
			optionsGroup.clearCheck();
			goPrev();
		}
		if (ques == 0)
			prevButton.setVisibility(View.GONE);
		nextC--;
		ans = 0;
	}

	public void goPrev() {
		ques--;

		q_no.setVisibility(View.VISIBLE);
		opA.setVisibility(View.VISIBLE);
		opB.setVisibility(View.VISIBLE);

		if (qAndA.results.get(ques).getType().equals("boolean")) {
			opC.setVisibility(View.GONE);
			opD.setVisibility(View.GONE);
		} else {
			opC.setVisibility(View.VISIBLE);
			opD.setVisibility(View.VISIBLE);
		}

		q_nos = "Question: " + (ques + 1) + "/" + qAndA.question.size();
		q_no.setText(q_nos);
		questions.setText(qAndA.question.get(ques));
		opA.setText(qAndA.optA.get(ques));
		opB.setText(qAndA.optB.get(ques));
		opC.setText(qAndA.optC.get(ques));
		opD.setText(qAndA.optD.get(ques));
		try {
			if (Answers.get(ques) == 1) {
				opA.setChecked(true);
			} else if (Answers.get(ques) == 2) {
				opB.setChecked(true);
			} else if (Answers.get(ques) == 3) {
				opC.setChecked(true);
			} else if (Answers.get(ques) == 4) {
				opD.setChecked(true);
			} else {
				optionsGroup.clearCheck();
			}
		} catch (Exception e) {
			optionsGroup.clearCheck();

		}
	}

	public void clickSubmit(final View view) {
		if(ques != 0)
		{
			arrayListTime.add(15 - iTime);
		}
		AlertDialog.Builder alertConfirm = new AlertDialog.Builder(this);
		alertConfirm.setTitle("Confirm Submission");
		alertConfirm.setMessage("Do you want to submit quiz?");
		alertConfirm.setCancelable(true);
		alertConfirm.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		alertConfirm.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				submit(view);
				database = FirebaseDatabase.getInstance();
				myRef = database.getReference("highscore");
				SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
					myRef.child(sharedPreferences.getString(getString(R.string.difficulty_key)," ")).child("catergaries:"+Integer.parseInt(sharedPreferences.getString(getString(R.string.category_key)," "))).
							child(""+score+"_"+arrayListTime.stream().mapToInt(a->a).sum()).setValue(HomeActivity.getStringName(getApplicationContext()));
				}
				Toast.makeText(QuizActivity.this, "123123", Toast.LENGTH_SHORT).show();
			}
		});
		AlertDialog dialog = alertConfirm.create();
		dialog.show();
	}

	public void submit(View view) {
		clickNext(view);
		if (submit)
			checkScore();
		submit = false;
		percentage = (float) (score * 100) / qAndA.question.size();
//		prevButton.setVisibility(View.INVISIBLE);
		prevButton.setVisibility(View.GONE);
		opA.setClickable(false);
		opB.setClickable(false);
		opC.setClickable(false);
		opD.setClickable(false);
		LayoutInflater inflater = getLayoutInflater();
		View alertLayout = inflater.inflate(R.layout.alert_dialog, null);
		final ProgressBar progressBar = alertLayout.findViewById(R.id.circularProgressbar);
		final TextView textView = alertLayout.findViewById(R.id.tv);
		Drawable drawable = getResources().getDrawable(R.drawable.circular);
		progressBar.setMax(qAndA.question.size());
		progressBar.setSecondaryProgress(qAndA.question.size());
		progressBar.setProgress(score);
		progressBar.setProgressDrawable(drawable);
		textView.setText((int) percentage + "%");

		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("RESULT");
		alert.setMessage("You scored " + score + " out of " + qAndA.question.size() + " questions.");
		alert.setView(alertLayout);
		alert.setCancelable(false);

		alert.setNegativeButton("Exit", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				moveTaskToBack(true);
				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(1);
			}
		});

		alert.setPositiveButton("View Solutions", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				clickSolutions();
			}
		});
		AlertDialog dialog = alert.create();
		dialog.show();
	}

	public void clickNext(View view) {
		if(ques != 0)
		{
			arrayListTime.add(15 - iTime);
		}
		handler.removeCallbacks(rTime);
		int selectedId = optionsGroup.getCheckedRadioButtonId();
		switch (selectedId) {
			case R.id.optionA:
				ans = 1;
				break;
			case R.id.optionB:
				ans = 2;
				break;
			case R.id.optionC:
				ans = 3;
				break;
			case R.id.optionD:
				ans = 4;
				break;
			default:
				ans = 0;
		}

		if (ques >= 0) {
			try {
				Answers.set(ques, ans);
			} catch (Exception e) {
				Answers.add(ques, ans);
			}
		}
		if (ques < qAndA.question.size() - 2) {
			optionsGroup.clearCheck();
			goNext();
		} else if (ques == qAndA.question.size() - 2) {
			Button button = findViewById(R.id.next);
			button.setVisibility(View.INVISIBLE);
			button = findViewById(R.id.submit);
			button.setVisibility(View.VISIBLE);
			optionsGroup.clearCheck();
			goNext();
		}
		if (ques > 0)
			prevButton.setVisibility(View.GONE);
//			prevButton.setVisibility(View.VISIBLE);

		nextC++;
		ans = 0;
	}

	public void checkScore() {
		if (ques != -1)
			for (int i = 0; i < Answers.size(); i++) {
				if (qAndA.Answer.get(i).equals(Answers.get(i))) {
					score++;
				}
			}
	}

	public void clickSolutions() {
		Intent solutions = new Intent(QuizActivity.this, SolutionActivity.class);
		solutions.putIntegerArrayListExtra("Answers", Answers);
		solutions.putStringArrayListExtra("Question", (ArrayList<String>) qAndA.question);
		solutions.putStringArrayListExtra("optA", (ArrayList<String>) qAndA.optA);
		solutions.putStringArrayListExtra("optB", (ArrayList<String>) qAndA.optB);
		solutions.putStringArrayListExtra("optC", (ArrayList<String>) qAndA.optC);
		solutions.putStringArrayListExtra("optD", (ArrayList<String>) qAndA.optD);
		solutions.putIntegerArrayListExtra("Answer", (ArrayList<Integer>) qAndA.Answer);
		solutions.putIntegerArrayListExtra("Times", arrayListTime);
		solutions.putExtra("Score",score);
		startActivity(solutions);
		finish();
//		Log.d("asdasd",Answers.toString());
//		Log.d("asdasd",qAndA.question.toString());
//		Log.d("asdasd",qAndA.optA.toString());
//		Log.d("asdasd",qAndA.optB.toString());
//		Log.d("asdasd",qAndA.optC.toString());
//		Log.d("asdasd",qAndA.optD.toString());
//		Log.d("asdasd",qAndA.Answer.toString());
	}

	@Override
	public void onBackPressed() {
		Toast.makeText(this, "Back Press is not allowed", Toast.LENGTH_LONG).show();
	}
}
