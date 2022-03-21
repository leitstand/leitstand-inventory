package io.leitstand.inventory.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema="inventory", name="content")
public class Content implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="hash")
	private String contentHash;
	@Column(name="size")
	private Long contentSize;
	@Column(name="type")
	private String contentType;

	protected Content() {
		// JPA constructor
	}
	
	protected Content(String contentType, String contentHash, Long contentSize ) {
		this.contentType = contentType;
		this.contentHash = contentHash;
		this.contentSize = contentSize;
	}
		
	
	public String getContentHash() {
		return contentHash;
	}
	
	
	public Long getContentSize() {
		return contentSize;
	}
	
	
	public String getContentType() {
		return contentType;
	}
	
	
}
