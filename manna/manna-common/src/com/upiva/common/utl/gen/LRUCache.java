package com.ianf.gdx.general.utl.gen;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache<K, V>
{
    /**
     * Called when a cached element is about to be removed.
     */
    public interface CacheEntryRemovedListener<K, V>
    {
        void notifyEntryRemoved( K key, V value );
    }

    private Map<K,V> m_cache;
    private CacheEntryRemovedListener<K,V> m_listener;

    /**
     * Creates the m_cache with the specified max entries.
     */
    public LRUCache( final int maxEntries )
    {
        m_cache = new LinkedHashMap<K,V>( maxEntries + 1, .75F, true ) {
            public boolean removeEldestEntry( Map.Entry<K,V> eldest )
            {
                if( size() > maxEntries ) {
                    if( m_listener != null ) {
                        m_listener.notifyEntryRemoved( eldest.getKey(), eldest.getValue() );
                    }
                    return true;
                }
                return false;
            }
        };
    }

    public void add( K key, V value )
    {
        m_cache.put( key, value );
    }

    public V get( K key )
    {
        return m_cache.get( key );
    }

    public Collection<V> retrieveAll()
    {
        return m_cache.values();
    }

    public void setListener( CacheEntryRemovedListener<K, V> listener )
    {
        this.m_listener = listener;
    }
}
