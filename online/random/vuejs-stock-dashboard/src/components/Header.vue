<template>
  <div class="the-header">
    <div class="header">
      <router-link :to="{ path: '/'}">
        <img alt="Vue logo" src="https://www.nicepng.com/png/full/138-1385735_data-analytics-and-visualization-analysis-clipart.png">
      </router-link>
      <h4 class="subtitle">{{ org + " " + msg }}</h4>
      <p>Click <a href="https://www.advfn.com/nasdaq/nasdaq.asp" target="_blank">here</a> to see list of companies listed on {{org}}</p>
    </div>
  </div>
</template>

<script>
export default {
  name: "Header",
  props: {
    org: String,
    msg: String,
  },
  data() {
    return {
      stock: ""
    }
  },
}

</script>

<style scoped>

img {
  width: 300px;
  height: auto;
  padding-right: 50px;
}

.input1 {
  font-size: 20px;
  padding: 5px 0 5px 12px;
}

.header {
  display: flex;
  justify-content: center;
}

.subtitle {
  padding-top: 125px;
  font-size: 35px;
}

</style>
