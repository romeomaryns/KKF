package eu.mayrns.romeo.kkf.repository.search;

import eu.mayrns.romeo.kkf.domain.Relatie;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Relatie entity.
 */
public interface RelatieSearchRepository extends ElasticsearchRepository<Relatie, Long> {
}
