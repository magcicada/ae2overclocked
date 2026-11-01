/*
 * This file includes code adapted from MakeAE2Better by QiuYe.
 * Licensed under the MIT License.
 * Original Source: https://github.com/qiuye2024github/MaekAE2Better
 * Full license text: src/main/resources/LICENSE_MakeAE2Better.txt
 */
package moakiee.mixin;

import appeng.api.parts.IPartItem;
import appeng.parts.automation.ExportBusPart;
import com.glodblock.github.extendedae.config.EPPConfig;
import moakiee.support.SuperSpeedNumberUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "com.glodblock.github.extendedae.common.parts.PartExExportBus", remap = false)
public abstract class MixinExExportBusSuperSpeed extends ExportBusPart {

    public MixinExExportBusSuperSpeed(IPartItem<?> partItem) {
        super(partItem);
    }

    // The superclass mixin has already applied the super speed card once.
    // Defer ExtendedAE's int multiplication to the saturating RETURN hook.
    @Redirect(
        method = "getOperationsPerTick",
        at = @At(value = "FIELD", target = "Lcom/glodblock/github/extendedae/config/EPPConfig;busSpeed:I"),
        require = 1,
        remap = false
    )
    private int ae2oc_deferBusSpeed() {
        return 1;
    }

    @Inject(method = "getOperationsPerTick", at = @At("RETURN"), cancellable = true, require = 1, remap = false)
    private void ae2oc_applyBusSpeed(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(SuperSpeedNumberUtil.multiplyIntSaturating(cir.getReturnValue(), EPPConfig.busSpeed));
    }
}
