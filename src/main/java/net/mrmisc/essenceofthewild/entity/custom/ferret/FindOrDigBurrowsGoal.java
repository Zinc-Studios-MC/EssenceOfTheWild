package net.mrmisc.essenceofthewild.entity.custom.ferret;

import java.util.ArrayList;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.mrmisc.essenceofthewild.block.EOTWBlocks;
import net.mrmisc.essenceofthewild.block.entity.custom.burrow.BurrowBlockEntity;

//blank cause i'm still planning
public class FindOrDigBurrowsGoal extends Goal{

    private final FerretEntity entity;
    private boolean hasToDigOwn = false;
    private BlockPos pos = null;
    private int ticks = 0;

    public FindOrDigBurrowsGoal(FerretEntity pEntity){
        this.entity = pEntity;
    }

    @Override
    public boolean canUse() {
        return this.entity.level().isNight() && !this.entity.isBaby();
    }

    @Override
    public void start() {
        super.start();
        ticks = 0;
        hasToDigOwn = false;
        pos = getBurrow();
    }

    @Override
    public void tick() {
        super.tick();
        if(hasToDigOwn){
            BlockPos bp = getBurrow();
            if(bp.getX() == pos.getX()&&bp.getY() == pos.getY()&&bp.getZ() == pos.getZ()){
                if(ticks <= 0){
                    this.entity.setDiggingIn(true);
                    ticks = 30;
                }else if(ticks == 1){
                    if(
                        this.entity.level().getBlockState(pos).is(Blocks.DIRT)
                    ){
                        this.entity.level().setBlock(pos, EOTWBlocks.DIRT_BURROW_BLOCK.get().defaultBlockState(), 2);
                    }else if(
                        this.entity.level().getBlockState(pos).is(Blocks.SAND)
                    ){
                        this.entity.level().setBlock(pos, EOTWBlocks.SAND_BURROW_BLOCK.get().defaultBlockState(), 2);
                    }else if(
                        this.entity.level().getBlockState(pos).is(Blocks.MUD)
                    ){
                        this.entity.level().setBlock(pos, EOTWBlocks.MUD_BURROW_BLOCK.get().defaultBlockState(), 2);
                    }else if(
                        this.entity.level().getBlockState(pos).is(EOTWBlocks.DIRT_BURROW_BLOCK.get())||
                        this.entity.level().getBlockState(pos).is(EOTWBlocks.SAND_BURROW_BLOCK.get())||
                        this.entity.level().getBlockState(pos).is(EOTWBlocks.MUD_BURROW_BLOCK.get())
                    ){
                        if(this.entity.level().getBlockEntity(pos) instanceof BurrowBlockEntity bbe && !bbe.canAddFerret()){
                            pos = pos.east();
                        }
                    }else{
                        this.entity.level().setBlock(pos, EOTWBlocks.DIRT_BURROW_BLOCK.get().defaultBlockState(), 2);
                    }
                    if(this.entity.level().getBlockEntity(pos) instanceof BurrowBlockEntity bbe){
                        bbe.addFerret(entity);
                        this.entity.discard();
                    }
                    --ticks;
                }else{
                    --ticks;
                }
            }else{
                ticks = 0;
                pos = bp;
                this.entity.setDiggingIn(false);
            }
        }else{
            BlockPos bp = getBurrow();
            if(bp.getX() == pos.getX()&&bp.getY() == pos.getY()&&bp.getZ() == pos.getZ()){
            }else{
                ticks = 0;
                pos = bp;
                this.entity.setDiggingIn(false);
            }
            this.entity.getNavigation().moveTo(pos.getX(), pos.getY(), pos.getZ(), 0.7);
            if(this.entity.distanceToSqr(pos.getX(), pos.getY(), pos.getZ())<3){
                if(ticks <= 0){
                    this.entity.setDiggingIn(true);
                    ticks = 30;
                }else if(ticks == 1){
                    if(this.entity.level().getBlockEntity(pos) instanceof BurrowBlockEntity bbe){
                        bbe.addFerret(entity);
                        this.entity.discard();
                    }
                    --ticks;
                }else{
                    --ticks;
                }
            }
        }
    }

    @Override
    public void stop() {
        super.stop();
    }

    private BlockPos getBurrow(){
        AABB bound = this.entity.getBoundingBox().inflate(10);
        ArrayList<BlockPos> availableBlocks = new ArrayList<>();
        ArrayList<BlockPos> usableBlocks = new ArrayList<>();
        for(double x = bound.minX; x < bound.maxX; x++){
            for(double y = bound.minY; y < bound.maxY; y++){
                for(double z = bound.minZ; z < bound.maxZ; z++){
                    BlockPos pos = new BlockPos((int)x, (int)y, (int)z);
                    if(
                        this.entity.level().getBlockState(pos).is(EOTWBlocks.DIRT_BURROW_BLOCK.get())||
                        this.entity.level().getBlockState(pos).is(EOTWBlocks.SAND_BURROW_BLOCK.get())||
                        this.entity.level().getBlockState(pos).is(EOTWBlocks.MUD_BURROW_BLOCK.get())
                    ){
                        availableBlocks.add(pos);
                    }
                }
            }
        }
        for(BlockPos bp : availableBlocks){
            if(this.entity.level().getBlockEntity(bp) instanceof BurrowBlockEntity bbe){
                if(bbe.canAddFerret()){
                    usableBlocks.add(bp);
                }
            }
        }
        if(!usableBlocks.isEmpty()){
            hasToDigOwn = false;
            return usableBlocks.get(0);
        }
        hasToDigOwn = true;
        return this.entity.getOnPos();
    }
}