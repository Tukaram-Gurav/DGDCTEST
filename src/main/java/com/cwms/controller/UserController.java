	package com.cwms.controller;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cwms.entities.Branch;
import com.cwms.entities.Company;
import com.cwms.entities.UpdateUserRights;
import com.cwms.entities.User;
import com.cwms.entities.UserRights;
import com.cwms.repository.BranchRepo;
import com.cwms.repository.CompanyRepo;
import com.cwms.repository.UserRepository;
import com.cwms.repository.UserRightsrepo;
import com.cwms.service.EmailService;
import com.cwms.service.MenuServiceImpl;
import com.cwms.service.UserRightsService;

import jakarta.transaction.Transactional;


@CrossOrigin("*")
@RestController
@RequestMapping("/user")
@ComponentScan("com.repo.all")
public class UserController {
	

	@Autowired
	public EmailService EmailService;
	
	@Autowired
	private BCryptPasswordEncoder passwordencode;
	
	@Value("${spring.from.mail}")
    private String from;
	
	
	private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
	
	 private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private CompanyRepo crepo;

    @Autowired
    private BranchRepo brepo;

    @Autowired
    private UserRepository urepo;

    @Autowired
    private UserRightsrepo urights;

    @Autowired
    private UserRightsService uservice;

    @Autowired
    private MenuServiceImpl menuservice;

 

    @GetMapping("/company")
    public List<Company> getCompanies(@RequestHeader("React-Page-Name") String reactPageName) {
    	MDC.put("reactPageName", reactPageName);
    	
    	 LocalDate currentDate = LocalDate.now();

         // Define the desired date format
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

         // Format the current date using the formatter
         String formattedDate = currentDate.format(formatter);
         System.setProperty("formattedDate", formattedDate);
         MDC.put("formattedDate", formattedDate);
         System.out.println(formattedDate);
    
    	System.out.println(reactPageName);
        List<Company> companies = this.crepo.findAll();
       
        return companies;
    }

    @GetMapping("/branch")
    public List<Branch> getBranchesByCompanyId(@RequestHeader("React-Page-Name") String reactPageName) {
    	MDC.put("reactPageName", reactPageName);
    	System.out.println(reactPageName);
    	 try{
        List<Branch> branches = brepo.findAll();
      
        return branches;
    	 }
    	 finally {
			MDC.remove("controller");
		}
    }
    
    @GetMapping("/get-User/{id}/{cid}/{bid}")
    public List<UserRights> getUserrights(@PathVariable("id") String id,@PathVariable("cid") String cid,@PathVariable("bid") String bid){
		return urights.findByUserId(id,cid,bid);
    	
    }

    @GetMapping("/getuser/{id}/{cid}/{bid}")
    public List<UserRights> getUserRightsbyId(@PathVariable("id") String id,@PathVariable("cid") String cid,@PathVariable("bid") String bid) {
    	
    	  List<UserRights> getuserrights = urights.findByUserId(id,cid,bid);
          
          // Format and log the SQL query with the parameter value
          String sqlQuery = "SELECT DISTINCT u.* FROM userinfo u LEFT JOIN userrights ur ON u.branch_id = ur.branch_id LEFT JOIN pmenu p ON ur.process_id = p.process_id LEFT JOIN cmenu c ON p.process_id = c.pprocess_id WHERE u.branch_id = '%s' AND role = 'ROLE_USER'";
          String formattedQuery = String.format(sqlQuery, id);
       
          System.out.println(getuserrights);
          return getuserrights;
    }

    @GetMapping("/mybranch/{id}")
    public List<UserRights> getUserByBranchId(@PathVariable("id") String id,@RequestHeader("React-Page-Name") String reactPageName) {
    	 MDC.put("reactPageName", reactPageName);
        List<UserRights> userrights = this.urights.findBybid(id);
      
        return userrights;
    }

    @GetMapping("ubranch/{id}/{cid}/{bid}")
    public ResponseEntity<List<User>> getUserByBranchId2(@PathVariable("id") String id,@RequestHeader("React-Page-Name") String reactPageName,@PathVariable("cid") String cid,@PathVariable("bid") String bid) {
    	 MDC.put("reactPageName", reactPageName);
        List<User> users = (List<User>) uservice.getUser(id,cid,bid);

        if (users != null && !users.isEmpty()) {
          
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/u/{id}/{cid}/{bid}")
    public List<User> getUsersWithUserRightsByBranchId(@PathVariable("id") String id,@PathVariable("cid") String cid,@PathVariable("bid") String bid) {
    	
        List<User> user = this.urepo.findbyuid(id,cid,bid);
        
        return user;
    }
    @GetMapping("/{compId}/{bid}/{userId}/findByUserId")
    public User findByUserId(@PathVariable("compId") String cid,@PathVariable("bid") String bid,@PathVariable("userId") String userId) {
    	return urepo.findByUser_IdandbranchAndCompany(userId,bid,cid);
    }
    
    
    @GetMapping("/{cid}/branch")
    public List<Branch> getBranchesOfCompany(@PathVariable("cid") String companyId ,@RequestHeader("React-Page-Name") String reactPageName) {
    	MDC.put("reactPageName", reactPageName);
    	
    	 LocalDate currentDate = LocalDate.now();
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");


         String formattedDate = currentDate.format(formatter);

         MDC.put("formattedDate", formattedDate);

    	 List<Branch> branches = brepo.findByCompanyId(companyId);
       
        return branches;
    }
    
    
    @PutMapping("/update-rights")
    @Transactional
    public void updateuserrights(@RequestBody UpdateUserRights request,@RequestHeader("React-Page-Name") String reactPageName) {
    	 MDC.put("reactPageName", reactPageName);
        uservice.updateUserrights(
            request.getApprove(), 
            request.getCreate(), 
            request.getDeleteRight(),
            request.getRead(), 
            request.getUpdate(), 
            request.getUserId(), 
            request.getProcessId()
        );
        
    }

    @PutMapping("/update-rights2")
    @Transactional
    public void updateuserrights2(@RequestBody List<UpdateUserRights> requests,@RequestHeader("React-Page-Name") String reactPageName) {
    	 MDC.put("reactPageName", reactPageName);
        for (UpdateUserRights request : requests) {
            uservice.updateUserrights(
                request.getApprove(), 
                request.getCreate(), 
                request.getDeleteRight(),
                request.getRead(), 
                request.getUpdate(), 
                request.getUserId(), 
                request.getProcessId()
            );
         
        }
    }

    @GetMapping("/get-user/{id}/{cid}/{bid}")
    public User getUserByUserId(@PathVariable("id") String id,@PathVariable("cid") String cid,@PathVariable("bid") String bid) {
    	 
        User user = urepo.findByUserId(id,cid,bid);
 
        return user;
    }

    @PostMapping("/addRights")
    public List<UserRights> insertByUid(@RequestBody List<UserRights> userRightsList,@RequestHeader("React-Page-Name") String reactPageName) {
    	 MDC.put("reactPageName", reactPageName);
        List<UserRights> userrights = urights.saveAll(userRightsList);

        return userrights;
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<String> updateUserRights(@RequestBody List<UserRights> userRights, @PathVariable("userId") String userId) {
        try {
            uservice.updateUserRights(userRights, userId);
            return ResponseEntity.ok("User rights updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating user rights!");
        }
    }

    @PostMapping("/insert/{userId}")
    public ResponseEntity<String> insertUserRights(@RequestBody List<UserRights> userRights, @PathVariable("userId") String userId) {
        try {
            uservice.insertUserRights(userRights, userId);
            return ResponseEntity.ok("User rights inserted successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inserting user rights!");
        }
    }

    @GetMapping("/getmenu/{id}")
    public List<UserRights> getmenudatafromuserrights(@PathVariable("id") String id,@RequestHeader("React-Page-Name") String reactPageName) {
    	 MDC.put("reactPageName", reactPageName);
        List<UserRights> user = this.urights.getMenuDataUsingUserRihts(id);
  
        return user;
    }

    @GetMapping("/getallmenu/{userId}/{cid}/{bid}")
    public List<UserRights> getUserRights(@PathVariable String userId,@PathVariable("cid") String cid,@PathVariable("bid") String bid) {
 
        List<UserRights> user = this.menuservice.getUserRightsByUserId(userId,cid,bid);

        return user;
    }

    @GetMapping("/combined-data/{id}")
    public ResponseEntity<List<Map<String, Object>>> getAllCombinedData(@PathVariable("id") String id) {
    	
        List<Map<String, Object>> combinedData = menuservice.getAllParentAndChildMenus(id);
  
        return ResponseEntity.ok(combinedData);
    }

    @GetMapping("/status/{id}")
    public UserRights getStatus(@PathVariable("id") String id) {
    	return urights.getStatus(id);
    }
    
    @GetMapping("/UpdatePassword")
    public boolean sendOtp(
        @RequestParam(name = "companyId", required = false) String companyId,
        @RequestParam(name = "branchId", required = false) String branchId,
        @RequestParam(name = "userId", required = false) String userId,
        @RequestParam(name = "password", required = false) String password
    ) {
        try {
            User userToUpdate = urepo.findByUser_IdandbranchAndCompany(userId, branchId, companyId);
            if (userToUpdate != null) {
                userToUpdate.setUser_Password(passwordencode.encode(password));
                urepo.save(userToUpdate);
                
                if (userToUpdate.getUser_Email()!= null && !userToUpdate.getUser_Email().isEmpty())
    	        {
                	
                	
                	String subject = "Your OTP for Password Change (User ID: " + userId + ")";

                	String emailMessage = "Dear Sir/Madam,<br/><br/>Your password for User ID: <strong>" + userId + "</strong> has been successfully changed.<br/><br/>	Your New Password is <strong>"+ password +" </strong><br/><br/>If you did not make this change, please contact us immediately.";

                	
    	        	executorService.schedule(() -> {
    	        		EmailService.sendEmailWithHtmlContent(subject,emailMessage,userToUpdate.getUser_Email(),from,"Tukaram8805@gmail.com");
    		        	}, 2, TimeUnit.SECONDS);
    	        }
                
                userToUpdate.setPasswordChangeOTP("");
                urepo.save(userToUpdate);
                return true;	
            } else {               
                return false;
            }
        } catch (Exception e) {
           
            e.printStackTrace(); 
            return false; 
        }
    }

    @GetMapping("/checkOtpForPasswordChange")
    public boolean checkOtpForPasswordChange(
        @RequestParam(name = "companyId", required = false) String companyId,
        @RequestParam(name = "branchId", required = false) String branchId,
        @RequestParam(name = "userId", required = false) String userId,
        @RequestParam(name = "otp", required = false) String otp
    ) {
        try {
            User userToUpdate = urepo.findByUser_IdandbranchAndCompany(userId, branchId, companyId);
            if (userToUpdate.getPasswordChangeOTP().equals(otp.trim())) {       	      	
                return true;	
            } else {               
                return false;
            }
        } catch (Exception e) {
           
            e.printStackTrace(); 
            return false; 
        }
    }
    
    
    @GetMapping("/changePassword")
    public String changePassword(@RequestParam("cid") String cid,@RequestParam("bid") String bid,@RequestParam("user") String userid,@RequestParam("old") String oldpassword,@RequestParam("new") String newpassword,@RequestParam("confirm") String confirmpassword)   {
    	User user = urepo.findByUser_IdandbranchAndCompany(userid, bid, cid);
    	
    	if(user != null) {
    		if(passwordencode.matches(oldpassword, user.getUser_Password())) {
    			if(passwordencode.matches(newpassword, user.getUser_Password())) {
    				return "do not enter same old password";
    			}
    			else {
    				if(newpassword.equals(confirmpassword)) {
        				String pass = passwordencode.encode(confirmpassword);
        				user.setUser_Password(pass);
        				urepo.save(user);
        				return "success";
        			}
        			else {
        				return "wrong confirm password";
        			}
    			}
    		}
    		else {
    			return "wrong old password";
    		}
    	}
    	else {
    		return "user not found";
    	}
    }
    
    
    
}
