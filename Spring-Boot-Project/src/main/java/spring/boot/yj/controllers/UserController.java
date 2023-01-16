package spring.boot.yj.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import spring.boot.yj.entities.Address;
import spring.boot.yj.entities.User;
import spring.boot.yj.model.DataChange;
import spring.boot.yj.repositories.AddressRepository;
import spring.boot.yj.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    private AddressRepository addressRepository;
    private SimpMessagingTemplate simpMessagingTemplate;
    private UserService userService;

    public UserController(AddressRepository addressRepository,SimpMessagingTemplate simpMessagingTemplate,UserService userService) {
        this.addressRepository = addressRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.userService = userService;
    }

    @GetMapping("/{userId}/address")
    public ResponseEntity<List<Address>> getAddress(@AuthenticationPrincipal User user, @PathVariable Long userId) {
        try {
            if (!userService.userHasPermissionToUser(user, userId)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            List<Address> addresses = new ArrayList<Address>();
            addresses = addressRepository.findByUser_Id(userId);
            if (addresses.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(addresses, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{userId}/address")
    public ResponseEntity<Address> updateAddress(
            @AuthenticationPrincipal User user, @PathVariable Long userId, @RequestBody Address address) {
        try {
            if (!userService.userHasPermissionToUser(user, userId)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            address.setId(null);
            User refUser = new User();
            refUser.setId(userId);
            address.setUser(refUser);
           Address savedAddress= addressRepository.save(address);
           simpMessagingTemplate.convertAndSend("/topic/user/"+userId + "/address",
           new DataChange<>(DataChange.ChangeType.INSERT,address));
            return new ResponseEntity<>(savedAddress, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PatchMapping("/{userId}/address/{addressId}")
    public ResponseEntity<Address> patchAddress(
            @AuthenticationPrincipal User user, @PathVariable Long userId, @PathVariable Long addressId,
            @RequestBody Address address) {
        try {
            if (!userService.userHasPermissionToUser(user, userId)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            if (address.getId() == addressId) {
                Optional<Address> opOriginalAddress = addressRepository.findById(addressId);
                if (opOriginalAddress.isPresent()) {
                    User originalUser = opOriginalAddress.get().getUser();
                    if (originalUser.getId() == userId) {
                        address.setUser(originalUser);
                        Address savedAddress= addressRepository.save(address);
           simpMessagingTemplate.convertAndSend("/topic/user/"+userId + "/address",
           new DataChange<>(DataChange.ChangeType.UPDATE,address));
                        return new ResponseEntity<>(savedAddress, HttpStatus.OK);
                    }
                }
            }
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
