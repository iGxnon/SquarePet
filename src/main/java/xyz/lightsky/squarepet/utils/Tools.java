package xyz.lightsky.squarepet.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.mob.EntitySlime;
import cn.nukkit.level.Position;
import cn.nukkit.level.particle.FloatingTextParticle;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.scheduler.Task;

import java.net.ServerSocket;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Tools {

    // TODO: 2021/07/16
    public static String convertItem(String eng) {
        return eng;
    }

    public static CompoundTag createProjectileTag(Position pos, int networkId) {
        CompoundTag tag = Entity.getDefaultNBT(pos);
        tag.putInt("NetworkId", networkId);
        return tag;
    }

    public static String randomColor() {
        final String STR_1 = "abcdefmn1234567890";
        final String STR_2 = "lo";
        final String STR_3 = "§";
        int i = (new Random()).nextInt(18);
        int j = (new Random()).nextInt(2);
        return STR_3 + STR_2.charAt(j) + STR_3 + STR_1.charAt(i);
    }

    public static String 转化成吃了炫迈的话(String raw) {
        final String STR_CONVERT = "abcdefmn1234567890";
        final String STR_KEY = "§";
        StringBuilder result = new StringBuilder();
        for(int i=0; i<raw.length(); i++) {
            int index = (new Random()).nextInt(18);
            String convert = STR_KEY + STR_CONVERT.charAt(index);
            result.append(convert).append(raw.charAt(i));
        }
        return result.toString();
    }

    public static void createHealParticle(Position pos, float value, HealType type){
        String color = (type == HealType.HP) ? "§c" : "§1";
        String text = color + "§l§o+" + value;
        addFloatDropText(text, pos);
    }

    public enum HealType {
        HP, SP
    }

    public static void createDamageParticle(Position centre, float damage, boolean crit) {
        String color = randomColor();
        String text = crit ? color+"§l§o-"+String.format("%.01f", damage)+"\n"+randomColor()+"暴击!" : "§l§o-"+String.format("%.01f", damage);
        addFloatDropText(text, centre);
    }


    public static void addFloatDropText(String text, Position pos) {
        float bit = 0.5F/16F;
        float x = new Random().nextFloat();
        float z = new Random().nextFloat();
        EntitySlime tool = new EntitySlime(pos.getChunk(), Entity.getDefaultNBT(pos.add(x, 0.5D, z)));
        tool.setMaxHealth(1000);
        tool.setHealth(1000F);
        tool.setScale(0);
        tool.setNameTagAlwaysVisible();
        tool.setNameTagVisible();
        tool.setNameTag(text);
        tool.spawnToAll();
        AtomicInteger ai = new AtomicInteger();
        Server.getInstance().getScheduler().scheduleRepeatingTask(new Task() {
            @Override
            public void onRun(int i) {
                ai.incrementAndGet();
                if(ai.get() > 31) {
                    getHandler().cancel();
                    tool.close();
                }
                tool.move(0, -bit, 0);
                tool.updateMovement();
            }
        }, 1);
    }

    public static String generateBar(int amount, int max, int length) {
        int count = (int) ((((float) amount) / ((float) max)) * length);
        StringBuilder builder = new StringBuilder();
        builder.append("§a");
        for(int i=0;i<length;i++){
            if(i == count) {
                builder.append("§c");
            }
            builder.append("|");
        }
        builder.append("§f");
        return builder.toString();
    }

    @Deprecated
    public static void addFloatDropText(Position centre, String text) {
        List<FloatingTextParticle> particles = new ArrayList<>();
        float bit = 0.5F/16F;
        float x = new Random().nextFloat();
        float z = new Random().nextFloat();
        for(int i=63;i>=32;i--) {
            Position newPos = centre.add(x, i * bit, z);
            FloatingTextParticle particle = new FloatingTextParticle(newPos, text);
            particles.add(particle);
        }
        AtomicInteger ai = new AtomicInteger();

        Server.getInstance().getScheduler().scheduleRepeatingTask(new Task() {
            @Override
            public void onRun(int i) {
                ai.incrementAndGet();
                if(ai.get() > 31) {
                    getHandler().cancel();
                    FloatingTextParticle en = particles.get(31);
                    en.setInvisible(true);
                    centre.getLevel().addParticle(en);
                    return;
                }
                FloatingTextParticle n = particles.get(ai.get());
                FloatingTextParticle o = particles.get(Math.max(0, ai.get() - 1));
                o.setInvisible(true);
                centre.getLevel().addParticle(n);
                centre.getLevel().addParticle(o);
            }
        }, 1);
    }

    public static void sendTitle(Player player, String... messages) {
        AtomicInteger ai = new AtomicInteger();
        int end = messages.length - 1;
        Server.getInstance().getScheduler().scheduleDelayedRepeatingTask(new Task() {
            @Override
            public void onRun(int i) {
                String mess = messages[ai.get()];
                player.sendTitle(mess,"",10, 15, 4);
                ai.incrementAndGet();
                if(ai.get() > end) {
                    getHandler().cancel();
                }
            }
        }, 20, 30);
    }

    public static List<Position> generatePos(Position core) {
        ArrayList<Position> positions = new ArrayList<>();

        double coreX = core.x;
        double coreY = core.y;
        double coreZ = core.z;

        Map<Double, Double> map = new LinkedHashMap<>();
        for(int i=30; i<=90; i+=30) {
            map.put(10 * Math.cos(Math.toRadians(i)), 10 * Math.sin(Math.toRadians(i)));
        }

        map.keySet().forEach(s->{
            for(int j=30; j<= 90; j += 30) {
                double x = s * Math.cos(Math.toRadians(j));
                double z = s * Math.sin(Math.toRadians(j));
                positions.add(new Position(x, map.get(s), z).add(coreX, coreY, coreZ));
                positions.add(new Position(-x, map.get(s), -z).add(coreX, coreY, coreZ));
                positions.add(new Position(-z, map.get(s), x).add(coreX, coreY, coreZ));
                positions.add(new Position(z, map.get(s), -x).add(coreX, coreY, coreZ));
            }
        });

        return positions;
    }

    public static String logo() {

        return randomColor() + "\n   _____                             ____       __ \n" +
                "  / ___/____ ___  ______ _________  / __ \\___  / /_\n" +
                "  \\__ \\/ __ `/ / / / __ `/ ___/ _ \\/ /_/ / _ \\/ __/\n" +
                " ___/ / /_/ / /_/ / /_/ / /  /  __/ ____/  __/ /_  \n" +
                "/____/\\__, /\\__,_/\\__,_/_/   \\___/_/    \\___/\\__/  \n" +
                "        /_/                                        ";

    }

}
