package cn.yxffcode.funcs.runtime.stream;

import cn.yxffcode.funcs.runtime.exec.SegmentEvaluator;
import cn.yxffcode.funcs.runtime.exec.code.CodeSegment;

/**
 * @author gaohang on 12/10/17.
 */
public class CodeSegmentFunction implements Function<Object> {

  private final SegmentEvaluator segmentEvaluator;
  private final CodeSegment codeSegment;

  public CodeSegmentFunction(SegmentEvaluator segmentEvaluator, CodeSegment codeSegment) {
    this.segmentEvaluator = segmentEvaluator;
    this.codeSegment = codeSegment;
  }

  @Override
  public Object apply(Object input) {
    return null;
  }
}
