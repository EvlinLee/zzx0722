package com.eryue.listener;

public interface DisposeListener {

    /**
     * 释放自己分配的内存，解除其它对象的引用
     */
    public abstract void dispose();
}
