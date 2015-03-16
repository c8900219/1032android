package com.wiwi.android0303;
import java.util.Random;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.text.Layout.Alignment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
public class PuzzleGame extends Activity {
	private MazeView myview;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myview = new MazeView(this);
		setContentView(R.layout.activity_main);/**/
		LinearLayout ll = (LinearLayout) findViewById(R.id.mylinear);
		ll.addView(myview);
	}
	private class MazeView extends View {
		private final int N = 3;
		private int maze[];
		private Random rnd;
		private int width, height;
		private int row, col;
		private Bitmap mBitmap, tBitmap;
		private boolean first = true;
		public MazeView(Context context) {
			super(context);
			setFocusable(true);
			setFocusableInTouchMode(true);
		}
		@Override
		public void onSizeChanged(int w, int h, int oldw, int oldh) {
			super.onSizeChanged(w, h, oldw, oldh);
			width = w;
			height = h;
			initialMazeView();
		}
		private void initialMazeView() {
			mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.img_puzz);
			if (width > mBitmap.getWidth());
			width = mBitmap.getWidth();
			if (height > mBitmap.getHeight());
			height = mBitmap.getHeight();
			initMaze();		
		}
		private void initMaze() {
			rnd = new Random();
			maze = new int[N * N];
			row = col = N - 1;
			for (int i = 0; i < N * N; i++)
				maze[i] = i;
		}
		private void swap(int m, int n) {
			int temp = maze[m];
			maze[m] = maze[n];
			maze[n] = temp;
		}
		private void up() {
			int n = row * N + col;
			if (row > 0)
			row--;
			int m = row * N + col;
			swap(m, n);
		}
		private void down() {
			int n = row * N + col;
			if (row < N - 1)
			row++;
			int m = row * N + col;
			swap(m, n);
		}
		private void left() {
			int n = row * N + col;
			if (col > 0)
			col--;
			int m = row * N + col;
			swap(m, n);
		}
		private void right() {
			int n = row * N + col;
			if (col < N - 1)
			col++;
			int m = row * N + col;
			swap(m, n);
		}
		@Override
		public void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			canvas.drawColor(Color.WHITE);
			int w = width/N;
			int h = height/N;
			Paint mPaint = new Paint();
			mPaint.setAntiAlias(true);
			for (int i = 0; i < N * N; i++) {
				mPaint.setColor(Color.RED);
				mPaint.setTextAlign(Align.CENTER);
				int c = i % N * w;
				int r = i / N * h;
				int x = maze[i] % N * w;
				int y = maze[i] / N * h;
				tBitmap = Bitmap.createBitmap(mBitmap, x, y, w, h);
				if (maze[i] != N * N - 1)
					canvas.drawBitmap(tBitmap, c, r, null);
				// canvas.drawText(maze[i]+"",c+w/2,r+h/2,mPaint);
				mPaint.setStyle(Paint.Style.STROKE);
				mPaint.setColor(Color.BLACK);
				canvas.drawLine(c, 0, c, height, mPaint);
				canvas.drawLine(0, r, width, r, mPaint);
			}
			canvas.drawLine(width, 0, width, height, mPaint);
			canvas.drawLine(0, height, width, height, mPaint);
		}
		/* Listen touch event */
		public boolean onTouchEvent(MotionEvent me) {
			super.onTouchEvent(me);
			switch (me.getAction()) {
			case MotionEvent.ACTION_UP:
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_DOWN:
				if (first) {
					for (int i = 0;; i++) {
						int k = rnd.nextInt(4);
						switch (k) {
						case 0:up();break;
						case 1:down();break;
						case 2:left();break;
						case 3:right();break;
						}
						invalidate();
						//for (int ii = 0; ii < 10000000; ii++)	;
						if (i > 1000 && row == N - 1 && col == N - 1)
							break;
					}
					first = false;
				}
				int x = (int) me.getX();
				int y = (int) me.getY();
				detect(x, y);
				break;
			}
			invalidate();
			return true;
		}
		private void detect(int x, int y) {
			int w = width / N;
			int h = height / N;
			int c = x / w;
			int r = y / h;
			if (c == col && r == row - 1)
				up();
			else if (c == col && r == row + 1)
				down();
			else if (r == row && c == col - 1)
				left();
			else if (r == row && c == col +  1)
				right();
		}
	}
}
