package cn.yxffcode.funcs.runtime.exec;

import cn.yxffcode.funcs.runtime.ScriptEvalUtils;
import org.junit.Test;

import java.util.Collections;

/**
 * @author gaohang on 12/21/17.
 */
public class ScriptEvalUtilsTest {

  @Test
  public void test() {
    System.out.println((Object) ScriptEvalUtils.evalJava("return true;", Collections.emptyMap()));
  }
}
