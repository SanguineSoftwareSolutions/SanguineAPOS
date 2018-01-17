package com.example.apos.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import android.widget.RelativeLayout;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.util.Random;

public class Chart extends View
{
   /* private GraphicalView ChartView1;
    private GraphicalView mChartView2;
    static int count=5;
    int[] Mycolors = new int[] { Color.RED, Color.parseColor("#FA58F4"),
            Color.parseColor("#0B0B61"),
            Color.parseColor("#800080"),Color.parseColor("#008000"),Color.GRAY };
    public Intent execute(Context context, RelativeLayout parent)
    {
        int[] colors = new int[count];
        for(int i=0;i<count;i++)
        {
            colors[i]=Mycolors[i];
        }
        DefaultRenderer renderer = buildCategoryRenderer(colors);
        renderer.setPanEnabled(false);// Disable User Interaction
        renderer.setLabelsColor(Color.BLACK);
        renderer.setShowLabels(true);
//renderer.setChartTitle("Total Assets");
        renderer.setLabelsTextSize(12);
        CategorySeries categorySeries = new CategorySeries("Fruits");
        categorySeries.add("Apple", 36);
        categorySeries.add("Banana", 23);
        categorySeries.add("Grapes", 30);
        categorySeries.add("Guava", 8);
        categorySeries.add("Orange", 3);
        ChartView1= ChartFactory.getPieChartView(context, categorySeries,renderer);
        parent.addView(ChartView1);
        return ChartFactory.getPieChartIntent(context, categorySeries, renderer,null);
    }
    protected DefaultRenderer buildCategoryRenderer(int[] colors)
    {
        DefaultRenderer renderer = new DefaultRenderer();
        for (int color : colors) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();

            r.setColor(color);
            renderer.addSeriesRenderer(r);
        }
        return renderer;
    }

    */

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float[] value_degree;
    RectF rectf = new RectF(150, 150, 380, 380);
    float temp = 0;

    public Chart(Context context, float[] values) {
        super(context);
        value_degree = new float[values.length];
        for (int i = 0; i<values.length; i++)
        {
            value_degree[i] = values[i];
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Random r;
        for (int i = 0; i < value_degree.length; i++) {
            if (i == 0) {
                r = new Random();
                int color = Color.argb(100, r.nextInt(256), r.nextInt(256),
                        r.nextInt(256));
                paint.setColor(color);
                canvas.drawArc(rectf, 0, value_degree[i], true, paint);
            } else {
                temp += value_degree[i - 1];
                r = new Random();
                int color = Color.argb(255, r.nextInt(256), r.nextInt(256),
                        r.nextInt(256));
                paint.setColor(color);
                canvas.drawArc(rectf, temp, value_degree[i], true, paint);
            }
        }
    }
}
