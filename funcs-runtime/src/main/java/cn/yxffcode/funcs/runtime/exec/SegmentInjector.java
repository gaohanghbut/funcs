package cn.yxffcode.funcs.runtime.exec;

/**
 * 代码片断对象的依赖注入器
 *
 * @author gaohang on 8/26/17.
 */
public interface SegmentInjector {
  Object inject(final Object obj);
}
