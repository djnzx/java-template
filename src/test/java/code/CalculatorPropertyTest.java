package code;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;
import net.jqwik.api.Tuple;
import net.jqwik.api.constraints.IntRange;

class CalculatorPropertyTest {

  private final Calculator calculator = new Calculator();

  @Property
  void additionIsCommutative(@ForAll int a, @ForAll int b) {
    assertThat(calculator.add(a, b)).isEqualTo(calculator.add(b, a));
  }

  @Property
  void multiplicationIsCommutative(@ForAll int a, @ForAll int b) {
    assertThat(calculator.multiply(a, b)).isEqualTo(calculator.multiply(b, a));
  }

  @Property
  void subtractionUndoesAddition(@ForAll int a, @ForAll int b) {
    assertThat(calculator.subtract(calculator.add(a, b), b)).isEqualTo(a);
  }

  @Property
  void multiplyingByZeroIsZero(@ForAll int a) {
    assertThat(calculator.multiply(a, 0)).isZero();
  }

  @Property
  void dividingByOneReturnsSameValue(@ForAll int a) {
    assertThat(calculator.divide(a, 1)).isEqualTo(a);
  }

  @Property
  void divisionByZeroAlwaysThrows(@ForAll int a) {
    assertThatThrownBy(() -> calculator.divide(a, 0)).isInstanceOf(ArithmeticException.class);
  }

  @Property
  void divisionByNonZeroNeverThrows(@ForAll int a, @ForAll @IntRange(min = 1) int b) {
    calculator.divide(a, b);
  }

  @Provide
  Arbitrary<Tuple.Tuple2<Integer, Integer>> evenlyDivisiblePairs() {
    Arbitrary<Integer> divisors = Arbitraries.integers().between(1, 1_000);
    Arbitrary<Integer> quotients = Arbitraries.integers().between(-1_000, 1_000);
    return Combinators.combine(divisors, quotients)
        .as((divisor, quotient) -> Tuple.of(divisor * quotient, divisor));
  }

  @Property
  void divisionOfEvenlyDivisibleNumbersHasNoRemainder(
      @ForAll("evenlyDivisiblePairs") Tuple.Tuple2<Integer, Integer> pair) {
    int dividend = pair.get1();
    int divisor = pair.get2();
    assertThat(calculator.multiply(calculator.divide(dividend, divisor), divisor))
        .isEqualTo(dividend);
  }
}
