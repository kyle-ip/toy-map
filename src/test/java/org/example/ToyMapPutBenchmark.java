package org.example;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.All)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@OutputTimeUnit(TimeUnit.SECONDS)
@Threads(4)
@Fork(2)
@State(value = Scope.Benchmark)
public class ToyMapPutBenchmark {

    private HashMap<String, String> hashMap = new HashMap<>(0);
    private ToyMap<String, String> toyMap = new ToyMap<>(0);

    public static final int SIZE = 10_000;

    private static final int STRING_SIZE = 64;

    private static final String[] STRINGS = new String[SIZE];

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateRandomString() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < STRING_SIZE; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }

    @Setup
    public void setup() {
        for (int i = 0; i < SIZE; i++) {
            STRINGS[i] = generateRandomString();
        }
    }


    @Benchmark
    public void testHashMap() {
        for (int i = 0; i < SIZE; i++) {
            hashMap.put(STRINGS[i], STRINGS[i]);
        }
    }

    @Benchmark
    public void testToyMap() {
        for (int i = 0; i < SIZE; i++) {
            toyMap.put(STRINGS[i], STRINGS[i]);
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ToyMapPutBenchmark.class.getSimpleName())
                .forks(1)
                .resultFormat(ResultFormatType.JSON)
                .result("put_benchmark.json")
                .build();
        new Runner(opt).run();
    }

}
