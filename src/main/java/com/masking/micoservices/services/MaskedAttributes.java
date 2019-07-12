package com.masking.micoservices.services;

import java.io.Serializable;

public class MaskedAttributes implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	
	public String getOriginalValue() {
		if(this.originalValue!=null)
		return originalValue;
		else return "";
	}
	public void setOriginalValue(String originalValue) {
		this.originalValue = originalValue;
	}
	public String getReplacedValue() {
		if(this.replacedValue!=null)
		return replacedValue;
		else return "";
	}
	public void setReplacedValue(String replacedValue) {
		this.replacedValue = replacedValue;
	}
	String originalValue;
	String replacedValue;


	@Override
	public String toString() {
		return "MaskedAttributes [originalValue=" + originalValue + ", replacedValue=" + replacedValue + "]";
	}
	
	@Override public boolean equals(Object o) {
		if (o == this) return true;
		if (!(o instanceof MaskedAttributes)) return false;
		MaskedAttributes other = (MaskedAttributes) o;
		if(this.getOriginalValue().equalsIgnoreCase(other.getOriginalValue()) && this.getReplacedValue().equalsIgnoreCase(other.getReplacedValue()))
		return true;
		else return false;
		
	}
	
	@Override public int hashCode() {
	      final int PRIME = 59;
	      int result = 1;
	      result = (result*PRIME) + super.hashCode();
	      result = (result*PRIME) + this.getOriginalValue().trim().hashCode();
	      result = (result*PRIME) + this.getReplacedValue().trim().hashCode();
	      return result;
	    }
	
	
}
