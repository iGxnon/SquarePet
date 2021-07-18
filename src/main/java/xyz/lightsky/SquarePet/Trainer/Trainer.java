package xyz.lightsky.SquarePet.Trainer;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Config;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import xyz.lightsky.ModelManagerRe.ModelManagerRe;
import xyz.lightsky.SquarePet.Main;
import xyz.lightsky.SquarePet.Manager.ConfigManager;
import xyz.lightsky.SquarePet.Manager.PetManager;
import xyz.lightsky.SquarePet.Manager.TrainerManager;
import xyz.lightsky.SquarePet.Pet.Attribute;
import xyz.lightsky.SquarePet.Pet.BaseSquarePet;
import xyz.lightsky.SquarePet.Pet.PetResourceAche;
import xyz.lightsky.SquarePet.Tools;

import java.io.File;
import java.util.*;

@Getter
@Setter
@ToString(exclude = {"cfg", "player", "spawnedPets"})
public class Trainer {

    private Config cfg;
    private String name;
    private Player player;
    private int level;
    private int maxLevel;
    private int exp;
    private int maxExp;
    private String prefix;
    private int luckRate;
    private Lineup lineup;
    private Bag bag;

    /**当宠物释放时读取spawnedPets为实时数据, 当宠物收回时,读取petMap为实时数据*/
    private Map<String, BaseSquarePet> spawnedPets = new HashMap<>();
    private Map<String, PetResourceAche> petMap = new HashMap<>();

    private List<String> petTypes = new ArrayList<>();

    private BaseSquarePet onRide = null;

    public Trainer(Player player) {
        this.name = player.getName();
        this.player = player;
        File info = new File(this.getPlayerFolder() + "/信息.yml");
        Config bagDat = new Config(getPlayerFolder() + "/背包.yml");
        if (!info.exists()) {
            this.cfg = new Config(info, 2);
            this.cfg.set("名称", player.getName());
            this.cfg.set("等级", 1);
            this.cfg.set("经验", 0);
            this.cfg.set("头衔", "");
            this.cfg.set("幸运值", (new Random()).nextInt(100));
            this.cfg.set("宠物阵容", new ArrayList());
            this.cfg.save();
            (new File(this.getPlayerFolder(), "宠物")).mkdirs();
            Main.info("正在创建" + player.getName() + "的数据");
        } else {
            this.cfg = new Config(info, 2);
        }
        this.prefix = this.cfg.getString("头衔");
        this.luckRate = this.cfg.getInt("幸运值");
        this.bag = new Bag(this, bagDat);
        this.level = this.cfg.getInt("等级");
        this.exp = this.cfg.getInt("经验");
        this.maxLevel = ConfigManager.getTrainerMaxLv();
        this.lineup = new Lineup(this.cfg.getStringList("宠物阵容"), this);
        updateMaxExp();
        refreshPetList();
        refreshLineup();
    }

    public void refreshPetList() {
        File path = new File(this.getPlayerFolder() + "宠物/");
        for (File file : Objects.requireNonNull(path.listFiles())) {
            if (file.getName().endsWith(".yml")) {
                String type = file.getName().split("\\.")[0];
                if(petTypes.contains(type)) continue;
                PetResourceAche ache = new PetResourceAche();
                Config config = new Config(file);
                ache.setName(config.getString("名称"));
                ache.setOwnerName(config.getString("主人"));
                ache.setOwner(this);
                ache.setType(config.getString("类型"));
                ache.setModelName(config.getString("模型"));
                ache.setAttributeStr(config.getString("属性"));
                ache.setAttribute(Attribute.of(ache.getAttributeStr()));
                ache.setLv(config.getInt("等级"));
                ache.setExp(config.getInt("经验"));
                ache.setMaxLv(PetManager.getMaxLv(type));
                ache.setMaxExp(PetManager.getPetNeedExp(type, ache.getLv()));
                ache.setHp(config.getInt("血量"));
                ache.setMaxHP(config.getInt("最大血量"));
                ache.setAttack(config.getDouble("攻击"));
                ache.setAttackSpeed(config.getDouble("攻速"));
                ache.setDefenceRate(config.getDouble("防御"));
                ache.setCritRate(config.getDouble("暴击率"));
                ache.setCritTimeRate(config.getDouble("暴击倍率"));
                ache.setSp(config.getInt("SP"));
                ache.setMaxSP(config.getInt("最大SP"));
                ache.setCd(config.getInt("CD"));
                ache.setScale(config.getDouble("大小"));
                ache.setSpRecoverRate(config.getInt("SP恢复速率"));
                ache.setSpLossRate(config.getDouble("SP损耗率"));
                ache.setFoods(config.getStringList("食物"));
                ache.setSkills(config.getStringList("技能"));
                ache.setMountedOffSet(PetManager.getBaseMountedOffSet(type));
                ache.setPreDead(ache.getHp() <= 0);
                ache.setInLineup(getLineup().contains(type));
                petMap.putIfAbsent(type, ache);
                petTypes.add(type);
            }
        }
    }

    public void refreshLineup() {
        lineup.update();
    }

    public void refreshAche(BaseSquarePet pet) {
        String type = pet.getType();
        PetResourceAche ache = petMap.get(type);
        if(ache != null) {
            ache.setName(pet.getName());
            ache.setLv(pet.getLv());
            ache.setExp(pet.getExp());
            ache.setMaxExp(PetManager.getPetNeedExp(type, ache.getLv()));
            ache.setHp((int) pet.getHealth());
            ache.setMaxHP(pet.getMaxHealth());
            ache.setAttack(pet.getAttack());
            ache.setAttackSpeed(pet.getAttackSpeed());
            ache.setDefenceRate(pet.getDefenceRate());
            ache.setCritRate(pet.getCritRate());
            ache.setCritTimeRate(pet.getCritTimeRate());
            ache.setSp(pet.getSp());
            ache.setMaxSP(pet.getMaxSP());
            ache.setCd(pet.getCd());
            ache.setScale(pet.getScale());
            ache.setSpRecoverRate(pet.getSpRecoverRate());
            ache.setSpLossRate(pet.getSpLossRate());
            ache.setSkills(pet.getSkillNames());
            ache.setPreDead(pet.isPreDead());
            ache.setInLineup(pet.isInLineup());
        }
    }

    public String getPetInfo(String type) {
        if(this.spawnedPets.containsKey(type)) {
            return this.spawnedPets.get(type).getInfo();
        }
        PetResourceAche ache = getPetMap().get(type);
        if(ache == null) return "";
        return "名称: " + ache.getName() + "\n"
                + "类别: " + ache.getType() + "\n"
                + "属性: " + ache.getAttribute().toString() + "\n"
                + "等级: " + ache.getLv() + "/" + PetManager.getMaxLv(ache.getType()) + "\n"
                + "经验: " + getExp() + "/" + getMaxExp() + "\n"
                + "SP: " + ache.getSp() + "/" + ache.getMaxSP() + "\n"
                + "血量: " + ache.getHp() + "/" + ache.getMaxHP() + "\n"
                + "攻击: " + ache.getAttack() + "\n"
                + "攻速: " + ache.getAttackSpeed() + "\n"
                + "防御: " + ache.getDefenceRate() + "\n"
                + "暴击率: " + ache.getCritRate() * 100 + "%" + "  幸运值加成: " + getLuckilyUpRate() * 100 + "%" + "\n"
                + "暴击倍率: " + ache.getCritTimeRate() * 100 + "%" + "\n"
                + "SP恢复速率: " + String.format("%.1f", 100F / ((float) ache.getSpRecoverRate())) + "\n"
                + "SP损失率: " + ache.getSpLossRate() * 100 + "%" + "\n"
                + "食物: " + Tools.convertItem(ache.getFoods().toString()) + "\n"
                + "技能: " + ache.getSkills().toString();
    }

    private float getLuckilyUpRate() {
        double rate = ((double) getLuckRate()) / 100D;
        return (float) (rate * ConfigManager.getMaxLuckilyUpRate());
    }

    public void spawnPet(String type) {
        if(type == null) return;
        if(!petMap.containsKey(type)) {
            sendMessage("你没有该宠物!");
        }
        if(petMap.get(type).isPreDead()) {
            sendMessage("该宠物已经濒死，请使用复活石复活!");
            return;
        }
        CompoundTag nbt = PetManager.createTag(player, petMap.get(type));
        BaseSquarePet pet = new BaseSquarePet(player.chunk, nbt);
        pet.setSkin(ModelManagerRe.getModel(pet.getModelName()));
        pet.spawnToAll();
        spawnedPets.put(type, pet);
        sendMessage("成功召唤该宠物 " + pet.getName());
    }

    public void closePet(String type) {
        spawnedPets.get(type).close();
        spawnedPets.remove(type);
        sendMessage("成功收回该宠物");
    }

    public void closeAllPets() {
        spawnedPets.keySet().forEach(s->{
            spawnedPets.get(s).close();
        });
        spawnedPets.clear();
    }

    public boolean hasSpawnedPet(String type) {
        return spawnedPets.containsKey(type);
    }

    public void addPet(String type) {
        if(ConfigManager.getPetContains(this) <= getPetTypes().size()) {
            sendMessage("宠物容量不够,请升级获取更多!");
            return;
        }
        Config petConf = new Config(getPlayerFolder() + "/宠物/" + type + ".yml", Config.YAML);
        petConf.set("名称", type);
        petConf.set("主人", getName());
        petConf.set("类型", type);
        petConf.set("模型", PetManager.getModel(type));
        petConf.set("属性", PetManager.getAttributeAsString(type));
        petConf.set("等级", 1);
        petConf.set("经验", 0);
        petConf.set("血量", PetManager.getBaseHp(type));
        petConf.set("最大血量", PetManager.getBaseHp(type));
        petConf.set("攻击", PetManager.getBaseAttack(type));
        petConf.set("攻速", PetManager.getBaseAttackSpeed(type));
        petConf.set("防御", PetManager.getBaseDefence(type));
        petConf.set("暴击率", PetManager.getCritRate(type));
        petConf.set("暴击倍率", PetManager.getCritTimeRate(type));
        petConf.set("SP", PetManager.getBaseMaxSP(type));
        petConf.set("最大SP", PetManager.getBaseMaxSP(type));
        petConf.set("CD", PetManager.getBaseCD(type));
        petConf.set("大小", PetManager.getMinSize(type));
        petConf.set("SP恢复速率", PetManager.getSpRecoverRate(type));
        petConf.set("SP损耗率", PetManager.getSpLossRate(type));
        petConf.set("食物", PetManager.getFoods(type));
        petConf.set("技能", new ArrayList<>());
        petConf.save();
        sendMessage("成功添加宠物" + type);
    }

    public void updateMaxExp() {
        this.maxExp = ConfigManager.getTrainerNeedExp(this.level);
    }

    public String getPlayerFolder() {
        return Main.getInstance().getDataFolder() + "/玩家数据/" + this.player.getName() + "/";
    }

    public void sendMessage(String s) {
        if(player.isOnline()) {
            this.player.sendMessage(s);
        }
    }

    public String getInfo() {
        return "名称: " + getName() + "\n"
                + "等级: " + getLevel() + "/" + getMaxLevel() + "\n"
                + "经验: " + getExp() + "/" + getMaxExp() + "\n"
                + "幸运值: " + getLuckRate() + "\n"
                + "头衔: " + getPrefix() + "\n"
                + "阵容: " + getLineup().toArray() + "\n"
                + "宠物列表: " + petTypes.toString();
    }

    public void levelUP() {
        if(this.level != maxLevel) {
            sendMessage("成功升级, 现在等级" + this.level + 1 + "级");
        }
        this.level = Math.min(this.level + 1, maxLevel);
        updateMaxExp();
    }

    public void levelJump(int value) {
        if(this.level != maxLevel) {
            sendMessage("成功升级, 现在等级" + Math.min(this.level + value, maxLevel) + "级");
        }
        this.level = Math.min(this.level + value, maxLevel);
        updateMaxExp();
    }

    private void gainExp(int value, int count) {
        this.exp += value;
        if(this.exp >= maxExp) {
            this.exp -= maxExp;
            count ++;
            maxExp = ConfigManager.getTrainerNeedExp(this.level + count);
            gainExp(0, count);
        }else {
            levelJump(count);
        }
    }

    public void gainExp(int value) {
        gainExp(value, 0);
    }

    public void onAttack(EntityDamageByEntityEvent source) {
        getSpawnedPets().values().forEach(s->s.setTarget(source.getDamager()));
    }

    public void onDamage(Entity target) {
        getSpawnedPets().values().forEach(s->s.setTarget(target));
    }

    public void save() {
        bag.save();
        lineup.save();
        this.cfg.set("名称", player.getName());
        this.cfg.set("等级", getLevel());
        this.cfg.set("经验", getExp());
        this.cfg.set("头衔", getPrefix());
        this.cfg.set("幸运值", getLuckRate());
        this.cfg.set("宠物阵容", getLineup().toArray());
        this.cfg.save();
    }

    public void close() {
        save();
        closeAllPets();
        TrainerManager.trainerMap.remove(getName());
        petMap.clear();
    }

    public void usePropToSelf(int propId) {
        getBag().use(propId);
    }

    public void usePropToPet(int propId, String type) {
        getBag().use(propId, type);
    }

}
