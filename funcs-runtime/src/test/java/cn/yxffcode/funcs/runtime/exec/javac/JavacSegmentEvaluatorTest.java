package cn.yxffcode.funcs.runtime.exec.javac;

import cn.yxffcode.funcs.runtime.exec.SegmentEvaluator;
import cn.yxffcode.funcs.runtime.exec.code.CodeSegment;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.Map;

/**
 * @author gaohang on 12/3/17.
 */
public class JavacSegmentEvaluatorTest {
  @Test
  public void eval() throws Exception {
    final SegmentEvaluator evaluator = new JavacSegmentEvaluator();

    CodeSegment seg = CodeSegment.fromClassContent(
        "package $packageName;\n" +
            "import cn.yxffcode.funcs.runtime.exec.Executable;\n" +
            "import java.util.*;\n" +
            "public class JavaTest implements Executable {\n" +
            "    public Object execute(Map<String, Object> context) {\n" +
            "        Integer i = (Integer) context.get(\"i\");\n" +
            "        return i * i;\n" +
            "    }\n" +
            "}");

    final Map<String, Object> context = Maps.newHashMap();
    context.put("packageName", "com.test");
    context.put("className", "JavaTest");
    context.put("i", 10);
    final Object value = evaluator.eval(seg, context);
    System.out.println("value = " + value);

    seg = CodeSegment.fromClassContent(
        "package $packageName;\n" +
            "import cn.yxffcode.funcs.runtime.exec.Executable;\n" +
            "import java.util.*;\n" +
            "public class JavaTest implements Executable {\n" +
            "    public Object execute(Map<String, Object> context) {\n" +
            "        Integer i = 20;\n" +
            "        return i * i;\n" +
            "    }\n" +
            "}");
    final Object r1 = evaluator.eval(seg, context);
    System.out.println(r1);

    final Object r2 = evaluator.eval(seg, true, context);
    System.out.println(r2);
  }

  @Test
  public void eval2() throws Exception {
    final SegmentEvaluator evaluator = new JavacSegmentEvaluator();

    final CodeSegment seg =
        CodeSegment.fromClassContent(
            "package com.test;\n" +
                "import cn.yxffcode.funcs.runtime.exec.Executable;\n" +
                "import java.util.*;\n" +
                "public class TestClass implements Executable {\n" +
                "    public Object execute(Map<String, Object> context) {\n" +
                "        Integer i = (Integer) context.get(\"i\");\n" +
                "        return i * i;\n" +
                "    }\n" +
                "}");

    final Map<String, Object> context = Maps.newHashMap();
    context.put("i", 10);
    final Object value = evaluator.eval(seg, context);
    System.out.println("value = " + value);
  }

}