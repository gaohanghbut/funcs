package cn.yxffcode.funcs.runtime.exec.groovy;

import cn.yxffcode.funcs.runtime.exec.JavaSource;
import com.google.common.base.MoreObjects;

/**
 * @author gaohang on 12/3/17.
 */
public class GroovySource implements JavaSource {

  private final String className;
  private final String packageName;
  private final String classContent;
  private final String fullClassName;

  public GroovySource(String className, String packageName, String classContent) {
    this.className = className;
    this.packageName = packageName;
    this.classContent = classContent;
    this.fullClassName = packageName + '.' + className;
  }

  @Override
  public String getClassName() {
    return className;
  }

  @Override
  public String getPackageName() {
    return packageName;
  }

  @Override
  public String getClassContent() {
    return classContent;
  }

  @Override
  public String getFullClassName() {
    return fullClassName;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("className", className)
        .add("packageName", packageName)
        .add("classContent", classContent)
        .add("fullClassName", fullClassName)
        .toString();
  }
}
