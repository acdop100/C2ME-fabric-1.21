package com.ishland.c2me.mixin.optimization.math;

import net.minecraft.util.math.noise.PerlinNoiseSampler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PerlinNoiseSampler.class, priority = 1090)
public abstract class MixinPerlinNoiseSampler {

    @Shadow @Final public double originY;

    @Shadow @Final public double originX;

    @Shadow @Final public double originZ;

    @Shadow @Final private byte[] permutations;

    @Unique
    private static final double[][] SIMPLEX_NOISE_GRADIENTS = new double[][]{
            {1, 1, 0},
            {-1, 1, 0},
            {1, -1, 0},
            {-1, -1, 0},
            {1, 0, 1},
            {-1, 0, 1},
            {1, 0, -1},
            {-1, 0, -1},
            {0, 1, 1},
            {0, -1, 1},
            {0, 1, -1},
            {0, -1, -1},
            {1, 1, 0},
            {0, -1, 1},
            {-1, 1, 0},
            {0, -1, -1}
    };

    @Unique
    private int[] permutationsInt = new int[0];
    @Unique
    private int[] permutationsIntAnd15 = new int[0];

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(CallbackInfo info) {
        this.permutationsInt = new int[this.permutations.length];
        this.permutationsIntAnd15 = new int[this.permutations.length];
        for (int i = 0; i < this.permutations.length; i++) {
            this.permutationsInt[i] = this.permutations[i] & 0xFF;
            this.permutationsIntAnd15[i] = this.permutations[i] & 15;
        }
    }

    /**
     * @author ishland
     * @reason optimize: remove frequent type conversions
     */
    @Deprecated
    @Overwrite
    public double sample(double x, double y, double z, double yScale, double yMax) {
        double d = x + this.originX;
        double e = y + this.originY;
        double f = z + this.originZ;
        double i = Math.floor(d);
        double j = Math.floor(e);
        double k = Math.floor(f);
        double g = d - i;
        double h = e - j;
        double l = f - k;
        double o = 0.0D;
        if (yScale != 0.0) {
            double m;
            if (yMax >= 0.0 && yMax < h) {
                m = yMax;
            } else {
                m = h;
            }

            o = Math.floor(m / yScale + 1.0E-7F) * yScale;
        }

        return this.sample((int) i, (int) j, (int) k, g, h - o, l, h);
    }

    /**
     * @author ishland
     * @reason inline math & small optimization: remove frequent type conversions, cache and reorder
     */
    @Overwrite
    private double sample(int sectionX, int sectionY, int sectionZ, double localX, double localY, double localZ, double fadeLocalX) {
        // TODO [VanillaCopy] but heavily modified
        final int var0 = sectionX & 0xFF;
        final int var1 = (sectionX + 1) & 0xFF;
        final int var2 = this.permutationsInt[var0];
        final int var3 = this.permutationsInt[var1];
        final int var4 = (var2 + sectionY) & 0xFF;
        final int var5 = (var2 + sectionY + 1) & 0xFF;
        final int var6 = (var3 + sectionY) & 0xFF;
        final int var7 = (var3 + sectionY + 1) & 0xFF;
        final int var8 = this.permutationsInt[var4];
        final int var9 = this.permutationsInt[var5];
        final int var10 = this.permutationsInt[var6];
        final int var11 = this.permutationsInt[var7];

        final int var12 = (var8 + sectionZ) & 0xFF;
        final int var13 = (var10 + sectionZ) & 0xFF;
        final int var14 = (var9 + sectionZ) & 0xFF;
        final int var15 = (var11 + sectionZ) & 0xFF;
        final int var16 = (var8 + sectionZ + 1) & 0xFF;
        final int var17 = (var10 + sectionZ + 1) & 0xFF;
        final int var18 = (var9 + sectionZ + 1) & 0xFF;
        final int var19 = (var11 + sectionZ + 1) & 0xFF;
        final int var20 = this.permutationsIntAnd15[var12];
        final int var21 = this.permutationsIntAnd15[var13];
        final int var22 = this.permutationsIntAnd15[var14];
        final int var23 = this.permutationsIntAnd15[var15];
        final int var24 = this.permutationsIntAnd15[var16];
        final int var25 = this.permutationsIntAnd15[var17];
        final int var26 = this.permutationsIntAnd15[var18];
        final int var27 = this.permutationsIntAnd15[var19];
        final double[] var28 = SIMPLEX_NOISE_GRADIENTS[var20];
        final double[] var29 = SIMPLEX_NOISE_GRADIENTS[var21];
        final double[] var30 = SIMPLEX_NOISE_GRADIENTS[var22];
        final double[] var31 = SIMPLEX_NOISE_GRADIENTS[var23];
        final double[] var32 = SIMPLEX_NOISE_GRADIENTS[var24];
        final double[] var33 = SIMPLEX_NOISE_GRADIENTS[var25];
        final double[] var34 = SIMPLEX_NOISE_GRADIENTS[var26];
        final double[] var35 = SIMPLEX_NOISE_GRADIENTS[var27];
        final double var36 = var28[0];
        final double var37 = var28[1];
        final double var38 = var28[2];
        final double var39 = var29[0];
        final double var40 = var29[1];
        final double var41 = var29[2];
        final double var42 = var30[0];
        final double var43 = var30[1];
        final double var44 = var30[2];
        final double var45 = var31[0];
        final double var46 = var31[1];
        final double var47 = var31[2];
        final double var48 = var32[0];
        final double var49 = var32[1];
        final double var50 = var32[2];
        final double var51 = var33[0];
        final double var52 = var33[1];
        final double var53 = var33[2];
        final double var54 = var34[0];
        final double var55 = var34[1];
        final double var56 = var34[2];
        final double var57 = var35[0];
        final double var58 = var35[1];
        final double var59 = var35[2];
        final double var60 = localX - 1.0;
        final double var61 = localY - 1.0;
        final double var62 = localZ - 1.0;
        final double var63 = var36 * localX;
        final double var64 = var37 * localY;
        final double var65 = var38 * localZ;
        final double var66 = var39 * var60;
        final double var67 = var40 * localY;
        final double var68 = var41 * localZ;
        final double var69 = var42 * localX;
        final double var70 = var43 * var61;
        final double var71 = var44 * localZ;
        final double var72 = var45 * var60;
        final double var73 = var46 * var61;
        final double var74 = var47 * localZ;
        final double var75 = var48 * localX;
        final double var76 = var49 * localY;
        final double var77 = var50 * var62;
        final double var78 = var51 * var60;
        final double var79 = var52 * localY;
        final double var80 = var53 * var62;
        final double var81 = var54 * localX;
        final double var82 = var55 * var61;
        final double var83 = var56 * var62;
        final double var84 = var57 * var60;
        final double var85 = var58 * var61;
        final double var86 = var59 * var62;
        final double var87 = var63 + var64 + var65;
        final double var88 = var66 + var67 + var68;
        final double var89 = var69 + var70 + var71;
        final double var90 = var72 + var73 + var74;
        final double var91 = var75 + var76 + var77;
        final double var92 = var78 + var79 + var80;
        final double var93 = var81 + var82 + var83;
        final double var94 = var84 + var85 + var86;

        final double var95 = localX * 6.0 - 15.0;
        final double var96 = fadeLocalX * 6.0 - 15.0;
        final double var97 = localZ * 6.0 - 15.0;
        final double var98 = localX * var95 + 10.0;
        final double var99 = fadeLocalX * var96 + 10.0;
        final double var100 = localZ * var97 + 10.0;
        final double var101 = localX * localX * localX * var98;
        final double var102 = fadeLocalX * fadeLocalX * fadeLocalX * var99;
        final double var103 = localZ * localZ * localZ * var100;

        final double var104 = var90 - var89;
        final double var105 = var88 - var87;
        final double var106 = var94 - var93;
        final double var107 = var92 - var91;
        final double var108 = var101 * var104;
        final double var109 = var101 * var105;
        final double var110 = var101 * var106;
        final double var111 = var101 * var107;
        final double var112 = var89 + var108;
        final double var113 = var87 + var109;
        final double var114 = var93 + var110;
        final double var115 = var91 + var111;
        final double var116 = var112 - var113;
        final double var117 = var114 - var115;
        final double var118 = var102 * var116;
        final double var119 = var102 * var117;
        final double var120 = var113 + var118;
        final double var121 = var115 + var119;
        return var120 + (var103 * (var121 - var120));
    }

}
