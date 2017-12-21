package cn.yxffcode.funcs.runtime.exec;

import cn.yxffcode.funcs.runtime.exec.code.CodeSegment;
import cn.yxffcode.funcs.runtime.exec.intercept.SegmentInterceptor;
import cn.yxffcode.funcs.runtime.exec.javac.EvaluateException;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.AbstractList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author gaohang on 12/3/17.
 */
public abstract class BaseSegmentEvaluator implements SegmentEvaluator {
  private static final Logger LOGGER = LoggerFactory.getLogger(BaseSegmentEvaluator.class);
  protected final List<SegmentInterceptor> segmentInterceptors;
  private final ConcurrentMap<CodeSegment, Executable> codeCache = Maps.newConcurrentMap();

  public BaseSegmentEvaluator(List<SegmentInterceptor> globalInterceptors) {
    this.segmentInterceptors = globalInterceptors == null ? Collections.emptyList()
        : globalInterceptors;
  }

  @Override
  public Object eval(CodeSegment codeSegment, SegmentInterceptor interceptor, Map<String, Object> context) {
    return eval(codeSegment, false, interceptor, context);
  }

  @Override
  public Object eval(CodeSegment codeSegment, boolean recompile, SegmentInterceptor interceptor, Map<String, Object> context) {
    checkNotNull(codeSegment);

    final Executable executable = recompile ? null : codeCache.get(codeSegment);

    final List<SegmentInterceptor> segmentInterceptors = CollectionUtils.isNotEmpty(this.segmentInterceptors) || interceptor != null ?
        new SegmentInterceptorList(interceptor) : Collections.emptyList();

    if (executable != null) {
      LOGGER.debug("invoke executable in cache\n:{}", codeSegment.code());
      final Object result = executable.execute(context);
      return invokeGlobalPostEvalResult(result, segmentInterceptors);

    }

    synchronized (this) {
      if (recompile || !codeCache.containsKey(codeSegment)) {
        invokeGlobalBeforeSource(codeSegment, segmentInterceptors);

        final JavaSource javaSource = createJavaSource(codeSegment, context);
        invokeGlobalPostSource(javaSource, segmentInterceptors);

        LOGGER.debug("to compile source\n:{}", javaSource.getClassContent());
        final Class<?> clazz = doCompile(javaSource);

        final Executable codeSegObject = newExecutable(clazz);
        codeCache.put(codeSegment, invokeGlobalPostExecutable(codeSegObject, segmentInterceptors));
      }
    }
    //执行代码
    LOGGER.debug("invoke executable after compiled\n:{}", codeSegment.code());
    Object result = codeCache.get(codeSegment).execute(context);
    return invokeGlobalPostEvalResult(result, segmentInterceptors);
  }

  @Override
  public Object eval(CodeSegment codeSegment, boolean recompile, Map<String, Object> context) {
    return eval(codeSegment, recompile, null, context);
  }

  @Override
  public Object eval(CodeSegment codeSegment, Map<String, Object> context) {
    return eval(codeSegment, false, context);
  }

  protected abstract JavaSource createJavaSource(CodeSegment codeSegment, Map<String, Object> context);

  private Executable newExecutable(Class<?> type) {
    Executable codeSegObject;
    try {
      codeSegObject = (Executable) type.newInstance();
    } catch (Exception e) {
      throw new EvaluateException(e);
    }
    return codeSegObject;
  }

  private Object invokeGlobalPostEvalResult(Object result, List<SegmentInterceptor> segmentInterceptors) {
    if (!segmentInterceptors.isEmpty()) {
      for (SegmentInterceptor segmentInterceptor : segmentInterceptors) {
        result = segmentInterceptor.postEvalResult(result);
      }
    }
    return result;
  }

  private Executable invokeGlobalPostExecutable(Executable codeSegObject, List<SegmentInterceptor> segmentInterceptors) {
    if (!segmentInterceptors.isEmpty()) {
      for (SegmentInterceptor segmentInterceptor : segmentInterceptors) {
        codeSegObject = segmentInterceptor.postExecutable(codeSegObject);
      }
    }
    return codeSegObject;
  }

  /**
   * 执行编译
   */
  protected abstract Class<?> doCompile(JavaSource javaSource);

  private void invokeGlobalPostSource(JavaSource javaSource, List<SegmentInterceptor> segmentInterceptors) {
    JavaSource js = javaSource;
    if (!segmentInterceptors.isEmpty()) {
      for (SegmentInterceptor segmentInterceptor : segmentInterceptors) {
        js = segmentInterceptor.postJavaSource(js);
      }
    }
  }

  private void invokeGlobalBeforeSource(CodeSegment codeSegment, List<SegmentInterceptor> segmentInterceptors) {
    CodeSegment seg = codeSegment;
    if (!segmentInterceptors.isEmpty()) {
      for (SegmentInterceptor segmentInterceptor : segmentInterceptors) {
        seg = segmentInterceptor.beforeJavaSource(seg);
      }
    }
  }

  private class SegmentInterceptorList extends AbstractList<SegmentInterceptor> {
    private final SegmentInterceptor interceptor;

    public SegmentInterceptorList(SegmentInterceptor interceptor) {
      this.interceptor = interceptor;
    }

    @Override
    public SegmentInterceptor get(int index) {
      if (index == 0) {
        return interceptor;
      }
      return BaseSegmentEvaluator.this.segmentInterceptors.get(index - 1);
    }

    @Override
    public int size() {
      return BaseSegmentEvaluator.this.segmentInterceptors.size() + 1;
    }
  }
}
