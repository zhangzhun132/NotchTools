package notchtools.geek.com.notchtools.helper;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ThreadUtils {

    private static Handler sMainHandler;
    private static final Object sLock = new Object();

    private static Handler getUIThreadHandler() {
        synchronized (sLock) {
            if (sMainHandler == null) {
                sMainHandler = new Handler(Looper.getMainLooper());
            }
            return sMainHandler;
        }
    }

    public static boolean isMainThread() {
        return Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId();
    }

    public static boolean post2UI(Runnable r) {
        return getUIThreadHandler().post(r);
    }

    public static boolean post2UI(Context context, Runnable r) {
        if (context == null) {
            return post2UI(r);
        }
        return getUIThreadHandler().postAtTime(r, context.hashCode(), SystemClock.uptimeMillis());
    }

    public static boolean postDelayed2UI(Runnable r, long delayMillis) {
        return getUIThreadHandler().postDelayed(r, delayMillis);
    }

    public static boolean postDelayed2UI(Context context, Runnable r, long delayMillis) {
        if (context == null) {
            return postDelayed2UI(r, delayMillis);
        }
        return getUIThreadHandler().postAtTime(r, context.hashCode(), SystemClock.uptimeMillis() + delayMillis);
    }

    public static void cancelUITask(Runnable r) {
        getUIThreadHandler().removeCallbacks(r);
    }

    //post过程中，如果有传入context 参数
    public static void cancelUITask(Context context) {
        if (context != null) {
            getUIThreadHandler().removeCallbacksAndMessages(context.hashCode());
        }
    }


    private static boolean runningOnUI() {
        return getUIThreadHandler().getLooper() == Looper.myLooper();
    }

    /**
     * logicThreadUtils
     * 这个是个单线程的队列，如果你的操作很耗时，建议不要放到这里面，避免影响其他业务
     * 另外放到这个线程里面的任务是对时间没有要求的
     */
    private static Handler sLogicThreadHandler;
    private static final Object sLogicThreadHandlerLock = new Object();

    private static Handler getLogicThreadHandler() {
        synchronized (sLogicThreadHandlerLock) {
            if (sLogicThreadHandler == null) {
                HandlerThread t = new HandlerThread("daemon-handler-thread");
                t.start();
                sLogicThreadHandler = new Handler(t.getLooper());
            }
            return sLogicThreadHandler;
        }
    }

    public static boolean post2Logic(Runnable r) {
        return getLogicThreadHandler().post(r);
    }

    public static boolean post2Logic(Context context, Runnable r) {
        if (context == null) {
            return post2Logic(r);
        }
        return getLogicThreadHandler().postAtTime(r, context.hashCode(), SystemClock.uptimeMillis());
    }

    private static boolean postDelayed2Logic(Runnable r, long delayMillis) {
        return getLogicThreadHandler().postDelayed(r, delayMillis);
    }

    public static boolean postDelayed2Logic(Context context, Runnable r, long delayMillis) {
        if (context == null) {
            return postDelayed2Logic(r, delayMillis);
        }
        return getLogicThreadHandler().postAtTime(r, context.hashCode(), SystemClock.uptimeMillis() + delayMillis);
    }

    public static void cancelLogicTask(Runnable r) {
        getLogicThreadHandler().removeCallbacks(r);
    }

    public static void cancelLogicTask(Context context) {
        if (context != null) {
            getLogicThreadHandler().removeCallbacksAndMessages(context.hashCode());
        }
    }

    public static void delayCancelLogicTask(Context context, long delayMillis) {
        if (context != null) {
            final Object token = context.hashCode();
            postDelayed2Logic(new Runnable() {
                @Override
                public void run() {
                    getLogicThreadHandler().removeCallbacksAndMessages(token);
                }
            }, delayMillis);
        }
    }


    public static boolean runningOnLogic() {
        return getLogicThreadHandler().getLooper() == Looper.myLooper();
    }

    /**
     * safeCheck
     */
    public static void safeCheckUIThread(String log) {
        if (!runningOnUI()) {
            throw new RuntimeException("ThreadUtils safeCheck alert " + log);
        }
    }

    public static String getThreadName() {
        return Thread.currentThread().getName() + " : " + Thread.currentThread().getId();
    }

    private static final Executor EXECUTOR = Executors.newFixedThreadPool(4, new ThreadFactory() {
        private int count = -1;

        @Override
        public Thread newThread(@NonNull Runnable r) {
            ++count;
            return new Thread(r, "ThreadUtils: " + count);
        }
    });

    public static void exec(Runnable runnable) {
        if (runnable != null) {
            try {
                EXECUTOR.execute(runnable);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    public static <T> void exec(final Provider<T> provider, final Consumer<T> consumer) {
        if (provider == null) {
            return;
        }
        EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                T tmp = null;
                try {
                    tmp = provider.provide();
                } catch (Throwable t) {
                    t.printStackTrace();
                }

                if (consumer == null) {
                    return;
                }

                final T result = tmp;
                post2UI(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            consumer.consume(result);
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public interface Provider<T> {
        @Nullable
        T provide();
    }

    public interface Consumer<T> {
        void consume(@Nullable T t);
    }
}
