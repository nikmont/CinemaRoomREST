package cinema.controller;

import cinema.exceptions.AlreadyPurchasedException;
import cinema.exceptions.SeatOutOfBoundExceprion;
import cinema.model.CinemaRoom;
import cinema.model.Seat;
import cinema.service.CinemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
            Seat seatToPurchase = service.purchase(seat);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(seatToPurchase);
        }
        catch(AlreadyPurchasedException | SeatOutOfBoundExceprion e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }


}
