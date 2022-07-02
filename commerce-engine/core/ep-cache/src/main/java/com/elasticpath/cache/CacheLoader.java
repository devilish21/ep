/**
 * Copyright (c) Elastic Path Software Inc., 2016
 */
package com.elasticpath.cache;

import java.util.Map;

/**
 * Used when a cache is read-through.
 *
 * Carbon copy of the JSR-107 {@link javax.cache.integration.CacheLoader} interface.  The
 * only reason we're using this instead of the actual JSR-107 interface is that the cache-api
 * bundle does not export javax.cache.integration.  Grrr.
 *
 * @param <K> the type of keys handled by this loader
 * @param <V> the type of values generated by this loader
 * @author Greg Luck
 * @author Yannis Cosmadopoulos
 * @since 1.0
 */
public interface CacheLoader<K, V> {

	/**
	 * Loads an object. Application developers should implement this
	 * method to customize the loading of a value for a cache entry. This method
	 * is called by a cache when a requested entry is not in the cache. If
	 * the object can't be loaded <code>null</code> should be returned.
	 *
	 * @param key the key identifying the object being loaded
	 * @return The value for the entry that is to be stored in the cache or
	 *         <code>null</code> if the object can't be loaded
	 */
	V load(K key);

	/**
	 * Loads multiple objects. Application developers should implement this
	 * method to customize the loading of cache entries. This method is called
	 * when the requested object is not in the cache. If an object can't be loaded,
	 * it is not returned in the resulting map.
	 *
	 * @param keys keys identifying the values to be loaded
	 * @return A map of key, values to be stored in the cache.
	 */
	Map<K, V> loadAll(Iterable<? extends K> keys);
}
