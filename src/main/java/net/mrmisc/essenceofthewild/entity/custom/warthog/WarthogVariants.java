package net.mrmisc.essenceofthewild.entity.custom.warthog;

import net.mrmisc.essenceofthewild.entity.util.Variant;
import net.mrmisc.essenceofthewild.entity.util.VariantSet;
import net.mrmisc.essenceofthewild.util.EOTWUtils;
import java.util.List;

public class WarthogVariants {
    public static final Variant BASIC = variant("basic", "warthog");
    public static final Variant DESERT = variant("desert", "desert_warthog");
    public static final VariantSet<Variant> SET = VariantSet.of(Variant::id, List.of(BASIC, DESERT));

    private static Variant variant(String id, String name) {
        return new Variant(id, EOTWUtils.getLoc("textures/entity/warthog/" + name + ".png"),
                EOTWUtils.getLoc("textures/entity/warthog/baby_" + name + ".png"));
    }
}
