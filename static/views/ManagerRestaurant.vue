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
        <router-link to="/manager/newarticle">
        <button class="btn btn-primary">Add a Product</button>
        </router-link>
      </div>




          <div v-else class="row row-cols-1 row-cols-3 g-2">
            <div v-for="article in articleList">
                <div v-if="article.deleted == true" class="card  text-white bg-danger">
                  <img  :src="'http://localhost:8080/image/' + article.logo" class="card-img-top h-100 w-100" alt="...">
                  <div class="card-body">
                    <h5 class="card-title">{{article.name}}</h5>
                    <p class="card-text">Price: {{article.price}} Type: {{article.articleType}} Quanityt: {{article.quantity }}
                      Description: {{article.description}}
                    </p>
                    <div class="btn-group" role="group">
                      <button @click="update(article.name)" class="btn btn-info">Update</button>
                      <button @click="deleteArticle(article.name)" class="btn btn-info">Delete</button>
                    </div>
                  </div>
                </div>
            <div v-if="article.articleType == 'Food' && article.deleted == false" class="card  text-white bg-primary">
                <img  :src="'http://localhost:8080/image/' + article.logo" class="card-img-top h-100 w-100" alt="...">
             <div class="card-body">
                <h5 class="card-title">{{article.name}}</h5>
                <p class="card-text">Price: {{article.price}} Type: {{article.articleType}} Quanityt: {{article.quantity }}
                  Description: {{article.description}}
                </p>
                <div class="btn-group" role="group">
                    <button @click="update(article.name)" class="btn btn-info">Update</button>
                    <button @click="deleteArticle(article.name)" class="btn btn-info">Delete</button>
                </div>
              </div>
              </div>
              <div v-if="article.articleType == 'Drink' && article.deleted == false" class="card  text-white bg-secondary">
                <img  :src="'http://localhost:8080/image/' + article.logo" class="card-img-top h-100 w-100" alt="...">
                <div class="card-body">
                  <h5 class="card-title">{{article.name}}</h5>
                  <p class="card-text">Price: {{article.price}} Type: {{article.articleType}} Quanityt: {{article.quantity }}
                    Description: {{article.description}}
                  </p>
                  <div class="btn-group" role="gorup">
                    <button @click="update(article.name)" class="btn btn-info">Update</button>
                    <div id="spacer"></div>
                    <button @click="deleteArticle(article.name)" class="btn btn-info">Delete</button>
                  </div>
                </div>
              </div>
              <div class="card">
                <router-link to="/manager/newarticle">
              <img class="card-img" src="images/add.png">
                </router-link>
              </div>
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

    noArticle: '',
    articleList: [],

  }
},
  methods:{

  update: function (articleName){
    if(!localStorage.jws){
      this.$router.push('/')
      return;
    }

  },
    deleteArticle: function(articleName){
      if(!localStorage.jws){
        this.$router.push('/')
        return;
      }
      console.log('Usao je u delete')
      axios.delete('/manager/restaurant/deletearticle/' + articleName, {headers:{'Authorization': 'Bearer' +localStorage.jws}})
      .then(() => {
        this.getRestaurant()
        this.noArticle = true;
      })
      .catch(r => console.log(r))
    },
  getRestaurant: function (){
    if(!localStorage.jws){
      this.$router.push('/')
      return;

    }
    axios.get('/manager/restaurant', {headers: {'Authorization': 'Bearer' + localStorage.jws}})
        .then(r => {this.restaurant = r.data
          if(this.restaurant.articleList.length == 0){
            console.log('nula je duzina')
            this.noArticle = true;
          }
          console.log(this.restaurant);
          this.articleList = this.restaurant.articleList;
        })
        .catch(r => console.log(r));

  },

  },
  mounted(){

  this.getRestaurant();
    }
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
#spacer{
  width: 50px;
}
</style>