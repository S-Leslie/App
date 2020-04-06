package com.a_s.spurs;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ZoomControls;

public class Image extends Activity {
    Point size = new Point(0,0);
    Point start = new Point(0,0);
    NestedScrollView NV;
    HorizontalScrollView HV;
    ImageView IV;
    FrameLayout FL;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_image);
        Bitmap BM = null;
        try {
            BM = BitmapFactory.decodeFile(getIntent().getStringExtra("img"));
        int width = getWindowManager().getDefaultDisplay().getWidth();
        int height = width*BM.getHeight()/BM.getWidth();
        size.set(width, height);
        start.set(0, 0);
        DrawImage();

        ZoomControls ZC = findViewById(R.id.zoom);
        ZC.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZC_ZoomInClick();
            }
        });
        ZC.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZC_ZoomOutClick();
            }
        });
        }catch (java.lang.OutOfMemoryError error){
            Toast.makeText(getApplicationContext(), "Картинка большая памяти мало, а ручки кривые. Увеличить не получится.", Toast.LENGTH_LONG).show();
            this.finish();
        }
    }

    private void ZC_ZoomOutClick()
    {
        double step = 0.8;
        ZoomChange(step);
    }

    private void ZC_ZoomInClick()
    {
        double step = 1.25;
        ZoomChange(step);
    }

    private void ZoomChange(double step)
    {
        if (size.x * step > 10 && size.y * step > 10 && size.x * step < 10000 && size.y * step < 10000)
        {
            size.set((int)(size.x * step), (int)(size.y * step));
            start.x = (int)(start.x * size.x / (size.x - step));
            start.y = (int)(start.y * size.y / (size.y - step));
            ReDrawImage();
        }
    }

    private void DrawImage()
    {
        NV = findViewById(R.id.scroll);
        FL = new FrameLayout(this);
        String uri = getIntent().getStringExtra("img");
        IV = new ImageView(this);
        IV.setImageURI(Uri.parse(uri));
        IV.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ViewGroup.LayoutParams LP = new ViewGroup.LayoutParams(size.x, size.y);
        FL.addView(IV,LP);
        NV.addView(FL);
    }

    private void ReDrawImage()
    {
        FL.removeAllViews();
        ViewGroup.LayoutParams LP = new ViewGroup.LayoutParams(size.x, size.y);
        FL.addView(IV,LP);
    }
}
