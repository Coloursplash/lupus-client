

package coloursplash.lupusclient.modules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Nullable;

import com.mojang.serialization.Lifecycle;

import org.lwjgl.glfw.GLFW;

import meteordevelopment.orbit.EventHandler;
import meteordevelopment.orbit.EventPriority;
import coloursplash.lupusclient.LupusClient;
import coloursplash.lupusclient.events.game.GameJoinedEvent;
import coloursplash.lupusclient.events.game.GameLeftEvent;
import coloursplash.lupusclient.events.game.OpenScreenEvent;
import coloursplash.lupusclient.events.lupus.ActiveModulesChangedEvent;
import coloursplash.lupusclient.events.lupus.KeyEvent;
import coloursplash.lupusclient.modules.combat.AimAssist;
import coloursplash.lupusclient.modules.combat.AnchorAura;
import coloursplash.lupusclient.modules.combat.AntiAnchor;
import coloursplash.lupusclient.modules.combat.AntiAnvil;
import coloursplash.lupusclient.modules.combat.AntiBed;
import coloursplash.lupusclient.modules.combat.AntiFriendHit;
import coloursplash.lupusclient.modules.combat.Auto32K;
import coloursplash.lupusclient.modules.combat.AutoAnvil;
import coloursplash.lupusclient.modules.combat.AutoArmor;
import coloursplash.lupusclient.modules.combat.AutoCity;
import coloursplash.lupusclient.modules.combat.AutoLog;
import coloursplash.lupusclient.modules.combat.AutoTotem;
import coloursplash.lupusclient.modules.combat.AutoTrap;
import coloursplash.lupusclient.modules.combat.AutoWeapon;
import coloursplash.lupusclient.modules.combat.AutoWeb;
import coloursplash.lupusclient.modules.combat.BedAura;
import coloursplash.lupusclient.modules.combat.BowAimbot;
import coloursplash.lupusclient.modules.combat.BowSpam;
import coloursplash.lupusclient.modules.combat.Criticals;
import coloursplash.lupusclient.modules.combat.CrystalAura;
import coloursplash.lupusclient.modules.combat.Hitboxes;
import coloursplash.lupusclient.modules.combat.HoleFiller;
import coloursplash.lupusclient.modules.combat.KillAura;
import coloursplash.lupusclient.modules.combat.OffhandExtra;
import coloursplash.lupusclient.modules.combat.Quiver;
import coloursplash.lupusclient.modules.combat.SelfAnvil;
import coloursplash.lupusclient.modules.combat.SelfTrap;
import coloursplash.lupusclient.modules.combat.SelfWeb;
import coloursplash.lupusclient.modules.combat.SmartSurround;
import coloursplash.lupusclient.modules.combat.Surround;
import coloursplash.lupusclient.modules.combat.TotemPopNotifier;
import coloursplash.lupusclient.modules.combat.Trigger;
import coloursplash.lupusclient.modules.exploit.AntiHunger;
import coloursplash.lupusclient.modules.exploit.Blink;
import coloursplash.lupusclient.modules.fun.Sex;
import coloursplash.lupusclient.modules.fun.Spam;
import coloursplash.lupusclient.modules.misc.Announcer;
import coloursplash.lupusclient.modules.misc.AntiPacketKick;
import coloursplash.lupusclient.modules.misc.AutoBreed;
import coloursplash.lupusclient.modules.misc.AutoBrewer;
import coloursplash.lupusclient.modules.misc.AutoMount;
import coloursplash.lupusclient.modules.misc.AutoNametag;
import coloursplash.lupusclient.modules.misc.AutoReconnect;
import coloursplash.lupusclient.modules.misc.AutoShearer;
import coloursplash.lupusclient.modules.misc.AutoSign;
import coloursplash.lupusclient.modules.misc.AutoSmelter;
import coloursplash.lupusclient.modules.misc.AutoSteal;
import coloursplash.lupusclient.modules.misc.BetterChat;
import coloursplash.lupusclient.modules.misc.BookBot;
import coloursplash.lupusclient.modules.misc.DiscordPresence;
import coloursplash.lupusclient.modules.misc.EChestFarmer;
import coloursplash.lupusclient.modules.misc.EntityLogger;
import coloursplash.lupusclient.modules.misc.LiquidFiller;
import coloursplash.lupusclient.modules.misc.MessageAura;
import coloursplash.lupusclient.modules.misc.MiddleClickFriend;
import coloursplash.lupusclient.modules.misc.OffhandCrash;
import coloursplash.lupusclient.modules.misc.PacketCanceller;
import coloursplash.lupusclient.modules.misc.Panic;
import coloursplash.lupusclient.modules.misc.SoundBlocker;
import coloursplash.lupusclient.modules.misc.Swarm;
import coloursplash.lupusclient.modules.misc.VisualRange;
import coloursplash.lupusclient.modules.movement.AirJump;
import coloursplash.lupusclient.modules.movement.Anchor;
import coloursplash.lupusclient.modules.movement.AntiLevitation;
import coloursplash.lupusclient.modules.movement.AutoJump;
import coloursplash.lupusclient.modules.movement.AutoWalk;
import coloursplash.lupusclient.modules.movement.BoatFly;
import coloursplash.lupusclient.modules.movement.ClickTP;
import coloursplash.lupusclient.modules.movement.ElytraBoost;
import coloursplash.lupusclient.modules.movement.EntityControl;
import coloursplash.lupusclient.modules.movement.EntitySpeed;
import coloursplash.lupusclient.modules.movement.FastClimb;
import coloursplash.lupusclient.modules.movement.Flight;
import coloursplash.lupusclient.modules.movement.GUIMove;
import coloursplash.lupusclient.modules.movement.HighJump;
import coloursplash.lupusclient.modules.movement.Jesus;
import coloursplash.lupusclient.modules.movement.NoFall;
import coloursplash.lupusclient.modules.movement.NoSlow;
import coloursplash.lupusclient.modules.movement.Parkour;
import coloursplash.lupusclient.modules.movement.ReverseStep;
import coloursplash.lupusclient.modules.movement.SafeWalk;
import coloursplash.lupusclient.modules.movement.Scaffold;
import coloursplash.lupusclient.modules.movement.Spider;
import coloursplash.lupusclient.modules.movement.Sprint;
import coloursplash.lupusclient.modules.movement.Step;
import coloursplash.lupusclient.modules.movement.Timer;
import coloursplash.lupusclient.modules.movement.Velocity;
import coloursplash.lupusclient.modules.movement.elytrafly.ElytraFly;
import coloursplash.lupusclient.modules.movement.speed.Speed;
import coloursplash.lupusclient.modules.player.AirPlace;
import coloursplash.lupusclient.modules.player.AntiAFK;
import coloursplash.lupusclient.modules.player.AntiCactus;
import coloursplash.lupusclient.modules.player.AutoClicker;
import coloursplash.lupusclient.modules.player.AutoDrop;
import coloursplash.lupusclient.modules.player.AutoEat;
import coloursplash.lupusclient.modules.player.AutoFish;
import coloursplash.lupusclient.modules.player.AutoGap;
import coloursplash.lupusclient.modules.player.AutoMend;
import coloursplash.lupusclient.modules.player.AutoReplenish;
import coloursplash.lupusclient.modules.player.AutoRespawn;
import coloursplash.lupusclient.modules.player.AutoTool;
import coloursplash.lupusclient.modules.player.BuildHeight;
import coloursplash.lupusclient.modules.player.ChestSwap;
import coloursplash.lupusclient.modules.player.DeathPosition;
import coloursplash.lupusclient.modules.player.EXPThrower;
import coloursplash.lupusclient.modules.player.EndermanLook;
import coloursplash.lupusclient.modules.player.FakePlayer;
import coloursplash.lupusclient.modules.player.FastUse;
import coloursplash.lupusclient.modules.player.GhostHand;
import coloursplash.lupusclient.modules.player.InfinityMiner;
import coloursplash.lupusclient.modules.player.LiquidInteract;
import coloursplash.lupusclient.modules.player.MiddleClickExtra;
import coloursplash.lupusclient.modules.player.MountBypass;
import coloursplash.lupusclient.modules.player.NameProtect;
import coloursplash.lupusclient.modules.player.NoBreakDelay;
import coloursplash.lupusclient.modules.player.NoInteract;
import coloursplash.lupusclient.modules.player.NoMiningTrace;
import coloursplash.lupusclient.modules.player.NoRotate;
import coloursplash.lupusclient.modules.player.PacketMine;
import coloursplash.lupusclient.modules.player.Portals;
import coloursplash.lupusclient.modules.player.PotionSpoof;
import coloursplash.lupusclient.modules.player.Reach;
import coloursplash.lupusclient.modules.player.Rotation;
import coloursplash.lupusclient.modules.player.SpeedMine;
import coloursplash.lupusclient.modules.player.XCarry;
import coloursplash.lupusclient.modules.render.Ambience;
import coloursplash.lupusclient.modules.render.BlockSelection;
import coloursplash.lupusclient.modules.render.BossStack;
import coloursplash.lupusclient.modules.render.Breadcrumbs;
import coloursplash.lupusclient.modules.render.BreakIndicators;
import coloursplash.lupusclient.modules.render.CameraClip;
import coloursplash.lupusclient.modules.render.Chams;
import coloursplash.lupusclient.modules.render.CityESP;
import coloursplash.lupusclient.modules.render.CustomFOV;
import coloursplash.lupusclient.modules.render.EChestPreview;
import coloursplash.lupusclient.modules.render.ESP;
import coloursplash.lupusclient.modules.render.EntityOwner;
import coloursplash.lupusclient.modules.render.FreeRotate;
import coloursplash.lupusclient.modules.render.Freecam;
import coloursplash.lupusclient.modules.render.Fullbright;
import coloursplash.lupusclient.modules.render.HandView;
import coloursplash.lupusclient.modules.render.HoleESP;
import coloursplash.lupusclient.modules.render.ItemByteSize;
import coloursplash.lupusclient.modules.render.ItemHighlight;
import coloursplash.lupusclient.modules.render.ItemPhysics;
import coloursplash.lupusclient.modules.render.LogoutSpots;
import coloursplash.lupusclient.modules.render.Nametags;
import coloursplash.lupusclient.modules.render.NoRender;
import coloursplash.lupusclient.modules.render.ParticleBlocker;
import coloursplash.lupusclient.modules.render.Search;
import coloursplash.lupusclient.modules.render.ShulkerPeek;
import coloursplash.lupusclient.modules.render.StorageESP;
import coloursplash.lupusclient.modules.render.Tracers;
import coloursplash.lupusclient.modules.render.Trail;
import coloursplash.lupusclient.modules.render.Trajectories;
import coloursplash.lupusclient.modules.render.UnfocusedCPU;
import coloursplash.lupusclient.modules.render.VoidESP;
import coloursplash.lupusclient.modules.render.Xray;
import coloursplash.lupusclient.modules.render.hud.HUD;
import coloursplash.lupusclient.modules.world.Nuker;
import coloursplash.lupusclient.modules.world.StashFinder;
import coloursplash.lupusclient.modules.world.TimeChanger;
import coloursplash.lupusclient.settings.Setting;
import coloursplash.lupusclient.settings.SettingGroup;
import coloursplash.lupusclient.systems.System;
import coloursplash.lupusclient.systems.Systems;
import coloursplash.lupusclient.utils.Utils;
import coloursplash.lupusclient.utils.misc.input.Input;
import coloursplash.lupusclient.utils.misc.input.KeyAction;
import coloursplash.lupusclient.utils.player.ChatUtils;
import coloursplash.lupusclient.utils.player.InvUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public class Modules extends System<Modules> {
    public static final ModuleRegistry REGISTRY = new ModuleRegistry();

    private static final List<Category> CATEGORIES = new ArrayList<>();
    public static boolean REGISTERING_CATEGORIES;

    private final Map<Class<? extends Module>, Module> modules = new HashMap<>();
    private final Map<Category, List<Module>> groups = new HashMap<>();

    private final List<Module> active = new ArrayList<>();
    private Module moduleToBind;

    public boolean onKeyOnlyBinding = false;

    public Modules() {
        super("modules");
    }

    public static Modules get() {
        return Systems.get(Modules.class);
    }

    @Override
    public void init() {
        initCombat();
        initPlayer();
        initMovement();
        initRender();
        initExploit();
        initWorld();
        initFun();
        initMisc();
    }

    public void sortModules() {
        for (List<Module> modules : groups.values()) {
            modules.sort(Comparator.comparing(o -> o.title));
        }
    }

    public static void registerCategory(Category category) {
        if (!REGISTERING_CATEGORIES) throw new RuntimeException("Modules.registerCategory - Cannot register category outside of onRegisterCategories callback.");

        CATEGORIES.add(category);
    }

    public static Iterable<Category> loopCategories() {
        return CATEGORIES;
    }

    public static Category getCategoryByHash(int hash) {
        for (Category category : CATEGORIES) {
            if (category.hashCode() == hash) return category;
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public <T extends Module> T get(Class<T> klass) {
        return (T) modules.get(klass);
    }

    public Module get(String name) {
        for (Module module : modules.values()) {
            if (module.name.equalsIgnoreCase(name)) return module;
        }

        return null;
    }

    public boolean isActive(Class<? extends Module> klass) {
        Module module = get(klass);
        return module != null && module.isActive();
    }

    public List<Module> getGroup(Category category) {
        return groups.computeIfAbsent(category, category1 -> new ArrayList<>());
    }

    public Collection<Module> getAll() {
        return modules.values();
    }

    public List<Module> getActive() {
        synchronized (active) {
            return active;
        }
    }

    public void setModuleToBind(Module moduleToBind) {
        this.moduleToBind = moduleToBind;
    }

    public List<Pair<Module, Integer>> searchTitles(String text) {
        List<Pair<Module, Integer>> modules = new ArrayList<>();

        for (Module module : this.modules.values()) {
            int words = Utils.search(module.title, text);
            if (words > 0) modules.add(new Pair<>(module, words));
        }

        modules.sort(Comparator.comparingInt(value -> -value.getRight()));
        return modules;
    }

    public List<Pair<Module, Integer>> searchSettingTitles(String text) {
        List<Pair<Module, Integer>> modules = new ArrayList<>();

        for (Module module : this.modules.values()) {
            for (SettingGroup sg : module.settings) {
                for (Setting<?> setting : sg) {
                    int words = Utils.search(setting.title, text);
                    if (words > 0) {
                        modules.add(new Pair<>(module, words));
                        break;
                    }
                }
            }
        }

        modules.sort(Comparator.comparingInt(value -> -value.getRight()));
        return modules;
    }

    void addActive(Module module) {
        synchronized (active) {
            if (!active.contains(module)) {
                active.add(module);
                LupusClient.EVENT_BUS.post(ActiveModulesChangedEvent.get());
            }
        }
    }

    void removeActive(Module module) {
        synchronized (active) {
            if (active.remove(module)) {
                LupusClient.EVENT_BUS.post(ActiveModulesChangedEvent.get());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST + 1)
    private void onKey(KeyEvent event) {
        if (event.action == KeyAction.Repeat) return;

        // Check if binding module
        if (event.action == KeyAction.Press && moduleToBind != null) {
            moduleToBind.setKey(event.key);
            ChatUtils.prefixInfo("KeyBinds", "Module (highlight)%s (default)bound to (highlight)%s(default).", moduleToBind.title, Utils.getKeyName(event.key));
            moduleToBind = null;
            event.cancel();
            return;
        }

        // Find module bound to that key
        if (!onKeyOnlyBinding && MinecraftClient.getInstance().currentScreen == null && !Input.isPressed(GLFW.GLFW_KEY_F3)) {
            for (Module module : modules.values()) {
                if (module.getKey() == event.key && (event.action == KeyAction.Press || module.toggleOnKeyRelease)) {
                    module.doAction();
                    module.sendToggledMsg();
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST + 1)
    private void onOpenScreen(OpenScreenEvent event) {
        for (Module module : modules.values()) {
            if (module.toggleOnKeyRelease) {
                if (module.isActive()) {
                    module.toggle();
                    module.sendToggledMsg();
                }
            }
        }
    }

    @EventHandler
    private void onGameJoined(GameJoinedEvent event) {
        synchronized (active) {
            for (Module module : active) {
                LupusClient.EVENT_BUS.subscribe(module);
                module.onActivate();
            }
            LupusClient.EVENT_BUS.subscribe(new InvUtils());
        }
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        synchronized (active) {
            for (Module module : active) {
                LupusClient.EVENT_BUS.unsubscribe(module);
                module.onDeactivate();
            }
        }
    }

    public void disableAll() {
        synchronized (active) {
            for (Module module : active.toArray(new Module[0])) {
                module.toggle(Utils.canUpdate());
            }
        }
    }

    @Override
    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();

        ListTag modulesTag = new ListTag();
        for (Module module : getAll()) {
            CompoundTag moduleTag = module.toTag();
            if (moduleTag != null) modulesTag.add(moduleTag);
        }
        tag.put("modules", modulesTag);

        return tag;
    }

    @Override
    public Modules fromTag(CompoundTag tag) {
        disableAll();

        ListTag modulesTag = tag.getList("modules", 10);
        for (Tag moduleTagI : modulesTag) {
            CompoundTag moduleTag = (CompoundTag) moduleTagI;
            Module module = get(moduleTag.getString("name"));
            if (module != null) module.fromTag(moduleTag);
        }

        return this;
    }

    // INIT MODULES

    public void add(Module module) {
        // Check if the module's category is registered
        if (!CATEGORIES.contains(module.category)) {
            throw new RuntimeException("Modules.addModule - Module's category was not registered.");
        }

        // Remove the previous module with the same name
        AtomicReference<Module> removedModule = new AtomicReference<>();
        if (modules.values().removeIf(module1 -> {
            if (module1.name.equals(module.name)) {
                removedModule.set(module1);
                module1.settings.unregisterColorSettings();
                
                return true;
            }
            
            return false;
        })) {
            getGroup(removedModule.get().category).remove(removedModule.get());
        }

        // Add the module
        modules.put(module.getClass(), module);
        getGroup(module.category).add(module);

        // Register color settings for the module
        module.settings.registerColorSettings(module);
    }

    /** Only for backwards compatibility **/
    @Deprecated
    public void addModule(Module module) {
        add(module);
    }

    private void initCombat() {
        add(new AimAssist());
        add(new AnchorAura());
        add(new AntiAnvil());
        add(new AntiAnchor());
        add(new AntiBed());
        add(new AntiFriendHit());
        add(new Auto32K());
        add(new AutoAnvil());
        add(new AutoArmor());
        add(new AutoCity());
        add(new AutoLog());
        add(new AutoTotem());
        add(new AutoTrap());
        add(new AutoWeapon());
        add(new AutoWeb());
        add(new BedAura());
        add(new BowSpam());
        add(new Criticals());
        add(new CrystalAura());
        add(new Hitboxes());
        add(new HoleFiller());
        add(new KillAura());
        add(new OffhandExtra());
        add(new Quiver());
        add(new SelfAnvil());
        add(new SelfTrap());
        add(new SelfWeb());
        add(new SmartSurround());
        add(new Surround());
        add(new Swarm());
        add(new TotemPopNotifier());
        add(new Trigger());
        add(new BowAimbot());
    }

    private void initPlayer() {
        add(new AirPlace());
        add(new AntiAFK());
        add(new AntiCactus());
        add(new AutoClicker());
        add(new AutoDrop());
        add(new AutoFish());
        add(new AutoMend());
        add(new AutoMount());
        add(new AutoReplenish());
        add(new AutoRespawn());
        add(new AutoTool());
        add(new BuildHeight());
        add(new ChestSwap());
        add(new DeathPosition());
        add(new EXPThrower());
        add(new EndermanLook());
        add(new FakePlayer());
        add(new FastUse());
        add(new GhostHand());
        add(new InfinityMiner());
        add(new LiquidInteract());
        add(new MiddleClickExtra());
        add(new MountBypass());
        add(new NameProtect());
        add(new NoBreakDelay());
        add(new NoInteract());
        add(new NoMiningTrace());
        add(new NoRotate());
        add(new PacketMine());
        add(new Portals());
        add(new PotionSpoof());
        add(new Reach());
        add(new Rotation());
        add(new SpeedMine());
        add(new Trail());
        add(new XCarry());
        add(new AutoGap());
        add(new AutoEat());
    }

    private void initMovement() {
        add(new AirJump());
        add(new Anchor());
        add(new AntiLevitation());
        add(new AutoJump());
        add(new Sprint());
        add(new AutoWalk());
        add(new Blink());
        add(new BoatFly());
        add(new ClickTP());
        add(new ElytraBoost());
        add(new ElytraFly());
        add(new EntityControl());
        add(new EntitySpeed());
        add(new FastClimb());
        add(new Flight());
        add(new GUIMove());
        add(new HighJump());
        add(new Jesus());
        add(new NoFall());
        add(new NoSlow());
        add(new Parkour());
        add(new ReverseStep());
        add(new SafeWalk());
        add(new Scaffold());
        add(new Speed());
        add(new Spider());
        add(new Step());
        add(new Timer());
        add(new Velocity());
    }

    private void initRender() {
        add(new BlockSelection());
        add(new Breadcrumbs());
        add(new BreakIndicators());
        add(new CameraClip());
        add(new Chams());
        add(new CityESP());
        add(new CustomFOV());
        add(new EChestPreview());
        add(new ESP());
        add(new EntityOwner());
        add(new FreeRotate());
        add(new Freecam());
        add(new Fullbright());
        add(new HUD());
        add(new HandView());
        add(new HoleESP());
        add(new ItemByteSize());
        add(new ItemPhysics());
        add(new LogoutSpots());
        add(new Nametags());
        add(new NoRender());
        add(new ParticleBlocker());
        add(new Search());
        add(new ShulkerPeek());
        add(new StorageESP());
        add(new TimeChanger());
        add(new Tracers());
        add(new Trajectories());
        add(new UnfocusedCPU());
        add(new VoidESP());
        add(new Xray());
        add(new BossStack());
        add(new ItemHighlight());
        add(new Ambience());
    }

    private void initExploit() {
        add(new AntiHunger());
    }

    private void initWorld() {
        add(new Nuker());
        add(new StashFinder());
        add(new TimeChanger());
    }

    private void initFun() {
        add(new Spam());
        add(new Sex());
    }

    private void initMisc() {
        add(new Announcer());
        add(new AntiPacketKick());
        add(new AutoBreed());
        add(new AutoBrewer());
        add(new AutoNametag());
        add(new AutoReconnect());
        add(new AutoShearer());
        add(new AutoSign());
        add(new AutoSmelter());
        add(new AutoSteal());
        add(new BetterChat());
        add(new BookBot());
        add(new DiscordPresence());
        add(new EChestFarmer());
        add(new EntityLogger());
        add(new LiquidFiller());
        add(new MessageAura());
        add(new MiddleClickFriend());
        add(new Nuker());
        add(new OffhandCrash());
        add(new PacketCanceller());
        add(new SoundBlocker());
        add(new Spam());
        add(new StashFinder());
        add(new VisualRange());
        add(new Panic());
    }

    public static class ModuleRegistry extends Registry<Module> {
        public ModuleRegistry() {
            super(RegistryKey.ofRegistry(new Identifier("lupus-client", "modules")), Lifecycle.stable());
        }

        @Nullable
        @Override
        public Identifier getId(Module entry) {
            return null;
        }

        @Override
        public Optional<RegistryKey<Module>> getKey(Module entry) {
            return Optional.empty();
        }

        @Override
        public int getRawId(@Nullable Module entry) {
            return 0;
        }

        @Nullable
        @Override
        public Module get(@Nullable RegistryKey<Module> key) {
            return null;
        }

        @Nullable
        @Override
        public Module get(@Nullable Identifier id) {
            return null;
        }

        @Override
        protected Lifecycle getEntryLifecycle(Module object) {
            return null;
        }

        @Override
        public Lifecycle getLifecycle() {
            return null;
        }

        @Override
        public Set<Identifier> getIds() {
            return null;
        }

        @Override
        public Set<Map.Entry<RegistryKey<Module>, Module>> getEntries() {
            return null;
        }

        @Override
        public boolean containsId(Identifier id) {
            return false;
        }

        @Nullable
        @Override
        public Module get(int index) {
            return null;
        }

        @Override
        public Iterator<Module> iterator() {
            return new ToggleModuleIterator();
        }

        private static class ToggleModuleIterator implements Iterator<Module> {
            private final Iterator<Module> iterator = Modules.get().getAll().iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Module next() {
                return iterator.next();
            }
        }
    }
}