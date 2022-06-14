<template>
  <div class="hello">

    <div class=" container input-group mb-3">
      <input type="text" class="form-control input1" @keypress.enter="search()" placeholder="Recipient's username"
             aria-label="Recipient's username" aria-describedby="basic-addon2" v-model="stock">
      <div class="input-group-append">
        <button type="button" class="btn btn-primary" @click="search()">
          <i class="material-icons">search</i>
        </button>
      </div>
    </div>

    <div class="container">

      <div class="div" v-if="price!==''">

        <div class="row">
          <div class="col-md-3">
            <div class="card card-stats card-background">
              <div class="card-header card-header-icon">
                <div class="card-icon">
                  <i class="material-icons">content_copy</i>
                </div>
                <p class="card-category">Beta</p>
                <h4 class="card-category">{{ this.beta }}</h4>
              </div>
            </div>
          </div>
          <div class="col-md-3">
            <div class="card card-stats card-background">
              <div class="card-header card-header-icon">
                <div class="card-icon">
                  <i class="material-icons">store</i>
                </div>
                <p class="card-category">Store</p>
                <h4 class="card-category">{{ this.ceo }}</h4>
              </div>
            </div>
          </div>
          <div class="col-md-3">
            <div class="card card-stats card-background">
              <div class="card-header card-header-icon">
                <div class="card-icon">
                  <i class="material-icons">info</i>
                </div>
                <p class="card-category">Info</p>
                <h4 class="card-category">{{ this.companyName }}</h4>
              </div>
            </div>
          </div>
          <div class="col-md-3">
            <div class="card card-stats card-background">
              <div class="card-header card-header-icon">
                <div class="card-icon">
                  <i class="material-icons">storage</i>
                </div>
                <p class="card-category">Sector</p>
                <h4 class="card-category">{{ this.sector }}</h4>
              </div>
            </div>
          </div>
        </div>

        <div class="row">
          <div class="col-md">
            <md-list-item class="col-md-3">
              <md-icon>timeline</md-icon>
              <span class="left">{{ this.price }}</span>
              <span class="right">P/B {{ this.pbvr }}</span>
            </md-list-item>
            <md-list-item class="col-md-3">
              <md-icon>attach_money</md-icon>
              <span class="left">ROA {{ this.roa }}</span>
              <span class="right">P/S {{ this.psr }}</span>
            </md-list-item>
            <md-list-item class="col-md-3">
              <md-icon>attach_money</md-icon>
              <span class="left">ROE {{ this.roe }}</span>
              <span class="right">P/E {{ this.per }}</span>
            </md-list-item>
            <md-list-item class="col-md-3">
              <md-icon>bar_chart</md-icon>
              <span class="">Gross Profit Mar {{ this.gpm }}%</span>
            </md-list-item>
          </div>
        </div>

        <div class="row">
          <div class="col-md-4">
            <div class="card card-profile border-0 add-css">
              <div class="card-avatar">
                <img class="img" :src="this.imgURL" alt="">
              </div>
              <div class="card-body">
                <h4 class="card-title no-outline">{{ this.companyName }}</h4>
                <p class="card-description">{{ this.description }}</p>
              </div>
            </div>
          </div>
          <div class="col-md-8">
            <div id="CandleStick"></div>
          </div>
        </div>

        <div class="row more-margin">
          <div class="col-md-4">
            <h5>Assets: USD ${{ this.totalAssets }} billion</h5>
            <div id="assets"></div>
          </div>
          <div class="col-md-4">
            <h5>Liabilities: USD ${{ this.totalLiabilities }} billion</h5>
            <div id="liabilities"></div>
          </div>
          <div class="col-md-4">
            <h5>Assets & Liabilities: USD ${{ this.totalAssetsLiabilities }} billion</h5>
            <div id="assetsLiabilities"></div>
          </div>
        </div>

        <div class="row">
          <div class="col-md-12 mt-3 align-center">
            <h4>Financial Growth</h4>
          </div>
        </div>
        <div class="row">
          <div id="myDiv"></div>
        </div>

        <div class="row mt-5 mb-5">
          <div class="col-md-12 mt-3 align-center">
            <h4>Income Statement</h4>
          </div>
        </div>

        <div class="row">
          <div class="card-body table-responsive">
            <table class="table table-hover">
              <thead class="headingcustomtable">
              <tr>
                <th>In Millions</th>
                <th>{{ ISDate[0] }}</th>
                <th>{{ ISDate[1] }}</th>
                <th>{{ ISDate[2] }}</th>
                <th>{{ ISDate[3] }}</th>
              </tr>
              </thead>
              <tbody>
              <tr>
                <td>Revenue</td>
                <td>{{ revenue[0] }}</td>
                <td>{{ revenue[1] }}</td>
                <td>{{ revenue[2] }}</td>
                <th>{{ revenue[3] }}</th>
              </tr>
              <tr>
                <td>Gross Profit</td>
                <td>{{ gp[0] }}</td>
                <td>{{ gp[1] }}</td>
                <td>{{ gp[2] }}</td>
                <td>{{ gp[3] }}</td>
              </tr>
              <tr>
                <td>Operating Income</td>
                <td>{{ inc[0] }}</td>
                <td>{{ inc[1] }}</td>
                <td>{{ inc[2] }}</td>
                <td>{{ inc[3] }}</td>
              </tr>
              <tr>
                <td>Net Income</td>
                <td>{{ ni[0] }}</td>
                <td>{{ ni[1] }}</td>
                <td>{{ ni[2] }}</td>
                <td>{{ ni[3] }}</td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
        <br/>
        <br/>
      </div>

    </div>

  </div>
</template>

<script>
import axios from 'axios';
import Plotly from 'plotly.js-dist'

export default {
  name: 'Dashboard',

  data: function () {
    return {
      isHidden: false,
      totalAssets: "",
      totalLiabilities: "",
      totalAssetsLiabilities: "",
      stock: "",
      beta: "",
      infoCompany: "",
      ceo: "",
      sector: "",
      companyName: "",
      price: "",
      marketCap: "",
      roa: "",
      roe: "",
      gpm: "",
      pbvr: "",
      psr: "",
      per: "",
      imgURL: "",
      description: "",
      open: '',
      close: '',
      high: '',
      low: '',
      x: [],
      openTemp: '',
      xi: '',
      cash: '',
      receivables: '',
      propertyPlantEquipmentNet: '',
      goodwill: '',
      longTermInvestments: '',
      accountPayables: '',
      shortTermDebt: '',
      longTermDebt: '',
      tax: '',
      deferredRevenue: '',
      revenue: '',
      cogs: '',
      gp: '',
      inc: '',
      ni: '',
      ISDate: ''
    }
  },

  methods: {

    update(stock) {
      try {
        this.isHidden = true
        this.getInfo(stock)
        this.getStock(stock)
        this.getCandleStickChart(stock)
        this.getPieChart(stock)
        this.getTable(stock)
        this.getLineGraph(stock)
      } catch (e) {
        alert("Failed")
      }
    },

    getLineGraph(stock) {
      axios.get("https://financialmodelingprep.com/api/v3/financial-growth/" + stock + "?limit=20&apikey=" + process.env.VUE_APP_FMP_API_KEY)
        .then(res => {

          const data2 = res.data
          let x = []
          let y1 = []
          let y2 = []
          let y3 = []

          for (let i = 0; i < data2.length; i++) {
            x.push(data2[i]["date"])
            y1.push(data2[i]["revenueGrowth"])
            y2.push(data2[i]["netIncomeGrowth"])
            y3.push(data2[i]["dividendsperShareGrowth"])
          }

          const trace1 = {
            type: 'scatter',
            x: x,
            y: y1,
            mode: 'lines',
            name: 'Revenue Growth',
            line: {
              color: 'rgb(219, 64, 82)',
              width: 2
            }
          };

          const trace2 = {
            type: 'scatter',
            x: x,
            y: y2,
            mode: 'lines',
            name: 'Net Income Growth',
            line: {
              color: 'rgb(55, 128, 191)',
              width: 2
            }
          };

          const trace3 = {
            type: 'scatter',
            x: x,
            y: y3,
            mode: 'lines',
            name: 'Dividends Growth',
            line: {
              color: 'rgb(160,64,219)',
              width: 2
            }
          };

          var layout = {
            width: 1200,
            height: 500
          };

          var data = [trace1, trace2, trace3];

          Plotly.newPlot('myDiv', data, layout);
        })
    },

    getTable(stock) {
      this.revenue = []
      this.cogs = []
      this.gp = []
      this.inc = []
      this.ni = []
      this.ISDate = []

      // axios.get("https://financialmodelingprep.com/api/v3/income-statement/AAPL?period=quarter&limit=400&apikey=demo")
      axios.get("https://financialmodelingprep.com/api/v3/income-statement/" + stock + "?limit=400&apikey=" + process.env.VUE_APP_FMP_API_KEY)
          .then(res => {
            this.ISTemp = res.data.slice(0, 4)
            for (this.xi of this.ISTemp) {
              this.revenue.push(this.xi["revenue"] / 1000000)
              this.cogs.push(this.xi["costOfRevenue"] / 1000000)
              this.gp.push(this.xi["grossProfit"] / 1000000)
              this.inc.push(this.xi["operatingIncome"] / 1000000)
              this.ni.push(this.xi["netIncome"] / 1000000)
              this.ISDate.push(this.xi["date"])
            }
          })
          .catch(err => {
            console.log(err)
          })

    },

    getPieChart(stock) {
      // axios.get("https://financialmodelingprep.com/api/v3/balance-sheet-statement/AAPL?period=quarter&limit=400&apikey=demo")
      axios.get("https://financialmodelingprep.com/api/v3/balance-sheet-statement/" + stock + "?limit=120&apikey=" + process.env.VUE_APP_FMP_API_KEY)
          .then(res => {
            let financialData = res.data[0]
            this.cash = financialData['cashAndCashEquivalents']
            this.receivables = financialData['netReceivables']
            this.inventory = financialData['inventory']
            this.ppe = financialData['propertyPlantEquipmentNet']
            this.goodwill = financialData['goodwill']
            this.longTermInvestments = financialData['longTermInvestments']
            this.accountPayables = financialData['accountPayables']
            this.shortTermDebt = financialData['shortTermDebt']
            this.longTermDebt = financialData['longTermDebt']
            this.tax = financialData['deferredTaxLiabilitiesNonCurrent']
            this.deferredRevenue = financialData['deferredRevenue']
            this.totalAssets = parseInt(financialData["totalAssets"]) / 1000000000
            this.totalLiabilities = parseInt(financialData["totalLiabilities"]) / 1000000000
            this.totalAssetsLiabilities = (parseInt(financialData["totalAssets"]) + parseInt(financialData["totalLiabilities"])) / 100000000

            var assets = [{
              type: "pie",
              values: [this.cash, this.receivables, this.inventory, this.ppe, this.goodwill, this.longTermInvestments],
              labels: ["Cash", "Receivables", "Inventories", "Plant", "PPE", "Goodwill", "LT Investment"],
              textinfo: "label+percent",
              textposition: "outside",
              automargin: true,
            }]

            var liabilities = [{
              type: "pie",
              values: [this.accountPayables, this.shortTermDebt, this.longTermDebt, this.tax, this.deferredRevenue],
              labels: ["Payables", "ST Debt", "LT Debt", "Tax", "Revenue"],
              textinfo: "label+percent",
              textposition: "outside",
              automargin: true,
            }]

            var assetsLiabilities = [{
              type: "pie",
              values: [this.cash, this.receivables, this.inventory, this.ppe, this.goodwill, this.longTermInvestments,
                this.accountPayables, this.shortTermDebt, this.longTermDebt, this.tax, this.deferredRevenue],
              labels: ["Cash", "Receivables", "Inventories", "Plant", "PPE", "Goodwill", "LT Investment",
                "Payables", "ST Debt", "LT Debt", "Tax", "Revenue"],
              textinfo: "label+percent",
              textposition: "outside",
              automargin: true
            }]

            var layout = {
              height: 600,
              width: 400,
              showlegend: false,
            }

            Plotly.newPlot('assets', assets, layout)
            Plotly.newPlot('liabilities', liabilities, layout)
            Plotly.newPlot('assetsLiabilities', assetsLiabilities, layout)

          })
          .catch(err => {
            console.log(err)
          })
    },

    getCandleStickChart(stock) {
      // axios.get("https://financialmodelingprep.com/api/v3/historical-price-full/AAPL?timeseries=80&apikey=demo")
      axios.get("https://financialmodelingprep.com/api/v3/historical-price-full/" + stock + "?timeseries=80&apikey=" + process.env.VUE_APP_FMP_API_KEY)
          .then(res => {
            this.open = []
            this.close = []
            this.high = []
            this.low = []
            this.x = []
            this.xi = []
            this.opentemp = res.data["historical"]
            for (this.xi of this.opentemp) {
              this.open.push(this.xi.open)
              this.close.push(this.xi.close)
              this.high.push(this.xi.high)
              this.low.push(this.xi.low)
              this.x.push(this.xi.date)
            }

            let trace1 = {
              x: this.x,
              close: this.close,
              decreasing: {line: {color: '#FF0000'}},
              high: this.high,
              increasing: {line: {color: '#17BECF'}},
              line: {color: 'rgba(31,119,180,1)'},
              low: this.low,
              open: this.open,
              type: 'candlestick',
              xaxis: 'x',
              yaxis: 'y'
            };

            let data = [trace1];
            let layout = {
              dragmode: 'zoom',
              margin: {
                r: 10,
                t: 20,
                b: 20,
                l: 30
              },
              showlegend: false,
              xaxis: {
                autorange: true,
                rangeslider: {range: [this.x[0], this.x[1000]]},
                title: 'Date',
                type: 'date'
              },
              yaxis: {
                autorange: true,
                range: [Math.min(...this.close) - 10, Math.max(...this.close) + 20],
                type: 'linear'
              }
            };

            Plotly.newPlot('CandleStick', data, layout);

          })
    },

    getStock(stock) {
      axios.get("https://financialmodelingprep.com/api/v3/ratios-ttm/" + stock + "?apikey=" + process.env.VUE_APP_FMP_API_KEY)
          // axios.get("https://financialmodelingprep.com/api/v3/ratios-ttm/AAPL?apikey=demo")
          .then(res => {
            let data = res.data[0]
            this.roa = parseFloat(data["returnOnAssetsTTM"]).toFixed(2)
            this.roe = parseFloat(data["returnOnEquityTTM"]).toFixed(2)
            this.gpm = parseFloat(data["grossProfitMarginTTM"]).toFixed(2)
            this.pbvr = parseFloat(data["priceBookValueRatioTTM"]).toFixed(2)
            this.psr = parseFloat(data["priceSalesRatioTTM"]).toFixed(2)
            this.per = parseFloat(data["priceEarningsRatioTTM"]).toFixed(2)
          })
          .catch(err => console.log(err))
    },

    getInfo(stock) {
      axios.get("https://financialmodelingprep.com/api/v3/profile/" + stock + "?apikey=" + process.env.VUE_APP_FMP_API_KEY)
          // axios.get("https://financialmodelingprep.com/api/v3/profile/AAPL?apikey=demo")
          .then(res => {
            this.infoCompany = res.data[0];
            if (this.infoCompany === undefined) {
              console.log("This is undefined")
            } else {
              this.beta = parseFloat(this.infoCompany.beta).toFixed(2)
              this.ceo = this.infoCompany.ceo
              this.sector = this.infoCompany.sector
              this.companyName = this.infoCompany.companyName
              this.marketCap = this.infoCompany.mktCap
              this.price = this.infoCompany.price
              this.imgURL = this.infoCompany.image
              this.description = this.infoCompany.description
            }
          })
          .catch(err => console.log(err))
    },

    search() {
      this.$router.push({path: 'search', query: {stock: this.stock}})
    }
  },

  mounted() {
    const stock = this.$route.query.stock ? this.$route.query.stock : "APPL"
    this.stock = stock
    this.update(stock)
  },

  watch: {
    '$route.query.stock'() {
      const stock = this.$route.query.stock ? this.$route.query.stock : "APPL"
      this.update(stock)
    }
  }

}
</script>

<style scoped lang="scss">
.more-margin {
  margin-top: 200px !important;
}

h3 {
  margin: 40px 0 0;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  display: inline-block;
  margin: 0 10px;
}

a {
  color: #42b983;
}

.card-background {
  background-color: rgb(28, 75, 169);
  color: white
}

md-list-item {
  max-width: 100%;
  display: inline-block;
  border: 1px solid rgba(0, 0, 0, .12);
}

.left {
  margin-left: 12px;
  margin-top: 0;
}

.right {
  margin-left: 12px;
  margin-top: 0;
}

.input1 {
  font-size: 20px;
  padding: 5px 0 5px 12px;
}

.material-icons {
  font-size: 20px;
}

.md-card {
  width: 320px;
  height: 420px;
}

.md-card-media-cover {
  width: 320px;
  height: 420px;
  display: inline-block;
  vertical-align: top;
}

.container-empty {
  display: flex;
  margin-top: 250px;
  margin-bottom: 50px;
  justify-content: space-between;
  position: relative;
}

.hello {
  height: 40vw !important;
}

.add-css {
  overflow-y: scroll;
  height: 450px;
}

/* width */
::-webkit-scrollbar {
  width: 10px;
}

/* Track */
::-webkit-scrollbar-track {
  background: rgba(241, 241, 241, 0);
}

/* Handle */
::-webkit-scrollbar-thumb {
  background: #888;
}

/* Handle on hover */
::-webkit-scrollbar-thumb:hover {
  background: #555;
}

html.md-theme-default {
  background-color: #b63c3c;
}
</style>
