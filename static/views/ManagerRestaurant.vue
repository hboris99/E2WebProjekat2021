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

      <div v-if="updateArticleForm">
        <div id="mainviewArticle">
          <div id="newArticleForm">
            <form class="row g-3 needs-validation" @submit="updateSelectedArticle">
              <div class="form-floating">
                <input class="form-control" id="articleNameForm"  v-model="articleToUpdate.name">
                <label for="articleNameForm">Article name</label>
              </div>
              <div class="form-floating ">
                <input class="form-control" id="articlePriceForm" v-model="articleToUpdate.price">
                <label for="articlePriceForm">Article price</label>
              </div>
              <select clas="form-select form-select-lg" v-model="articleToUpdate.articleType">
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
                <input class="form-control" id="articleDescForm" v-model="articleToUpdate.description">
                <label for="articleDescForm">Article description</label>
              </div>
              <div class="form-floating ">
                <input class="form-control" id="articleQuantityForm" v-model="articleToUpdate.quantity">
                <label for="articleQuantityForm">Article Quantity</label>
              </div>



              <input class="btn btn-primary" type="submit" value="Update Article">

            </form>
          </div>
        </div>
      </div>


          <div class="row row-cols-1 row-cols-3 g-2" v-if="!updateArticleForm && !noArticle">
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
    updateArticleForm: '',
    noArticle: '',
    articleList: [],
    articleToUpdate: null,
    fileUrl: '',
    file: '',

  }

},

  methods:{
    selectFile: function (){
      let file = this.$refs.file.files[0]
      this.fileUrl = URL.createObjectURL(file);
      this.file = file;
    },
    updateSelectedArticle:  function (){
      if(!localStorage.jws){
        this.$router.push('/')
        return;
      }
      let data = new FormData();
      let req = {
        name: this.articleToUpdate.name,
        price: this.articleToUpdate.price,
        articleType: this.articleToUpdate.articleType,
        quantity: this.articleToUpdate.quantity,
        description: this.articleToUpdate.description,
      }
      data.append('req', JSON.stringify(req));
      console.log(this.file)
      console.log(this.articleToUpdate.logo)

        data.append('file', this.file);
     let config = {
        headers:{
          'Content-Type': 'multipart/form-data',
          'Authorization': 'Bearer' + localStorage.jws,
        },
      };
      axios.post('/manager/restaurant/updatearticle ', data, config)
          .then(() => {
            alert('Article updated')
            this.getRestaurant();

            this.updateArticleForm = false;
          })
          .catch(r => console.log(r));
    },

  update: function (article){
    if(!localStorage.jws){
      this.$router.push('/')
      return;
    }
    axios.get('/manager/restaurant/getarticle/' + article,{headers:{'Authorization': 'Bearer' +localStorage.jws}} )
    .then(r => {
      this.articleToUpdate = r.data
      console.log(this.articleToUpdate);
      this.updateArticleForm = true;

    })
    .catch(r=> console.log(r));

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