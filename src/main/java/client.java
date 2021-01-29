
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.util.*;

public class client {
    static List<WeatherToday> weatherList = new ArrayList<WeatherToday>();

    public static void xmlParser(String filename){
        try{
            //xml parser
            WeatherToday today =null;
            City city = null;
            String date ="";
            String cityname ="";
            long temp;

            File file = new File(filename);
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLEventReader eventReader = factory.createXMLEventReader(new FileReader(file));

            while (eventReader.hasNext()){
                XMLEvent event = eventReader.nextEvent();

                if(event.isStartElement()) {
                    StartElement element = (StartElement) event;

                    if ("date".equalsIgnoreCase(element.getName().getLocalPart())) {
                        Characters dateToday = (Characters) eventReader.nextEvent();
                        date = dateToday.getData();
                        today = new WeatherToday(date);
                    }
                    if ("city".equalsIgnoreCase(element.getName().getLocalPart())) {
                        city = new City();
                    }
                        switch (element.getName().getLocalPart()) {
                            case "name":
                                Characters cityName = (Characters) eventReader.nextEvent();
                                cityname = cityName.getData();
                                city.setName(cityname);
                                break;

                            case "temperature":
                                Characters temperatureData = (Characters) eventReader.nextEvent();
                                String tempt = temperatureData.getData();//with leading and trailing spaces
                                String editTempt = tempt.trim();
                                temp = Long.valueOf(editTempt);
                                city.setTemp(temp);
                                break;
                        }
                }//end of start element
                if (event.isEndElement()){
                    EndElement endElement = (EndElement) event;
                    if("city".equalsIgnoreCase(endElement.getName().getLocalPart())){
                        today.setCities(city);
                    }
                    if ("date".equalsIgnoreCase(endElement.getName().getLocalPart())){
                        weatherList.add(today);
                    }
                }//end of endElement
            }//end of while eventReader
            calcAvgTemp(weatherList);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void jsonParser(String filename){

        JSONParser parser = new JSONParser();
        try {
            FileReader inputFile = new FileReader(filename);//file from the user
            Object obj = parser.parse(inputFile);//create an object by parsing the file
            JSONObject jsonObject = (JSONObject) obj;//cast the object as json object and create a json object
            JSONArray weather = (JSONArray) jsonObject.get("weather");//from the json object get the array and cast it to be json array
            Iterator iterator = weather.iterator();//iterate through the json array

            String prevDate = "";
            String currDate;
            String cityName;
            long temp;
            WeatherToday today;
            City city;
                /*
                Assuming that the data in the json file is in ascending order of the date. example: all the data for jan 1
                preceed the data for jan2.
                 */
            while (iterator.hasNext()){
                JSONObject jobj = (JSONObject) iterator.next();//extract json object from each array element
                currDate = (String) jobj.get("date");//get the values from the key value pair of the json object
                cityName = (String)jobj.get("city");
                temp = (long) jobj.get("temperature");

                if(!prevDate.equals(currDate)){
                    today = new WeatherToday(currDate);
                    city = new City(cityName, temp);
                    today.setCities(city);
                    weatherList.add(today);
                    prevDate = currDate;
                }else{
                    int index = weatherList.size() -1;
                    weatherList.get(index).setCities(new City(cityName, temp));
                }
            }//end of while loop that checks if the iterator has any object
            calcAvgTemp(weatherList);

        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    /*
    calculates avg weather per day from the list of weatherToday objects, prints the results on console
     */
    public static void calcAvgTemp(List<WeatherToday> weatherList){

        int numOfCities;
        int totalTemp = 0;
        double avgTemp= 0.00;
        for (WeatherToday w:weatherList) {
            System.out.println("\n Date : "+ w.getDate()+"\n");
            numOfCities = w.getCities().size();
            for (City c: w.getCities()) {
                System.out.println("City name : " + c.getName() +",         Temperature : "+ c.getTemp());
                totalTemp += c.getTemp();
            }
            avgTemp=totalTemp/numOfCities;
            System.out.println();
            System.out.println("Total number of cities: "+ numOfCities);
            System.out.println("Total temperature: "+ totalTemp);
            System.out.println("Average temperature: "+avgTemp);
            System.out.println("_____________________________________________");
        }

    }

    public static void main(String[] args)  {

        Scanner in = new Scanner(System.in);
        System.out.println("Please provide an XML or a JSON file to read and process the weather information from.");
        String filename = in.nextLine();

        int e = filename.lastIndexOf(".");
        String ext = filename.substring(e+1);

        while (!filename.substring(filename.lastIndexOf(".")+1).equals("json") && !filename.substring(filename.lastIndexOf(".")+1).equals("xml")){
            System.out.println("Enter a xml or json file only");
            filename = in.nextLine();
        }
        if(ext.equalsIgnoreCase("json")){
            jsonParser(filename);
        }else{
            xmlParser(filename);
        }

    }
}
