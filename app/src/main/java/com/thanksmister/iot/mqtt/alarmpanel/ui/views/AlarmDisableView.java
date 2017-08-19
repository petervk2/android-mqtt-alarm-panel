/*
 * <!--
 *   ~ Copyright (c) 2017. ThanksMister LLC
 *   ~
 *   ~ Licensed under the Apache License, Version 2.0 (the "License");
 *   ~ you may not use this file except in compliance with the License. 
 *   ~ You may obtain a copy of the License at
 *   ~
 *   ~ http://www.apache.org/licenses/LICENSE-2.0
 *   ~
 *   ~ Unless required by applicable law or agreed to in writing, software distributed 
 *   ~ under the License is distributed on an "AS IS" BASIS, 
 *   ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 *   ~ See the License for the specific language governing permissions and 
 *   ~ limitations under the License.
 *   -->
 */

package com.thanksmister.iot.mqtt.alarmpanel.ui.views;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.things.contrib.driver.pwmspeaker.Speaker;
import com.thanksmister.iot.mqtt.alarmpanel.BoardDefaults;
import com.thanksmister.iot.mqtt.alarmpanel.MusicNotes;
import com.thanksmister.iot.mqtt.alarmpanel.R;
import com.todddavies.components.progressbar.ProgressWheel;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

import static android.content.ContentValues.TAG;

public class AlarmDisableView extends LinearLayout {

    public static final int MAX_CODE_LENGTH = 4;

    @Bind(R.id.pinCode1)
    ImageView pinCode1;

    @Bind(R.id.pinCode2)
    ImageView pinCode2;

    @Bind(R.id.pinCode3)
    ImageView pinCode3;

    @Bind(R.id.pinCode4)
    ImageView pinCode4;

    @Bind(R.id.button0)
    View button0;

    @Bind(R.id.button1)
    View button1;

    @Bind(R.id.button2)
    View button2;

    @Bind(R.id.button3)
    View button3;

    @Bind(R.id.button4)
    View button4;

    @Bind(R.id.button5)
    View button5;

    @Bind(R.id.button6)
    View button6;

    @Bind(R.id.button7)
    View button7;

    @Bind(R.id.button8)
    View button8;

    @Bind(R.id.button9)
    View button9;

    @Bind(R.id.buttonDel)
    View buttonDel;

    @Bind(R.id.buttonCancel)
    View buttonCancel;

    @Bind(R.id.countDownProgressWheel)
    ProgressWheel countDownProgressWheel;

    private boolean codeComplete = false;
    private String enteredCode = "";
    private ViewListener listener;
    private int code;
    private int displaySeconds;
    private CountDownTimer countDownTimer;
    private Handler handler;

    private Speaker mSpeaker;
  
    public AlarmDisableView(Context context) {
        super(context);
    }

    public AlarmDisableView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {

        super.onFinishInflate();

        ButterKnife.bind(this);

        try {
            mSpeaker = new Speaker(BoardDefaults.getPwmPin());
            mSpeaker.stop(); // in case the PWM pin was enabled already
        } catch (IOException e) {
            Log.e(TAG, "Error initializing speaker");
        }

        button0.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                playBuzzerOnButtonPress();
                addPinCode("0");
            }
        });

        button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                playBuzzerOnButtonPress();
                addPinCode("1");
            }
        });

        button2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                playBuzzerOnButtonPress();
                addPinCode("2");
            }
        });

        button3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                playBuzzerOnButtonPress();
                addPinCode("3");
            }
        });

        button4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                playBuzzerOnButtonPress();
                addPinCode("4");
            }
        });

        button5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                playBuzzerOnButtonPress();
                addPinCode("5");
            }
        });

        button6.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                playBuzzerOnButtonPress();
                addPinCode("6");
            }
        });

        button7.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                playBuzzerOnButtonPress();
                addPinCode("7");
            }
        });

        button8.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                playBuzzerOnButtonPress();
                addPinCode("8");
            }
        });

        button9.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                playBuzzerOnButtonPress();
                addPinCode("9");
            }
        });

        buttonDel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                playBuzzerOnButtonPress();
                removePinCode();
            }
        });

        buttonDel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                playBuzzerOnButtonPress();
                removePinCode();
            }
        });

        buttonCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                playBuzzerOnButtonPress();
                clear();
                listener.onCancel();
            }
        });
    }

    public void playBuzzerOnButtonPress() {
        double note = MusicNotes.DRAMATIC_THEME[0];
        try {
            mSpeaker.play(note);
            Thread.sleep(100);
            mSpeaker.stop();
            button0.setEnabled(true);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void startCountDown(int pendingTime) {
        Timber.d("startCountDown: "+ pendingTime*1000);
        final int divideBy = 360/pendingTime;
        countDownTimer = new CountDownTimer(pendingTime*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                displaySeconds = (int) (millisUntilFinished / 1000);
                Animation an = new RotateAnimation(0.0f, 90.0f, 250f, 273f);
                an.setFillAfter(true);
                countDownProgressWheel.setText(String.valueOf(displaySeconds));
                countDownProgressWheel.setProgress(displaySeconds * divideBy);
            }

            @Override
            public void onFinish() {
                Timber.d("Timed up...");
                listener.onCancel();
            }
        }.start();
    }

    @Override
    protected void onDetachedFromWindow() {

        super.onDetachedFromWindow();
        if(countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }

        if (mSpeaker != null) {
            try {
                mSpeaker.stop();
                mSpeaker.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing speaker", e);
            } finally {
                mSpeaker = null;
            }
        }
    }
    
    public void setCode(int code) {
        this.code = code;
    }

    public void setListener(@NonNull ViewListener listener) {
        this.listener = listener;
    }

    public interface ViewListener {
        void onComplete();
        void onError();
        void onCancel();
    }

    private void clear() {
        codeComplete = false;
        enteredCode = "";
        showFilledPins(0);
    }

    private void addPinCode(String code) {
        if (codeComplete)
            return;

        enteredCode += code;

        showFilledPins(enteredCode.length());

        if (enteredCode.length() == MAX_CODE_LENGTH) {
            codeComplete = true;
            handler = new Handler();
            handler.postDelayed(delayRunnable, 500);
        }
    }

    private Runnable delayRunnable = new Runnable() {
        @Override
        public void run() {
            handler.removeCallbacks(delayRunnable);
            validateCode(enteredCode);
        }
    };

    private void removePinCode() {
        if (codeComplete) {
            return;
        }

        if (!TextUtils.isEmpty(enteredCode)) {
            enteredCode = enteredCode.substring(0, enteredCode.length() - 1);
        }

        showFilledPins(enteredCode.length());
    }

    private void validateCode(String validateCode) {
        int codeInt = Integer.parseInt(validateCode);
        if(codeInt == code) {
            countDownTimer.cancel();
            countDownTimer = null;
            listener.onComplete();
        } else {
            clear();
            listener.onError();
        }
    }

    private void showFilledPins(int pinsShown) {
        switch (pinsShown) {
            case 1:
                pinCode1.setImageResource(R.drawable.ic_radio_button_checked_black_32dp);
                pinCode2.setImageResource(R.drawable.ic_radio_button_unchecked_black_32dp);
                pinCode3.setImageResource(R.drawable.ic_radio_button_unchecked_black_32dp);
                pinCode4.setImageResource(R.drawable.ic_radio_button_unchecked_black_32dp);
                break;
            case 2:
                pinCode1.setImageResource(R.drawable.ic_radio_button_checked_black_32dp);
                pinCode2.setImageResource(R.drawable.ic_radio_button_checked_black_32dp);
                pinCode3.setImageResource(R.drawable.ic_radio_button_unchecked_black_32dp);
                pinCode4.setImageResource(R.drawable.ic_radio_button_unchecked_black_32dp);
                break;
            case 3:
                pinCode1.setImageResource(R.drawable.ic_radio_button_checked_black_32dp);
                pinCode2.setImageResource(R.drawable.ic_radio_button_checked_black_32dp);
                pinCode3.setImageResource(R.drawable.ic_radio_button_checked_black_32dp);
                pinCode4.setImageResource(R.drawable.ic_radio_button_unchecked_black_32dp);
                break;
            case 4:
                pinCode1.setImageResource(R.drawable.ic_radio_button_checked_black_32dp);
                pinCode2.setImageResource(R.drawable.ic_radio_button_checked_black_32dp);
                pinCode3.setImageResource(R.drawable.ic_radio_button_checked_black_32dp);
                pinCode4.setImageResource(R.drawable.ic_radio_button_checked_black_32dp);
                break;
            default:
                pinCode1.setImageResource(R.drawable.ic_radio_button_unchecked_black_32dp);
                pinCode2.setImageResource(R.drawable.ic_radio_button_unchecked_black_32dp);
                pinCode3.setImageResource(R.drawable.ic_radio_button_unchecked_black_32dp);
                pinCode4.setImageResource(R.drawable.ic_radio_button_unchecked_black_32dp);
                break;
        }
    }
}