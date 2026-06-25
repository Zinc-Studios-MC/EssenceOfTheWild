package net.mrmisc.essenceofthewild.block.entity.custom.burrow;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
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
    private ArrayList<FerretEntity> ferretEntities = new ArrayList<>();

    public BurrowBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(EOTWBlockEntities.BURROW_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    public boolean addFerret(FerretEntity entity){
        if(canAddFerret()){
            ferretData.add(
                new FerretData(
                    entity.getStringUUID(), 
                    entity.getVariant().id(), 
                    entity.getOwnerUUID() == null ? "" : entity.getOwnerUUID().toString(),
                    entity.saveInventoryToTag(),
                    entity.isTame(),
                    entity.isInLove()
                )
            );
            setChanged();
            return true;
        }

        return false;
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
                ferret.loadInventoryFromTag(fd.inventory());
                switch (fd.variant()) {
                    case "basic":
                        ferret.setVariant(FerretVariants.BASIC);
                        break;
                    case "red":
                        ferret.setVariant(FerretVariants.RED);
                        break;
                    case "white":
                        ferret.setVariant(FerretVariants.WHITE);
                        break;
                }
                ferret.ticks = 28;
                ferret.setDiggingOut(true);
                ferret.setTame(fd.isTamed());
                ferret.setInLove((Player)ferret.getOwner());
                ferret.moveTo(this.getBlockPos(), 0, 0);
                this.getLevel().addFreshEntity(ferret);
                ferretEntities.add(ferret);
            }
        }
        if(ferretEntities.size()>1){
            FerretEntity en1 = ferretEntities.get(0);
            FerretEntity en2 = ferretEntities.get(1);
            if(en1.canMate(en2)){
                int bound = en1.getRandom().nextInt(1, 4);
                for(int i = 0; i < bound; i++){
                    en1.spawnChildFromBreeding((ServerLevel)en1.level(), en2);
                }
            }
        }
        ferretData.clear();
        setChanged();
    }

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
            CompoundTag ferretTag = new CompoundTag();
            ferretTag.putString("UUID", fd.ferretUUID());
            ferretTag.putString("Variant", fd.variant());
            ferretTag.putString("Owner", fd.ownerUUID());
            ferretTag.put("Inventory", fd.inventory());
            ferretTag.putBoolean("isTame", fd.isTamed());
            ferretTag.putBoolean("isInLove", fd.isInLove());
            pTag.put("Ferret"+i, ferretTag);
        }
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        ferretData.clear();
        for(int i = 0; i < 2; i++){
            String key = "Ferret"+i;
            Tag t = pTag.get(key);

            if(t instanceof CompoundTag ferretTag){
                ferretData.add(new FerretData(
                    ferretTag.getString("UUID"),
                    ferretTag.getString("Variant"),
                    ferretTag.getString("Owner"),
                    ferretTag.getCompound("Inventory"),
                    ferretTag.getBoolean("isTame"),
                    ferretTag.getBoolean("isInLove")
                ));
            } else if(t!=null){
                String[] array = t.getAsString().split("\\|", -1);

                if (array.length >= 5) {
                    ferretData.add(new FerretData(array[0], array[1], array[2], Boolean.getBoolean(array[3]), Boolean.getBoolean(array[4])));
                }
            }
        }
    }
}
