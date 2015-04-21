package com.daemon.xqyz.view;

import com.daemon.xqyz.R;
import com.daemon.xqyz.utils.DensityUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.BitmapFactory.Options;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;


/**
 * 只需要 确定width 和 height 作为圆心 其余不用管
 * @author daemon
 *2015.01.05
 */
public class CustView extends View{

	public   int R1 ;   //一个半径
	public   int R2 ;
	
	public static CustView mInstance;
	
	//每一格 cos y的差距
	
	public static float textNewAngle_out;  //对外提供的  
	
	private  float width;
	private  float height;
  
	
	public static   int newAngle=0;  //新的角度 开始时30
	public static   int textNewAngle=30; 
	public static   int count=0;  //圈数    3.5圈 不能转动 就是  textNewAngle+count*60==240 只能回退
	
	private  int newAngle1;  //新的角度 开始时30
	
	public  double newX;
	public   double newY;   //给Main提供位置 空间移动到这个位置上面去
	
	private Paint mPaint2;
	private boolean flag=false;
	private Paint mPaint3;
	private Paint mPaint;
	private Context mContext;
	private Bitmap bitmap;
	private Bitmap bitmap1;
	private static int BJ_SIZE;
	private static int XQ_SIZE;
    
	private int YH=85;
	
	// tText.setTextColor(android.graphics.Color.parseColor("#87CEFA")) ; //还是利用Color类；
	
	private String Color_LAN="#15a4f9";
	private String Color_LV="#00d397";
	private String Color_Huang="#dfcb2f";
	private String Color_Hong="#e75171";
	private RectF oval;
	private Paint mPaint1;
	private Paint mPaint4;
	public static boolean isDraw; // 默认是false
	
	public CustView(Context context) {  
        this(context, null);  
    }  
	
	
	public CustView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, 0);
		
		this.mContext=context;
		mInstance=this;
		
		bitmap=ZoomImg1(R.drawable.ic_da);
		
		bitmap=zoomImg(bitmap, DensityUtil.dip2px(mContext, 250), DensityUtil.dip2px(mContext, 250));
		
		//bitmap=createScaledBitmap(bitmap,DensityUtil.dip2px(mContext, 300),DensityUtil.dip2px(mContext, 200),ScalingLogic.FIT);
		
		System.out.println(bitmap+"--------");
		
		/*Options options2 = new Options();
		options2.inJustDecodeBounds = true;  
		BitmapFactory.decodeResource(getResources(), R.drawable.jp_xq, options2);
		options2.inSampleSize = calculateInSampleSize(options2, 2,  2);   //1/2
		options2.inJustDecodeBounds = false;  */
		
		bitmap1=BitmapFactory.decodeResource(getResources(), R.drawable.indicator );
		bitmap1=zoomImg(bitmap1, DensityUtil.dip2px(mContext, 35), DensityUtil.dip2px(mContext, 35));
		
		
		//小球里圆心的位置
		R1=bitmap.getWidth()/2-bitmap1.getWidth()-DensityUtil.dip2px(mContext, 10);
		
		//小球半径
		R2=bitmap1.getWidth()/2;
		
		//背景大图的一半宽  图片画的位置要减去这个  因为左上角
		BJ_SIZE=bitmap.getWidth()/2;
		//同上
		XQ_SIZE=bitmap1.getWidth()/2;
		
		YH=R1-R2-DensityUtil.dip2px(mContext, 8);
		
		System.out.println(YH+"YH");
		System.out.println("DensityUtil.dip2px(mContext, 10)   "+DensityUtil.dip2px(mContext, 9));
		initPaint();
	}


	@SuppressLint("NewApi")
	public CustView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}


	public Bitmap ZoomImg1(int r){
		BitmapFactory.Options opts = new Options();
		opts.inJustDecodeBounds = true;
		    BitmapFactory.decodeResource(getResources(), r,  opts);
		    // 从Options中获取图片的分辨率
		    int imageHeight = opts.outHeight;
		    int imageWidth = opts.outWidth;

		    // 获取Android屏幕的服务
		    WindowManager wm = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
		    // 获取屏幕的分辨率，getHeight()、getWidth已经被废弃掉了
		    // 应该使用getSize()，但是这里为了向下兼容所以依然使用它们
		    int windowHeight = wm.getDefaultDisplay().getHeight();
		    int windowWidth = wm.getDefaultDisplay().getWidth();

		    // 计算采样率
		    int scaleX = imageWidth / windowWidth;
		    int scaleY = imageHeight / windowHeight;
		    int scale = 1;
		    // 采样率依照最大的方向为准
		    if (scaleX > scaleY && scaleY >= 1) {
		        scale = scaleX;
		    }
		    if (scaleX < scaleY && scaleX >= 1) {
		        scale = scaleY;
		    }

		    // false表示读取图片像素数组到内存中，依照设定的采样率
		    opts.inJustDecodeBounds = false;
		    // 采样率
		    opts.inSampleSize = 1;
		    
		    return     BitmapFactory.decodeResource(getResources(), r,  opts);

	}

	public static Bitmap zoomImg(Bitmap bm, int newWidth ,int newHeight){   
	    // 获得图片的宽高   
	    int width = bm.getWidth();   
	    int height = bm.getHeight();   
	    // 计算缩放比例   
	    float scaleWidth = ((float) newWidth) / width;   
	    float scaleHeight = ((float) newHeight) / height;   
	    // 取得想要缩放的matrix参数   
	    Matrix matrix = new Matrix();   
	    matrix.postScale(scaleWidth, scaleHeight);   
	    // 得到新的图片  
	    Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);   
	    bm.recycle();
	    
	    return newbm;   
	}  
	   
	public static void refresh(){
		mInstance.invalidate();
	}
	
	/**
	 * 初始化画笔
	 */
	private void initPaint() {
		
		 // 实例化画笔并打开抗锯齿  
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);  
        mPaint.setStyle(Paint.Style.STROKE);  
        mPaint.setStrokeWidth(7);
        mPaint.setColor(android.graphics.Color.parseColor(Color_LAN));   //这个的颜色  蓝(#15a4f9)---绿(#00d397)---黄(#dfcb2f)---红(#e75171)--
        
        mPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);  
        mPaint1.setStyle(Paint.Style.STROKE);  
        mPaint1.setStrokeWidth(7);
        mPaint1.setColor(android.graphics.Color.parseColor(Color_LV));   //这个的颜色  蓝(#15a4f9)---绿(#00d397)---黄(#dfcb2f)---红(#e75171)--
        
        mPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);  
        mPaint2.setStyle(Paint.Style.STROKE);    
        mPaint2.setStrokeWidth(7);
        mPaint2.setColor(android.graphics.Color.parseColor(Color_Huang));
        
       
        mPaint3 = new Paint(Paint.ANTI_ALIAS_FLAG);  
        mPaint3.setStyle(Paint.Style.STROKE);  
        mPaint3.setStrokeWidth(7);
        mPaint3.setColor(android.graphics.Color.parseColor(Color_Hong));  
        
        // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()  
        
        mPaint4 = new Paint(Paint.ANTI_ALIAS_FLAG);  
        
        mPaint4.setStrokeWidth(4);  
        mPaint4.setTextSize(30);  
        mPaint4.setColor(android.graphics.Color.parseColor(Color_LAN));  
        
        // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()  
        mPaint4.setTextAlign(Paint.Align.CENTER);  
        
      /*  DisplayMetrics metric = new DisplayMetrics();
        
        if(FTApplication.isDialog){
            ((PlayRecordActivity)(mContext)).getWindowManager().getDefaultDisplay().getMetrics(metric);
        }else{
        ((MainActivity)(mContext)).getWindowManager().getDefaultDisplay().getMetrics(metric);
        }*/
        
        DisplayMetrics dm = new DisplayMetrics();
		dm = getResources().getDisplayMetrics();
		
		
       //圆心的点   这个不要变化  影响整个布局 
        width = dm.widthPixels/2;
        height =dm.heightPixels/3;
        
        System.out.println(width+"---++-"+height);
		 oval = new RectF(width - YH, height - YH, width+YH  
	                , height + YH);
		 
		 
        System.out.println(width+"---"+height);
        
	}

	@Override
	protected void onDraw(Canvas canvas) {
		
		// 绘制圆环  
	   // canvas.drawCircle(width, height, R1, mPaint); 
		
		canvas.drawBitmap(bitmap, width-BJ_SIZE, height-BJ_SIZE,null);
		
		//canvas.drawCircle(width, height, 144, mPaint); 
		
		  if(count==0){
			  canvas.drawArc(oval, -90, newAngle, false, mPaint);  //根据进度画圆弧  
		    }
		  else  if(count==1){
		    	//先画一个蓝色的  后面的在改变 整个颜色
		    	canvas.drawCircle(width, height, YH, mPaint);
		    	
		    	//在画一个
		    	canvas.drawArc(oval, -90, newAngle, false, mPaint1);  //根据进度画圆弧  
		    }
		  else if(count==2){
			  System.out.println("count  " +count);
			  	//先画一个蓝色的  后面的在改变 整个颜色
		    	canvas.drawCircle(width, height, YH, mPaint);
		    	canvas.drawCircle(width, height, YH, mPaint1);
		    	
		    	//在画一个
		    	canvas.drawArc(oval, -90, newAngle, false, mPaint2);  //根据进度画圆弧  
		  }
		  else if(count==3){
			//先画一个蓝色的  后面的在改变 整个颜色
		    	canvas.drawCircle(width, height, YH, mPaint);
		    	canvas.drawCircle(width, height, YH, mPaint1);
		    	canvas.drawCircle(width, height, YH, mPaint2);
		    	
		    	//在画一个3
		    	canvas.drawArc(oval, -90, newAngle, false, mPaint3);  //根据进度画圆弧  
		  }
		    
	    System.out.println("现在角度 "+newAngle);
	    
	    
	    float testX=width;
	    float testY=height;
	    
//	    if(FTApplication.isDialog){
//	    	testY+=60;
//	    }
	    
	    
	    textNewAngle_out=textNewAngle+newAngle/6+count*60;
	    
	    System.out.println("out:  "+textNewAngle_out);
	    
	    canvas.drawText(textNewAngle_out+"",testX, testY,mPaint4);  
	    
	    	  if (textNewAngle_out == 40.0) {
	 			 canvas.drawText("Grave",testX, testY+mPaint4.getTextSize(),mPaint4);  
	 		}
	 	    else if (textNewAngle_out == 44.0) {
	 	    	 canvas.drawText("Largo",testX, testY+mPaint4.getTextSize(),mPaint4);  
	 		}
	 	    else if (textNewAngle_out == 52.0) {
	 	    	 canvas.drawText("Lento",testX, testY+mPaint4.getTextSize(),mPaint4);    
	 		}
	 	    else if (textNewAngle_out == 56.0) {
	 	    	 canvas.drawText("Adagio",testX, testY+mPaint4.getTextSize(),mPaint4);  
	 		}
	 	    else if (textNewAngle_out == 60.0) {
	 	    	 canvas.drawText("Lento",testX, testY+mPaint4.getTextSize(),mPaint4);  
	 		}
	 	    else if (textNewAngle_out == 66.0) {
	 	    	 canvas.drawText("Andante",testX, testY+mPaint4.getTextSize(),mPaint4);  
	 		}
	 	    else if (textNewAngle_out == 69.0) {
	 	    	 canvas.drawText("Andantino",testX, testY+mPaint4.getTextSize(),mPaint4);  
	 		}
	 	    else if (textNewAngle_out == 88.0) {
	 	    	 canvas.drawText("Moderato",testX, testY+mPaint4.getTextSize(),mPaint4);  
	 		}
	 		else if (textNewAngle_out == 108.0) {
	 			 canvas.drawText("Allegretto",testX, testY+mPaint4.getTextSize(),mPaint4);  
	 		}
	 		else if (textNewAngle_out == 132.0) {
	 			 canvas.drawText("Allegro",testX, testY+mPaint4.getTextSize(),mPaint4);  
	 		}
	 		else if (textNewAngle_out == 152.0) {
	 			 canvas.drawText("Vivo",testX, testY+mPaint4.getTextSize(),mPaint4);  
	 		}
	 		if (textNewAngle_out == 160.0) {
	 			 canvas.drawText("Vivace",testX, testY+mPaint4.getTextSize(),mPaint4);  
	 		}
	 		else if (textNewAngle_out == 189.0) {
	 			 canvas.drawText("Presto",testX, testY+mPaint4.getTextSize(),mPaint4);   
	 		}
	 		else if (textNewAngle_out == 210.0) {
	 			 canvas.drawText("Prestissimo",testX, testY+mPaint4.getTextSize(),mPaint4);  
	 		}
	  
	    
	    
       // canvas.drawText(newAngle+"", targetRect.centerX(), baseline, mPaint3);  
	    
	    
	    getNewLocation(); //根据判断 来移动小球 获得新的位置
	    
	    //确定Main中控件的位置
	   
	    System.out.println("newX " +newX +"---------- newY "+newY);
	    
	   /* if(newAngle==90){
	    	mPaint2.setColor(Color.BLACK);
	    }else if(newAngle==180){
	    	mPaint2.setColor(Color.RED);
	    }else if(newAngle==270){
	    	mPaint2.setColor(Color.DKGRAY);
	    }else if(newAngle==360){
	    	mPaint2.setColor(Color.MAGENTA);
	    }*/
	   //canvas.drawCircle((float)newX, (float)newY, R2, mPaint2); 
	    canvas.drawBitmap(bitmap1, (float)newX-XQ_SIZE, (float)newY-XQ_SIZE, null);
	    
	}

	
	
	public  void getNewLocation() {
		/**
	     * 0-90的变化规律
	     */
		if(newAngle==0){
			newX=width;
			newY=height-R1;
		}
		else if(newAngle==90){
			newX=width+R1;
			newY=height;
		}
		else if(newAngle==180){
			newX=width;
			newY=height+R1;
		}
		else if(newAngle==270){
			newX=width-R1;
			newY=height;
		}
		else if(newAngle==360){
			newX=width;
			newY=height-R1;
		}
		else if(newAngle>360){
			newAngle=360;
			newX=width;
			newY=height-R1;
		}
		else if(newAngle>0&&newAngle<90){
	    newX = width+ (R1*Math.sin(newAngle*Math.PI/180));
	    newY = height-(R1*Math.cos(newAngle*Math.PI/180));
	    }
	    
	    /**
	     * 90-180的变化规律
	     */
	    else if(newAngle>90&&newAngle<180){
	    	newAngle1=180-newAngle;
	    	 newX=width+ (R1*Math.sin(newAngle1*Math.PI/180));
	    	 newY=height+(R1*Math.cos(newAngle1*Math.PI/180));
	    }
	    
	    /**
	     * 180-270的变化规律
	     */
	    else if(newAngle>180&&newAngle<270){
	    	newAngle1=270-newAngle;
	    	 newX=width- (R1*Math.cos(newAngle1*Math.PI/180));
	    	 newY=height+(R1*Math.sin(newAngle1*Math.PI/180));
	    }
	    
	    /**
	     * 270-360的变化规律
	     */
	    else if(newAngle>270&&newAngle<360){
	    	newAngle1=360-newAngle;
	    	 newX=width- (R1*Math.sin(newAngle1*Math.PI/180));
	    	 newY=height-(R1*Math.cos(newAngle1*Math.PI/180));
	    }
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			flag=false;  //每次按下都是重新开始判断
			isDraw=false;
			float action_x=event.getX();
			float action_y=event.getY();
			
			System.out.println("点击位置 ： x "+action_x+"   y "+action_y);
			
			//只有当点击的在那个小圆上面才可以被进入移动的条件
			// x>=newX-R2 && x<=newX+R2 y 也一样
			System.out.println("x 范围: "+(newX-R2)+"-----"+(newX+R2));
			System.out.println("y 范围: "+(newY-R2)+"-----"+(newY+R2));
			
			if((action_x>=newX-R2 && action_x<=newX+R2)&&(action_y>=newY-R2&&action_y<=newY+R2)){
			flag=true;  //触碰到小球的情况
			}
			
			break;
		case MotionEvent.ACTION_MOVE:
		
			
			 if(flag){
				//根据判断 让小球一格一的移动
				//分为两种情况 左边和右边   右边就是   就newAngle++
				float action_newY=event.getY();
				float action_newX=event.getX();
				
				if(newAngle==0){
					if(count==0){
						if(action_newX<newX){
							
						}else{
							newAngle++;
						}
					}else{
						if(action_newX<newX){
							newAngle=360;
							count--;
							newAngle--;
						}else{
							newAngle++;
						}
					}
				}
				else if(newAngle==90){
					if(action_newY>newY){
						newAngle++;
					}else{
						newAngle--;
					}
				}
				else if(newAngle==180){
					if(action_newX>newX){   //180 右边
						newAngle--;
					}else{
						newAngle++;
					}
				}
				else if(newAngle==270){
					if(action_newY<newY){
						newAngle++;
					}
					else{
						newAngle--;
					}
				}
				else if(newAngle==360){
					if(action_newX>newX){  //先不让他超过360
						newAngle=0;
						count++;
						
						//给那个位置画一个圆 半径是  width+144 颜色是当前mPaint的颜色
						
						newAngle++;
					}else{
						newAngle--;
					}
				}
				else if(textNewAngle+newAngle/6+count*60>=240){
					//newAngle=180;
					if(action_newX<newX){  //先不让他超过360
					}else{
						newAngle--;
					}
				}
				
				else if(newAngle>0&&newAngle<90){
					double x=action_newX-width;
					double y=height-action_newY;
					System.out.println("x: "+x +"  y : "+y+"------------            "+(Math.atan(x/y)*180/Math.PI));
					if(y<0){
						newAngle=90;
					}
					else if(x<0){
						newAngle=0;
					}
					else{
					newAngle=(int) (Math.atan(x/y)*180/Math.PI);
					}
				}
				else if(newAngle>90&&newAngle<180){
					double x=action_newX-width;
					double y=action_newY-height;
					
					System.out.println("x: "+x +"  y : "+y+"------------"+(180-Math.atan(x/y)*180/Math.PI));
					
					if(y<0){
						newAngle=90;
					}
					else if(x<0){
						newAngle=180;
					}
					else
					newAngle=(int) (180-Math.atan(x/y)*180/Math.PI);
					
				}
				else if(newAngle>180&&newAngle<270){
					double x=width-action_newX;
					double y=action_newY-height;
					
					System.out.println("x: "+x +"  y : "+y+"------------"+(270-Math.atan(x/y)*180/Math.PI));
					
					if(x<0){
						newAngle=180;
					}
					else if(y<0){
						newAngle=270;
					}
					else
					newAngle=(int) (270- Math.atan(y/x)*180/Math.PI);
				}
				else if(newAngle>270&&newAngle<360){
					double x=width-action_newX;
					double y=height-action_newY;
					
					System.out.println("x: "+x +"  y : "+y+"------------"+(360-Math.atan(x/y)*180/Math.PI));
					
					if(y<0){
						newAngle=270;
					}
					else if(x<0){
						newAngle=360;
					}
					else
					newAngle=(int)(360- Math.atan(x/y)*180/Math.PI);
				}
				
				System.out.println(newAngle+"---角度");
				
			
				invalidate();
			}
			break;
			
		case MotionEvent.ACTION_UP:
			isDraw=true;
			break;

		default:
			break;
		}
		
		return true;
	}

}
