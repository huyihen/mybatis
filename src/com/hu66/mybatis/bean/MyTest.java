package com.hu66.mybatis.bean;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.junit.Test;

import com.hu66.mybatis.core.MyBatisConfig;
import com.hu66.mybatis.core.SqlSession;
import com.hu66.mybatis.core.SqlSessionFactory;

public class MyTest {
	@Test
	public void test() {
		SqlSessionFactory factory = new SqlSessionFactory(new MyBatisConfig("mybatis-config.xml"));
		SqlSession session = factory.openSession();
		try {
			int result = session.update("GoodsTypeMapper.del", 9);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void test2() {
		SqlSessionFactory factory = new SqlSessionFactory(new MyBatisConfig("mybatis-config.xml"));
		SqlSession session = factory.openSession();
		try {
			int result = session.update("GoodsTypeMapper.add", new ttinfo("不好吃"));
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void test3() {
		SqlSessionFactory factory = new SqlSessionFactory(new MyBatisConfig("mybatis-config.xml"));
		SqlSession session = factory.openSession();
		try {
			List<ttinfo> list = session.selectList("GoodsTypeMapper.finds", null);
			list.forEach(System.out::println);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
