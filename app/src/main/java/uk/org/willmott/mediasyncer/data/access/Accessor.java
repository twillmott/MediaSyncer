package uk.org.willmott.mediasyncer.data.access;

/**
 * Created by tomwi on 11/10/2016.
 */

public interface Accessor<T, U> {

    public T getDaoForModel(U model);

    public U getModelForDao(T dao);
}
