package io.wdsj.homoium.interfaces.world.chunk;

import java.util.Random;

public interface IMixinChunkMethods {
    boolean shouldDoLightning(Random random);
    boolean shouldDoIceAndSnow(Random random);
}
