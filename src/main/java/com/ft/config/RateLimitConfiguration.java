package com.ft.config;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.CompleteConfiguration;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.hazelcast.core.HazelcastInstance;

import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.grid.GridBucketState;
import io.github.bucket4j.grid.ProxyManager;
import io.github.bucket4j.grid.jcache.JCache;

@ConfigurationProperties(prefix = "rate-limit")
public class RateLimitConfiguration {

	private final static Logger log = LoggerFactory.getLogger(RateLimitConfiguration.class);
	
    @Autowired
    HazelcastInstance hazelcastInstance;
    
    /**
     * Map of Hourly TPS, hour from "0" - "23", and TPS in long
     */
    private Map<String, Long> hourlyTps = new HashMap<>();
    
    @PostConstruct
	public void init() {
		log.info("=== Rate Limit Properties: {}", toString());
		hourlyTps.entrySet().forEach(entry -> {
			String key = Constants.MAP_TPS + entry.getKey(); 
			hazelcastInstance.getMap(Constants.MAP_TPS).remove(key);
			log.info("==== {} rate limit: {} TPS", key, entry.getValue());
		});
	}
	
	/**
     * Create proxy manager for Bucket4j
     * @return
     */
    @Bean
    public ProxyManager<String> bucketProxyManager() {
    	CachingProvider cachingProvider = Caching.getCachingProvider();
        CacheManager cacheManager = cachingProvider.getCacheManager();
        CompleteConfiguration<String, GridBucketState> config =
            new MutableConfiguration<String, GridBucketState>()
                .setTypes(String.class, GridBucketState.class);

        Cache<String, GridBucketState> cache = cacheManager.createCache(Constants.MAP_TPS, config);
            ProxyManager<String> proxyManager = Bucket4j
                            .extension(JCache.class)
                            .proxyManagerForCache(cache);
            return proxyManager;
    }

	@Override
	public String toString() {
		return "RateLimitConfiguration [hourlyTps=" + hourlyTps + "]";
	}

	public Map<String, Long> getHourlyTps() {
		return hourlyTps;
	}

	public void setHourlyTps(Map<String, Long> hourlyTps) {
		this.hourlyTps = hourlyTps;
	}
    
    
}
