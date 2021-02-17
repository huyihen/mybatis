package com.hu66.mybatis.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlSession {
	private DBHelper db= null;
	protected SqlSession(Map<String,String> map) {
		db = new DBHelper(map);
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> selectList(String sqlId,Object obj) throws Exception{
		MapperInfo mapperInfo = MyBatisConfig.getMapperInfo(sqlId);
		if(mapperInfo ==null) {
			throw new RuntimeException("没要你要执行的"+sqlId+"...");
		}
		String sql = mapperInfo.getSql();
		List<String> paramNames = mapperInfo.getParamNames();//获取占位符属性名
		String paramType = mapperInfo.getParameterType();//获取用户指定的类型 map bean  原始类型
		List<Object> params = this.getParams(paramType, paramNames, obj);
		String resultType = mapperInfo.getResultType();
		
		if("map".equalsIgnoreCase(paramType)) {
			return (List<T>) db.finds(sql, params);
		}
		Class<?> clazz = Class.forName(resultType);
		
		return (List<T>) db.finds(clazz,sql, params);
	}
	
	public int update(String sqlId,Object obj) throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		MapperInfo mapperInfo = MyBatisConfig.getMapperInfo(sqlId);
		System.out.println(sqlId);
		if(mapperInfo ==null) {
			throw new RuntimeException("没要你要执行的"+sqlId+"...");
		}
		
		String sql = mapperInfo.getSql();
		List<String> paramNames = mapperInfo.getParamNames();//获取占位符属性名
		if(paramNames ==null || paramNames.isEmpty()) {
			//说明没有占位符
			return db.update(sql);
		}
		//说明有占位符
		String paramType = mapperInfo.getParameterType();//获取用户指定的类型 map bean  原始类型
		List<Object> params = this.getParams(paramType, paramNames, obj);
		
		return db.update(sql, params);
	}
	
	private List<Object> getParams(String paramType,List<String> paramNames,Object obj) throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		if(obj ==null) {
			return Collections.emptyList();//空集合
		}
		if(paramNames==null|| paramNames.isEmpty()) {
			return Collections.emptyList();//空集合
		}
		
		List<Object> params = new ArrayList<Object>();
		if("int".equalsIgnoreCase(paramType)||"float".equalsIgnoreCase(paramType)||
		   "double".equalsIgnoreCase(paramType)||"string".equalsIgnoreCase(paramType)) {
			params.add(obj);
		}else if("map".equalsIgnoreCase(paramType)) {
			Map<String ,Object> map = (Map<String, Object>) obj;
			for(String name:paramNames) {
				params.add(map.get(name));
			}
		}else { //基于实体类对象
			Class<?>  clazz=Class.forName(paramType);
			Method[] methods = clazz.getDeclaredMethods();//获取这个类的所有方法
			Map<String,Method> getters = new HashMap<String, Method>();
			String methodName = null;
			for(Method md:methods) {
				methodName=md.getName();//获取当前方法的方法名
				if(!methodName.startsWith("get")) {
					continue;
				}
				getters.put(methodName, md);
			}
			//根据参数名来获取方法
			Method method=null;
			for(String name:paramNames) {
				methodName = "get"+name.substring(0,1).toUpperCase()+name.substring(1);//将属性名第一个大写
				
				method = getters.getOrDefault(methodName, null);
				if(method ==null) {
					continue;
				}
				params.add(method.invoke(obj));//如果存在，则反向激活这个方法获取这个方法的返回值
			}
		}
		
		return params;
	}
}
