package net.mrmisc.essenceofthewild.assets;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HopAnimationTest {
    private static final Path ANIMATIONS =
            Path.of("src/main/resources/assets/essenceofthewild/animations/entity");

    private static JsonObject clip(String file, String name) throws IOException {
        try (Reader r = Files.newBufferedReader(ANIMATIONS.resolve(file), StandardCharsets.UTF_8)) {
            return JsonParser.parseReader(r).getAsJsonObject()
                    .getAsJsonObject("animations").getAsJsonObject(name);
        }
    }

    private static List<Double> times(JsonObject channel) {
        List<Double> out = new ArrayList<>();
        for (Map.Entry<String, ?> e : channel.entrySet()) {
            out.add(Double.parseDouble(e.getKey()));
        }
        return out;
    }

    @ParameterizedTest
    @CsvSource({
            "rabbit.animation.json, animation.rabbit.walk",
            "rabbit.animation.json, animation.rabbit.run",
            "hare.animation.json,   animation.hare.walk",
            "hare.animation.json,   animation.hare.run",
    })
    void hopSpansExactlyOneLoop(String file, String name) throws IOException {
        JsonObject clip = clip(file, name);
        double length = clip.get("animation_length").getAsDouble();
        JsonObject body = clip.getAsJsonObject("bones").getAsJsonObject("body");
        assertTrue(body != null && body.has("position"), name + " has no body position channel");

        JsonObject position = body.getAsJsonObject("position");
        List<Double> times = times(position);
        assertEquals(0.0, times.get(0), 1e-6, name + " hop does not start at 0");
        assertEquals(length, times.get(times.size() - 1), 1e-4, name + " hop does not end at animation_length");
    }

    @ParameterizedTest
    @CsvSource({
            "rabbit.animation.json, animation.rabbit.walk",
            "rabbit.animation.json, animation.rabbit.run",
            "hare.animation.json,   animation.hare.walk",
            "hare.animation.json,   animation.hare.run",
    })
    void hopLeavesTheGroundAndComesBackToIt(String file, String name) throws IOException {
        JsonObject position = clip(file, name).getAsJsonObject("bones")
                .getAsJsonObject("body").getAsJsonObject("position");

        double lowest = Double.MAX_VALUE;
        double highest = -Double.MAX_VALUE;
        for (Map.Entry<String, ?> e : position.entrySet()) {
            double y = position.getAsJsonObject(e.getKey()).getAsJsonArray("post").get(1).getAsDouble();
            lowest = Math.min(lowest, y);
            highest = Math.max(highest, y);
        }
        assertEquals(0.0, lowest, 1e-6, name + " never touches the ground");
        assertTrue(highest > 0.5, name + " hop is too shallow to read as a hop");
    }

    @Test
    void walkCarriesOneHopNotTwo() throws IOException {
        for (String file : List.of("rabbit.animation.json", "hare.animation.json")) {
            String name = "animation." + file.substring(0, file.indexOf('.')) + ".walk";
            JsonObject bones = clip(file, name).getAsJsonObject("bones");
            JsonObject bone = bones.getAsJsonObject("bone");
            assertFalse(bone.has("position") && times(bone.getAsJsonObject("position")).size() > 1,
                    name + " still bounces the bone channel against the body hop");
        }
    }
}
