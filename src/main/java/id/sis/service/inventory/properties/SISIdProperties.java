package id.sis.service.inventory.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@ConfigurationProperties(prefix = "sis.id")
@Configuration("sisIdProperties")
@Data
public class SISIdProperties {
	String dir_po;
    Integer ad_client_id;
    Integer ad_user_id;
    Integer c_currency_id;
    Integer po_doctype_id;
    Integer po_pricelist_id;
    Integer po_paymentterm_id;

	public Integer getPo_paymentterm_id() {
		return po_paymentterm_id;
	}

	public void setPo_paymentterm_id(Integer po_paymentterm_id) {
		this.po_paymentterm_id = po_paymentterm_id;
	}

	public Integer getC_currency_id() {
		return c_currency_id;
	}

	public void setC_currency_id(Integer c_currency_id) {
		this.c_currency_id = c_currency_id;
	}

	public Integer getPo_pricelist_id() {
		return po_pricelist_id;
	}

	public void setPo_pricelist_id(Integer po_pricelist_id) {
		this.po_pricelist_id = po_pricelist_id;
	}

	public Integer getAd_user_id() {
		return ad_user_id;
	}

	public void setAd_user_id(Integer ad_user_id) {
		this.ad_user_id = ad_user_id;
	}

	public Integer getPo_doctype_id() {
		return po_doctype_id;
	}

	public void setPo_doctype_id(Integer po_doctype_id) {
		this.po_doctype_id = po_doctype_id;
	}

	public String getDir_po() {
		return dir_po;
	}

	public void setDir_po(String dir_po) {
		this.dir_po = dir_po;
	}

	public Integer getAd_client_id() {
		return ad_client_id;
	}

	public void setAd_client_id(Integer ad_client_id) {
		this.ad_client_id = ad_client_id;
	}
    
    
}
