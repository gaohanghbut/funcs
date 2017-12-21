package cn.yxffcode.funcs.runtime.exec;

/**
 * @author gaohang on 12/3/17.
 */
public interface JavaSource {
  String getClassName();

  String getPackageName();

  String getClassContent();

  String getFullClassName();
}
