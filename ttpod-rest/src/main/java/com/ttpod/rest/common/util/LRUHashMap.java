package com.ttpod.rest.common.util;

import groovy.transform.CompileStatic;

import java.util.LinkedHashMap;
import java.util.Map;
@CompileStatic
public final class LRUHashMap<K, V> extends LinkedHashMap<K, V> {

  private final int maxSize;

  /**
   * Constructor.
   * 
   * @param initialSize initial cache size.
   * @param maxSize maximum cache size.
   */
  public LRUHashMap(final int initialSize, final int maxSize) {
    super(initialSize);
    this.maxSize = maxSize;
  }

  /**
   * Constructor.
   * Equivalent to calling LRUHashMap(size, size);
   * 
   * @param size initial and maximum cache size.
   */
  public LRUHashMap(final int size) {
    this(size, size);
  }

  @Override
  protected boolean removeEldestEntry(final Map.Entry<K, V> eldest) {
    return size() >= this.maxSize;
  }
}