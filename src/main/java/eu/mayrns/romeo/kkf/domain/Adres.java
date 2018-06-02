package eu.mayrns.romeo.kkf.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Adres.
 */
@Entity
@Table(name = "adres")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "adres")
public class Adres implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "straatnaam", nullable = false)
    private String straatnaam;

    @Column(name = "huisnummer")
    private String huisnummer;

    @Column(name = "postcode")
    private String postcode;

    @Column(name = "stad")
    private String stad;

    @ManyToOne
    private AdresType adresType;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStraatnaam() {
        return straatnaam;
    }

    public Adres straatnaam(String straatnaam) {
        this.straatnaam = straatnaam;
        return this;
    }

    public void setStraatnaam(String straatnaam) {
        this.straatnaam = straatnaam;
    }

    public String getHuisnummer() {
        return huisnummer;
    }

    public Adres huisnummer(String huisnummer) {
        this.huisnummer = huisnummer;
        return this;
    }

    public void setHuisnummer(String huisnummer) {
        this.huisnummer = huisnummer;
    }

    public String getPostcode() {
        return postcode;
    }

    public Adres postcode(String postcode) {
        this.postcode = postcode;
        return this;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getStad() {
        return stad;
    }

    public Adres stad(String stad) {
        this.stad = stad;
        return this;
    }

    public void setStad(String stad) {
        this.stad = stad;
    }

    public AdresType getAdresType() {
        return adresType;
    }

    public Adres adresType(AdresType adresType) {
        this.adresType = adresType;
        return this;
    }

    public void setAdresType(AdresType adresType) {
        this.adresType = adresType;
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
        Adres adres = (Adres) o;
        if (adres.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), adres.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Adres{" +
            "id=" + getId() +
            ", straatnaam='" + getStraatnaam() + "'" +
            ", huisnummer='" + getHuisnummer() + "'" +
            ", postcode='" + getPostcode() + "'" +
            ", stad='" + getStad() + "'" +
            "}";
    }
}
