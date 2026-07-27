package net.mrmisc.essenceofthewild;

import java.util.List;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.mrmisc.essenceofthewild.block.EOTWBlocks;
import net.mrmisc.essenceofthewild.config.EOTWConfig;
import net.mrmisc.essenceofthewild.entity.EOTWEntities;
import net.mrmisc.essenceofthewild.entity.custom.chicken.ChickenRenderer;
import net.mrmisc.essenceofthewild.entity.custom.cow.CowRenderer;
import net.mrmisc.essenceofthewild.entity.custom.duck.DuckRenderer;
import net.mrmisc.essenceofthewild.entity.custom.ferret.FerretEntity;
import net.mrmisc.essenceofthewild.entity.custom.ferret.FerretRenderer;
import net.mrmisc.essenceofthewild.entity.custom.hare.HareRenderer;
import net.mrmisc.essenceofthewild.entity.custom.mooshroom.MooshroomRenderer;
import net.mrmisc.essenceofthewild.entity.custom.pig.PigRenderer;
import net.mrmisc.essenceofthewild.entity.custom.rabbit.RabbitRenderer;
import net.mrmisc.essenceofthewild.entity.custom.rat.RatEntity;
import net.mrmisc.essenceofthewild.entity.custom.rat.RatRenderer;
import net.mrmisc.essenceofthewild.entity.custom.sheep.SheepRenderer;
import net.mrmisc.essenceofthewild.entity.misc.arrow.UnderwaterArrowRenderer;
import net.mrmisc.essenceofthewild.menu.EOTWMenuTypes;
import net.mrmisc.essenceofthewild.screen.ferret.FerretScreen;
import net.mrmisc.essenceofthewild.screen.freezer.WoodenFreezerScreen;
import net.mrmisc.essenceofthewild.util.EOTWEntityUtils;
import net.mrmisc.essenceofthewild.util.EOTWUtils;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(EssenceOfTheWildMod.MOD_ID)
public class EssenceOfTheWildMod
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "essenceofthewild";


    public EssenceOfTheWildMod(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();
        context.registerConfig(ModConfig.Type.COMMON, EOTWConfig.COMMON_SPEC);
        EOTWUtils.modInit(modEventBus);
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        MinecraftForge.EVENT_BUS.register(new ServerInteractionListener());
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            EOTWUtils.clientInit();
            EntityRenderers.register(EOTWEntities.SHEEP.get(), SheepRenderer::new);
            EntityRenderers.register(EOTWEntities.PIG.get(), PigRenderer::new);
            EntityRenderers.register(EOTWEntities.COW.get(), CowRenderer::new);
            EntityRenderers.register(EOTWEntities.MOOSHROOM.get(), MooshroomRenderer::new);
            EntityRenderers.register(EOTWEntities.CHICKEN.get(), ChickenRenderer::new);
            EntityRenderers.register(EOTWEntities.DUCK.get(), DuckRenderer::new);
            EntityRenderers.register(EOTWEntities.RABBIT.get(), RabbitRenderer::new);
            EntityRenderers.register(EOTWEntities.HARE.get(), HareRenderer::new);
            EntityRenderers.register(EOTWEntities.UNDERWATER_ARROW.get(), UnderwaterArrowRenderer::new);
            EntityRenderers.register(EOTWEntities.UNDERWATER_ARROW.get(), UnderwaterArrowRenderer::new);
            EntityRenderers.register(EOTWEntities.FERRET.get(), FerretRenderer::new);
            EntityRenderers.register(EOTWEntities.RAT.get(), RatRenderer::new);
            MenuScreens.register(EOTWMenuTypes.WOODEN_FREEZER.get(), WoodenFreezerScreen::new);
            MenuScreens.register(EOTWMenuTypes.FERRET.get(), FerretScreen::new);
            ItemBlockRenderTypes.setRenderLayer(EOTWBlocks.NEST.get(), RenderType.cutout());
        }
    }
    @Mod.EventBusSubscriber(modid = EssenceOfTheWildMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public class ServerInteractionListener {
        @SubscribeEvent
        public void onInteract(PlayerInteractEvent.RightClickBlock event) {
            if (event.getLevel().isClientSide()) return;
            Player p = event.getEntity();
            if(!event.getItemStack().is(Items.STICK)){
                return;
            }

            // Rat -> composter assignment.
            String ratUuid = EOTWEntityUtils.getRatClicked(p);
            if(!ratUuid.isEmpty()
                    && event.getLevel().getBlockState(event.getPos()).is(net.minecraft.world.level.block.Blocks.COMPOSTER)){
                List<RatEntity> lre = event.getLevel().getEntitiesOfClass(RatEntity.class, p.getBoundingBox().inflate(200),
                        (e) -> e.getStringUUID().equals(ratUuid));
                if(!lre.isEmpty()){
                    lre.get(0).assignComposter(event.getPos());
                    EOTWEntityUtils.removeRatClicked(p);
                    p.displayClientMessage(
                            net.minecraft.network.chat.Component.translatable("message.essenceofthewild.rat.assigned"), true);
                    return;
                }
            }

            // Ferret -> dig-block assignment.
            if(!p.getPersistentData().contains("OwnsFerret")){
                return;
            }
            String uuid = EOTWEntityUtils.getPlayerClicked(p);
            if(uuid.equals("")){
                return;
            }
            List<FerretEntity> lfe = event.getLevel().getEntitiesOfClass(FerretEntity.class, p.getBoundingBox().inflate(200), (e) ->{
                return e.getStringUUID().equals(uuid);
            });
            if(lfe.isEmpty()){
                return;
            }
            FerretEntity fe = lfe.get(0);
            fe.setBlockToDig(event.getPos());
        }
    }
}