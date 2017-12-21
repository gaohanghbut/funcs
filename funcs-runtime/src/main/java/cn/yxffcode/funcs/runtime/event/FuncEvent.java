package cn.yxffcode.funcs.runtime.event;

import java.util.Map;

/**
 * 事件
 *
 * @author gaohang on 12/3/17.
 */
public class FuncEvent {
  private final Map<String, String> eventProperties;
  private final Object payload;

  public FuncEvent(Map<String, String> eventProperties, Object payload) {
    this.eventProperties = eventProperties;
    this.payload = payload;
  }

  public Map<String, String> getEventProperties() {
    return eventProperties;
  }

  public Object getPayload() {
    return payload;
  }

}
