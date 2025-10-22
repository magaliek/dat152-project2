/**
 * 
 */
package no.hvl.dat152.rest.ws.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.model.User;
import no.hvl.dat152.rest.ws.service.UserService;

/**
 * @author tdoy
 */
@RestController
@RequestMapping("/elibrary/api/v1")
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/users")
	public ResponseEntity<Object> getUsers(){
		
		List<User> users = userService.findAllUsers();
		
		if(users.isEmpty())
			
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		else
			return new ResponseEntity<>(users, HttpStatus.OK);
	}
	
	@GetMapping(value = "/users/{id}")
	public ResponseEntity<Object> getUser(@PathVariable Long id) throws UserNotFoundException{
		
		User user = userService.findUser(id);
		
		return new ResponseEntity<>(user, HttpStatus.OK);	
		
	}
	
	// TODO - createUser (@Mappings, URI=/users, and method)
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user){
        userService.saveUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

	// TODO - updateUser (@Mappings, URI, and method)
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable Long id) throws UserNotFoundException{
        User updatedUser = userService.updateUser(user, id);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
	
	// TODO - deleteUser (@Mappings, URI, and method)
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) throws UserNotFoundException {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

	// TODO - getUserOrders (@Mappings, URI=/users/{id}/orders, and method)
    @GetMapping("/users/{id}/orders")
    public ResponseEntity<Set<Order>> getUserOrders(@PathVariable Long id) throws UserNotFoundException {
        Set<Order> orders = userService.getUserOrders(id);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
	
	// TODO - getUserOrder (@Mappings, URI=/users/{uid}/orders/{oid}, and method)
    @GetMapping("/users/{uid}/orders/{oid}")
    public ResponseEntity<Order> getUserOrder(@PathVariable Long uid, @PathVariable Long oid) throws UserNotFoundException, OrderNotFoundException {
        Order order = userService.getUserOrder(uid, oid);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

	// TODO - deleteUserOrder (@Mappings, URI, and method)
    @DeleteMapping("/users/{uid}/orders/{oid}")
    public ResponseEntity<Void> deleteUserOrder(@PathVariable Long uid, @PathVariable Long oid) throws UserNotFoundException, OrderNotFoundException {
        userService.deleteOrderForUser(uid, oid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
	
	// TODO - createUserOrder (@Mappings, URI, and method) + HATEOAS links
    @PostMapping("/users/{id}/orders")
    public ResponseEntity<Map<String,Object>> createUserOrder(
            @PathVariable Long id, @RequestBody Order order) throws UserNotFoundException, OrderNotFoundException {

        User user = userService.createOrdersForUser(id, order);
        Order created = user.getOrders().stream()
                .filter(o -> o.getId()!=null && o.getId().equals(order.getId())
                        || (o.getId()==null && o.getIsbn().equals(order.getIsbn())
                        && o.getExpiry().equals(order.getExpiry())))
                .findFirst().orElse(order);

        var self   = linkTo(methodOn(OrderController.class).getBorrowOrder(created.getId())).withSelfRel();
        var userL  = linkTo(methodOn(UserController.class).getUser(id)).withRel("user");
        var orders = linkTo(methodOn(UserController.class).getUserOrders(id)).withRel("userOrders");

        Map<String,Object> body = new LinkedHashMap<>();
        body.put("isbn", List.of(created.getIsbn()));
        body.put("links", List.of(self, userL, orders));

        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }



}
