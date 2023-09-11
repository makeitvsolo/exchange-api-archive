package com.makeitvsolo.exchangeapi.core.mapping;

public interface AnyObject<R, M extends Mapper> {

    R map(M mapper);
}
