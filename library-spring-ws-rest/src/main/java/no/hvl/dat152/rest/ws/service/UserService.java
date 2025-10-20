/**
 * 
 */
package no.hvl.dat152.rest.ws.service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import jakarta.transaction.Transactional;
import no.hvl.dat152.rest.ws.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.model.User;
import no.hvl.dat152.rest.ws.repository.UserRepository;

/**
 * @author tdoy
 */
@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;


    public List<User> findAllUsers(){
		
		List<User> allUsers = (List<User>) userRepository.findAll();
		
		return allUsers;
	}
	
	public User findUser(Long userid) throws UserNotFoundException {
		
		User user = userRepository.findById(userid)
				.orElseThrow(()-> new UserNotFoundException("User with id: "+userid+" not found"));
		
		return user;
	}
	
	
	// TODO public User saveUser(User user)
    public User saveUser(User user) {
        return userRepository.save(user);
    }
	
	// TODO public void deleteUser(Long id) throws UserNotFoundException
    public void deleteUser(Long id) throws UserNotFoundException {
        userRepository.findById(id).ifPresent(userRepository::delete);
    }

    // TODO public User updateUser(User user, Long id)
    public User updateUser(User user, Long id) throws UserNotFoundException {
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setFirstname(user.getFirstname());
            existingUser.setLastname(user.getLastname());
            existingUser.setOrders(user.getOrders());
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new UserNotFoundException("User with id: "+id+" not found"));
    }
	
	// TODO public Set<Order> getUserOrders(Long userid)
    public Set<Order> getUserOrders(Long userid) throws UserNotFoundException {
        userRepository.findById(userid).orElseThrow(() -> new UserNotFoundException("User with id: "+userid+" not found"));
        return userRepository.findById(userid).get().getOrders();
    }

    // TODO public Order getUserOrder(Long userid, Long oid)
    public Order getUserOrder(Long userid, Long oid) throws UserNotFoundException, OrderNotFoundException {
        User user = userRepository.findById(userid).orElseThrow(()->new UserNotFoundException("User with id: " + userid + " not found"));
        return user.getOrders().stream().filter(filteredorder -> filteredorder.getId().equals(oid)).
                findFirst().orElseThrow(()-> new OrderNotFoundException("Order with id: "+oid+" not found"));
    }
	
	// TODO public void deleteOrderForUser(Long userid, Long oid)
    @Transactional
    public void deleteOrderForUser(Long userid, Long oid) throws UserNotFoundException, OrderNotFoundException {
        User user = userRepository.findById(userid).orElseThrow(()->new UserNotFoundException("User with id: " + userid + " not found"));
        boolean removed = user.getOrders().removeIf(filteredorder -> filteredorder.getId().equals(oid));
        if (!removed) {
            throw new OrderNotFoundException("Order with id: " + oid + " not found");
        }
    }
	
	// TODO public User createOrdersForUser(Long userid, Order order)
    public User createOrdersForUser(Long userid, Order order) throws UserNotFoundException {
        User user = userRepository.findById(userid).orElseThrow(()->new UserNotFoundException("User with id: " + userid + " not found"));
        user.getOrders().add(order); //orphan removal goes both ways
        return userRepository.save(user);
    }
}
