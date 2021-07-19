package xyz.lightsky.SquarePet.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Position;
import cn.nukkit.level.particle.FloatingTextParticle;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.scheduler.Task;

import java.util.*;

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

    public static void createDamageParticle(Position centre, float damage, boolean crit) {
        List<FloatingTextParticle> particles = new ArrayList<>();
        float bit = 0.5F/16F;

        float x =new Random().nextFloat();
        float z = new Random().nextFloat();
        String color = randomColor();
        String text = crit ? color+"§l§o-"+String.format("%.01f", damage)+"\n"+randomColor()+"暴击!" : "§l§o-"+String.format("%.01f", damage);
        for(int i=63;i>=32;i--) {
            Position newPos = centre.add(x, i * bit, z);
            FloatingTextParticle particle = new FloatingTextParticle(newPos, text);
            particles.add(particle);
        }

        final Integer[] j = {0};

        Server.getInstance().getScheduler().scheduleRepeatingTask(new Task() {
            @Override
            public void onRun(int i) {
                j[0]++;
                if(j[0] > 31) {
                    getHandler().cancel();
                    FloatingTextParticle en = particles.get(31);
                    en.setInvisible(true);
                    centre.getLevel().addParticle(en);
                    return;
                }
                FloatingTextParticle n = particles.get(j[0]);
                FloatingTextParticle o = particles.get(Math.max(0, j[0] - 1));
                o.setInvisible(true);
                centre.getLevel().addParticle(n);
                centre.getLevel().addParticle(o);
            }
        }, 1);
    }

    public static void sendTitle(Player player, String... messages) {
        int[] j = new int[]{0};
        int end = messages.length - 1;
        Server.getInstance().getScheduler().scheduleDelayedRepeatingTask(new Task() {
            @Override
            public void onRun(int i) {
                String mess = messages[j[0]];
                player.sendTitle(mess,"",10, 15, 4);
                j[0] ++;
                if(j[0] > end) {
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
