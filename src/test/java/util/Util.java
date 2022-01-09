package util;
public class Util {

    public enum iDayOfWeek {
        Sunday(7),Monday(1),Tuesday(2),Wednesday(3),Thursday(4),Friday(5),Saturday(6);

        private final int value;

        iDayOfWeek(int value) {

            this.value = value;
        }

        public int getValue() {

            return value;
        }

        @Override
        public String toString() {

            return value + "";
        }
    }

}