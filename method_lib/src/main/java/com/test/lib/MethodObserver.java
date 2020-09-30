package com.test.lib;

public interface MethodObserver {
    default void onMethodEnter(String tag, String methodName) {}
    default void onMethodExit(String tag, String methodName) {}
}