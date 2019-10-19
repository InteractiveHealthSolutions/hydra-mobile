package com.ihsinformatics.dynamicformsgenerator.views.widgets;


public class InputWidgetProvider {

	private InputWidgetProvider() {
		
	}
	
	private static InputWidgetProvider inputWidgetProvider;
	
	public static InputWidgetProvider getInstance() {
		if(inputWidgetProvider == null) {
			inputWidgetProvider = new InputWidgetProvider();
		}
		
		return inputWidgetProvider;
	}
	
	public InputWidget getInputWidget(InputWidget.InputWidgetsType inputWidgetsType) {
		
		return null;
	}

}
