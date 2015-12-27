package com.owo.asynctask;

import java.util.ArrayList;
import java.util.List;

public class SourcesReducer<Param> {
    public static interface Source<Param> {
        void run(Param param);
    }

    public static interface SourceMatcher<Param> {
        boolean match(Param param);
    }

    private List<SourceImpl> mSources = new ArrayList<>();
    private Runnable mCallback;

    public SourcesReducer<Param> callback(Runnable callback) {
        mCallback = callback;
        return this;
    }

    private void ruduce() {
        for (SourceImpl source : mSources) {
            if (!source.isMatch()) {
                return;
            }
        }
        mCallback.run();
    }

    private class SourceImpl implements Source<Param> {
        private final SourceMatcher<Param> mMatcher;
        private boolean mIsMatch;

        public boolean isMatch() {
            return mIsMatch;
        }

        public SourceImpl(SourceMatcher<Param> matcher) {
            mMatcher = matcher;
        }

        @Override
        public void run(Param param) {
            mIsMatch = mMatcher.match(param);
            ruduce();
        }
    }

    public Source<Param> registerSource(SourceMatcher<Param> matcher) {
        SourceImpl source = new SourceImpl(matcher);
        mSources.add(source);
        return source;
    }

  //public class Main {
    //
//        public static void main(String[] args) {
//            SourcesReducer<String> reducer = new SourcesReducer<>();
//            Source<String> s1 = reducer.registerSource(new SourceMatcher<String>() {
//                @Override
//                public boolean match(String param) {
//                    return "abc".equals(param);
//                }
//            });
//            Source<String> s2 = reducer.registerSource(new SourceMatcher<String>() {
//                @Override
//                public boolean match(String param) {
//                    return param.startsWith("3");
//                }
//            });
//            Source<String> s3 = reducer.registerSource(new SourceMatcher<String>() {
//                @Override
//                public boolean match(String param) {
//                    return param.endsWith("b");
//                }
//            });
    //
//            reducer.callback(new Runnable() {
//                @Override
//                public void run() {
//                    // condition match
//                    System.out.println("match");
//                }
//            });
//            s1.run("abc");
//            s2.run("3f3");
//            s3.run("4b");
//        }
    //}
}
