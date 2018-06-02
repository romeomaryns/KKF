package eu.mayrns.romeo.kkf.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Contact entity. This class is used in ContactResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /contacts?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ContactCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter omschrijving;

    private LongFilter persoonId;

    private LongFilter contactTypeId;

    public ContactCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getOmschrijving() {
        return omschrijving;
    }

    public void setOmschrijving(StringFilter omschrijving) {
        this.omschrijving = omschrijving;
    }

    public LongFilter getPersoonId() {
        return persoonId;
    }

    public void setPersoonId(LongFilter persoonId) {
        this.persoonId = persoonId;
    }

    public LongFilter getContactTypeId() {
        return contactTypeId;
    }

    public void setContactTypeId(LongFilter contactTypeId) {
        this.contactTypeId = contactTypeId;
    }

    @Override
    public String toString() {
        return "ContactCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (omschrijving != null ? "omschrijving=" + omschrijving + ", " : "") +
                (persoonId != null ? "persoonId=" + persoonId + ", " : "") +
                (contactTypeId != null ? "contactTypeId=" + contactTypeId + ", " : "") +
            "}";
    }

}
