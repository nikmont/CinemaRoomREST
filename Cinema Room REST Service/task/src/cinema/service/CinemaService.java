package cinema.service;

import cinema.exceptions.AlreadyPurchasedException;
import cinema.exceptions.SeatOutOfBoundExceprion;
import cinema.model.CinemaRoom;
import cinema.model.Seat;
import org.springframework.stereotype.Service;

@Service
public class CinemaService {

    private final int TOTAL_ROWS = 9;
    private final int TOTAL_COLS = 9;
    CinemaRoom cinema = new CinemaRoom(TOTAL_ROWS, TOTAL_COLS);

    public CinemaRoom getInfo() {
        return cinema;
    }

    public void checkPurchase(Seat seat) {
        int row = seat.getRow();
        int col = seat.getColumn();

        if (row > TOTAL_ROWS || row < 0 || col > TOTAL_COLS || col < 0) throw new SeatOutOfBoundExceprion("The number of a row or a column is out of bounds!");

        Seat toPurchase = cinema.getAllSeats().get(row * col - 1);
        if (!toPurchase.isAvaliable()) throw new AlreadyPurchasedException("The ticket has been already purchased!");
    }

    public Seat purchase(Seat seat) {
        Seat toPurchase = cinema.getAllSeats().get(seat.getRow() * seat.getColumn() - 1);
        toPurchase.setAvaliable(false);

        return toPurchase;
    }

}
