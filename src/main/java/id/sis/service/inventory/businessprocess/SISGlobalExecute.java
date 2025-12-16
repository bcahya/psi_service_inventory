package id.sis.service.inventory.businessprocess;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import id.sis.service.inventory.pojo.RB_InventoryCharge;
import id.sis.service.inventory.pojo.RB_InventoryChargeBOM;
import id.sis.service.inventory.pojo.RB_InventoryChargeBOMMaterial;
import id.sis.service.inventory.pojo.RB_InventoryChargeDetail;
import id.sis.service.inventory.pojo.RB_MO;
import id.sis.service.inventory.pojo.RB_MOBOM;
import id.sis.service.inventory.pojo.RB_MOBOMLine;
import id.sis.service.inventory.pojo.RB_MOProduct;
import id.sis.service.inventory.pojo.RB_MOProductReplenish;
import id.sis.service.inventory.pojo.RB_MORouting;
import id.sis.service.inventory.pojo.RB_MOSOH;
import id.sis.service.inventory.pojo.RB_MOWH;
import id.sis.service.inventory.pojo.RB_Req;
import id.sis.service.inventory.pojo.RB_ReqLine;
import id.sis.service.inventory.properties.SISIdProperties;
import id.sis.service.inventory.response.SISResponse;
import id.sis.service.inventory.utils.SISConstants;
import id.sis.service.inventory.utils.SISUtil;
import id.sis.service.inventory.utils.SIS_BisproUtils;

@Component
public class SISGlobalExecute {
	private final static Logger logger = LoggerFactory.getLogger(SISGlobalExecute.class);
	List<Map<String, Object>> listData = new ArrayList<>();
	SIS_BisproUtils bu = new SIS_BisproUtils();
	String sql = "";
    
	@Autowired
	@Qualifier("jdbcTemplateCdcSource")
	private JdbcTemplate source;

	@Autowired
	private SISIdProperties sisIdProperties;
	
	@Autowired
    private PlatformTransactionManager transactionManager;

	SISUtil u = new SISUtil(source, sisIdProperties, transactionManager);
	

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

	//calculate MO
	//////////////////////////////////////////////
	List<LinkedHashMap<String, Object>> listMO = new ArrayList<>();
	LinkedHashMap<Integer, BigDecimal> mapReqs = new LinkedHashMap<>();
	List<Integer> listCekReq = new ArrayList<>();
	public SISResponse calculateRoutingMO(RB_MO rbmo) throws Exception {
		logger.info("[SIS] calculateRoutingMO mo_id : " + rbmo.getMo_id());
		listMO.clear();
		mapReqs.clear();
		listCekReq.clear();
		SISResponse response = new SISResponse();
		try {
			List<Map<String, Object>> resultList = new ArrayList<>();
			LinkedHashMap<String, Object> mapResult = new LinkedHashMap<String, Object>();
			mapResult.put("mo_id", rbmo.getMo_id());
			RB_MOBOM bomFG = null;
			for (RB_MOBOM bom: rbmo.getList_bom()) {
				if (bom.isIs_fg()) {
					bomFG = bom;
					break;
				}
			}
			if (bomFG == null) {
				throw new Exception("Please set bom FG!");
			}
			
			calculateRouting(rbmo, bomFG, null, rbmo.getWarehouse_id(), rbmo.getLocator_pre_id(), rbmo.getLocator_post_id(), rbmo.getQty());
			
			for (LinkedHashMap<String, Object> mapMO: listMO) {
				List<LinkedHashMap<String, Object>> listReq = (List<LinkedHashMap<String, Object>>)mapMO.get("list_req");
				for (LinkedHashMap<String, Object> mapReq: listReq) {
					List<LinkedHashMap<String, Object>> listReqLine = (List<LinkedHashMap<String, Object>>)mapReq.get("list_line");
					for (LinkedHashMap<String, Object> mapReqLine: listReqLine) {
						int productID = (int)mapReqLine.get("product_id");
						if (listCekReq.contains(productID)) {
							mapReqLine.put("qty", new BigDecimal(0));
							continue;
						}
						RB_MOProductReplenish pr = bu.getProductReplenish(rbmo.getList_replenish(), (int)mapReqLine.get("product_id"), (int)mapReq.get("warehouse_id"));
//						if (pr != null) {
//							BigDecimal qtyRep = pr.getMin();
//							if (pr.getMax().compareTo(qtyRep) > 0) {
//								qtyRep = pr.getMax();
//							}
//							if (qtyRep.compareTo(mapReqs.get(productID)) < 0){
//								qtyRep = mapReqs.get(productID);
//							}
//							mapReqLine.put("qty", qtyRep);
//						}
						mapReqLine.put("qty", mapReqs.get(productID));
						listCekReq.add(productID);
					}
				}
			}
			
			mapResult.put("list_mo", listMO);
			resultList.add(mapResult);
			response = SISResponse.successResponse(resultList);
		} catch (Exception e) {
			response = SISResponse.errorResponse(e.getMessage());
		}
		return response;
	}
	
	void calculateRouting(
			RB_MO rbmo,
			RB_MOBOM bom,
			RB_MOBOM bomSource,
			int warehouseID,
			int preLocatorID,
			int postLocatorID,
			BigDecimal qty
			) throws Exception {
		if (preLocatorID == 0) {
			throw new Exception("Please setup pre locator for bom id "+bom.getBom_id());
		}
		if (postLocatorID == 0) {
			throw new Exception("Please setup post locator for bom id "+bom.getBom_id());
		}
		LinkedHashMap<String, Object> mapMO = new LinkedHashMap<>();
		mapMO.put("bom_id", bom.getBom_id());
		mapMO.put("bom_source_id", 0);
		mapMO.put("warehouse_id", 0);
		mapMO.put("qty", new BigDecimal(0));
		mapMO.put("list_line", new ArrayList<>());
		mapMO.put("list_move", new ArrayList<>());
		mapMO.put("list_req", new ArrayList<>());
		if (bomSource != null) {
			mapMO.put("bom_source_id", bomSource.getBom_id());
		}
		if (warehouseID > 0) {
			mapMO.put("warehouse_id", warehouseID);
		}
		if (qty != null) {
			mapMO.put("qty", qty);
		}
		
		List<LinkedHashMap<String, Object>> listLine = (List<LinkedHashMap<String, Object>>) mapMO.get("list_line");
		List<LinkedHashMap<String, Object>> listMove = (List<LinkedHashMap<String, Object>>) mapMO.get("list_move");
		List<LinkedHashMap<String, Object>> listReq = (List<LinkedHashMap<String, Object>>) mapMO.get("list_req");
		
		int seqMove = 100000;
		int seqSC = 100000;
		for (RB_MOBOMLine bomLine: bom.getList_line()) {
			RB_MOProduct product = bu.getProduct(rbmo.getList_product(), bomLine.getProduct_id());
			if (product == null) {
				throw new Exception("Product id "+bomLine.getProduct_id()+" not in bom id "+bom.getBom_id());
			}
			List<RB_MORouting> listProductRouting = bu.getListProductRouting(rbmo.getList_routing(),
					rbmo.getList_product(), bomLine.getProduct_id());
			BigDecimal qtyLine = bomLine.getQty().multiply(qty);
			
			LinkedHashMap<String, Object> mapLine = new LinkedHashMap<>();
			mapLine.put("product_id", bomLine.getProduct_id());
			mapLine.put("qty", qtyLine);
			listLine.add(mapLine);
			
			int locSearchID = preLocatorID;
			int routingLineID = 0;
			boolean isFrom = false;
			boolean isSC = false;
			if (bom.getBomtype().equalsIgnoreCase("SC")) {
				isSC = true;
			}
			if (isSC) {
//				isFrom = true;
				RB_MOWH wh = bu.getWarehouse(rbmo.getList_wh(), bom.getWarehouse_id());
				locSearchID = wh.getLocator_stock_id();
				if (product.isIs_bom()) {
					RB_MOBOM bomSC = bu.getBOM(rbmo.getList_bom(), bom.getBom_id(), product.getProduct_id());
					if (!bomSC.getBomtype().equalsIgnoreCase("SC")) {
						wh = bu.getWarehouse(rbmo.getList_wh(), bomSC.getWarehouse_id());
						locSearchID = wh.getLocator_pre_id();
					}
				}
			}
			int counter = 0;
			while(true) {
				counter += 1;
				if (counter > 15) {
					throw new Exception("Routing never ending loop!");
				}
				RB_MORouting routing = null;
				routing = bu.getNextRouting(listProductRouting, locSearchID, isFrom);
				if (routing == null) {
					break;
				}
				if (routing.getAction().equalsIgnoreCase(SISConstants.MO_ROUTING_ACTION_PUSHTO)) {
					locSearchID = routing.getLocatorto_id();
					isFrom = true;
				} else if (routing.getAction().equalsIgnoreCase(SISConstants.MO_ROUTING_ACTION_PULLFROM)) {
					isFrom = false;
					locSearchID = routing.getLocatorfrom_id();
				}
				if (routingLineID == routing.getRoutingline_id()) {
					break;
				}
				routingLineID = routing.getRoutingline_id();
				if (routing.getAction().equalsIgnoreCase(SISConstants.MO_ROUTING_ACTION_PUSHTO)
						&& !bom.getBomtype().equalsIgnoreCase("SC")) {
					throw new Exception("Routing Line ID "+routing.getRoutingline_id()+" not allowed to set action PUSH TO!");
				}
				if (routing.getOperation_type().equalsIgnoreCase(SISConstants.MO_ROUTING_OPERATION_TAKEFROMSTOCK)
						|| routing.getOperation_type().equalsIgnoreCase(SISConstants.MO_ROUTING_OPERATION_TAKEFROMSTOCKTRIGGERANOTHERRULE)) {
					BigDecimal qtySOH = new BigDecimal(0);
					RB_MOSOH soh = bu.getSOH(rbmo.getList_soh(), product.getProduct_id(), locSearchID);
					if (soh != null) {
						qtySOH = soh.getQty();
					}
					if (routing.getAction().equalsIgnoreCase(SISConstants.MO_ROUTING_ACTION_PULLFROM)) {
						BigDecimal qtyDiff = qtySOH.subtract(qtyLine);
						if (soh != null) {
							soh.setQty(qtyDiff);
						}
						if (qtyDiff.signum() >= 0) {
							seqMove = bu.generateMove(listMove, routing, bomLine.getProduct_id(), qtyLine, false, seqMove);
							break;
						} else {
							seqMove = bu.generateMove(listMove, routing, bomLine.getProduct_id(), qtyLine, false, seqMove);
						}
					} else {
						seqMove = bu.generateMove(listMove, routing, bomLine.getProduct_id(), qtyLine, false, seqMove);
					}
				}
				if (routing.getAction().equalsIgnoreCase(SISConstants.MO_ROUTING_ACTION_BUY)) {
//					BigDecimal qtySOH = new BigDecimal(0);
//					RB_MOSOH soh = bu.getSOH(rbmo.getList_soh(), product.getProduct_id(), locSearchID);
//					if (soh != null) {
//						qtySOH = soh.getQty();
//					}
//					BigDecimal qtyDiff = qtyLine;
//					if (qtySOH.signum() > 0) {
//						qtyDiff = qtySOH.subtract(qtyLine);
//						qtySOH = qtySOH.subtract(qtyLine);
//					}
//					bu.generateReq(mapReqs, listReq, rbmo.getList_product(), routing.getWarehouseto_id(), bomLine.getProduct_id(), qtyDiff.abs());
//					if (soh != null) {
//						soh.setQty(qtySOH);
//					}
					
					BigDecimal qtySOH = new BigDecimal(0);
					RB_MOSOH soh = bu.getSOH(rbmo.getList_soh(), product.getProduct_id(), locSearchID);
					if (soh != null) {
						qtySOH = soh.getQty();
					}
					if (qtySOH.signum() < 0) {
						bu.generateReq(mapReqs, listReq, rbmo.getList_product(), routing.getWarehouseto_id(), bomLine.getProduct_id(), qtySOH.abs());
						soh.setQty(new BigDecimal(0));
					}
					if (isSC) {
						RB_MOWH wh = bu.getWarehouse(rbmo.getList_wh(), bom.getWarehouse_id());
						seqSC = generateMoveSC(listMove, rbmo.getList_wh(), wh, rbmo.getList_bom(), bom, product,
								listProductRouting, qtyLine, seqSC);
					}
					break;
				}
				if (routing.getAction().equalsIgnoreCase(SISConstants.MO_ROUTING_ACTION_MANUFACTURE)) {
					if (!product.isIs_bom()
							&& routing.getAction().equalsIgnoreCase(SISConstants.MO_ROUTING_ACTION_MANUFACTURE)) {
						throw new Exception("product id "+product.getProduct_id()+" doesn't have bom!");
					}
					RB_MOBOM bomSrc = bom;
					RB_MOBOM bomTo = bu.getBOM(rbmo.getList_bom(), bomSrc.getBom_id(), bomLine.getProduct_id());
					if (bomTo == null) {
						throw new Exception("BOM for product id "+product.getProduct_id()+" is missing!");
					}
					RB_MOWH wh = bu.getWarehouse(rbmo.getList_wh(), bomTo.getWarehouse_id());
					calculateRouting(rbmo, bomTo, bomSrc, wh.getWarehouse_id(), wh.getLocator_pre_id(), wh.getLocator_post_id(), qtyLine);
					if (isSC) {
						RB_MOWH whf = bu.getWarehouse(rbmo.getList_wh(), bom.getWarehouse_id());
						seqSC = generateMoveSC(listMove, rbmo.getList_wh(), whf, rbmo.getList_bom(), bom, product,
								listProductRouting, qtyLine, seqSC);
					}
					break;
				}
				
			}
		}
		
		int routingLineID = 0;
		int locSearchID = postLocatorID;
		int counter = 0;
		List<RB_MORouting> listProductRouting = bu.getListProductRouting(rbmo.getList_routing(),
				rbmo.getList_product(), bom.getProduct_id());
		boolean isFrom = true;
		seqMove = 150000;
		while(true) {
			counter += 1;
			if (counter > 15) {
				throw new Exception("Routing never ending loop!");
			}
			RB_MORouting routing = bu.getNextRouting(listProductRouting, locSearchID, isFrom, "PT");
			if (routing == null
					|| (routing.getIs_subcont().equalsIgnoreCase("Y")
							&& !rbmo.getIs_subcont().equalsIgnoreCase("Y"))
					) {
				break;
			}
			if (routing.getAction().equalsIgnoreCase(SISConstants.MO_ROUTING_ACTION_PUSHTO)
					|| routing.getAction().equalsIgnoreCase(SISConstants.MO_ROUTING_ACTION_MANUFACTURE)) {
				isFrom = true;
				locSearchID = routing.getLocatorto_id();
			} else if (routing.getAction().equalsIgnoreCase(SISConstants.MO_ROUTING_ACTION_PULLFROM)) {
//				isFrom = false;
//				locSearchID = routing.getLocatorfrom_id();
				break;
			}
			if (routingLineID == routing.getRoutingline_id()) {
				break;
			}
			routingLineID = routing.getRoutingline_id();
			locSearchID = routing.getLocatorto_id();
			if (routing.getAction().equalsIgnoreCase(SISConstants.MO_ROUTING_ACTION_PUSHTO)
					|| routing.getAction().equalsIgnoreCase(SISConstants.MO_ROUTING_ACTION_PULLFROM)) {
				seqMove = bu.generateMove(listMove, routing, bom.getProduct_id(), qty, true, seqMove);
			}
		}
		
//		List<LinkedHashMap<String, Object>> listGroupMove = groupingMove(listMove);
//		mapMO.put("list_move", listGroupMove);
		listMO.add(mapMO);
	}
	
	
	public int generateMoveSC(
			List<LinkedHashMap<String, Object>> listMove,
			List<RB_MOWH> listWH,
			RB_MOWH wh,
			List<RB_MOBOM> listBOM,
			RB_MOBOM bom,
			RB_MOProduct product,
			List<RB_MORouting> listProductRouting,
			BigDecimal qtyLine,
			int seqMove
			) throws Exception {
		boolean isFrom = true;
		int locSearchID = wh.getLocator_stock_id();
		if (product.isIs_bom()) {
			RB_MOBOM bomSC = bu.getBOM(listBOM, bom.getBom_id(), product.getProduct_id());
			if (!bomSC.getBomtype().equalsIgnoreCase("SC")) {
				wh = bu.getWarehouse(listWH, bomSC.getWarehouse_id());
				locSearchID = wh.getLocator_post_id();
			}
		}
		int routingPushLineID = 0;
		int counter = 0;
		while(true) {
			counter += 1;
			if (counter > 15) {
				throw new Exception("Routing never ending loop!");
			}
			RB_MORouting routingPush = bu.getNextRouting(listProductRouting, locSearchID, isFrom, "PT");
			if (routingPush == null) {
				break;
			}
			if (routingPush.getAction().equalsIgnoreCase(SISConstants.MO_ROUTING_ACTION_PUSHTO)
					|| routingPush.getAction().equalsIgnoreCase(SISConstants.MO_ROUTING_ACTION_MANUFACTURE)) {
				isFrom = true;
				locSearchID = routingPush.getLocatorto_id();
			} else if (routingPush.getAction().equalsIgnoreCase(SISConstants.MO_ROUTING_ACTION_PULLFROM)) {
//				isFrom = false;
//				locSearchID = routingPush.getLocatorfrom_id();
				break;
			}
			if (routingPushLineID == routingPush.getRoutingline_id()) {
				break;
			}
			routingPushLineID = routingPush.getRoutingline_id();
			locSearchID = routingPush.getLocatorto_id();
			if (routingPush.getAction().equalsIgnoreCase(SISConstants.MO_ROUTING_ACTION_PUSHTO)
					|| routingPush.getAction().equalsIgnoreCase(SISConstants.MO_ROUTING_ACTION_PULLFROM)) {
				seqMove = bu.generateMove(listMove, routingPush, product.getProduct_id(), qtyLine, true, seqMove);
			}
		}
		return seqMove;
	}
	///////////////////////////////////////////////////
	
	
	//calculate Requisition Subcont
	//////////////////////////////////////////////
	public SISResponse calculateReqSubcont(RB_Req rbReq) throws Exception {
		logger.info("[SIS] calculateReqSubcont po_id : " + rbReq.getPo_id());
		mapReqs.clear();
		listCekReq.clear();
		SISResponse response = new SISResponse();
		try {
			List<Map<String, Object>> resultList = new ArrayList<>();
			List<LinkedHashMap<String, Object>> listReq = new ArrayList<>();
			List<LinkedHashMap<String, Object>> listMove = new ArrayList<>();
			List<LinkedHashMap<String, Object>> listPOP = new ArrayList<>();
			
			for (RB_ReqLine rbRL: rbReq.getList_line()) {
				Map<String, Object> mapResult = new LinkedHashMap<>();
				boolean isExistReq = false;
				for (Map<String, Object> mapR: resultList) {
					int reqID = (int)mapR.get("requisition_id");
					if (rbRL.getRequisition_id() == reqID) {
						isExistReq = true;
						mapResult = mapR;
						break;
					}
				}
				
				if (mapResult.isEmpty()) {
					mapResult.put("requisition_id", rbRL.getRequisition_id());
					mapResult.put("list_req", new ArrayList<>());
					mapResult.put("list_move", new ArrayList<>());
					mapResult.put("list_pop", new ArrayList<>());
				}
				listReq = (List<LinkedHashMap<String, Object>>)mapResult.get("list_req");
				listMove = (List<LinkedHashMap<String, Object>>)mapResult.get("list_move");
				listPOP = (List<LinkedHashMap<String, Object>>)mapResult.get("list_pop");
				
				BigDecimal qty = rbRL.getQty();
				int locSearchID = rbRL.getLocator_id();
				RB_MOProduct product = bu.getProduct(rbReq.getList_product(), rbRL.getProduct_id());
				
				//generate movement
				if (product.isIs_bom()) {
					int seqMove = 99900;
					RB_MOBOM bom = bu.getBOM(rbReq.getList_bom(), rbRL.getBom_id());
					if (bom.getBomtype().equalsIgnoreCase("SC")) {
						for (RB_MOBOMLine bl: bom.getList_line()) {
							BigDecimal qtyLine = qty.multiply(bl.getQty());
							int routingLineID = 0;
							locSearchID = rbRL.getLocator_id();
							int counter = 0;
							RB_MOProduct productLine = bu.getProduct(rbReq.getList_product(), bl.getProduct_id());
							List<RB_MORouting> listProductRouting = bu.getListProductRouting(rbReq.getList_routing(),
									rbReq.getList_product(), bl.getProduct_id());
							boolean isFrom = true;
							
							if (productLine.isIs_bom()) {
								RB_MOBOM bomRef = bu.getBOM(rbReq.getList_bom(), bom.getBom_id(), bl.getProduct_id());
								if (!bomRef.getBomtype().equalsIgnoreCase("SC")) {
									continue;
								}
							}
							
							while(true) {
								counter += 1;
								if (counter > 15) {
									throw new Exception("Routing never ending loop!");
								}
								RB_MORouting routing = bu.getNextRouting(listProductRouting, locSearchID, isFrom, "PT");
								if (routing == null) {
									break;
								}
								if (routing.getAction().equalsIgnoreCase(SISConstants.MO_ROUTING_ACTION_PUSHTO)
										|| routing.getAction().equalsIgnoreCase(SISConstants.MO_ROUTING_ACTION_MANUFACTURE)) {
									isFrom = true;
									locSearchID = routing.getLocatorto_id();
								} else if (routing.getAction().equalsIgnoreCase(SISConstants.MO_ROUTING_ACTION_PULLFROM)) {
			//						isFrom = false;
			//						locSearchID = routing.getLocatorfrom_id();
									break;
								}
								if (routingLineID == routing.getRoutingline_id()) {
									break;
								}
								routingLineID = routing.getRoutingline_id();
								locSearchID = routing.getLocatorto_id();
								if (routing.getAction().equalsIgnoreCase(SISConstants.MO_ROUTING_ACTION_PUSHTO)
										|| routing.getAction().equalsIgnoreCase(SISConstants.MO_ROUTING_ACTION_PULLFROM)) {
									seqMove = bu.generateMove(listMove, routing, productLine.getProduct_id(), qtyLine, true, seqMove);
								}
							}
						}
					}
				}
				
				//generate Requisition Components
				if (product.isIs_bom()) {
					RB_MOBOM bom = bu.getBOM(rbReq.getList_bom(), rbRL.getBom_id());
					if (bom.getBomtype().equalsIgnoreCase("SC")) {
						for (RB_MOBOMLine bl: bom.getList_line()) {
							BigDecimal qtyLine = qty.multiply(bl.getQty());
							RB_MOProduct prd = bu.getProduct(rbReq.getList_product(), bl.getProduct_id());
							boolean isReq = false;
							if (!prd.isIs_bom()) {
								isReq = true;
							} else {
								RB_MOBOM bomRef = bu.getBOM(rbReq.getList_bom(), bom.getBom_id(), prd.getProduct_id());
								if (bomRef.getBomtype().equalsIgnoreCase("SC")) {
									isReq = true;
								}
							}
							if (isReq) {
								BigDecimal qtySOH = new BigDecimal(0);
								RB_MOSOH soh = bu.getSOH(rbReq.getList_soh(), prd.getProduct_id(), rbRL.getLocator_id());
								if (soh != null) {
									qtySOH = soh.getQty();
								}
								BigDecimal qtyDiff = qtySOH.subtract(qtyLine);
								if (soh != null) {
									soh.setQty(qtyDiff);
								}
								if (qtyDiff.signum() < 0) {
									generateReqSC(rbReq, bl.getProduct_id(), locSearchID, rbRL.getWarehouse_id(), qtyDiff.abs(), listReq);
									soh.setQty(new BigDecimal(0));
								}
							} else {
								LinkedHashMap<String, Object> mapPOP = new LinkedHashMap<String, Object>();
								boolean isExistPOP = false;
								for (LinkedHashMap<String, Object> mapP: listPOP) {
									int productID = (int)mapP.get("product_id");
									int bomID = (int)mapP.get("bom_id");
									if (productID == prd.getProduct_id()
											&& bomID == rbRL.getBom_id()) {
										mapPOP = mapP;
										isExistPOP = true;
										break;
									}
								}
								if (mapPOP.isEmpty()) {
									RB_MOBOM b = bu.getBOM(rbReq.getList_bom(), rbRL.getBom_id(), prd.getProduct_id());
									mapPOP.put("product_id", prd.getProduct_id());
									mapPOP.put("bom_id", b.getBom_id());
									mapPOP.put("qty", new BigDecimal(0));
								}
								mapPOP.put("qty", ((BigDecimal)mapPOP.get("qty")).add(qtyLine));
								if (!isExistPOP) {
									listPOP.add(mapPOP);
								}
							}
						}
					}
				}
				
				if (!isExistReq) {
					resultList.add(mapResult);
				}
			}
			
			for (Map<String, Object> mapResult: resultList) {
				listReq = (List<LinkedHashMap<String, Object>>)mapResult.get("list_req");
				for (LinkedHashMap<String, Object> mapReq: listReq) {
					List<LinkedHashMap<String, Object>> listReqLine = (List<LinkedHashMap<String, Object>>)mapReq.get("list_line");
					for (LinkedHashMap<String, Object> mapReqLine: listReqLine) {
						int productID = (int)mapReqLine.get("product_id");
						if (listCekReq.contains(productID)) {
							mapReqLine.put("qty", new BigDecimal(0));
							continue;
						}
//						RB_MOProductReplenish pr = bu.getProductReplenish(rbReq.getList_replenish(), (int)mapReqLine.get("product_id"), (int)mapReq.get("warehouse_id"));
//						if (pr != null) {
//							BigDecimal qtyRep = pr.getMin();
//							if (pr.getMax().compareTo(qtyRep) > 0) {
//								qtyRep = pr.getMax();
//							}
//							if (qtyRep.compareTo(mapReqs.get(productID)) < 0){
//								qtyRep = mapReqs.get(productID);
//							}
//							mapReqLine.put("qty", qtyRep);
//						}
						listCekReq.add(productID);
					}
				}
			}
			response = SISResponse.successResponse(resultList);
		} catch (Exception e) {
			response = SISResponse.errorResponse(e.getMessage());
		}
		return response;
	}
	
	public void generateReqSC(
			RB_Req rbReq,
			int productID,
			int locID,
			int warehouseID,
			BigDecimal qty,
			List<LinkedHashMap<String, Object>> listReq
			) {
		BigDecimal qtySOH = new BigDecimal(0);
		RB_MOSOH soh = bu.getSOH(rbReq.getList_soh(), productID, locID);
		if (soh != null) {
			qtySOH = soh.getQty();
		}
		BigDecimal qtyDiff = qty;
		if (qtySOH.signum() > 0) {
			qtyDiff = qtySOH.subtract(qty);
			qtySOH = qtySOH.subtract(qty);
		}
		bu.generateReq(mapReqs, listReq, rbReq.getList_product(), warehouseID, productID, qtyDiff.abs());
		if (soh != null) {
			soh.setQty(qtySOH);
		}
	}
	//////////////////////////////////////////////////
	
	public SISResponse importTrans() throws Exception {
		logger.info("[SIS] importTrans");
		u = new SISUtil(source, sisIdProperties, transactionManager);
		SISResponse response = new SISResponse();
		List<Map<String, Object>> resultList = new ArrayList<>();
		try {
			
			List<String> listErr = new ArrayList<>();
			List<Integer> listpoID = new ArrayList<>();
			try {
				List<Integer> listID = u.execDir(listErr, sisIdProperties.getDir_po(), dirs -> {
					return generatePO(dirs);
				});
				
				Set<Integer> uniqueSet = new HashSet<>(listID);
				listpoID = new ArrayList<>(uniqueSet);
				
				for (int poID: listpoID) {
					sql =
							"with t1 as ( "
							+ "	select "
							+ "		ol.c_orderline_id, "
							+ "		ol.c_order_id, "
							+ "		pl.istaxincluded, "
							+ "		ol.linenetamt, "
							+ "		sis_gettaxamt(ol.c_tax_id, ol.linenetamt, pl.istaxincluded) taxamt "
							+ "	from c_orderline ol "
							+ "	inner join c_order o "
							+ "		on o.c_order_id = ol.c_order_id "
							+ "	inner join m_pricelist pl "
							+ "		on pl.m_pricelist_id = o.m_pricelist_id "
							+ "	where ol.c_order_id = ? "
							+ "), t2 as ( "
							+ "	select "
							+ "		t1.c_order_id, "
							+ "		sum( "
							+ "			case "
							+ "				when t1.istaxincluded = 'N' "
							+ "				then t1.linenetamt + t1.taxamt "
							+ "				else t1.linenetamt "
							+ "			end "
							+ "		) grandtotal, "
							+ "		sum(t1.linenetamt) totallines "
							+ "	from t1 "
							+ "	group by "
							+ "		t1.c_order_id "
							+ ") "
							+ "update c_order "
							+ "	set grandtotal = t2.grandtotal, "
							+ "	totallines = t2.totallines "
							+ "from t2 "
							+ "where t2.c_order_id = c_order.c_order_id ";
					int rowAffected = source.update(
							sql,
							poID
					);
				}
			} catch (Exception e) {
				System.out.println("generate PO error "+e.getMessage());
			}
			
	        Map<String, Object> map = new LinkedHashMap<String, Object>();
	        map.put("list_po_id", listpoID);
	        map.put("list_error", listErr);
	        resultList.add(map);
			response = SISResponse.successResponse(resultList);
		} catch (Exception e) {
			response = SISResponse.errorResponse(e.getMessage());
		}
		return response;
	}
	
	BigDecimal docCount = SISUtil.getBigDecimal(u.getRefNoTime());
	List<Integer> generatePO(String filePath){
		u = new SISUtil(source, sisIdProperties, transactionManager);
		List<Integer> listID = new ArrayList<>();
		String csvFile = filePath; // Ensure this file exists
        String line = "";
        String csvDelimiter = ","; // Use the correct delimiter
        
        String[] files = filePath.split("/");
        String fileName = files[files.length-1].replace(".csv", "");
        String[] fileNames = fileName.split("_");
        int ad_org_id = (int)u.getObject("ad_org", "value", "ad_org_id::int", fileNames[0]);
        int m_product_id = (int)u.getObject("m_product", "value", "m_product_id::int", fileNames[1]);
        String period = (String)fileNames[2];
        
        Timestamp now = u.getCurrentTime();
        HashMap<String, Integer> mapCol = new HashMap<>();
        mapCol.put("wh", 0);
        mapCol.put("price", 1);
        mapCol.put("bp", 2);
        mapCol.put("tax", 3);
        mapCol.put("date", 4);
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String headerLine = br.readLine();
            if (headerLine != null) {
                System.out.println("Headers: " + String.join(", ", headerLine.split(csvDelimiter)));
            }
            
            // Read data lines
            while ((line = br.readLine()) != null) {
                String[] values = line.split(csvDelimiter);
                int whID = (int)u.getObject("m_warehouse", "value", "m_warehouse_id::int", values[mapCol.get("wh")]);
                BigDecimal price = SISUtil.getBigDecimal(values[mapCol.get("price")]);
                int bpID = (int)u.getObject("c_bpartner", "value", "c_bpartner_id::int", values[mapCol.get("bp")]);
                int taxID = (int)u.getObject("c_tax", "name", "c_tax_id::int", values[mapCol.get("tax")]);
                int bpLocID = (int)u.getObject("c_bpartner_location", "c_bpartner_id", "c_bpartner_location_id::int", bpID);
                int day = 0;
                for (int a=mapCol.get("date"); a<values.length; a++) {
                	day += 1;
                	String date = period + SISUtil.addZero(day, 2);
                	if (values[a] == null
                			|| values[a].equalsIgnoreCase("")) {
                		continue;
                	}
                	
                	BigDecimal qty = SISUtil.getBigDecimal(values[a]);
                    Timestamp dateordered = SISUtil.getTimestamp(date);
                	int c_order_id = getOrderID(date, ad_org_id, sisIdProperties.getPo_doctype_id(), bpID, whID, "N");
                	if (c_order_id <= 0) {
                		docCount = docCount.add(new BigDecimal(1));
                		c_order_id = u.getNextSysID("C_Order");
    	            	sql =
                    		"insert into c_order ( "
                    		+ "	c_order_id, "
                    		+ "	ad_client_id, "
                    		+ "	ad_org_id, "
                    		+ "	issotrx, "
                    		+ "	sis_status_docno, "
                    		+ "	documentno, "
                    		+ "	c_doctypetarget_id, "
                    		+ "	dateacct, "
                    		+ "	dateordered, "
                    		+ "	datepromised, "
                    		+ "	c_bpartner_id, "
                    		+ "	c_bpartner_location_id, "
                    		+ "	ad_user_id, "
                    		+ "	m_warehouse_id, "
                    		+ "	deliveryviarule, "
                    		+ "	priorityrule, "
                    		+ "	m_pricelist_id, "
                    		+ "	c_currency_id, "
                    		+ "	salesrep_id, "
                    		+ "	paymentrule, "
                    		+ "	invoicerule, "
                    		+ "	deliveryrule, "
                    		+ "	freightcostrule, "
                    		+ "	docstatus, "
                    		+ "	docaction, "
                    		+ "	c_doctype_id, "
                    		+ "	c_paymentterm_id, "
                    		+ "	c_order_uu, "
                    		+ "	created, "
                    		+ "	updated, "
                    		+ "	createdby, "
                    		+ "	updatedby "
                    		+ ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
                        int rowsAffected = source.update(
                                sql, 
                                c_order_id, 
                                sisIdProperties.getAd_client_id(), 
                                ad_org_id, 
                                "N",
                                "R",
                                String.valueOf(docCount),
                                sisIdProperties.getPo_doctype_id(),
                                dateordered,
                                dateordered,
                                dateordered,
                                bpID,
                                bpLocID,
                                sisIdProperties.getAd_user_id(),
                                whID,
                                "P",
                                "5",
                                sisIdProperties.getPo_pricelist_id(),
                                sisIdProperties.getC_currency_id(),
                                sisIdProperties.getAd_user_id(),
                                "B",
                                "D",
                                "A",
                                "I",
                                "DR",
                                "CO",
                                0,
                                sisIdProperties.getPo_paymentterm_id(),
                                UUID.randomUUID(),
                                now,
                                now,
                                sisIdProperties.getAd_user_id(),
                                sisIdProperties.getAd_user_id()
                            );
                	}
                	
                	int c_orderline_id = getOrderLineID(c_order_id, m_product_id, taxID, qty, price);
                	if (c_orderline_id <= 0) {
                		if (!listID.contains(c_order_id)) {
                			listID.add(c_order_id);
                		}
                    	int lineNo = ((int)u.getObject("c_orderline", "c_order_id", "coalesce((max(line)),0)::int lineno", c_order_id))+10;
                    	int c_uom_id = (int)u.getObject("m_product", "m_product_id", "c_uom_id::int", m_product_id);
                		c_orderline_id = u.getNextSysID("C_OrderLine");
    	            	sql =
                    		"insert into c_orderline ( "
                    		+ "	c_orderline_id, "
                    		+ "	ad_client_id, "
                    		+ "	ad_org_id, "
                    		+ "	c_order_id, "
                    		+ "	c_bpartner_id, "
                    		+ "	c_bpartner_location_id, "
                    		+ "	datepromised, "
                    		+ "	dateordered, "
                    		+ "	line, "
                    		+ "	m_product_id, "
                    		+ "	qtyentered, "
                    		+ "	c_uom_id, "
                    		+ "	qtyordered, "
                    		+ "	priceentered, "
                    		+ "	priceactual, "
                    		+ "	c_tax_id, "
                    		+ "	m_warehouse_id, "
                    		+ "	c_currency_id, "
                    		+ "	linenetamt, "
                    		+ "	c_orderline_uu, "
                    		+ "	created, "
                    		+ "	updated, "
                    		+ "	createdby, "
                    		+ "	updatedby "
                    		+ ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
                        int rowsAffected = source.update(
                                sql, 
                                c_orderline_id, 
                                sisIdProperties.getAd_client_id(), 
                                ad_org_id, 
                                c_order_id,
                                bpID,
                                bpLocID,
                                dateordered,
                                dateordered,
                                lineNo,
                                m_product_id,
                                qty,
                                c_uom_id,
                                qty,
                                price,
                                price,
                                taxID,
                                whID,
                                sisIdProperties.getC_currency_id(),
                                qty.multiply(price),
                                UUID.randomUUID(),
                                now,
                                now,
                                sisIdProperties.getAd_user_id(),
                                sisIdProperties.getAd_user_id()
                            );
                	}
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		return listID;
	}
	
	int getOrderID(
			String dateordered,
			int ad_org_id,
			int c_doctype_id,
			int c_bpartner_id,
			int m_warehouse_id,
			String issotrx
			) {
		int id = 0;
		String sql = 
			"select "
			+ "	o.c_order_id::int id "
			+ "from c_order o "
			+ "where o.docstatus in ('DR') "
			+ "and o.ad_client_id = "+sisIdProperties.getAd_client_id()+" "
			+ "and trunc(o.dateordered) = trunc('"+dateordered+"'::date) "
			+ "and o.ad_org_id = "+ad_org_id+" "
			+ "and o.c_doctypetarget_id = "+c_doctype_id+" "
			+ "and o.c_bpartner_id = "+c_bpartner_id+" "
			+ "and o.m_warehouse_id = "+m_warehouse_id+" "
			+ "and o.issotrx = '"+issotrx+"' "
	        ;
		List<Map<String, Object>> resultList = source.queryForList(sql);
		if (!resultList.isEmpty()) {
			for (Map<String, Object> map: resultList) {
				id = (int)map.get("id");
				break;
			}
		}
		return id;
	}
	
	int getOrderLineID(
			int c_order_id,
			int m_product_id,
			int c_tax_id,
			BigDecimal qty,
			BigDecimal price
			) {
		int id = 0;
		String sql = 
			"select "
			+ "	ol.c_orderline_id::int id "
			+ "from c_orderline ol "
			+ "where ol.c_order_id = "+c_order_id+" "
			+ "and ol.isactive = 'Y' "
			+ "and ol.m_product_id = "+m_product_id+" "
			+ "and ol.c_tax_id = "+c_tax_id+" "
			+ "and ol.qtyentered = "+qty+" "
	        + "and ol.priceentered = "+price+" "
	        ;
		List<Map<String, Object>> resultList = source.queryForList(sql);
		if (!resultList.isEmpty()) {
			for (Map<String, Object> map: resultList) {
				id = (int)map.get("id");
				break;
			}
		}
		return id;
	}
	
}