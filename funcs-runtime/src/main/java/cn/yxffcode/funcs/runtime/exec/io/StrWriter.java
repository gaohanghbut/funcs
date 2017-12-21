package cn.yxffcode.funcs.runtime.exec.io;

import java.io.IOException;
import java.io.Writer;

/**
 * @author gaohang on 8/26/17.
 */
public class StrWriter extends Writer {

  private StringBuilder buf;

  public StrWriter() {
    buf = new StringBuilder();
    lock = buf;
  }

  public StrWriter(int initialSize) {
    if (initialSize < 0) {
      throw new IllegalArgumentException("Negative buffer size");
    }
    buf = new StringBuilder(initialSize);
    lock = buf;
  }

  public void write(int c) {
    buf.append((char) c);
  }

  public void write(char cbuf[], int off, int len) {
    if ((off < 0) || (off > cbuf.length) || (len < 0) ||
        ((off + len) > cbuf.length) || ((off + len) < 0)) {
      throw new IndexOutOfBoundsException();
    } else if (len == 0) {
      return;
    }
    buf.append(cbuf, off, len);
  }

  public void write(String str) {
    buf.append(str);
  }

  public void write(String str, int off, int len) {
    buf.append(str.substring(off, off + len));
  }

  public StrWriter append(CharSequence csq) {
    if (csq == null)
      write("null");
    else
      write(csq.toString());
    return this;
  }

  public StrWriter append(CharSequence csq, int start, int end) {
    CharSequence cs = (csq == null ? "null" : csq);
    write(cs.subSequence(start, end).toString());
    return this;
  }

  public StrWriter append(char c) {
    write(c);
    return this;
  }

  public String toString() {
    return buf.toString();
  }

  public StringBuilder getBuffer() {
    return buf;
  }

  public void flush() {
  }

  public void close() throws IOException {
  }

}
