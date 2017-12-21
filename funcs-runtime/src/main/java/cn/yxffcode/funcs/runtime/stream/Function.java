package cn.yxffcode.funcs.runtime.stream;

/**
 * @author gaohang on 12/10/17.
 */
public interface Function<T> {
  Object apply(T input);
}

