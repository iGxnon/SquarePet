package xyz.lightsky.squarepet.prop;

import lombok.Getter;
import xyz.lightsky.squarepet.Main;
import xyz.lightsky.squarepet.language.Lang;
import xyz.lightsky.squarepet.manager.MarketManager;
import xyz.lightsky.squarepet.prop.symbol.PetAcceptable;
import xyz.lightsky.squarepet.prop.symbol.TrainerAcceptable;
import xyz.lightsky.squarepet.trainer.Trainer;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class BaseProp {

    public static Map<Integer, BaseProp> propMap = new HashMap<>();

    public static void init() {
        Main.info(Lang.translate("%sys.prop.load%"));
        registerProp(LuckyStrawProp.class);
        registerProp(LittleHPProp.class);
        registerProp(LittleSPProp.class);
        registerProp(MiddleHPProp.class);
        registerProp(MiddleSPProp.class);
        registerProp(LargeHPProp.class);
        registerProp(LargeSPProp.class);
        registerProp(HPEnhanceProp.class);
        registerProp(SPEnhanceProp.class);
        registerProp(SkillStoneProp.class);
        registerProp(ResurrectionStoneProp.class);
        registerProp(炫迈.class);
        registerProp(LevelUPProp.class);
    }

    public static final Map<String, Integer> name2Id = new HashMap<>();

    @Deprecated
    public static void registerProp(int id, BaseProp prop) {
        if(id <= 10) return;
        propMap.put(id, prop);
        name2Id.put(prop.getName(), id);
    }

    public static int getID(String name) {
        return name2Id.get(name);
    }

    public static void registerProp(Class<? extends BaseProp> clazz) {
        try {
            int id = clazz.getField("ID").getInt(null);
            if(!PetAcceptable.class.isAssignableFrom(clazz) && !TrainerAcceptable.class.isAssignableFrom(clazz)) {
                Main.warning(clazz.getSimpleName() + " register failed，please confirm you had implemented 'Acceptable'!");
                return;
            }
            BaseProp prop = clazz.newInstance();
            propMap.put(id, prop);
            name2Id.put(prop.getName(), id);
        } catch (NoSuchFieldException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            Main.warning(clazz.getSimpleName() + " register failed!, do not find final field 'ID'");
        }
    }

    public static BaseProp getProp(int id) {
        if(id == 9) {
            Main.warning("We suggest you to use 'new SkillStoneProp() and setSkill(BaseSkill skill)' or 'MarketManager.getSkillStone(String skillName)' to create this prop");
            return null;
        }
        return propMap.get(id);
    }

    @Override
    public String toString() {
        return getName();
    }

    public abstract String getName();

    public abstract int getId();

    public abstract String getInfo();

    public int get$Cost() {
        return MarketManager.getBaseProp$Cost(getId());
    }

    public boolean work(Trainer trainer) {
        return onUseToTrainer(trainer);
    }

    /**
     *
     * @param trainer trainer
     * @param petType type of pet
     * @return isWorked , used to check if reduce prop
     */
    public boolean work(Trainer trainer, String petType) {
        return onUseToPet(trainer, petType);
    }

    boolean onUseToPet(Trainer trainer, String petType){
        return false;
    }

    boolean onUseToTrainer(Trainer trainer){
        return false;
    }
}
