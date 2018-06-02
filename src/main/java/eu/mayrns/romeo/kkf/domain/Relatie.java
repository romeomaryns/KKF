package eu.mayrns.romeo.kkf.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Relatie.
 */
@Entity
@Table(name = "relatie")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "relatie")
public class Relatie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "omschrijving", nullable = false)
    private String omschrijving;

    @ManyToOne
    private RelatieType relatieType;

    @ManyToMany(mappedBy = "relaties")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Persoon> personens = new HashSet<>();

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

    public Relatie omschrijving(String omschrijving) {
        this.omschrijving = omschrijving;
        return this;
    }

    public void setOmschrijving(String omschrijving) {
        this.omschrijving = omschrijving;
    }

    public RelatieType getRelatieType() {
        return relatieType;
    }

    public Relatie relatieType(RelatieType relatieType) {
        this.relatieType = relatieType;
        return this;
    }

    public void setRelatieType(RelatieType relatieType) {
        this.relatieType = relatieType;
    }

    public Set<Persoon> getPersonens() {
        return personens;
    }

    public Relatie personens(Set<Persoon> persoons) {
        this.personens = persoons;
        return this;
    }

    public Relatie addPersonen(Persoon persoon) {
        this.personens.add(persoon);
        persoon.getRelaties().add(this);
        return this;
    }

    public Relatie removePersonen(Persoon persoon) {
        this.personens.remove(persoon);
        persoon.getRelaties().remove(this);
        return this;
    }

    public void setPersonens(Set<Persoon> persoons) {
        this.personens = persoons;
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
        Relatie relatie = (Relatie) o;
        if (relatie.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), relatie.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Relatie{" +
            "id=" + getId() +
            ", omschrijving='" + getOmschrijving() + "'" +
            "}";
    }
}
