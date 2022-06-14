# ============================ # 
# Question 1: Data Preparation #
# ============================ # 

# --------------------------------------------------------------------------- # 
# 1a: Import the csv dataset as data1 and ensure that all textual data        #
# are treated as categories instead of text string characters. Show your      #
# code.                                                                       #
# --------------------------------------------------------------------------- # 

library(data.table)
setwd("/Users/ernestang98/Desktop/Year\ 2\ Sem\ 2/BC2407/AY2020s2\ BC2407\ CBA")
data1 <- fread("resale-flat-prices-201701-202103.csv", stringsAsFactors = T)
str(data1)

# --------------------------------------------------------------------------- # 
# 1b: Create a new derived variable remaining_lease_yrs (defined as remaining # 
# lease in years) from remaining_lease and save as an integer datatype column #
# in data1. Show your code.                                                   #
# --------------------------------------------------------------------------- #

extract_years <- as.numeric(substr(data1$remaining_lease, 1, 2))
data1$remaining_lease_yrs <- extract_years
str(data1)

# extract_months <- as.numeric(substr(data1$remaining_lease, 10, 11))
# extract_months[is.na(extract_months)] <- 0
# extract_months_in_years <- extract_months/12
# convert_remaining_lease_in_years <- extract_months_in_years + extract_years

# --------------------------------------------------------------------------- # 
# 1c: Remove lease_commence_date and remaining_lease from data1.              #
# Show your code.                                                             #
# --------------------------------------------------------------------------- #

data1$lease_commence_date <- NULL
data1$remaining_lease <- NULL
str(data1)

# --------------------------------------------------------------------------- # 
# 1d: Create a new derived variable block_street by combining block and       #
# street information (with one white space as separator) and save as a        #
# categorical datatype column in data1. Remove block and street_name from     #
# data1. Show your code.                                                      #
# --------------------------------------------------------------------------- #

block_street <- paste(as.character(data1$block), 
                      as.character(data1$street_name),
                      sep = " ") 
data1$block_street <- factor(block_street)

data1$block <- NULL
data1$street_name <- NULL
str(data1)

# --------------------------------------------------------------------------- # 
# 2a. Which month year has the (i) lowest transaction volume,                 #                       
# (ii) highest transaction volume, and what are their number of sales?        #                                   
# --------------------------------------------------------------------------- #

number_of_sales_by_month <- as.data.frame(table(data1$month))

highest_sales_by_month <- number_of_sales_by_month[
  number_of_sales_by_month$Freq == max(number_of_sales_by_month$Freq), 
]

lowest_sales_by_month <- number_of_sales_by_month[
  number_of_sales_by_month$Freq == min(number_of_sales_by_month$Freq), 
]

highest_sales_by_month
lowest_sales_by_month

library(plotly)

fig <- plot_ly(
  x = number_of_sales_by_month$Var1,
  y = number_of_sales_by_month$Freq,
  type = "bar"
)

fig

# --------------------------------------------------------------------------- # 
# 2b. Which town has the (i) lowest transaction volume,                       #
# (ii) highest transaction volume, and what are their number of sales?        #                            
# --------------------------------------------------------------------------- #

number_of_sales_by_town <- as.data.frame(table(data1$town))

highest_sales_by_town <- number_of_sales_by_town[
  number_of_sales_by_town$Freq == max(number_of_sales_by_town$Freq), 
]
lowest_sales_by_town <- number_of_sales_by_town[
  number_of_sales_by_town$Freq == min(number_of_sales_by_town$Freq), 
]

lowest_sales_by_town
highest_sales_by_town

library(plotly)

fig <- plot_ly(
  x = number_of_sales_by_town$Var1,
  y = number_of_sales_by_town$Freq,
  type = "bar"
)

fig

# --------------------------------------------------------------------------- # 
# 2c. Generate an output that shows the top 5 resale prices and bottom 5      #
# resale prices in terms of flat_type, block_street, town, floor_area_sqm,    #
# storey_range, and resale_price.                                             #                                
# --------------------------------------------------------------------------- #

bottom_5_prices <- head(unique(sort(data1$resale_price)), 5)
top_5_prices <- tail(unique(sort(data1$resale_price)), 5)

temp_bottom_1 <- data1[data1$resale_price == bottom_5_prices[1]]
temp_bottom_2 <- data1[data1$resale_price == bottom_5_prices[2]]
temp_bottom_3 <- data1[data1$resale_price == bottom_5_prices[3]]
temp_bottom_4 <- data1[data1$resale_price == bottom_5_prices[4]]
temp_bottom_5 <- data1[data1$resale_price == bottom_5_prices[5]]

temp_top_1 <- data1[data1$resale_price == top_5_prices[1]]
temp_top_2 <- data1[data1$resale_price == top_5_prices[2]]
temp_top_3 <- data1[data1$resale_price == top_5_prices[3]]
temp_top_4 <- data1[data1$resale_price == top_5_prices[4]]
temp_top_5 <- data1[data1$resale_price == top_5_prices[5]]

bottom_5 <- rbind(temp_bottom_1, temp_bottom_2, temp_bottom_3, temp_bottom_4, temp_bottom_5)
top_5 <- rbind(temp_top_1, temp_top_2, temp_top_3, temp_top_4, temp_top_5)

bottom_5 <- bottom_5 %>% select(town, flat_type, storey_range, floor_area_sqm, resale_price, block_street)
top_5 <- top_5 %>% select(town, flat_type, storey_range, floor_area_sqm, resale_price, block_street)

bottom_5 <- head(data1[order(data1$resale_price)], 5)
top_5 <- tail(data1[order(data1$resale_price)], 5)

bottom_5$month <- NULL
bottom_5$flat_model <- NULL
bottom_5$remaining_lease_yrs <- NULL

top_5$month <- NULL
top_5$flat_model <- NULL
top_5$remaining_lease_yrs <- NULL

bottom_5
top_5

# --------------------------------------------------------------------------- # 
# 2d. Conduct additional data exploration. Show (with screenshots of software # 
# outputs) and explain the interesting findings discovered.                   #                                                   
# --------------------------------------------------------------------------- #

library(ggplot2)

temp <- data1[data1$town == "CENTRAL AREA" | 
              data1$town == "QUEENSTOWN" | 
              data1$town == "BUKIT TIMAH" |
                data1$town == "BISHAN" | 
                data1$town == "TOA PAYOH" | 
                
                data1$town == "SEMBAWANG" | 
              
                data1$town == "BUKIT BATOK" | 
                data1$town == "BUKIT PANJANG" |
                data1$town == "YISHUN" |
                data1$town == "WOODLANDS" 
              ]

# # ggplot(data1, aes(town, resale_price)) + geom_point() + geom_jitter()
ggplot(temp) + geom_boxplot(aes(x=town, y=resale_price, fill=town))

ggplot(data = temp, aes(x = temp$town,y = frequency(temp$flat_type),fill = factor(temp$flat_type)))+
  geom_bar(stat = "identity", position = "fill")+
  ggtitle("")+
  labs(x = "", y = "Frequency" , fill = "Flat Types")+
  scale_y_continuous(labels = function(y)paste0(y*100, "%"))


ggplot(data = temp) + 
  geom_boxplot(aes(x=flat_type, y=resale_price, fill=flat_type)) + 
  facet_grid(. ~ temp$town) +
  facet_wrap(. ~ temp$town, ncol = 5)

ggplot(data = temp) + 
  geom_boxplot(aes(x=storey_range, y=resale_price, fill=storey_range)) + 
  facet_grid(. ~ temp$town) +
  facet_wrap(. ~ temp$town, ncol = 5)

ggplot(data = temp) + 
  geom_boxplot(aes(x=flat_model, y=resale_price, fill=flat_model)) + 
  facet_grid(. ~ temp$town) +
  facet_wrap(. ~ temp$town, ncol = 5)

ggplot(data = temp, aes(x = temp$floor_area_sqm, y = temp$resale_price, colour=factor(temp$town))) + 
  facet_grid(. ~ temp$town) + geom_point() + geom_smooth(method='lm', color="black") +
  labs(x = "Floor Area", y = "Resale Price", color="Town") +
  facet_wrap(. ~ temp$town, ncol = 5)

barplot(table(temp[temp$town == "CENTRAL AREA"]$month),
        main="Number of sales per month in Central Area",
        xlab="Month",
        ylab="Count",
        border="blue",
        col="deepskyblue"
)

ggplot(data = temp[temp$town == "CENTRAL AREA"]) + 
  geom_boxplot(aes(x=month, y=resale_price, fill=month))

barplot(table(temp[temp$town == "QUEENSTOWN"]$month),
        main="Number of sales per month in Queenstown",
        xlab="Month",
        ylab="Count",
        border="blue",
        col="deepskyblue"
)

ggplot(data = temp[temp$town == "QUEENSTOWN"]) + 
  geom_boxplot(aes(x=month, y=resale_price, fill=month))

barplot(table(temp[temp$town == "BUKIT TIMAH"]$month),
        main="Number of sales per month in Bukit Timah",
        xlab="Month",
        ylab="Count",
        border="blue",
        col="deepskyblue"
)

ggplot(data = temp[temp$town == "BUKIT TIMAH"]) + 
  geom_boxplot(aes(x=month, y=resale_price, fill=month))


barplot(table(temp[temp$town == "BISHAN"]$month),
        main="Number of sales per month in Bishan",
        xlab="Month",
        ylab="Count",
        border="blue",
        col="deepskyblue"
)

ggplot(data = temp[temp$town == "BISHAN"]) + 
  geom_boxplot(aes(x=month, y=resale_price, fill=month))

barplot(table(temp[temp$town == "TOA PAYOH"]$month),
        main="Number of sales per month in Toa Payoh",
        xlab="Month",
        ylab="Count",
        border="blue",
        col="deepskyblue"
)

ggplot(data = temp[temp$town == "TOA PAYOH"]) + 
  geom_boxplot(aes(x=month, y=resale_price, fill=month))


barplot(table(temp[temp$town == "SEMBAWANG"]$month),
        main="Number of sales per month in Sembawang",
        xlab="Month",
        ylab="Count",
        border="blue",
        col="deepskyblue"
)

ggplot(data = temp[temp$town == "SEMBAWANG"]) + 
  geom_boxplot(aes(x=month, y=resale_price, fill=month))

barplot(table(temp[temp$town == "BUKIT BATOK"]$month),
        main="Number of sales per month in Bukit Batok",
        xlab="Month",
        ylab="Count",
        border="blue",
        col="deepskyblue"
)

ggplot(data = temp[temp$town == "BUKIT BATOK"]) + 
  geom_boxplot(aes(x=month, y=resale_price, fill=month))

barplot(table(temp[temp$town == "BUKIT PANJANG"]$month),
        main="Number of sales per month in Bukit Panjang",
        xlab="Month",
        ylab="Count",
        border="blue",
        col="deepskyblue"
)

ggplot(data = temp[temp$town == "BUKIT PANJANG"]) + 
  geom_boxplot(aes(x=month, y=resale_price, fill=month))

barplot(table(temp[temp$town == "YISHUN"]$month),
        main="Number of sales per month in Yishun",
        xlab="Month",
        ylab="Count",
        border="blue",
        col="deepskyblue"
)

ggplot(data = temp[temp$town == "YISHUN"]) + 
  geom_boxplot(aes(x=month, y=resale_price, fill=month))

barplot(table(temp[temp$town == "WOODLANDS"]$month),
        main="Number of sales per month in Woodlands",
        xlab="Month",
        ylab="Count",
        border="blue",
        col="deepskyblue"
)

ggplot(data = temp[temp$town == "WOODLANDS"]) + 
  geom_boxplot(aes(x=month, y=resale_price, fill=month))


barplot(table(data1$month),
        main="Number of sales per month",
        xlab="Month",
        ylab="Count",
        border="blue",
        col="deepskyblue"
)

barplot(table(data1$town),
        main="Number of sales per town",
        xlab="Month",
        ylab="Count",
        border="blue",
        col="deepskyblue"
)

# ggplot(data1, aes(town, resale_price)) + geom_point() + geom_jitter()
ggplot(data1) + geom_boxplot(aes(x=town, y=resale_price, fill=town))

# ggplot(data1, aes(block_street, resale_price)) + geom_point() + geom_jitter()
ggplot(data1) + geom_boxplot(aes(x=block_street, y=resale_price, fill=block_street))

# ggplot(data1, aes(month, resale_price)) + geom_point() + geom_jitter()
ggplot(data1) + geom_boxplot(aes(x=month, y=resale_price, fill=month))

# ggplot(data1, aes(flat_type, resale_price)) + geom_point() + geom_jitter()
ggplot(data1) + geom_boxplot(aes(x=flat_type, y=resale_price, fill=flat_type))

# ggplot(data1, aes(storey_range, resale_price)) + geom_point() + geom_jitter()
ggplot(data1) + geom_boxplot(aes(x=storey_range, y=resale_price, fill=storey_range))

ggplot(
  data = data1,
  aes(x = data1$floor_area_sqm, y = data1$resale_price)
) + geom_point() + geom_smooth(method='lm') + labs(x = "Floor Area Sqm", y = "Resale Price")

# ggplot(data1, aes(flat_model, resale_price)) + geom_point() + geom_jitter()
ggplot(data1) + geom_boxplot(aes(x=flat_model, y=resale_price, fill=flat_model))

ggplot(
  data = data1,
  aes(x = data1$remaining_lease_yrs, y = data1$resale_price)
) + geom_point() + geom_smooth(method='lm') + labs(x = "Remaining Lease Years", y = "Resale Price")

ggplot(data1) + geom_boxplot(aes(x=flat_type, y=resale_price, fill=town))

ggplot(data1) + geom_boxplot(aes(x=flat_model, y=resale_price, fill=storey_range))

# --------------------------------------------------------------------------- # 
# 3a. Copy data1 and save as data2. Show your code.                           #                                
# --------------------------------------------------------------------------- #

# copy data1 and save as data2 by creating a whole new dataframe

data2 <- copy(data1)
# data2 <- data.table(data1)
tracemem(data2)==tracemem(data1)

# cannot do this as data3 and data2 will be pointing to the same 
# memory space.

data3 <- data2
tracemem(data2)==tracemem(data3)

# --------------------------------------------------------------------------- # 
# 3b. Remove flat_type "1 ROOM" and "MULTI-GENERATION" cases from data2, and  #
# ensure these levels are also removed from the categorical level definition. # 
# Show the categorical levels of flat_type and list the number of cases by    #
# flat_type.                                                                  #                                
# --------------------------------------------------------------------------- #

summary(data2$flat_type)

data2 <- data2[!(data2$flat_type == "1 ROOM" |
                   data2$flat_type == "MULTI-GENERATION"), ]

data2$flat_type <- factor(data2$flat_type)

summary(data2$flat_type)

# --------------------------------------------------------------------------- # 
# 3c. Remove block_street from data2. Show your code.                         #                                                                                      
# --------------------------------------------------------------------------- #

str(data2)

data2$block_street <- NULL

str(data2)

# --------------------------------------------------------------------------- # 
# 3d. In data2, create a new variable storey by copying storey_range, and     #
# then create and use the categorical level “40 to 51” to combine all the     #
# relevant storey levels into this bigger category. Show and verify that the  #
# categorical levels in storey are created correctly to hold the right cases. #                                                 
# --------------------------------------------------------------------------- #

summary(data2$storey_range)

data2$storey <- data.table(data2$storey_range)

data2$storey <- as.character(data2$storey)

data2$storey[data2$storey == "40 TO 42"] <- "40 TO 51"
data2$storey[data2$storey == "43 TO 45"] <- "40 TO 51"
data2$storey[data2$storey == "46 TO 48"] <- "40 TO 51"
data2$storey[data2$storey == "49 TO 51"] <- "40 TO 51"

data2$storey <- factor(data2$storey)

levels(data2$storey)

# --------------------------------------------------------------------------- # 
# 3e. Show the categorical levels in storey and list the number of cases by   #
# storey.                                                                     #                                                                                    
# --------------------------------------------------------------------------- #

summary(data2$storey)

# --------------------------------------------------------------------------- # 
# 3f. Remove storey_range from data2. Show your code.                         #                                                                                         
# --------------------------------------------------------------------------- #

# str(data2)
# multi <- lm(resale_price ~ ., data = data2)
# vif(multi)

data2$storey_range <- NULL
str(data2)

# --------------------------------------------------------------------------- # 
# 3g. Remove flat model "2-room", "Premium Maisonette" and "Improved-         #
# Maisonette" cases from data2, and ensure these levels are also removed from #
# the categorical level definition. Show the categorical levels of flat_model #
# and list the number of cases by flat_model.                                 #                                                                                            
# --------------------------------------------------------------------------- #

summary(data2$flat_model)

data2 <- data2[!(data2$flat_model == "2-room" |
                   data2$flat_model == "Premium Maisonette" |
                   data2$flat_model == "Improved-Maisonette"), ]

data2$flat_model <- factor(data2$flat_model)

summary(data2$flat_model)

# --------------------------------------------------------------------------- # 
# 3h. How many cases and columns are in data2 after completing all the data   #
# prep steps above?                                                           #                                                                                
# --------------------------------------------------------------------------- #

dim(data2)

# --------------------------------------------------------------------------- # 
# 3i. Suggest a reason for executing such data preparation steps listed above #                                             
# --------------------------------------------------------------------------- #

# --------------------------------------------------------------------------- # 
# 4. Set seed as 2021 and do 70-30 train-test split on data2. Execute (i)    #
# Linear Regression (all predictor variables), (ii) MARS degree 2, and (iii)  #
# Random Forest to predict the target variable. Create and show a summary     #
# table that lists the trainset RMSE and testset RMSE for the 3 models (to    #
# nearest dollar). Which model performed the best?                            #                                                                    
# --------------------------------------------------------------------------- #

library(caTools)
library(car)
set.seed(2021)

train <- sample.split(Y = data2$resale_price, SplitRatio = 0.7)
trainset <- subset(data2, train == T)
testset <- subset(data2, train == F)

m1.start = Sys.time()

m1 <- lm(resale_price ~ ., data = trainset)

m1.end = Sys.time()

m1.time <- m1.end - m1.start

vif(m1)

summary(m1)
residuals(m1) 
RMSE.m1.train <- round(sqrt(mean(residuals(m1)^2)))
summary(abs(residuals(m1))) 

predict.m1.test <- predict(m1, newdata = testset)
testset.error <- testset$resale_price - predict.m1.test
RMSE.m1.test <- round(sqrt(mean(testset.error^2)))
summary(abs(testset.error))

RMSE.m1.train 
RMSE.m1.test 

library(earth)

mars.start = Sys.time()
mars1 <- earth(resale_price ~ ., degree=2, data=trainset)
mars.end = Sys.time()
mars.time <- mars.end - mars.start
summary(mars1)

mars1.trainset <- predict(mars1)
RMSE.mars1.train <- round(sqrt(mean((trainset$resale_price - mars1.trainset)^2)))
mars1.testset <-  predict(mars1, newdata = testset)
RMSE.mars1.test <- round(sqrt(mean((testset$resale_price - mars1.testset)^2)))

RMSE.mars1.train
RMSE.mars1.test

library(randomForest)

rf.start = Sys.time()
RF1 <- randomForest(resale_price ~ . , data=trainset, importance=T)
rf.end = Sys.time()
rf.time <- rf.end - rf.start

RF1.trainset <- predict(RF1)
RMSE.RF1.train <- round(sqrt(mean((trainset$resale_price - RF1.trainset)^2)))
RF1.testset <- predict(RF1, newdata = testset)
RMSE.RF1.test <- round(sqrt(mean((testset$resale_price - RF1.testset)^2)))

RMSE.RF1.train
RMSE.RF1.test

PredictiveModel <- c(
  "LinReg Train",
  "LinReg Test",
  "MARS Train",
  "MARS Test",
  "RandomForest Train",
  "RandomForest Test"
)

RMSE = c(
  RMSE.m1.train,
  RMSE.m1.test,
  RMSE.mars1.train, 
  RMSE.mars1.test, 
  RMSE.RF1.train, 
  RMSE.RF1.test
)

TimeTaken <- c(
  paste(round(m1.time), "secs"),
  paste(round(m1.time), "secs"),
  paste(round(mars.time), "mins"),
  paste(round(mars.time), "mins"),
  paste(round(rf.time), "hrs"),
  paste(round(rf.time), "hrs")
)

answer <- data.table(PredictiveModel, RMSE, TimeTaken)

# B <- c(25, 25, 25, 100, 100, 100, 500, 500, 500)
# 
# m <- ncol(data2)-1
# 
# RSF <- rep.int(c(1, floor(sqrt(m)), m), times=3)
# 
# OOB.error <- seq(1:9)
# 
# for (i in 1:length(B)) {
#   m.RF <- randomForest(resale_price ~ ., 
#                        data = trainset,
#                        mtry = RSF[i],
#                        ntree = B[i],
#                        na.action = na.omit)
#   OOB.error[i] <- m.RF$mse[B[i]]
# }
# 
# results <- data.frame(B, RSF, OOB.error)
# importance(m.RF)
# plot(m.RF)

# --------------------------------------------------------------------------- # 
# 5. What is the OOB RMSE of the Random Forest? Can this be used as the       #
# estimate of Random Forest performance instead of testset RMSE? Explain.     #                                            
# --------------------------------------------------------------------------- #

RF1

# RF1$mse

RF1$mse[500]

# RF1.OOB.MSE = RF1$mse[500]

RF1.OOB.RMSE <- round(sqrt(RF1$mse[500]))
RF1.OOB.RMSE


install.packages("stablelearner")
library("stablelearner")
stab<-stability(RF1,control=stab_control(sampler=bootstrap,v=0.9))

# --------------------------------------------------------------------------- # 
# 6. In Soifua (2018) report 2 , Gini-based variable importance was used      #
# instead of Accuracy-based variable importance via Perturbation. What are    #
# the Pros and Cons of using Gini-based variable importance? Is this better   #
# than using Accuracy-based variable importance?                              #
# --------------------------------------------------------------------------- # 

# --------------------------------------------------------------------------- # 
# 7. Soifua (2018) report Appendix B mentioned the idea of weighting each     #
# tree in the random forest by their respective OOB error to improve model    #
# performance. However, it was “concluded that the improvements were too      #
# modest to be worthwhile...” Suggest a reason based on your understanding    #
# of random forest.                                                           #
# --------------------------------------------------------------------------- # 

