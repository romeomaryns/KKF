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
 * Criteria class for the Adres entity. This class is used in AdresResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /adres?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AdresCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter straatnaam;

    private StringFilter huisnummer;

    private StringFilter postcode;

    private StringFilter stad;

    private LongFilter adresTypeId;

    public AdresCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getStraatnaam() {
        return straatnaam;
    }

    public void setStraatnaam(StringFilter straatnaam) {
        this.straatnaam = straatnaam;
    }

    public StringFilter getHuisnummer() {
        return huisnummer;
    }

    public void setHuisnummer(StringFilter huisnummer) {
        this.huisnummer = huisnummer;
    }

    public StringFilter getPostcode() {
        return postcode;
    }

    public void setPostcode(StringFilter postcode) {
        this.postcode = postcode;
    }

    public StringFilter getStad() {
        return stad;
    }

    public void setStad(StringFilter stad) {
        this.stad = stad;
    }

    public LongFilter getAdresTypeId() {
        return adresTypeId;
    }

    public void setAdresTypeId(LongFilter adresTypeId) {
        this.adresTypeId = adresTypeId;
    }

    @Override
    public String toString() {
        return "AdresCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (straatnaam != null ? "straatnaam=" + straatnaam + ", " : "") +
                (huisnummer != null ? "huisnummer=" + huisnummer + ", " : "") +
                (postcode != null ? "postcode=" + postcode + ", " : "") +
                (stad != null ? "stad=" + stad + ", " : "") +
                (adresTypeId != null ? "adresTypeId=" + adresTypeId + ", " : "") +
            "}";
    }

}
