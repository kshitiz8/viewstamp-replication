package vr.proxy.client;

import java.util.List;

import vr.code.service.ReplicaState;

public class ProxyConfiguration {
	
	private ProxyConfiguration() {
		
	}	
	public static ProxyConfiguration getInstance(){
		return ProxyConfigurationHolder.instance;
	}

	private static class ProxyConfigurationHolder {
		static final ProxyConfiguration instance = new ProxyConfiguration();
	}

	
	String proxyId;
	List<String> qouroms;
	int primaryReplica;
	long viewNumber;
	boolean idle = true;
	long requestNumber;
	public String getProxyId() {
		return proxyId;
	}
	public void setProxyId(String proxyId) {
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
	public long getRequestNumber() {
		return requestNumber;
	}
	public void setRequestNumber(long requestNumber) {
		this.requestNumber = requestNumber;
	}
}