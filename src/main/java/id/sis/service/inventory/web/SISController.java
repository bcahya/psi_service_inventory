package id.sis.service.inventory.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import id.sis.service.inventory.pojo.RB_InventoryCharge;
import id.sis.service.inventory.response.SISResponse;
import id.sis.service.inventory.service.SISServiceImpl;

@Controller
@Configuration
public class SISController {

	@Autowired
	SISServiceImpl sisService;

	@ResponseBody
	@RequestMapping(value = "/test")
	SISResponse test() {
		SISResponse response = new SISResponse();
		try {
			response.setStatus("S");
			response.setMessage("Test Sukses!");
		} catch (Exception e) {
			response.setStatus("E");
			response.setMessage("Test Failed: " + e.getMessage());
			e.printStackTrace();
		}

		return response;
	}
	
	@ResponseBody
	@RequestMapping(value = "/calculateInventoryBook", method = RequestMethod.POST)
	SISResponse calculateInventoryBook(
			@RequestParam("m_inventory_id") Integer m_inventory_id
			) {
		SISResponse response = new SISResponse();

		try {
			response = sisService.calculateInventoryBook(
					m_inventory_id
					);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}
	
	@ResponseBody
	@RequestMapping(value = "/calculateInventoryCharge", method = RequestMethod.POST)
	SISResponse calculateInventoryCharge(
			@RequestBody RB_InventoryCharge param
			) {
		SISResponse response = new SISResponse();

		try {
			response = sisService.calculateInventoryCharge(
					param
					);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

	
	
}