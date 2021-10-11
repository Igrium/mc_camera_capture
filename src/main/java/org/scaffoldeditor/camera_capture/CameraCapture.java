package org.scaffoldeditor.camera_capture;

import net.fabricmc.api.ClientModInitializer;

public class CameraCapture implements ClientModInitializer {

    private static CameraCapture instance;
    private Recorder recorder = new Recorder();

    /**
     * Get the active mod instance.
     */
    public static CameraCapture getInstance() {
        return instance;
    }

    public Recorder getRecorder() {
        return recorder;
    }

    @Override
    public void onInitializeClient() {
        instance = this;
        CaptureCommand.register();
    }
    
}
