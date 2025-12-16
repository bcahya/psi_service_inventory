package id.sis.service.inventory.utils;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import id.sis.service.inventory.properties.SISIdProperties;

@Component
public class SISUtil {
	
	private JdbcTemplate source;
	private SISIdProperties sisIdProperties;
	private PlatformTransactionManager transactionManager;

	public SISUtil(
			JdbcTemplate source,
			SISIdProperties sisIdProperties,
			PlatformTransactionManager transactionManager
		) {
		this.source = source;
		this.sisIdProperties = sisIdProperties;
		this.transactionManager = transactionManager;
	}
	
	public SISUtil() {
	}
	
	public String nullToString(String str) {
		if (str == null) {
			return "";
		}
		return str;
	}

	public static String addSingleQuote(String str) {
		if (str == null)
			return null;
		return String.format("'%s'", str.replace("'", "''"));
	}

	public static String addZero(int str, int total) {
		return String.format("%0" + total + "d", str);
	}

	public static boolean isEmptyString(String str) {
		if (str == null || "".equalsIgnoreCase(str)) {
			return true;
		}
		return false;
	}

	public static Date getDate(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date dates = null;
		try {
			dates = sdf.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dates;
	}
	
	public static Timestamp getTimestamp(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Timestamp dates = null;
		try {
			dates = new Timestamp(sdf.parse(date).getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dates;
	}
	
	public static Timestamp getDateyyMMdd(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		Timestamp dates = null;
		try {
			dates = new Timestamp(sdf.parse(date).getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dates;
	}

	public static String getStringDate(Timestamp date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(date);
	}

	public static String getStringDateDmy(Timestamp date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		return sdf.format(date);
	}

	public static String getStringPeriod(Timestamp date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		return sdf.format(date);
	}

	public Timestamp getCurrentTime() {
		return new Timestamp((new Date().getTime()));
	}

	public List<List<Map<String, Object>>> getSplitThreadData(int p_TotalThread, List<Map<String, Object>> listData) {
		int countMultiThreading = p_TotalThread;
		int tempCountAccount = 0;
		int splitter = listData.size() / countMultiThreading;

		List<List<Map<String, Object>>> listResult = new ArrayList<>();
		List<Map<String, Object>> listTemp = new ArrayList<>();
		for (Map<String, Object> mapData : listData) {
			if (tempCountAccount > splitter || tempCountAccount == 0) {
				tempCountAccount = 0;
				listTemp = new ArrayList<>();
				listResult.add(listTemp);
			}
			listResult.get(listResult.size() - 1).add(mapData);
			tempCountAccount += 1;
		}

		return listResult;
	}

	public List<List<String>> getSplitThreadDataString(int p_TotalThread, List<String> listData) {
		int countMultiThreading = p_TotalThread;
		int tempCountAccount = 0;
		int splitter = listData.size() / countMultiThreading;

		List<List<String>> listResult = new ArrayList<>();
		List<String> listTemp = new ArrayList<>();
		for (String data : listData) {
			if (tempCountAccount > splitter || tempCountAccount == 0) {
				tempCountAccount = 0;
				listTemp = new ArrayList<>();
				listResult.add(listTemp);
			}
			listResult.get(listResult.size() - 1).add(data);
			tempCountAccount += 1;
		}

		return listResult;
	}

	public List<List<Integer>> getSplitThreadDataInt(int p_TotalThread, List<Integer> listData) {
		int countMultiThreading = p_TotalThread;
		int tempCountAccount = 0;
		int splitter = listData.size() / countMultiThreading;

		List<List<Integer>> listResult = new ArrayList<>();
		List<Integer> listTemp = new ArrayList<>();
		for (int data : listData) {
			if (tempCountAccount > splitter || tempCountAccount == 0) {
				tempCountAccount = 0;
				listTemp = new ArrayList<>();
				listResult.add(listTemp);
			}
			listResult.get(listResult.size() - 1).add(data);
			tempCountAccount += 1;
		}

		return listResult;
	}
	
	public static BigDecimal getBigDecimal(Object value) {
		BigDecimal ret = new BigDecimal(0);
		if (value != null) {
			if (value instanceof BigDecimal) {
				ret = (BigDecimal) value;
			} else if (value instanceof String) {
				ret = new BigDecimal((String) value);
			} else if (value instanceof BigInteger) {
				ret = new BigDecimal((BigInteger) value);
			} else if (value instanceof Double) {
				ret = new BigDecimal((Double) value);
			} else if (value instanceof Number) {
				ret = new BigDecimal(((Number) value).doubleValue());
			} else if (value instanceof Integer) {
				ret = new BigDecimal(((Integer) value).doubleValue());
			} else {
				throw new ClassCastException("Not possible to coerce [" + value + "] from class " + value.getClass()
						+ " into a BigDecimal.");
			}
		}
		return ret;
	}

	public static BigDecimal getBigDecimal(Object value, int scale) {
		return getBigDecimal(value).setScale(scale, BigDecimal.ROUND_HALF_UP);
	}
	
	public static boolean cekIsNull (Object o) {
		if (o == null) {
			return true;
		}
		if (o instanceof Integer) {
			if ((int)o == 0) {
				return true;
			}
		}
		if (o instanceof BigDecimal) {
			if (getBigDecimal(o).signum() == 0) {
				return true;
			}
		}
		if (o instanceof String) {
			if (isEmptyString((String)o)) {
				return true;
			}
		}
		return false;
	}
	
	public static String getStringDashFromTimeStampTime(Timestamp date) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String result = sdf.format(date);
		return result;
	}
	
	public String getRefNoTime() {
		Timestamp date = getCurrentTime();
		if (date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String result = sdf.format(date);
		return result;
	}
	
	public int getNextSysID(
			String tableName
			) {
		int id = 0;
		String sql = 
			"select "
			+ "	nextidfunc(( "
			+ "	select "
			+ "		ad_sequence_id "
			+ "	from "
			+ "		ad_sequence "
			+ "	where "
			+ "		name = '"+tableName+"')::int, "
			+ "	'N')::int id "
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
	
	public Object getObject(
			String tableName,
			String colParam,
			String colName,
			Object value
			) {
		Object id = 0;
		String cols = "";
		if (value instanceof String) {
			cols += "'"+value+"'";
		} else {
			cols += value;
		}
		String[] colNames = colName.split(" ");
		String colKey = colNames[colNames.length-1];
		String sql = 
			"SELECT " + 
	        "    "+colName+" " + 
	        "FROM "+tableName+" "+ 
	        "WHERE ad_client_id = "+sisIdProperties.getAd_client_id()+" " + 
	        "AND "+colParam+" = "+cols+" " +
	        "and isactive = 'Y' "
	        ;
		List<Map<String, Object>> resultList = source.queryForList(sql);
		if (!resultList.isEmpty()) {
			for (Map<String, Object> map: resultList) {
				id = map.get(colKey.replace("::int", ""));
				break;
			}
		}
		return id;
	}
	
	@FunctionalInterface
	public interface ThrowingFunction<T, R, E extends Exception> {
	    R apply(T t) throws E;
	}
	
	public List<Integer> execDir(
			List<String> listErr, 
			String dirs, 
			ThrowingFunction<String, List<Integer>, Exception> action) throws Exception{
		List<Integer> listBSID = new ArrayList<>();
		String dirPath = dirs;
		if (SISUtil.cekIsNull(dirPath)) {
    		throw new Exception("Please setup Directory!");
    	}
		
		File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (files.length == 0) {
        	throw new Exception("file is empty!");
        }
        Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());

        for (File file: files) {
        	if (file.isDirectory()) {
        		continue;
        	}
        	String filePath = dirPath+file.getName();
        	DefaultTransactionDefinition def = new DefaultTransactionDefinition();
    		TransactionStatus status = transactionManager.getTransaction(def);
    		try {
        		List<Integer> listMT = action.apply(filePath);// exec bispro
        		listBSID.addAll(listMT);
        		transactionManager.commit(status);
        		
        		//move file to done
		        String dirDone = dirPath+"done/";
		        Path donePath = Paths.get(dirDone);
		        Files.createDirectories(donePath);
		        Path sourcePath = Paths.get(filePath);
		        Path targetPath = Paths.get(dirDone+file.getName());
		        Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
		        
        	} catch (Exception e) {
        		listErr.add("filename "+file.getName()+" - "+e.getMessage());
        		transactionManager.rollback(status);
			}
        }
        return listBSID;
	}
}
