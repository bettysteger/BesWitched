package mmt08.beswitched;

import android.content.Context;
import android.graphics.*;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.FrameLayout.LayoutParams;
import mmt08.beswitched.R;

public class Block extends ImageView {
	
	public float left;
	public float bottom;
	private float width;
	private int color;
	private int whatImage = R.drawable.white;
//    private Paint paint = new Paint();
	
    public Block(Context context, float left, float bottom, float width, int color) {
        super(context);
//        paint.setStyle(Paint.Style.FILL);
//        paint.setAntiAlias(true);
//        paint.setColor(color);
        this.left = left;
        this.bottom = bottom;
        this.width = width;
        this.color = color;

    	if(color == Color.BLACK) this.setVisibility(INVISIBLE);
    	else if(color == Color.BLUE) whatImage = R.drawable.blue;
        else if(color == Color.GREEN) whatImage = R.drawable.green;
        else if(color == Color.YELLOW) whatImage = R.drawable.yellow;
        else if(color == Color.RED) whatImage = R.drawable.red;
        else if(color == Color.MAGENTA) whatImage = R.drawable.lila;
        else whatImage = R.drawable.orange; 
        
    	this.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
    	
        this.setImageResource(whatImage);
        this.setFocusableInTouchMode(true);
        this.setClickable(true);
//        this.setMeasuredDimension((int)width, (int)width);
//        this.setBackgroundColor(0x333333FF);
        this.invalidate();
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
    	super.onDraw(canvas);
    	layout((int)left, (int)(bottom-width), (int)(left+width), (int)bottom);
//    	setMeasuredDimension((int)width, (int)width);
//        canvas.drawRect(left, bottom-width, left+width, bottom, paint);
    }

	public int getColor() {;
		return this.color;
	}
	public void setColor(int color) {
		this.color = color;
		this.setVisibility(VISIBLE);
    	if(color == Color.BLACK) this.setVisibility(INVISIBLE);
    	else if(color == Color.BLUE) whatImage = R.drawable.blue;
        else if(color == Color.GREEN) whatImage = R.drawable.green;
        else if(color == Color.YELLOW) whatImage = R.drawable.yellow;
        else if(color == Color.RED) whatImage = R.drawable.red;
        else if(color == Color.MAGENTA) whatImage = R.drawable.lila;
        else whatImage = R.drawable.orange; 
    	this.setImageResource(whatImage);
    	
//    	this.setMeasuredDimension((int)width, (int)width);
//		this.invalidate();
//		paint.setColor(color);
	}
	
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	setMeasuredDimension((int)width, (int)width);
    }
    @Override
    protected void onAnimationEnd() {
    	setImageResource(whatImage);
    }

}

