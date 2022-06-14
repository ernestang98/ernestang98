# Name: Ernest Ang Cheng Han
# Course: BC2406
# Seminar Group 3
# Matriculation Number: U1921310H

#============================================================================================#
#============================================================================================#

# Import Data set, and clean data accordingly
# 1. Categorize categorical variables
# 2. Rename boolean categorical variables
# 3. Identify outliers/NA if any

library(data.table)
dt <- fread("/Users/ernestang98/Desktop/cba/AY2020sem1CBA/AHD.csv", stringsAsFactors = TRUE)
summary(dt)

dt$Sex<- factor(dt$Sex)
dt$Fbs<- factor(dt$Fbs)
dt$RestECG<- factor(dt$RestECG)
dt$ExAng<- factor(dt$ExAng)
dt$Slope<- factor(dt$Slope)
dt$Ca<- factor(dt$Ca)
summary(dt)

dt[dt$Sex==1, Sex_New := "Male"]
dt[dt$Sex==0, Sex_New := "Female"]

dt[dt$Fbs==1, Fbs_New := "FBS_TRUE"]
dt[dt$Fbs==0, Fbs_New := "FBS_FALSE"]

dt[dt$ExAng==1, ExAng_New := "ANGINA_TRUE"]
dt[dt$ExAng==0, ExAng_New := "ANGINA_FALSE"]

dt[dt$RestECG==0, RestECG_New := "normal"]
dt[dt$RestECG==1, RestECG_New := "ST-T wave abnormality"]
dt[dt$RestECG==2, RestECG_New := "hypertrophy"]

dt[dt$Slope==1, Slope_New := "upsloping"]
dt[dt$Slope==2, Slope_New := "flat"]
dt[dt$Slope==3, Slope_New := "downsloping"]

dt$Sex_New<- factor(dt$Sex_New)
dt$ExAng_New<- factor(dt$ExAng_New)
dt$Fbs_New<- factor(dt$Fbs_New)
dt$RestECG_New<- factor(dt$RestECG_New, ordered=T, levels = c("normal", "ST-T wave abnormality", "hypertrophy"))
dt$Slope_New<- factor(dt$Slope_New, ordered=T, levels = c("upsloping", "flat", "downsloping"))
summary(dt)

spare_dt <- copy(dt)
spare_dt2 <- copy(dt)
spare_dt3 <- copy(dt)
spare_dt4 <- copy(dt)
spare_dt5 <- copy(dt)

#============================================================================================#
#============================================================================================#

# None of the continuous variables seem cleaning in spite of outliers observed
# Outliers do not seem to be recorded incorrectly or as a result of a mistake
# Outliers could be a result of really unhealthy people :(

boxplot(dt$RestBP)
boxplot(dt$Age)
boxplot(dt$Chol)
boxplot(dt$MaxHR)
boxplot(dt$Oldpeak)

# Need to clean categorical variables Thal & Ca since they have NA values

Ca_NA <- c(which(is.na(dt$Ca)))
Thal_NA <- c(which(is.na(dt$Thal)))

dt[c(Ca_NA)]
dt[c(Thal_NA)]

# Thal & Ca values seem to be missing completely at random since the results 
# and values were collated and measured by a machine or human objectively
# and by observation, there does not seem to be a systemic error which would
# have caused these particular rows of the data set to be missing Ca or Thal

# Clean by column mode:
mode <- function(dt) {
  findMode <- unique(dt)
  findMode[which.max(tabulate(match(dt, findMode)))]
}

mode(dt$Ca)
mode(dt$Thal)

spare_dt[is.na(Ca), Ca := mode(dt$Ca)]
spare_dt[is.na(Thal), Thal := mode(dt$Thal)]
summary(spare_dt)

# Clean by relevant subgroup mode:
# For Ca, the similarity would be Male Gender, hence, subgroup will be the males
subgroup1 <- dt[Sex_New == "Male"]
mode(subgroup1$Ca)

subgroup2 <- dt[Ca == 0 & RestBP == 128]
mode(subgroup2$Thal)

spare_dt2[is.na(Ca), Ca := mode(subgroup1$Ca)]
spare_dt2[is.na(Thal), Thal := mode(subgroup2$Thal)]
summary(spare_dt2)

# Clean by CART:
library(rpart)
library(rpart.plot) 
set.seed(2000)
CART <- rpart(Ca ~ ., data = spare_dt3, method = 'class', control = rpart.control(minsplit = 2, cp = 0))
printcp(CART)
plotcp(CART, main = "CP Error Plot")
CVerror.cap <- CART$cptable[which.min(CART$cptable[,"xerror"]), "xerror"] + CART$cptable[which.min(CART$cptable[,"xerror"]), "xstd"]
i <- 1; j<- 4
while (CART$cptable[i,j] > CVerror.cap) {i <- i + 1}
cp.A = ifelse(i > 1, sqrt(CART$cptable[i,1] * CART$cptable[i-1,1]), 1)
cp.A
pCART <- prune(CART, cp = cp.A)
printcp(pCART)
cart.predictCa <- predict(pCART, newdata = spare_dt3, type = "class")
temp <- data.table(spare_dt3, cart.predictCa)
temp[is.na(Ca), Ca := cart.predictCa]

CART <- rpart(Thal ~ ., data = spare_dt3, method = 'class', control = rpart.control(minsplit = 2, cp = 0))
printcp(CART)
plotcp(CART, main = "CP Error Plot")
CVerror.cap <- CART$cptable[which.min(CART$cptable[,"xerror"]), "xerror"] + CART$cptable[which.min(CART$cptable[,"xerror"]), "xstd"]
i <- 1; j<- 4
while (CART$cptable[i,j] > CVerror.cap) {i <- i + 1}
cp.A = ifelse(i > 1, sqrt(CART$cptable[i,1] * CART$cptable[i-1,1]), 1)
pCART <- prune(CART, cp = cp.A)
printcp(pCART)
cart.predictThal <- predict(pCART, newdata = spare_dt3, type = "class")
spare_dt3 <- data.table(temp, cart.predictThal)
spare_dt3[is.na(Thal), Thal := cart.predictThal]

temp1 <- copy(spare_dt3)
temp1 <- temp1[-c(Ca_NA)]
temp1 <- temp1[-c(Thal_NA)]
summary(temp1)

mean(temp1$cart.predictCa == temp1$Ca)
mean(temp1$cart.predictThal == temp1$Thal)

# Clean by Logistic Regression:
library(nnet)
levels(spare_dt4$Ca)
levels(spare_dt4$Thal)
Ca_NA <- c(which(is.na(spare_dt4$Ca))) ## 4
Thal_NA <- c(which(is.na(spare_dt4$Thal))) ## 2

temp <- copy(spare_dt4)
temp[is.na(Ca), Ca := mode(spare_dt4$Ca)] ## temporarily fill the NA values
fit <- multinom(Ca ~ ., data = temp)
summary(fit)
OR <- exp(coef(fit))
OR
OR.CI <- exp(confint(fit))
OR.CI
z <- summary(fit)$coefficients/summary(fit)$standard.errors
pvalue <- (1 - pnorm(abs(z), 0, 1))*2 
pvalue
m.final <- multinom(Ca ~ Age + AHD, data = temp)
predictedCa <- predict(m.final)

temp1 <- copy(spare_dt4)
temp1[is.na(Thal), Thal := mode(temp1$Thal)] ## temporarily fill the NA values
fit <- multinom(Thal ~ ., data = temp1)
summary(fit)
OR <- exp(coef(fit))
OR
OR.CI <- exp(confint(fit))
OR.CI
z <- summary(fit)$coefficients/summary(fit)$standard.errors
pvalue <- (1 - pnorm(abs(z), 0, 1))*2 
pvalue
m.final <- multinom(Thal ~ Slope, data = temp1)
predictedThal <- predict(m.final)

mean(temp$Ca == predictedCa)
mean(temp1$Thal == predictedThal)

spare_dt4 = data.table(spare_dt4, predictedCa, predictedThal)
spare_dt4[is.na(Ca), Ca:=predictedCa]
spare_dt4[is.na(Thal), Thal:=predictedThal]
summary(spare_dt4)

# Clean by removing the NA data:
spare_dt5 <- na.omit(spare_dt5)
summary(spare_dt5)

# Checking results
identical(spare_dt, spare_dt2)
identical(spare_dt, spare_dt3)
identical(spare_dt, spare_dt4)
identical(spare_dt3, spare_dt4)

# Cleaning the NA values by CART produces different results
# Cleaning the NA values by Logistic Regression produces different results
# CART & Logistic Regression has low accuracy rate and is time consuming 
# Hence we use mode to clean categorical NA values

#============================================================================================#
#============================================================================================#

# Copying the chosen way to clean table into a cleaned data table
clean <- copy(spare_dt)
summary(clean)

library(ggplot2)

# Visualize relationship between AHD and the other categorical/continuous variables

# Sex
ggplot(data = clean, aes(x = Sex_New,
                        y = frequency(AHD),
                        fill = factor(AHD)))+
  geom_bar(stat = "identity")+
  ggtitle("Stacked bar chart: AHD Status against Gender")+
  labs(x = "Gender", y = "Frequency" , fill = "AHD")+
  scale_fill_manual(values= c("light green", "orange"))

ggplot(data = clean, aes(x = Sex_New,
                         y = frequency(AHD),
                         fill = factor(AHD)))+
  geom_bar(stat = "identity", position = "fill")+
  ggtitle("Stacked bar chart: AHD Status against Gender")+
  labs(x = "Gender", y = "Frequency" , fill = "AHD")+
  scale_y_continuous(labels = function(y)paste0(y*100, "%"))+
  scale_fill_manual(values = c("light blue", "dark blue"))

# Fbs
ggplot(data = clean, aes(x = Fbs_New,
                         y = frequency(AHD),
                         fill = factor(AHD)))+
  geom_bar(stat = "identity")+
  ggtitle("Stacked bar chart: AHD Status against Fbs")+
  labs(x = "Fbs", y = "Frequency" , fill = "AHD")+
  scale_fill_manual(values= c("light green", "orange"))

ggplot(data = clean, aes(x = Fbs_New,
                         y = frequency(AHD),
                         fill = factor(AHD)))+
  geom_bar(stat = "identity", position = "fill")+
  ggtitle("Stacked bar chart: AHD Status against Fbs")+
  labs(x = "Fbs", y = "Frequency" , fill = "AHD")+
  scale_y_continuous(labels = function(y)paste0(y*100, "%"))+
  scale_fill_manual(values = c("light blue", "dark blue"))

# Angina
ggplot(data = clean, aes(x = ExAng_New,
                         y = frequency(AHD),
                         fill = factor(AHD)))+
  geom_bar(stat = "identity")+
  ggtitle("Stacked bar chart: AHD Status against Angina")+
  labs(x = "Angina", y = "Frequency" , fill = "AHD")+
  scale_fill_manual(values= c("light green", "orange"))

ggplot(data = clean, aes(x = ExAng_New,
                         y = frequency(AHD),
                         fill = factor(AHD)))+
  geom_bar(stat = "identity", position = "fill")+
  ggtitle("Stacked bar chart: AHD Status against Angina")+
  labs(x = "Angina", y = "Frequency" , fill = "AHD")+
  scale_y_continuous(labels = function(y)paste0(y*100, "%"))+
  scale_fill_manual(values = c("light blue", "dark blue"))

# Ca
ggplot(data = clean, aes(x = Ca,
                         y = frequency(AHD),
                         fill = factor(AHD)))+
  geom_bar(stat = "identity")+
  ggtitle("Stacked bar chart: AHD Status against Ca")+
  labs(x = "Ca", y = "Frequency" , fill = "AHD")+
  scale_fill_manual(values= c("light green", "orange"))

ggplot(data = clean, aes(x = Ca,
                         y = frequency(AHD),
                         fill = factor(AHD)))+
  geom_bar(stat = "identity", position = "fill")+
  ggtitle("Stacked bar chart: AHD Status against Ca")+
  labs(x = "Ca", y = "Frequency" , fill = "AHD")+
  scale_y_continuous(labels = function(y)paste0(y*100, "%"))+
  scale_fill_manual(values = c("light blue", "dark blue"))

# Slope
ggplot(data = clean, aes(x = Slope_New,
                         y = frequency(AHD),
                         fill = factor(AHD)))+
  geom_bar(stat = "identity")+
  ggtitle("Stacked bar chart: AHD Status against Slope")+
  labs(x = "Slope", y = "Frequency" , fill = "AHD")+
  scale_fill_manual(values= c("light green", "orange"))

ggplot(data = clean, aes(x = Slope_New,
                         y = frequency(AHD),
                         fill = factor(AHD)))+
  geom_bar(stat = "identity", position = "fill")+
  ggtitle("Stacked bar chart: AHD Status against Slope")+
  labs(x = "Slope", y = "Frequency" , fill = "AHD")+
  scale_y_continuous(labels = function(y)paste0(y*100, "%"))+
  scale_fill_manual(values = c("light blue", "dark blue"))

# Thal
ggplot(data = clean, aes(x = Thal,
                         y = frequency(AHD),
                         fill = factor(AHD)))+
  geom_bar(stat = "identity")+
  ggtitle("Stacked bar chart: AHD Status against Thal")+
  labs(x = "Thal", y = "Frequency" , fill = "AHD")+
  scale_fill_manual(values= c("light green", "orange"))

ggplot(data = clean, aes(x = Thal,
                         y = frequency(AHD),
                         fill = factor(AHD)))+
  geom_bar(stat = "identity", position = "fill")+
  ggtitle("Stacked bar chart: AHD Status against Thal")+
  labs(x = "Thal", y = "Frequency" , fill = "AHD")+
  scale_y_continuous(labels = function(y)paste0(y*100, "%"))+
  scale_fill_manual(values = c("light blue", "dark blue"))

# RestECG
ggplot(data = clean, aes(x = RestECG_New,
                         y = frequency(AHD),
                         fill = factor(AHD)))+
  geom_bar(stat = "identity")+
  ggtitle("Stacked bar chart: AHD Status against RestECG")+
  labs(x = "RestECG", y = "Frequency" , fill = "AHD")+
  scale_fill_manual(values= c("light green", "orange"))

ggplot(data = clean, aes(x = RestECG_New,
                         y = frequency(AHD),
                         fill = factor(AHD)))+
  geom_bar(stat = "identity", position = "fill")+
  ggtitle("Stacked bar chart: AHD Status against RestECG")+
  labs(x = "RestECG", y = "Frequency" , fill = "AHD")+
  scale_y_continuous(labels = function(y)paste0(y*100, "%"))+
  scale_fill_manual(values = c("light blue", "dark blue"))

# ChestPain
ggplot(data = clean, aes(x = ChestPain,
                         y = frequency(AHD),
                         fill = factor(AHD)))+
  geom_bar(stat = "identity")+
  ggtitle("Stacked bar chart: AHD Status against ChestPain")+
  labs(x = "ChestPain", y = "Frequency" , fill = "AHD")+
  scale_fill_manual(values= c("light green", "orange"))

ggplot(data = clean, aes(x = ChestPain,
                         y = frequency(AHD),
                         fill = factor(AHD)))+
  geom_bar(stat = "identity", position = "fill")+
  ggtitle("Stacked bar chart: AHD Status against ChestPain")+
  labs(x = "ChestPain", y = "Frequency" , fill = "AHD")+
  scale_y_continuous(labels = function(y)paste0(y*100, "%"))+
  scale_fill_manual(values = c("light blue", "dark blue"))

# Age
ggplot(data = clean) + 
  geom_boxplot(aes(x=AHD, y=Age, fill=AHD)) 

# RestBP
ggplot(data = clean) + 
  geom_boxplot(aes(x=AHD, y=RestBP, fill=AHD))

# Chol
ggplot(data = clean) + 
  geom_boxplot(aes(x=AHD, y=Chol, fill=AHD))

# MaxHR
ggplot(data = clean) + 
  geom_boxplot(aes(x=AHD, y=MaxHR, fill=AHD))

# Oldpeak
ggplot(data = clean) + 
  geom_boxplot(aes(x=AHD, y=Oldpeak, fill=AHD))

# Visualize relationship other relationships

ggplot(data = clean, aes(x = clean$Thal, y = clean$Oldpeak)) + 
  geom_boxplot(aes(x=Thal, y=Oldpeak, fill=Sex_New)) + 
  labs(x = "Thal", y = "Oldpeak", color="Sex")

ggplot(clean, aes(x=Age, y=frequency(Thal), fill=Thal)) +
  geom_col() + facet_grid(. ~ clean$Sex_New)+
  labs(x = "Gender", y = "Frequency")

ggplot(data = clean, aes(x = Sex_New,
                         y = frequency(ExAng_New),
                         fill = factor(ExAng_New)))+
  geom_bar(stat = "identity", position = "fill")+
  ggtitle("Stacked bar chart: Angina Status against Gender")+
  labs(x = "Gender", y = "Frequency" , fill = "Angina")+
  scale_y_continuous(labels = function(y)paste0(y*100, "%"))+
  scale_fill_manual(values = c("light blue", "dark blue"))

ggplot(data = clean, aes(x = Sex_New,
                         y = frequency(Ca),
                         fill = factor(Ca)))+
  geom_bar(stat = "identity", position = "fill")+
  ggtitle("Stacked bar chart: Ca Status against Gender")+
  labs(x = "Gender", y = "Frequency" , fill = "Ca")+
  scale_y_continuous(labels = function(y)paste0(y*100, "%"))+
  scale_fill_manual(values = c("light blue", "cadet blue", "deep sky blue", "dark blue"))

ggplot(data = clean, aes(x = Sex_New,
                         y = frequency(ChestPain),
                         fill = factor(ChestPain)))+
  geom_bar(stat = "identity", position = "fill")+
  ggtitle("Stacked bar chart: Ca Status against Gender")+
  labs(x = "Gender", y = "Frequency" , fill = "Ca")+
  scale_y_continuous(labels = function(y)paste0(y*100, "%"))+
  scale_fill_manual(values = c("light blue", "cadet blue", "deep sky blue", "dark blue"))

ggplot(data = clean) + 
  geom_boxplot(aes(x=Sex_New, y=Age, fill=AHD)) + 
  facet_grid(. ~ clean$RestECG_New)

ggplot(data = clean, aes(x = clean$Chol, y = clean$RestBP, colour=factor(clean$Sex_New))) + 
  facet_grid(. ~ clean$Sex_New) + geom_point() + geom_smooth(method='lm') +
  labs(x = "Cholesterol", y = "Blood Pressure", color="Sex")

ggplot(data = clean, aes(x = clean$Chol, y = clean$Oldpeak, colour=factor(clean$Sex_New))) + 
  facet_grid(. ~ clean$Sex_New) + geom_point() + geom_smooth(method='lm') +
  labs(x = "Cholesterol", y = "Oldpeak", color="Sex")

ggplot(clean, aes(x=Age, y=frequency(AHD), fill=AHD)) +
  geom_col() + facet_grid(. ~ clean$Sex_New)+
  labs(x = "Gender", y = "Frequency")

ggplot(clean, aes(x=Age, y=frequency(Age), fill=Age)) +
  geom_col() + facet_grid(. ~ clean$Sex_New)+
  labs(x = "Gender", y = "Frequency")

summary(clean)

#============================================================================================#
#============================================================================================#

# Creating Summary Table
library(table1)
table1::label(clean$Age) <- "Age"
table1::label(clean$Sex_New) <- "Sex"
table1::label(clean$ChestPain) <- "ChestPain"
table1::label(clean$RestBP) <- "RestBP"
table1::label(clean$Chol) <- "Cholesterol"
table1::label(clean$RestECG_New) <- "RestECG"
table1::label(clean$MaxHR) <- "MaxHR"
table1::label(clean$ExAng_New) <- "ExAng"
table1::label(clean$Oldpeak) <- "Oldpeak"
table1::label(clean$Slope_New) <- "Slope"
table1::label(clean$Ca) <- "Ca"
table1::label(clean$Thal) <- "Thal"
table1::table1(~ Age + Sex_New + ChestPain  + RestBP + Chol + RestECG_New 
               + MaxHR + ExAng_New + Oldpeak + Slope_New + Ca + Thal | AHD + Sex_New, data = clean)

#============================================================================================#
#============================================================================================#

clean <- clean[,1:14]

library(caTools)
library(car)
library(factoextra)

set.seed(2000)
train <- sample.split(Y = clean$AHD, SplitRatio = 0.7)
trainset <- subset(clean, train == T)
testset <- subset(clean, train == F)
log <- glm(AHD ~ . , family = binomial, data = trainset)
summary(log)
finalLog <- glm(AHD ~ Age + Sex + ChestPain + RestBP + Chol + Ca, family = binomial, data = trainset)
summary(finalLog)
OR.finalLog <- exp(coef(finalLog))
OR.finalLog
OR.CI.finalLog <- exp(confint(finalLog))
OR.CI.finalLog

threshold <- 0.5

train.p <- predict(finalLog, type = 'response')
train.p.v <- ifelse(train.p > threshold, 1, 0)
table3 <- table(Trainset.Actual = trainset$AHD, train.p.v, deparse.level = 2)
train.p.v <- ifelse(train.p.v == 1, "Yes", "No")
mean(train.p.v == trainset$AHD)

test.p <- predict(finalLog, newdata = testset, type = 'response')
test.p.v <- ifelse(test.p > threshold, 1, 0)
test.p.v <- ifelse(test.p.v == 1, "Yes", "No")
table4 <- table(Testset.Actual = testset$AHD, test.p.v, deparse.level = 2)
mean(test.p.v == testset$AHD)

vif(finalLog)

step(log)

#============================================================================================#
#============================================================================================#

library(rpart)
library(rpart.plot) 
set.seed(2000)  
CART <- rpart(AHD ~ ., data = trainset, method = 'class', control = rpart.control(minsplit = 2, cp = 0))
# rpart.plot(CART, nn= T, main = "CART Model (Unpruned)")
print(CART)
printcp(CART)
plotcp(CART, main = "CP Error Plot")
CVerror.cap <- CART$cptable[which.min(CART$cptable[,"xerror"]), "xerror"] + CART$cptable[which.min(CART$cptable[,"xerror"]), "xstd"]
i <- 1; j<- 4
while (CART$cptable[i,j] > CVerror.cap) {i <- i + 1}
cp.A = ifelse(i > 1, sqrt(CART$cptable[i,1] * CART$cptable[i-1,1]), 1)
pCART <- prune(CART, cp = cp.A)
rpart.plot(pCART, nn= T, main = "CART Model (Pruned)")
print(pCART)
printcp(pCART)
plotcp(pCART, main = "CP Error Plot")

cart.predict <- predict(pCART, newdata = trainset, type = "class")
results <- data.table(trainset, cart.predict)
table1 <- table(results$AHD, results$cart.predict, deparse.level = 2)
mean(results$AHD == results$cart.predict)

cart.predict <- predict(pCART, newdata = testset, type = "class")
results <- data.table(testset, cart.predict)
table2 <- table(results$AHD, results$cart.predict, deparse.level = 2)
mean(results$AHD == results$cart.predict)

pCART$variable.importance

scaledVarImpt <- round(100*pCART$variable.importance/sum(pCART$variable.importance))
scaledVarImpt[scaledVarImpt > 3]

summary(pCART)

#============================================================================================#
#============================================================================================#

# Comparing CART to Logistic Regression

table2

table4

# Calculating Gini Index & Entropy of Model

gini <- pCART$frame
gini[['gini']] <- 1-(gini[['dev']]/gini[['n']])^2-(1-gini[['dev']]/gini[['n']])^2
gini[,c('var','n','dev','gini')]
giniModel <- mean(gini$gini)
giniModel

entropy <- pCART$frame
entropy[['entropy']] <- -(entropy[['dev']]/entropy[['n']])^2 * log((entropy[['dev']]/entropy[['n']])^2, 2) + -(1-entropy[['dev']]/entropy[['n']])^2 * log((1-entropy[['dev']]/entropy[['n']])^2, 2)
entropy[,c('var','n','dev','entropy')]
enModel <- mean(entropy$entropy)
enModel
