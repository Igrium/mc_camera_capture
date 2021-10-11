import bpy
from . import operator_import_camera

bl_info = {
    "name" : "Minecraft Camera Capture",
    "author" : "Igrium",
    "description" : "",
    "blender" : (2, 93, 1),
    "version" : (0, 1, 0),
    "location" : "",
    "warning" : "",
    "category" : "Import-Export"
}

def register():
    operator_import_camera.register()

def unregister():
    operator_import_camera.unregister()
