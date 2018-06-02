package eu.mayrns.romeo.kkf.repository.search;

import eu.mayrns.romeo.kkf.domain.Adres;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Adres entity.
 */
public interface AdresSearchRepository extends ElasticsearchRepository<Adres, Long> {
}
