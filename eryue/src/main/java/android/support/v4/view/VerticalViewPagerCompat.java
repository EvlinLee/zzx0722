package android.support.v4.view;

public final class VerticalViewPagerCompat {
    private VerticalViewPagerCompat() {}

    public static void setDataSetObserver(PagerAdapter adapter, DataSetObserver observer) {
        adapter.registerDataSetObserver(observer);
    }

    public static class DataSetObserver extends android.database.DataSetObserver {
    }
}
