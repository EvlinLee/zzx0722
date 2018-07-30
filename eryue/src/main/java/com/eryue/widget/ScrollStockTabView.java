package com.eryue.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.eryue.R;
import com.eryue.listener.DisposeListener;
import com.eryue.listener.TouchEventListener;
import com.library.util.StringUtils;
import com.library.util.UIScreen;

import java.util.List;


public class ScrollStockTabView extends LinearLayout implements View.OnTouchListener, DisposeListener {
    // 是否显示箭头标示，默认显示
    private boolean needArrow = true;
    // 是否显示间隔，默认显示
    private boolean needMargin = true;
    private static final float TXT_SIZE = 14;
    private int MAX_SIZE = 5;
    private List<String> optionList;
    private TouchEventListener touchEventListener;
    private TabBarItemView preView;
    private int buttonWidth;
    private Point downPoint = new Point();
    private boolean isMoved;
    private int selectedIndex = 0;
    private HorizontalScrollView horizontalScrollView;
    private LinearLayout mainView;

    private static Bitmap rightArrow;
    private static Bitmap leftArrow;

    private static final int CUSTOM_HEIGTH = StringUtils.dipToPx(27); // 机构版 彩虹头高度
    private int arrowMargin;
    private Drawable redRect;
    private Path path = new Path();
    private Scroller scroller;
    private int type = 1;
    public final static int STOCK_TYPE = 1;
    public final static int FUND_TYPE = 2;
    //一个文字的宽度
    private float sigleTextWidth;
    //是否彩虹头的宽度总和超过了屏幕宽度
    private boolean isWidthGT = false;

    //private Bitmap fundSep = null;

    // 用户自己存储的id
    private int recordID;

    public ScrollStockTabView(Context context) {
        super(context);
    }

    public ScrollStockTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(0xFF1d1e23);
    }

    public void dispose() {
        if (mainView != null) {
            mainView.removeAllViews();
        }
        if (horizontalScrollView != null) {
            horizontalScrollView.removeAllViews();
        }
        DisposeHelper.dispose(preView);
        preView = null;
        mainView = null;
        horizontalScrollView = null;
    }

    public int getScrollWidth() {
        if (horizontalScrollView != null) {
            return horizontalScrollView.getScrollX();
        }
        return 0;
    }

    public void setScrollWidth(int w) {
        if (horizontalScrollView != null) {
            //horizontalScrollView.scrollTo(-w, 0);
            Message message = handler.obtainMessage();
            message.what = w;
            handler.sendMessage(message);
        }
    }

    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        //If need to draw right arrow

        if (horizontalScrollView != null && leftArrow != null && rightArrow != null) {
            final int maxScrollX = horizontalScrollView.getChildAt(0).getRight() - horizontalScrollView.getWidth();
            //final int iconHeight = (getHeight() / 2)==0?70:getHeight() / 2;
            //final int iconTop = (getHeight() - iconHeight)/ 2;
            final int iconWidth = getHeight() * 25 / 70;
            if (needArrow) {
                if (horizontalScrollView.getScrollX() < maxScrollX) {
                    canvas.drawBitmap(rightArrow, new Rect(0, 0, rightArrow.getWidth(), rightArrow.getHeight()),
                            new Rect(getWidth() - arrowMargin - iconWidth, 0, getWidth() - arrowMargin, getHeight()), null);
                }
                if (horizontalScrollView.getScrollX() > 0) {
                    canvas.drawBitmap(leftArrow, new Rect(0, 0, leftArrow.getWidth(), leftArrow.getHeight()),
                            new Rect(arrowMargin, 0, arrowMargin + iconWidth, getHeight()), null);
                }
            }
        }
    }

    private int addButtonWidth = -1;

    public void addItems(List<String> optionList, int maxCols, int addButtonWidth) {
        MAX_SIZE = maxCols;
        this.addButtonWidth = addButtonWidth;
        addItem(optionList);
    }

    private boolean isNeedAdd = false;
    private static Bitmap tabAddButton;
    public final static int ADD_TAG = -100;

    public final static int MORE_TAG = -200;
    private static final String MORE_TAB = "更多";
    public static final int TYPE_NONE = 0;//0 - 显示+号
    public static final int TYPE_MORE = 1;//1 - "更多"
    private int tabType = TYPE_NONE;// 0 - none  1 - more

    public void addItems(List<String> optionList, int type) {
        this.tabType = type;
        this.addButtonWidth = -1;
        //this.addButtonWidth = addButtonWidth;
        optionList.add(MORE_TAB);
        addItem(optionList);
    }

    public void addItem(List<String> optionList) {
        //fundSep = ((BitmapDrawable)getResources().getDrawable(R.drawable.fund_tab_spe)).getBitmap();
        if (mainView != null) {
            mainView.removeAllViews();
            mainView = null;
        }
        if (horizontalScrollView != null) {
            horizontalScrollView.removeAllViews();
            horizontalScrollView = null;
        }
        removeAllViews();
        this.optionList = optionList;
        int size = optionList.size();
        if (isNeedAdd) {
            size++;
        }
        redRect = ((Drawable) getResources().getDrawable(R.drawable.slidebar));
//		if(TOP_ARROW == null){
//			TOP_ARROW = ((BitmapDrawable)getResources().getDrawable(R.drawable.tab_arrow_top)).getBitmap();
//		}

        if (rightArrow == null) {
            rightArrow = ((BitmapDrawable) getResources().getDrawable(R.drawable.icon_more_right)).getBitmap();
        }
        if (leftArrow == null) {
            leftArrow = ((BitmapDrawable) getResources().getDrawable(R.drawable.icon_more_left)).getBitmap();
        }

        Paint paint = new Paint();
        paint.setTextSize(StringUtils.dipToPx(TXT_SIZE));


        //计算彩虹头的总宽度
        float totleWidth = 0;
        float textWidth = 0;

        float extendWidth = paint.measureText("正正");
        for (int i = 0; i < size; i++) {
            textWidth = paint.measureText(optionList.get(i));

            totleWidth += (textWidth + extendWidth);
        }

        if ((totleWidth + StringUtils.dipToPx(6)) <= UIScreen.screenWidth) {
//            isWrapContent = false;
        } else {
            MAX_SIZE = Math.round(5 * UIScreen.screenWidth / totleWidth + StringUtils.dipToPx(10));
        }


        if (isWrapContent) {

            //不固定宽度的彩虹头，计算彩虹头的总宽度
            isWidthGT = true;

            FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams((int) totleWidth, FrameLayout.LayoutParams.WRAP_CONTENT);
            horizontalScrollView = new HorizontalScrollView(getContext());
            horizontalScrollView.setHorizontalFadingEdgeEnabled(false);
            horizontalScrollView.setHorizontalScrollBarEnabled(false);
            mainView = new LinearLayout(getContext());
            horizontalScrollView.addView(mainView, flp);
            addView(horizontalScrollView);
            buttonWidth = (int) totleWidth / size;
        } else {
            if (size > MAX_SIZE) {
                horizontalScrollView = new HorizontalScrollView(getContext());
                horizontalScrollView.setHorizontalFadingEdgeEnabled(false);
                horizontalScrollView.setHorizontalScrollBarEnabled(false);
                mainView = new LinearLayout(getContext());
                horizontalScrollView.addView(mainView);
                addView(horizontalScrollView);
                buttonWidth = UIScreen.screenWidth / MAX_SIZE;

                if (rightArrow == null) {
                    rightArrow = ((BitmapDrawable) getResources().getDrawable(R.drawable.icon_more_right)).getBitmap();
                }
                if (leftArrow == null) {
                    leftArrow = ((BitmapDrawable) getResources().getDrawable(R.drawable.icon_more_left)).getBitmap();
                }
            } else {
                buttonWidth = UIScreen.screenWidth / size;
            }
        }


        arrowMargin = StringUtils.dipToPx(2);

        int index = 0;
        for (int i = 0; i < size; i++) {
            index = i;
            TabBarItemView optionItem = new TabBarItemView(getContext());
            optionItem.setOnTouchListener(this);
            if (isNeedAdd && i == size - 1) {
                optionItem.setText(i, "");
                optionItem.setTag(ADD_TAG);
            } else if (tabType == TYPE_MORE && i == size - 1) {
                optionItem.setText(i, MORE_TAB);
                optionItem.setTag(MORE_TAG);
            } else {
                optionItem.setText(i, optionList.get(i));
                optionItem.setTag(i);
            }

            if (isWrapContent) {
                if (isWidthGT) {
                    mainView.addView(optionItem);
                } else {
                    addView(optionItem);
                }
            } else {
                if (size > MAX_SIZE) {
                    mainView.addView(optionItem);
                } else {
                    addView(optionItem);
                }
            }

            if (i == selectedIndex) {
                optionItem.setSelected(true);
                preView = optionItem;
                preView.optionItem.setTextSize(TXT_SIZE);
            }
        }
        //如果是可以添加tab模型
        if (addButtonWidth > 0) {
            TabBarItemView optionItem = new TabBarItemView(getContext());
            optionItem.viewWidth = addButtonWidth;
            optionItem.setText(index, "");
            optionItem.setTag(index);
            mainView.addView(optionItem);
            //buttonWidth = (UIScreen.screenWidth - addButtonWidth) / MAX_SIZE;
        }
//		if(isNeedAdd){
//			TabBarItemView optionItem = new TabBarItemView(getContext());
//			optionItem.viewWidth = buttonWidth;
//			//optionItem.setText(index,"");
//			optionItem.setTag(ADD_TAG);
//			if(size > MAX_SIZE){
//				mainView.addView(optionItem);
//			}else{
//				addView(optionItem);
//			}
//		}
        this.getLayoutParams().height = CUSTOM_HEIGTH;
    }

    private NinePatchDrawable fundSep = null;

    public void addItem(List<String> optionList, int type) {
        this.type = type;
        if (type == FUND_TYPE)
            fundSep = (NinePatchDrawable) getContext().getResources().getDrawable(R.drawable.fund_tab_sep);
        addItem(optionList);
    }

    public void addItem(List<String> optionList, List<Integer> recordList) {
        this.optionList = optionList;
        final int size = optionList.size();
        redRect = ((BitmapDrawable) getResources().getDrawable(R.drawable.slidebar));
        if (size > MAX_SIZE) {
            horizontalScrollView = new HorizontalScrollView(getContext());
            horizontalScrollView.setHorizontalFadingEdgeEnabled(false);
            horizontalScrollView.setHorizontalScrollBarEnabled(false);
            mainView = new LinearLayout(getContext());
            horizontalScrollView.addView(mainView);
            addView(horizontalScrollView);
            buttonWidth = UIScreen.screenWidth / MAX_SIZE;
            rightArrow = ((BitmapDrawable) getResources().getDrawable(R.drawable.icon_more_right)).getBitmap();
            leftArrow = ((BitmapDrawable) getResources().getDrawable(R.drawable.icon_more_left)).getBitmap();

        } else {
            buttonWidth = UIScreen.screenWidth / size;
        }
        arrowMargin = StringUtils.dipToPx(2);

        for (int i = 0; i < size; i++) {
            TabBarItemView optionItem = new TabBarItemView(getContext());
            optionItem.setOnTouchListener(this);
            optionItem.setRecordID(recordList.get(i));
            optionItem.setText(i, optionList.get(i));
            optionItem.setTag(i);
            if (size > MAX_SIZE) {
                mainView.addView(optionItem);
            } else {
                addView(optionItem);
            }

            if (i == selectedIndex) {
                optionItem.setSelected(true);
                preView = optionItem;
            }
        }
    }


    public void setIndex(int index) {
        setIndex(index, true);
    }

    public void setIndex(int index, boolean touchAfter) {
        try {
            final TabBarItemView view;
            int tSize = optionList.size();
            if (isNeedAdd) {//ylzhang添加 防止无限进入+号
                tSize++;
                if (index >= tSize - 1) {
                    index = 0;
                }
            }


//			if(tSize > MAX_SIZE){
//				view = (TabBarItemView)mainView.getChildAt(index);
//			}else{
//				view = (TabBarItemView)getChildAt(index);
//			}

            if (null != mainView) {
                view = (TabBarItemView) mainView.getChildAt(index);
            } else {
                view = (TabBarItemView) getChildAt(index);
            }
            if (null != preView) {
                preView.setSelected(false);
                preView.optionItem.setTextSize(TXT_SIZE);
                preView = view;
            }
            view.setSelected(true);
            view.optionItem.setTextSize(TXT_SIZE);
            selectedIndex = StringUtils.valueOfInteger(String.valueOf(view.getTag()), 0);
            recordID = view.getRecordID();
            //		if(horizontalScrollView != null && view.getLeft() - horizontalScrollView.getScrollX() <= 0){
            //
            //			 Message message = handler.obtainMessage();
            //			 message.arg1 = 0;
            //			 handler.sendMessageDelayed(message, 300);
            //		}
            if (touchAfter) {
                if ((index + 1) > MAX_SIZE) {
                    setScrollWidth((index + 1 - MAX_SIZE) * buttonWidth);
                }
                if (touchEventListener != null) {
                    MotionEvent me = MotionEvent.obtain(0, 0, MotionEvent.ACTION_UP, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                    touchEventListener.touchEvent(this, me);
                    me.recycle();
                }
            } else {
//                int relativeIndex = index - 2 > 0 ? index - 2 : 0;
//                int relativeIndex = index - 1 > 0 ? index - 1 : 0;
//                setScrollWidth(relativeIndex * buttonWidth);
                int w = (index+ 1) * buttonWidth;
                w = w - UIScreen.screenWidth / 2 - buttonWidth / 2;
//                if (w > 0) {
                    setScrollWidth(w);
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    /**
     * handler???????
     */
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (horizontalScrollView == null) {
                return;
            }
            horizontalScrollView.smoothScrollTo(msg.what, 30);
        }
    };

    /**
     * ???ü??????
     *
     * @param touchEventListener ??????
     */
    public void setTouchListener(TouchEventListener touchEventListener) {
        this.touchEventListener = touchEventListener;
    }

    public boolean isAllowSelectedAnswerClick() {
        return allowSelectedAnswerClick;
    }

    public void setAllowSelectedAnswerClick(boolean allowSelectedAnswerClick) {
        this.allowSelectedAnswerClick = allowSelectedAnswerClick;
    }

    /**
     * 是否允许当前tab被点击
     * true 	允许
     * false	不允许
     */
    private boolean allowSelectedAnswerClick = false;


    public boolean onTouch(View view, MotionEvent arg1) {
        if (!isEnabled()) {
            return true;
        }

        final TabBarItemView item = (TabBarItemView) view;
        if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
            downPoint.x = (int) arg1.getX();
            downPoint.y = (int) arg1.getY();
        } else if (arg1.getAction() == MotionEvent.ACTION_MOVE) {
            if (horizontalScrollView != null) {
                if (Math.abs(downPoint.x - arg1.getX()) > 8) {
                    isMoved = true;
                }
                if (arg1.getY() < 0 || arg1.getY() > view.getHeight()) {
                    isMoved = true;
                }
            } else {
                if (arg1.getX() < 0 || arg1.getX() > view.getWidth()) {
                    isMoved = true;
                }
                if (arg1.getY() < 0 || arg1.getY() > view.getHeight()) {
                    isMoved = true;
                }
            }

        } else if (arg1.getAction() == MotionEvent.ACTION_UP) {
            if (!isMoved) {
                if (StringUtils.valueOfInteger(String.valueOf(item.getTag()), 0) == ADD_TAG) {
                    if (touchEventListener != null) {
                        selectedIndex = ADD_TAG;
                        touchEventListener.touchEvent(this, arg1);
                    }
                    return true;
                } else if (StringUtils.valueOfInteger(String.valueOf(item.getTag()), 0) == MORE_TAG) {
                    if (touchEventListener != null) {
                        selectedIndex = MORE_TAG;
                        touchEventListener.touchEvent(this, arg1);
                    }
                    return true;
                }
                if (preView != null) {
                    // TIP 4.1新增点击当前tab要刷新  根据是否允许选择的tab被点击的标记来判断
                    /**
                     * @see #setAllowSelectedAnswerClick(boolean)
                     */
                    if (preView == item && !allowSelectedAnswerClick) {
                        return true;
                    }
                    preView.setSelected(false);
                    preView.optionItem.setTextSize(TXT_SIZE);
                    //preView.postInvalidate();
                }
                item.postInvalidate();
                item.setSelected(true);


                preView = item;
                selectedIndex = StringUtils.valueOfInteger(String.valueOf(item.getTag()), 0);
                recordID = item.recordID;
                item.optionItem.setTextSize(TXT_SIZE);
                if (touchEventListener != null) {
                    touchEventListener.touchEvent(this, arg1);
                }
            }
            isMoved = false;
        } else if (arg1.getAction() == MotionEvent.ACTION_CANCEL) {
            isMoved = false;
        }
        return true;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public String getSelectedString() {
        return optionList.get(selectedIndex);
    }

    public List<String> getListString() {
        return optionList;
    }

    public int getRecordID() {
        return recordID;
    }

    public void setRecordID(int recordID) {
        this.recordID = recordID;
    }

    class TabBarItemView extends LinearLayout implements DisposeListener {

        private boolean isSelected;
        private final Paint paint = new Paint();
        private int index;
        private int recordID;
        public TextView optionItem;
        public int viewWidth = -1;


        public TabBarItemView(Context context) {
            super(context);
            scroller = new Scroller(context);
        }

        public int getRecordID() {
            return recordID;
        }

        public void setRecordID(int recordID) {
            this.recordID = recordID;
        }

        int tempTabBottomBgColor = 0xffEAEAEA;

        public void setText(int index, String text) {
            this.index = index;
            optionItem = new TextView(getContext());
            optionItem.setText(text);


            if (isSelected) {
                optionItem.setTextColor(0xffF24C42);
            } else {
                optionItem.setTextColor(0xFF222222);
            }

            optionItem.setTextSize(TXT_SIZE);
            setBackgroundColor(0xffffffff);
//            setBackgroundResource(R.drawable.transparent);
            //optionItem.setFocusable(true);
            //optionItem.setLayoutParams(lp);
            //自适应宽度
            if (isWrapContent) {

                if (isWidthGT) {
                    float textWidth = optionItem.getPaint().measureText(text);

                    optionItem.setWidth((int) (textWidth + 2 * StringUtils.dipToPx(TXT_SIZE)));
                } else {
                    optionItem.setWidth((int) buttonWidth);
                }

            } else {
                if (viewWidth != -1) {
                    optionItem.setWidth((int) viewWidth);
                } else {
                    optionItem.setWidth((int) buttonWidth);
                }
            }

            optionItem.setHeight(CUSTOM_HEIGTH);
            optionItem.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            optionItem.setPadding(0, 0, 0, arrowHeight >> 1);
            addView(optionItem);

            if (type == STOCK_TYPE) {
            } else if (type == FUND_TYPE) {
                if (isSelected) {
                    optionItem.setTextColor(FUND_SELECT_COLOR);
                    optionItem.setTextSize(TXT_SIZE);
                    if (StringUtils.valueOfInteger(getTag().toString(), 0) == 0) {
                        setBackgroundResource(R.drawable.fund_tab_left);
//						canvas.drawBitmap(fundSelectLeft, new Rect(0,0,fundSep.getWidth(), fundSep.getHeight()),
//								 new Rect(0,0,getWidth(),getHeight()), null);
                    } else if (StringUtils.valueOfInteger(getTag().toString(), 0) == optionList.size() - 1) {
                        setBackgroundResource(R.drawable.fund_tab_right);
//						canvas.drawBitmap(fundSelectRight, new Rect(0,0,fundSep.getWidth(), fundSep.getHeight()),
//								 new Rect(0,0,getWidth(),getHeight()), null);
                    } else {
                        setBackgroundResource(R.drawable.fund_tab_center);
//						canvas.drawBitmap(fundSelectCenter, new Rect(0,0,fundSep.getWidth(), fundSep.getHeight()),
//								 new Rect(0,0,getWidth(),getHeight()), null);
                    }
                } else {
                    optionItem.setTextColor(Color.BLACK);
                    setBackgroundResource(R.drawable.fund_tab_unfocus_center);
                    //setBackgroundColor(fundBgColor);
                }
            }
        }

        public void setSelected(boolean isSelected) {
            if (this.isSelected == isSelected) {
                return;
            } else if (isSelected) {
                invalidate();

                //	scroller.startScroll(0, 0, 100, 0, 200);
            } else if (!isSelected) {
                //scroller.startScroll(100, 0, -100, 0, 300);
            }
            this.isSelected = isSelected;

            if (isSelected) {
                optionItem.setTextColor(0xffF24C42);
            } else {
                optionItem.setTextColor(0xFF222222);
            }

            postInvalidate();
            super.setSelected(isSelected);
            if (type == FUND_TYPE) {
                if (isSelected) {
                    optionItem.setTextColor(FUND_SELECT_COLOR);
                    if (StringUtils.valueOfInteger(getTag().toString(), 0) == 0) {
                        setBackgroundResource(R.drawable.fund_tab_left);
//						canvas.drawBitmap(fundSelectLeft, new Rect(0,0,fundSep.getWidth(), fundSep.getHeight()),
//								 new Rect(0,0,getWidth(),getHeight()), null);
                    } else if (StringUtils.valueOfInteger(getTag().toString(), 0) == optionList.size() - 1) {
                        setBackgroundResource(R.drawable.fund_tab_right);
//						canvas.drawBitmap(fundSelectRight, new Rect(0,0,fundSep.getWidth(), fundSep.getHeight()),
//								 new Rect(0,0,getWidth(),getHeight()), null);
                    } else {
                        setBackgroundResource(R.drawable.fund_tab_center);
//						canvas.drawBitmap(fundSelectCenter, new Rect(0,0,fundSep.getWidth(), fundSep.getHeight()),
//								 new Rect(0,0,getWidth(),getHeight()), null);
                    }
                } else {
                    optionItem.setTextColor(Color.BLACK);
                    setBackgroundResource(R.drawable.fund_tab_unfocus_center);
                }
            }
            postInvalidate();
        }

        @Override
        public void computeScroll() {
            if (scroller.computeScrollOffset()) {
                if (scroller.getFinalX() == scroller.getCurrX()) {
                    scroller.forceFinished(true);
                }
                invalidate();
            }
        }

        //		private final int fundBgColor = 0xFFE5E5E5;
//		private final int fundSepColor = 0xFFBBBBBB;
//		private final int fundSepColorE = 0xFFF2F2F2;
//		private final int sepWidth = StringUtils.dipToPx(4);
        private final int FUND_SELECT_COLOR = 0xFF638C0B;
        private final int arrowHeight = StringUtils.dipToPx(5);
        private int speedTabBgColor = 0xFFFF2828;//行情tab底部颜色
        private int newsTabBgColor = 0xFFFF6326;//新闻tab底部颜色
        private int InfoTabBgColor = 0xFFFECF1F;//研报tab底部颜色
        private int BulletinTabBgColor = 0xFF16B74B;//公告tab底部颜色
        private int GainTabBgColor = 0xFF399EFC;//盈亏tab底部颜色

        @Override
        protected void dispatchDraw(Canvas canvas) {
            int alpha = 255;
            paint.setStyle(Paint.Style.FILL);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2);
//            paint.setColor(tempTabBottomBgColor);
//            paint.setAlpha(alpha);
//            if (needMargin) {
//                canvas.drawRect(new Rect(4, 4, getWidth() - 4, getHeight() - 4), paint);
//            } else {
//                canvas.drawRect(new Rect(0, 0, getWidth(), getHeight()), paint);
//            }

            if (isSelected) {
                paint.setStyle(Paint.Style.FILL);
//                paint.setColor(tempTabBottomBgColor);
//                paint.setAlpha(alpha);
//                if (needMargin) {
//                    canvas.drawRect(new Rect(4, 4, getWidth() - 4, getHeight() - 4), paint);
//                } else {
//                    canvas.drawRect(new Rect(0, 0, getWidth(), getHeight()), paint);
//                }

                paint.setColor(Color.RED);
                canvas.drawRect(new Rect(10, getHeight() - 2, getWidth() - 10, getHeight()), paint);
            }

            super.dispatchDraw(canvas);
        }

        public void dispose() {
            this.removeAllViews();
            optionItem = null;
        }
    }

    //彩虹头宽度是否自适应
    private boolean isWrapContent = true;

    public boolean isWrapContent() {
        return isWrapContent;
    }

    public void setWrapContent(boolean wrapContent) {
        isWrapContent = wrapContent;
    }

    public void setNeedArrow(boolean need) {
        this.needArrow = need;
    }

    public void setNeedMargin(boolean need) {
        this.needMargin = need;
    }
}