package com.bedwarswaypoint;

import net.minecraft.util.BlockPos;

public class Waypoints {
    private BlockPos baseLocation;
    private boolean hidden = false;

    public void setBase(BlockPos pos) {
        hidden = false;
        this.baseLocation = pos;
    }

    public BlockPos getBase() {
        return this.baseLocation;
    }

    public boolean hasBase() {
        return (hidden || this.baseLocation != null);
    }

    public void clearBase() {
        this.baseLocation = null;
    }

    public void hide() { this.hidden = !(this.hidden); }

    public boolean hidden() { return this.hidden; }
}