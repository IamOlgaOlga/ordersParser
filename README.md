# ordersParser
This is a solving for some task: Need to create a file parser (for json, csv files) and parse different files in parallel.
Result parsed string must be like: 
"id":1,"amount":1000,"currency":"RUB","comment":"some comments","filename":"testFile","line":1,"result":"OK" 

for original string (in case testFile.json):
1. {"orderId": 1, "amount": 1000, "currency": "RUB", "comment": "some comments"}

Steps to run the application:
1) mvn clean install
2) java -jar parser-0.1.jar <path_to_file1> <path_to_file2>
