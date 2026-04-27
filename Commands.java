package com.bedwarswaypoint;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class Commands extends CommandBase {

    @Override
    public String getCommandName() {
        return "bedway";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/bedway <set|clear|hide>";
    }

    // Essential for client-side commands on multiplayer servers like Hypixel
    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    // Always allow the client player to use it
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Usage: " + getCommandUsage(sender)));
            return;
        }

        if (args[0].equalsIgnoreCase("set")) {
            // Grab the player's exact block position
            BlockPos pos = sender.getPosition();
            Bedway.waypointManager.setBase(pos);
            if (Bedway.waypointManager.hidden()) {
                sender.addChatMessage(new ChatComponentText((EnumChatFormatting.BLUE + "Bedway revealed.")));
            }
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Bedway set at: " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ()));

        } else if (args[0].equalsIgnoreCase("clear")) {
            Bedway.waypointManager.clearBase();
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.YELLOW + "Bedway cleared."));

        } else if (args[0].equalsIgnoreCase("hide")) {
            Bedway.waypointManager.hide();
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.BLUE + ((Bedway.waypointManager.hidden()) ? "Bedway revealed." : "Bedway hidden.")));

        } else {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Unknown argument. Usage: " + getCommandUsage(sender)));
        }
    }
}