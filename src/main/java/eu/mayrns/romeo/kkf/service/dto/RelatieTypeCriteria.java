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
 * Criteria class for the RelatieType entity. This class is used in RelatieTypeResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /relatie-types?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RelatieTypeCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter omschrijving;

    public RelatieTypeCriteria() {
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

    @Override
    public String toString() {
        return "RelatieTypeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (omschrijving != null ? "omschrijving=" + omschrijving + ", " : "") +
            "}";
    }

}
