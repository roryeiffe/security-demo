## Spring Security

### Definitions

- Authorization - the process of determining a user's level of access
- Authentication - the process of verifying that someone is who they say they are

### JWT
- JWT stands for JSON Web Tokens
- "JWTs are an open ... method for representing claims securely between two parties"
- Good resource: https://jwt.io/
- A JWT has 3 parts
    - Header - keeps track of things like algorithm
    - Payload - the data (username, expiration, initialization time)
    - Verify Signature - used to detect if JWT has been tampered with

### How Does JWT Work?
- A Client sends a POST request to a server
  - Login, etc.
- The server creates a JWT for the user using a secret key
- The server sends this JWT back to the client
- The client can then send another request, this time including the JWT in the request
- The server verifies the JWT signature with the secret key and then gets the user from the JWT
- Finally the server can send the response

### How does the server verify the JWT?
- Server does not store the JWT, or any session data
- It uses hashing, which is an irreversible operation, to verify that the header and payload match the verify signature
- If a malicious entity tries to change the JWT, the server will know, because the signature won't match


## Running/Testing the program:
1. Run the main program (SecurityDemoApplication.java)
2. On Postman, send a POST request to localhost:8080/authenticate. In the body, include the username and password.
   1. You shold get a JWT as a response
3. Attempt to access the greetings endpoint by sending a GET request to localhost:8080/api/v1/greetings
   1. You should get a 401 error code
4. Include the JWT as a header in the your GET request
   1. Tag should be "Authorization"
   2. Value should be "Bearer ${token}"
   3. Send the request, you should get the Hello message