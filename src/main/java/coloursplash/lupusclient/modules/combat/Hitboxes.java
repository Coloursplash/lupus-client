

package coloursplash.lupusclient.modules.combat;

import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;
import coloursplash.lupusclient.settings.DoubleSetting;
import coloursplash.lupusclient.settings.EntityTypeListSetting;
import coloursplash.lupusclient.settings.Setting;
import coloursplash.lupusclient.settings.SettingGroup;
import coloursplash.lupusclient.utils.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

public class Hitboxes extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Object2BooleanMap<EntityType<?>>> entities = sgGeneral.add(new EntityTypeListSetting.Builder()
            .name("entities")
            .description("Which entities to target.")
            .defaultValue(Utils.asObject2BooleanOpenHashMap(EntityType.PLAYER))
            .build()
    );

    private final Setting<Double> value = sgGeneral.add(new DoubleSetting.Builder()
            .name("expand")
            .description("How much to expand the hitbox of the entity.")
            .defaultValue(0.5)
            .build()
    );

    public Hitboxes() {
        super(Categories.Combat, "hitboxes", "Expands an entity's hitboxes.");
    }

    public double getEntityValue(Entity entity) {
        if (!isActive()) return 0;
        if (entities.get().getBoolean(entity.getType())) return value.get();
        return 0;
    }
}
