package com.thang.quiz.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.thang.quiz.R;
import com.thang.quiz.api.Api;
import com.thang.quiz.api.ApiCount;
import com.thang.quiz.api.QuizQuestion;
import com.thang.quiz.api.Result;
import com.thang.quiz.Item.Question;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomeActivity extends AppCompatActivity {
	Button start;
	Button filter;
	Button highscore;
	ProgressBar progressBar;
	Question q;
	String difficulty;
	String category;
	View.OnClickListener onClickListener = new View.OnClickListener() {
		@SuppressLint("NonConstantResourceId")
		@Override
		public void onClick(View view) {
			switch (view.getId())
			{
				case R.id.home_start:
				{
					progressBar.setVisibility(View.VISIBLE);
					q = new Question(getApplicationContext());
					view.setClickable(false);
					fetchQuestionAPI(10);
				}
				break;
				case R.id.home_filter:
				{
					Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				}break;
				case R.id.home_highscore:{
					startActivity(new Intent(HomeActivity.this,HighScoreActivity.class));
				}break;
				default:{

				}
			}
//			if (view.getId() == R.id.home_start) {
//				progressBar.setVisibility(View.VISIBLE);
//				q = new Question(getApplicationContext());
//				view.setClickable(false);
//				fetchQuestionCount();
//			} else if (view.getId() == R.id.home_filter) {
//				Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(intent);
//			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		setFilterDefaultValues();
		start = findViewById(R.id.home_start);
		filter = findViewById(R.id.home_filter);
		highscore = findViewById(R.id.home_highscore);
		progressBar = findViewById(R.id.progressBar2);
		start.setOnClickListener(onClickListener);
		filter.setOnClickListener(onClickListener);
		highscore.setOnClickListener(onClickListener);

		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		category = sharedPrefs.getString(
				getString(R.string.category_key),
				getString(R.string.medium_value)
		);

		difficulty = sharedPrefs.getString(
				getString(R.string.difficulty_key),
				getString(R.string.medium_value)
		);
	}

	private void setFilterDefaultValues() {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		difficulty = sharedPrefs.getString(
				getString(R.string.difficulty_key),
				null
		);
		category = sharedPrefs.getString(
				getString(R.string.category_key),
				null
		);
		if (difficulty == null) {
			sharedPrefs
					.edit()
					.putString(getString(R.string.difficulty_key), getString(R.string.easy_value))
					.apply();
		}
		if (category == null) {
			sharedPrefs
					.edit()
					.putString(getString(R.string.category_key), getString(R.string.any_category_value))
					.apply();
		}
	}

	private void fetchQuestionCount() {
		int category_value = Integer.parseInt(category);

		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(Api.BASE_URL)
				.addConverterFactory(GsonConverterFactory.create())
				.build();
		Api api = retrofit.create(Api.class);
		Call<ApiCount> call = api.getQuizQuestions(10,category_value);
		Log.d("123123",call.request().toString());
		call.enqueue(new Callback<ApiCount>() {
			@Override
			public void onResponse(Call<ApiCount> call, Response<ApiCount> response) {
				switch (difficulty) {
					case "easy":
						fetchQuestionAPI(response.body().getCategoryQuestionCount().getTotalEasyQuestionCount());
						break;
					case "medium":
						fetchQuestionAPI(response.body().getCategoryQuestionCount().getTotalMediumQuestionCount());
						break;
					case "hard":
						fetchQuestionAPI(response.body().getCategoryQuestionCount().getTotalHardQuestionCount());
						break;
				}
				Log.d("123123",response.toString());
			}

			@Override
			public void onFailure(Call<ApiCount> call, Throwable t) {
				Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
				progressBar.setVisibility(View.INVISIBLE);
				start.setClickable(true);
			}
		});
	}

	public void fetchQuestionAPI(int categoryCount) {
		int category_value = Integer.parseInt(category);
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(Api.BASE_URL)
				.addConverterFactory(GsonConverterFactory.create())
				.build();
		Api api = retrofit.create(Api.class);
//		Call<QuizQuestion> call = api.getQuizQuestions("url3986", categoryCount >= 10 ? 10 : categoryCount - 1, difficulty, category_value);
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		Call<QuizQuestion> call = api.getQuizQuestions("url3986", categoryCount >= 10 ? 10 : categoryCount - 1,
				sharedPreferences.getString(getString(R.string.difficulty_key)," ") , Integer.parseInt(sharedPreferences.getString(getString(R.string.category_key)," ")));
		call.enqueue(new Callback<QuizQuestion>() {
			@Override
			public void onResponse(Call<QuizQuestion> call, Response<QuizQuestion> response) {

				Log.v("url-----", call.request().url().toString());

				QuizQuestion quizQuestion = response.body();

				if (quizQuestion.getResponseCode() == 0) {

					q.results = quizQuestion.getResults();

					if (q.results != null) {
						for (Result r : q.results) {
							try {
								q.question.add(java.net.URLDecoder.decode(r.getQuestion(), "UTF-8"));
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
							Random random = new Random();
							// For Boolean Type Questions, only 2 possible options (True/False)
							// For multiple choices, 4 options are required.
							int ran = r.getType().equals("boolean")
									? random.nextInt(2)
									: random.nextInt(4);
							setOptions(r, ran);
							q.Answer.add(ran + 1);
						}
						Log.v("answers", q.Answer.toString());
					}
				}
				progressBar.setVisibility(View.INVISIBLE);
				start.setClickable(true);
				Intent intent = new Intent(HomeActivity.this, QuizActivity.class);
				intent.putExtra("question", q);
				Log.d("123123",response.toString());
				startActivity(intent);
			}

			@Override
			public void onFailure(Call<QuizQuestion> call, Throwable t) {
				Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
				progressBar.setVisibility(View.INVISIBLE);
				start.setClickable(true);
			}
		});
	}

	void setOptions(Result r, int ran) {
		List<String> wrong;
		switch (ran) {
			case 0:
				try {
					q.optA.add(java.net.URLDecoder.decode(r.getCorrectAnswer(), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

				wrong = r.getIncorrectAnswers();
				try {
					q.optB.add(java.net.URLDecoder.decode(wrong.get(0), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				// Options C, D are not applicable for Boolean Type Questions.
				if (r.getType().equals("boolean")) {
					q.optC.add("false");
					q.optD.add("false");
					return;
				}
				try {
					q.optC.add(java.net.URLDecoder.decode(wrong.get(1), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				try {
					q.optD.add(java.net.URLDecoder.decode(wrong.get(2), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				break;
			case 1:
				try {
					q.optB.add(java.net.URLDecoder.decode(r.getCorrectAnswer(), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

				wrong = r.getIncorrectAnswers();
				try {
					q.optA.add(java.net.URLDecoder.decode(wrong.get(0), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				// Options C, D are not applicable for Boolean Type Questions.
				if (r.getType().equals("boolean")) {
					q.optC.add("false");
					q.optD.add("false");
					return;
				}
				try {
					q.optC.add(java.net.URLDecoder.decode(wrong.get(1), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				try {
					q.optD.add(java.net.URLDecoder.decode(wrong.get(2), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				break;
			case 2:
				try {
					q.optC.add(java.net.URLDecoder.decode(r.getCorrectAnswer(), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

				wrong = r.getIncorrectAnswers();
				try {
					q.optA.add(java.net.URLDecoder.decode(wrong.get(0), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				try {
					q.optB.add(java.net.URLDecoder.decode(wrong.get(1), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				try {
					q.optD.add(java.net.URLDecoder.decode(wrong.get(2), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				break;
			case 3:
				try {
					q.optD.add(java.net.URLDecoder.decode(r.getCorrectAnswer(), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

				wrong = r.getIncorrectAnswers();
				try {
					q.optA.add(java.net.URLDecoder.decode(wrong.get(0), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				try {
					q.optB.add(java.net.URLDecoder.decode(wrong.get(1), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				try {
					q.optC.add(java.net.URLDecoder.decode(wrong.get(2), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				break;
		}
	}
}