### Implementation details
- This project demonstrates the implmentation of api rate limiting in spring boot.
- For the aforementioned implementation, "bucket4j" package is used to rate limit the apis using the "token-bucket" algorithm.
- The rate limiting logic is applicable on a per user, per api basis the details of which are defined using appropriate enums for the purpose of this project.
- In a production environment, these details should be stored inside a database(for e.g an SQL database) which will contain the details of all the mappings of users, apis and the corresponding limits
- The test cases have been defined inside the test folder.
- The tests include the cases where no api key is provided or an invalid api key is included.

### Assumptions made
- For this project, we have assumed that each user is provided with a unique "api key" which corresponds to the details of limits applicable for that user - the details of which can be seen defined in the "RateLimitModel" enum.
- So for e.g, for the "developers" api, the api key available to one of the user is of the format "USER1_DEV".
- That api key is passed in the headers of the request which is then intercepted by the "RateLimitInterceptor" (autowired inside the main class), and based on the limits set, appropriate response along with status code is sent as response.
- As mentioned earlier the enums have been defined stating the various limits in this project, but in a production environment, the interceptor can get those details from a database.

### Scope for improvement
- In this implementation we are storing the token buckets in a "concurrent hashmap" defined inside the "RateLimitService". 
- In a distributed computing environment, we should ideally store the tokens in a cache.
- This can be achieved by implementing appropriate cache libraries such as redis/jcache with their corresponding bucket4j integrations.