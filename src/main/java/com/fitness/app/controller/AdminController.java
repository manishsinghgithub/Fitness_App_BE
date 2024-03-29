package com.fitness.app.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.fitness.app.auth.Authenticate;
import com.fitness.app.config.JwtUtils;
import com.fitness.app.entity.AdminPay;
import com.fitness.app.entity.GymClass;
import com.fitness.app.entity.UserClass;
import com.fitness.app.model.SignUpResponce;
import com.fitness.app.repository.AddGymRepository;
import com.fitness.app.repository.UserRepository;
import com.fitness.app.repository.VendorRepository;
import com.fitness.app.security.service.UserDetailsServiceImpl;
import com.fitness.app.service.AdminService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
@RestController
public class AdminController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private VendorRepository vendorRepo;

	@Autowired
	private AddGymRepository gymRepo;

	@Autowired
	private AdminService adminService;

	// log in user....with custom sign option.
	@PostMapping("/login/admin")
	public ResponseEntity<?> authenticateUser(@RequestBody Authenticate authCredential) throws Exception {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authCredential.getEmail(), authCredential.getPassword()));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new Exception("Error");
		}
		final UserDetails usrDetails = userDetailsService.loadUserByUsername(authCredential.getEmail());
		final String jwt = jwtUtils.generateToken(usrDetails);
		final UserClass localUser = userRepo.findByEmail(authCredential.getEmail());
		if (localUser.getRole().equals("ADMIN")) {
			return ResponseEntity.ok(new SignUpResponce(localUser, jwt));
		} else {
			return ResponseEntity.ok(new SignUpResponce(null, null));
		}

	}


	//finding all user details....
	@GetMapping("/get-all-users")
	public List<UserClass> getAllUsers() {
		List<UserClass> l = userRepo.findAll();
		l = l.stream().filter(e -> e.getRole().equals("USER")).collect(Collectors.toList());
		return l;
	}

	//finding all vendors from the database.
	@GetMapping("/get-all-vendors")
	public List<UserClass> getAllVendors() {
		List<UserClass> l = userRepo.findAll();
		l = l.stream().filter(e -> e.getRole().equals("VENDOR")).collect(Collectors.toList());
		return l;
	}


	//finding all fitness centers in the list.
	@GetMapping("/get-all-gyms")
	public List<GymClass> getAllGyms() {
		List<GymClass> l = gymRepo.findAll();
		return l;
	}


	//finding list of registered fitness center by email id of vendor.
	@GetMapping("/get-all-gyms-by-email/{email}")
	public List<GymClass> getAllGymsByEmail(@PathVariable String email) {
		List<GymClass> l = gymRepo.findByEmail(email);
		return l;
	}


	//finding total amount to pay to the vendor.
	@GetMapping("/vendor-payment/{vendor}")
	public AdminPay vendorPayment(@PathVariable String vendor) {
		return adminService.vendorPayment(vendor);
	}

	//demo api for payment.
	@GetMapping("/get-data-pay")
	public AdminPay getDatapay(@RequestBody AdminPay pay)
	{
		return adminService.getDataPay(pay);
	}



	//Initiating payment to the vendor
	@PutMapping("/pay-vendor-now")
	@ResponseBody
	public String payNow(@RequestBody AdminPay payment) throws Exception {
		RazorpayClient razorpayClient = new RazorpayClient("rzp_test_vmHcJh5Dj4v5EB", "SGff6EaJ7l3RzR47hnE4dYJz");

		JSONObject ob = new JSONObject();
		ob.put("amount", payment.getAmount() * 100);
		ob.put("currency", "INR");
		ob.put("receipt", "txn_201456");

		Order myOrder = razorpayClient.Orders.create(ob);

		boolean flag= adminService.PayNow(payment, myOrder);
        if(flag) {
		return myOrder.toString();
		}
        else
        {
        	return null;
        }
	}


	//updating status of payment maid to the vendor by admin.
	@PutMapping("/update-vendor-payment")
	public AdminPay updatingOrder(@RequestBody Map<String, String> data) {

		return adminService.updatePayment(data);
	}


	//Finding payment history of the vendor.
	@GetMapping("/paid-history/{vendor}")
	public List<AdminPay> paidHistroy(@PathVariable String vendor) throws Exception {
		return adminService.paidHistroyVendor(vendor);
	}


	//finding total registered vendors, fitness center and fitness enthusiast.
	@GetMapping("/all-numbers")
	public ResponseEntity<?> getAllNumber() {
		List<UserClass> l = userRepo.findAll();
		int u = l.stream().filter(e -> e.getRole().equals("USER")).collect(Collectors.toList()).size();
		int v = l.stream().filter(e -> e.getRole().equals("VENDOR")).collect(Collectors.toList()).size();
		List<GymClass> gyms = gymRepo.findAll();
		int g = gyms.size();

		List<String> nums = new ArrayList<>();
		nums.add(Integer.toString(u));
		nums.add(Integer.toString(v));
		nums.add(Integer.toString(g));

		return new ResponseEntity<List<String>>(nums, HttpStatus.OK);
	}


}