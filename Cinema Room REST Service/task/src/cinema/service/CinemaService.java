package cinema.service;

import cinema.exceptions.AlreadyPurchasedException;
import cinema.exceptions.SeatOutOfBoundExceprion;
import cinema.exceptions.WrongPasswordException;
import cinema.exceptions.WrongTokenException;
import cinema.model.CinemaRoom;
import cinema.model.Purchase;
import cinema.model.Seat;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CinemaService {
    private final int TOTAL_ROWS = 9;
    private final int TOTAL_COLS = 9;
    private final String PASSWORD = "super_secret";

    private CinemaRoom cinema = new CinemaRoom(TOTAL_ROWS, TOTAL_COLS);
    private List<Purchase> purchaseList = new ArrayList<>();

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

    public Purchase purchase(Seat seat) {

        //Seat toPurchase = cinema.getAllSeats().get(seat.getRow() * seat.getColumn() - 1);

        Seat toPurchase = findSeatByRowCol(seat.getRow(), seat.getColumn());
        toPurchase.setAvaliable(false);

        Purchase currentPurchase = new Purchase(toPurchase);
        purchaseList.add(currentPurchase);

        return currentPurchase;
    }

    private Seat findSeatByRowCol(int row, int col) {
        return cinema.getSeats().stream()
                .filter(s -> s.getRow() == row && s.getColumn() == col)
                .findFirst().get();
    }

    public Map<String, Seat> refund(String id) {

        Seat refundedSeat = purchaseList.stream()
                .filter(p -> id.equals(p.getToken()))
                .map(Purchase::getTicket)
                .findFirst().orElseThrow(() -> new WrongTokenException("Wrong token!"));

        purchaseList.remove(purchaseList.stream()
                .filter(p -> id.equals(p.getToken()))
                        .findFirst().get());

        refundedSeat.setAvaliable(true);

        return Map.of("returned_ticket", refundedSeat);
    }

    public boolean checkPass(String pswd) {
        if (pswd == null | !PASSWORD.equals(pswd)) throw new WrongPasswordException("The password is wrong!");

        return true;
    }

    public Map<String, Integer> getCurrentStats() {

        Integer total = purchaseList.stream()
                .map(Purchase::getTicket)
                .mapToInt(Seat::getPrice).sum();

        return Map.of("current_income", total,
                "number_of_available_seats", cinema.getSeats().size(),
                "number_of_purchased_tickets", purchaseList.size());
    }

}
