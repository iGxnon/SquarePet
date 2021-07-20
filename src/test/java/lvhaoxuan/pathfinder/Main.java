package lvhaoxuan.pathfinder;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Location;
import cn.nukkit.plugin.PluginBase;

public class Main extends PluginBase {

    public static Main ins;

    @Override
    public void onEnable() {
        ins = this;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player) {

            sender.sendMessage(((Player) sender).getPosition().x + ":" + ((Player) sender).getPosition().y + ":" + ((Player) sender).getPosition().z);

            if (args.length == 4 && args[0].equalsIgnoreCase("astar")) {
                int x = Integer.parseInt(args[1]);
                int y = Integer.parseInt(args[2]);
                int z = Integer.parseInt(args[3]);
                Player player = (Player) sender;
                Location loc = new Location(x, y, z, 0, 0, player.getLevel());
                AStarPathfinder pathfinder = new AStarPathfinder(player.getLocation(), loc, 20000);
                pathfinder.find();
                pathfinder.result();
            }
        }
        return true;
    }
}
