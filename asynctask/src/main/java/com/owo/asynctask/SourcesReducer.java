package com.owo.asynctask;

import java.util.HashMap;
import java.util.Map;

public class SourcesReducer<Param> {
    public static interface Source<Param> {
        void run(Param param);

        Object getTag();

        Param getParam();
    }

    private Map<Object, Source<Param>> mSources = new HashMap<>();
    private Callback<Map<Object, Source<Param>>> mCallback;

    public SourcesReducer<Param> callback(
            Callback<Map<Object, Source<Param>>> callback) {
        mCallback = callback;
        return this;
    }

    private class SourceImple implements Source<Param> {
        private final Object mTag;
        private Param mParam;

        public SourceImple(Object tag) {
            mTag = tag;
        }

        @Override
        public void run(Param param) {
            mParam = param;
            mCallback.run(mSources);
        }

        @Override
        public Object getTag() {
            return mTag;
        }

        @Override
        public Param getParam() {
            return mParam;
        }
    }

    public Source<Param> makeSource(Object tag) {
        Source<Param> source = new SourceImple(tag);
        mSources.put(tag, source);
        return source;
    }
    
//    public class Main {
//
//        public static void main(String[] args) {
//            SourcesReducer<String> reducer = new SourcesReducer<>();
//            Source<String> s1 = reducer.makeSource("s1");
//            Source<String> s2 = reducer.makeSource("s2");
//            Source<String> s3 = reducer.makeSource("s3");
//            reducer.callback(new Callback<Map<Object, Source<String>>>() {
//                @Override
//                public void run(Map<Object, Source<String>> t) {
//                    if ("abc".equals(t.get("s1").getParam())
//                            && "3f".equals(t.get("s2").getParam())
//                            && "4c".equals(t.get("s3").getParam())) {
//                        // condition match
//                        System.out.println("match");
//                    }
//                }
//            });
//            s1.run("abc");
//            s2.run("3f");
//            s3.run("4c");
//        }
//    }
}
