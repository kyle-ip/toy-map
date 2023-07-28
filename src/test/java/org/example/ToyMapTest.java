package org.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("测试 ToyMap")
class ToyMapTest {

    private static int[] strToIntArray(String str) {
        if (str == null || str.isEmpty() || "[]".equals(str)) {
            return new int[]{};
        }
        if ("null".equals(str)) {
            return null;
        }
        return Arrays.stream(str.split(","))
                .map(String::trim)
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    @BeforeAll
    public static void init() {
        //
    }

    @ParameterizedTest
    @DisplayName("测试存入、读取非空值")
    @CsvSource({
            "'1, 2, 3, 4, 5'",
            "'38298, 8760, 60, 558032, 8, 824821, 709, 2727, 36, 69161'",
    })
    void testPutAndGet(ArgumentsAccessor arguments) {
        int[] nums = strToIntArray(arguments.getString(0));
        assert nums != null;

        ToyMap<Integer, Integer> tm1 = new ToyMap<>(nums.length);
        ToyMap<String, String> tm2 = new ToyMap<>(nums.length);
        for (int i = 0; i < nums.length; i++) {
            tm1.put(nums[i], nums[nums.length - i - 1]);
            tm2.put(String.valueOf(nums[i]), String.valueOf(nums[nums.length - i - 1]));
        }

        for (int i = 0; i < nums.length; i++) {
            assertEquals(nums[nums.length - i - 1], tm1.get(nums[i]));
            assertEquals(String.valueOf(nums[nums.length - i - 1]), tm2.get(String.valueOf(nums[i])));
        }
    }

    @Test
    @DisplayName("测试存入、读取空值")
    void testPutNull() {
        ToyMap<Object, Object> tm = new ToyMap<>();
        tm.put(null, null);
        assertNull(tm.get(null));

        assertNull(tm.get(1));
    }


    @Test
    @DisplayName("测试初始化负值")
    void testInitWithNegative() {
        ToyMap<Object, Object> tm = new ToyMap<>(-1);
        tm.put(null, null);
        assertNull(tm.get(null));
    }

    @ParameterizedTest
    @DisplayName("测试存入元素触发扩容")
    @CsvSource({
            "'1, 2, 3', 1, 3, 4",
            "'1, 2, 3, 4, 5', 1, 5, 8",
    })
    void testPutAndResize(ArgumentsAccessor arguments) {
        int[] nums = strToIntArray(arguments.getString(0));
        assert nums != null;

        int initCap = arguments.getInteger(1);
        int expectedSize = arguments.getInteger(2);
        int expectedCap = arguments.getInteger(3);
        ToyMap<Integer, Integer> tm = new ToyMap<>(initCap);
        for (int num : nums) {
            tm.put(num, num);
        }

        assertEquals(expectedSize, tm.size());
        assertEquals(expectedCap, tm.capacity());
    }
}