import os
import bpy
from bpy.types import Camera, Object
from bpy.props import StringProperty
from mathutils import Quaternion, Euler
from bpy_extras.io_utils import ImportHelper
import math

def import_camera(filepath: str, context: bpy.context):
    # Read file
    file = open(filepath)
    lines = file.readlines()
    file.close()

    print('Reading ' + filepath)
    name = os.path.basename(filepath)

    # Create camera
    camera_data: Camera = bpy.data.cameras.new(name=name)
    camera_object: Object = bpy.data.objects.new(name, camera_data)
    context.scene.collection.objects.link(camera_object)
    camera_object.rotation_mode = 'QUATERNION'

    cursor = context.scene.cursor.location
    
    # Animation
    for i in range(0, len(lines)):
        line = lines[i]
        data = line.split(' ')
        first = lines[0].split(' ')

        delta = float(data[0])
        
        posX = float(data[1]) - float(first[1])
        posY = float(data[2]) - float(first[2])
        posZ = float(data[3]) - float(first[3])

        rotW = float(data[4])
        rotX = float(data[5])
        rotY = float(data[6])
        rotZ = float(data[7])

        camera_object.location = [-posX + cursor[0], posZ + cursor[1], posY + cursor[2]]
        quat = Quaternion([rotW, -rotX, rotY, -rotZ])
        quat.rotate(Euler([math.radians(90), 0, 0]))

        camera_object.rotation_quaternion = quat
        camera_object.keyframe_insert(data_path='location', frame=i)  
        camera_object.keyframe_insert(data_path='rotation_quaternion', frame=i)  

class ImportCameraOperator(bpy.types.Operator, ImportHelper):
    bl_idname = "mcam.import_camera"
    bl_label = "mcam.import_camera"

     # ImportHelper mixin class uses this
    filename_ext = ".txt"

    filter_glob: StringProperty(default="*.mcap",options={'HIDDEN'})


    def execute(self, context):
        import_camera(self.filepath, context)
        return {'FINISHED'}

def menu_func_import(self, context):
    self.layout.operator(ImportCameraOperator.bl_idname, text="Minecraft Camera Capture (.mcap)")

def register():
    print('Adding to menu')
    bpy.utils.register_class(ImportCameraOperator)
    bpy.types.TOPBAR_MT_file_import.append(menu_func_import)

def unregister():
    bpy.utils.unregister_class(ImportCameraOperator)
    bpy.types.TOPBAR_MT_file_import.remove(menu_func_import)