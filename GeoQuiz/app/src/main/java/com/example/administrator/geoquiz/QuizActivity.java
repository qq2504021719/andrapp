package com.example.administrator.geoquiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mShangButton;
    private Button mNextButton;
    private TextView mQuestionTextView;

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";

    private Question[] mQuestionBank = new Question[]{
        new Question(R.string.question_oceans,true),
        new Question(R.string.question_mideast,true),
        new Question(R.string.question_africa,true),
        new Question(R.string.question_americas,true),
        new Question(R.string.question_asia,true),
    };

    private int mCurrentIndex = 0;

    /*
    *
    * 更新TextView显示的内容
    * */
    private void updateQuestion(int is){
//        Log.d(TAG,"updateQuestion 方法调用 mCurrentIndex="+mCurrentIndex,new Exception());
        if(is == 1){
            mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
        }
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    /*
    * 用户点击提示
    * */
    private void checkAnswer(boolean userPressedTrue){

        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId = 0;

        if(userPressedTrue == answerIsTrue){
            messageResId = R.string.correct_toast;
        }else{
            messageResId = R.string.incorrect_toast;
        }

        Toast.makeText(this,messageResId, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Log.d(TAG,"onCreate(Bundle) 调用");

        if(savedInstanceState != null){
            int key_index = savedInstanceState.getInt(KEY_INDEX,0);
            if(key_index > 0){
                mCurrentIndex = key_index;
            }
        }

        // 显示问题到TextView
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        updateQuestion(2);

        // 监听上一步
        mShangButton = (Button) findViewById(R.id.shang_button);
        mShangButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(mCurrentIndex > 0){
                    mCurrentIndex = mCurrentIndex - 1;
                }else{
                    mCurrentIndex = mQuestionBank.length-1;
                }
                updateQuestion(2);
            }
        });

        // 监听下一步 递增数组索引并更新显示TextView的文本内容
        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                updateQuestion(1);
            }
        });

        // TextView监听,点击更换内容
        mQuestionTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                updateQuestion(1);
            }
        });


        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                checkAnswer(true);
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                checkAnswer(false);
            }
        });
    }

    /*
    * 存储Activity销毁需要保存的变量
    * */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG,"onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX,mCurrentIndex);
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG,"onStart() 调用");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG,"onPause() 调用");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG,"onResume() 调用");

    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG,"onStop() 调用");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG,"onDestroy() 调用");
    }
}
