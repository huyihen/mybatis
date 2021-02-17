package com.hu66.mybatis.bean;

public class ttinfo {
	private Integer ttid;
	private String name;
	public Integer getTtid() {
		return ttid;
	}
	public void setTtid(Integer ttid) {
		this.ttid = ttid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((ttid == null) ? 0 : ttid.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ttinfo other = (ttinfo) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (ttid == null) {
			if (other.ttid != null)
				return false;
		} else if (!ttid.equals(other.ttid))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ttinfo [ttid=" + ttid + ", name=" + name + "]";
	}
	public ttinfo( String name) {
		this.name = name;
	}
	public ttinfo() {
	}
	
}
