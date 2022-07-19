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

    <div class="container-empty container">
      <div class="card-1 card">
        <div class="image">
          <img src="https://png.pngtree.com/thumb_back/fw800/back_our/20190620/ourmid/pngtree-black-atmosphere-investment-financial-poster-background-image_142222.jpg" alt="sasd"/>
          <h1>What is this?</h1>
        </div>
        <div class="details">
          <div class="center">
            <h1>Finance Dashboard<br><span>Your one-stop solution</span></h1>
            <p>Simple web app to view financial data of NASDAQ-listed companies before making your next calculated investment purchase</p>
          </div>
        </div>
      </div>
      <div class="card-2 card">
        <div class="image">
          <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQaVALeK2GTv1P4nPwibVehOXz9DymMFdKbCQ&usqp=CAU"/>
          <h1>Tech Stack</h1>
        </div>
        <div class="details">
          <div class="center">
            <h1>Technology Stack<br><span>Vue.js, Plotly.js</span></h1>
            <p>Vue.js for the frontend. Plotly.js for data visualisation. Axios to query data from Financial Modeling Prep's API</p>
          </div>
        </div>
      </div>
      <div class="card-3 card">
        <div class="image">
          <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/8/85/Cartoon_Call_Centre_Guy_Using_A_Computer.svg/432px-Cartoon_Call_Centre_Guy_Using_A_Computer.svg.png"/>
          <h1>How to use?</h1>
        </div>
        <div class="details">
          <div class="center">
            <h1>How to use?<br><span>Step-by-step guide:</span></h1>
            <p>Step 1: Navigate to the top of the screen and click on the link to view list of companies listed on NASDAQ</p>
            <p>Step 2: Find the company that you want details on and its symbol (e.g. Apple - APPL)</p>
            <p>Step 3: Click search and you're done! (Not much visualisations though...)</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Landing',
  data() {
    return {
      stock: ''
    }
  },
  methods: {
    search() {
      this.$router.push({ path: 'search', query: { stock: this.stock }})
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
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
  border: 1px solid rgba(0,0,0,.12);
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

.card-1 {
  left: 20%;
}

.card-2 {
  left: 50%;
}

.card-3 {
  left: 80%;
}

.card {
  position: absolute;
  transform: translate(-50%,-50%);
  width: 300px;
  height: 400px;
  background: #000;
}
.card .image {
  width: 100%;
  height: 100%;
  overflow: hidden;
}
.card .image img {
  width: 100%;
  height: 100%;
  transition: .5s;
}
.card:hover .image img {
  opacity: .5;
  transform: translateX(30%);/*100%*/
}
.card .details {
  position: absolute;
  top: 0;
  left: 0;
  width: 80%;
  height: 100%;
  background: #ffffff;
  transition: .5s;
  transform-origin: left;
  transform: perspective(2000px) rotateY(-90deg);
}
.card:hover .details {
  transform: perspective(2000px) rotateY(0deg);
}
.card .details .center {
  padding: 20px;
  text-align: center;
  background: #fff;
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
}
.card .details .center h1 {
  margin: 0;
  padding: 0;
  color: #3686ff;
  line-height: 20px;
  font-size: 17px;
  text-transform: uppercase;
}
.card .details .center h1 span {
  font-size: 10px;
  color: #262626;
}
.card .details .center p {
  margin: 10px 0;
  padding: 0;
  color: #262626;
}
.card .details .center ul {
  margin: 10px auto 0;
  padding: 0;
  display: table;
}
.card .details .center ul li {
  list-style: none;
  margin: 0 5px;
  float: left;
}
.card .details .center ul li a {
  display: block;
  background: #262626;
  color: #fff;
  width: 20px;
  height: 20px;
  line-height: 30px;
  text-align: center;
}
.card .details .center ul li a:hover {
  background: #ff3636;
}
.card .image h1 {
  color: white;
  position: absolute;
  bottom: 200px;
  left: 20px;
  font-size: 30px;
}
.card .image img {
  opacity: 0.5;
}
.card .image {
  padding: 10px;
}
.image:hover h1  {
  display: none
}
.image:hover h1  {
  display: none
}
</style>
