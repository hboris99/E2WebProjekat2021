<template>
  <div>
  <custom-nav-bar></custom-nav-bar>

  <div class="container">
    <div v-if="!restaurant">
      <h3>You're currently not assigned to a restaurant</h3>
    </div>
    <div v-else>
        <img :src="'http://localhost:8080/image/' + restaurant.logo">
      <div>


      <h2>{{restaurant.name}}</h2>
       <b :class="restaurant.working == true ? 'open' : 'closed'">{{restaurant.working}}</b>
      </div>

      <div id="articleRestaurant" v-if="noArticle == true">
        <img id="noArticleImg"src="images/saucer.png">
        <h3> Looks like you haven't added any products to your restaurant. Do you want to add any?</h3>
        <button class="btn btn-primary" @click="openArticleForm">Add a Product</button>
      </div>


      <div id="newArticleForm" v-if="articleFormVisible == true">
        <form class="row g-3 needs-validation" @submit="newArticle">
        <div class="form-floating">
          <input class="form-control" id="articleNameForm" v-model="articleName">
          <label for="articleNameForm">Article name</label>
        </div>
        <div class="form-floating ">
          <input class="form-control" id="articlePriceForm" v-model="articlePrice">
          <label for="articlePriceForm">Article price</label>
        </div>
        <select clas="form-select form-select-lg" v-model="articleType">
          <option :value="null" disabled hidden>Select article type</option>
          <option value="Food">Food</option>
          <option value="Drink">Drink</option>
        </select>

          <div class="form-floating form-group">
            <input style="display: none" type="file" ref="file" @change="selectFile">
            <div v-if="!file" id="add-thumbnail" @click="$refs.file.click()">
              +
            </div>
            <img v-else :src="fileUrl" alt="rest img"/>

          </div>
          <div class="form-floating ">
            <input class="form-control" id="articleDescForm" v-model="articleDescription">
            <label for="articleDescForm">Article description</label>
          </div>
          <div class="form-floating ">
            <input class="form-control" id="articleQuantityForm" v-model="articleQuantity">
            <label for="articleQuantityForm">Article Quantity</label>
          </div>



        <input class="btn btn-primary" type="submit" value="Create new Article">

        </form>
      </div>
      <div v-if="articleCardsVisible">
        <div v-for="article in articleList">
          <h3>{{article.name}}</h3>
        </div>
      </div>

    </div>
  </div>
  </div>
</template>

<script>
module.exports = {
data: function(){
  return{
    restaurant: null,
    articleName: '',
    articlePrice: '',
    articleType: '',
    noArticle: '',
    articleFormVisible: '',
    articleCardsVisible: '',
    articleList: [],
    file: '',
    fileUrl: '',
    articleDescription: '',
    articleQuantity: '',
  }
},
  methods:{
    newArticle: function (){

      if(!localStorage.jws){
        this.$router.push('/');
      }
      let data = new FormData()
      let req = {
        restaurantID: this.restaurant.ID,
        name: this.articleName,
        price: this.price,
        articleType: this.articleType,
        quantity: this.articleQuantity,
        description: this.articleDescription,
      }
      data.append('req', JSON.stringify(req));
      data.append('file', this.file);
      let config = {
        headers:{
          'Content-Type': 'multipart/form-data',
          'Authorization': 'Bearer' + localStorage.jws,
        },
      };
      axios.post('/manager/restaurant/article ', data, config)
      .then(() => {

        this.articleFormVisible = false
        this.articleCardsVisible = true;
      })
      .catch(r => console.log(r));
    },
    selectFile: function (){
      let file = this.$refs.file.files[0]
      this.fileUrl = URL.createObjectURL(file);
      this.file = file;
    },
  getRestaurant: function (){
    if(!localStorage.jws){
      this.$router.push('/')
      return;

    }
    axios.get('/manager/restaurant', {headers: {'Authorization': 'Bearer' + localStorage.jws}})
        .then(r => {this.restaurant = r.data
          console.log(this.restaurant.articleList)
          if(this.restaurant.articleList.length == 0){
            this.noArticle = true;
          }else{
            this.noArticle = false;
          }
        })
        .catch(r => console.log(r));

  }, openArticleForm(){
        this.noArticle = false;
        this.articleFormVisible = true;
    }

  },
  mounted(){

  this.getRestaurant();
    if(this.restaurant.articleList.length == 0){
      this.noArticle = true;
      this.articleCardsVisible = false;
      this.articleFormVisible = false;
    }
    if(this.restaurant.articleList.length != 0){
      this.noArticle = false;
      this.articleCardsVisible = true;
    }
    this.articleList = this.restaurant.articleList;
  },
}
</script>

<style scoped>
#articleRestaurant{
  display: grid;
  place-items: center;
}
#noArticleImg{
 height: 250px;
  width: 250px;
  align-self: center;
}
</style>