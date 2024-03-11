package me.hsgamer.morefoworld;

import io.github.projectunified.minelib.plugin.base.BasePlugin;
import io.github.projectunified.minelib.plugin.command.CommandComponent;
import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.hscore.config.proxy.ConfigGenerator;
import me.hsgamer.morefoworld.command.MainCommand;
import me.hsgamer.morefoworld.config.MainConfig;
import me.hsgamer.morefoworld.config.PortalConfig;
import me.hsgamer.morefoworld.config.RespawnConfig;
import me.hsgamer.morefoworld.config.SpawnConfig;
import me.hsgamer.morefoworld.listener.PortalListener;
import me.hsgamer.morefoworld.listener.RespawnListener;
import me.hsgamer.morefoworld.listener.SpawnListener;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class MoreFoWorld extends BasePlugin {
    @Override
    protected List<Object> getComponents() {
        return List.of(
                ConfigGenerator.newInstance(MainConfig.class, new BukkitConfig(this)),
                ConfigGenerator.newInstance(PortalConfig.class, new BukkitConfig(this, "portals.yml")),
                ConfigGenerator.newInstance(RespawnConfig.class, new BukkitConfig(this, "respawn.yml")),
                ConfigGenerator.newInstance(SpawnConfig.class, new BukkitConfig(this, "spawn.yml")),
                new DebugComponent(this),
                new PortalListener(this),
                new RespawnListener(this),
                new SpawnListener(this),
                new CommandComponent(this, () -> List.of(new MainCommand(this)))
        );
    }

    @Override
    public void load() {
        MessageUtils.setPrefix("&8[&6MoreFuckingWorlds&8] &r");
    }

    @Override
    public void enable() {


        for (int i = 1; i <= 1; i++) {
            WorldSetting purpeworlds = new WorldSetting("purpleworld_" + i);
            WorldCreator worldCreator = purpeworlds.toWorldCreator();
            WorldUtil.FeedbackWorld feedbackWorld = WorldUtil.addWorld(worldCreator);
            if (feedbackWorld.feedback == WorldUtil.Feedback.SUCCESS) {
                getLogger().info("World " + purpeworlds.getName() + " is added");
            } else {
                getLogger().warning("World " + purpeworlds.getName() + " is not added: " + feedbackWorld.feedback);
            }
            getLogger().info(purpeworlds.toString());

        }
    }
    public static void separate_tread(Player player) {
        Player sender = player;
        Thread wle = new Thread(new Runnable() {
            @Override
            public void run() {
                // OVERWORLD
                WorldSetting purpeov = new WorldSetting(player.getName()+"_PurpleWorld");
                purpeov.setEnvironment(World.Environment.NORMAL);
                WorldCreator worldCreator = purpeov.toWorldCreator();
                WorldUtil.FeedbackWorld feedbackWorld = WorldUtil.addWorld(worldCreator);
                if (feedbackWorld.feedback == WorldUtil.Feedback.SUCCESS) {
                    MessageUtils.sendMessage(sender, "World " + purpeov.getName() + " is added");
                } else {
                    MessageUtils.sendMessage(sender, "World " + purpeov.getName() + " is not added: " + feedbackWorld.feedback);
                }

                // NETHER
                WorldSetting purpe_nether = new WorldSetting(player.getName()+"_NETHER_PurpleWorld");
                purpe_nether.setEnvironment(World.Environment.NETHER);
                WorldCreator NetherworldCreator = purpe_nether.toWorldCreator();
                WorldUtil.FeedbackWorld NetherfeedbackWorld = WorldUtil.addWorld(NetherworldCreator);
                if (NetherfeedbackWorld.feedback == WorldUtil.Feedback.SUCCESS) {
                    MessageUtils.sendMessage(sender, "NetherWorld " + purpeov.getName() + " is added");
                } else {
                    MessageUtils.sendMessage(sender, "NetherWorld " + purpeov.getName() + " is not added: " + feedbackWorld.feedback);
                }
                // END
                // NETHER
                WorldSetting purpe_end = new WorldSetting(player.getName()+"_THE_END_PurpleWorld");
                purpe_end.setEnvironment(World.Environment.THE_END);
                WorldCreator EndworldCreator = purpe_end.toWorldCreator();
                WorldUtil.FeedbackWorld EndfeedbackWorld = WorldUtil.addWorld(EndworldCreator);
                if (EndfeedbackWorld.feedback == WorldUtil.Feedback.SUCCESS) {
                    MessageUtils.sendMessage(sender, "NetherWorld " + purpeov.getName() + " is added");
                } else {
                    MessageUtils.sendMessage(sender, "NetherWorld " + purpeov.getName() + " is not added: " + feedbackWorld.feedback);
                }


                // END OF GENERATION



                boolean loadattempt = true;
                World world = Bukkit.getWorld(player.getName()+"_PurpleWorld");
                while(Objects.requireNonNull(world).getLoadedChunks().length == 0){
                    MessageUtils.sendMessage(sender, "&aWaiting to teleport... Loaded chunks: "+ world.getLoadedChunks().length);
                    try {
                        Thread.sleep(1000);
                        if (loadattempt){
                            world.getChunkAtAsync(0,0);
                            loadattempt = false;
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                MessageUtils.sendMessage(sender, "&aLoad attempt successful! Loaded chunks: "+ world.getLoadedChunks().length);
                MessageUtils.sendMessage(sender, "&aTeleporting...");
                player.teleportAsync(world.getSpawnLocation()).whenComplete((aVoid, throwable) -> {
                    if (throwable != null) {
                        MessageUtils.sendMessage(sender, "&cAn error occurred: " + throwable.getMessage());
                    } else {
                        MessageUtils.sendMessage(sender, "&aYou have been teleported to &e" + world.getName());
                    }
                });
            }
        });
        wle.start();
    }
}
