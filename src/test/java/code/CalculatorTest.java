package code;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class CalculatorTest {

  private Calculator calculator = new Calculator();

  @Test
  void addsTwoNumbers() {
    assertThat(calculator.add(2, 3)).isEqualTo(5);
  }

  @Test
  void subtractsTwoNumbers() {
    assertThat(calculator.subtract(5, 3)).isEqualTo(2);
  }

  @Test
  void multipliesTwoNumbers() {
    assertThat(calculator.multiply(4, 3)).isEqualTo(12);
  }

  @Test
  void dividesTwoNumbers() {
    assertThat(calculator.divide(10, 2)).isEqualTo(5);
  }

  @Test
  void throwsOnDivisionByZero() {
    assertThatThrownBy(() -> calculator.divide(10, 0))
        .isInstanceOf(ArithmeticException.class)
        .hasMessage("Division by zero");
  }
}
