use IndividualAssignment;

#1 Based on the list of shopping malls, what are the sector names?
select distinct Sector from IndividualAssignment.shoppingmalls;

#2 How many shopping malls has “city” (consider both upper and lower cases) in its name?
select distinct Mall_Name from IndividualAssignment.shoppingmalls
where Mall_Name like "%city%" or "%City%";

#3 What are the top 3 most common surnames? Display the number of individuals of each.
select Surname, count(Surname) as Amt from IndividualAssignment.demo
group by Surname order by count(Surname) desc limit 3;

#4 What is the age distribution among the data? Display the number of individuals by age in descending order.
select Age, count(Age) as Amt from IndividualAssignment.demo group by Age order by count(Age) desc;

#5 What is the percentage of individuals in essential services among all individuals in the dataset?
select count(*)/(select count(*) from IndividualAssignment.demo)
as PercOFEssen from IndividualAssignment.demo where EssentialSrv = 1;

#6 What is the percentage of female who is working in essential services among all female in the dataset?
select count(*)/(select count(*) from IndividualAssignment.demo where gender = 0)
as PercOFEssenFemale from IndividualAssignment.demo where EssentialSrv = 1 and Gender = 0;

/* 7 What is the percentage of male who is between 22 to 45 years (inclusive of 22 and 45) old working
     in essential services among all male of the age range? */
select count(*)/(select count(*) from IndividualAssignment.demo where gender = 1 and Age <= 45 and Age >= 22)
as PercOFEssenMale22To45yo from IndividualAssignment.demo
where EssentialSrv = 1 and gender = 1 and Age <= 45 and Age >= 22;

#8 How many unique people have checked in at shopping malls in the west sector?
select count(distinct(ID)) as UniquePpl from IndividualAssignment.checkin as c
inner join IndividualAssignment.shoppingmalls as s on c.Location = s.mall_id
where sector = "West";

/* 9 What are the top 3 shopping malls in the east sector with the most instances of checked in (an individual
     can perform multiple instances of check-in at a mall)? */
select Location, count(MobileNO) as Amt from IndividualAssignment.checkin as c
inner join IndividualAssignment.shoppingmalls as s on c.Location = s.mall_id
where sector = "East" group by Location order by count(Amt) desc limit 3;

#10 What are the top 3 shopping malls in the west sector with the most checked in after 6pm (including 6pm sharp)?
select Location, count(MobileNO) as Amt from IndividualAssignment.checkin as c
inner join IndividualAssignment.shoppingmalls as s on c.Location = s.mall_id
where sector = "West" and hour >= 18 group by Location order by Amt desc, Location desc limit 3;

#11 What is the check-in distribution among sectors? Display the number of individuals of each.
select sector, count(MobileNO) as AmtOfCheckins from IndividualAssignment.checkin as c
inner join IndividualAssignment.shoppingmalls as s on c.location = s.mall_id group by sector;

#12 How many unique people checked in at “Tiong Bahru Plaza” between 1-May-2020 and 28-June-2020?
select count(distinct ID) as PplTBP from IndividualAssignment.checkin as c
inner join IndividualAssignment.shoppingmalls as s on c.location = s.mall_id
where Mall_Name = "Tiong Bahru Plaza" and Year = 2020 and day between 1 and 28 and Month between 5 and 6;

/* 13 How many people checked in at “Tiong Bahru Plaza” between 1-May-2020 and 28-June-2020, who are female
	  and older than 40 years old (40 yo inclusive)? */
select count(d.ID) as PplTBP from IndividualAssignment.checkin as c
inner join IndividualAssignment.shoppingmalls as s on c.location = s.mall_id
inner join IndividualAssignment.demo as d on d.ID = c.ID
where Mall_Name = "Tiong Bahru Plaza" and Year = 2020 and day between 1 and 28 and Month between 5 and 6
and Gender = 0 and age >= 40;

#14 Who (and on which day and month) has checked in at “Tiong Bahru Plaza” or at “Alexandra Central” more than once on a day?
-- Query ID of people checking in more than once either at TBP or AC 
select ID, day, month, count(ID) as Amt from IndividualAssignment.checkin as c
inner join IndividualAssignment.shoppingmalls as s on c.location = s.mall_id
where Mall_Name in ("Alexandra Central", "Tiong Bahru Plaza") group by ID, day, month having Amt > 1;

-- Query ID of people checking in at either TBP more than once or AC more than once
(select * from
(select ID, day, month, count(ID) as Amt from IndividualAssignment.checkin as c
inner join IndividualAssignment.shoppingmalls as s on c.location = s.mall_id
where Mall_Name = "Alexandra Central" group by day, month, ID)
as new_table where Amt > 1)
union
(select * from (select ID, day, month, count(ID) as Amt from IndividualAssignment.checkin as c
inner join IndividualAssignment.shoppingmalls as s on c.location = s.mall_id
where Mall_Name = "Tiong Bahru Plaza" group by day, month, ID)
as new_table where Amt > 1);

#15 Display the instances of check-in at each shopping mall when it was at the maximum crowdedness level?
select mall_name, count(mobileno) as Amt from
(select id, mobileNO, mall_name, crowdedness_id from IndividualAssignment.checkin as c
inner join IndividualAssignment.shoppingmalls as s on s.mall_id = c.location
inner join IndividualAssignment.crowdedness as cr on cr.crowdedness_id = c.crowdedness
where cr.crowdedness_id = 1)
as new_table
group by mall_name;

#16 Which are the shopping malls with more female check-ins than male check-ins?
select table0.Location as Location, female as FemaleAmt, male as MaleAmt from
(select Location, count(gender) as female from IndividualAssignment.checkin as c
inner join IndividualAssignment.demo as d on c.id = d.id
inner join IndividualAssignment.shoppingmalls as s on s.mall_id = c.location where gender = 0 group by Location)
as table0
inner join
(select Location, count(gender) as male from IndividualAssignment.checkin as c
inner join IndividualAssignment.demo as d on c.id = d.id
inner join IndividualAssignment.shoppingmalls as s on s.mall_id = c.location where gender = 1 group by Location)
as table1
on table0.Location = table1.Location
where female > male;

#17 Who are the individuals with multiple check-ins at a shopping mall, sort results in descending order?
select ID, Location, count(ID) as checkinAmt
from IndividualAssignment.checkin
group by ID, Location
having checkinAmt > 1
order by checkinAmt desc;

/* 18 Show individuals (an individual who checked in with two different mobile numbers
      are considered two occasions of check-ins) who had multiple check-ins at a shopping mall? */
select ID, Location, count(mobileno) as checkinAmt
from IndividualAssignment.checkin
group by ID, Location
having checkinAmt > 1;

#19 Among the list of individuals in the dataset, what are the mobile numbers that appear to be in proximity in at least one occasion?
select mobileno, nearbymobileno from
(select mobileno, nearbymobileno, count(mobileno) as amt from IndividualAssignment.tracetogether
group by mobileno, nearbymobileno having amt > 0) as new_table;

#20 Mobile numbers were manually entered into the system during check-ins. Are there any problems with the mobile numbers? If so, what are those?
select mobileno from IndividualAssignment.checkin
where mobileno not in
(select distinct MobileNo from IndividualAssignment.checkin
where mobileno regexp '^[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]$' and mobileno != '')
