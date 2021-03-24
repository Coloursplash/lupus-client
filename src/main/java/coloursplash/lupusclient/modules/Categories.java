

package coloursplash.lupusclient.modules;

public class Categories {
    public static final Category Combat = new Category("Combat");
    public static final Category Player = new Category("Player");
    public static final Category Movement = new Category("Movement");
    public static final Category Render = new Category("Render");
    public static final Category World = new Category("World");
    public static final Category Exploit = new Category("Exploit");
    public static final Category Fun = new Category("Fun");
    public static final Category Misc = new Category("Misc");

    public static void register() {
        Modules.registerCategory(Combat);
        Modules.registerCategory(Player);
        Modules.registerCategory(Movement);
        Modules.registerCategory(Render);
        Modules.registerCategory(World);
        Modules.registerCategory(Exploit);
        Modules.registerCategory(Fun);
        Modules.registerCategory(Misc);
    }
}
