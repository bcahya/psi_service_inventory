package id.sis.service.inventory.pojo;

import java.util.ArrayList;
import java.util.List;

public class RB_MOProduct {
	int product_id = 0;
	int bp_id = 0;
	boolean is_bom = false;
	List<Integer> list_routing = new ArrayList<>();
	List<RB_MOProductReplenish> list_replenish = new ArrayList<RB_MOProductReplenish>();
	
	public List<RB_MOProductReplenish> getList_replenish() {
		return list_replenish;
	}
	public void setList_replenish(List<RB_MOProductReplenish> list_replenish) {
		this.list_replenish = list_replenish;
	}
	public boolean isIs_bom() {
		return is_bom;
	}
	public void setIs_bom(boolean is_bom) {
		this.is_bom = is_bom;
	}
	public List<Integer> getList_routing() {
		return list_routing;
	}
	public void setList_routing(List<Integer> list_routing) {
		this.list_routing = list_routing;
	}
	public int getProduct_id() {
		return product_id;
	}
	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}
	public int getBp_id() {
		return bp_id;
	}
	public void setBp_id(int bp_id) {
		this.bp_id = bp_id;
	}
}
