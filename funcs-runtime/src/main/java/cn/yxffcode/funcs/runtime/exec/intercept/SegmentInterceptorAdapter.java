package cn.yxffcode.funcs.runtime.exec.intercept;

import cn.yxffcode.funcs.runtime.exec.Executable;
import cn.yxffcode.funcs.runtime.exec.code.CodeSegment;
import cn.yxffcode.funcs.runtime.exec.JavaSource;

/**
 * @author gaohang on 8/26/17.
 */
public class SegmentInterceptorAdapter implements SegmentInterceptor {
  @Override
  public CodeSegment beforeJavaSource(CodeSegment segment) {
    return segment;
  }

  @Override
  public JavaSource postJavaSource(JavaSource javaSource) {
    return javaSource;
  }

  @Override
  public Executable postExecutable(Executable obj) {
    return obj;
  }

  @Override
  public Object postEvalResult(Object result) {
    return result;
  }
}
