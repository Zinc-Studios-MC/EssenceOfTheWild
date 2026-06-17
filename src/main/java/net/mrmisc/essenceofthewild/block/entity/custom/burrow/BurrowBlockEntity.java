package net.mrmisc.essenceofthewild.block.entity.custom.burrow;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.mrmisc.essenceofthewild.block.entity.EOTWBlockEntities;
import net.mrmisc.essenceofthewild.entity.EOTWEntities;
import net.mrmisc.essenceofthewild.entity.custom.ferret.FerretData;
import net.mrmisc.essenceofthewild.entity.custom.ferret.FerretEntity;
import net.mrmisc.essenceofthewild.entity.custom.ferret.FerretVariants;

public class BurrowBlockEntity extends BlockEntity{

    private ArrayList<FerretData> ferretData = new ArrayList<>();

    public BurrowBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(EOTWBlockEntities.BURROW_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    public void addFerret(FerretEntity entity){
        if(canAddFerret()){
            ferretData.add(
                new FerretData(
                    entity.getStringUUID(), 
                    entity.getVariant().id(), 
                    entity.getOwnerUUID() == null ? "" : entity.getOwnerUUID().toString()
                )
            );
        }
    }

    public List<FerretData> getFerretData(){
        return ferretData;
    }

    public boolean canAddFerret(){
        return ferretData.size()<2;
    }

    public void extractFerrets(){
        for(FerretData fd : ferretData){
            if(!this.getLevel().isClientSide){
                FerretEntity ferret = new FerretEntity(EOTWEntities.FERRET.get(), this.getLevel());
                ferret.setUUID(UUID.fromString(fd.ferretUUID()));
                if(!fd.ownerUUID().equals("")){
                    ferret.setOwnerUUID(UUID.fromString(fd.ownerUUID()));
                }
                switch (fd.variant()) {
                    case "basic":
                        ferret.setVariant(FerretVariants.BASIC);
                        break;
                    case "red_ferret":
                        ferret.setVariant(FerretVariants.RED_FERRET);
                        break;
                    case "white_ferret":
                        ferret.setVariant(FerretVariants.WHITE_FERRET);
                        break;
                }
                ferret.ticks = 28;
                ferret.setDiggingOut(true);
                ferret.moveTo(this.getBlockPos(), 0, 0);
                this.getLevel().addFreshEntity(ferret);
            }
        }
        ferretData.clear();
    }

    //will do the breeding thing later
    public static void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        if(!(blockEntity instanceof BurrowBlockEntity bbe)){
            return;
        }
        if(!level.isClientSide()){
            if(level.isDay() && !bbe.getFerretData().isEmpty()){
                bbe.extractFerrets();
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        for(int i = 0; i < ferretData.size(); i++){
            FerretData fd = ferretData.get(i);
            pTag.putString("Ferret"+i, fd.ferretUUID()+"|"+fd.variant()+"|"+fd.ownerUUID());
        }
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        ferretData.clear();
        for(int i = 1; i < 3; i++){
            Tag t = pTag.get("Ferret"+i);
            if(t!=null){
                String[] array = t.getAsString().split("|");
                FerretData fd = new FerretData(array[0], array[1], array[2]);
                ferretData.add(fd);
            }
        }
    }
}