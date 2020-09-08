#Restaurant voting system API

2 types of users: admin and regular users

ADMIN action list:
- save/delete restaurant
- watch all restaurants
- save/delete dish
- watch menu for the restaurant

USER action list:
- watch menu for each restaurant
- vote on which restaurant they want to have lunch at (one vote per USER)
- change vote the same day if it is before 11:00

##Possible project improvements
- implement data transfer object
- validation
- hibernate second level cache
- refactoring  

### curl samples (application deployed at application context `voting`)
##### get all Restaurants
curl -s http://localhost:8080/voting/rest/admin/restaurants --user admin@email.com:password

##### get all Dishes for Restaurant
curl -s http://localhost:8080/voting/rest/admin/dishes?id=100004 --user admin@email.com:password

#### ADMIN actions:
##### get restaurant
curl -s http://localhost:8080/voting/rest/admin/restaurants/100003 --user admin@email.com:password

##### save restaurant
curl -s -X POST http://localhost:8080/voting/rest/admin/restaurants -H "Content-Type: application/json" -d '{"name":"New Restaurant"}' --user admin@email.com:password

##### delete restaurant
curl -s -X DELETE http://localhost:8080/voting/rest/admin/restaurants/100003 --user admin@email.com:password

##### get dish
curl -s http://localhost:8080/voting/rest/admin/dishes/100010 --user admin@email.com:password

##### save dish
curl -s -X POST -d '{"restaurant":{"id":100004,"name":"Random Sushi"}, "name":"New Dish", "price":199.00}' -H "Content-Type: application/json" http://localhost:8080/voting/rest/admin/dishes --user admin@email.com:password

##### delete dish
curl -s -X DELETE http://localhost:8080/voting/rest/admin/dishes/100010 --user admin@email.com:password

#### USER actions:
##### get All Restaurants
curl -s http://localhost:8080/voting/rest/profile/restaurants --user user1@email.com:password

##### vote for restaurant
curl -s -X POST -d '{"id":100003}' -H "Content-Type: application/json" http://localhost:8080/voting/rest/profile/restaurants --user user1@email.com:password