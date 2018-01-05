package com.changcai.buyer.rx;

import android.support.annotation.NonNull;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * Created by liuxingwei on 2017/2/15.
 * 事件总线
 */

public class RxBus {

    public static synchronized RxBus get() {
        return RxBusGenerator.instance;
    }

    private static class RxBusGenerator {
        private static RxBus instance = new RxBus();
    }

    private RxBus() {
    }

    private ConcurrentHashMap<Object, Vector<Subject>> subjectMapper = new ConcurrentHashMap<>();

    public <T> Observable<T> register(@NonNull Class<T> clazz) {
        return register(clazz.getName(), clazz);
    }

    public <T> Observable<T> register(@NonNull Object tag, @NonNull Class<T> clazz) {
        Vector<Subject> subjects = subjectMapper.get(tag);
        if (null == subjects) {
            subjects = new Vector<>();
            subjectMapper.put(tag, subjects);
        }
        Subject<T, T> subject;
        subjects.add(subject = PublishSubject.create());
        return subject;
    }

    public void unregister(@NonNull Object tag, @NonNull Observable observable) {
        Vector<Subject> subjects = subjectMapper.get(tag);
        if (null != subjects) {
            subjects.remove((Subject) observable);
            if (subjects.isEmpty()) {
                subjectMapper.remove(tag);
            }
        }
    }

    public void post(@NonNull String content) {
        post(content, content);
    }

    public void post(@NonNull Object content) {
        post(content.getClass().getName(), content);
    }

    public void post(@NonNull Object tag, @NonNull Object content) {
        Vector<Subject> subjectList = subjectMapper.get(tag);
        if (!(null == subjectList || subjectList.isEmpty())) {
            for (Subject subject : subjectList) {
                subject.onNext(content);
            }
        }

    }
}
