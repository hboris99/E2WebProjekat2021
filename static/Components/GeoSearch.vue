<template>
<div>
  <div id="container">
  <input type="text" class="form-control" @keyup="search" placeholder="Search location" v-model="query">
  <div v-if="results.length > 0"  id="results">
    <div v-for="r in results" :key="r.place_id" @click="select(r)" class="result">{{r.display_name}}</div>
  </div>
</div></div>
</template>

<script>
module.exports = {
  data: () => ({
    timeout: null,
    results: [],
    selected: null,
    query:'',
  }),
  methods: {
    select: function (r){
      this.selected = r;
      this.results = [];
      this.$emit('selected',r);
    },
    search: function (){
      clearTimeout(this.timeout);
      this.results=[];
      let self = this;
      this.timeout=setTimeout(function() {
        axios.get(' https://eu1.locationiq.com/v1/search.php?key=pk.3a0a65d7d139ba57d7f77aa5915e6724&format=json&q=' + self.query)
        .then(r => {self.results = r.data; console.log(r.data)})
        .catch(r => console.log(r))
      }, 1000)
    }
  }
}
</script>

<style scoped>
#container {
  position: relative;
  display: inline-block;
}
#results {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  z-index: 99;
  padding: 4px;
  background: #fff;
  border: solid 1px #eee;
  max-height: 400px;
}
.result {
    cursor: pointer;
    background: #fff;
  }
.result:hover {
  background: #eee;
}
</style>