package app.utils.screenUtils;


import java.awt.*;

public class UIDimensions implements Dimensions {

    private double currentDeviceWidth;
    private double currentDeviceHeight;

    public UIDimensions() {
        this.calcScreenDimensions();
    }

    public double getCurrentDeviceWidth() {
        return currentDeviceWidth;
    }

    public double getCurrentDeviceHeight() {
        return currentDeviceHeight;
    }

    private void calcScreenDimensions() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        this.setCurrentDeviceHeight(height);
        this.setCurrentDeviceWidth(width);
    }

    private void setCurrentDeviceWidth(double currentDeviceWidth) {
        this.currentDeviceWidth = currentDeviceWidth;
    }

    private void setCurrentDeviceHeight(double currentDeviceHeight) {
        this.currentDeviceHeight = currentDeviceHeight;
    }
}
