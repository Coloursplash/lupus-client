package coloursplash.lupusclient.modules.combat;

import java.util.ArrayList;
import java.util.List;

import meteordevelopment.orbit.EventHandler;
import coloursplash.lupusclient.events.world.TickEvent;
import coloursplash.lupusclient.friends.Friends;
import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;
import coloursplash.lupusclient.settings.BoolSetting;
import coloursplash.lupusclient.settings.DoubleSetting;
import coloursplash.lupusclient.settings.EnumSetting;
import coloursplash.lupusclient.settings.Setting;
import coloursplash.lupusclient.settings.SettingGroup;
import coloursplash.lupusclient.utils.entity.EntityUtils;
import coloursplash.lupusclient.utils.entity.SortPriority;
import coloursplash.lupusclient.utils.player.PlayerUtils;
import coloursplash.lupusclient.utils.player.RotationUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BowAimbot extends Module {
    public enum Priority {
        LowestDistance,
        HighestDistance,
        LowestHealth,
        HighestHealth
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> players = sgGeneral.add(new BoolSetting.Builder()
            .name("player-only")
            .description("Whether or not to just attack players.")
            .defaultValue(false)
            .build()
    );

    private final Setting<SortPriority> priority = sgGeneral.add(new EnumSetting.Builder<SortPriority>()
        .name("priority")
        .description("What type of entities to target.")
        .defaultValue(SortPriority.LowestHealth)
        .build()
    );
    
    private final Setting<Double> range = sgGeneral.add(new DoubleSetting.Builder()
            .name("range")
            .description("The maximum range the entity can be to attack it.")
            .defaultValue(40)
            .min(0)
            .max(100)
            .sliderMax(100)
            .build()
    );

    private final Setting<Boolean> friends = sgGeneral.add(new BoolSetting.Builder()
            .name("friends")
            .description("Whether or not to attack friends.")
            .defaultValue(false)
            .build()
    );

    private final Setting<Boolean> ambient = sgGeneral.add(new BoolSetting.Builder()
            .name("ambient-mobs")
            .description("Whether or not to attack ambient mobs.")
            .defaultValue(false)
            .build()
    );

    private final Setting<Boolean> babies = sgGeneral.add(new BoolSetting.Builder()
            .name("babies")
            .description("Whether or not to attack baby variants of the entity.")
            .defaultValue(true)
            .build()
    );

    private final Setting<Boolean> pets = sgGeneral.add(new BoolSetting.Builder()
            .name("pets")
            .description("Whether or not to attack tamed mobs.")
            .defaultValue(false)
            .build()
    );

    private final Setting<Boolean> nametagged = sgGeneral.add(new BoolSetting.Builder()
            .name("nametagged")
            .description("Whether or not to attack mobs with a name tag.")
            .defaultValue(false)
            .build()
    );

    public BowAimbot() {
        super(Categories.Combat, "bow-aimbot", "Automatically aims your (cross)bow at entities.");
    }
    
    private final List<Entity> entityList = new ArrayList<>();

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        // check if item is ranged weapon
        ItemStack stack = mc.player.inventory.getMainHandStack();
        Item item = stack.getItem();
        if (!(item instanceof BowItem || item instanceof CrossbowItem)) {
            entityList.clear();
            return;
        }
        
        // check if using bow/crossbow and is loaded
        if (item instanceof BowItem && !mc.options.keyUse.isPressed() && !mc.player.isUsingItem()) {
            entityList.clear();
            return;
        }
        if (item instanceof CrossbowItem && !CrossbowItem.isCharged(stack)) {
            entityList.clear();
            return;
        }

        // set target
		EntityUtils.getList(entity -> {
            // ignore if out of range
            if (mc.player.distanceTo(entity) > range.get())  return false;
            if (entity == mc.player || entity == mc.cameraEntity) return false;
            if (!(entity instanceof PlayerEntity) && players.get()) return false;
            if ((entity instanceof LivingEntity && ((LivingEntity) entity).isDead()) || !entity.isAlive()) return false;
            if (!nametagged.get() && entity.hasCustomName()) return false;
            if (entity instanceof PlayerEntity) {
                if (((PlayerEntity) entity).isCreative()) return false;
                if (!friends.get() && !Friends.get().attack((PlayerEntity) entity)) return false;
            }
            if ((entity instanceof TameableEntity || entity instanceof HorseBaseEntity) && !(pets.get())) return false;
            if ((entity instanceof AnimalEntity || entity instanceof AmbientEntity || entity instanceof WaterCreatureEntity) && !(ambient.get())) return false;
            // non-mob entities
            if (entity instanceof ItemEntity || entity instanceof ExperienceOrbEntity || entity instanceof ExperienceBottleEntity
                || entity instanceof ArrowEntity) return false;
            return !(entity instanceof AnimalEntity) || babies.get() || !((AnimalEntity) entity).isBaby();
        }, priority.get(), entityList);

        // check if there are entities
        if (entityList.isEmpty()) return;
        // get top priority entity
        entityList.subList(1, entityList.size()).clear();
        Entity target = entityList.get(0);

        // set velocity
		Float velocity = (72000 - mc.player.getItemUseTimeLeft()) / 20F;
		velocity = (velocity * velocity + velocity * 2) / 3;
		if (velocity > 1)
			velocity = (float) 1;

        // set position to aim at
		double d = PlayerUtils.getEyesPos().distanceTo(
			target.getBoundingBox().getCenter());
		double posX = target.getX() + (target.getX() - target.lastRenderX) * d
			- mc.player.getX();
		double posY = target.getY() + (target.getY() - target.lastRenderY) * d
			+ target.getHeight() * 0.5 - mc.player.getY()
			- mc.player.getEyeHeight(mc.player.getPose());
		double posZ = target.getZ() + (target.getZ() - target.lastRenderZ) * d
			- mc.player.getZ();
		
		// set yaw
		mc.player.yaw = (float)Math.toDegrees(Math.atan2(posZ, posX)) - 90;
		
		// calculate needed pitch
		double hDistance = Math.sqrt(posX * posX + posZ * posZ);
		double hDistanceSq = hDistance * hDistance;
		float g = 0.006F;
		float velocitySq = velocity * velocity;
		float velocityPow4 = velocitySq * velocitySq;
		float neededPitch = (float)-Math.toDegrees(Math.atan((velocitySq - Math.sqrt(velocityPow4 - g * (g * hDistanceSq + 2 * posY * velocitySq))) / (g * hDistance)));
        
        // set pitch
        if (Float.isNaN(neededPitch)) {
            RotationUtils.Rotation rotations = RotationUtils.getNeededRotations(target.getBoundingBox().getCenter());
            
            mc.player.yaw = rotations.getYaw();
        } else mc.player.pitch = neededPitch;
    }
}