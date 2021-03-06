package com.zhihu.client.utils;

import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/9 0009.
 * 各种方法集合的类，可以把一个方法类以key-value的形式放入本类，
 * 可以通过key值类调用相应的方法
 */
public class Functions {


    /**
     * 没带阐述的返回值的方法
     * @param <Result>
     * @param <Param>
     */
    public static abstract class FunctionWithParamAndResult<Result, Param> extends Function{

        public FunctionWithParamAndResult(String functionName) {
            super(functionName);
        }
        public abstract Result function(Param param);
    }

    /**
     * 基础方法类
     */
    public static abstract class Function{
        //方法的名字，用来做调用，也可以理解为方法的指针
        public String mFunctionName;
        public Function(String functionName){
            this.mFunctionName = functionName;
        }
    }

    /**
     * 有返回值，没有参数的方法
     * @param <Result>
     */
    public static abstract class FunctionWithResult<Result> extends Function{

        public FunctionWithResult(String functionName) {
            super(functionName);
        }
        public abstract Result function();
    }

    /**
     * 带有参数没有返回值的方法
     * @param <Param>
     */
    public static abstract class FunctionWithParam<Param> extends Function{

        public FunctionWithParam(String functionName) {
            super(functionName);
        }

        public abstract void function(Param param);
    }

    /**
     * 没有返回值和参数的方法
     */
    public static abstract class FunctionNoParamAndResult extends Function{

        public FunctionNoParamAndResult(String functionName) {
            super(functionName);
        }
        public abstract void function();
    }

    private HashMap<String, FunctionWithParam> mFunctionWithParam;
    private HashMap<String, FunctionWithResult> mFunctionWithResult;
    private HashMap<String, FunctionNoParamAndResult> mFunctionNotParamAndResult;
    private HashMap<String, FunctionWithParamAndResult> mFunctionWithParamAndResult;

    /**
     * 添加带参数的函数
     * @param function
     * @return
     */
    public Functions addFunction(FunctionWithParam function){
        if (function == null){
            return this;
        }
        if (mFunctionWithParam == null){
            mFunctionWithParam = new HashMap<>(1);
        }
        mFunctionWithParam.put(function.mFunctionName, function);
        return this;
    }

    /**
     * 添加带返回值的函数
     * @param function
     * @return
     */
    public Functions addFunction(FunctionNoParamAndResult function){
        if (function == null){
            return this;
        }
        if (mFunctionNotParamAndResult == null){
            mFunctionNotParamAndResult = new HashMap<>(1);
        }
        mFunctionNotParamAndResult.put(function.mFunctionName, function);
        return this;
    }

    /**
     * 添加既有参数又有返回值的函数
     * @param function
     * @return
     */
    public Functions addFunction(FunctionWithParamAndResult function){
        if (function == null){
            return this;
        }
        if (mFunctionWithParamAndResult == null){
            mFunctionWithParamAndResult = new HashMap<>(1);
        }
        mFunctionWithParamAndResult.put(function.mFunctionName, function);
        return this;
    }

    /**
     * 添加带返回值的函数
     * @param function
     * @return
     */
    public Functions addFunction(FunctionWithResult function){
        if (function == null){
            return this;
        }
        if (mFunctionWithResult == null){
            mFunctionWithResult = new HashMap<>(1);
        }
        mFunctionWithResult.put(function.mFunctionName, function);
        return this;
    }

    /**
     * 根据函数名，回调无参数无返回值的函数
     * @param funcName
     * @throws FunctionException
     */
    public void invokeFunc(String funcName) throws FunctionException{
        FunctionNoParamAndResult f = null;
        if (mFunctionNotParamAndResult != null){
            f = mFunctionNotParamAndResult.get(funcName);
            if (f != null){
                f.function();
            }
        }
        if (f == null){
            throw new FunctionException("没有此函数");
        }
    }

    /**
     * 根据函数名，回调无参数有返回值的函数
     * @param funcName
     * @param c
     * @param <Result>
     * @return
     * @throws FunctionException
     */
    public <Result> Result invokeFuncWithResult(String funcName, Class<Result> c) throws FunctionException{
        FunctionWithResult f = null;
        if (mFunctionWithResult != null){
            f = mFunctionWithResult.get(funcName);
            if (f != null){
                if (c != null){
                    return c.cast(f.function());
                } else {
                    return (Result)f.function();
                }
            }
        }
        if (f == null){
            throw new FunctionException("没有此函数");
        }
        return null;
    }
    /**
     * 调用具有参数的函数
     */
    public <Param> void invokeFunc(String funcName, Param param) throws FunctionException{
        FunctionWithParam f = null;
        if (mFunctionWithParam != null){
            f = mFunctionWithParam.get(funcName);
            if (f != null){
                f.function(param);
            }
        }
    }

    /**
     * 调用具体函数，同时具有返回值的函数
     * @param funcName
     * @param param
     * @param c
     * @param <Result>
     * @param <Param>
     * @return
     * @throws FunctionException
     */
    public <Result, Param> Result invokeFuncWithResult(String funcName, Param param, Class<Result> c) throws FunctionException{
        FunctionWithParamAndResult f = null;
        if (mFunctionWithParamAndResult != null){
            f = mFunctionWithParamAndResult.get(funcName);
            if (f != null){
                if (c != null){
                    return c.cast(f.function(param));
                } else {
                    return (Result)f.function(param);
                }
            }
        }
        if (f == null){
            throw new FunctionException("没有此函数");
        }
        return null;
    }

    /**
     * 函数的参数，当函数的参数涉及到多个值时，可以用此类，
     * 此类使用规则：存参数与取参数的顺序必须一致，否则报错
     */
    public static class FunctionParams{
        private Bundle mParams = new Bundle(1);
        private int mIndex = -1;
        private Map mObjectParams = new HashMap(1);

        FunctionParams(Bundle mParams, Map mObjectParams){
            this.mParams = mParams;
            this.mObjectParams = mObjectParams;
        }

        public <Param> Param getObject(Class<Param> p){
            if (mObjectParams == null){
                return null;
            }
            return p.cast(mObjectParams.get(mIndex++) + "");
        }

        /**
         * 获取Int值
         * @return
         */
        public int getInt(){
            if (mParams != null){
                return mParams.getInt((mIndex++) + "");
            }
            return 0;
        }

        /**
         * 获取Int值
         * @param defalut
         * @return
         */
        public int getInt(int defalut){
            if (mParams != null){
                return mParams.getInt((mIndex++) + "");
            }
            return defalut;
        }
        /**
         * 获取字符串
         * @param defalut
         * @return
         */
        public String getString(String defalut){
            if(mParams != null){
                return mParams.getString((mIndex++) + "");
            }
            return defalut;
        }

        /**
         * 获取字符串
         * @return
         */
        public String getString(){
            if(mParams != null){
                return mParams.getString((mIndex++) + "");
            }
            return null;
        }


        /**
         * 获取Boolean值
         * @return 默认返回false
         */
        public boolean getBoolean(){
            if(mParams != null){
                return mParams.getBoolean((mIndex++) + "");
            }
            return false;
        }

        /**
         * 该类用来创建函数参数
         */
        public static class FunctionParamsBuilder{
            private Bundle mParams ;
            private int mIndex = -1;
            private Map mObjectParams = new HashMap(1);

            public FunctionParamsBuilder(){

            }

            public FunctionParamsBuilder putInt(int value){
                if(mParams == null){
                    mParams = new Bundle(2);
                }
                mParams.putInt((mIndex++) + "", value);
                return this;
            }

            public FunctionParamsBuilder putString(String value){
                if(mParams == null){
                    mParams = new Bundle(2);
                }
                mParams.putString((mIndex++) + "", value);
                return this;
            }

            public FunctionParamsBuilder putBoolean(boolean value){
                if(mParams == null){
                    mParams = new Bundle(2);
                }
                mParams.putBoolean((mIndex++) + "", value);
                return this;
            }

            public  FunctionParamsBuilder putObject(Object value){

                if(mObjectParams == null){
                    mObjectParams = new HashMap(1);
                }
                mObjectParams.put((mIndex++) + "", value);
                return this;
            }

            public FunctionParams create(){
                FunctionParams instance = new FunctionParams(mParams,mObjectParams);
                return instance;
            }
        }
    }
}
