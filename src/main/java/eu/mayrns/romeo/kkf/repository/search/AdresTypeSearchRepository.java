package eu.mayrns.romeo.kkf.repository.search;

import eu.mayrns.romeo.kkf.domain.AdresType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the AdresType entity.
 */
public interface AdresTypeSearchRepository extends ElasticsearchRepository<AdresType, Long> {
}
