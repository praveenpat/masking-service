package com.masking.micoservices.services;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;


@RedisHash(value="MaskedResult",timeToLive=600)
public class MaskedResult implements Serializable{
	
	
	 
	private static final long serialVersionUID = 1L;
	
	 
	@Id private String requestId;

	private String originalUtterance;
	 
	 private String maskedUtterance;
	 
	 List<MaskedAttributes> maskedAttributes;
	 
	 
	 
	 

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getOriginalUtterance() {
		return originalUtterance;
	}

	public void setOriginalUtterance(String originalUtterance) {
		this.originalUtterance = originalUtterance;
	}

	public String getMaskedUtterance() {
		return maskedUtterance;
	}

	public void setMaskedUtterance(String maskedUtterance) {
		this.maskedUtterance = maskedUtterance;
	}

	public List<MaskedAttributes> getMaskedAttributes() {
		return maskedAttributes;
	}

	public void setMaskedAttributes(List<MaskedAttributes> maskedAttributes) {
		this.maskedAttributes = maskedAttributes;
	}
	 
	 
	 

}
