package com.coders.istanbul.cache;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;
import redis.clients.jedis.Jedis;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


public class RedisBackedCacheTest {

    @Rule
    public GenericContainer redis = new GenericContainer("redis:3.0.6")
            .withExposedPorts(6379);

    private Cache cache;

    @Before
    public void setUp() {
        Jedis jedis = new Jedis(redis.getContainerIpAddress(), redis.getMappedPort(6379));
        cache = new RedisBackedCache(jedis, "test");
    }

    @Test
    public void it_should_find_inserted_value_from_cache_by_key_name() {
        //Arrange
        cache.put("coders", "Enes Açıkoğlu");

        //Act
        Optional<String> foundObject = cache.get("coders", String.class);

        //Assert
        assertThat(foundObject).isPresent();
        assertThat(foundObject.get()).isEqualTo("Enes Açıkoğlu");
    }

    @Test
    public void it_should_not_find_anything_from_cache_when_no_value_inserted_by_given_key_name() {
        //Arrange

        //Act
        Optional<String> foundObject = cache.get("software-craftsmanship", String.class);

        //Assert
        assertThat(foundObject).isNotPresent();
    }

}
