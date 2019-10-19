package com.ihsinformatics.dynamicformsgenerator.data.core;

public class Form {

	private int formTypeId;
	private int formId;
	
	public Form() {
		// TODO Auto-generated constructor stub
	}

	public Form(int formTypeId, int formId) {
		super();
		this.formTypeId = formTypeId;
		this.formId = formId;
	}

	public int getFormTypeId() {
		return formTypeId;
	}

	public void setFormTypeId(int formTypeId) {
		this.formTypeId = formTypeId;
	}

	public int getFormId() {
		return formId;
	}

	public void setFormId(int formId) {
		this.formId = formId;
	}
	
	
}
