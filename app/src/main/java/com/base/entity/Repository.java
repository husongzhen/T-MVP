package com.base.entity;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by baixiaokang on 16/7/19.
 */
public interface Repository {
    Observable<DataArr> getData(HashMap<String, Object> param);
}
