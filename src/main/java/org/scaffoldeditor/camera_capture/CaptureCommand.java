package org.scaffoldeditor.camera_capture;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import org.apache.logging.log4j.LogManager;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;

@Environment(EnvType.CLIENT)
public final class CaptureCommand {

    public static void register() {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("capture")
            .then(ClientCommandManager.literal("start").executes(context -> {
                try {
                    CameraCapture.getInstance().getRecorder().start();
                    context.getSource().sendFeedback(new LiteralText("Started capturing camera movement."));
                } catch (IllegalStateException e) {
                    throw new SimpleCommandExceptionType(new LiteralText(e.getMessage())).create();
                }
                return 0;
            }))
            .then(ClientCommandManager.literal("stop").executes(context -> {
                Recorder recorder = CameraCapture.getInstance().getRecorder();
                try {
                    recorder.stop();
                } catch (IllegalStateException e) {
                    throw new SimpleCommandExceptionType(new LiteralText(e.getMessage())).create();
                }
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                
                try {
                    Path capturesFolder = client.runDirectory.toPath().resolve("captures").normalize();
                    if (!capturesFolder.toFile().isDirectory()) {
                        capturesFolder.toFile().mkdir();
                    }
                    File targetFile = capturesFolder.resolve(formatter.format(new Date())+".cap").toFile();
                    Recorder.save(recorder.getMemory(), targetFile);
                    context.getSource().sendFeedback(new LiteralText("Saved file to "+targetFile.toString()));
                } catch (IOException e) {
                    LogManager.getLogger().error(e);
                    throw new SimpleCommandExceptionType(new LiteralText("Unable to save file. See console for details.")).create();
                }
                return 0;
            })));
    }
    
}
