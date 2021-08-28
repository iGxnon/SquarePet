package cc.igxnon.square.pet.trainer;

import cc.igxnon.square.pet.pets.PetDescription;
import cn.nukkit.Player;

/**
 * @author iGxnon
 * @date 2021/08/27
 */
public interface ITrainer {

    /**
     * 放出宠物
     * @param identifier 宠物标识符
     * @return 是否释放成功
     */
    boolean spawnPet(String identifier);

    /**
     * 收回宠物
     * @param identifier 宠物标识符
     * @return 是否收回成功
     */
    boolean closePet(String identifier);

    /**
     * @return 获取训练师信息
     */
    TrainerDescription getDescription();

    /**
     * @return 获取训练师等级
     */
    int getTrainerLv();

    /**
     * @return 获取训练师所对应的玩家对象
     */
    Player getPlayer();

    /**
     * @return 获取训练师的经验值
     */
    int getTrainerExp();

    /**
     * @param identifier 宠物标识符
     * @return 获取宠物信息
     */
    PetDescription getPetDescription(String identifier);


}
