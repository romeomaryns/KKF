package eu.mayrns.romeo.kkf.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;


import io.github.jhipster.service.filter.LocalDateFilter;



/**
 * Criteria class for the Persoon entity. This class is used in PersoonResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /persoons?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PersoonCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter voornaam;

    private StringFilter familienaam;

    private LocalDateFilter geboortedatum;

    private LongFilter contactInfoId;

    private LongFilter geslachtId;

    private LongFilter adresInfoId;

    private LongFilter relatiesId;

    private LongFilter contactId;

    public PersoonCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getVoornaam() {
        return voornaam;
    }

    public void setVoornaam(StringFilter voornaam) {
        this.voornaam = voornaam;
    }

    public StringFilter getFamilienaam() {
        return familienaam;
    }

    public void setFamilienaam(StringFilter familienaam) {
        this.familienaam = familienaam;
    }

    public LocalDateFilter getGeboortedatum() {
        return geboortedatum;
    }

    public void setGeboortedatum(LocalDateFilter geboortedatum) {
        this.geboortedatum = geboortedatum;
    }

    public LongFilter getContactInfoId() {
        return contactInfoId;
    }

    public void setContactInfoId(LongFilter contactInfoId) {
        this.contactInfoId = contactInfoId;
    }

    public LongFilter getGeslachtId() {
        return geslachtId;
    }

    public void setGeslachtId(LongFilter geslachtId) {
        this.geslachtId = geslachtId;
    }

    public LongFilter getAdresInfoId() {
        return adresInfoId;
    }

    public void setAdresInfoId(LongFilter adresInfoId) {
        this.adresInfoId = adresInfoId;
    }

    public LongFilter getRelatiesId() {
        return relatiesId;
    }

    public void setRelatiesId(LongFilter relatiesId) {
        this.relatiesId = relatiesId;
    }

    public LongFilter getContactId() {
        return contactId;
    }

    public void setContactId(LongFilter contactId) {
        this.contactId = contactId;
    }

    @Override
    public String toString() {
        return "PersoonCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (voornaam != null ? "voornaam=" + voornaam + ", " : "") +
                (familienaam != null ? "familienaam=" + familienaam + ", " : "") +
                (geboortedatum != null ? "geboortedatum=" + geboortedatum + ", " : "") +
                (contactInfoId != null ? "contactInfoId=" + contactInfoId + ", " : "") +
                (geslachtId != null ? "geslachtId=" + geslachtId + ", " : "") +
                (adresInfoId != null ? "adresInfoId=" + adresInfoId + ", " : "") +
                (relatiesId != null ? "relatiesId=" + relatiesId + ", " : "") +
                (contactId != null ? "contactId=" + contactId + ", " : "") +
            "}";
    }

}
