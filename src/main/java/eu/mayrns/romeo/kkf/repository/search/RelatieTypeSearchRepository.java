package eu.mayrns.romeo.kkf.repository.search;

import eu.mayrns.romeo.kkf.domain.RelatieType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the RelatieType entity.
 */
public interface RelatieTypeSearchRepository extends ElasticsearchRepository<RelatieType, Long> {
}
