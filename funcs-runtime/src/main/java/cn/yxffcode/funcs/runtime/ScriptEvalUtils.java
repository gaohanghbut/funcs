package cn.yxffcode.funcs.runtime;

import cn.yxffcode.funcs.runtime.exec.SegmentEvaluator;
import cn.yxffcode.funcs.runtime.exec.code.CodeSegment;
import cn.yxffcode.funcs.runtime.exec.groovy.GroovySegmentEvaluator;
import cn.yxffcode.funcs.runtime.exec.javac.JavacSegmentEvaluator;

import java.util.Map;

/**
 * @author gaohang on 12/21/17.
 */
public final class ScriptEvalUtils {

  private ScriptEvalUtils() {
  }

  private static final String TEMPLATE = "package $packageName;\n" +
      "import cn.yxffcode.funcs.runtime.exec.Executable;\n" +
      "import java.util.*;\n" +
      "public class $className implements Executable {\n" +
      "    public Object execute(Map<String, Object> context) {\n" +
      "        $expression\n" +
      "    }\n" +
      "}";

  private static final SegmentEvaluator JAVAC_SEGMENT_EVALUATOR = new JavacSegmentEvaluator();
  private static final SegmentEvaluator GROOVY_SEGMENT_EVALUATOR = new GroovySegmentEvaluator();


  public static <T> T evalGroovy(String script, Map<String, Object> ctx) {
    return doEval(script, ctx, GROOVY_SEGMENT_EVALUATOR);
  }

  public static <T> T evalJava(String script, Map<String, Object> ctx) {
    return doEval(script, ctx, JAVAC_SEGMENT_EVALUATOR);
  }

  private static <T> T doEval(String script, Map<String, Object> ctx, SegmentEvaluator segmentEvaluator) {
    CodeSegment seg = CodeSegment.fromStringTemplate(script,
        TEMPLATE);

    return (T) segmentEvaluator.eval(seg, ctx);
  }
}
