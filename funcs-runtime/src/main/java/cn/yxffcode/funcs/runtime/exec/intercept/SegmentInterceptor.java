package cn.yxffcode.funcs.runtime.exec.intercept;

import cn.yxffcode.funcs.runtime.exec.Executable;
import cn.yxffcode.funcs.runtime.exec.code.CodeSegment;
import cn.yxffcode.funcs.runtime.exec.JavaSource;

/**
 * @author gaohang on 8/26/17.
 */
public interface SegmentInterceptor {
  /**
   * 转换成JavaSource前调用
   */
  CodeSegment beforeJavaSource(final CodeSegment segment);

  /**
   * 转换成JavaSource后调用
   */
  JavaSource postJavaSource(final JavaSource javaSource);

  /**
   * 处理由CodeSegment创建的对象
   */
  Executable postExecutable(final Executable obj);

  /**
   * 处理代码片断的执行结果
   */
  Object postEvalResult(final Object result);
}
