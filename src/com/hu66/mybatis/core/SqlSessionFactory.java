package com.hu66.mybatis.core;

public class SqlSessionFactory {
	private MyBatisConfig mybatisConfig;
	public SqlSessionFactory( MyBatisConfig mybatisConfig) {
		this.mybatisConfig=mybatisConfig;
	}
	public SqlSession openSession() {
		return new SqlSession(mybatisConfig.getDataSourceMap());
	}
}
