package com.eryue.widget;

import android.view.View;
import android.view.ViewGroup;

import com.eryue.listener.DisposeListener;

import java.util.List;
import java.util.Vector;


public class DisposeHelper {

    public DisposeHelper() {

    }

    public static void dispose(DisposeListener view) {
        if (view != null && view instanceof View) {
            view.dispose();
        }
    }

    public static void dispose(DisposeListener[] view) {
        if (view == null) return;
        for (int i = 0; i < view.length; i++) {
            if (view[i] != null && view[i] instanceof View) view[i].dispose();
            view[i] = null;
        }
    }

    public static void dispose(DisposeListener[][] views) {
        if (views == null) return;
        for (int i = 0; i < views.length; i++) {
            if (views[i] != null) dispose(views[i]);
            views[i] = null;
        }
    }

    public static void dispose(Vector v) {
        if (v == null) return;
        for (int i = 0, n = v.size(); i < n; i++) {
            if (v.elementAt(i) != null && v.elementAt(i) instanceof DisposeListener) {
                ((DisposeListener) v.elementAt(i)).dispose();
            }
        }
        v.clear();
    }

    public static void dispose(List _list) {
        if (_list == null) return;
        for (int i = 0, n = _list.size(); i < n; i++) {
            if (_list.get(i) != null && _list.get(i) instanceof DisposeListener) {
                ((DisposeListener) _list.get(i)).dispose();
            }
        }
        _list.clear();
    }

    public static void disposeViewGroup(View view) {
        ViewGroup viewGroup;
        try {
            viewGroup = (ViewGroup) view;
        } catch (ClassCastException ex) {
            ex.printStackTrace();
            viewGroup = null;
        }

        if (viewGroup == null)
            return;
        View v;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            v = viewGroup.getChildAt(i);
            if (v instanceof DisposeListener) {
                ((DisposeListener) v).dispose();
            } else if (v instanceof ViewGroup) {
                disposeViewGroup(v);
            }
        }
    }
}
