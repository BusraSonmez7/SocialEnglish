package com.busrasonmez.socialenglish;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.busrasonmez.socialenglish.education.Education;
import com.busrasonmez.socialenglish.social_media.SocialMedia;

import de.hdodenhof.circleimageview.CircleImageView;

public class main_page extends AppCompatActivity{

    private CircleImageView right, left, sm, work;
    Animation animation;
    TextView textView;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.right);
        right = findViewById(R.id.right_button);
        left = findViewById(R.id.left_button);
        sm = findViewById(R.id.sm_img);
        work = findViewById(R.id.work_img);
        textView = findViewById(R.id.txtsm);

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SocialMedia.class);
                startActivity(intent);
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Education.class);
                startActivity(intent);
            }
        });

//        right.setOnClickListener(main_page.this);
//        right.setOnTouchListener(gestureListener);
//
//        right.startAnimation(animation);
//
//        gestureDetector = new GestureDetector(this, new MyGestureDetector());
//        gestureListener = new View.OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent event) {
//                return gestureDetector.onTouchEvent(event);
//            }
//        };
        //right.setOnTouchListener(this);

    }

//    public boolean onTouch(View v, MotionEvent event) {
//        float x = event.getX();
//        float y = event.getY();
//        int action = event.getAction();
//        int edgeFlags = event.getEdgeFlags();
//
//        switch (edgeFlags) {
//
//            case MotionEvent.ACTION_DOWN: {
//                textView.setText("sağa kaydı");
//                Intent intent = new Intent(getApplicationContext(),register.class);
//                startActivity(intent);
//// Here u can write code which is executed after the user touch on the screen break;
//                break;
//            }
//
//            case MotionEvent.EDGE_LEFT: {
//                textView.setText("sola kaydı");
//                Intent intent = new Intent(getApplicationContext(),user_login.class);
//                startActivity(intent);
//// Here u can write code which is executed after the user release the touch on the screen break;
//                break;
//
//            }
//
//
//        }
//
//        return true;
//
//    }
//
//    @Override
//    public void onClick(View v) {
//        Filter f = (Filter) v.getTag();
//    }
//
//    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
//        @Override
//        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//            try {
//                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
//                    return false;
//                // right to left swipe
//                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//                    Toast.makeText(main_page.this, "Left Swipe", Toast.LENGTH_SHORT).show();
//                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//                    Toast.makeText(main_page.this, "Right Swipe", Toast.LENGTH_SHORT).show();
//                }
//            } catch (Exception e) {
//                // nothing
//            }
//            return false;
//        }
//
//        @Override
//        public boolean onDown(MotionEvent e) {
//            return true;
//        }
//    }
}