package cn.yxffcode.funcs.runtime.exec;

import cn.yxffcode.funcs.runtime.exec.code.CodeSegment;
import cn.yxffcode.funcs.runtime.exec.intercept.SegmentInterceptor;

import java.util.Map;

/**
 * 代码片断求值
 *
 * @author gaohang on 8/26/17.
 */
public interface SegmentEvaluator {

  /**
   * 执行CodeSegment
   *
   * @param context 代码片断中需要的外界变量
   */
  Object eval(CodeSegment codeSegment, Map<String, Object> context);

  /**
   * 执行CodeSegment
   *
   * @param context 代码片断中需要的外界变量
   */
  Object eval(CodeSegment codeSegment, boolean recompile, Map<String, Object> context);

  /**
   *
   * @param codeSegment
   * @param interceptor
   * @param context
   * @return
   */
  Object eval(CodeSegment codeSegment, SegmentInterceptor interceptor, Map<String, Object> context);

  /**
   *
   * @param codeSegment
   * @param recompile
   * @param interceptor
   * @param context
   * @return
   */
  Object eval(CodeSegment codeSegment, boolean recompile, SegmentInterceptor interceptor, Map<String, Object> context);

}
