package cn.yxffcode.funcs.runtime.exec;

import org.junit.Test;

import static cn.yxffcode.funcs.runtime.ScriptEvalUtils.evalJava;

/**
 * @author gaohang on 12/21/17.
 */
public class ScriptEvalUtilsTest {

  @Test
  public void test() {
    final boolean result = evalJava("return true;");
    System.out.println(result);
  }
}
