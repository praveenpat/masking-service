package com.masking.micoservices.services;

public class UnMaskingRequest {
	
	String textToUnmask;
	MaskedResult maskedResult;
	
	public String getTextToUnmask() {
		return textToUnmask;
	}
	public void setTextToUnmask(String textToUnmask) {
		this.textToUnmask = textToUnmask;
	}
	public MaskedResult getMaskedResult() {
		return maskedResult;
	}
	public void setMaskedResult(MaskedResult maskedResult) {
		this.maskedResult = maskedResult;
	}

}
