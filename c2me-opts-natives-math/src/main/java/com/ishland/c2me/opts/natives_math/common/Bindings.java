package com.ishland.c2me.opts.natives_math.common;

import java.lang.foreign.MemorySegment;
import java.lang.invoke.MethodHandle;

public class Bindings {

    private static MethodHandle bind(MethodHandle template, String prefix) {
        return template.bindTo(NativeLoader.lookup.find(prefix + NativeLoader.currentMachineTarget.getSuffix()).get());
    }

    private static final MethodHandle MH_c2me_natives_noise_perlin_double_octave_sample = bind(BindingsTemplate.c2me_natives_noise_perlin_double_octave_sample, "c2me_natives_noise_perlin_double_octave_sample");

    public static double c2me_natives_noise_perlin_double_octave_sample(MemorySegment data, double x, double y, double z) {
        try {
            return (double) MH_c2me_natives_noise_perlin_double_octave_sample.invokeExact(data, x, y, z);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

}
