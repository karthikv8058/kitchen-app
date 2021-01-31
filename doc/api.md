# Server API Documentation

SmartTONi Server running on port **8888**, Here is Full documentation to the end points


## [Method]  /[PATH]
> parameter 1 : description\
> parameter 2 : description\
> parameter 3 : description

[API Description] 


## POST /assign-task
> taskId : task to be assigned

to assign task 

## POST /update-task
> queueId :work to be updated
> status  : status of the task
> time : time to update in work

to update task 


## POST /check-task-to-finish
> taskId :task to be checked to finsish

to check the task to be finsished or not 


## POST /get-task 
> taskid : task to check 
> isIntervention  : to check the task is intervention or not 
> interventionId : generate page using interventionId
> workId :unique Id of work 

to retrive the task and generate page 


## POST /get-recipes

to get the list of recipes 


## POST /get-recipe-labels

to get the list of labels associated with each recipes


## POST /new-pos-order
> order : order object conatins order details include meals,courses,delivery time,table no., isInventory ,  recipes etc

to place a new order


## POST /fetch-order-data
> orderId : Id used to get the details of order 

to retrive the detailed data of a specified order


## POST /get-all-users

to get the list of all users 

## POST /login
> email : username 
> password  : to verify the user 
> ip :client Ip from which the user login

to check and verify the user who uses the application

## POST /get-stations

to get the list of stations

## POST /station-tasks

to get all the station associated tasks 

## POST /get-station-users
> stationId : to compare in user station assignment 

to get the list of the users associated with each station

## POST /overview-task

to get all the order item

## POST /check-order-started
> orderId :Id to get the order

to check wheather the order is started or not


## POST /finish-order
> orderId :Id to get the order

To finish an open order


## POST /add-station-user
> stationId :set of staion Ids

to associate each station to a user


## POST /get-station-list

to get the list of all the user who are associated with each station


## POST /get-all-labels

to get the list of all labels


## POST /assign-task
> taskId :task to be assigned 

To assign a task to a specific user 


## POST /unassign-task

To Unassign a task to a specific user 

## POST /logout

To logout a specific user from the application


## POST /ping

To check the server is alive or not 


## POST /delete-order
> orderId :order to be deleted 

To delete a specific order

## POST /get-inventory-data

To get the details of items present in inventory

## POST /update-inventory-quantity
> inventoryId :unique Id of an item 
> inventoryQuantity :Quanytity to be updated 

To update the recipe quantity of an inventory item 

## POST /get-printer-data
> orderId :unique Id of an order 

To retrive the printer data of a specific order to parse

## POST /delete-printer-message
> orderId :unique Id of an order 

To delete  the printer data of a specific order 

## POST /store-to-analyze
> orderId :unique Id of an order 

To store the order data into web 


## POST /on-call
> courseId :unique Id of an course 

To make the onCall specified item to normal order

## POST /get-order-details
> orderId :unique Id of an order 

To get the list of task and details of a specific order



## POST /update-intervention
> intervention :unique Id of an intervention
> status :current status of an intervention
> time :Time in which the intervention are to be shown 
> reduceValue :The value of time to be reduced to the current time of intervention 
 
To update the intervention time 

## POST /chat
> query :Question to be processed
> session :A random number

To get the help of administartor


## POST /recipe-Details
> recipeId :unique Id of a recipe

To get the details of a recipe

## POST /get-web-credentials

To get the details of a user credentials and tokens of a user


## POST /sync-request

To start the cloud synching and initiating the same 

## POST /image-upload
> isIntervention :check whether the task is intervention or not
> isSteps :check whether step or not
> isFromRecipe :to get the source of the image upload
> imageType :to check the image type whether for reciep or task or step
> uuid :unique Id of an item
> uri :uri of new uploaded image 

To upload a new image to a recipe,task or step

## POST /get-user-right

To get the rights assigned to a user
