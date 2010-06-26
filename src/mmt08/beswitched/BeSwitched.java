package mmt08.beswitched;

import mmt08.beswitched.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

public class BeSwitched extends Activity implements View.OnTouchListener  {
    /** Called when the activity is first created. */    
    private FrameLayout main;
    private TextView timeView, scoreView;
    private float width = 53;
    private float bottom = 480;
    private int n = 6, m = 6;
    private int pointHeight = 80;
    private Block blocks[][] = new Block[n][m];
    private int colors[] = new int[n];
    private float x_start = 0, y_start = 0;
    private boolean isSwitching = false;
    private float curLeft;
    private float curBottom;
	private int elapsedTime = 0;
	private Handler updatehandler = new Handler();
	private Handler falldownhandler = new Handler();
	private Handler falldownhandler2 = new Handler();
	private Handler allBlocksRiseHandler = new Handler();
	private int score = 0;
	private int gamespeed = 10;
    //private int DisplayHeight = getWindowManager().getDefaultDisplay().getHeight();
    //private int DisplayWidth = getWindowManager().getDefaultDisplay().getWidth();

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        main = (FrameLayout) findViewById(R.id.main_view);  //greift auf die main.xml zu
        timeView = (TextView) findViewById(R.id.point_view);
        scoreView = (TextView) findViewById(R.id.score_view);
        
        colors[0] = Color.BLUE;
        colors[1] = Color.GREEN;
        colors[2] = Color.YELLOW;
        colors[3] = Color.RED;
        colors[4] = Color.CYAN;
        colors[5] = Color.MAGENTA;
        
        for (int i=0; i<n;i++) {
        	for (int j=0; j<m;j++) {
	        	//left bottom width color
	        	blocks[i][j] = new Block(this,width*i,bottom-width*(1+j),width,colors[(int) (Math.random()*n)]);
	        	main.addView(blocks[i][j]);
	        	blocks[i][j].setOnTouchListener(this); 
        	}
        }
        destroyBlocks();
        update();
        allBlocksRise();
        releaseUpperRow();
//        timeView.bringToFront();
    }

	
    public boolean onTouch(View v, MotionEvent event) {
    	
	    Block b = (Block) v;
		float x = event.getX();
		float y = event.getY();
		int action = event.getAction();
		
		Boolean randomTouch = false;
		if (v != b && action == MotionEvent.ACTION_DOWN) {
			randomTouch = true;
			scoreView.setText(""+randomTouch);
			gamespeed=1;
			allBlocksRise();
		} 
			
		// Is the event inside of this block(view)?
//		if (x > b.left && x < (b.left+width) && y > (b.bottom-width) && y < b.bottom) {
		
		b.bringToFront();
		main.bringChildToFront(b);
		
		if (action == MotionEvent.ACTION_DOWN) {
        	x_start = event.getX();
            y_start = event.getY();
//            timeView.setText("");

        	curBottom = b.bottom;
        	curLeft = b.left;
        	isSwitching = false; 	
        	RunAnimations(b,"mousedown");
        }
        
		if(action == MotionEvent.ACTION_MOVE) {
			if ( !isSwitching ) {
				isSwitching = true;
//				b.left = x-width/2;
//				b.bottom = y+width/2;
				boolean bla = false;
	        	if((x_start - x) > 0.5) {
					bla = swapBlocks(b,-1,0);
					RunAnimations(b,"right");
	        	}
	        	else if((x_start - x) < -0.5) {
	        		bla = swapBlocks(b,1,0);
	        		RunAnimations(b,"left");
	        	}
//	        	else if((y_start - y) > 0.5) {
//	        		bla = swapBlocks(b,0,1);
//	        	}
//	        	else if((y_start - y) < -0.5) {
//	        		bla = swapBlocks(b,0,-1);	
//	        	}
	        	if (!bla) {
	        		b.bottom = curBottom;
	            	b.left = curLeft;
	        	}
			}				
		}
		
        if(action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
        	isSwitching = false;  
        }
        
//        b.invalidate();
		return true;
    } 

 

	//positionen der 2 bloecke werden vertauscht
	private boolean swapBlocks(Block b,int leftorright,int upordown) {
			
    	//swap blocks in touch direction
        for (int i=0; i<n;i++) {
        	for (int j=0; j<m;j++) {
        		if (blocks[i][j]==b && i+leftorright < n && j+upordown < m && i+leftorright >= 0 && j+upordown >= 0) {  
        			
        			//position und arraypos austauschen
        			float besLeft = blocks[i+leftorright][j+upordown].left;
        			float besBottom = blocks[i+leftorright][j+upordown].bottom;
        			
        			blocks[i][j].left = besLeft;
        			blocks[i][j].bottom = besBottom;
        			
        			blocks[i+leftorright][j+upordown].bottom = curBottom;
        			blocks[i+leftorright][j+upordown].left = curLeft;
        			
        			blocks[i][j] = blocks[i+leftorright][j+upordown];
        			blocks[i+leftorright][j+upordown] = b;

        			destroyBlocks();
        			//timeView.setText("SWAP b:"+(i)+(j) + " bSwi: " + (i+leftorright)+(j+upordown));
        			return true;
        		}
        	}
		}
        return false;
	}
	
	//2 nebeneinander gleichfarbige werden schwarz (nur horizontal)
	private void destroyBlocks() {
		
		int flag1Col = 0;
		int flag2Col = 0;
		int flag3Col = 0;
		int flag4Col = 0;
		
		int flag1Row = 0;
		int flag2Row = 0;
		int flag3Row = 0;
		
		int totalCombo = 0;
		
		// Destroy Columnwise
		for ( int row = 0; row < n; row++ ) {
			for ( int column = 0; column < m - 1; column++ ) {
				if ( blocks[row][column].getColor() == blocks[row][column+1].getColor() ) {
					if ( blocks[row][column].getColor() != Color.BLACK && blocks[row][column+1].getColor() != Color.BLACK ) {
						
						// 5 Combo
						if ( flag3Col == 1 ) {
							flag4Col = 1;
							
							RunAnimations(blocks[row][column+1],"destroy");
							RunAnimations(blocks[row][column],"destroy");
							RunAnimations(blocks[row][column-1],"destroy");
							RunAnimations(blocks[row][column-2],"destroy");
							RunAnimations(blocks[row][column-3],"destroy");
							score += 300;
							
							//before without animation:
//							blocks[row][column+1].setColor(Color.BLACK);
//							blocks[row][column].setColor(Color.BLACK);
//							blocks[row][column-1].setColor(Color.BLACK);
//							blocks[row][column-2].setColor(Color.BLACK);
//							blocks[row][column-3].setColor(Color.BLACK);
							
							totalCombo += 5;
							
							flag1Col = flag2Col = flag3Col = flag4Col = 0;
						}
						
						// 4 Combo
						if ( flag2Col == 1 ) {
							
							if ( column + 2 < m ) {
								if ( blocks[row][column+1].getColor() == blocks[row][column+2].getColor() ) {
									flag3Col = 1;
									continue;
								}
							}
							
							RunAnimations(blocks[row][column+1],"destroy");
							RunAnimations(blocks[row][column],"destroy");
							RunAnimations(blocks[row][column-1],"destroy");
							RunAnimations(blocks[row][column-2],"destroy");
							score += 200;
							
							totalCombo += 4;
							
							flag1Col = flag2Col = flag3Col = flag4Col = 0;
						}
						
						// 3 Combo
						if ( flag1Col == 1 ) {
							
							if ( column + 2 < m ) {
								if ( blocks[row][column+1].getColor() == blocks[row][column+2].getColor() ) {
									flag2Col = 1;
									continue;
								}
							}
									
							RunAnimations(blocks[row][column+1],"destroy");
							RunAnimations(blocks[row][column],"destroy");
							RunAnimations(blocks[row][column-1],"destroy");
							score += 100;
							
							totalCombo += 3;
							
							flag1Col = flag2Col = flag3Col = flag4Col = 0;	
						}
						
						else
							flag1Col = 1;
					}
				}
				else
					flag1Col = flag2Col = flag3Col = 0;
			}
		}
		
		// Destroy Rowwise
		for ( int column = 0; column < m; column++ ) {
			for ( int row = 0; row < n - 1; row++ ) {
				if ( blocks[row][column].getColor() == blocks[row+1][column].getColor() ) {
					if ( blocks[row][column].getColor() != Color.BLACK && blocks[row+1][column].getColor() != Color.BLACK ) {
						if ( flag2Row == 1 ) {
							flag3Row = 1;
							
							
							flag1Row = flag2Row = flag3Row = 0;
						}
						
						else if ( flag1Row == 1 ){
							flag2Row = 1;
							RunAnimations(blocks[row+1][column],"destroy");
							RunAnimations(blocks[row][column],"destroy");
							RunAnimations(blocks[row-1][column],"destroy");
							
							score += 100;
							totalCombo += 3;
							
							flag1Row = flag2Row = flag3Row = 0;
						}
						
						else
							flag1Row = 1;
					}
				}
				else
					flag1Row = flag2Row = flag3Row = 0;
			}
		}
		
		if ( totalCombo != 0 ) {
//			timeView.setTextSize(20+totalCombo*2);
//			timeView.setText( (totalCombo) + " Combo");	
		}		
		fallDown();
	}

	private void fallDown() {
	
		for ( int column = 0; column < n; column++ ) {
			for ( int row = 0; row < m-1; row++ ) {
				if(blocks[column][row].getColor() == Color.BLACK && blocks[column][row+1].getColor() != Color.BLACK) {
					RunAnimations(blocks[column][row],"falldown");
					
					int color = blocks[column][row+1].getColor();
        			blocks[column][row+1].setColor(Color.BLACK);
					blocks[column][row].setColor(color);
					
			        falldownhandler.postDelayed(new Runnable() {
						
						public void run() {
							fallDown();
						}
					}, 1500); //after 1,5 seconds fallDown() executes
			        
			        falldownhandler2.postDelayed(new Runnable() {
						
						public void run() {
							destroyBlocks();
						}
					}, 2500);
				}
			}
		}
	}
	
	private void RunAnimations(Block b,String anim) {
		Animation a = AnimationUtils.loadAnimation(this, R.anim.right);
		
		if (anim == "left") a = AnimationUtils.loadAnimation(this, R.anim.left);
		else if(anim == "mousedown") a = AnimationUtils.loadAnimation(this, R.anim.mousedown);
		else if(anim == "falldown") a = AnimationUtils.loadAnimation(this, R.anim.falldown);
		else if(anim == "destroy") {
			a = AnimationUtils.loadAnimation(this, R.anim.destroy);
			b.setColor(Color.BLACK);
		}
		
	    a.reset();
	    b.clearAnimation();
	    b.startAnimation(a);
	    
	}
	
	public void update() {
		elapsedTime++;
		score++;
		timeView.setText(DateUtils.formatElapsedTime(elapsedTime));
		scoreView.setText("Score: "+score);
		
		if(score%500 == 0 && gamespeed>0) gamespeed-=2;
		
		if ( blocks[0][0].bottom < bottom )
        	growBlocks();
		
        updatehandler.postDelayed(new Runnable() {			
			public void run() {
				update();
			}
		}, 1000); //after 1 seconds update() executes
        
	}
	
	public void allBlocksRise() {
		
		for ( int i = 0; i < n; i++ ) {
			for ( int j = 0; j < m; j++ ) {
				blocks[i][j].bottom -= 1.0;
				blocks[i][j].invalidate();
			}
		}
		
		if(gameOver()) {
			timeView.setText("GAME OVER");
            Intent i = new Intent();
            i.setClassName("mmt08.beswitched", "mmt08.beswitched.GameOver");
            startActivity(i);
            return;
		}
		
		allBlocksRiseHandler.postDelayed(new Runnable() {
			public void run() {
				allBlocksRise();
			}
		}, gamespeed*100);
	}
	
	private void growBlocks() {
		int tempArraySize = m;
		m += 1;
		// Copy current array to a temporary array
    	Block tempBlocks[][] = blocks;
    	// initiate current array with new size
    	blocks = new Block[n][m];
    	
    	
    	// copy all values from temporary array except the lowest row
    	for ( int i = 0; i < n; i++ ) {
    		for ( int j = 0; j < tempArraySize; j++ ) {
    			blocks[i][j+1] = tempBlocks[i][j];
    		}
    	}
    	
    	
    	// generate new blocks for lowest row
        for (int i=0; i<n;i++) {
        	//left bottom width color
        	//blocks[i][0] = new Block(this,width*i,bottom-width,width,colors[(int) (Math.random()*n)]);

        	blocks[i][0] = new Block(this,width*i,blocks[i][1].bottom+width+1,width,colors[(int) (Math.random()*n)]);
        	main.addView(blocks[i][0]);
        	blocks[i][0].setOnTouchListener(this);
        	
        }
	}
	
	private void releaseUpperRow() {
		boolean release = true;
		for ( int i = 0; i < n; i++ ) {
			if ( blocks[i][m-1].getColor() != Color.BLACK )
				release = false;
		}
		
		if ( release ) {
			/*
			for ( int i = 0; i < n; i++ ) {
				blocks[n][m-1] = null;
			}
			*/
			m -= 1;
		}
		
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				releaseUpperRow();
			}
		}, 1000);
	}
    
	private boolean gameOver() {
		for ( int i = 0; i < n; i++ ) {
			if((blocks[i][m-1].bottom-width) < pointHeight+10) {
				timeView.setText("Hurry up!");
			}
			if((blocks[i][m-1].bottom-width) < pointHeight) return true;
		}
		return false;
	}
}