package cn.yxffcode.funcs.runtime.exec.javac;

/**
 * @author gaohang on 8/26/17.
 */
public class CompileException extends RuntimeException {
  public CompileException(Throwable cause) {
    super(cause);
  }

  public CompileException(String message) {
    super(message);
  }
}
