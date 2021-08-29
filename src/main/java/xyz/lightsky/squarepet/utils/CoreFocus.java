package xyz.lightsky.squarepet.utils;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.DustParticle;
import cn.nukkit.level.particle.LavaDripParticle;
import cn.nukkit.nbt.tag.CompoundTag;

public class CoreFocus extends EntityProjectile {

    private int tick;

    public CoreFocus(FullChunk chunk, CompoundTag nbt, Entity shootingEntity) {
        super(chunk, nbt, shootingEntity);
    }

    @Override
    public int getNetworkId() {
        return this.namedTag.getInt("NetworkId");
    }

    @Override
    public boolean onUpdate(int currentTick) {
        level.addParticle(new LavaDripParticle(this));
        level.addParticle(new DustParticle(this, 255, 0 ,0));
        lock();
        tick ++;
        if(distanceSquared(shootingEntity) < 0.49){
            level.addParticle(new DustParticle(this, 255, 0 ,0));
            close();
        }

        double rateX = shootingEntity.x - this.x;
        double rateY = shootingEntity.y - this.y;
        double rateZ = shootingEntity.z - this.z;

        double all = Math.abs(rateX) + Math.abs(rateY) + Math.abs(rateZ);

        rateX = rateX / all;
        rateY = rateY / all;
        rateZ = rateZ / all;

        if(tick < 20){
            level.addParticle(new DustParticle(this, 255, 0 ,0));
            move(rateX * 0.3, rateY * 0.3, rateZ * 0.3);
        }
        if(tick > 20){
            move(rateX * 1.5, rateY * 1.5, rateZ * 1.5);
        }
        if(tick > 100){
            level.addParticle(new DustParticle(this, 255, 0 ,0));
            close();
        }

        updateMovement();
        return true;
    }



    // special for projectile
    private void lock() {
        double horizontal = Math.sqrt(Math.pow((this.shootingEntity.x - this.x),2) + Math.pow((this.shootingEntity.z - this.z),2));
        double vertical = this.shootingEntity.y + (double) this.shootingEntity.getEyeHeight() - this.y;
        double atan2 = Math.atan2(vertical, horizontal) / Math.PI * 180;

        if(atan2 > 90) {
            this.pitch = atan2 + 180;
        }else if(atan2 > -90) {
            this.pitch = atan2;
        }else if(atan2 > -180) {
            this.pitch = atan2 - 180;
        }

        double xDist = this.shootingEntity.x - this.x;
        double zDist = this.shootingEntity.z - this.z;
        this.yaw = - Math.atan2(zDist, xDist) / Math.PI * 180 - 270;
        if(this.yaw < 0){
            this.yaw += 360.0;
        }
    }
}
