package com.mochijump.leveleditor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mochijump.leveleditor.Level;
import com.mochijump.leveleditor.LevelRepository;

@Controller    
@RequestMapping(path="/test") 
public class MainController {
	private ObjectMapper mapper = new ObjectMapper();
	
	
	@Autowired
	private LevelRepository levelRepository;
	
	
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping("")
	public String drawPageLaunch() {
		return"draw.html";
	}
	
	@GetMapping(path = "/version")
	public @ResponseBody String getVersion(){
			return "0.2.5";
	}
	
	@RequestMapping(path = "/user")
	  public @ResponseBody NewUserTemplate user(Principal user) {
	    User uFinder = userRepository.findByUserName(user.getName());
	    NewUserTemplate userInfo = new NewUserTemplate();
	    userInfo.setUserName(uFinder.getUserName());
	    userInfo.setUserFirstName(uFinder.getUserFirstName());
	    userInfo.setEmailAddress(uFinder.getEmailAddress());
	    userInfo.setPassword("Hidden");
	    return userInfo;
	  }
	
	@RequestMapping(path="/changePassword")
	public @ResponseBody String changePassword (@RequestBody ChangePasswordTemplate template) {
		// not finished
		
		return "something";
	}
	
	@GetMapping (path="/getDownloadLink")
	// this hides the download url on the server
	public @ResponseBody Map<String,String> getDownloadLink() {
		HashMap<String, String> map = new HashMap<>();
		map.put("javaURL", 
				"https://github.com/AndoryuRenoa/MochiJump/blob/master/dist/MochiJump.jar?raw=true");
		map.put("exeURL",
				"https://github.com/AndoryuRenoa/MochiJump/blob/master/dist/MochiJump.exe?raw=true");
		return map;
	}
	
	
	@GetMapping(path="/returnAll")
	public @ResponseBody Iterable <Level> getAllUserInputs(){
		return levelRepository.findAll();
	}
	
	
	// this obviously will need to be changed later
	@GetMapping(path="/returnAllUsers")
	public @ResponseBody Iterable <User> getListOfAllUsers(){
		return userRepository.getAllExceptPassword();
	}
	
	@RequestMapping(path="/addUser")
	public @ResponseBody String addUser(@RequestParam String firstName, @RequestParam String userName,
			@RequestParam String emailAddress, @RequestParam String password){
		User newUser = new User ();
		@SuppressWarnings("unused")
		User nameTaken = null;
		try {
			nameTaken = userRepository.findByUserName(userName);
			return "Name Taken";
		}catch (Exception e) {
			
		}
		newUser.setUserFirstName(firstName);
		newUser.setUserName(userName);
		newUser.setEmailAddress(emailAddress);
		newUser.setPassword(password);
		userRepository.save(newUser);
		return "User added";
	}
	
	
	@GetMapping(path="/return")
	public @ResponseBody Iterable <Level> getLevel (@RequestParam String name) {
		return levelRepository.findByLevelName(name);
	}
	
	@GetMapping(path="/emailTest")
	public @ResponseBody String sendEmail() {
		String output;
		String output2="";
		try {
			
			URL url = new URL("http://mochijumpemailer-env.evyk8k3wmq.us-east-2.elasticbeanstalk.com/email/test");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP Error code : "
                        + conn.getResponseCode());
            }
			InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);
            while ((output = br.readLine()) != null) {
                System.out.println(output);
                output2 = output;
            }
            return output2;
		}
		catch (Exception e) {
			return "error" + e;
		}	
	}
	

	
	@PostMapping(path="/json")	
	public @ResponseBody void recieverTest(@RequestBody String s){
		try {
		Level i = mapper.readValue(s, Level.class);
		Level exists = null;
		try {
			exists = levelRepository.findByLevelName(i.getLevelName()).get(0);
		}catch (Exception e) {
			//this Exception will be thrown every time a new level is made
		}
		if (exists != null){
			i.setId(exists.getId());
		}
		levelRepository.save(i);
		} catch (JsonMappingException e) {
		    e.printStackTrace();
		} catch (JsonGenerationException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
	
	
	@PostMapping(path="/message")
	public @ResponseBody String sendRestMessage (@RequestBody Message s) {
		HttpHeaders headers = new HttpHeaders();
		RestTemplate rest = new RestTemplate();
		@SuppressWarnings("unused")
		HttpStatus status;
		HttpEntity <String> requestEntity = new HttpEntity (s, headers);
		ResponseEntity<String> responseEntity = rest.exchange("http://mochijumpemailer-env.evyk8k3wmq.us-east-2.elasticbeanstalk.com/email/message", 
				HttpMethod.POST, requestEntity, String.class);
		status = responseEntity.getStatusCode();
		return responseEntity.getBody();
	}
	
	@PostMapping(path="/userLevelList")
	public @ResponseBody String userLevelList() {
		//This will return a list, need to update entities/repositories to do this.
		return "hi";
	}
	
	
	@PostMapping (path="/newUserCreation")
	public @ResponseBody String makeNewUser (@RequestBody NewUserTemplate newUserT) {
		Random rand = new Random();
		User newUser = new User();
		User nameTaken = null;
		
		//The below tests if the name is already taken. Please note if there is more than
		//one username in the database this will not work as nameTaken will be assigned null
		//because there is no unique answer to the query
		try {
			nameTaken = userRepository.findByUserName(newUserT.getUserName());
		}catch (Exception e) {
			// to be expected
		}
		if (nameTaken != null) {
			return "Name Taken";
		}
		newUser.setEmailAddress(newUserT.getEmailAddress());
		newUser.setIsAccountNonLocked(false);
		newUser.setKeyNum(rand.nextLong());
		newUser.setPassword(newUserT.getPassword());
		newUser.setUserName(newUserT.getUserName());
		newUser.setUserFirstName(newUserT.getUserFirstName());
		userRepository.save(newUser);
		
		Message message = new Message();
		message.setEmail(newUser.getEmailAddress());
		message.setSubject("Please Activate your new account");
		message.setMessageBody("Please click the following link to activate your new account :"+
		"mochijump.com/test/activate?username="+newUser.getUserName()+"&userKey="+newUser.getKeyNum());
		
		HttpHeaders headers = new HttpHeaders();
		RestTemplate rest = new RestTemplate();
		@SuppressWarnings("unused")
		HttpStatus status;
		HttpEntity <String> requestEntity = new HttpEntity (message, headers);
		ResponseEntity<String> responseEntity = rest.exchange("http://mochijumpemailer-env.evyk8k3wmq.us-east-2.elasticbeanstalk.com/email/activateNewAccount", 
				HttpMethod.POST, requestEntity, String.class);
		status = responseEntity.getStatusCode();
		
		
		
		return responseEntity.getBody() + newUser.getIsAccountNonLocked();
		
		
	}
	
	@RequestMapping (path="/activate")
	public @ResponseBody String activateUser (@RequestParam String username, @RequestParam  long userKey) {
		User activateMe = userRepository.findByUserName(username);
		if (userKey == activateMe.getKeyNum()) {
			activateMe.setIsAccountNonLocked(true);
			userRepository.save(activateMe);
			return "success";
		} else {
			return "failure";
		}
	}
	
}
