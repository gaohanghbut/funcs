package cn.yxffcode.funcs.runtime.exec.groovy;

import cn.yxffcode.funcs.runtime.exec.Executable;
import cn.yxffcode.funcs.runtime.exec.SegmentEvaluator;
import cn.yxffcode.funcs.runtime.exec.code.CodeSegment;
import com.google.common.collect.Maps;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

/**
 * @author gaohang on 12/3/17.
 */
public class GroovySegmentEvaluatorTest {
  @Test
  public void eval() throws Exception {
    final SegmentEvaluator evaluator = new GroovySegmentEvaluator();

    for (int i = 0; i < 10; i++) {
      final CodeSegment seg =
          CodeSegment.fromClassContent(
              "package $packageName;\n" +
                  "import cn.yxffcode.funcs.runtime.exec.Executable;\n" +
                  "import java.util.*;\n" +
                  "public class $className implements Executable {\n" +
                  "    def execute(Map<String, Object> context) {\n" +
                  "        def i = context.get(\"i\");\n" +
                  "        return i * i;\n" +
                  "    }\n" +
                  "}");

      final Map<String, Object> context = Maps.newHashMap();
      context.put("i", 10);
      final Object value = evaluator.eval(seg, context);
      System.out.println("value = " + value);
    }
  }

  @Test
  public void testCompile() throws IllegalAccessException, InstantiationException {
    final GroovyClassLoader cl = new GroovyClassLoader();
    final GroovyCodeSource gcs1 = new GroovyCodeSource("import cn.yxffcode.funcs.runtime.exec.Executable;\n" +
        "import java.util.*;\n" +
        "public class TestGroovy implements Executable {\n" +
        "    def execute(Map<String, Object> context) {\n" +
        "        def i = 2;\n" +
        "        return i * i;\n" +
        "    }\n" +
        "}", "TestGroovy.groovy", "/groovy/script");
    gcs1.setCachable(false);
    final Class class1 = cl.parseClass(gcs1);
    final Executable e1 = (Executable) class1.newInstance();
    final Object r1 = e1.execute(Collections.emptyMap());
    System.out.println(r1);

    final GroovyCodeSource gcs2 = new GroovyCodeSource("import cn.yxffcode.funcs.runtime.exec.Executable;\n" +
        "import java.util.*;\n" +
        "public class TestGroovy implements Executable {\n" +
        "    def execute(Map<String, Object> context) {\n" +
        "        def i = 3;\n" +
        "        return i * i;\n" +
        "    }\n" +
        "}", "TestGroovy.groovy", "/groovy/script");
    gcs1.setCachable(false);
    final Class class2 = cl.parseClass(gcs2);
    final Executable e2 = (Executable) class2.newInstance();
    final Object r2 = e2.execute(Collections.emptyMap());
    System.out.println(r2);
  }
}