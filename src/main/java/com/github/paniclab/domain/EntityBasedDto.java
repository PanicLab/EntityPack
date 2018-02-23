package com.github.paniclab.domain;

import java.io.Serializable;

public interface EntityBasedDto<E extends Entity> extends Dto{

    @Override
    EntityBasedDto<E> getThis();
    Class<E> getEntityClass();
    <ID extends Serializable> ID getId();
    E toEntity();

}
