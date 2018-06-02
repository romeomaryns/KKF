package eu.mayrns.romeo.kkf.repository.search;

import eu.mayrns.romeo.kkf.domain.Persoon;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Persoon entity.
 */
public interface PersoonSearchRepository extends ElasticsearchRepository<Persoon, Long> {
}
