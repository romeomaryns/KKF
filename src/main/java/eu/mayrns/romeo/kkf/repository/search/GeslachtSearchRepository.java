package eu.mayrns.romeo.kkf.repository.search;

import eu.mayrns.romeo.kkf.domain.Geslacht;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Geslacht entity.
 */
public interface GeslachtSearchRepository extends ElasticsearchRepository<Geslacht, Long> {
}
