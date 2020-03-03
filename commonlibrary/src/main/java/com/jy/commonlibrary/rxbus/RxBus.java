package com.jy.commonlibrary.rxbus;

import com.jy.baselibrary.utils.YLogUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

@SuppressWarnings("unused")
public class RxBus {
    public static final String TAG = "RxBus_log";
    private static volatile RxBus defaultInstance;

    /**
     * key==》订阅者对象，value==》订阅事件（disposable ）
     */
    private Map<Object, List<Disposable>> subscriptionsByEventType = new HashMap<>();

    /**
     * key==》订阅者对象，value==》事件类型
     * <p>
     * value是 根据Key所注解的方法的参数类型确定，默认==》BusData，如：
     * <p>
     * 注解方法==》a()，则value==》BusData
     * 注解方法==》a(String str)，则value==》String
     */
    private Map<Object, List<Class>> eventTypesBySubscriber = new HashMap<>();

    /**
     * key==》事件类型，value==》订阅者信息
     * <p>
     * value信息包括：订阅者对象（subscriber）, 方法名（method）, 事件类型（eventType）, 事件code（code）, 线程类型（threadMode）
     */
    private Map<Class, List<SubscriberMethod>> subscriberMethodByEventType = new HashMap<>();

    private final Subject<Object> bus;

    private RxBus() {
        this.bus = PublishSubject.create().toSerialized();
    }

    public static RxBus getDefault() {
        RxBus rxBus = defaultInstance;
        if (defaultInstance == null) {
            synchronized (RxBus.class) {
                rxBus = defaultInstance;
                if (defaultInstance == null) {
                    rxBus = new RxBus();
                    defaultInstance = rxBus;
                }
            }
        }
        return rxBus;
    }

    /**
     * 注册
     *
     * @param subscriber 订阅者
     */
    public void register(Object subscriber) {
        YLogUtils.INSTANCE.iTag(TAG, "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@注册@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@--订阅者对象", subscriber);
        //根据对象获取类
        Class<?> subClass = subscriber.getClass();
        //获取所有方法名
        Method[] methods = subClass.getDeclaredMethods();
        for (Method method : methods) {
            //判断方法是否存在注解
            if (method.isAnnotationPresent(Subscribe.class)) {
                //获得参数类型
                Class[] parameterType = method.getParameterTypes();
                //参数不为空 且参数个数为1
                if (parameterType != null && parameterType.length == 1) {

                    Class eventType = parameterType[0];
                    executeRegistered(subscriber, method, eventType);

                } else if (parameterType == null || parameterType.length == 0) {

                    Class eventType = BusData.class;
                    executeRegistered(subscriber, method, eventType);

                }
            }
        }
        YLogUtils.INSTANCE.iTag(TAG, "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@注册完成@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@--订阅者对象", subscriber);
    }

    /**
     * 取消注册
     *
     * @param subscriber 订阅者
     */
    public void unregister(Object subscriber) {
        YLogUtils.INSTANCE.iTag(TAG, "###################################取消注册###################################--订阅者对象", subscriber);
        //通过订阅者对象，获取对应的事件类型集合
        List<Class> subscribedTypes = eventTypesBySubscriber.get(subscriber);
        if (subscribedTypes != null) {
            for (Class<?> eventType : subscribedTypes) {
                unSubscribeByEventType(subscriber);
                unSubscribeMethodByEventType(subscriber, eventType);
                YLogUtils.INSTANCE.iTag(TAG, "执行取消注册--订阅者对象", subscriber, "事件类型", eventType);
            }
            eventTypesBySubscriber.remove(subscriber);
        }
        YLogUtils.INSTANCE.iTag(TAG, "###################################取消注册完成###############################--订阅者对象", subscriber);
    }

    /**
     * 是否注册
     *
     * @param subscriber 订阅者
     * @return
     */
    public synchronized boolean isRegistered(Object subscriber) {
        boolean isRegistered = eventTypesBySubscriber.containsKey(subscriber);
        YLogUtils.INSTANCE.iTag(TAG, "************************************判断是否注册******************************--订阅者对象", subscriber, "是？", isRegistered);
        return isRegistered;
    }

    /**
     * 执行注册
     *
     * @param subscriber 订阅者对象
     * @param method     方法名
     * @param eventType  事件类型
     */
    private void executeRegistered(Object subscriber, Method method, Class eventType) {
        //判断是否为基本类型
        boolean isPrimitive = eventType.isPrimitive();
        if (isPrimitive) {
            YLogUtils.INSTANCE.iTag(TAG, "*********基础类型自动装箱---开始*********装箱前类型", eventType);
            eventType = PrimitiveWrapper.wrapper(eventType);
            YLogUtils.INSTANCE.iTag(TAG, "*********基础类型自动装箱---完成*********装箱后类型", eventType);
        }
        // key==》订阅者对象，value==》事件类型，保存到map里
        addEventTypeToMap(subscriber, eventType);

        //获取注解
        Subscribe sub = method.getAnnotation(Subscribe.class);
        int code = sub.code();
        ThreadMode threadMode = sub.threadMode();

        //key==》事件类型，value==》订阅者信息，保存到map里
        SubscriberMethod subscriberMethod = new SubscriberMethod(subscriber, method, eventType, code, threadMode, isPrimitive);
        addSubscriberToMap(eventType, subscriberMethod);

        //添加到RxJava订阅
        addSubscriber(subscriberMethod);

        YLogUtils.INSTANCE.iTag(TAG, "执行注册---方法:", method, "---事件类型:", eventType);
    }

    /**
     * key==》订阅者对象，value==》事件类型，保存到map里
     *
     * @param subscriber 订阅者对象
     * @param eventType  事件类型
     */
    private void addEventTypeToMap(Object subscriber, Class eventType) {
        List<Class> eventTypes = eventTypesBySubscriber.get(subscriber);
        if (eventTypes == null) {
            eventTypes = new ArrayList<>();
            eventTypesBySubscriber.put(subscriber, eventTypes);
        }
        if (!eventTypes.contains(eventType)) {
            eventTypes.add(eventType);
        }
    }

    /**
     * key==》事件类型，value==》订阅者信息，保存到map里
     *
     * @param eventType        事件类型
     * @param subscriberMethod 订阅者信息
     */
    private void addSubscriberToMap(Class eventType, SubscriberMethod subscriberMethod) {
        List<SubscriberMethod> subscriberMethods = subscriberMethodByEventType.get(eventType);
        if (subscriberMethods == null) {
            subscriberMethods = new ArrayList<>();
            subscriberMethodByEventType.put(eventType, subscriberMethods);
        }
        if (!subscriberMethods.contains(subscriberMethod)) {
            subscriberMethods.add(subscriberMethod);
        }
    }


    /**
     * 用RxJava添加订阅者
     *
     * @param subscriberMethod 订阅者信息
     */
    @SuppressWarnings("unchecked")
    private void addSubscriber(final SubscriberMethod subscriberMethod) {
        Flowable flowable;
        if (subscriberMethod.code == -1) {
            flowable = toObservable(subscriberMethod.eventType);
        } else {
            flowable = toObservable(subscriberMethod.code, subscriberMethod.eventType);
        }
        Disposable subscription = postToObservable(flowable, subscriberMethod)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        callEvent(subscriberMethod, o);
                    }
                });
        //key==》订阅者对象，value==》订阅事件（disposable ）
        addSubscriptionToMap(subscriberMethod.subscriber, subscription);
    }

    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     *
     * @param eventType 事件类型
     * @return return
     */
    public <T> Flowable<T> toObservable(Class<T> eventType) {
        //ofType(class)  指定某个类型的class，过滤属于这个类型的的结果，其它抛弃
        return bus.toFlowable(BackpressureStrategy.BUFFER).ofType(eventType);
    }

    /**
     * 根据传递的code和 eventType 类型返回特定类型(eventType)的 被观察者
     *
     * @param code      事件code
     * @param eventType 事件类型
     */
    private <T> Flowable<T> toObservable(final int code, final Class<T> eventType) {
        //ofType(class)  指定某个类型的class，过滤属于这个类型的的结果，其它抛弃
        return bus.toFlowable(BackpressureStrategy.BUFFER).ofType(Message.class)
                //采用filter（）变换操作符
                .filter(new Predicate<Message>() {
                    // 根据test()的返回值 对被观察者发送的事件进行过滤 & 筛选
                    // a. 返回true，则继续发送
                    // b. 返回false，则不发送（即过滤）
                    @Override
                    public boolean test(Message o) throws Exception {
                        return o.getCode() == code && eventType.isInstance(o.getObject());
                    }
                    // map操作符，Function<Object,Object>，只要类型为Object的子类就可以进行转换
                }).map(new Function<Message, Object>() {
                    @Override
                    public Object apply(Message o) throws Exception {
                        return o.getObject();
                    }
                }).cast(eventType);
    }

    /**
     * 用于处理订阅事件在那个线程中执行
     *
     * @param observable       订阅事件
     * @param subscriberMethod 订阅者信息
     * @return Observable
     */
    private Flowable postToObservable(Flowable observable, SubscriberMethod subscriberMethod) {
        Scheduler scheduler;
        switch (subscriberMethod.threadMode) {
            case MAIN:
                scheduler = AndroidSchedulers.mainThread();
                break;

            case NEW_THREAD:
                scheduler = Schedulers.newThread();
                break;

            case CURRENT_THREAD:
                scheduler = Schedulers.trampoline();
                break;
            default:
                throw new IllegalStateException("Unknown thread mode: " + subscriberMethod.threadMode);
        }
        return observable.observeOn(scheduler);
    }

    /**
     * key==》订阅者对象，value==》订阅事件（disposable ）
     *
     * @param subscriber 订阅者对象
     * @param disposable 订阅事件
     */
    private void addSubscriptionToMap(Object subscriber, Disposable disposable) {
        List<Disposable> disposables = subscriptionsByEventType.get(subscriber);
        if (disposables == null) {
            disposables = new ArrayList<>();
            subscriptionsByEventType.put(subscriber, disposables);
        }
        if (!disposables.contains(disposable)) {
            disposables.add(disposable);
        }
    }


    /**
     * 回调到订阅者的方法中
     *
     * @param subscriberMethod 订阅者信息
     * @param object           事件类型对象
     */
    private void callEvent(SubscriberMethod subscriberMethod, Object object) {
        YLogUtils.INSTANCE.iTag(TAG, "执行回调----订阅者对象", subscriberMethod.subscriber, "方法", subscriberMethod.method, "事件类型对象", object);
        //因为最终发送的为事件类型对象
        //所以需要通过事件类型对象，获取对应的事件类型
        Class eventClass = object.getClass();
        //通过事件类型，获取订阅者信息
        List<SubscriberMethod> subscriberMethodList = subscriberMethodByEventType.get(eventClass);
        //判断订阅者信息是否相同，并回调订阅者
        if (subscriberMethodList != null && subscriberMethodList.size() > 0) {
            for (SubscriberMethod tmpSubscriberMethod : subscriberMethodList) {
                if (tmpSubscriberMethod.code == subscriberMethod.code && subscriberMethod.subscriber.equals(tmpSubscriberMethod.subscriber)
                        && subscriberMethod.method.equals(tmpSubscriberMethod.method)) {
                    tmpSubscriberMethod.invoke(object);
                    YLogUtils.INSTANCE.iTag(TAG, "回调成功----订阅者对象", subscriberMethod.subscriber, "方法", subscriberMethod.method, "事件类型对象", object);
                }

            }
        }
    }

    /**
     * 移除订阅者对象对应的订阅事件
     *
     * @param subscriber 订阅者对象
     */
    private void unSubscribeByEventType(Object subscriber) {
        List<Disposable> disposables = subscriptionsByEventType.get(subscriber);
        if (disposables != null) {
            Iterator<Disposable> iterator = disposables.iterator();
            while (iterator.hasNext()) {
                Disposable disposable = iterator.next();
                if (disposable != null && !disposable.isDisposed()) {
                    disposable.dispose();
                    iterator.remove();
                }
            }
        }
    }

    /**
     * 移除订阅者对象对应的订阅者信息
     *
     * @param subscriber 订阅者对象
     * @param eventType  事件类型
     */
    private void unSubscribeMethodByEventType(Object subscriber, Class eventType) {
        //通过事件类型，获取订阅者信息
        List<SubscriberMethod> subscriberMethods = subscriberMethodByEventType.get(eventType);
        //判断订阅者信息的订阅者对象是否相同
        if (subscriberMethods != null) {
            Iterator<SubscriberMethod> iterator = subscriberMethods.iterator();
            while (iterator.hasNext()) {
                SubscriberMethod subscriberMethod = iterator.next();
                if (subscriberMethod.subscriber.equals(subscriber)) {
                    iterator.remove();
                }
            }
        }
    }

    public void send(int code, Object o) {
        Message message = new Message(code, o);
        bus.onNext(message);
        YLogUtils.INSTANCE.iTag(TAG, "发送RxBus", o);
    }

    public void post(Object o) {
        bus.onNext(o);
    }

    public void send(int code) {
        Message message = new Message(code, new BusData());
        bus.onNext(message);
        YLogUtils.INSTANCE.iTag(TAG, "发送RxBus", message.object);
    }

    private class Message {
        private int code;
        private Object object;

        public Message() {
        }

        private Message(int code, Object o) {
            this.code = code;
            this.object = o;
        }

        private int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        private Object getObject() {
            return object;
        }

        public void setObject(Object object) {
            this.object = object;
        }
    }
}