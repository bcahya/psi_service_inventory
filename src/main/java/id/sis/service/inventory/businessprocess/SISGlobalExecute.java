package id.sis.service.inventory.businessprocess;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import id.sis.service.inventory.pojo.RB_InventoryCharge;
import id.sis.service.inventory.pojo.RB_InventoryChargeBOM;
import id.sis.service.inventory.pojo.RB_InventoryChargeBOMMaterial;
import id.sis.service.inventory.pojo.RB_InventoryChargeDetail;
import id.sis.service.inventory.properties.SISIdProperties;
import id.sis.service.inventory.response.SISResponse;
import id.sis.service.inventory.utils.SISUtil;

@Component
public class SISGlobalExecute {
	private final static Logger logger = LoggerFactory.getLogger(SISGlobalExecute.class);
	List<Map<String, Object>> listData = new ArrayList<>();

	@Autowired
	@Qualifier("jdbcTemplateCdcSource")
	private JdbcTemplate source;

	@Autowired
	private SISIdProperties sisIdProperties;

//	@Transactional(rollbackFor = Exception.class)
//	public SISResponse executeFWspCreateMaterialEDX(String name) throws Exception {
//		SISResponse response = new SISResponse();
//
//		try {
//			String edxIdMaterial = UUID.randomUUID().toString().toUpperCase();
//			String itemIdMaterial = UUID.randomUUID().toString().toUpperCase();
//			String sqlMaterial = "exec FWsp_CreateMATERIALEDX " +
//				"@EDXID = '" + edxIdMaterial + "'" +
//				", @ItemID = '" + itemIdMaterial + "'" +
//				", @Item_Code = 'RM-22 " + name + "'" +
//				", @Description = 'RM-22 " + name + "'" +
//				", @Internal_UOM = 'kg'" +
//				", @Item_Group_Code = '1 AGG'" +
//				", @CompanyID = '" + sisIdProperties.getCompanyid() + "'" +
//				", @LocationID = '" + sisIdProperties.getLocationid() + "'" +
//				", @PlantID = '" + sisIdProperties.getPlantid() + "'" +
//				", @Item_Group_type = 'T'"; 
//			source.execute(sqlMaterial);
//			logger.info("material edx created");
//
//			String edxIdMix = UUID.randomUUID().toString().toUpperCase();
//			String itemIdMix = UUID.randomUUID().toString().toUpperCase();
//			String sqlMix = "exec FWsp_CreateMIXEDX " +
//				"@EDXID = '" + edxIdMix + "'" +
//				", @ItemID = '" + itemIdMix + "'" +
//				", @Item_Code = 'RMX-K22 " + name + "'" +
//				", @Description = 'RMX-K22 " + name + "'" +
//				", @Internal_UOM = 'm3'" +
//				", @CompanyID  = '" + sisIdProperties.getCompanyid() + "'" +
//				", @LocationID = '" + sisIdProperties.getLocationid() + "'" +
//				", @PlantID    = '" + sisIdProperties.getPlantid() + "'" +
//				", @Max_Batch_Size_UOM = 'm3'" +
//				", @ItemType = 'M'" +
//				", @Track_Usage_Flag = 1" +
//				", @Trade_Discount_Flag = 0";
//			source.execute(sqlMix);
//			logger.info("mix edx created");
//
//			String sqlMixIngre = "exec FWsp_CreateMIXIngredientEDX " +
//				"@MixEDXID = '" + edxIdMix + "'" +
//				", @IngredItemID = '" + itemIdMaterial + "'" +
//				", @MixCode = ''" +
//				", @IngredCode= ''" +
//				", @LocationID = '" + sisIdProperties.getLocationid() + "'" +
//				", @Entry_Qty = 15000" +
//				", @Entry_UOM = 'kg'" +
//				", @Item_Type = 'M'" +
//				", @Based_On_Qty = 17";
//			source.execute(sqlMixIngre);
//			logger.info("mix ingredient line created");
//
//			String orderId = UUID.randomUUID().toString();
//			String sqlOrder = "exec FWsp_CreateOrderEDX " +
//				"@EDXID = '" + edxIdMix + "'" +
//				", @CompanyID  = '" + sisIdProperties.getCompanyid() + "'" +
//				", @LocationID = '" + sisIdProperties.getLocationid() + "'" +
//				", @Order_Code = 'STO-2024.C'" +
//				", @OrderID = '" + orderId + "'" +
//				", @CustomerID = 'B6C005F2-8A28-4E05-9DE1-7C049889D176'" +
//				", @PO_Num = 'PO-2024'" +
//				", @Address_Line1 = 'Jl. Melati No.1'" +
//				", @Address_Line2 = 'Kec.Manyar, Kab.Gresik'" +
//				", @Address_Line3 = 'Jawa Timur'" +
//				", @Req_First_Load_On_Job_TDS = '2024-03-04'";
//			source.execute(sqlOrder);
//
//			String orderLineId = UUID.randomUUID().toString();
//			String sqlOrderLine = "exec FWsp_CreateOrderLineEDX " +
//				"@Order_LineID = '" + orderLineId + "'" +
//				", @OrderID = '" + orderId + "'" +
//				", @ItemID = '" + itemIdMix + "'" +
//				", @Load_Size = '6'" +
//				", @Ordered_Qty = '6'" +
//				", @Ordered_Qty_UOM = 'm3'" +
//				", @Sort_Line_Num='0'";
//			source.execute(sqlOrderLine);
//
//			response.setStatus("S");
//			response.setMessage("Material RM-22 " + name + " created");
//			logger.info("Create material Success");
//		} catch (Exception e) {
//			response.setStatus("E");
//			response.setMessage("Create material Failed: " + e.getMessage());
//			logger.info(e.getMessage());
//			throw new Exception(e.getMessage());
//		}
//
//		return response;
//	}
//
//	
//
	public SISResponse calculateInventoryBook(Integer m_inventoryline_id) throws Exception {
		logger.info("[SIS] calculateInventoryBook m_inventoryline_id : " + m_inventoryline_id.toString());
		SISResponse response = new SISResponse();
		try {
			String sql =
					"select "
					+ "    il.m_locator_id::int, "
					+ "    il.m_product_id::int, "
					+ "    il.ad_org_id::int, "
					+ "    to_char(trunc(i.movementdate),'YYYYMMDD') movementdate "
					+ "from m_inventoryline il "
					+ "inner join m_inventory i "
					+ "    on i.m_inventory_id = il.m_inventory_id "
					+ "and il.m_inventoryline_id = "+m_inventoryline_id;
			List<Map<String, Object>> resultList = source.queryForList(sql);
			int m_locator_id = 0;
			int m_product_id = 0;
			int ad_org_id = 0;
			String movementdate = "";
			if (!resultList.isEmpty()) {
				Map<String, Object> mapResult = resultList.get(0);
				m_locator_id = (int)mapResult.get("m_locator_id");
				m_product_id = (int)mapResult.get("m_product_id");
				ad_org_id = (int)mapResult.get("ad_org_id");
				movementdate = (String)mapResult.get("movementdate");
			}
			
			sql =
				"select "
				+ "    sum(t.movementqty)::text qty "
				+ "from m_transaction t "
				+ "where t.m_locator_id = "+m_locator_id+" "
				+ "and t.m_product_id = "+m_product_id+" "
				+ "and t.ad_org_id = "+ad_org_id+" "
				+ "and trunc(t.movementdate) <= '"+movementdate+"'::date ";
			resultList = source.queryForList(sql);
			if (!resultList.isEmpty()) {
				response = SISResponse.successResponse(resultList);
			}
		} catch (Exception e) {
			response = SISResponse.errorResponse(e.getMessage());
		}
		return response;
	}
	
	public SISResponse calculateInventoryCharge(RB_InventoryCharge param) throws Exception {
		logger.info("[SIS] calculateInventoryCharge");
		SISResponse response = new SISResponse();
		try {
			//sorting
			LinkedHashMap<String, RB_InventoryChargeDetail> mapSort = new LinkedHashMap();
			List<String> listKey = new ArrayList<>();
			List<RB_InventoryChargeDetail> listSorted = new ArrayList<>();
			for (RB_InventoryChargeDetail pd: param.getList_detail()) {
				String key =
						"1"+SISUtil.addZero(Integer.valueOf(pd.getType()), 5)
						+ SISUtil.addZero(pd.getCategory_seqno(), 10)
						+ SISUtil.addZero(pd.getSubcategory_seqno(), 10)
						+ SISUtil.addZero(pd.getLine(), 10);
				mapSort.put(key, pd);
				listKey.add(key);
			}			
			Collections.sort(listKey, Collections.reverseOrder());
			for (String key: listKey) {
				listSorted.add(mapSort.get(key));
			}
			
			LinkedHashMap<String, List<RB_InventoryChargeDetail>> mapType = new LinkedHashMap<>();
			for (RB_InventoryChargeDetail pd: listSorted) {
				if (!mapType.containsKey(pd.getType())) {
					mapType.put(pd.getType(), new ArrayList<RB_InventoryChargeDetail>());
				}
				mapType.get(pd.getType()).add(pd);
			}
			
			List<Map<String, Object>> resultList = new ArrayList<>();
			for (String type: mapType.keySet()) {
				int category_id = 0;
				int subcategory_id = 0;
				BigDecimal qtySC = new BigDecimal(0);
				BigDecimal qtyCredit = new BigDecimal(0);
				List<Map<String, Object>> listSC = new ArrayList<>();
				Map<String, Object> mapSC = new HashMap<String, Object>();
				if (type.equalsIgnoreCase("1")) {
					int countSC = 0;
					List<RB_InventoryChargeDetail> listPD = mapType.get(type);
					for (int a=0; a<listPD.size(); a++) {
						RB_InventoryChargeDetail pd = listPD.get(a);
						
						if (subcategory_id != pd.getSubcategory_id()
								|| a==listPD.size()-1) {
							subcategory_id = pd.getSubcategory_id();
							if (qtySC.signum() > 0) {
								qtyCredit = qtySC;
								countSC = 0;
							}
							
							//last record
							if (a==listPD.size()-1) {
								if (subcategory_id == pd.getSubcategory_id()
										&& category_id == pd.getCategory_id()) {
									qtySC = qtySC.add(pd.getQty());
									mapSC.put("m_inventoryline_id", pd.getM_inventoryline_id());
									mapSC.put("qty", pd.getQty());
									mapSC.put("price", pd.getPrice());
									listSC.add(mapSC);
								}
							}
							
							countSC += 1;
							if (qtySC.signum() < 0) {
								BigDecimal diff = qtySC;
								if (qtyCredit.signum() > 0) {
									diff = qtySC.add(qtyCredit);
								}
								if (diff.signum() < 0) {
									Map<String, Object> mapResult = new HashMap<String, Object>();
									mapResult.put("m_inventoryline_id", mapSC.get("m_inventoryline_id"));
									mapResult.put("amt", diff.abs().multiply((BigDecimal)mapSC.get("price")));
									resultList.add(mapResult);
								}
							}
							
							//last record
							if (a==listPD.size()-1) {
								if (subcategory_id != pd.getSubcategory_id()
										|| category_id != pd.getCategory_id()) {
									qtySC = pd.getQty();
									mapSC.put("m_inventoryline_id", pd.getM_inventoryline_id());
									mapSC.put("qty", pd.getQty());
									mapSC.put("price", pd.getPrice());
									if (qtySC.signum() < 0) {
										BigDecimal diff = qtyCredit.subtract(qtySC);
										if (diff.signum() < 0) {
											Map<String, Object> mapResult = new HashMap<String, Object>();
											mapResult.put("m_inventoryline_id", mapSC.get("m_inventoryline_id"));
											mapResult.put("amt", diff.abs().multiply((BigDecimal)mapSC.get("price")));
											resultList.add(mapResult);
										}
									}
								}
							}
							listSC.clear();
							qtySC = new BigDecimal(0);
						}
						if (category_id != pd.getCategory_id()) {
							category_id = pd.getCategory_id();
							qtySC = new BigDecimal(0);
							qtyCredit = new BigDecimal(0);
						}
						qtySC = qtySC.add(pd.getQty());
						if (countSC == 2) {
							countSC = 0;
							qtyCredit = new BigDecimal(0);
						}
						if (qtySC.signum() > 0
								&& countSC == 0) {
							qtyCredit = qtySC;
						}
						
						mapSC = new HashMap<String, Object>();
						mapSC.put("m_inventoryline_id", pd.getM_inventoryline_id());
						mapSC.put("qty", qtySC);
						mapSC.put("price", pd.getPrice());
						listSC.add(mapSC);
					}
				} else if (type.equalsIgnoreCase("2")) {
					List<RB_InventoryChargeDetail> listPD = mapType.get(type);
					for (int a=0; a<listPD.size(); a++) {
						RB_InventoryChargeDetail pd = listPD.get(a);
						mapSC = new HashMap<String, Object>();
						if (category_id != pd.getCategory_id()
								|| a==listPD.size()-1) {
							
							//last record
							if (a==listPD.size()-1
									&& category_id == pd.getCategory_id()) {
								qtySC = qtySC.add(pd.getQty());
								mapSC.put("m_inventoryline_id", pd.getM_inventoryline_id());
								mapSC.put("qty", pd.getQty());
								mapSC.put("price", pd.getPrice());
								listSC.add(mapSC);
							}
							
							category_id = pd.getCategory_id();
							if (qtySC.signum() < 0) {
								for (Map<String, Object> mapDetail: listSC) {
									if (qtySC.signum() >= 0) {
										break;
									}
									BigDecimal qtyDetail = (BigDecimal)mapDetail.get("qty");
									if (qtyDetail.signum() > 0) {
										continue;
									}
									BigDecimal diff = qtySC.subtract(qtyDetail);
									if (diff.signum() >= 0) {
										qtyDetail = diff;
										if (diff.compareTo(qtySC) > 0) {
											qtyDetail = qtySC;
										}
									}
									qtySC = diff;
									Map<String, Object> mapResult = new HashMap<String, Object>();
									mapResult.put("m_inventoryline_id", mapDetail.get("m_inventoryline_id"));
									mapResult.put("amt", qtyDetail.abs().multiply((BigDecimal)mapDetail.get("price")));
									resultList.add(mapResult);
								}
							}
							
							//last record
							if (a==listPD.size()-1
									&& category_id != pd.getCategory_id()) {
								qtySC = pd.getQty();
								if (qtySC.signum() >= 0) {
									break;
								}
								BigDecimal qtyDetail = pd.getQty();
								if (qtyDetail.signum() > 0) {
									continue;
								}
								BigDecimal diff = qtySC.subtract(qtyDetail);
								if (qtySC.signum() >= 0) {
									qtyDetail = qtySC;
									if (diff.compareTo(qtySC) > 0) {
										qtyDetail = qtySC;
									}
								}
								qtySC = diff;
								Map<String, Object> mapResult = new HashMap<String, Object>();
								mapResult.put("m_inventoryline_id", pd.getM_inventoryline_id());
								mapResult.put("amt", qtyDetail.abs().multiply(pd.getPrice()));
								resultList.add(mapResult);
							}
							
							qtySC = new BigDecimal(0);
						}
						qtySC = qtySC.add(pd.getQty());
						
						mapSC.put("m_inventoryline_id", pd.getM_inventoryline_id());
						mapSC.put("qty", qtySC);
						mapSC.put("price", pd.getPrice());
						listSC.add(mapSC);
					}
				} else if (type.equalsIgnoreCase("3")) {
					List<RB_InventoryChargeDetail> listPD = mapType.get(type);
					BigDecimal totalMaterial = new BigDecimal(0);
					for (int a=0; a<listPD.size(); a++) {
						RB_InventoryChargeDetail pd = listPD.get(a);
						if (!pd.isIs_kemas()) {
							totalMaterial = totalMaterial.add(pd.getQty().multiply(pd.getPrice()));
						} else {
							for (RB_InventoryChargeBOM cb: param.getList_bom()) {
								if (cb.getProduct_kemas_id() == pd.getM_product_id()) {
									for (RB_InventoryChargeBOMMaterial cbm: cb.getList_material()) {
										for(RB_InventoryChargeDetail pdm: listPD) {
											if (pdm.getM_product_id().equals(cbm.getM_product_id())) {
												totalMaterial = totalMaterial.add(pdm.getQty().multiply(pdm.getPrice()));
											}
										}
									}
								}
							}
						}
					}
					Map<String, Object> mapResult = new HashMap<String, Object>();
					mapResult.put("m_inventory_id", param.getM_inventory_id());
					mapResult.put("amt", totalMaterial);
					resultList.add(mapResult);
				} else if (type.equalsIgnoreCase("4")) {
					List<RB_InventoryChargeDetail> listPD = mapType.get(type);
					for (int a=0; a<listPD.size(); a++) {
						RB_InventoryChargeDetail pd = listPD.get(a);
						if (pd.getQty().signum() < 0 && pd.isIs_dibebankan()) {
							Map<String, Object> mapResult = new HashMap<String, Object>();
							mapResult.put("m_inventory_id", param.getM_inventory_id());
							mapResult.put("amt", pd.getQty().abs().multiply(pd.getPrice()));
							resultList.add(mapResult);
						}
					}
				} else if (type.equalsIgnoreCase("5")) {
					List<RB_InventoryChargeDetail> listPD = mapType.get(type);
					BigDecimal totalAmt = new BigDecimal(0);
					BigDecimal amt = new BigDecimal(0);
					BigDecimal amt_netto = new BigDecimal(0);
					BigDecimal percent = new BigDecimal(0);
					BigDecimal price = new BigDecimal(0);
					BigDecimal price_netto = new BigDecimal(0);
					for (int a=0; a<listPD.size(); a++) {
						RB_InventoryChargeDetail pd = listPD.get(a);
						totalAmt = totalAmt.add(pd.getQty().multiply(pd.getPrice()));
						percent = pd.getPercent();
						
						if (category_id != pd.getCategory_id()
								|| a==listPD.size()-1) {
							category_id = pd.getCategory_id();
							
							//last record
							if (a==listPD.size()-1) {
								qtySC = qtySC.add(pd.getQty());
							}
							
							if (qtySC.signum() < 0) {
								amt = amt.add(qtySC.abs().multiply(price));
								amt_netto = amt_netto.add(qtySC.abs().multiply(price_netto));
							}
							
							qtySC = new BigDecimal(0);
							price = new BigDecimal(0);
							price_netto = new BigDecimal(0);
						}
						qtySC = qtySC.add(pd.getQty());
						if (pd.getQty().signum() < 0) {
							if (price.compareTo(pd.getPrice()) < 0) {
								price = pd.getPrice();
							}
							if (price_netto.compareTo(pd.getPrice_netto()) < 0) {
								price_netto = pd.getPrice_netto();
							}
						}
					}
					BigDecimal total = new BigDecimal(0);
					if (totalAmt.signum() < 0) {
						BigDecimal amtBruto = (totalAmt.abs().subtract(amt)).multiply(percent).divide(new BigDecimal(100), RoundingMode.HALF_UP);
						total = amtBruto.add(amt_netto);
					}
					Map<String, Object> mapResult = new HashMap<String, Object>();
					mapResult.put("m_inventoryline_id", 0);
					mapResult.put("amt", total);
					resultList.add(mapResult);
				}
			}
			response = SISResponse.successResponse(resultList);
		} catch (Exception e) {
			response = SISResponse.errorResponse(e.getMessage());
		}
		return response;
	}

	
}