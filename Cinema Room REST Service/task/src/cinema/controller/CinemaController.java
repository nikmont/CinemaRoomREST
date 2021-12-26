package cinema.controller;

import cinema.exceptions.AlreadyPurchasedException;
import cinema.exceptions.SeatOutOfBoundExceprion;
import cinema.exceptions.WrongPasswordException;
import cinema.exceptions.WrongTokenException;
import cinema.model.CinemaRoom;
import cinema.model.Purchase;
import cinema.model.Seat;
import cinema.service.CinemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class CinemaController {

    CinemaService service;

    @Autowired
    public CinemaController(CinemaService service) {
        this.service = service;
    }

    @GetMapping("/seats")
    CinemaRoom getAvaliable() {
        return service.getInfo();
    }

    @PostMapping("/purchase")
    public ResponseEntity buyTicket(@RequestBody Seat seat) {

        try {
            service.checkPurchase(seat);
            Purchase purchase = service.purchase(seat);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(purchase);

        }
        catch (AlreadyPurchasedException | SeatOutOfBoundExceprion e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }



    @PostMapping("/return")
    public ResponseEntity returnTicket(@RequestBody Map<String, String> token) {
        try {


            Map<String, Seat> toRefund = service.refund(token.get("token"));

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(toRefund);

        } catch (WrongTokenException e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/stats")
    public ResponseEntity getStatistic(@RequestParam(required = false) String password) {

        try {
            service.checkPass(password);
            Map<String, Integer> info = service.getCurrentStats();

            return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(info);

        } catch (WrongPasswordException e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", e.getMessage()));
        }
    }


}
