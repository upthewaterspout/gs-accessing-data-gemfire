package hello;

import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.lucene.LuceneService;
import org.apache.geode.cache.lucene.LuceneServiceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.CacheFactoryBean;
import org.springframework.data.gemfire.PartitionedRegionFactoryBean;
import org.springframework.data.gemfire.mapping.MappingPdxSerializer;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

import java.util.Properties;

@Configuration
@EnableGemfireRepositories
@SuppressWarnings("unused")
public class ApplicationConfiguration {

  @Bean
  Properties gemfireProperties() {
    Properties gemfireProperties = new Properties();
    gemfireProperties.setProperty("name", "DataGemFireApplication");
    gemfireProperties.setProperty("mcast-port", "0");
    gemfireProperties.setProperty("log-level", "config");
    return gemfireProperties;
  }

  @Bean
  CacheFactoryBean gemfireCache() {
    CacheFactoryBean gemfireCache = new CacheFactoryBean();
    gemfireCache.setClose(true);
    gemfireCache.setPdxSerializer(new MappingPdxSerializer());
    gemfireCache.setProperties(gemfireProperties());
    return gemfireCache;
  }

  @Bean
  LuceneService luceneService(final GemFireCache cache) {
    final LuceneService luceneService = LuceneServiceProvider.get(cache);
    luceneService.createIndex("luceneIndex", "hello", "name", "age");
    return luceneService;
  }

  @Bean
  PartitionedRegionFactoryBean<String, Person> helloRegion(final GemFireCache cache,
                                                           final LuceneService luceneService) {
    PartitionedRegionFactoryBean<String, Person>
        helloRegion =
        new PartitionedRegionFactoryBean<String, Person>();
    helloRegion.setCache(cache);
    helloRegion.setClose(false);
    helloRegion.setName("hello");
    helloRegion.setPersistent(false);
    return helloRegion;
  }
}