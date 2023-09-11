# DigiBanking

This application is developed using Maven, Spring Boot, and the H2 database. Hence, to use the application, 
1. Run the following maven command from the project root directory:

    mvn spring-boot:run

3. Once started, the application will be running on port 8080
4. Install latest version of Postman and Import DigiBank.postman_collection
5. Then run all services as mention in postman
6. Register Service:
     End Point: http://localhost:8080/register(Post Request)
     Sample Request:
                   {
            "username":"shubha",
            "fullName":"Kumari",
            "mobileNumber":"3245451",
            "dateOfBirth":"15/05/1987",
            "country":"NL"
            }
   Sample Response:
      {
    "username": "shubha",
    "password": "3DenTCyU"
}

7.Using username and password logon 
   End Point:http://localhost:8080/logon(Post Request)
    Sample Request:
                   {
                "username": "shubha",
                "password": "3DenTCyU"
            }
    Sample Response:
      {
    "accessToken": "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJzZWxmIiwic3ViIjoic2h1YmhhIiwiZXhwIjoxNjk0NDE1Mjg1LCJpYXQiOjE2OTQzNzkyODUsInNjb3BlIjoiQ1VTVE9NRVIifQ.Ft_jjVbWv_KJAVWgcUA6xVNtiYrYMZxV2zMFIqxVn-ZrXKfld16EeXPdVnngT1oYtCCLy2ckt50342BUjK8Izd2jmZykJQs89AH4novGwNtHA6OImrziPw10jRSs9zV3jc2JkVkDAOTn4XcNHFNV2Ldr4FEUe6DHNDYgy8cILD1QJ3Q_ABIzHo_mzh5NlhgqzjvLKylsHr75xvOXrOskDUMI9ONsPun9AaAKnKg0vs6j6JT0MJF3GJMhcRzvtZ1OEkLz2xdYDkeBFKBolQfudRCkpZP3dgDQr3QJv7Gv6pdQ0WD447KnshReDY8ebiqyqPEMCGq9x9LvYHc7lnNQFg"
}

8.Using above token initiate transfer and also can get overview of user with token

9. Initiate transfer by using above toekn in Authrorization header with bearer schema
    End point:http://localhost:8080/transfer(Post Request)
    Sample Request:
      {
"originatorAccountNumber":"NL21ABNA6684106181",
"counterpartyAccountNumber":"NL61ABNA5400702561",
"amount":10
}
  Sample Response:
   Transfer successful.
   
11. Get Overview of account(Get Request)
   End Point:http://localhost:8080/overview
   {
    "accountNumber": "NL61ABNA5400702561",
    "accountType": "SAVINGS",
    "balance": 10010.00,
    "currency": "EUR"
}

12. Descoped document id functionality
    
13.Assuming mobile number provided is correct one which is used previously

     

