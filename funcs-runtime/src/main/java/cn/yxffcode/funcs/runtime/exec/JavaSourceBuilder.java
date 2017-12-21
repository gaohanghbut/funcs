package cn.yxffcode.funcs.runtime.exec;

import cn.yxffcode.funcs.runtime.exec.code.CodeSegment;
import cn.yxffcode.funcs.runtime.exec.io.StrWriter;
import cn.yxffcode.funcs.runtime.exec.javac.ReadClassTemplateException;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author gaohang on 12/3/17.
 */
public abstract class JavaSourceBuilder {
  private static final String DEFAULT_PACKAGE = "javasegment.gen";
  private static final AtomicInteger CLASS_NAME_NUM = new AtomicInteger();
  private static final String CLASS_NAME_PREFIX = "JavasegmentGen_";
  private static final Splitter STATEMENT_SPLITTER = Splitter.on(';').trimResults();
  private static final Joiner STATEMENT_JOINER = Joiner.on(";\n");
  private static final VelocityEngine VE = new VelocityEngine();
  public static final String CLASS_NAME_PARAM = "className";
  public static final String PACKAGE_NAME_PARAM = "packageName";

  public JavaSource build(CodeSegment codeSegment, Map<String, Object> context) {
    checkNotNull(codeSegment);

    final Iterable<String> statements = STATEMENT_SPLITTER.split(codeSegment.code());
    final List<String> imports = Lists.newArrayList();
    final List<String> states = Lists.newArrayList();

    final Map<String, Object> ctx = Maps.newHashMap(context);

    boolean hasPackage = false;
    for (String statement : statements) {
      if (statement.startsWith("import")) {
        imports.add(statement);
      } else {
        states.add(statement);
        if (statement.startsWith("package ")) {
          final int idx = statement.indexOf("package ");
          hasPackage = true;
          final String packageName = statement.substring(idx + "package ".length()).trim();
          if (packageName.charAt(0) != '$') {
            ctx.put(PACKAGE_NAME_PARAM, packageName);
          }
        } else if (statement.startsWith("public class ")) {
          //class name
          final int start = "public class ".length();
          final int end = statement.indexOf(' ', start);
          ctx.put(CLASS_NAME_PARAM, statement.substring(start, end));
        }
      }
    }

    if (!ctx.containsKey(CLASS_NAME_PARAM)) {
      final String className = CLASS_NAME_PREFIX + CLASS_NAME_NUM.getAndIncrement();
      ctx.put(CLASS_NAME_PARAM, className);
    }
    if (hasPackage && !ctx.containsKey(PACKAGE_NAME_PARAM)) {
      ctx.put(PACKAGE_NAME_PARAM, DEFAULT_PACKAGE);
    }

    ctx.put("imports", STATEMENT_JOINER.join(imports));
    if (!states.isEmpty()) {
      ctx.put("expression", STATEMENT_JOINER.join(states));
    }

    final VelocityContext vcontext = new VelocityContext(ctx);

    try (Writer out = new StrWriter()) {
      VE.evaluate(vcontext, out, "javasegmentGen",
          codeSegment.isNeedRend()
              ? codeSegment.classTemplate()
              : codeSegment.code());
      return createJavaSource(ctx, out);
    } catch (IOException e) {
      throw new ReadClassTemplateException(e);
    }
  }

  protected abstract JavaSource createJavaSource(Map<String, Object> ctx, Writer out);

}
