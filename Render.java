package com.bedwarswaypoint;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class Render {

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (Bedway.waypointManager.hidden() || Bedway.waypointManager == null || !Bedway.waypointManager.hasBase()) return;

        BlockPos pos = Bedway.waypointManager.getBase();
        Minecraft mc = Minecraft.getMinecraft();

        double viewerX = mc.getRenderManager().viewerPosX;
        double viewerY = mc.getRenderManager().viewerPosY;
        double viewerZ = mc.getRenderManager().viewerPosZ;

        double x = pos.getX() - viewerX;
        double y = pos.getY() - viewerY;
        double z = pos.getZ() - viewerZ;

        GlStateManager.pushMatrix();

        // setup opengl stuff, i stole this code idk how it works
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glLineWidth(2.0f);

        // change the numbers for different colours, will add custom colours soon
        GL11.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);

        // 1. Draw Tracer Line from camera to the block
        // (0,0,0) is exactly where the camera is.
        // We add 0.5 to target the dead-center of the block.
        GL11.glBegin(GL11.GL_LINES);
        net.minecraft.util.Vec3 look = mc.thePlayer.getLook(event.partialTicks);
        GL11.glVertex3d(look.xCoord, look.yCoord + ((mc.thePlayer.isSneaking()) ? 1.541 : 1.62), look.zCoord); // Camera
        GL11.glVertex3d(x + 0.5, y + 0.5, z + 0.5); // Center of target block
        GL11.glEnd();

        // 2. Draw 3D Box Outline around the block
        // Move our matrix to the corner of the block
        GlStateManager.translate(x, y, z);

        // Draw the main frame
        GL11.glBegin(GL11.GL_LINE_STRIP);
        GL11.glVertex3d(0, 0, 0);
        GL11.glVertex3d(1, 0, 0);
        GL11.glVertex3d(1, 0, 1);
        GL11.glVertex3d(0, 0, 1);
        GL11.glVertex3d(0, 0, 0);
        GL11.glVertex3d(0, 1, 0);
        GL11.glVertex3d(1, 1, 0);
        GL11.glVertex3d(1, 1, 1);
        GL11.glVertex3d(0, 1, 1);
        GL11.glVertex3d(0, 1, 0);
        GL11.glEnd();

        // Connect the remaining vertical pillars for the box
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3d(1, 0, 0); GL11.glVertex3d(1, 1, 0);
        GL11.glVertex3d(1, 0, 1); GL11.glVertex3d(1, 1, 1);
        GL11.glVertex3d(0, 0, 1); GL11.glVertex3d(0, 1, 1);
        GL11.glEnd();

        // Reset GL state back to normal so we don't break Minecraft's normal rendering
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);

        GlStateManager.popMatrix();
    }
}