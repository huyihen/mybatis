package com.hu66.mybatis.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;



public class MyBatisConfig {
	private Map<String, String> dataSourceMap = new HashMap<String, String>();
	private List<String> mappers = new ArrayList<String>();
	private static Map<String,MapperInfo> mapperInfos = new HashMap<String,MapperInfo>();
	public MyBatisConfig(String config) {
		parseXml(config);
		/*
		dataSourceMap.forEach((key,val)->{
			System.out.println(key+":"+val);
		});
		mappers.forEach(System.out::println);*/
		parseMapper();
		
		/*mapperInfos.forEach((key,val)->{
			System.out.println(key+":"+val);
		});*/
	}
	/**
	 * 解析映射文件
	 */
	private void parseMapper() {
		if(mappers ==null || mappers.isEmpty()) {
			return;
		}
		MapperInfo mapperInfo = null;
		Document doc = null;
		String sql=null;
		String namespace = null;//命名空间
		String nodeName = "";//节点名称
		SAXReader read = null;//解析器
		XPath xpath = null;
		List<Element> nodes = null;
		Pattern pattern = null;
		Matcher matcher = null;
		List<String> paramNames = null;
		
		for(String mapper:mappers) {
			try(InputStream is = this.getClass().getClassLoader().getResourceAsStream(mapper)){
				read = new SAXReader();
				doc= read.read(is);
				namespace=doc.getRootElement().attributeValue("namespace");
				if(namespace !=null && !"".equals(namespace)) {
					namespace+=".";
				}
				xpath = doc.createXPath("/mapper/*");
				nodes = xpath.selectNodes(doc);
				for(Element el:nodes) {
					mapperInfo = new MapperInfo();
					
					nodeName = el.getName();//获取节点名
					if("select".equals(nodeName.toLowerCase())) {
						mapperInfo.setUpdate(false);//说明是查询
					}
					
					mapperInfo.setParameterType(el.attributeValue("parameterType"));
					mapperInfo.setResultType(el.attributeValue("resultType"));
					sql=el.getTextTrim();
					
					pattern = Pattern.compile("[#][{]\\w+}");
					matcher = pattern.matcher(sql);
					paramNames=new ArrayList<String>();
					while(matcher.find()) {
						paramNames.add(matcher.group().replaceAll("[#{}]*", ""));
					}
					mapperInfo.setParamNames(paramNames);
					sql=matcher.replaceAll("?");
					mapperInfo.setSql(sql);
					mapperInfos.put(namespace+el.attributeValue("id"),mapperInfo);
					//System.out.println(sql);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (DocumentException e) {
				e.printStackTrace();
			} 
		}
	}
	/**
	 * 解析配置文件
	 * @param config
	 */
	@SuppressWarnings("unchecked")
	private void parseXml(String config) {
		SAXReader read = new SAXReader();
		try(InputStream is = this.getClass().getClassLoader().getResourceAsStream(config)){
			Document doc = read.read(is);
			XPath xpath = doc.createXPath("//dataSource/property");
			List<Element> list = xpath.selectNodes(doc);
			
			for( Element el:list) {
				dataSourceMap.put(el.attributeValue("name"), el.attributeValue("value"));
			}
			//获取映射文件
			xpath = doc.createXPath("//mappers/mapper");
			list = xpath.selectNodes(doc);
			
			for( Element el:list) {
				mappers.add(el.attributeValue("resource"));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	public Map<String, String> getDataSourceMap() {
		return dataSourceMap;
	}

	public List<String> getMappers() {
		return mappers;
	}
	public Map<String, MapperInfo> getMapperInfos() {
		return mapperInfos;
	}
	public static MapperInfo getMapperInfo(String key) {
		return mapperInfos.getOrDefault(key, null);
	}
	
	
}
