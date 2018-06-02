package eu.mayrns.romeo.kkf.repository.search;

import eu.mayrns.romeo.kkf.domain.ContactType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ContactType entity.
 */
public interface ContactTypeSearchRepository extends ElasticsearchRepository<ContactType, Long> {
}
