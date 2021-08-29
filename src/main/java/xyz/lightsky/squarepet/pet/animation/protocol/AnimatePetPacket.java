package xyz.lightsky.squarepet.pet.animation.protocol;

import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.ProtocolInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AnimatePetPacket extends DataPacket {


    public static final byte NETWORK_ID = ProtocolInfo.ANIMATE_ENTITY_PACKET;

    private String animation;
    private String nextState;
    private String stopExpression;
    private String controller;
    private float blendOutTime;
    private List<Long> entityRuntimeIds = new ArrayList<>();

    // 客户端一般不会发这个包
    @Override
    public void decode() {
    }

    @Override
    public void encode() {
        this.reset();
        this.putString(this.animation);
        this.putString(this.nextState == null ? "default" : this.nextState);
        this.putString(this.stopExpression == null ? "" : this.stopExpression);
        this.putString(this.controller == null ? "" : this.controller);
        this.putLFloat(this.blendOutTime == 0F ? 100F : this.blendOutTime);
        this.putUnsignedVarInt(this.entityRuntimeIds.size());
        for (long entityRuntimeId : this.entityRuntimeIds){
            this.putEntityRuntimeId(entityRuntimeId);
        }
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

}
