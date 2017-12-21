package cn.yxffcode.funcs.runtime.exec.javac;

import cn.yxffcode.funcs.runtime.exec.BaseSegmentEvaluator;
import cn.yxffcode.funcs.runtime.exec.JavaSource;
import cn.yxffcode.funcs.runtime.exec.JavaSourceBuilder;
import cn.yxffcode.funcs.runtime.exec.code.CodeSegment;
import cn.yxffcode.funcs.runtime.exec.intercept.SegmentInterceptor;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author gaohang on 8/26/17.
 */
public class JavacSegmentEvaluator extends BaseSegmentEvaluator {
  private final JdkCompiler compiler = new JdkCompiler(Collections.emptyList());
  private final JavaSourceBuilder javaSourceBuilder = new JavacSourceBuilder();

  public JavacSegmentEvaluator() {
    this(Collections.emptyList());
  }

  public JavacSegmentEvaluator(List<SegmentInterceptor> segmentInterceptors) {
    super(segmentInterceptors);
  }

  @Override
  protected JavaSource createJavaSource(CodeSegment codeSegment, Map<String, Object> context) {
    return javaSourceBuilder.build(codeSegment, context);
  }

  @Override
  protected Class<?> doCompile(JavaSource javaSource) {
    try {
      return compiler.compile((JavacSource) javaSource, true);
    } catch (CompileException e) {
      throw e;
    } catch (Throwable throwable) {
      throw new CompileException(throwable);
    }
  }
}
