package id.sis.service.inventory.pojo;

import java.util.List;

public class RB_InventoryChargeBOM {
	int rm_id;
	List<RB_InventoryChargeBOMMaterial> list_material;
	
	public int getRm_id() {
		return rm_id;
	}
	public void setRm_id(int rm_id) {
		this.rm_id = rm_id;
	}
	public List<RB_InventoryChargeBOMMaterial> getList_material() {
		return list_material;
	}
	public void setList_material(List<RB_InventoryChargeBOMMaterial> list_material) {
		this.list_material = list_material;
	}
}
