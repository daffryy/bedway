package com.bedwarswaypoint;

import net.minecraft.util.BlockPos;

public class Waypoints {
    private BlockPos baseLocation;
    private boolean hidden = false;

    // Sets the base location
    public void setBase(BlockPos pos) {
        hidden = false;
        this.baseLocation = pos;
    }

    // Retrieves the base location
    public BlockPos getBase() {
        return this.baseLocation;
    }

    // Checks if a base is currently set
    public boolean hasBase() {
        return (hidden || this.baseLocation != null);
    }

    // Clears the base location
    public void clearBase() {
        this.baseLocation = null;
    }

    // toggles visibility of waypoint
    public void hide() { this.hidden = !(this.hidden); }

    public boolean hidden() { return this.hidden; }
}