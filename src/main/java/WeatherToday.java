import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WeatherToday {
    private String date;
    private List<City> cities = new ArrayList<City>();

    public WeatherToday(String date){
        this.date = date;
        //think about how to make a class that can not have duplicate class with the same date!!!
    }

    public String getDate() {
        return date;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(City city) {
        cities.add(city);
    }

}
