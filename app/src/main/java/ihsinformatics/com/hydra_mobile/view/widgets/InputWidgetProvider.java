package ihsinformatics.com.hydra_mobile.view.widgets;


import ihsinformatics.com.hydra_mobile.utils.InputWidgetsType;

public class InputWidgetProvider {

    private InputWidgetProvider() {

    }

    private static InputWidgetProvider inputWidgetProvider;

    public static InputWidgetProvider getInstance() {
        if (inputWidgetProvider == null) {
            inputWidgetProvider = new InputWidgetProvider();
        }

        return inputWidgetProvider;
    }

    public InputWidget getInputWidget(InputWidgetsType inputWidgetsType) {

        return null;
    }

}
