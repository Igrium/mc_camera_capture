## What is this about?
In the past few weeks, a trend has arisin of capturing Minecraft gameplay and compositing "realisitic" elements over it. A major step in this process is motion tracking the gameplay, which can be quite tedious. This mod and addon removes this step by capturing camera animation directly from the game.

## How to use
First, install the Minecraft mod with Fabric (if you don't know how to do that, google it). Then, on any server, start your screen recording software and type `/capture start` into the console. When you're done, type `/capture stop` into console, and the game will output a `.mcap` file.

Using the included Blender addon, this file can be imported into Blender. It must be manually aligned to the video, and it is reccomended to use FSpy to match the rest of your camera settings to the Minecraft clip.

*Warning: if your Minecraft frame rate is less than that of your recording, your camera will appear to "glide". This can be fixed by changing the interpolation mode of the keyframes surrounding the frame drop.*