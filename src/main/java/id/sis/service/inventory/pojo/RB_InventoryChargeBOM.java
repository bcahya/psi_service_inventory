package id.sis.service.inventory.pojo;

import java.util.List;

public class RB_InventoryChargeBOM {
	int bom_id;
	int m_product_id;
	List<RB_InventoryChargeBOMMaterial> list_material;
	
	public int getBom_id() {
		return bom_id;
	}
	public void setBom_id(int bom_id) {
		this.bom_id = bom_id;
	}
	public int getM_product_id() {
		return m_product_id;
	}
	public void setM_product_id(int m_product_id) {
		this.m_product_id = m_product_id;
	}
	public List<RB_InventoryChargeBOMMaterial> getList_material() {
		return list_material;
	}
	public void setList_material(List<RB_InventoryChargeBOMMaterial> list_material) {
		this.list_material = list_material;
	}
}
