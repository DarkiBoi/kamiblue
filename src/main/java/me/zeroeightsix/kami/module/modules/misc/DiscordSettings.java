package me.zeroeightsix.kami.module.modules.misc;

import me.zeroeightsix.kami.DiscordPresence;
import me.zeroeightsix.kami.KamiMod;
import me.zeroeightsix.kami.command.Command;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;

/**
 * @author S-B99
 * Updated by S-B99 on 13/01/20
 */
@Module.Info(name = "DiscordRPC", category = Module.Category.MISC, description = "Discord Rich Presence")
public class DiscordSettings extends Module {

    public Setting<Boolean> startupGlobal = register(Settings.b("Enable Automatically", true));
    public Setting<Boolean> coordsConfirm = register(Settings.b("Coords Confirm", false));
    public Setting<LineInfo> line1Setting = register(Settings.e("Line 1 Left", LineInfo.VERSION)); // details left
    public Setting<LineInfo> line3Setting = register(Settings.e("Line 1 Right", LineInfo.USERNAME)); // details right
    public Setting<LineInfo> line2Setting = register(Settings.e("Line 2 Left", LineInfo.SERVERIP)); // state left
    public Setting<LineInfo> line4Setting = register(Settings.e("Line 2 Right", LineInfo.HEALTH)); // state right

    public enum LineInfo {
        VERSION, WORLD, USERNAME, HEALTH, SERVERIP, COORDS, NONE
    }

    public String getLine(LineInfo line) {
        switch (line) {
            case VERSION: return KamiMod.MODVER;
            case WORLD:
                if (mc.isIntegratedServerRunning()) return "Singleplayer";
                else if (mc.getCurrentServerData() != null) return "Multiplayer";
                else return "Main Menu";
            case USERNAME:
                if (mc.player != null) return mc.player.getName();
                else return "(Not logged in)";
            case HEALTH:
                if (mc.player != null) return "(" + ((int) mc.player.getHealth()) + " hp)";
                else return "(No hp)";
            case SERVERIP:
                if (mc.getCurrentServerData() != null) return mc.getCurrentServerData().serverIP;
                else return "(Offline)";
            case COORDS:
                if (mc.player != null && coordsConfirm.getValue()) return "(" + (int) mc.player.posX + " " + (int) mc.player.posY + " " + (int) mc.player.posZ + ")";
                else return "(No coords)";
            default: return "";
        }
    }

    @Override
    public void onEnable() {
        DiscordPresence.start();
    }

    private static long startTime = 0;
    @Override
    public void onUpdate() {
        if (startTime == 0) startTime = System.currentTimeMillis();
        if (startTime + 10000 <= System.currentTimeMillis()) { // 10 seconds in milliseconds
            if (line1Setting.getValue().equals(LineInfo.COORDS) || line2Setting.getValue().equals(LineInfo.COORDS) || line3Setting.getValue().equals(LineInfo.COORDS) || line4Setting.getValue().equals(LineInfo.COORDS)) {
                if (!coordsConfirm.getValue() && mc.player != null) {
                    Command.sendWarningMessage("[DiscordRPC] Warning: In order to use the coords option please enable the coords confirmation option. This will display your coords on the discord rpc. Do NOT use this if you do not want your coords displayed");
                }
            }
            startTime = System.currentTimeMillis();
        }
    }

}
