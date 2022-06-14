//delete rows that are not required
db.covid.deleteMany({location:"World"})
db.covid.deleteMany({location:"International"})
db.mobility.deleteMany({sub_region_1:{$ne:""}})
db.mobility.deleteMany({sub_region_2:{$ne:""}})
db.mobility.deleteMany({metro_area:{$ne:""}})

// Question 1
db.covid.distinct("location", {"continent":"Asia"})

// Question 2
db.covid.aggregate([
     {$match:{$or: [{continent:"Asia"},
    {continent:"Europe"}}},
    {$match:{date:ISODate("2020-04-01T00:00:00.000+08:00")}},
    {$project:{_id:0, location:1 ,total_cases:{$convert: { input: "$total_cases", to: "double"}}}},
    {$match:{"total_cases":{$gt:10.0}}},
    {$group:{_id:"$location"}},
    {$sort:{_id:1}}
])

// Question 3
db.covid.aggregate([
{$project:{_id:0, location:1, continent:1 , date:1, total_cases:{$convert: { input: "$total_cases", to: "double", onError:0.0, onNull:0.0}}}},
{$match:{$and:[
{continent:"Africa"},
 {total_cases:{$lt:10000}},
{date:{$gte:ISODate("2020-04-01T00:00:00.000+08:00")}},
{date:{$lt:ISODate("2020-04-21T00:00:00.000+08:00")}}}},
{$group:{_id:"$location"}},  
{$sort:{_id:1}}
 ])

// Question 4
db.covid.aggregate([  
{$project:{_id:0, total_tests:{$convert: { input: "$total_tests", to: "double", onError:" "}},location:1}},
{$group:{_id:{country:"$location"},totalTests:{$sum:"$total_tests"}}},
{$match:{totalTests:{$eq:0}}},
{$project:{"_id.country":1}},
{$sort:{_id:1}}
])

// Question 5
db.covid.aggregate([
{$project:{_id:0, "month":{$month:{date:"$date", timezone:"+08:00"}}, "day":{$dayOfMonth: {date:"$date", timezone:"+08:00"}}, location:1, totalNewCases:{$convert:{input:"$new_cases", to:"double", onError:0.0, onNull:0.0}}}},
{$group:{_id:{groupByMonth:"$month"}, totalNewCases:{$sum:"$totalNewCases"}}},
{$sort:{totalNewCases:1}}
])

// Question 6
db.covid.aggregate([
{$project:{_id: 0, continent: 1 , "month":{$month:{date:"$date", timezone:"+08:00"}}, newCases:{$convert:{input:"$new_cases", to:"double", onError:0.0, onNull:0.0}}}},
{$group:{_id:{continent:"$continent", month:"$month"}, totalNewCases:{$sum:"$newCases"}}},
{$sort:{"_id.continent":1, "_id.month":1}}
])

//Question 7
db.response.aggregate([
    {$match:{$or: [{Country:"Austria"},
    {Country:"Belgium"},
    {Country:"Bulgaria"},
    {Country:"Croatia"},
    {Country:"Cyprus"},
    {Country:"Czechia"},
    {Country:"Denmark"},
    {Country:"Estonia"},
    {Country:"Finland"},
    {Country:"France"},
    {Country:"Germany"},
    {Country:"Greece"},
    {Country:"Hungary"},
    {Country:"Ireland"},
    {Country:"Italy"},
    {Country:"Latvia"},
    {Country:"Lithuania"},
    {Country:"Luxembourg"},
    {Country:"Malta"},
    {Country:"Netherlands"},
    {Country:"Poland"},
    {Country:"Portugal"},
    {Country:"Romania"},
    {Country:"Slovakia"},
    {Country:"Slovenia"},
    {Country:"Spain"},
    {Country:"Sweden"}]}},
    {$match: {"Response_measure":{$regex: "Masks"}}},
    {$group:{_id:"$Country"}},
    {$sort:{_id:1}}
])

// Question 8
db.response.updateMany({date_end: "NA"}, {$set: {date_end: "2020-08-01"}})

var countryListLength = 0
var countryList = []
var dateStartList = []
var dateEndList = []
var countryOverlapCounter = {}

var obj = db.response.aggregate([
    {$match:{$or: [
    {Country:"Austria"},
    {Country:"Belgium"},
    {Country:"Bulgaria"},
    {Country:"Croatia"},
    {Country:"Cyprus"},
    {Country:"Czechia"},
    {Country:"Denmark"},
    {Country:"Estonia"},
    {Country:"Finland"},
    {Country:"France"},
    {Country:"Germany"},
    {Country:"Greece"},
    {Country:"Hungary"},
    {Country:"Ireland"},
    {Country:"Italy"},
    {Country:"Latvia"},
    {Country:"Lithuania"},
    {Country:"Luxembourg"},
    {Country:"Malta"},
    {Country:"Netherlands"},
    {Country:"Poland"},
    {Country:"Portugal"},
    {Country:"Romania"},
    {Country:"Slovakia"},
    {Country:"Slovenia"},
    {Country:"Spain"},
    {Country:"Sweden"},
    ]}},
    {$match:{Response_measure: "MasksMandatory"}}
    ]).project({Country:1, date_start:1, date_end:1, _id:0}).toArray();

countryListLength = obj.length

for (var i = 0; i < countryListLength; i++) {
    countryList.push(obj[i]["Country"])
    var temp = new ISODate(obj[i]["date_start"].toString())
    temp = temp.getFullYear()+'-'+(temp.getMonth()+1)+'-'+temp.getDate();
    dateStartList.push(temp)
    temp = new ISODate(obj[i]["date_end"].toString())
    temp = temp.getFullYear()+'-'+(temp.getMonth()+1)+'-'+temp.getDate();
    dateEndList.push(temp)
}

var add = 0

for (var z = 0; z < countryListLength; z++) {
    countryOverlapCounter[countryList[z] + add] = 0
    add++
}

for (var a = 0; a < countryListLength; a++) {
    var country = countryList[a]
    var dateC = dateStartList[a]
    for (var b = 0; b < countryListLength; b++) {
        var comparedc = countryList[b]
        var compareds = dateStartList[b]
        var comparede = dateEndList[b]
        if (dateC >= compareds && dateC <= comparede && comparedc != country) {
            countryOverlapCounter[country+a] += 1
        }
    }
}

var max = 0;
var maxC = "";
var change = 0;

var copy = [];

for (var i = 0; i < countryListLength; i++) {
    copy[i] = countryList[i] + change;
    change++;
}

for (var i = 0; i < countryListLength; i++) {
    if (countryOverlapCounter[copy[i]] > max) {
        max = countryOverlapCounter[copy[i]]
        maxC = copy[i]
    }
}

var index = 0;

for (var i = 0; i < countryListLength; i++) {
    if (copy[i] == maxC) {
        index = i
        break
    }
}

var temp = dateStartList[index].toString()

var findMinDateArr = []
var findMinDate = ""

for (var a = 0; a < countryListLength; a++) {
    
    var com1 = new Date(dateStartList[a])
    var com2 = new Date(temp)
    var com3 = new Date(dateEndList[a])
    
    if (com1 <= com2 && com2 <= com3) {
        findMinDateArr.push(dateEndList[a])
    }
}

var sizeArr = findMinDateArr.length

findMinDate = findMinDateArr[0]

for (var b = 0; b < sizeArr; b++) {
    if (findMinDate > findMinDateArr[b]) {
        findMinDate = findMinDateArr[b]
    }
}

var minMax = [temp, findMinDate]
print(minMax)



// Question 9
db.covid.aggregate([
    {$match:
    {$and:[
        {$or:[
        {continent:"Europe"},
        {continent:"North America"}]},
        {date:{$gte:ISODate("2020-07-25T00:00:00.000+08:00")}},
        {date:{$lte:ISODate("2020-08-01T00:00:00.000+08:00")}}]}},
        
    {$project:{_id:0, date:1, newCases:{$convert:{input:"$new_cases", to:"double", onError:0.0, onNull:0.0}}, location:1}},
    {$group:{_id:{date:"$date"}, total_new_cases:{$sum:"$newCases"}}},
    {$sort:{"_id.date":1}}
    ])




//Question 10
function addIfConsec(array, country, final) {
    for (var i = 0; i < array.length-14; i++) {
     if ((array[i]+array[i+1]+array[i+2]+array[i+3]+array[i+4]+array[i+5]+array[i+6]+array[i+7]+array[i+8]+array[i+9]+array[i+10]+array[i+11]+array[i+12]+array[i+13]+array[i+14]) == 0) {
        final.push(country)
        break;
    }
  }   
}

var q10 = [];
var countries = []

var obj = db.covid.aggregate([
{$project:{_id:0, location:1 , date:1, total_cases:{$convert: { input: "$total_cases", to: "double", onError:0.0, onNull:0.0}}}},
{$match:{$and:[
 {total_cases:{$gt:50}},
},
{$group:{_id:"$location"}},  
{$sort:{_id:1}}
 ]).toArray()

for (var i = 0; i < obj.length; i++) {
    countries.push(obj[i]["_id"])
}
var len = countries.length;



for (var a = 0; a < len; a++) {
    // --- country to test --- //
    var test = countries[a];
    var array = [];
    var temp = db.covid.aggregate([
        {$project:{_id:0, location:1 , date:1, total_cases:{$convert: { input: "$total_cases", to: "double", onError:0.0, onNull:0.0}}, new_cases:{$convert: { input: "$new_cases", to: "double", onError:0.0, onNull:0.0}}}},
        {$match:{$and:[
         {location:test},
         {total_cases:{$gt:50}},
        }).toArray()
    for (var o = 0; o < temp.length; o++) {
        if (temp[o]["new_cases"] == 0) {
            array.push(0)
        }
        else {
            array.push(1)
        }
    }
    addIfConsec(array, test, q10)
}

print(q10)



//Question 11
var countries = [
	"Andorra",
	"Aruba",
	"Bahamas",
	"Barbados",
	"Bermuda",
	"Brunei",
	"Cambodia",
	"Cayman Islands",
	"Equatorial Guinea",
	"Faeroe Islands",
	"French Polynesia",
	"Gibraltar",
	"Guernsey",
	"Isle of Man",
	"Liechtenstein",
	"Mauritius",
	"Monaco",
	"Montenegro",
	"New Zealand",
	"San Marino",
	"Sint Maarten (Dutch part)",
	"Tanzania",
	"Trinidad and Tobago",
	"United States Virgin Islands",
	"Western Sahara"
];

var clen = countries.length;

var date = {}

for (var w = 0; w < clen; w++) {
    date[countries[w]] = ""
}

var dlen = date.length;

var arr = {};

for (var v = 0; v < clen; v++) {
    arr[countries[v]] = 0
}

for (var b = 0; b < clen; b++) {
    var tempCountry = countries[b]
    var temp = db.covid.aggregate([
        {$project:{_id:0, location:1 , date:1, total_cases:{$convert: { input: "$total_cases", to: "double", onError:0.0, onNull:0.0}}, new_cases:{$convert: { input: "$new_cases", to: "double", onError:0.0, onNull:0.0}}}},
        {$match:{$and:[
         {location:tempCountry},
         {total_cases:{$gt:50}},
        }).toArray()
    for (var a = 0; a < temp.length; a++) {
        if (temp[a]["location"] == tempCountry) {
            if (temp[a]["new_cases"] == 0) {
                arr[tempCountry] += 1;
                if (arr[tempCountry] == 14) {
                    date[tempCountry] = temp[a]["date"]
                    break;
                    }
                }
            }
            else {
                arr[tempCountry] = 0;
            }
        }
    }
}


var final = [];

function checkIfUptick(array) {
    var len = array.length
    for (var d = 0; d < len - 6; d++) {
        var temp = array[d] + array[d+1] + array[d+2] + array[d+3] + array[d+4] + array[d+5] + array[d+6]
        if (temp > 50) {
            return true;
        }
    }
}


for (var c = 0; c < clen; c++) {
    var theCounter = 0;
    var newArray = []
    var tempCountry = countries[c]
    var tempDate = date[tempCountry]
    var temp = db.covid.aggregate([
        {$project:{_id:0, location:1 , date:1, total_cases:{$convert: { input: "$total_cases", to: "double", onError:0.0, onNull:0.0}}, new_cases:{$convert: { input: "$new_cases", to: "double", onError:0.0, onNull:0.0}}}},
        {$match:{$and:[
         {location:tempCountry},
         {total_cases:{$gt:50}},
        }).toArray()
    
    for (var q = 0; q < temp.length; q++) {
        var conDate = temp[q]["date"]
        if (temp[q]["location"] == tempCountry && conDate >= tempDate) {
            var value = temp[q]["new_cases"]
            newArray.push(value)
        }
        var check = checkIfUptick(newArray);
        if (check && theCounter == 0) {
            final.push(tempCountry)
            theCounter++
        }
    }
}

print(final)




//Question 12
//retail and recre
db.mobility.aggregate([
    {$project:{_id:0, country_region:1, retail_and_recreation_percent_change_from_baseline:{$convert: { input: "$retail_and_recreation_percent_change_from_baseline", to: "double", onError: 0.0}}}},
    {$group:{_id:{country:"$country_region"}, totalrr:{$sum:"$retail_and_recreation_percent_change_from_baseline"}}},
    {$sort:{totalrr:-1}},
    {$limit:3},
    {$project:{country_region:1}}
    ])
    
    
    
//grocery and pharm   
db.mobility.aggregate([
    {$project:{_id:0, country_region:1, grocery_and_pharmacy_percent_change_from_baseline:{$convert: { input: "$grocery_and_pharmacy_percent_change_from_baseline", to: "double", onError: 0.0}}}},
    {$group:{_id:{country:"$country_region"}, totalgh:{$sum:"$grocery_and_pharmacy_percent_change_from_baseline"}}},
    {$sort:{totalgh:-1}},
    {$limit:3},
    {$project:{country_region:1}}
    ])
    
    
    
//parks
db.mobility.aggregate([
    {$project:{_id:0, country_region:1, parks_percent_change_from_baseline:{$convert: { input: "$parks_percent_change_from_baseline", to: "double", onError: 0.0}}}},
    {$group:{_id:{country:"$country_region"}, totalp:{$sum:"$parks_percent_change_from_baseline"}}},
    {$sort:{totalp:-1}},
    {$limit:3},
    {$project:{country_region:1}}
    ])
    
    
    
//transit stations
db.mobility.aggregate([
    {$project:{_id:0, country_region:1, transit_stations_percent_change_from_baseline:{$convert: { input: "$transit_stations_percent_change_from_baseline", to: "double", onError: 0.0}}}},
    {$group:{_id:{country:"$country_region"}, totalts:{$sum:"$transit_stations_percent_change_from_baseline"}}},
    {$sort:{totalts:-1}},
    {$limit:3},
    {$project:{country_region:1}}
    ])
    
    
//workplace
db.mobility.aggregate([
    {$project:{_id:0, country_region:1, workplaces_percent_change_from_baseline:{$convert: { input: "$workplaces_percent_change_from_baseline", to: "double", onError: 0.0}}}},
    {$group:{_id:{country:"$country_region"}, totalwp:{$sum:"$workplaces_percent_change_from_baseline"}}},
    {$sort:{totalwp:-1}},
    {$limit:3},
    {$project:{country_region:1}}
    ])
    
    
//residential
db.mobility.aggregate([
    {$project:{_id:0, country_region:1, residential_percent_change_from_baseline:{$convert: { input: "$residential_percent_change_from_baseline", to: "double", onError: 0.0}}}},
    {$group:{_id:{country:"$country_region"}, totalr:{$sum:"$residential_percent_change_from_baseline"}}},
    {$sort:{totalr:-1}},
    {$limit:3},
    {$project:{country_region:1}}
    ])

// Question 13
db.covid.aggregate([
    {$match:{location:"Indonesia"}},
    {$project:{_id:0, location:1, date:1, total_cases:{$convert: { input: "$total_cases", to: "double", onError: 0.0}}}},
    {$match:{total_cases:{$gt:20000}}}
    {$sort:{date:1}},
    {$limit:1}
    ])
 
db.mobility.aggregate([
    {$project: {_id:0, country_region:1, retail_and_recreation_percent_change_from_baseline:1, workplaces_percent_change_from_baseline:1, 
    grocery_and_pharmacy_percent_change_from_baseline:1, date:{$convert:{input:"$date", to:"date"}}}},
    {$match:{$and:[
    {country_region:"Indonesia"},    
    {"date":{$gt:ISODate("2020-05-22T00:00:00.000+08:00")}}]}}
   
    ])   