package co.topper.configuration;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

import java.time.Duration;

@Configuration
//@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfiguration {

    public static final String CACHE_ARTISTS_DB = "artistsCacheDb";
    public static final String CACHE_TRACKS_DB = "tracksCacheDb";
    public static final String CACHE_ALBUMS_DB = "albumsCacheDb";
    public static final String CACHE_ARTIST_SERVICE = "tracksCacheService";
    public static final String CACHE_TRACKS_SERVICE = "tracksCacheService";
    public static final String CACHE_ALBUMS_SERVICE = "albumsCacheService";
    public static final String CACHE_USER_EMAILS = "userEmailsCache";

    private static final Integer CACHE_SERVICE_IN_MINUTES = 120;
    private static final Integer CACHE_DB_IN_MINUTES = 10;
    private static final Integer CACHE_DB_EMAILS_MINUTES = 60;

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(CACHE_SERVICE_IN_MINUTES))
                .disableCachingNullValues()
                .serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return (builder) -> builder
                .withCacheConfiguration(CACHE_ARTISTS_DB,
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(CACHE_DB_IN_MINUTES)))
                .withCacheConfiguration(CACHE_TRACKS_DB,
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(CACHE_DB_IN_MINUTES)))
                .withCacheConfiguration(CACHE_ALBUMS_DB,
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(CACHE_DB_IN_MINUTES)))
                .withCacheConfiguration(CACHE_ARTIST_SERVICE,
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(CACHE_SERVICE_IN_MINUTES)))
                .withCacheConfiguration(CACHE_TRACKS_SERVICE,
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(CACHE_SERVICE_IN_MINUTES)))
                .withCacheConfiguration(CACHE_ALBUMS_SERVICE,
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(CACHE_SERVICE_IN_MINUTES)))
                .withCacheConfiguration(CACHE_USER_EMAILS,
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(CACHE_DB_EMAILS_MINUTES)));
    }

}
