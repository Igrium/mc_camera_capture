package org.scaffoldeditor.camera_capture;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.StartTick;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;

/**
 * Handles the actual capturing of camera movements.
 */
@Environment(EnvType.CLIENT)
public class Recorder {

    public static class Frame {
        public final float delta;
        public final Vec3d position;
        public final Quaternion rotation;

        public Frame(float delta, Vec3d position, Quaternion rotation) {
            this.delta = delta;
            this.position = position;
            this.rotation = rotation;
        }
        
    }
    
    private List<Frame> memory = new ArrayList<>();
    private boolean isRecording = false;

    /**
     * Create a recorder.
     * @param camera The camera to capture.
     */
    public Recorder() {
        ClientTickEvents.START_CLIENT_TICK.register(listener);
    }

    private StartTick listener = new StartTick(){

        @Override
        public void onStartTick(MinecraftClient client) {
            if (!isRecording) return;
            Camera camera = client.gameRenderer.getCamera();
            memory.add(new Frame(client.getLastFrameDuration(), camera.getPos(), camera.getRotation().copy()));
        }
        
    };

    public void start() {
        if (isRecording) {
            throw new IllegalStateException("Recorder is already recording.");
        }

        memory = new ArrayList<>();
        isRecording = true;
    }

    public void stop() {
        if (!isRecording) {
            throw new IllegalStateException("Recorder is not recording.");
        }
        isRecording = false;
    }

    public boolean isRecording() {
        return isRecording;
    }

    /**
     * Get all the frames from the previous recording.
     * @return A list of all the frames in order.
     */
    public List<Frame> getMemory() {
        return memory;
    }

    public static void save(List<Frame> frames, OutputStream out) {
        PrintWriter writer = new PrintWriter(new BufferedOutputStream(out));
        for (Frame frame : frames) {
            String pos = frame.position.x + " " + frame.position.y + " " + frame.position.z;
            String rot = frame.rotation.getW() + " " + frame.rotation.getX() + " " + frame.rotation.getY() + " " + frame.rotation.getZ();
            writer.println(frame.delta + " " + pos + " " + rot);
        }
        writer.close();
    }
    
    public static void save(List<Frame> frames, File target) throws FileNotFoundException {
        save(frames, new FileOutputStream(target));
    }
}
