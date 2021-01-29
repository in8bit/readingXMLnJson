public class City {

        private String name;
        private long temp;

        City(String name, long temp) {
            this.name = name;
            this.temp = temp;
        }
        City(){}

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getTemp() {
            return temp;
        }

        public void setTemp(long temp) {
            this.temp = temp;
        }

}
