## To Try out Program:
- Try to access the Greetings endpoint, you should got a 401 unauthorized error code
- Send in your username/password as a body to the authenticate endpoint
- You should get a JWT
- Pass in the JWT token as a request param to the Greetings endpoint
  - key is authorization
  - value is bearer followed by a space and the jwt
- The greetings endpoint should work

## Exercise
Now that we have a fully configured Spring Security app, the next step is to make it more similar to a real-world setting. 

### This can be at least partically accomplished by not hard-coding in the username and password in JwtUserDetailsService. Change up the code so that the user can:
- Register with a new username and password
  - Store the username and hashed password in a db
- Authenticate themselves with the username and password
  - They should get a corresponding JWT token
- Access the Greetings endpoint only if they have the correct JWT Token and Username and Password


### Hints: 
```{toggle}
Change these files: JwtUserDetailsService.java, SecurityConfig.java, JTWAuthenticationController
Add new files: models/MyUser.java, repository/MyUserRepository.java

Note that these hints are specific to one solution to this exercise. If your solution does not match this, it
is not necessarily an incorrect approach. 

Click the next hint to see what exactly we change/add:
```