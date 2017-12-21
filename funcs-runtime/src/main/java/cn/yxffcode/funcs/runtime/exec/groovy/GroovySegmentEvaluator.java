package cn.yxffcode.funcs.runtime.exec.groovy;

import cn.yxffcode.funcs.runtime.exec.BaseSegmentEvaluator;
import cn.yxffcode.funcs.runtime.exec.JavaSource;
import cn.yxffcode.funcs.runtime.exec.code.CodeSegment;
import cn.yxffcode.funcs.runtime.exec.intercept.SegmentInterceptor;
import com.google.common.collect.Maps;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * @author gaohang on 12/3/17.
 */
public class GroovySegmentEvaluator extends BaseSegmentEvaluator {

  private GroovySourceBuilder groovySourceBuilder = new GroovySourceBuilder();

  private CachedGroovyClassLoader groovyClassLoader = new CachedGroovyClassLoader(GroovySegmentEvaluator.class.getClassLoader());

  public GroovySegmentEvaluator() {
    this(Collections.emptyList());
  }

  public GroovySegmentEvaluator(List<SegmentInterceptor> globalInterceptors) {
    super(globalInterceptors);
  }

  @Override
  protected JavaSource createJavaSource(CodeSegment codeSegment, Map<String, Object> context) {
    return groovySourceBuilder.build(codeSegment, context);
  }

  @Override
  protected Class<?> doCompile(JavaSource javaSource) {
    GroovyCodeSource gcs = AccessController.doPrivileged(new PrivilegedAction<GroovyCodeSource>() {
      public GroovyCodeSource run() {
        final String fileName = "script" + System.currentTimeMillis() +
            Math.abs(javaSource.getClassContent().hashCode()) + ".groovy";
        return new GroovyCodeSource(javaSource.getClassContent(), fileName, "/groovy/script");
      }
    });
    gcs.setCachable(true);
    return groovyClassLoader.parseClass(gcs);
  }

  private static final class CachedGroovyClassLoader extends GroovyClassLoader {

    private ConcurrentMap<String, Class<?>> classCache = Maps.newConcurrentMap();

    public CachedGroovyClassLoader(ClassLoader loader) {
      super(loader);
    }

    private Class<?> parse(JavaSource javaSource) {
      final Class<?> clazz = classCache.get(javaSource.getFullClassName());
      if (clazz != null) {
        return clazz;
      }
      synchronized (this) {
        final Class<?> parsedClass = super.parseClass(javaSource.getClassContent());
        return classCache.put(javaSource.getFullClassName(), parsedClass);
      }
    }
  }
}
