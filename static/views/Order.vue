<template>
  <div>
  <custom-nav-bar>
  </custom-nav-bar>
    <div id="order-main">
      <div id="order-container">
        <div id="articles-select">
  <h2> Choose your article</h2>
          <div id="articles-container">

          <div v-for="article in articles" :key="article.name" class="article">
      <div :class="article.selected ? 'article-info selected' : 'article-info'">
        <div @click="article.selected = !article.selected" class="article-header">
          <h3>
            {{article.name}} ({{article.quantity}} <span v-if="article.articleType == 'Food'">g</span> <span v-else>ml</span>)
          </h3>
          <p>{{article.description}}</p>
        </div>
        <div class="price-and-amount">
          <b>
            {{article.price}} &#8364;
          </b>
          <div class="spacer"></div>
          <input
              v-if="article.selected"
              type="number"
              min="1"
              v-model="article.count"
              @blur="article.count = article.count < 1 ? 1 : article.count">
        </div>
      </div>
      <div class="spacer"></div>
      <img :src="'http://localhost:8080/image/' + article.logo" alt="article pic">
    </div>
  </div>
  <div>
    <p>The total price is: {{calculateWithDiscount(totalPrice).toFixed(2)}}</p>
    <span v-if="discount" class="discount">(-{{discount}} %)</span>
  </div>
    <div>
      <div class="spacer"></div>

      <button class="btn btn-primary" :disabled="totalPrice<=0" @click="addToCart">Add to cart</button>
    </div>
  </div>
    </div>
  </div>
  </div>
</template>

<script>
module.exports = {
 data: () => ({
   articles: [],
   errorList: [],
   discount: 0.0,
 }),

  methods:{
   getDiscount: function (){
     if(!localStorage.jws) {
       this.$router.push('/');
       return;
     }
     axios.get('/user/discount', {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
     .then(r=> this.discount = r.data)
     .catch(r=> console.log(r));
   },
   addToCart: function (){
     if(!localStorage.jws) {
       this.$router.push('/');
       return;
     }
     let data = [];
     this.selectedArticles.forEach(ar => {
       data.push({
         articleName: ar.name,
         ammount: ar.count,
       })
     })
     axios.post('/user/cart/' + this.$route.params.name, data, {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
         .then(() => this.$router.go(-1))
         .catch(r => console.log(r));

   },
   getArticles: function (){
     axios.get('/restaurant/' + this.$route.params.name + '/article')
     .then(r => {
       let articles = r.data
       articles.forEach(a => {
         a.count = 1;
         a.selected = false;
       })
       this.articles = articles;
     })
   },
    calculateWithDiscount: function(price) {
      return price * (100.0 - this.discount) / 100.0;
    },
  },computed:{
    selectedArticles: function (){
      let ar = []
      this.articles.forEach(article =>{
        if(article.selected)
          ar.push(article)
      })
      return ar;
    },
    totalPrice: function (){
      let price = 0.0
      this.articles.forEach(ar => {
        if(ar.selected)
          price += ar.price * ar.count
      })
    return price;
    }
  },
  mounted(){
   this.getArticles();
   this.getDiscount();
  }
}
</script>

<style scoped>
#order-main {
  display: grid;
  place-items: center;
  height: 87vh;
}
#order-container {
  display: flex;
  flex-direction: column;
  background: #fff;
  border: solid 1px #eee;
  padding: 10px;
}
#articles-select {
  display: flex;
  flex-direction: column;
  height: 60vh;
}
#articles-select h2 {
  color: #666;
  margin-bottom: 10px;
}
#articles-container {
  height: 400px;
  overflow: auto;
}
.article {
  display: flex;
  flex-direction: row;
  padding: 5px;
  /* width: 400px; */
  border-bottom: solid 1px #eee;
}
.article-header {
  cursor: pointer;
}
.selected {
  background: #eee;
}

.article img {
  width: 128px;
  height: 128px;
}
.article-info {
  width: 320px;
}
.article-info h3 {
  font-size: 2.5rem;
}
.price-and-amount {
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-items: center;
  padding: 10px;
}
.price-and-amount input {
  width: 80px;
}
#total-select {
  display: flex;
  flex-direction: row;
}
button:disabled {
  background: #eee;
}
#preview-container {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}
#order-info {
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  text-align: right;
}
#order-info div {
  display: flex;
  flex-direction: row;
}
.discount {
  color: #2ecc71;
}
</style>