package com.happy.gene.pdfreport.pdf;

/**
 * Created by zhaolisong on 20/04/2017.
 */
public interface IPosition<T> {
    T translate(float deltaX, float deltaY);
}
