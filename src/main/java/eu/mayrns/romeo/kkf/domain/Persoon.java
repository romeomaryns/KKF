package eu.mayrns.romeo.kkf.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Persoon.
 */
@Entity
@Table(name = "persoon")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "persoon")
public class Persoon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "voornaam", nullable = false)
    private String voornaam;

    @NotNull
    @Column(name = "familienaam", nullable = false)
    private String familienaam;

    @Column(name = "geboortedatum")
    private LocalDate geboortedatum;

    @ManyToOne
    private Contact contactInfo;

    @ManyToOne
    private Geslacht geslacht;

    @ManyToOne
    private Adres adresInfo;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "persoon_relaties",
               joinColumns = @JoinColumn(name="persoons_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="relaties_id", referencedColumnName="id"))
    private Set<Relatie> relaties = new HashSet<>();

    @ManyToOne
    private Contact contact;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVoornaam() {
        return voornaam;
    }

    public Persoon voornaam(String voornaam) {
        this.voornaam = voornaam;
        return this;
    }

    public void setVoornaam(String voornaam) {
        this.voornaam = voornaam;
    }

    public String getFamilienaam() {
        return familienaam;
    }

    public Persoon familienaam(String familienaam) {
        this.familienaam = familienaam;
        return this;
    }

    public void setFamilienaam(String familienaam) {
        this.familienaam = familienaam;
    }

    public LocalDate getGeboortedatum() {
        return geboortedatum;
    }

    public Persoon geboortedatum(LocalDate geboortedatum) {
        this.geboortedatum = geboortedatum;
        return this;
    }

    public void setGeboortedatum(LocalDate geboortedatum) {
        this.geboortedatum = geboortedatum;
    }

    public Contact getContactInfo() {
        return contactInfo;
    }

    public Persoon contactInfo(Contact contact) {
        this.contactInfo = contact;
        return this;
    }

    public void setContactInfo(Contact contact) {
        this.contactInfo = contact;
    }

    public Geslacht getGeslacht() {
        return geslacht;
    }

    public Persoon geslacht(Geslacht geslacht) {
        this.geslacht = geslacht;
        return this;
    }

    public void setGeslacht(Geslacht geslacht) {
        this.geslacht = geslacht;
    }

    public Adres getAdresInfo() {
        return adresInfo;
    }

    public Persoon adresInfo(Adres adres) {
        this.adresInfo = adres;
        return this;
    }

    public void setAdresInfo(Adres adres) {
        this.adresInfo = adres;
    }

    public Set<Relatie> getRelaties() {
        return relaties;
    }

    public Persoon relaties(Set<Relatie> relaties) {
        this.relaties = relaties;
        return this;
    }

    public Persoon addRelaties(Relatie relatie) {
        this.relaties.add(relatie);
        relatie.getPersonens().add(this);
        return this;
    }

    public Persoon removeRelaties(Relatie relatie) {
        this.relaties.remove(relatie);
        relatie.getPersonens().remove(this);
        return this;
    }

    public void setRelaties(Set<Relatie> relaties) {
        this.relaties = relaties;
    }

    public Contact getContact() {
        return contact;
    }

    public Persoon contact(Contact contact) {
        this.contact = contact;
        return this;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
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
        Persoon persoon = (Persoon) o;
        if (persoon.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), persoon.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Persoon{" +
            "id=" + getId() +
            ", voornaam='" + getVoornaam() + "'" +
            ", familienaam='" + getFamilienaam() + "'" +
            ", geboortedatum='" + getGeboortedatum() + "'" +
            "}";
    }
}
