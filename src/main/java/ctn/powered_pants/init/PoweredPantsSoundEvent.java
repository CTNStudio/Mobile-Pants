package ctn.powered_pants.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static ctn.powered_pants.PoweredPants.MOD_ID;

public class PoweredPantsSoundEvent {
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, MOD_ID);

	public static final DeferredHolder<SoundEvent, SoundEvent> PROMPT =
			SOUND_EVENTS.register("prompt", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, "prompt")));
}
