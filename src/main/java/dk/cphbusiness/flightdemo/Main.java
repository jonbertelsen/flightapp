package dk.cphbusiness.flightdemo;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Main
{

    public static void main(String[] args) throws IOException
    {
        FlightReader flightReader = new FlightReader();
        List<DTOs.FlightDTO> flightList = flightReader.getFlightsFromFile("flights.json");
        List<DTOs.FlightInfo> flightInfoList = flightReader.getFlightInfoDetails(flightList);
        //flightInfoList.stream().forEach(System.out::println);

        // calculate the average flight time for a specific airline (Lufthansa).
        double averageFlightTime = flightInfoList.stream()
                .filter(flightInfo -> flightInfo.getAirline() != null)
                .filter(flightInfo -> flightInfo.getAirline().equals("Lufthansa"))
                .mapToLong(flightInfo -> flightInfo.getDuration().toMinutes())
                .average()
                .getAsDouble();
        System.out.println("Average flight time for Lufthansa: " + averageFlightTime + " minutes");

        // calculate the total flight time for a specifc airline (Lufthansa)
        long totalFlightTime = flightInfoList.stream()
                .filter(flightInfo -> flightInfo.getAirline() != null)
                .filter(flightInfo -> flightInfo.getAirline().equals("Lufthansa"))
                .mapToLong(flightInfo -> flightInfo.getDuration().toMinutes())
                .sum();
        System.out.println("Total flight time for Lufthansa: " + totalFlightTime + " minutes");

        // make a list of flights that are operated between two specific airports. For example, all flights between Fukuoka and Haneda Airport
        System.out.println("Flights between Fukuoka and Haneda Airport:");
        flightInfoList.stream()
                .filter(flightInfo -> flightInfo.getOrigin() != null)
                .filter(flightInfo -> flightInfo.getOrigin().equals("Fukuoka"))
                .filter(flightInfo -> flightInfo.getDestination().equals("Haneda Airport"))
                .forEach(System.out::println);

        // make a list of flights that leaves before a specific time in the day. For example, all flights that leave before 02:00
        System.out.println("Flights that leave before 00:30:");
        flightInfoList.stream()
                .filter(flightInfo -> flightInfo.getDeparture() != null)
                .filter(flightInfo -> flightInfo.getDeparture().getHour() < 2)
                .forEach(System.out::println);

        // calculate the average flight time for each airline
        System.out.println("Average flight time for each airline:");
        flightInfoList.stream()
                .filter(flightInfo -> flightInfo.getAirline() != null)
                .collect(Collectors.groupingBy(DTOs.FlightInfo::getAirline, Collectors.averagingLong(flightInfo -> flightInfo.getDuration().toMinutes())))
                .forEach((airline, average) -> System.out.println(airline + ": " + average + " minutes"));

        // make a list of all flights sorted by arrival time
        System.out.println("Flights sorted by arrival time:");
        flightInfoList.stream()
                .sorted((flightInfo1, flightInfo2) -> flightInfo1.getArrival().compareTo(flightInfo2.getArrival()))
                .limit(50)
                .forEach(System.out::println);

        // (calculate the total flight time for each airline
        System.out.println("Total flight time for each airline:");
        flightInfoList.stream()
                .filter(flightInfo -> flightInfo.getAirline() != null)
                .collect(Collectors.groupingBy(DTOs.FlightInfo::getAirline, Collectors.summingLong(flightInfo -> flightInfo.getDuration().toMinutes())))
                .forEach((airline, total) -> System.out.println(airline + ": " + total + " minutes"));

        // make a list of all flights sorted by duration in descending order
        System.out.println("Flights sorted by duration in descending order:");
        flightInfoList.stream()
                .sorted((flightInfo1, flightInfo2) -> flightInfo2.getDuration().compareTo(flightInfo1.getDuration()))
                .limit(150)
                .forEach(System.out::println);

    }
}
