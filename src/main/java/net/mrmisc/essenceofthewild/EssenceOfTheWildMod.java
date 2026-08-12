package net.mrmisc.essenceofthewild;

import java.util.List;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.mrmisc.essenceofthewild.block.entity.EOTWBlockEntities;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
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
import net.mrmisc.essenceofthewild.entity.custom.cave_spider.CaveSpiderRenderer;
import net.mrmisc.essenceofthewild.entity.custom.spider.SpiderRenderer;
import net.mrmisc.essenceofthewild.entity.misc.arrow.UnderwaterArrowRenderer;
import net.mrmisc.essenceofthewild.entity.misc.silk_ball.SilkBallRenderer;
import net.mrmisc.essenceofthewild.item.EOTWItemProperties;
import net.mrmisc.essenceofthewild.menu.EOTWMenuTypes;
import net.mrmisc.essenceofthewild.screen.ferret.FerretScreen;
import net.mrmisc.essenceofthewild.screen.freezer.WoodenFreezerScreen;
import net.mrmisc.essenceofthewild.util.EOTWEntityUtils;
import net.mrmisc.essenceofthewild.util.EOTWUtils;

// this has to match an entry in META-INF/mods.toml
@Mod(EssenceOfTheWildMod.MOD_ID)
public class EssenceOfTheWildMod
{
    // mod id lives here so everything else can just point at it
    public static final String MOD_ID = "essenceofthewild";


    public EssenceOfTheWildMod(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();
        context.registerConfig(ModConfig.Type.COMMON, EOTWConfig.COMMON_SPEC);
        EOTWUtils.modInit(modEventBus);
        // hook up commonSetup for modloading
        modEventBus.addListener(this::commonSetup);

        // sign us up for the server and game events we care about
        MinecraftForge.EVENT_BUS.register(this);

        MinecraftForge.EVENT_BUS.register(new ServerInteractionListener());
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }

    // SubscribeEvent lets the event bus find methods to call on its own
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    }

    // EventBusSubscriber auto registers every static method in here marked with SubscribeEvent
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
            EntityRenderers.register(EOTWEntities.FERRET.get(), FerretRenderer::new);
            EntityRenderers.register(EOTWEntities.RAT.get(), RatRenderer::new);
            EntityRenderers.register(EOTWEntities.THROWN_DUCK_EGG.get(), ThrownItemRenderer::new);
            EntityRenderers.register(EOTWEntities.SPIDER.get(), SpiderRenderer::new);
            EntityRenderers.register(EOTWEntities.CAVE_SPIDER.get(), CaveSpiderRenderer::new);
            EntityRenderers.register(EOTWEntities.SILK_BALL.get(), SilkBallRenderer::new);
            BlockEntityRenderers.register(EOTWBlockEntities.MANGO_SIGN.get(), SignRenderer::new);
            BlockEntityRenderers.register(EOTWBlockEntities.MANGO_HANGING_SIGN.get(), HangingSignRenderer::new);
            MenuScreens.register(EOTWMenuTypes.WOODEN_FREEZER.get(), WoodenFreezerScreen::new);
            MenuScreens.register(EOTWMenuTypes.FERRET.get(), FerretScreen::new);
            ItemBlockRenderTypes.setRenderLayer(EOTWBlocks.NEST.get(), RenderType.cutout());
            event.enqueueWork(EOTWItemProperties::register);
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

            // assigning a rat to a composter
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

            // assigning a ferret its dig block
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