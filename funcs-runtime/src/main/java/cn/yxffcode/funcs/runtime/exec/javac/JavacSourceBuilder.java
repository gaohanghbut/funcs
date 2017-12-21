package cn.yxffcode.funcs.runtime.exec.javac;

import cn.yxffcode.funcs.runtime.exec.JavaSource;
import cn.yxffcode.funcs.runtime.exec.JavaSourceBuilder;

import java.io.Writer;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author gaohang on 8/26/17.
 */
public class JavacSourceBuilder extends JavaSourceBuilder {

  @Override
  protected JavaSource createJavaSource(Map<String, Object> ctx, Writer out) {
    return new JavacSource(String.valueOf(ctx.get("className")),
        String.valueOf(ctx.get("packageName")), out.toString());
  }
}
