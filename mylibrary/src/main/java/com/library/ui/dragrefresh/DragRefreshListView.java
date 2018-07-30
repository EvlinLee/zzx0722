package com.library.ui.dragrefresh;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.text.Html;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.library.R;
import com.library.util.CommonFunc;
import com.library.util.StringUtils;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * DragRefreshListView
 * <p/>
 * ListView support (a) Pull down to refresh, (b) Pull up
 * to load more. Implement setDragRefreshListViewListener, and see stopRefresh() /
 * stopLoadMore()
 *
 * @author ylrong
 */
public class DragRefreshListView extends ListView implements OnScrollListener {

    private static final String TAG = DragRefreshListView.class.getName();

    private float mLastY = -1; // save event y
    private Scroller mScroller; // used for scroll back
//	private OnScrollListener mScrollListener; // user's scroll listener

    // the interface to trigger refresh and load more.
    private DragRefreshListViewListener dragRefreshListViewListener;

    // -- header view
    protected ListViewHeader mHeaderView;
    // header view content, use it to calculate the Header's height. And hide it
    // when disable pull refresh.
    private RelativeLayout mHeaderViewContent;
    private TextView mHeaderTimeView;
    //	private TextView mHeaderLastTimeView;
    private int mHeaderViewHeight; // header view's height
    private boolean mEnablePullRefresh = true;
    private boolean mPullRefreshing = false; // is refreashing.

    private boolean loadTimeout = false; //判断加载是否超时

    // -- footer view
    private ListViewFooter mFooterView;

    // total list items, used to detect is at the bottom of listview.
    private int mTotalItemCount;

    // for mScroller, scroll back from header or footer.
    private int mScrollBack;
    private final static int SCROLLBACK_HEADER = 0;
    private final static int SCROLLBACK_FOOTER = 1;

    private final static int SCROLL_DURATION = 400; // scroll back duration
    private final static int PULL_LOAD_MORE_DELTA = 80; // when pull up >= 50px
    // at bottom, trigger
    // load more.
    private final static float OFFSET_RADIO = 1.5f; // support iOS like pull
    // feature.

    /**
     * @param context
     */
    public DragRefreshListView(Context context) {
        super(context);
        initWithContext(context);
    }

    public DragRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWithContext(context);
    }

    public DragRefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initWithContext(context);
    }

    public void setOnScrollListener() {
        setOnScrollListener(this);
    }

    private void initWithContext(Context context) {
        mScroller = new Scroller(context, new DecelerateInterpolator());
        // XListView need the scroll event, and it will dispatch the event to
        // user's listener (as a proxy).
        super.setOnScrollListener(this);

        // init header view
        mHeaderView = new ListViewHeader(context);
        mHeaderViewContent = (RelativeLayout) mHeaderView
                .findViewById(R.id.listview_header);
        mHeaderTimeView = (TextView) mHeaderView
                .findViewById(R.id.listview_header_time);
//		mHeaderLastTimeView = (TextView) mHeaderView
//				.findViewById(R.id.listview_header_last_time);
        addHeaderView(mHeaderView);

        // init footer view
        mFooterView = new ListViewFooter(context);

        // init header height
        mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
                new OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mHeaderViewHeight = mHeaderViewContent.getHeight();
                        getViewTreeObserver()
                                .removeGlobalOnLayoutListener(this);
                    }
                });
    }

    public View getFooterView() {
        return mFooterView;
    }

    /**
     * 动态配置底部FooterView字体颜色
     *
     * @param color
     */
    public void setFooterViewColor(int color) {
        ((TextView) mFooterView.findViewById(R.id.listview_footer_textview))
                .setTextColor(color);
    }

    /**
     * 动态配置顶部HeaderView字体颜色
     *
     * @param hintColor
     * @param lastTimeColor
     * @param timeColor
     */
    public void setHeaderViewColor(int hintColor, int lastTimeColor,
                                   int timeColor) {
        ((TextView) mHeaderView
                .findViewById(R.id.listview_header_hint_textview))
                .setTextColor(hintColor);
        ((TextView) mHeaderView.findViewById(R.id.listview_header_last_time))
                .setTextColor(lastTimeColor);
        ((TextView) mHeaderView.findViewById(R.id.listview_header_time))
                .setTextColor(timeColor);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        try {
            super.dispatchDraw(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean mIsFooterReady = false;
    @Override
    protected void layoutChildren() {
        try {
            super.layoutChildren();
        }catch (IllegalStateException e){
            e.printStackTrace();
        }

    }
    @Override
    public void setAdapter(ListAdapter adapter) {
        // make sure ListViewFooter is the last footer view, and only add once.
        if (!mIsFooterReady) {
            mIsFooterReady = true;
            addFooterView(mFooterView);
        }
        super.setAdapter(adapter);

        adapterIsInit = true;
        if (adapter != null) {
            adapter.registerDataSetObserver(new DataSetObserver() {
                @Override
                public void onChanged() {
                    // TODO Auto-generated method stub
                    super.onChanged();
                    adapterIsInit = true;
                    isInitFirst = false;
                }
            });
        }
    }

    /**
     * 设置HeaderView是否隐藏，是否有下拉刷新功能
     *
     * @param enable
     */
    public void setHeaderViewEnable(boolean enable) {
        isRefreshExist = enable;
//		mEnablePullRefresh = enable;
//		if (!mEnablePullRefresh) { // disable, hide the content
//			mHeaderViewContent.setVisibility(View.INVISIBLE);
//		} else {
//			mHeaderViewContent.setVisibility(View.VISIBLE);
//		}
    }

    /**
     * 是否可上拉加载更多
     */
    private boolean mEnablePullLoad;
    /**
     * 是否正在加载更多
     */
    private boolean mPullLoading;

    public int getHeaderViewState() {
        return mHeaderView.getState();
    }

    //添加显示头部进度条的方法
    public void setHeaderViewState() {
        mPullRefreshing = true;
        setHeaderViewEnable(true);
        mHeaderView.setVisiableHeight(StringUtils.dipToPx(60));
        mHeaderView.setState(ListViewHeader.STATE_REFRESHING);
        if (dragRefreshListViewListener != null) {
            setFooterViewState(ListViewFooter.STATE_HIDE);
            dragRefreshListViewListener.onRefresh();
        }
//        resetHeaderHeight();
    }

    private boolean isAutoLoadMore = false;

    /**
     * 是否自动加载更多
     *
     * @param isAutoLoadMore
     */
    public void setAutoLoadMore(boolean isAutoLoadMore) {
        this.isAutoLoadMore = isAutoLoadMore;
        mEnablePullLoad = true;
        mPullLoading = true;
    }

    private int footState;
    /**
     * 设置FooterView的状态+normal时显示文字
     * state == ListViewFooter.STATE_NORMAL 更多
     * state == ListViewFooter.STATE_LOADING ProgressBar+加载中
     * state == ListViewFooter.STATE_HIDE 隐藏
     *
     * @param state
     */
    public void setFooterViewState(int state) {
        footState = state;
        setFooterViewState(state, null);
    }

    public void setFooterViewState(final int state, final String showBtn, final int color) {
        post(new Runnable() {
            @Override
            public void run() {
                setFooterViewState(state, showBtn);
                mFooterView.setFootTextColor(color);
            }
        });
    }

    private int state;

    /**
     * 设置FooterView的状态+normal时显示文字
     * state == ListViewFooter.STATE_NORMAL 更多或者showBtn 可点击
     * state == ListViewFooter.STATE_LOADING ProgressBar+加载中 不可点击
     * state == ListViewFooter.STATE_HIDE 隐藏
     * state == ListViewFooter.STATE_RESULT 显示结果(如无数据结果)  不可点击
     *
     * @param state
     * @param showBtn
     */
    public void setFooterViewState(int state, String showBtn) {
        mFooterView.setState(state, showBtn);
        this.state = state;
        mPullLoading = false;
        mEnablePullLoad = true;
        if (state == ListViewFooter.STATE_NORMAL) {
            mFooterView.setFootTextColor(Color.GRAY);
            mEnablePullLoad = true;
            mFooterView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startLoadMore();
                }
            });
        } else if (state == ListViewFooter.STATE_LOADING) {
            mEnablePullLoad = true;
            mPullLoading = true;
            mFooterView.setOnClickListener(null);
        } else if (state == ListViewFooter.STATE_HIDE) {
        } else if (state == ListViewFooter.STATE_RESULT) {
            mFooterView.setOnClickListener(null);
        } else if (state == ListViewFooter.STATE_BUTTON) {
            mEnablePullLoad = false;
            mFooterView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startLoadMore();
                }
            });
        }
    }

    /**
     * ListView数据更新后,更改状态
     *
     * @param timeIsNeedRefresh 是否需要更新时间,当为加载更多调用时不需要更新时间,置false,只有下拉刷新和首次进页面请求数据后才置为true
     * @param time              具体时间
     */
    public void refreshComplete(boolean timeIsNeedRefresh, long time) {
        stopRefresh();
        stopLoadMore();
        if (timeIsNeedRefresh) {
            if (time == 0) {
                time = System.currentTimeMillis();
            }
            Calendar timeCalendar = Calendar.getInstance(TimeZone.getDefault());
            timeCalendar.clear();
            timeCalendar.setTimeInMillis(time);
            int month = timeCalendar.get(Calendar.MONTH) + 1;
            int day = timeCalendar.get(Calendar.DAY_OF_MONTH);
            int hour = timeCalendar.get(Calendar.HOUR_OF_DAY);
            int minute = timeCalendar.get(Calendar.MINUTE);
            String strTime = CommonFunc.paddingZeroLeft(String.valueOf(month), 2)
                    + "-" + CommonFunc.paddingZeroLeft(String.valueOf(day), 2)
                    + " " + CommonFunc.paddingZeroLeft(String.valueOf(hour), 2)
                    + ":" + CommonFunc.paddingZeroLeft(String.valueOf(minute), 2);
            setRefreshTime(strTime);
        }
    }


    private void stopLoadMore() {
        if (mPullLoading) {
            mPullLoading = false;
            setFooterViewState(footState, null);
        }
    }

    /**
     * stop refresh, reset header view.
     */
    private void stopRefresh() {
        if (mPullRefreshing == true) {
            mPullRefreshing = false;
            resetHeaderHeight();
        }
    }

    /**
     * set last refresh time
     *
     * @param time
     */
    private void setRefreshTime(String time) {
//		mHeaderLastTimeView.setVisibility(View.VISIBLE);
        mHeaderTimeView.setText(time);
    }

    private void invokeOnScrolling() {
//		if (mScrollListener instanceof OnDragRefreshScrollListener) {
//			OnDragRefreshScrollListener l = (OnDragRefreshScrollListener) mScrollListener;
//			l.onDragRefreshScrolling(this);
//		}
    }

    private void updateHeaderHeight(float delta) {
        mHeaderView.setVisiableHeight((int) delta
                + mHeaderView.getVisiableHeight());
        if (mEnablePullRefresh && !mPullRefreshing) { // 未处于刷新状态，更新箭头
            if (mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
                mHeaderView.setState(ListViewHeader.STATE_READY);
            } else {
                mHeaderView.setState(ListViewHeader.STATE_NORMAL);
            }
        }
        setSelection(0); // scroll to top each time
    }

    /**
     * reset header view's height.
     */
    private void resetHeaderHeight() {
        int height = mHeaderView.getVisiableHeight();
        if (height == 0) // not visible.
            return;
        // refreshing and header isn't shown fully. do nothing.
        if (mPullRefreshing && height <= mHeaderViewHeight) {
            return;
        }
        int finalHeight = 0; // default: scroll back to dismiss header.
        // is refreshing, just scroll back to show all the header.
        if (mPullRefreshing && height > mHeaderViewHeight) {
            finalHeight = mHeaderViewHeight;
        }
        mScrollBack = SCROLLBACK_HEADER;
        mScroller.startScroll(0, height, 0, finalHeight - height,
                SCROLL_DURATION);
        // trigger computeScroll
        invalidate();
    }

//	private void updateFooterHeight(float delta) {
//		int height = mFooterView.getBottomMargin() + (int) delta;
//		if (mEnablePullLoad && !mPullLoading) {
//			if (height > PULL_LOAD_MORE_DELTA) { // height enough to invoke load
//													// more.
//				mFooterView.setState(ListViewFooter.STATE_READY);
//			} else {
//				mFooterView.setState(ListViewFooter.STATE_NORMAL);
//			}
//		}
//		mFooterView.setBottomMargin(height);
//
////		setSelection(mTotalItemCount - 1); // scroll to bottom
//	}

    private void resetFooterHeight() {
        int bottomMargin = mFooterView.getBottomMargin();
        if (bottomMargin > 0) {
            mScrollBack = SCROLLBACK_FOOTER;
            mScroller.startScroll(0, bottomMargin, 0, -bottomMargin,
                    SCROLL_DURATION);
            invalidate();
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            stopLoadMore();
            if (loadTimeout) {
                String htmlText = getResources().getString(R.string.listview_footer_failure);
                mFooterView.getmTextView().setText(Html.fromHtml(htmlText));
            }
        }

        ;
    };

    private void startLoadMore() {
        if (!isAutoLoadMore&&state != ListViewFooter.STATE_BUTTON) {
            setFooterViewState(ListViewFooter.STATE_LOADING, null);

        }

        if (dragRefreshListViewListener != null) {
            dragRefreshListViewListener.onLoadMore();
        }
    }

    public void loadTimeout(boolean timeout) {
        loadTimeout = timeout;
        handler.sendEmptyMessage(0);
    }

    /**
     * 更多控制
     */
    private boolean isLoadMoreExist;
    /**
     * 刷新控制
     */
    private boolean isRefreshExist = true;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getY();
            isLoadMoreExist = false;
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getY();
                isLoadMoreExist = false;
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getY() - mLastY;
                mLastY = ev.getY();
                if (getFirstVisiblePosition() == 0
                        && (mHeaderView.getVisiableHeight() > 0 || deltaY > 0) && isRefreshExist) {
                    // the first item is showing, header has shown or pull down.
                    updateHeaderHeight(deltaY / OFFSET_RADIO);
                    invokeOnScrolling();
                } else if (getLastVisiblePosition() == mTotalItemCount - 1
                        && (mFooterView.getBottomMargin() > 0 || deltaY < 0)) {
                    // last item, already pulled up or want to pull up.
//				updateFooterHeight(-deltaY / OFFSET_RADIO);
                    isLoadMoreExist = true;
                }
                break;
            default:
                mLastY = -1; // reset
                if (getFirstVisiblePosition() == 0 && isRefreshExist) {
                    // invoke refresh
                    if (mEnablePullRefresh
                            && mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
                        mPullRefreshing = true;
                        mHeaderView.setState(ListViewHeader.STATE_REFRESHING);
                        if (dragRefreshListViewListener != null) {
                            setFooterViewState(ListViewFooter.STATE_HIDE);
                            dragRefreshListViewListener.onRefresh();
                        }
                    }
                    resetHeaderHeight();
                } else if (getLastVisiblePosition() == mTotalItemCount - 1) {
                    // invoke load more.
//				if (mEnablePullLoad
//						&& mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA) {
//					startLoadMore();
//				}
//				resetFooterHeight();

                    if (isLoadMoreExist && mEnablePullLoad) {
                        startLoadMore();
                        return true;
                    }
                }
                break;
        }
        try {
            return super.onTouchEvent(ev);
        }catch (Exception e){
            e.printStackTrace();
            return  false;
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (mScrollBack == SCROLLBACK_HEADER) {
                mHeaderView.setVisiableHeight(mScroller.getCurrY());
            } else {
                mFooterView.setBottomMargin(mScroller.getCurrY());
            }
            postInvalidate();
            invokeOnScrolling();
        }
        super.computeScroll();
    }

//Override
//	public void setOnScrollListener(OnScrollListener l) {
//		mScrollListener = l;
//	}

    /**
     * adapter初始化
     */
    private boolean adapterIsInit = false;
    /**
     * 页面在setAdapter后数据第一次初始化
     */
    private boolean isInitFirst = false;

    private ScrollStateChangeListener scrollStateChangeListener;

    public void setScrollStateChangeListener(
            ScrollStateChangeListener scrollStateChangeListener) {
        this.scrollStateChangeListener = scrollStateChangeListener;
    }

    public interface ScrollStateChangeListener {

        // -1 代表没有变化  isUp: true.Up   false.down
        public void onItemsDisappear(int startPosition, int endPosition, boolean isUp);

        public void onItemsVisible(int startPosition, int endPosition, boolean isUp);
    }

    private int itemsCount;
    private int startFirst;
    private int startLast;
    private int endFirst;
    private int endLast;

    /**
     * 0.开始滚动 1.isFling 2.SCROLL_STATE_IDLE
     */
    public int isFlingState = 0;

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
            // 开始滚动监听
            if (isFlingState != 1) {
                startFirst = view.getFirstVisiblePosition();
                startLast = view.getLastVisiblePosition();
                isFlingState = 0;
            }

        } else if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
            if (isFlingState != 2) {
                if (isAutoLoadMore && mEnablePullLoad
                        && view.getLastVisiblePosition() == view.getCount() - 1) {
                    startLoadMore();
                }
                resetHeaderHeight();
                scrollStateIdle(view);
                isFlingState = 2;
            }
        } else if (scrollState == OnScrollListener.SCROLL_STATE_FLING) {
            isFlingState = 1;
            if (view.getFirstVisiblePosition() == 0
                    || view.getLastVisiblePosition() == view.getCount() - 1) {
                if (isAutoLoadMore && mEnablePullLoad
                        && view.getLastVisiblePosition() == view.getCount() - 1) {
                    startLoadMore();
                }
                // 增加冗余判断,是否已滚动到顶端或者底端,解决MOTO手机等特殊机型问题
                // 滚动停止之后监听不了SCROLL_STATE_IDLE(主要体现在滚动到最上或者最下时)
                resetHeaderHeight();
                scrollStateIdle(view);
                isFlingState = 2;
            }
        }

//		if (mScrollListener != null) {
//			mScrollListener.onScrollStateChanged(view, scrollState);
//		}
    }

    private void scrollStateIdle(AbsListView view) {
        if (scrollStateChangeListener != null) {
            // 滚动停止监听
            endFirst = view.getFirstVisiblePosition();
            endLast = view.getLastVisiblePosition();
            int disappearFirst;
            int disappearLast;
            int visibleFirst;
            int visibleLast;
            boolean isUp = false; // true.up false.down
            if (endLast < startFirst || startLast < endFirst) {
                // 1.左右分离
                if (endLast < startFirst) {
                    isUp = true;
                } else {
                    isUp = false;
                }
                disappearFirst = startFirst - 1 < 0 ? 0 : startFirst - 1;
                disappearLast = startLast - 1 > itemsCount - 1 ? itemsCount - 1
                        : startLast - 1;
                scrollStateChangeListener.onItemsDisappear(disappearFirst,
                        disappearLast, isUp);
                visibleFirst = endFirst - 1 < 0 ? 0 : endFirst - 1;
                visibleLast = endLast - 1 > itemsCount - 1 ? itemsCount - 1
                        : endLast - 1;
//				scrollStateChangeListener.onItemsVisible(visibleFirst,
//						visibleLast, isUp);
            } else if (startFirst == endFirst && startLast == endLast) {
                // 2.完全重合 TODO
//				scrollStateChangeListener.onItemsDisappear(-1, -1);
//				scrollStateChangeListener.onItemsVisible(-1, -1);
            } else if (endFirst <= startLast && startLast < endLast
                    && startFirst < endFirst) {
                // 3.部分重叠，在右
                isUp = false;
                disappearFirst = startFirst - 1 < 0 ? 0 : startFirst - 1;
                disappearLast = endFirst - 1 - 1 < 0 ? 0 : endFirst - 1 - 1;
                scrollStateChangeListener.onItemsDisappear(disappearFirst,
                        disappearLast, isUp);
                visibleFirst = startLast - 1 + 1 > itemsCount - 1 ? itemsCount - 1
                        : startLast - 1 + 1;
                visibleLast = endLast - 1 > itemsCount - 1 ? itemsCount - 1
                        : endLast - 1;
//				scrollStateChangeListener.onItemsVisible(visibleFirst,
//						visibleLast, isUp);
            } else if (startFirst <= endLast && endFirst < startFirst
                    && endLast < startLast) {
                // 4.部分重叠，在左
                isUp = true;
                disappearFirst = endLast - 1 + 1 > itemsCount - 1 ? itemsCount - 1
                        : endLast - 1 + 1;
                disappearLast = startLast - 1 > itemsCount - 1 ? itemsCount - 1
                        : startLast - 1;
                scrollStateChangeListener.onItemsDisappear(disappearFirst,
                        disappearLast, isUp);
                visibleFirst = endFirst - 1 < 0 ? 0 : endFirst - 1;
                visibleLast = startFirst - 1 - 1 < 0 ? 0 : startFirst - 1 - 1;
//				scrollStateChangeListener.onItemsVisible(visibleFirst,
//						visibleLast, isUp);
            } else {
                // 5.其他特殊情况，主要体现在上拉刷新以及下拉加载更多时. TODO
            }
            // 修改图片问题,主要体现在MOTO手机上
            scrollStateChangeListener.onItemsVisible(endFirst - 1 < 0 ? 0
                    : endFirst - 1, endLast, isUp);
//			scrollStateChangeListener
//			.onItemsVisible(endFirst - 1 < 0 ? 0 : endFirst - 1,
//					endLast - 1 > itemsCount - 1 ? itemsCount - 1
//							: endLast - 1, isUp);
        }
    }

    /**
     * 控制当页面变化时，图片的刷新
     */
    private boolean isSizeChanged = false;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // TODO Auto-generated method stub
        super.onSizeChanged(w, h, oldw, oldh);
        isSizeChanged = true;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (dragRefreshScrollListener != null) {
            dragRefreshScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
        if (adapterIsInit && !isInitFirst) {
            isInitFirst = true;
            // 第一次给出显示位置
            if (scrollStateChangeListener != null) {
                if (totalItemCount > 2) {
                    int visibleFirst = firstVisibleItem - 1 < 0 ? 0
                            : firstVisibleItem - 1;
                    int visibleLast = firstVisibleItem + visibleItemCount > totalItemCount ? firstVisibleItem
                            + visibleItemCount - 2
                            : firstVisibleItem + visibleItemCount - 1;
                    scrollStateChangeListener.onItemsVisible(visibleFirst,
                            visibleLast, true);
                }
            }
        } else if (isSizeChanged) {
            isSizeChanged = false;
            if (scrollStateChangeListener != null) {
                if (totalItemCount > 2) {
                    int visibleFirst = firstVisibleItem - 1 < 0 ? 0
                            : firstVisibleItem - 1;
                    int visibleLast = firstVisibleItem + visibleItemCount > totalItemCount ? firstVisibleItem
                            + visibleItemCount - 2
                            : firstVisibleItem + visibleItemCount - 1;
                    scrollStateChangeListener.onItemsVisible(visibleFirst,
                            visibleLast, true);
                }
            }
        }
        itemsCount = totalItemCount - 2 < 0 ? 0 : (totalItemCount - 2);
        // send to user's listener
        mTotalItemCount = totalItemCount;
//		if (mScrollListener != null) {
//			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
//					totalItemCount);
//		}
    }

    public void setDragRefreshListViewListener(DragRefreshListViewListener l) {
        dragRefreshListViewListener = l;
    }

    /**
     * you can listen ListView.OnScrollListener or this one. it will invoke
     * onXScrolling when header/footer scroll back.
     */
    public interface OnDragRefreshScrollListener extends OnScrollListener {
        public void onDragRefreshScrolling(View view);
    }

    /**
     * implements this interface to get refresh/load more event.
     */
    public interface DragRefreshListViewListener {
        public void onRefresh();

        public void onLoadMore();
    }


    private DragRefreshScrollListener dragRefreshScrollListener;

    public void setDragRefreshScrollListener(DragRefreshScrollListener dragRefreshScrollListener) {
        this.dragRefreshScrollListener = dragRefreshScrollListener;
    }

    public interface DragRefreshScrollListener {
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount);
    }
}
