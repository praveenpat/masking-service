package com.masking.micoservices.services;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("masker")
public class MaskerConfig {
	
	List<Masker> maskers;

	public List<Masker> getMaskers() {
		return maskers;
	}

	public void setMaskers(List<Masker> maskers) {
		this.maskers = maskers;
	}

}
