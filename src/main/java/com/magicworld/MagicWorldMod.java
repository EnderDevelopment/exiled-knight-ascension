package com.magicworld;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.feature.FeatureFlags;

public final class MagicWorldMod implements ModInitializer {
    public static final String MOD_ID = "magic-world";
    public static final String[] CLASS_IDS = {
            "heavy_knight", "pierrot", "master_swordsman", "shield_guardian", "berserker", "ranger",
            "assassin", "elementalist", "arcane_scholar", "battle_mage", "oracle", "saint",
            "alchemist", "beast_tamer", "merchant", "cartographer", "spellsword"
    };
    public static final String[] CLASS_NAMES = {
            "Heavy Knight", "Pierrot", "Master Swordsman", "Shield Guardian", "Berserker", "Ranger",
            "Assassin", "Elementalist", "Arcane Scholar", "Battle Mage", "Oracle", "Saint",
            "Alchemist", "Beast Tamer", "Merchant", "Cartographer", "Spellsword"
    };
    public static final ScreenHandlerType<MagicWorldScreenHandler> CLASS_SELECTION_HANDLER = Registry.register(
            Registries.SCREEN_HANDLER,
            Identifier.of(MOD_ID, "class_selection"),
            new ScreenHandlerType<>(MagicWorldScreenHandler::new, FeatureFlags.VANILLA_FEATURES));

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(CommandManager.literal("mw")
                        .executes(context -> help(context.getSource()))
                        .then(CommandManager.literal("help").executes(context -> help(context.getSource())))
                        .then(CommandManager.literal("choose").executes(context -> openClassSelection(context.getSource())))
                        .then(CommandManager.literal("status").executes(context -> status(context.getSource())))
                        .then(CommandManager.literal("gods").executes(context -> gods(context.getSource())))
                        .then(CommandManager.literal("regions").executes(context -> regions(context.getSource())))
                        .then(CommandManager.literal("bless")
                                .then(CommandManager.literal("heavy_knight").executes(context -> bless(context.getSource(), "heavy_knight", "Heavy Knight")))
                                .then(CommandManager.literal("pierrot").executes(context -> bless(context.getSource(), "pierrot", "Pierrot")))
                                .then(CommandManager.literal("master_swordsman").executes(context -> bless(context.getSource(), "master_swordsman", "Master Swordsman")))
                                .then(CommandManager.literal("shield_guardian").executes(context -> bless(context.getSource(), "shield_guardian", "Shield Guardian")))
                                .then(CommandManager.literal("berserker").executes(context -> bless(context.getSource(), "berserker", "Berserker")))
                                .then(CommandManager.literal("ranger").executes(context -> bless(context.getSource(), "ranger", "Ranger")))
                                .then(CommandManager.literal("assassin").executes(context -> bless(context.getSource(), "assassin", "Assassin")))
                                .then(CommandManager.literal("elementalist").executes(context -> bless(context.getSource(), "elementalist", "Elementalist")))
                                .then(CommandManager.literal("arcane_scholar").executes(context -> bless(context.getSource(), "arcane_scholar", "Arcane Scholar")))
                                .then(CommandManager.literal("battle_mage").executes(context -> bless(context.getSource(), "battle_mage", "Battle Mage")))
                                .then(CommandManager.literal("oracle").executes(context -> bless(context.getSource(), "oracle", "Oracle")))
                                .then(CommandManager.literal("saint").executes(context -> bless(context.getSource(), "saint", "Saint")))
                                .then(CommandManager.literal("alchemist").executes(context -> bless(context.getSource(), "alchemist", "Alchemist")))
                                .then(CommandManager.literal("beast_tamer").executes(context -> bless(context.getSource(), "beast_tamer", "Beast Tamer")))
                                .then(CommandManager.literal("merchant").executes(context -> bless(context.getSource(), "merchant", "Merchant")))
                                .then(CommandManager.literal("cartographer").executes(context -> bless(context.getSource(), "cartographer", "Cartographer")))
                                .then(CommandManager.literal("spellsword").executes(context -> bless(context.getSource(), "spellsword", "Spellsword"))))
                        .then(CommandManager.literal("skill")
                                .then(CommandManager.literal("iron_wall").executes(context -> skill(context.getSource(), "iron_wall", "Iron Wall")))
                                .then(CommandManager.literal("shield_bash").executes(context -> skill(context.getSource(), "shield_bash", "Shield Bash")))
                                .then(CommandManager.literal("fortress_stance").executes(context -> skill(context.getSource(), "fortress_stance", "Fortress Stance")))
                                .then(CommandManager.literal("lucky_break").executes(context -> skill(context.getSource(), "lucky_break", "Lucky Break")))
                                .then(CommandManager.literal("arcane_surge").executes(context -> skill(context.getSource(), "arcane_surge", "Arcane Surge"))))
                        .then(CommandManager.literal("contract")
                                .then(CommandManager.literal("goblin_watch").executes(context -> contract(context.getSource(), "goblin_watch", "Goblin Watch", 8)))
                                .then(CommandManager.literal("crypt_dive").executes(context -> contract(context.getSource(), "crypt_dive", "Crypt Dive", 16)))
                                .then(CommandManager.literal("mana_storm").executes(context -> contract(context.getSource(), "mana_storm", "Mana Storm", 24)))));
    }

    private static int openClassSelection(ServerCommandSource source) {
        if (!(source.getEntity() instanceof ServerPlayerEntity player)) return 0;
        if (player.getCommandTags().contains("mw_class_locked")) {
            source.sendError(Text.literal("Your Divine Blessing is already locked."));
            return 0;
        }
        player.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                (syncId, inventory, ignored) -> new MagicWorldScreenHandler(syncId, inventory),
                Text.literal("Divine Blessing")));
        return 1;
    }

    public static void selectClassFromScreen(ServerPlayerEntity player, int index) {
        if (index < 0 || index >= CLASS_IDS.length || player.getCommandTags().contains("mw_class_locked")) return;
        bless(new ServerCommandSource(player.getCommandSource().getOutput(), player.getPos(), player.getRotationClient(), player.getServerWorld(), 2, player.getName().getString(), player.getDisplayName(), player.getServerWorld().getServer(), player), CLASS_IDS[index], CLASS_NAMES[index]);
        player.closeHandledScreen();
    }

    private static int help(ServerCommandSource source) {
        source.sendFeedback(() -> Text.literal("Magic World System: /mw choose, /mw bless <class>, /mw status, /mw skill <node>, /mw contract <contract>, /mw gods, /mw regions"), false);
        source.sendFeedback(() -> Text.literal("Use /mw choose for the Divine Blessing class selection screen. Classes are permanent once selected."), false);
        return 1;
    }

    private static int bless(ServerCommandSource source, String id, String name) {
        if (source.getEntity() == null) return 0;
        run(source, "scoreboard objectives add mw_level dummy");
        run(source, "scoreboard objectives add mw_skill_points dummy");
        run(source, "execute unless entity @s[tag=mw_blessed] run tag @s add mw_blessed");
        run(source, "execute if entity @s[tag=mw_blessed] unless entity @s[tag=mw_class_chosen] run tag @s add mw_class_chosen");
        run(source, "execute if entity @s[tag=mw_class_chosen] unless entity @s[tag=mw_class_locked] run tag @s add mw_class_" + id);
        run(source, "execute if entity @s[tag=mw_class_" + id + "] run tag @s add mw_class_locked");
        run(source, "execute if entity @s[tag=mw_class_" + id + "] run scoreboard players set @s mw_level 1");
        run(source, "execute if entity @s[tag=mw_class_" + id + "] run scoreboard players set @s mw_skill_points 1");
        source.sendFeedback(() -> Text.literal("Divine Blessing recorded: " + name + ". Your class is now permanent."), false);
        return 1;
    }

    private static int status(ServerCommandSource source) {
        source.sendFeedback(() -> Text.literal("Magic World character ledger"), false);
        source.sendFeedback(() -> Text.literal("Class, level, and skill points are stored in scoreboard objectives mw_level and mw_skill_points."), false);
        source.sendFeedback(() -> Text.literal("Use /scoreboard players get @s mw_level and /scoreboard players get @s mw_skill_points to inspect exact values."), false);
        return 1;
    }

    private static int skill(ServerCommandSource source, String id, String name) {
        run(source, "execute if score @s mw_skill_points matches 1.. unless entity @s[tag=mw_skill_" + id + "] run tag @s add mw_skill_purchase");
        run(source, "execute if entity @s[tag=mw_skill_purchase] run scoreboard players remove @s mw_skill_points 1");
        run(source, "execute if entity @s[tag=mw_skill_purchase] run tag @s add mw_skill_" + id);
        run(source, "tag @s remove mw_skill_purchase");
        source.sendFeedback(() -> Text.literal("Skill attempt: " + name + ". Spend one available skill point; each node can be learned once."), false);
        return 1;
    }

    private static int contract(ServerCommandSource source, String id, String name, int reward) {
        run(source, "scoreboard objectives add mw_contracts dummy");
        run(source, "scoreboard objectives add mw_skill_points dummy");
        run(source, "tag @s add mw_contract_" + id);
        run(source, "scoreboard players add @s mw_contracts 1");
        run(source, "scoreboard players add @s mw_skill_points 1");
        source.sendFeedback(() -> Text.literal("Guild contract accepted: " + name + ". Suggested tier reward: " + reward + " emeralds and one skill point."), false);
        return 1;
    }

    private static int gods(ServerCommandSource source) {
        source.sendFeedback(() -> Text.literal("The Nine Gods: Alxeros, Vaelune, Morvane, Tharos, Eirene, Nym, Orun, Sylva, and Kharos."), false);
        source.sendFeedback(() -> Text.literal("Dungeons form where Alxeros's dreams condense mana; nightmares produce unstable bosses and rare materials."), false);
        return 1;
    }

    private static int regions(ServerCommandSource source) {
        source.sendFeedback(() -> Text.literal("Known regions: Asterfall Kingdom, Veyr Marches, Lumen Theocracy, Kharad Free Cities, and the Ashen Expanse."), false);
        source.sendFeedback(() -> Text.literal("Dungeon sites: Glassroot Labyrinth, Saint Orun's Vault, and the Nightmare Below."), false);
        return 1;
    }

    private static void run(ServerCommandSource source, String command) {
        source.getServer().getCommandManager().executeWithPrefix(source, command);
    }
}
