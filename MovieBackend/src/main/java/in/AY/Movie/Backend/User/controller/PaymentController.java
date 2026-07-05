package in.AY.Movie.Backend.User.controller;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "http://localhost:5173")
public class PaymentController 
{
	private static final String KEY_ID     = "rzp_test_T9neVKaIyORw7F"; 
    private static final String KEY_SECRET = "z7T0HTSJ217gkaBd2juEhidV";
    
    // ── Step 1: Frontend calls this to get a Razorpay order ID ─────────────
    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> data)
    {
    	try 
    	{
			RazorpayClient razorpay = new RazorpayClient(KEY_ID, KEY_SECRET);
			
			JSONObject orderRequest = new JSONObject();
			orderRequest.put("amount", data.get("amount"));
			orderRequest.put("currency", "INR");
			orderRequest.put("receipt", "receipt_"+System.currentTimeMillis());
			
			Order order = razorpay.orders.create(orderRequest);
			
			Map<String, Object> response = new HashMap();
            response.put("orderId",  order.get("id"));
            response.put("amount",   order.get("amount"));
            response.put("currency", order.get("currency"));
            response.put("keyId",    KEY_ID);   // frontend needs this to open the modal

            return ResponseEntity.ok(response);
		} 
    	catch (RazorpayException e) {
    		System.err.println("Razorpay error: " + e.getMessage()); // ← add this
    		return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    // ── Step 2: After payment, frontend sends signatures here to verify ─────
    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody Map<String, String> data)
    {
    	try 
    	{
    		JSONObject options = new JSONObject();
            options.put("razorpay_order_id",   data.get("razorpay_order_id"));
            options.put("razorpay_payment_id", data.get("razorpay_payment_id"));
            options.put("razorpay_signature",  data.get("razorpay_signature"));
            
            boolean valid = Utils.verifyPaymentSignature(options, KEY_SECRET);
            
            if(valid)
            {
            	return ResponseEntity.ok(Map.of(
            		"status", "success",
            		"paymentId", data.get("razorpay_payment_id")
            	));
            }
            
            else
            {
            	return ResponseEntity.badRequest().body(Map.of("status", "failed"));
            }
		} 
    	catch (RazorpayException e) 
    	{
    		System.err.println("Razorpay error: " + e.getMessage()); // ← add this
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
