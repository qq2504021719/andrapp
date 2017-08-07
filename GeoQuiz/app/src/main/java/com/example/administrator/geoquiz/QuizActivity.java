package com.example.administrator.geoquiz;

import android.app.Activity;
import android.content.Intent;
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
    private Button mCheatButton;
    private Button mNextButton;

    private TextView mQuestionTextView;

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_INDEX_S = "index_s";
    private static final String KEY_INDEX_MIS = "index_mis";
    private static final int REQUEST_CODE_CHEAT = 0;

    private Question[] mQuestionBank = new Question[]{
        new Question(R.string.question_oceans,true),
        new Question(R.string.question_mideast,true),
        new Question(R.string.question_africa,true),
        new Question(R.string.question_americas,true),
        new Question(R.string.question_asia,true),
    };

    private int mCurrentIndex = 0;
    private boolean mIsCheater;
    private int mIsCheaterNum = 0;

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
    * 用户点击显示提示
    * */
    private void checkAnswer(boolean userPressedTrue){

        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId = 0;

        if (mIsCheater){
            mIsCheaterNum = mIsCheaterNum+1;
            messageResId = R.string.judgment_toast;
        } else {
            if (userPressedTrue == answerIsTrue){
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }

        showTiShow(messageResId);

    }

    /*
    * 显示查看答案提示
    * */
    private void ChakanDaAn(){
        if(mIsCheater){
            int messageResId = 0;
            mIsCheaterNum = mIsCheaterNum+1;
            messageResId = R.string.judgment_toast;
            showTiShow(messageResId);
        }
    }

    /*
    * 查看查看答案显示了几次,大于等于1就不显示了
    * */
    private void IsChaKanDaAn(){
        if(mIsCheaterNum == 1){
            mIsCheater = false;
            mIsCheaterNum = 0;
        }
    }

    /*
    * 显示提示
    * */
    private void showTiShow(int essageResId){
        Toast.makeText(this,essageResId, Toast.LENGTH_SHORT).show();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Log.d(TAG,"onCreate(Bundle) 调用");

        if(savedInstanceState != null){
            int key_index = savedInstanceState.getInt(KEY_INDEX,0);
            mIsCheaterNum = savedInstanceState.getInt(KEY_INDEX_S,0);
            if(key_index > 0){
                mCurrentIndex = key_index;
            }
            mIsCheater = savedInstanceState.getBoolean(KEY_INDEX_MIS);

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
                IsChaKanDaAn();
                ChakanDaAn();
                updateQuestion(2);
            }
        });

        // 点击创建Activity显示答案
        mCheatButton = (Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // 创建Intent对象
//                Intent i = new Intent(QuizActivity.this,CheatActivity.class);
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent i = CheatActivity.newIntent(QuizActivity.this,answerIsTrue);
                // 启动新的Activity
//                startActivity(i);
                startActivityForResult(i,REQUEST_CODE_CHEAT);
            }
        });

        // 监听下一步 递增数组索引并更新显示TextView的文本内容
        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                IsChaKanDaAn();
                ChakanDaAn();
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
                IsChaKanDaAn();
                checkAnswer(true);
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                IsChaKanDaAn();
                checkAnswer(false);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        if(resultCode != Activity.RESULT_OK){
            return;
        }
        if(requestCode == REQUEST_CODE_CHEAT){
            if(data == null){
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }

    /*
    * 存储Activity销毁需要保存的变量
    * */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
//        Log.i(TAG,"onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX,mCurrentIndex);
        savedInstanceState.putInt(KEY_INDEX_S,mIsCheaterNum);
        savedInstanceState.putBoolean(KEY_INDEX_MIS,mIsCheater);

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
