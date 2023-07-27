package org.example;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.All)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@OutputTimeUnit(TimeUnit.SECONDS)
@Threads(4)
@Fork(2)
@State(value = Scope.Benchmark)
public class ToyMapGetBenchmark {

    private HashMap<Integer, Integer> hashMap = new HashMap<>(SIZE);
    private ToyMap<Integer, Integer> toyMap = new ToyMap<>(SIZE);

    public static final int SIZE = 1_000_000;

    @Setup
    public void setup() {
        hashMap = new HashMap<>();
        toyMap = new ToyMap<>();
        for (int i = 0; i < SIZE; i++) {
            hashMap.put(i, i);
            toyMap.put(i, i);
        }
    }

    @Benchmark
    public int testHashMap() {
        int sum = 0;
        for (int i = 0; i < SIZE; i++) {
            sum += hashMap.get(i);
        }
        return sum;
    }

    @Benchmark
    public int testToyMap() {
        int sum = 0;
        for (int i = 0; i < SIZE; i++) {
            sum += toyMap.get(i);
        }
        return sum;
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ToyMapGetBenchmark.class.getSimpleName())
                .forks(1)
                .resultFormat(ResultFormatType.JSON)
                .result("get_benchmark.json")
                .build();
        new Runner(opt).run();
    }

}
