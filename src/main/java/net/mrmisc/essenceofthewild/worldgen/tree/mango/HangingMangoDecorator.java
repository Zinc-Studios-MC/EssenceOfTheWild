package net.mrmisc.essenceofthewild.worldgen.tree.mango;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.mrmisc.essenceofthewild.block.EOTWBlocks;
import net.mrmisc.essenceofthewild.worldgen.registry.EOTWTreeDecorators;

import java.util.ArrayList;
import java.util.List;

public class HangingMangoDecorator extends TreeDecorator {
    public static final Codec<HangingMangoDecorator> CODEC =
            Codec.intRange(1, 20).fieldOf("tries")
                    .xmap(HangingMangoDecorator::new, d -> d.tries)
                    .codec();

    private final int tries;

    public HangingMangoDecorator(int tries) {
        this.tries = tries;
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return EOTWTreeDecorators.MANGO_DECORATOR.get();
    }

    @Override
    public void place(Context ctx) {
        RandomSource random = ctx.random();
        List<BlockPos> leaves = new ArrayList<>(ctx.leaves());

        if (leaves.isEmpty()) return;

        int target = 3 + random.nextInt(4);
        int placed = 0;
        int attempts = 0;

        int maxAttempts = tries * 3;

        while (placed < target && attempts < maxAttempts) {
            attempts++;

            BlockPos leafPos = leaves.get(random.nextInt(leaves.size()));
            BlockPos fruitPos = leafPos.below();

            if (ctx.isAir(fruitPos) && ctx.isAir(fruitPos.below())) {
                ctx.setBlock(fruitPos, EOTWBlocks.MANGO.get().defaultBlockState());
                placed++;
            }
        }
    }
}
