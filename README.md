<h3 align="center"><img src="https://user-images.githubusercontent.com/33310255/217078166-67f6e7b7-be4b-4828-9e35-b3a1e115f5e3.png" width="400px"></h3>

---

# What's Sharevax?
Sharevax is a web application that empowers the global health by ensuring the efficient distribution of vaccines with its smart algorithms.
Pandemic hasn’t gone yet, and we noticed one piece of CDC news about vaccine shortages & delays, because countries are seeking for vaccines just for themselves, they are trading with each other by themselves, and trade between countries is not efficient at global scale. That’s where Sharevax sits. ‘Sharevax is … ‘ We will show you shortly how countries can make use of Sharevax to request or supply vaccines from others, make agreement easily, and get what they want as they request.

# Features
  1. Provide Suggestions to Demander/Supplier through Matching Algorithm
  2. Mutual Approval between Supplier and Demander 
  3. Deliver Vaccines with the most Appropriate Route which is computed by the Dijkstra's algorithm
  4. React to Unexpected Events such as harbor blockage
  
# Overview
## Home Page
![Overview - Home Page](https://user-images.githubusercontent.com/33310255/217080752-74157b5b-559f-4b18-93c9-ff87d8bc1da2.png)
## Delivery
![iShot_2023-02-06_21 36 35](https://user-images.githubusercontent.com/33310255/217080915-77bb13df-ff1e-4cd8-9110-5d8def7e7896.jpg)
## Supply & Demand
![iShot_2023-02-06_21 37 22](https://user-images.githubusercontent.com/33310255/217080851-f19e1d3e-2618-4413-a58b-75473abaf1ce.jpg)
## Suggestion
![iShot_2023-02-06_21 37 48](https://user-images.githubusercontent.com/33310255/217081039-29dcff98-7f26-463c-886e-d8b78e0b1f2e.jpg)
## Event Report
![iShot_2023-02-06_21 38 06](https://user-images.githubusercontent.com/33310255/217081121-7ad63ee5-41a8-41be-a24a-81eb459f48f6.jpg)

# Sharevax API
![Sharevax Swagger](https://user-images.githubusercontent.com/33310255/217095378-0401fa55-000e-4b42-a6df-9bdb49fe6203.jpg)

# Tech Stack
- Frontend: [React](https://reactjs.org/), [Leaflet](https://leafletjs.com/), [Tailwind CSS](https://tailwindcss.com/).
- Backend: [Spring](https://spring.io/), [PostgreSQL](https://www.postgresql.org/).
- SeaRoute: [SeaRoute](https://github.com/eurostat/searoute) computes shortest maritime routes between two locations.
