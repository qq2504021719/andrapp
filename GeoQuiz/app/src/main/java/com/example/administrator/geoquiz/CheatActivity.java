package com.example.administrator.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE = "com.example.administrator.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.example.administrator.geoquiz.answer_shown";

    private static final String CHEAT_MISONCLIC = "mIsOnClickShowAnswer";
    private static final String CHEAT_MISONCLICS = "mIsOnClickShowAnswers";

    private boolean mAnswerIsTrue;

    private TextView mAnswerTextView;
    private Button mShowAnswer;

    private boolean mIsOnClickShowAnswer = false;


    /*
    * 封装创建Intent对象,并传入参数
    * */
    public static Intent newIntent(Context packageContext, boolean answerIsTrue){
        Intent i = new Intent(packageContext,CheatActivity.class);
        i.putExtra(EXTRA_ANSWER_IS_TRUE,answerIsTrue);
        return i;
    }

    /*
    * 给父Activity解析传递过去的信息
    * */
    public static boolean wasAnswerShown(Intent result){
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN,false);
    }

    /*
    * 给父Activity返回数据
    * */
    private void setAnswerShownResult(boolean isAnswerShown){
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN,isAnswerShown);
        setResult(RESULT_OK,data);
    }

    /*
    * 显示文本框内容
    * */
    private void ShowTextView(){
        if(mIsOnClickShowAnswer){
            if(mAnswerIsTrue){
                mAnswerTextView.setText(R.string.true_button);
            }else{
                mAnswerTextView.setText(R.string.false_button);
            }
            setAnswerShownResult(true);
        }
    }

    @Override
    protected void onCreate(Bundle outState) {
        super.onCreate(outState);
        setContentView(R.layout.activity_cheat);

        // Activity重新创建获取数据
        if(outState != null){
            mIsOnClickShowAnswer = outState.getBoolean(CHEAT_MISONCLIC);
            mAnswerIsTrue = outState.getBoolean(CHEAT_MISONCLICS);
            if(mIsOnClickShowAnswer){
                ShowTextView();
            }
        }else{
            // 获取QuizActivity传来的参数
            mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE,false);
        }



        // 点击显示答案按钮,将答案显示在TextView上
        mAnswerTextView = (TextView) findViewById(R.id.answerTextView);

        mShowAnswer = (Button)findViewById(R.id.showAnswerButton);
        mShowAnswer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mIsOnClickShowAnswer = true;
                ShowTextView();

                // 加动画效果,第102页,效果没有作用
                // 此效果只有21及以上才有,所有加了判断,运行设备API级别大于本应用目标版本
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){

                    int cx = mShowAnswer.getWidth()/2;
                    int cy = mShowAnswer.getHeight()/2;
                    float radius = mShowAnswer.getWidth();
                    Animator anim = ViewAnimationUtils.createCircularReveal(mShowAnswer,cx,cy,radius,0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation){
                            super.onAnimationEnd(animation);
                            mShowAnswer.setVisibility(View.INVISIBLE);
                        }
                    });
                }else{
                    mShowAnswer.setVisibility(View.INVISIBLE);
                }

            }
        });
    }

    /*
    * 存储用户重新创建Activity需要存储的信息
    * */
    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putBoolean(CHEAT_MISONCLIC,mIsOnClickShowAnswer);
        outState.putBoolean(CHEAT_MISONCLICS,mAnswerIsTrue);

    }
}
