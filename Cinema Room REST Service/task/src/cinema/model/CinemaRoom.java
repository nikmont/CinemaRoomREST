package cinema.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CinemaRoom {

    @JsonProperty("total_rows")
    private int totalRows;
    @JsonProperty("total_columns")
    private int totalColumns;
    @JsonProperty("available_seats")
    private List<Seat> seats;

    public CinemaRoom() {
    }

    public CinemaRoom(int total_rows, int total_columns) {
        this.totalRows = total_rows;
        this.totalColumns = total_columns;
        seats = new ArrayList<>();

        for (int i = 1; i <= total_rows; i++) {
            for (int j = 1; j <= total_columns; j++) {
                Seat seat = new Seat(i, j);

                if (i <= 4) {
                    seat.setPrice(10);
                } else {
                    seat.setPrice(8);
                }

                seats.add(seat);
            }
        }

    }

    @JsonGetter(value = "total_rows")
    public int getTotalRows() {
        return totalRows;
    }

    @JsonGetter(value = "total_columns")
    public int getTotalColumns() {
        return totalColumns;
    }

    @JsonGetter(value = "available_seats")
    public List<Seat> getSeats() {
        return seats.stream().filter(Seat::isAvaliable).collect(Collectors.toList());
    }

    @JsonIgnore
    public List<Seat> getAllSeats() {
        return seats;
    }
}
