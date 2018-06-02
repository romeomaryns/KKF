package eu.mayrns.romeo.kkf.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ContactType.
 */
@Entity
@Table(name = "contact_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "contacttype")
public class ContactType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "omschrijving", nullable = false)
    private String omschrijving;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public ContactType omschrijving(String omschrijving) {
        this.omschrijving = omschrijving;
        return this;
    }

    public void setOmschrijving(String omschrijving) {
        this.omschrijving = omschrijving;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ContactType contactType = (ContactType) o;
        if (contactType.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), contactType.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ContactType{" +
            "id=" + getId() +
            ", omschrijving='" + getOmschrijving() + "'" +
            "}";
    }
}
