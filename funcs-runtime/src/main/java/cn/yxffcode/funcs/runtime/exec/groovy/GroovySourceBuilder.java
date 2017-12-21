package cn.yxffcode.funcs.runtime.exec.groovy;

import cn.yxffcode.funcs.runtime.exec.JavaSource;
import cn.yxffcode.funcs.runtime.exec.JavaSourceBuilder;

import java.io.Writer;
import java.util.Map;

/**
 * @author gaohang on 8/26/17.
 */
public class GroovySourceBuilder extends JavaSourceBuilder {

  @Override
  protected JavaSource createJavaSource(Map<String, Object> ctx, Writer out) {
    return new GroovySource(String.valueOf(ctx.get("className")),
        String.valueOf(ctx.get("packageName")), out.toString());
  }
}
