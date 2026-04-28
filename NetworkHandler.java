package com.bedwarswaypoint;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import scala.util.control.TailCalls;


public class NetworkHandler {
    // these vars are a very hacky way to make the mod only run once per lobby
    // every tick until the game starts counter is incremented
    // once a waypoint is autoset inited is set to true
    private int counter = Integer.MIN_VALUE;
    private boolean inited = false;

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText();
        // each bedwars game starts with this message,
        // so look for it and then once the game starts get lobby info
        if (message.contains("Protect your bed and destroy the enemy beds.")) {
            Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C01PacketChatMessage("/locraw"));
        }
        // this is the format of the server info message you get when typing /locraw
        // this is how the mod knows to start doing the waypoint
        if (message.contains("{\"server\":\"")) {
            // no matter what kind of game we want to hide the locraw info to not clog the chat
            event.setCanceled(true);
            // i cant have the waypoint generate on some game start message because
            // the message is sent as the player is teleported
            // and sometimes lag will cause the waypoint to be set in the waiting area
            // so this counter basically works as a timer because the mod needs to be asynchronous
            if (message.contains("\"gametype\":\"BEDWARS\",\"mode\":\"BEDWARS")) {
                counter = -50;
            }
        }
    }
    // this code is my hacky way of making the autoset only run once
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (!inited) {
            if (counter < 0) {
                counter++;
            } else if (counter == 0) {
                BlockPos current = Minecraft.getMinecraft().thePlayer.getPosition();
                Bedway.waypointManager.setBase(current);
                inited = true;
            }
        }

    }

    @SubscribeEvent
    public void onWorldJoin(EntityJoinWorldEvent event) {
    // check that player is joining world
        if (event.entity == Minecraft.getMinecraft().thePlayer) {
            // clear waypoint per lobby swap
            if (Bedway.waypointManager != null) {
                Bedway.waypointManager.clearBase();
            }
            inited = false;
        }
    }

    @SubscribeEvent
    public void onServerDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        // clear waypoints on disconnects for gits and shiggles
        if (Bedway.waypointManager != null) {
            Bedway.waypointManager.clearBase();
        }
    }
}
