package com.evgeniyfedorchenko.simplearraylist.implementations;

import com.evgeniyfedorchenko.simplearraylist.interfaces.SimpleList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

import static com.evgeniyfedorchenko.simplearraylist.implementations.Constants.*;
import static org.assertj.core.api.Assertions.*;


/* Тестовый класс проверяет работу только со типом Integer */
class SimpleArrayListIntegerTest {

    private final SimpleList<Integer> out = new SimpleArrayList<>();

    @BeforeEach
    public void beforeEach() {
        IntStream.range(0, 7).forEach(i -> out.add(INTEGER_1));
    }

    @AfterEach
    public void afterEach() {
        out.clear();
    }

    @Test
    public void add_without_index_positive_test() {
        // given
        int sizeBeforeAdding = out.size();
        // invoking
        out.add(INTEGER_2);
        // assertions
        assertThat(out.toArray()).isNotEmpty();
        assertThat(INTEGER_2)
                .isEqualTo(out.get(7))
                .isIn(out);
        assertThat(out.size()).isEqualTo(sizeBeforeAdding + 1);
    }

    @Test
    public void add_without_index_with_boostSize_test() {
        // given
        IntStream.range(0, 3).forEach(i -> out.add(INTEGER_1));
        // invoking and assertions
        assertThatCode(() -> out.add(INTEGER_1)).doesNotThrowAnyException();
    }

    @Test
    public void npe_test_in_add_without_index() {
        assertThatThrownBy(() -> out.add(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void add_with_index_positive_test() {
        // given
        int sizeBeforeAdding = out.size();
        // invoking
        out.add(3, INTEGER_2);
        // assertions
        assertThat(INTEGER_2)
                .isEqualTo(out.get(3))
                .isIn(out);
        assertThat(out.size()).isEqualTo(sizeBeforeAdding + 1);

    }

    @Test
    public void add_with_index_recursive_call_test() {
        IntStream.range(0, 3).forEach(i -> out.add(INTEGER_1));
        assertThatCode(() -> out.add(3, INTEGER_2))
                .doesNotThrowAnyException();
    }

    @Test
    public void add_with_index_negative_test1() {
        // given
        int sizeBeforeAdding = out.size();

        // invoking and assertions
        // Попытка вставки за пределы массива  -> [one, one, one, one, one, one, one, null, null, null]   ВОТ_СЮДА
        assertThatThrownBy(() -> out.add(11, INTEGER_1))
                .isInstanceOf(IndexOutOfBoundsException.class);

        // Попытка вставки на null-поизицию (внутри массива) -> [one, one, one, one, one, one, one, null, ВОТ_СЮДА, null]
        assertThatThrownBy(() -> out.add(8, INTEGER_1))
                .isInstanceOf(IndexOutOfBoundsException.class);

        assertThat(sizeBeforeAdding).isEqualTo(out.size());
    }

    @Test
    public void npe_test_in_add_with_index() {
        assertThatThrownBy(() -> out.add(2, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void set_positive_test() {
        // given
        int sizeBeforeSetting = out.size();
        // invoking
        out.set(4, INTEGER_2);
        // assertions
        assertThat(INTEGER_2)
                .isEqualTo(out.get(4))
                .isIn(out);
        assertThat(sizeBeforeSetting).isEqualTo(out.size());
    }

    @Test
    public void set_negative_test() {
        // given
        int sizeBeforeSetting = out.size();

        // invoking and assertions
        // Попытка вставки за пределы массива  -> [one, one, one, one, one, one, one, null, null, null]   ВОТ_СЮДА
        assertThatThrownBy(() -> out.set(11, INTEGER_1))
                .isInstanceOf(IndexOutOfBoundsException.class);

        // Попытка вставки на null-позицию (внутри массива) -> [one, one, one, one, one, one, one, null, ВОТ_СЮДА, null]
        assertThatThrownBy(() -> out.set(8, INTEGER_1))
                .isInstanceOf(IndexOutOfBoundsException.class);

        assertThat(sizeBeforeSetting).isEqualTo(out.size());
    }

    @Test
    public void npe_test_in_set() {
        assertThatThrownBy(() -> out.set(2, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void remove_on_index_positive_test() {
        // given
        out.set(3, INTEGER_2);
        int sizeBeforeRemoving = out.size();
        // invoking
        out.remove(3);
        // assertions
        assertThat(INTEGER_2)
                .isNotEqualTo(out.get(3))
                .isNotIn(out);
        assertThat(out.size()).isEqualTo(sizeBeforeRemoving - 1);
    }

    @Test
    public void remove_on_index_negative_test() {
        // given
        int sizeBeforeRemoving = out.size();
        // invoking and assertions
        assertThatThrownBy(() -> out.remove(8))
                .isInstanceOf(IndexOutOfBoundsException.class);
        assertThat(sizeBeforeRemoving).isEqualTo(out.size());
    }

    @Test
    public void remove_on_item_positive_test() {
        // given
        out.set(3, INTEGER_2);
        int sizeBeforeRemoving = out.size();
        // invoking
        out.remove(INTEGER_1);
        // assertions
        assertThat(INTEGER_2)
                .isEqualTo(out.get(2))
                .isNotIn(out);
        assertThat(out.size()).isEqualTo(sizeBeforeRemoving - 1);
    }

    @Test
    public void remove_item_negative_test() {
        assertThatThrownBy(() -> out.remove(INTEGER_2))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void npe_test_in_remove_on_item() {
        assertThatThrownBy(() -> out.remove(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void indexOf_positive_test() {
        // given
        out.add(INTEGER_2);
        out.add(INTEGER_3);
        // invoking and assertions
        assertThat(out.indexOf(INTEGER_2))
                .isEqualTo(7)
                .isNotEqualTo(3)
                .isNotEqualTo(9);

    }

    @Test
    public void indexOf_negative_test() {
        // invoking and assertions
        assertThat(out.indexOf(INTEGER_2)).isEqualTo(-1);
        assertThatCode(() -> out.lastIndexOf(INTEGER_3))
                .doesNotThrowAnyException();
    }

    @Test
    public void npe_test_in_indexOf() {
        assertThatThrownBy(() -> out.indexOf(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void lastIndexOf_positive_test() {
        // given
        out.add(INTEGER_2);
        out.add(INTEGER_3);
        // assertions
        assertThat(out.lastIndexOf(INTEGER_1))
                .isEqualTo(6)
                .isNotEqualTo(5);
    }

    @Test
    public void lastIndexOf_negative_test() {
        // invoking and assertions
        assertThat(out.lastIndexOf(INTEGER_2)).isEqualTo(-1);
        assertThatCode(() -> out.lastIndexOf(INTEGER_3))
                .doesNotThrowAnyException();
    }

    @Test
    public void npe_test_in_lastIndexOf() {
        assertThatThrownBy(() -> out.lastIndexOf(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void contains_positive_test() {
        // given
        out.add(INTEGER_2);
        // assertions
        assertThat(out.contains(INTEGER_1)).isTrue();
        assertThat(out.contains(INTEGER_2)).isTrue();

    }

    @Test
    public void contains_negative_test() {
        // assertion
        assertThat(out.contains(INTEGER_2)).isFalse();
    }

    @Test
    public void get_positive_test() {
        // given
        out.add(INTEGER_1);
        out.add(INTEGER_2);
        // invoking and assertion
        assertThat(INTEGER_1)
                .isEqualTo(out.get(0))
                .isEqualTo(out.get(6));
    }

    @Test
    public void get_negative_test() {
        // assertion
        assertThatThrownBy(() -> out.get(7))
                .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    public void equals_positive_test() {
        // given
        SimpleList<Integer> actual = new SimpleArrayList<>(SEVEN_INTEGER_1_TEST_ARRAY_LIST);
        // invoking and assertion
        assertThat(out.equals(actual)).isTrue();
    }

    @Test
    public void equals_positive_test2() {
        // given
        SimpleList<Integer> sameList = out;
        // invoking and assertion
        assertThat(out.equals(sameList)).isTrue();
    }

    @Test
    public void equals_negative_test() {
        // given
        SimpleList<Integer> actual = null;
        // invoking and assertion
        assertThat(out.equals(actual)).isFalse();
    }

    @Test
    public void equals_negative_test2() {
        // given
        List<Integer> actual = new ArrayList<>(SEVEN_INTEGER_1_TEST_ARRAY_LIST);
        // invoking and assertion
        assertThat(out.equals(actual)).isFalse();
    }

    @Test
    public void equals_negative_test3() {
        // given
        SimpleList<Integer> actual = new SimpleArrayList<>(SEVEN_INTEGER_1_TEST_ARRAY_LIST);
        actual.add(INTEGER_1);
        // invoking and assertion
        assertThat(out.equals(actual)).isFalse();
    }

    @Test
    public void equals_negative_test4() {
        // given
        SimpleList<Integer> actual = new SimpleArrayList<>(SEVEN_INTEGER_1_TEST_ARRAY_LIST);
        actual.set(3, INTEGER_2);
        // invoking and assertions
        assertThat(out.size() == actual.size()).isTrue();
        assertThat(out.equals(actual)).isFalse();
    }

    @Test
    public void size_test() {
        // invoking and assertions
        assertThat(out.size()).isEqualTo(7);
        assertThat(out.size() == 7).isTrue();
    }

    @Test
    public void isEmpty_test() {
        // invoking and assertion
        assertThat(out.isEmpty()).isFalse();
        // given
        out.clear();
        // invoking and assertion
        assertThat(out.isEmpty()).isTrue();
    }

    @Test
    public void clear_test() {
        // given
        SimpleList<Integer> actual = new SimpleArrayList<>();
        // invoking
        out.clear();
        // assertions
        assertThat(out.size() == 0).isTrue();
        assertThat(out.equals(actual)).isTrue();
    }

    @Test
    public void toArray_test() {
        // given
        Integer[] array = new Integer[]{INTEGER_1, INTEGER_1, INTEGER_1, INTEGER_1, INTEGER_1, INTEGER_1, INTEGER_1};
        // invoking
        Object[] actual = out.toArray();
        // assertion
        assertThat(actual).isEqualTo(array);
    }

    @Test
    public void toString_test() {
        // given
        String actual = "[";
        for (int i = 0; i < 7; i++) {
            actual += INTEGER_1 + ", ";
        }
        actual = actual.substring(0, actual.length() - 2) + "]";
        // invoking
        String expected = out.toString();
        // assertion
        assertThat(expected).isEqualTo(actual);
    }

    @Test
    public void binarySearch_test_from_contains() {
        // given
        out.add(INTEGER_2);
        out.add(INTEGER_3);
        out.add(15);
        out.add(0);
        out.add(-1);
        SimpleList<Integer> expected = new SimpleArrayList<>(
                List.of(-1, 0, 1, 1, 1, 1, 1, 1, 1, 2, 3, 15));
        // invoking and assertions
        assertThat(out.contains(-1)).isTrue();
        assertThat(out).isEqualTo(expected);
        assertThat(out.equals(expected)).isTrue();
    }
}