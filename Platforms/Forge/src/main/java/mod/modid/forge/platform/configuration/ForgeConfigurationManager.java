package mod.modid.forge.platform.configuration;

import com.google.common.collect.Sets;
import mod.modid.platforms.core.config.ConfigurationType;
import mod.modid.platforms.core.config.IConfigurationBuilder;
import mod.modid.platforms.core.config.IConfigurationManager;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import java.util.Set;

public class ForgeConfigurationManager implements IConfigurationManager
{
    private static final ForgeConfigurationManager INSTANCE = new ForgeConfigurationManager();

    public static ForgeConfigurationManager getInstance()
    {
        return INSTANCE;
    }

    private final Set<String> availableKeys = Sets.newConcurrentHashSet();

    private ForgeConfigurationManager()
    {
    }

    @Override
    public IConfigurationBuilder createBuilder(
      final ConfigurationType type, final String name)
    {
        return new ForgeDelegateConfigurationBuilder(forgeConfigSpec -> {
            final ModConfig config = new ModConfig(remapType(type), forgeConfigSpec, ModLoadingContext.get().getActiveContainer(), String.format("%s.toml", name));
            ModLoadingContext.get().getActiveContainer().addConfig(config);
        },
          availableKeys::add);
    }

    private static ModConfig.Type remapType(final ConfigurationType type) {
        return switch (type) {
            case CLIENT_ONLY -> ModConfig.Type.CLIENT;
            case NOT_SYNCED -> ModConfig.Type.COMMON;
            case SYNCED -> ModConfig.Type.SERVER;
        };
    }

    public Set<String> getAvailableKeys()
    {
        return availableKeys;
    }
}
