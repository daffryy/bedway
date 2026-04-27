package com.bedwarswaypoint;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.client.ClientCommandHandler;

@Mod(modid = Bedway.MODID, version = Bedway.VERSION)
public class Bedway {
    public static final String MODID = "bedwarswaypoint";
    public static final String VERSION = "1.0";

    @Mod.Instance(MODID)
    public static Bedway instance;

    // Here we use your Waypoints class to manage the coordinates
    public static Waypoints waypointManager = new Waypoints();

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // Register the rendering event
        MinecraftForge.EVENT_BUS.register(new Render());

        // Register the CLIENT-SIDE command
        ClientCommandHandler.instance.registerCommand(new Commands());
    }
}