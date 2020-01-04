/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static io.leitstand.inventory.model.Checksum.Algorithm.MD5;
import static io.leitstand.inventory.model.Checksum.Algorithm.SHA1;
import static io.leitstand.inventory.model.Checksum.Algorithm.SHA256;
import static io.leitstand.inventory.model.Checksum.Algorithm.SHA512;
import static javax.persistence.EnumType.STRING;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Enumerated;

import io.leitstand.commons.model.CompositeValue;

@Embeddable
public class Checksum extends CompositeValue implements Serializable{

	private static final long serialVersionUID = 1L;


	public static Checksum sha512Checksum(String value) {
		return newChecksum()
			   .withAlgorithm(SHA512)
			   .withValue(value)
			   .build();
	}
	
	public static Checksum sha256Checksum(String value) {
		return newChecksum()
			   .withAlgorithm(SHA256)
			   .withValue(value)
			   .build();
	}
	
	public static Checksum sha1Checksum(String value) {
		return newChecksum()
			   .withAlgorithm(SHA1)
			   .withValue(value)
			   .build();
	}
	
	public static Checksum md5Checksum(String value) {
		return newChecksum()
			   .withAlgorithm(MD5)
			   .withValue(value)
			   .build();
	}
	
	public enum Algorithm {
		SHA256,
		SHA512,
		SHA1,
		MD5;
	}
	
	public static Builder newChecksum() {
		return new Builder();
	}
	
	public static class Builder {
		
		private Checksum chksum = new Checksum();
		
		public Builder withAlgorithm(Algorithm algorithm) {
			assertNotInvalidated(getClass(), chksum);
			chksum.algorithm = algorithm;
			return this;
		}

		public Builder withValue(String value) {
			assertNotInvalidated(getClass(), chksum);
			chksum.value = value;
			return this;
		}
		
		public Checksum build() {
			try {
				assertNotInvalidated(getClass(), chksum);
				return chksum;
			} finally {
				this.chksum = null;
			}
		}
		
	}
	

	@Enumerated(STRING)
	@Column(name="algorithm")
	private Algorithm algorithm;
	@Column(name="checksum")
	private String value;
	
	
	public Algorithm getAlgorithm() {
		return algorithm;
	}
	
	public String getValue() {
		return value;
	}
}
