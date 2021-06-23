package com.bst.cloud.bstxposeddemo;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.lang.reflect.Field;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import de.robv.android.xposed.callbacks.XCallback;

public class BstXposedDemo1 implements IXposedHookLoadPackage {
    private static String TAG = "BstXposedDemo1";

//    @Override
//    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
//        XposedBridge.log("Loaded app: " + lpparam.packageName);
//        Log.d(TAG, "Loaded app: " + lpparam.packageName);
//
//
//        Class clazz = lpparam.classLoader.loadClass("com.nexon.nexonanalyticssdk.util.NxLogcat");
//        XposedHelpers.findAndHookMethod(NxLogcat.class, "handleBindApplication", "android.app.ActivityThread.AppBindData", new XC_MethodHook() {
//
//        };
//
//    }



    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        Log.d(TAG, "Bst handleLoadPackage test");

        if (loadPackageParam.packageName.equals("com.nexon.baram")) {
            XposedBridge.log("hook success!");
            Class clazz = loadPackageParam.classLoader.loadClass("com.nexon.nexonanalyticssdk.util.NxLogcat");

            Field field = XposedHelpers.findField(clazz, "DEBUG_FLAG");
            field.setBoolean(null, true);
            Log.d(TAG, "Set debug flag \"true\" success");

//            XposedHelpers.findAndHookMethod(clazz, "initLogcatLevel", new XC_MethodHook() {
//                @Override
//                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                    super.beforeHookedMethod(param);
//                }
//
//                @Override
//                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                    super.afterHookedMethod(param);
//                }
//            });

            // 修改返回结果
            XposedHelpers.findAndHookMethod("com.nexon.nexonanalyticssdk.util.NxLogcat",
                    loadPackageParam.classLoader, "initLogcatLevel", Context.class, new XC_MethodHook() {
                        @Override
                        public int compareTo(XCallback o) {
                            return 0;
                        }

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            // 修改debug开关
                            Field field = XposedHelpers.findField(clazz, "DEBUG_FLAG");
                            field.setBoolean(null, true);
                            boolean value = field.getBoolean(null);
                            if(value)
                                Log.d(TAG, "Bst Xposed. Set debug flag \"true\" success.");
                            else
                                Log.d(TAG, "Bst Xposed. Set debug flag \"true\" failed.");
                        }


                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                        }
                    });

            // 修改返回结果
            XposedHelpers.findAndHookMethod("com.nexon.nexonanalyticssdk.feature.emulator.NxEmulatorInfo",
                    loadPackageParam.classLoader, "detectEmulator", new XC_MethodHook() {
                        @Override
                        public int compareTo(XCallback o) {
                            return 0;
                        }

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            String result = (String) param.getResult();
                            Log.d(TAG, "detectEmulator return: " + result);
                            param.setResult("NoEmulator");
                            Log.d(TAG, "Replace detectEmulator return: " + (String) param.getResult());
                        }
                    });

            Class clazz1 = loadPackageParam.classLoader.loadClass("com.nexon.nexonanalyticssdk.core.NxScheduler");

            // 替换原来的函数
            XposedHelpers.findAndHookMethod(clazz1, "startInfoUser", new XC_MethodReplacement() {
                @Override
                public int compareTo(XCallback o) {
                    return 0;
                }

                @Override
                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                    Log.d(TAG, "replace startInfoUser method.");
                    return null;
                }
            });


            // 替换原来的函数
            Class clazz2 = loadPackageParam.classLoader.loadClass("com.nexon.ngsm.Ngsm");

            XposedHelpers.findAndHookMethod(clazz2, "onAbuseDetect", int.class, String.class, boolean.class, new XC_MethodReplacement() {
                @Override
                public int compareTo(XCallback o) {
                    return 0;
                }

                @Override
                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                    Log.d(TAG, "replace onAbuseDetect method.");
                    return null;
                }
            });


            // 先反射获取到要被调用的类：Ngsm
            Class<?> ngsmClass = XposedHelpers.findClass("com.nexon.ngsm.Ngsm", loadPackageParam.classLoader);
            // 替换原来的函数
            XposedHelpers.findAndHookMethod("com.nexon.ngsm.Ngsm",
                    loadPackageParam.classLoader, "ngsmNativeInit", Activity.class, Context.class,
                    ngsmClass, int.class,

                    new XC_MethodReplacement() {
                        @Override
                        public int compareTo(XCallback o) {
                            return 0;
                        }

                        @Override
                        protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                            Log.d(TAG, "replace ngsmNativeInit method.");
                            return null;
                        }
                    });


        }
    }
}
