package cn.yxffcode.funcs.runtime.exec.javac;

import cn.yxffcode.funcs.runtime.exec.JavaSource;
import com.google.common.collect.Maps;
import org.codehaus.groovy.control.CompilationFailedException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentMap;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author gaohang on 8/27/17.
 */
public class CodeSegmentClassLoader extends ClassLoader {

  private final ClassLoader parentClassLoader;
  private final ConcurrentMap<String, JavaSource> javaSources = Maps.newConcurrentMap();
  private final ConcurrentMap<String, Class<?>> loadedClasses = Maps.newConcurrentMap();

  public CodeSegmentClassLoader() {
    this(Thread.currentThread().getContextClassLoader());
  }

  public CodeSegmentClassLoader(ClassLoader parentClassLoader) {
    this.parentClassLoader = parentClassLoader;
  }

  @Override
  public Class<?> loadClass(String name) throws ClassNotFoundException {
    return loadClass(name, false);
  }

  public Class<?> loadClass0(String name, boolean reload) throws ClassNotFoundException {
    return loadClass(name, reload, false);
  }

  public void addToClasspath(JavaSource javaSource) {
    checkNotNull(javaSource);
    javaSources.put(javaSource.getFullClassName(), javaSource);
  }

  private void doLoad(String name, boolean reload, boolean resolve) throws ClassNotFoundException {
    final JavaSource javaSource = javaSources.get(name);
    if (javaSource == null) {
      throw new ClassNotFoundException(name);
    }
    if (reload || !loadedClasses.containsKey(name)) {
      synchronized (this) {
        if (reload || !loadedClasses.containsKey(name)) {
          loadedClasses.put(name, loadClass(javaSource, resolve));
        }
      }
    }
  }

  @Override
  protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {

    return loadClass(name, false, resolve);
  }

  protected Class<?> loadClass(String name, boolean reload, boolean resolve) throws ClassNotFoundException {

    if (!reload && loadedClasses.containsKey(name)) {
      return loadedClasses.get(name);
    }

    try {
      final Class<?> type = parentClassLoader.loadClass(name);
      if (type != null) {
        return type;
      }
    } catch (ClassNotFoundException e) {
      //ignore
    }

    doLoad(name, reload, resolve);
    return loadedClasses.get(name);
  }

  @Override
  protected Class<?> findClass(String name) throws ClassNotFoundException {
    return loadClass0(name, true);
  }

  private Class<?> loadClass(JavaSource javaSource, boolean resolve) throws ClassNotFoundException {
    final JavacSource javacSource = (JavacSource) javaSource;
    final String fullClassName = javacSource.getFullClassName();

    final byte[] byteCode = javacSource.getByteCode();

    final InnerLoader innerLoader = new InnerLoader(this);

    final Class<?> clazz = innerLoader.defineClass0(fullClassName, byteCode, 0, byteCode.length);
    if (resolve) {
      resolveClass(clazz);
    }
    return clazz;
  }

  public static class InnerLoader extends ClassLoader {
    private final CodeSegmentClassLoader delegate;

    public InnerLoader(CodeSegmentClassLoader delegate) {
      super(delegate);
      this.delegate = delegate;
    }

    public URL findResource(String name) {
      return delegate.findResource(name);
    }

    public Enumeration findResources(String name) throws IOException {
      return delegate.findResources(name);
    }

    public URL getResource(String name) {
      return delegate.getResource(name);
    }

    public InputStream getResourceAsStream(String name) {
      return delegate.getResourceAsStream(name);
    }

    @Override
    public Class loadClass(String name, boolean resolve) throws ClassNotFoundException, CompilationFailedException {
      Class c = findLoadedClass(name);
      if (c != null) return c;
      return delegate.loadClass(name, resolve);
    }

    private final Class<?> defineClass0(String name, byte[] b, int off, int len)
        throws ClassFormatError {
      return super.defineClass(name, b, off, len, null);
    }
  }
}
