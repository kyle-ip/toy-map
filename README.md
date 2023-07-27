# toy-map

A simplified HashMap implemented in open addressing with linear probing.

## Example
```java
class Example {
    public static void main(String[] args) {
        ToyMap<Integer, Integer> tm = new ToyMap<>(nums.length);
        tm.put(1, 1);
        tm.get(1);
        tm.size();
        tm.capacity();
    }
}
```

## Benchmark
Upload report files (*_benchmark.json) to [JMH Visualizer](https://jmh.morethan.io).

## About
This repository is only for interview take-home test.