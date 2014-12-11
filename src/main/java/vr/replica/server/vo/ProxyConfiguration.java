package vr.replica.server.vo;

import java.util.List;

public class ProxyConfiguration {
	int proxyId ;
	List<String> qouroms ;
	int primaryReplica;
	long viewNumber;
	boolean idle = true;
	long serialNumber;
	public int getProxyId() {
		return proxyId;
	}
	public void setProxyId(int proxyId) {
		this.proxyId = proxyId;
	}
	public List<String> getQouroms() {
		return qouroms;
	}
	public void setQouroms(List<String> qouroms) {
		this.qouroms = qouroms;
	}
	public int getPrimaryReplica() {
		return primaryReplica;
	}
	public void setPrimaryReplica(int primaryReplica) {
		this.primaryReplica = primaryReplica;
	}
	public long getViewNumber() {
		return viewNumber;
	}
	public void setViewNumber(long viewNumber) {
		this.viewNumber = viewNumber;
	}
	public boolean isIdle() {
		return idle;
	}
	public void setIdle(boolean idle) {
		this.idle = idle;
	}
	public long getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(long serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	
	
		
	
}