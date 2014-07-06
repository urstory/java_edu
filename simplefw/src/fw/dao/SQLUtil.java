package fw.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLUtil {
	public static String[] getProperties(String sql) {
		Pattern p = Pattern.compile("#[a-zA-Z0-9]+#");
		Matcher m = p.matcher(sql);
		List<String> list = new ArrayList<String>();
		
		while (m.find()) {
			list.add(m.group());
		}
		return list.toArray(new String[list.size()]);
	}
	
	public static String[] changeGetMethods(String[] properties){
		String[] methods = new String[properties.length];
		for(int i = 0; i < properties.length; i++){
			String method = properties[i].substring(1, properties[i].length() -1);
			if(method.length() > 1){
				method = "get" + method.substring(0,1).toUpperCase() + method.substring(1);
				methods[i] = method;
			}
		}
		return methods;
	}
	
	public static String[] changeSetMethods(String[] properties){
		String[] methods = new String[properties.length];
		for(int i = 0; i < properties.length; i++){
			String method = properties[i].substring(1, properties[i].length() -1);
			if(method.length() > 1){
				method = "set" + method.substring(0,1).toUpperCase() + method.substring(1);
				methods[i] = method;
			}
		}
		return methods;		
	}	
	
	public static String changePreparedSql(String sql, String[] properties){
		String psql = sql;
		
		for (int i = 0; i < properties.length; i++ ){
			psql = psql.replace(properties[i], "?");
		}
		
		return psql;
	}

	public static void main(String args[]) {
		String sql = "insert into guestbook(id, name, password, content, ip, regdate) values (guestbook_seq.NEXTVAL, #name#, #password#, #content#, #ip#, sysdate)";
		String[] properties = SQLUtil.getProperties(sql);
		String[] getters = changeGetMethods(properties);
		String[] setters = changeSetMethods(properties);
		
		for(int i = 0; i < properties.length; i++){
			System.out.println(properties[i]);
		}
		System.out.println("-------------------------------------");		
		for(int i = 0; i < getters.length; i++){
			System.out.println(getters[i]);
		}
		System.out.println("-------------------------------------");
		for(int i = 0; i < setters.length; i++){
			System.out.println(setters[i]);
		}		
		System.out.println("-------------------------------------");
		System.out.println(changePreparedSql(sql, properties));
	}
}