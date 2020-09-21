# Restaurant voting system API

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

## Possible project improvements
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

##### create restaurant
curl -s -X POST -d '{"name":"New Restaurant"}' http://localhost:8080/voting/rest/admin/restaurants -H "Content-Type: application/json" --user admin@email.com:password

##### update restaurant
curl -s -X PUT -d '{"id":100016,"name":"New Restaurant"}' http://localhost:8080/voting/rest/admin/restaurants/100016 -H "Content-Type: application/json"  --user admin@email.com:password

##### delete restaurant
curl -s -X DELETE http://localhost:8080/voting/rest/admin/restaurants/100003 --user admin@email.com:password

##### get dish
curl -s http://localhost:8080/voting/rest/admin/dishes/100010 --user admin@email.com:password

##### create dish
curl -s -X POST -d '{"restaurant":{"id":100004,"name":"Random Sushi"}, "name":"New Dish", "price":199.00}' http://localhost:8080/voting/rest/admin/dishes -H "Content-Type: application/json" --user admin@email.com:password

#### update dish
curl -s -X PUT -d '{"id":100016,"name":"Updated Dish","restaurant":{"id":100004,"name":"Random Sushi"},"price":199.00,"date":"2020-09-19"}' http://localhost:8080/voting/rest/admin/dishes/100016 -H "Content-Type: application/json"  --user admin@email.com:password

##### delete dish
curl -s -X DELETE http://localhost:8080/voting/rest/admin/dishes/100010 --user admin@email.com:password

#### USER actions:
##### get all restaurants
curl -s http://localhost:8080/voting/rest/profile/votes/restaurants --user user1@email.com:password

##### get restaurant menu
curl -s http://localhost:8080/voting/rest/profile/votes/restaurants/100005/dishes --user user1@email.com:password

##### vote for restaurant
curl -s -X PUT -d '{"id":100003}' -H "Content-Type: application/json" http://localhost:8080/voting/rest/profile/votes --user user1@email.com:password